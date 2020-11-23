package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Sala;
import org.springframework.samples.petclinic.service.SalaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salas")
public class SalaController {
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping()
	public String listadoSalas(ModelMap modelMap) {
		String vista = "salas/listadoSalas";
		Iterable<Sala> salas = salaService.findAll();
		modelMap.addAttribute("salas", salas);
		return vista;
	}
	
	@GetMapping("/new")
	public String crearSala(ModelMap modelMap) {
		String view = "salas/editarSala";
		modelMap.addAttribute("sala", new Sala());
		return view;
	}
	
	@PostMapping(path = "/save")
	public String guardarSala(@Valid Sala sala, BindingResult result, ModelMap modelMap) {
		String view = "salas/ListadoSalas";
		if(result.hasErrors()) {
			modelMap.addAttribute("sala", sala);
			return "salas/editarSala";
		}
		salaService.save(sala);
		modelMap.addAttribute("message", "sala guardada correctamente");
		view = listadoSalas(modelMap);
		return view;
		
	}
	
	@GetMapping(path = "/delete/{salaId}")
	public String borrarSala(@PathVariable("SalaId") int salaId, ModelMap modelMap) {
		String view = "salas/ListadoSalas";
		Optional<Sala> sala = salaService.findSalaById(salaId);
		if(sala.isPresent()) {
			salaService.delete(sala.get());
			modelMap.addAttribute("message", "Sala eliminada correctamente");
			view = listadoSalas(modelMap);
		} else {
			modelMap.addAttribute("message", "Usuaria no encontrada");
		}
		return view;
	}

}
