/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.h2.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.model.WorkoutTraining;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.samples.petclinic.service.exceptions.ExistingWorkoutInDateRangeException;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.samples.petclinic.util.ProductPurchaseCollectionEditor;
import org.springframework.samples.petclinic.util.WorkoutTrainingCollectionEditor;
import org.springframework.samples.petclinic.util.WorkoutValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Slf4j
@Controller
public class WorkoutController {

	private static final String VIEWS_ASSIGN_WORKOUT = "workouts/assignWorkout";
	
	private static final String VIEWS_WORKOUT_DETAILS = "workouts/workoutDetails";
	
	private static final String VIEWS_FORBIDDEN_ACCESS = "workouts/forbidden";

	private final WorkoutService workoutService;

	private final UserService userService;

	@Autowired
	public WorkoutController(WorkoutService workoutService, UserService userService) {
		this.workoutService = workoutService;
		this.userService = userService;
	}

	/*@ModelAttribute("exercise")
	public Exercise findExercise(@PathVariable("exerciseId") int exerciseId) {
		return this.workoutService.findExerciseById(exerciseId);
	}*/

	/*
	 * @ModelAttribute("pet") public Pet findPet(@PathVariable("petId") Integer
	 * petId) { Pet result=null; if(petId!=null)
	 * result=this.clinicService.findPetById(petId); else result=new Pet(); return
	 * result; }
	 */

	/*
	 * @InitBinder("pet") public void initPetBinder(WebDataBinder dataBinder) {
	 * dataBinder.setValidator(new PetValidator()); }
	 */
	
