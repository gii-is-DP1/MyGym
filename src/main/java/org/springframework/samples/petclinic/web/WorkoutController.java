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
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.Usuario;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.model.Workouts;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.samples.petclinic.service.WorkoutService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import antlr.collections.List;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class WorkoutController {

	private static final String VIEWS_WORKOUTS_CREATE_OR_UPDATE_FORM = "workouts/createOrUpdateWorkoutForm";
	
	private static final String VIEWS_ASSIGN_WORKOUT = "workouts/assignWorkout";

	private final WorkoutService workoutService;

	private final UsuarioService userService;

	@Autowired
	public WorkoutController(WorkoutService workoutService, UsuarioService userService) {
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

	@InitBinder("user")
	public void initUserBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("workout")
	public void initWorkoutBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	@ModelAttribute("users")
	public Collection<Usuario> populateUsers() {
		return (Collection<Usuario>) this.userService.findAll(); 
	}


	@ModelAttribute("trainings")
	public Collection<Training> populateTrainings() {
		Collection<Training> trainings = this.workoutService.findTrainingsByName(null);
		return trainings.stream()
				.filter(training -> training.getExercisesSize() > 0)
				.collect(Collectors.toSet());
	}
	
	@GetMapping( 
		value = { "/workouts"}, 
		consumes = { "application/json;charset=utf-8" }
	)
	public @ResponseBody Workouts getWorkoutsList() {
		Workouts workouts = new Workouts();
		
		/*String name = null;
		if (exercise != null) { 
			name = exercise.getName();
		}
		
		exercises.getExerciseList().addAll(this.workoutService.findExercises(name));*/
		
		return workouts;
	}
	
	@GetMapping(value = "/workouts")
	public String processFindForm(Workout workout, BindingResult result, Map<String, Object> model, Principal principal) {
		
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		model.put("assignWorkoutsAllowed", SecurityConfiguration.isAllowedTo("assign-workouts", authorities));
		
		// find owners by last name
		Collection<Workout> results = this.workoutService.findWorkoutsByUser(principal.getName());
		
		final LocalDate today = LocalDate.now();
		
		Workout currentWorkout = results.stream().filter((wout) -> !today.isBefore(wout.getStartDate()) && !today.isAfter(wout.getEndDate())).findFirst().orElse(null);
		
		Collection<Workout> doneWorkouts = results.stream().filter((wout) -> today.isAfter(wout.getEndDate())).collect(Collectors.toSet());
		model.put("done", doneWorkouts);
		
		if (currentWorkout != null) {			
			Collection<Workout> nextWorkouts = results.stream().filter((wout) -> currentWorkout.getEndDate().isBefore(wout.getStartDate())).collect(Collectors.toSet());
			
			model.put("current", currentWorkout);
			model.put("next", nextWorkouts);			
		}
		
		return "workouts/workoutsList";
	}

	@GetMapping(value = "/workouts/assign")
	public String initCreationForm(Map<String, Object> model) {
		Workout workout = new Workout();
		model.put("workout", workout);
		model.put("workoutTrainings", workout.getWorkoutTrainings());
		return VIEWS_ASSIGN_WORKOUT;
	}

	@PostMapping(value = "/workouts/assign")
	public String processCreationForm(@Valid Workout workout, BindingResult result, ModelMap model) {
		System.out.println("ASSIGN POST");
		System.out.println("workout trainings = " + workout.getWorkoutTrainings());
		if (result.hasErrors()) {
			model.put("workout", workout);
			return VIEWS_ASSIGN_WORKOUT;
		} else {
			this.workoutService.saveWorkout(workout);
			return "redirect:/workouts";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/workouts/{workoutId}")
	public ModelAndView showWorkout(@PathVariable("workoutId") int workoutId) {
		ModelAndView mav = new ModelAndView("workouts/workoutDetails");
		mav.addObject(this.workoutService.findExerciseById(workoutId));
		return mav;
	}

	@GetMapping(value = "/workouts/{workoutId}/edit")
	public String initUpdateForm(@PathVariable("workoutId") int workoutId, ModelMap model) {
		Workout workout = this.workoutService.findWorkoutById(workoutId);
		model.put("workout", workout);
		return VIEWS_WORKOUTS_CREATE_OR_UPDATE_FORM;
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
	public String processUpdateForm(@Valid Workout workout, BindingResult result,
			@PathVariable("workoutId") int workoutId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("workout", workout);
			return VIEWS_WORKOUTS_CREATE_OR_UPDATE_FORM;
		} else {
			Workout workoutToUpdate = this.workoutService.findWorkoutById(workoutId);
			BeanUtils.copyProperties(workout, workoutToUpdate, "id");
			this.workoutService.saveWorkout(workoutToUpdate);
			return "redirect:/workouts";
		}
	}

}
