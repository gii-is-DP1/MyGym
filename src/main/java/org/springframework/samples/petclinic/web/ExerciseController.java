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
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Exercises;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.samples.petclinic.util.ExerciseValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Slf4j
@Controller
public class ExerciseController {

	private static final String VIEWS_EXERCISES_CREATE_OR_UPDATE_FORM = "exercises/createOrUpdateExerciseForm";

	private final WorkoutService workoutService;

	@Autowired
	public ExerciseController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@InitBinder("exercise")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.addValidators(new ExerciseValidator());
	}


	@ModelAttribute("types")
	public Collection<ExerciseType> populatePetTypes() {
		return this.workoutService.findExerciseTypes();
	}
	
	@GetMapping( 
		value = { "/exercises"}, 
		consumes = { "application/json;charset=utf-8" }
	)
	public @ResponseBody Exercises getExercisesList(Exercise exercise) {
		Exercises exercises = new Exercises();
		
		String name = null;
		if (exercise != null) {
			name = exercise.getName();
		}
		
		exercises.getExerciseList().addAll(this.workoutService.findExercises(name));
		
		return exercises;
	}
	
	@GetMapping(value = "/exercises")
	public String processFindForm(Exercise exercise, BindingResult result, @RequestParam(name = "error", required = false) String error, Map<String, Object> model) {

		Collection<Exercise> results = this.workoutService.findExercises();
		model.put("selections", results);
		
		if (error != null && !error.isEmpty()) {
			model.put("error", error);
		}
		
		return "exercises/exercisesList";
	}

	@GetMapping(value = "/exercises/new")
	public String initCreationForm(Map<String, Object> model) {
		Exercise exercise = new Exercise();
		model.put("exercise", exercise);
		return VIEWS_EXERCISES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/exercises/new")
	public String processCreationForm(@Valid Exercise exercise, BindingResult result, ModelMap model, Principal principal) {
		if (result.hasErrors()) {
			model.put("exercise", exercise);
			return VIEWS_EXERCISES_CREATE_OR_UPDATE_FORM;
		} else {
			exercise.setIsGeneric(Boolean.TRUE);
			this.workoutService.saveExercise(exercise);
			log.info("exercise with ID=" + exercise.getId() + " has been created by " + principal.getName());
			return "redirect:/exercises";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/exercises/{exerciseId}")
	public ModelAndView showExercise(@PathVariable("exerciseId") int exerciseId) {
		ModelAndView mav = new ModelAndView("exercises/exerciseDetails");
		mav.addObject(this.workoutService.findExerciseById(exerciseId));
		return mav;
	}

	@GetMapping(value = "/exercises/{exerciseId}/edit")
	public String initUpdateForm(@PathVariable("exerciseId") int exerciseId, ModelMap model) {
		Exercise exercise = this.workoutService.findExerciseById(exerciseId);
		model.put("exercise", exercise);
		return VIEWS_EXERCISES_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/exercises/{exerciseId}/delete")
	public ModelAndView deleteExercise(@PathVariable("exerciseId") int exerciseId, ModelMap model, Principal principal) {
		ModelAndView mav = new ModelAndView("redirect:/exercises");
		Exercise exercise = this.workoutService.findExerciseById(exerciseId);
		if (exercise != null) {
			try {
				this.workoutService.deleteExercise(exercise);
			} catch (DataIntegrityViolationException e) {
				mav.addObject("error", "No se puede eliminar el ejercicio debido a que se encuentra vinculado a uno o más entrenamientos");
			}
			log.info("exercise with ID=" + exerciseId + " has been deleted by " + principal.getName());
		}
		return mav;
	}

	/**
	 *
	 * @param pet
	 * @param result
	 * @param petId
	 * @param model
	 * @param owner
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/exercises/{exerciseId}/edit")
	public String processUpdateForm(@Valid Exercise exercise, BindingResult result,
			@PathVariable("exerciseId") int exerciseId, ModelMap model, Principal principal) {
		if (result.hasErrors()) {
			model.put("exercise", exercise);
			return VIEWS_EXERCISES_CREATE_OR_UPDATE_FORM;
		} else {
			Exercise exerciseToUpdate = this.workoutService.findExerciseById(exerciseId);
			BeanUtils.copyProperties(exercise, exerciseToUpdate, "id", "isGeneric");
			this.workoutService.saveExercise(exerciseToUpdate);
			log.info("exercise with ID=" + exerciseId + " has been updated by " + principal.getName());
			return "redirect:/exercises";
		}
	}

}
