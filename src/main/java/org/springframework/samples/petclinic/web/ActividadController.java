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
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Sala;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.SalaService;
import org.springframework.samples.petclinic.service.UsuarioService;
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

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class ActividadController {

	private static final String VIEWS_ACTIVIDADES_CREATE_OR_UPDATE_FORM = "actividades/createOrUpdateActividadForm";

	private final ActividadService actividadService;

	@Autowired
	public ActividadController(ActividadService actividadService) {
		this.actividadService = actividadService;
	}

	

	@InitBinder("actividad")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	
	
	@GetMapping(value = "/actividades")
	public String processFindForm(Actividad actividad, BindingResult result, Map<String, Object> model) {

		
		Iterable<Actividad> results = this.actividadService.findAll();
		
			model.put("actividades", results);
			return "actividades/actividadesList";
		
	}

	@GetMapping(value = "/actividades/new")
	public String initCreationForm(Map<String, Object> model) {
		Actividad actividad = new Actividad();
		model.put("actividad", actividad);
		return VIEWS_ACTIVIDADES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/actividades/new")
	public String processCreationForm(@Valid Actividad actividad, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return VIEWS_ACTIVIDADES_CREATE_OR_UPDATE_FORM;
		} else {
			this.actividadService.save(actividad);
			return "redirect:/actividades";
		}
	}

	
	@GetMapping("/actividades/{actividadId}")
	public ModelAndView showActividad(@PathVariable("actividadId") int actividadId) {
		ModelAndView mav = new ModelAndView("actividades/actividadDetails");
		mav.addObject(this.actividadService.findActividadById(actividadId));
		return mav;
	}

	@GetMapping(value = "/actividades/{actividadId}/edit")
	public String initUpdateForm(@PathVariable("actividadId") int actividadId, ModelMap model) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		model.put("actividad", actividad);
		return VIEWS_ACTIVIDADES_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/actividades/{actividadId}/delete")
	public String deleteSala(@PathVariable("actividadId") int actividadId, ModelMap model) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		if (actividad != null) {
			this.actividadService.delete(actividad);
		}
		return "redirect:/actividades";
	}

	
	@PostMapping(value = "/actividades/{actividadId}/edit")
	public String processUpdateForm(@Valid Actividad actividad, BindingResult result,
			@PathVariable("actividadId") int actividadId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return VIEWS_ACTIVIDADES_CREATE_OR_UPDATE_FORM;
		} else {
			Actividad actividadToUpdate = this.actividadService.findActividadById(actividadId);
			BeanUtils.copyProperties(actividad, actividadToUpdate, "id");
			this.actividadService.save(actividadToUpdate);
			return "redirect:/actividades";
		}
	}

}