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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Sala;
import org.springframework.samples.petclinic.service.SalaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class SalaController {

	private static final String VIEWS_SALAS_CREATE_OR_UPDATE_FORM = "salas/createOrUpdateSalaForm";

	private final SalaService salaService;

	@Autowired
	public SalaController(SalaService salaService) {
		this.salaService = salaService;
	}

	

	@InitBinder("sala")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	
	
	@GetMapping(value = "/salas")
	public String processFindForm(Sala sala, BindingResult result, Map<String, Object> model) {

		
		Iterable<Sala> results = this.salaService.findAll();
		
			model.put("salas", results);
			return "salas/salasList";
		
	}

	@GetMapping(value = "/salas/new")
	public String initCreationForm(Map<String, Object> model) {
		Sala sala = new Sala();
		model.put("sala", sala);
		return VIEWS_SALAS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/salas/new")
	public String processCreationForm(@Valid Sala sala, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("sala", sala);
			return VIEWS_SALAS_CREATE_OR_UPDATE_FORM;
		} else {
			this.salaService.save(sala);
			return "redirect:/salas";
		}
	}

	
	@GetMapping("/salas/{salaId}")
	public ModelAndView showSala(@PathVariable("salaId") int salaId) {
		ModelAndView mav = new ModelAndView("salas/salaDetails");
		mav.addObject(this.salaService.findSalaById(salaId));
		return mav;
	}

	@GetMapping(value = "/salas/{salaId}/edit")
	public String initUpdateForm(@PathVariable("salaId") int salaId, ModelMap model) {
		Sala sala = this.salaService.findSalaById(salaId);
		model.put("sala", sala);
		return VIEWS_SALAS_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/salas/{salaId}/delete")
	public String deleteSala(@PathVariable("salaId") int salaId, ModelMap model) {
		Sala sala = this.salaService.findSalaById(salaId);
		if (sala != null) {
			this.salaService.delete(sala);
		}
		return "redirect:/salas";
	}

	
	@PostMapping(value = "/salas/{salaId}/edit")
	public String processUpdateForm(@Valid Sala sala, BindingResult result,
			@PathVariable("salaId") int salaId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("sala", sala);
			return VIEWS_SALAS_CREATE_OR_UPDATE_FORM;
		} else {
			Sala salaToUpdate = this.salaService.findSalaById(salaId);
			BeanUtils.copyProperties(sala, salaToUpdate, "id");
			this.salaService.save(salaToUpdate);
			return "redirect:/salas";
		}
	}

}