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
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Memories;
import org.springframework.samples.petclinic.model.Memory;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.samples.petclinic.service.exceptions.MemoryOutOfTimeException;
import org.springframework.samples.petclinic.util.MemoryValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Slf4j
@Controller
public class MemoryController {

	private static final String VIEWS_CREATE_OR_UPDATE_MEMORY = "memories/createOrUpdateMemoryForm";
	
	private final WorkoutService workoutService;

	@Autowired
	public MemoryController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder, HttpServletRequest request) {
		dataBinder.setDisallowedFields("id");
		if (!request.getRequestURL().toString().endsWith("/delete")) {
			dataBinder.setValidator(new MemoryValidator());
		}
	}

	@ModelAttribute("memory")
	public Memory loadTrainingWithMemory() {
		Memory memory = new Memory();
		return memory;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/trainings/{trainingId}/memories/new")
	public String initNewMemoryForm(@PathVariable("trainingId") int trainingId, Map<String, Object> model) {
		return VIEWS_CREATE_OR_UPDATE_MEMORY;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/trainings/{trainingId}/memories/{memoryId}/delete")
	public String deleteMemory(@PathVariable("memoryId") int memoryId, Map<String, Object> model) {
		Memory memory = this.workoutService.findMemoryById(memoryId);
		if (memory != null) {
			this.workoutService.deleteMemory(memory);
		}
		return "redirect:/trainings/{trainingId}";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/trainings/{trainingId}/memories/new")
	public String processNewMemoryForm(@PathVariable("trainingId") int trainingId, @Valid Memory memory, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return VIEWS_CREATE_OR_UPDATE_MEMORY;
		}
		else {
			Training training = this.workoutService.findTrainingById(trainingId);
			memory.setTraining(training);
			try {
				this.workoutService.saveMemory(memory);
                log.info("added memory with ID=" + memory.getId() + " to training with ID=" + trainingId + " by " + principal.getName());
			} catch (MemoryOutOfTimeException e) {
				Workout workout = training.getWorkoutTraining().getWorkout();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
				result.rejectValue("date", "outOfTime", 
						"La fecha debe estar contenida dentro del periodo de la rutina (" 
						+ formatter.format(workout.getStartDate()) + " - " 
						+ formatter.format(workout.getEndDate()) + ")");
				return VIEWS_CREATE_OR_UPDATE_MEMORY;
			}
			return "redirect:/trainings/{trainingId}";
		}
	}

	@GetMapping(value = "/trainings/*/memories/{memoryId}/edit")
	public String editMemory(@PathVariable int memoryId, Map<String, Object> model) {
		model.put("memory", this.workoutService.findMemoryById(memoryId));
		return VIEWS_CREATE_OR_UPDATE_MEMORY;
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/trainings/{trainingId}/memories/{memoryId}/edit")
	public String processEditMemoryForm(@PathVariable("memoryId") int memoryId, @Valid Memory memory, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return VIEWS_CREATE_OR_UPDATE_MEMORY;
		}
		else {
			Memory memoryToUpdate = this.workoutService.findMemoryById(memoryId);
			BeanUtils.copyProperties(memory, memoryToUpdate, "id", "training");
			try {
				this.workoutService.saveMemory(memoryToUpdate);
                log.info("memory with ID=" + memoryId + " has been updated by " + principal.getName());
			} catch (MemoryOutOfTimeException e) {
				Workout workout = memoryToUpdate.getTraining().getWorkoutTraining().getWorkout();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");
				result.rejectValue("date", "outOfTime", 
						"La fecha debe estar contenida dentro del periodo de la rutina (" 
						+ formatter.format(workout.getStartDate()) + " - " 
						+ formatter.format(workout.getEndDate()) + ")");
				return VIEWS_CREATE_OR_UPDATE_MEMORY;
			}
			return "redirect:/trainings/{trainingId}";
		}
	}
	
	@GetMapping( 
		value = { "/memories"}, 
		consumes = { "application/json;charset=utf-8" }, 
		produces = { "application/json;charset=utf-8" }
	)
	public @ResponseBody Memories getMemoriesList(Principal principal) {
		Memories memories = new Memories();
		
		/*String name = null;
		if (exercise != null) { 
			name = exercise.getName();
		}
		
		exercises.getExerciseList().addAll(this.workoutService.findExercises(name));*/
		
		User user = new User();
		user.setUsername(principal.getName());
		Collection<Training> userTrainings = this.workoutService.findTrainingsByUser(user);
		
		memories.getMemoriesList().addAll(userTrainings.stream()
				.flatMap(t -> t.getMemories().stream())
				.peek(m -> m.setTraining(null)) // set training to null to avoid circular objects
				.sorted(Comparator.comparing(Memory::getDate))
				.collect(Collectors.toList())
		);
		
		return memories;
	}
	
	/* @SuppressWarnings("unchecked")
	private boolean isAllowedTo(String permission) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return SecurityConfiguration.isAllowedTo(permission, authorities);
	} */

}
