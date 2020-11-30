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
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class TrainingController {

	private static final String VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM = "trainings/createOrUpdateTrainingForm";

	private static final String VIEWS_TRAININGS_DETAIL = "trainings/trainingDetails";

	private static final String VIEWS_TRAININGS_LIST = "trainings/trainingsList";

	private final WorkoutService workoutService;

	private final UsuarioService userService;

	@Autowired
	public TrainingController(WorkoutService workoutService, UsuarioService userService) {
		this.workoutService = workoutService;
		this.userService = userService;
	}
	
	/* @ModelAttribute("training")
	public Training findTraining(@PathVariable("trainingId") Integer trainingId) {
		Training result = null; 
		if (trainingId != null) {
			result = this.workoutService.findTrainingById(trainingId);
		} else {
			result = new Training();
		}
		return result;
	}*/

	/* @InitBinder("training")
	public void initTrainingBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	} */
	
	/* @GetMapping(value = "/trainings/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("training", new Training());
		return "trainings/trainingsList";
	} */

	@GetMapping(value = "/trainings")
	public String processFindForm(Training training, BindingResult result, Map<String, Object> model) {
		
		String name = training != null && training.getName() != null ? training.getName() : "";
		System.out.println("name = " + name);
		
		// find owners by last name
		Collection<Training> results = this.workoutService.findTrainingsByName(name);
		System.out.println("results size: " + results.size());
		if (results.isEmpty()) {
			// no exercises found
			result.rejectValue("name", "notFound", "not found");
			return VIEWS_TRAININGS_LIST;
		}
		else if (!name.isEmpty() && results.size() == 1) {
			// 1 owner found
			Training uniqueFound = results.iterator().next();
			return "redirect:/trainings/" + uniqueFound.getId();
		}
		else {
			// multiple owners found
			model.put("selections", results);			
			return VIEWS_TRAININGS_LIST;
		}
	}

	@GetMapping(value = "/trainings/new")
	public String initCreationForm(Map<String, Object> model) {
		Training training = new Training();
		model.put("training", training);
		return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/trainings/new")
	public String processCreationForm(@Valid Training training, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("training", training);
			return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
		} else {
			try {
				this.workoutService.saveTraining(training);
			} catch (NoNameException e) {
                result.rejectValue("name", "required", "required field");
                return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/trainings";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/trainings/{trainingId}")
	public ModelAndView showTraining(@PathVariable("trainingId") int trainingId) {
		ModelAndView mav = new ModelAndView(VIEWS_TRAININGS_DETAIL);
		mav.addObject(this.workoutService.findTrainingById(trainingId));
		return mav;
	}

	@GetMapping(value = "/trainings/{trainingId}/edit")
	public String initUpdateForm(@PathVariable("trainingId") int trainingId, ModelMap model) {
		Training training = this.workoutService.findTrainingById(trainingId);
		model.put("training", training);
		return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/trainings/{trainingId}/delete")
	public String deleteTraining(@PathVariable("trainingId") int trainingId, ModelMap model) {
		Training training = this.workoutService.findTrainingById(trainingId);
		if (training != null) {
			this.workoutService.deleteTraining(training);
		}
		return "redirect:/trainings";
	}

	/**
	 * 
	 * @param training
	 * @param result
	 * @param trainingId
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/trainings/{trainingId}/edit")
	public String processUpdateForm(@Valid Training training, BindingResult result, @PathVariable("trainingId") int trainingId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("training", training);
			return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
		} else {
			Training trainingToUpdate = this.workoutService.findTrainingById(trainingId);
			BeanUtils.copyProperties(training, trainingToUpdate, "id");
			try {
				this.workoutService.saveTraining(trainingToUpdate);
			} catch (NoNameException e) {
                result.rejectValue("name", "required", "required field");
                return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/trainings/" + trainingId;
		}
	}

	/**
	 * 
	 * @param training
	 * @param result
	 * @param trainingId
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/trainings/{trainingId}/addExercise/{exerciseId}")
	public String processTrainingExerciseAddition(@Valid Training training, BindingResult result, @PathVariable("trainingId") int trainingId, @PathVariable("exerciseId") int exerciseId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("training", training);
			return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
		} else {
			Training trainingToUpdate = this.workoutService.findTrainingById(trainingId);
			Exercise exercise = this.workoutService.findExerciseById(exerciseId);
			if (exercise != null) {
				trainingToUpdate.addExercise(exercise);
				try {
					this.workoutService.saveTraining(trainingToUpdate);
				} catch (NoNameException e) {
	                result.rejectValue("name", "required", "required field");
	                return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
				}
			}
			return "redirect:/trainings/" + trainingId + "/edit";
		}
	}
	
	/**
	 * 
	 * @param training
	 * @param result
	 * @param trainingId
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/trainings/{trainingId}/deleteExercise/{exerciseId}")
	public String processTrainingExerciseRemoval(@Valid Training training, BindingResult result, @PathVariable("trainingId") int trainingId, @PathVariable("exerciseId") int exerciseId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("training", training);
			return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
		} else {
			Training trainingToUpdate = this.workoutService.findTrainingById(trainingId);
			Exercise exercise = this.workoutService.findExerciseById(exerciseId);
			if (exercise != null) {
				trainingToUpdate.removeExercise(exercise);
				try {
					this.workoutService.saveTraining(trainingToUpdate);
				} catch (NoNameException e) {
	                result.rejectValue("name", "required", "required field");
	                return VIEWS_TRAININGS_CREATE_OR_UPDATE_FORM;
				}
			}
			return "redirect:/trainings/" + trainingId + "/edit";
		}
	}

}
