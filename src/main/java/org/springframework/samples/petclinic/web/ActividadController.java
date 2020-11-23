package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/actividades")
public class ActividadController {
	
	@Autowired
	private ActividadService actividadService;
	
	@GetMapping()
	public String listadoActividades(ModelMap modelMap) {
		String vista = "actividades/listadoActividades";
		Iterable<Actividad> actividades = actividadService.findAll();
		modelMap.addAttribute("actividades", actividades);
		return vista;
	}
	
	@GetMapping("/new")
	public String crearActividad(ModelMap modelMap) {
		String view = "actividades/editarActividad";
		modelMap.addAttribute("actividad", new Actividad());
		return view;
	}
	
	@PostMapping(path = "/save")
	public String guardarActividad(@Valid Actividad actividad, BindingResult result, ModelMap modelMap) {
		String view = "actividades/ListadoActividades";
		if(result.hasErrors()) {
			modelMap.addAttribute("actividad", actividad);
			return "actividades/editarActividad";
		}
		actividadService.save(actividad);
		modelMap.addAttribute("message", "actividad guardada correctamente");
		view = listadoActividades(modelMap);
		return view;
		
	}
	
	@GetMapping(path = "/delete/{actividadId}")
	public String borrarActividad(@PathVariable("actividadId") int actividadId, ModelMap modelMap) {
		String view = "actividades/listadoActividades";
		Optional<Actividad> actividad = actividadService.findActividadById(actividadId);
		if(actividad.isPresent()) {
			actividadService.delete(actividad.get());
			modelMap.addAttribute("message", "Actividad eliminada correctamente");
			view = listadoActividades(modelMap);
		} else {
			modelMap.addAttribute("message", "Actividad no encontrada");
		}
		return view;
	}

}
