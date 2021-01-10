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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Memory;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.samples.petclinic.util.MemoryValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
public class MemoryController {

	private static final String VIEWS_ASSIGN_WORKOUT = "memories/createOrUpdateMemoryForm";
	
	private final WorkoutService workoutService;

	@Autowired
	public MemoryController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		// dataBinder.setValidator(new MemoryValidator());
	}


	@ModelAttribute("memory")
	public Memory loadTrainingWithMemory(@PathVariable("trainingId") int trainingId) {
		Training training = this.workoutService.findTrainingById(trainingId);
		Memory memory = new Memory();
		training.addMemory(memory);
		return memory;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/trainings/{trainingId}/memory/new")
	public String initNewMemoryForm(@PathVariable("trainingId") int trainingId, Map<String, Object> model) {
		return VIEWS_ASSIGN_WORKOUT;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/trainings/{trainingId}/memory/{memoryId}/delete")
	public String deleteMemory(Training training, @PathVariable("memoryId") int memoryId, Map<String, Object> model) {
		Memory memory = this.workoutService.findMemoryById(memoryId);
		System.out.println("Memory " + memory );
		if (memory != null) {
			training.removeMemory(memory);
			this.workoutService.deleteMemory(memory);
		}
		return "redirect:/trainings/{trainingId}";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/trainings/{trainingId}/memory/new")
	public String processNewMemoryForm(@Valid Memory memory, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_ASSIGN_WORKOUT;
		}
		else {
			this.workoutService.saveMemory(memory);
			return "redirect:/trainings/{trainingId}";
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean isAllowedTo(String permission) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return SecurityConfiguration.isAllowedTo(permission, authorities);
	}

}
