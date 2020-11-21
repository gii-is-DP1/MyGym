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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class ExerciseController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "exercise/createOrUpdateExerciseForm";

	private final WorkoutService workoutService;

	private final UsuarioService userService;

	@Autowired
	public ExerciseController(WorkoutService workoutService, UsuarioService userService) {
		this.workoutService = workoutService;
		this.userService = userService;
	}

	@ModelAttribute("types")
	public Collection<ExerciseType> populateExerciseTypes() {
		return this.workoutService.findExerciseTypes();
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

	@InitBinder("exercise")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/exercises")
	public String processFindForm(Exercise exercise, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /exercises to return all records
		/* if (exercise.getName() == null) {
			exercise.setName(""); // empty string signifies broadest possible search
		} */

		// find owners by last name
		Collection<Exercise> results = this.workoutService.findExercises();
		/* if (results.isEmpty()) {
			// no exercises found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}
		else */
		if (results.size() == 1) {
			// 1 owner found
			exercise = results.iterator().next();
			return "redirect:/exercises/" + exercise.getId();
		}
		else {
			// multiple owners found
			model.put("exercises", results);
			return "exercises/exercisesList";
		}
	}

	@GetMapping(value = "/exercises/new")
	public String initCreationForm(Exercise exercise, ModelMap model) {
		model.put("exercise", exercise);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/exercises/new")
	public String processCreationForm(@Valid Exercise exercise, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("exercise", exercise);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			this.workoutService.saveExercise(exercise);
			return "redirect:/exercises/{exerciseId}";
		}
	}

	@GetMapping(value = "/exercise/{exerciseId}/edit")
	public String initUpdateForm(@PathVariable("exerciseId") int exerciseId, ModelMap model) {
		Exercise exercise = this.workoutService.findExerciseById(exerciseId);
		model.put("exercise", exercise);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
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
			@PathVariable("exerciseId") int exerciseId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("exercise", exercise);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			Exercise exerciseToUpdate = this.workoutService.findExerciseById(exerciseId);
			BeanUtils.copyProperties(exercise, exerciseToUpdate, "id", "name", "description", "image", "numReps", "time", "type");
			this.workoutService.saveExercise(exerciseToUpdate);
			return "redirect:/owners/{ownerId}";
		}
	}

}