	@InitBinder("workout")
	public void initWorkoutBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setValidator(new WorkoutValidator());
		dataBinder.registerCustomEditor(Set.class, "workoutTrainings", new WorkoutTrainingCollectionEditor(Set.class, workoutService));
	}


	@ModelAttribute("users")
	public Collection<User> populateUsers() {
		return (Collection<User>) this.userService.findAll(); 
	}


	@ModelAttribute("trainings")
	public Collection<Training> populateTrainings(@PathVariable(name="workoutId", required=false) Integer workoutId) {
		Workout workout = new Workout();
		workout.setId(workoutId);
		
		Collection<Training> trainings = this.workoutService.findTrainingsGenericOrByworkout(workout);
		return trainings.stream()
				.filter(training -> training.getExercisesSize() > 0)
				.collect(Collectors.toSet());
	}
	
	public void extendTrainingsList(Collection<Training> trainingsList, Workout workout) {
		trainingsList.addAll(workout.getWorkoutTrainings().stream()
				.map(WorkoutTraining::getTraining)
				.collect(Collectors.toSet())
		);
	}
	
	@GetMapping(value = "/workouts")
	public String processFindForm(User user, Workout workout, BindingResult result, Map<String, Object> model, Principal principal) {
		
		final boolean viewUserWorkoutsAllowed = isAllowedTo("view-users-workouts");
		
		model.put("assignWorkoutsAllowed", isAllowedTo("assign-workouts"));
		model.put("viewUsersWorkoutsAllowed", viewUserWorkoutsAllowed);
		
		if (user == null) {
			user = new User();
		}
		if (!viewUserWorkoutsAllowed || StringUtils.isNullOrEmpty(user.getUsername())) {
			user.setUsername(principal.getName());
		} else { // add user model for filtering purposes	
			model.put("user", user);
		}
		
		log.debug("getting workouts for user " + user.getUsername());
		
		// find owners by last name
		Collection<Workout> results = this.workoutService.findWorkoutsByUser(user.getUsername());
		
		final LocalDate today = LocalDate.now();
		
		Workout currentWorkout = results.stream().filter((wout) -> !today.isBefore(wout.getStartDate()) && !today.isAfter(wout.getEndDate())).findFirst().orElse(null);
		
		Collection<Workout> doneWorkouts = results.stream().filter((wout) -> today.isAfter(wout.getEndDate())).collect(Collectors.toSet());
		model.put("done", doneWorkouts);
		
		if (currentWorkout != null) {			
			Collection<Workout> nextWorkouts = results.stream().filter((wout) -> currentWorkout.getEndDate().isBefore(wout.getStartDate())).collect(Collectors.toSet());
			
			model.put("current", currentWorkout);
			model.put("next", nextWorkouts);			
		}
		
		log.info("workouts list [user=" + user.getUsername() + ", principal=" + principal.getName() + "]");
		
		return "workouts/workoutsList";
	}

	@GetMapping(value = "/workouts/assign")
	public String initCreationForm(Map<String, Object> model) {
		Workout workout = new Workout();
		model.put("workout", workout);
		return VIEWS_ASSIGN_WORKOUT;
	}

	@PostMapping(value = "/workouts/assign")
	public String processCreationForm(@Valid Workout workout, BindingResult result, ModelMap model, HttpServletRequest request) {
		Set<WorkoutTraining> sentWorkoutTrainings = populateWorkoutTrainings(request);
		if (!sentWorkoutTrainings.stream().anyMatch(wt -> wt.getTraining() != null)) {
			result.rejectValue("workoutTrainings", "notEmpty", "Debe seleccionar algún entrenamiento para la rutina");
		}
		
		if (result.hasErrors()) {
			model.put("workout", workout);
			return VIEWS_ASSIGN_WORKOUT;
		} else {
			
			Collection<Training> trainings = this.populateTrainings(null);
			Set<WorkoutTraining> workoutTrainings = workout.getWorkoutTrainings();
			for (int i = 1; i <= 6; i++) {
				final int weekday = i;
				WorkoutTraining workoutTraining = workoutTrainings.stream()
						.filter(wt -> wt.getWeekDay().equals(weekday))
						.findFirst().orElse(null);
				String tmpId = request.getParameter("wt-training-" + weekday);	
				Training training = null;
				if (tmpId != null && !tmpId.isEmpty()) {
					Integer trainingId = new Integer(tmpId);
					training = trainings.stream().filter(t -> t.getId().equals(trainingId)).findFirst().orElse(null);
				}
				
				if (workoutTraining != null && training == null) {
					workout.removeWorkoutTraining(workoutTraining);
				} else if (training != null) {
					if (workoutTraining == null) {
						workoutTraining = new WorkoutTraining();
						workoutTraining.setWeekDay(weekday);
					}
					Training trainingToAssign = new Training();
					BeanUtils.copyProperties(training, trainingToAssign, "id");
					trainingToAssign.setIsGeneric(Boolean.FALSE);

					training.getExercises().forEach(ex -> {
						Exercise exercise = new Exercise();
						BeanUtils.copyProperties(ex, exercise, "id");
						exercise.setIsGeneric(Boolean.FALSE);
						
						trainingToAssign.addExercise(exercise);
					});
					workoutTraining.setTraining(trainingToAssign);
					workout.addWorkoutTraining(workoutTraining);
				}
			}
			
			try {
				workout.getWorkoutTrainings().forEach(wt -> {
					Training training = wt.getTraining();
					
					training.getExercises().forEach(ex -> {
						this.workoutService.saveExercise(ex);
					});
					
					try {
						this.workoutService.saveTraining(training);
					} catch (NoNameException e) {
						e.printStackTrace();
					}
				});
				
				this.workoutService.saveWorkout(workout);
				
				log.info("workout with ID " + workout.getId() + " was assigned to user " + workout.getUser().getUsername());
			} catch (ExistingWorkoutInDateRangeException e) {
				log.error("error on workout assignment", e);
				
				model.put("workout", workout);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
				
				LocalDate wStartDate = workout.getStartDate();
				if (!e.getStartDate().isAfter(wStartDate) && !e.getEndDate().isBefore(wStartDate)) {
					result.rejectValue("startDate", "invalidDate", "Ya existe una rutina asignada para el periodo " + e.getStartDate().format(formatter) + " - " + e.getEndDate().format(formatter));					
				}
				LocalDate wEndDate = workout.getEndDate();
				if (!e.getStartDate().isAfter(wEndDate) && !e.getEndDate().isBefore(wEndDate)) {
					result.rejectValue("endDate", "invalidDate", "Ya existe una rutina asignada para el periodo " + e.getStartDate().format(formatter) + " - " + e.getEndDate().format(formatter));					
				}
				
                return VIEWS_ASSIGN_WORKOUT;
			}
			return "redirect:/workouts";
		}
	}
	

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/workouts/{workoutId}")
	public ModelAndView workoutDetails(@PathVariable("workoutId") int workoutId, Principal principal) {
		Workout workout = this.workoutService.findWorkoutById(workoutId);
		final boolean viewUserWorkoutsAllowed = isAllowedTo("view-users-workouts");
		
		ModelAndView mav;
		if (viewUserWorkoutsAllowed || workout.getUser().getUsername().equals(principal.getName())) {
			log.info("workout " + workoutId + " details access allowed for user " + principal.getName());
			mav = new ModelAndView(VIEWS_WORKOUT_DETAILS);
			mav.addObject(workout);
		} else {
			log.warn("workout " + workoutId + " details access has been denied for user " + principal.getName());
			mav = new ModelAndView(VIEWS_FORBIDDEN_ACCESS);
			mav.addObject("message", "No tiene permiso para visualizar el contenido");
		}
		
		mav.addObject("assignWorkoutAllowed", isAllowedTo("assign-workouts"));
		
		return mav;		
	}

	@GetMapping(value = "/workouts/{workoutId}/edit")
	public String initUpdateForm(@PathVariable("workoutId") int workoutId, ModelMap model, Principal principal) {
		Workout workout = this.workoutService.findWorkoutById(workoutId);
		model.put("workout", workout);
		// on edition view we must load generic trainings (to assign new) and currently
		// owned by user (to dont loose current trainings)
		extendTrainingsList((Set<Training>) model.get("trainings"), workout);
		return VIEWS_ASSIGN_WORKOUT;
	}

	@GetMapping(value = "/workouts/{workoutId}/delete")
	public String deleteWorkout(@PathVariable("workoutId") int workoutId, ModelMap model) {
		Workout workout = this.workoutService.findWorkoutById(workoutId);
		if (workout != null) {
			this.workoutService.deleteWorkout(workout);
		}
		return "redirect:/workouts";
	}

	@PostMapping(value = "/workouts/{workoutId}/edit")
	public String processUpdateForm(@Valid Workout workout, BindingResult result, @PathVariable("workoutId") int workoutId, ModelMap model, HttpServletRequest request, Principal principal) {
		if (workout.getWorkoutTrainings().isEmpty()) {
			result.rejectValue("workoutTrainings", "notEmpty", "Debe seleccionar algún entrenamiento para la rutina");
		}
		
		if (result.hasErrors()) {
			model.put("workout", workout);
			return VIEWS_ASSIGN_WORKOUT;
		} else {
			Workout workoutToUpdate = this.workoutService.findWorkoutById(workoutId);
			BeanUtils.copyProperties(workout, workoutToUpdate, "id", "workoutTrainings");
			
			updateWorkoutTrainingsList(workoutToUpdate, workout.getWorkoutTrainings());
			
			try {
				this.workoutService.saveWorkout(workoutToUpdate);
				
				log.info("workout with ID " + workoutToUpdate.getId() + " has been updated by user " + principal.getName());
			} catch (ExistingWorkoutInDateRangeException e) {
				log.error("error updating workout", e);
				model.put("workout", workout);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
				
				LocalDate wStartDate = workout.getStartDate();
				if (!e.getStartDate().isAfter(wStartDate) && !e.getEndDate().isBefore(wStartDate)) {
					result.rejectValue("startDate", "invalidDate", "Ya existe una rutina asignada para el periodo " + e.getStartDate().format(formatter) + " - " + e.getEndDate().format(formatter));					
				}
				LocalDate wEndDate = workout.getEndDate();
				if (!e.getStartDate().isAfter(wEndDate) && !e.getEndDate().isBefore(wEndDate)) {
					result.rejectValue("endDate", "invalidDate", "Ya existe una rutina asignada para el periodo " + e.getStartDate().format(formatter) + " - " + e.getEndDate().format(formatter));					
				}
				
                return VIEWS_ASSIGN_WORKOUT;
			}
			return "redirect:/workouts?username=" + workout.getUser().getUsername();
		}
	}
	
	
	private void updateWorkoutTrainingsList(Workout workout, Set<WorkoutTraining> newWorkoutTrainings) {
		IntStream.rangeClosed(1, 6).boxed()
			.map(weekDay -> new WorkoutTraining[] { 
				findByWeekday(workout.getWorkoutTrainings(), weekDay),
				findByWeekday(newWorkoutTrainings, weekDay),
			})
			.filter(workoutTrainings -> workoutTrainings[0] != null && workoutTrainings[1] != null)
			.forEach(workoutTrainings -> {
				WorkoutTraining oldWorkoutTraining = workoutTrainings[0];
				WorkoutTraining newWorkoutTraining = workoutTrainings[1];
				
				if (oldWorkoutTraining == null) {
					// new workoutTraining added
					workout.addWorkoutTraining(newWorkoutTraining);
				} else if (newWorkoutTraining != null) {
					// update oldWorkoutTraining with new bounds
					oldWorkoutTraining.setTraining(newWorkoutTraining.getTraining());
				} else {
					// remove oldTraining
					workout.removeWorkoutTraining(oldWorkoutTraining);
				} 
			});
	}
	
	private WorkoutTraining findByWeekday(Collection<WorkoutTraining> workoutTrainings, Integer weekDay) {
		return workoutTrainings.stream()
				.filter(wt -> wt != null && wt.getWeekDay().equals(weekDay))
				.findFirst().orElse(null);
	}
 	
	@SuppressWarnings("unchecked")
	private boolean isAllowedTo(String permission) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return SecurityConfiguration.isAllowedTo(permission, authorities);
	}

}
