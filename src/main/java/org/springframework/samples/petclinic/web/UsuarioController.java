package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Usuario;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping()
	public String listadoUsuarios(ModelMap modelMap) {
		String vista = "usuarios/listadoUsuarios";
		Iterable<Usuario> users = usuarioService.findAll();
		modelMap.addAttribute("users", users);
		return vista;
	}
	
	@GetMapping("/new")
	public String crearUsuario(ModelMap modelMap) {
		String view = "usuarios/editarUsuario";
		modelMap.addAttribute("user", new Usuario());
		return view;
	}
	
	@PostMapping(path = "/save")
	public String guardarUsuario(@Valid Usuario user, BindingResult result, ModelMap modelMap) {
		String view = "usuarios/ListadoUsuarios";
		if(result.hasErrors()) {
			modelMap.addAttribute("user", user);
			return "usuarios/editarUsuario";
		}
		usuarioService.save(user);
		modelMap.addAttribute("message", "usuario guardado correctamente");
		view = listadoUsuarios(modelMap);
		return view;
		
	}
	
	@GetMapping(path = "/delete/{userId}")
	public String borrarUsuario(@PathVariable("userId") int userId, ModelMap modelMap) {
		String view = "usuarios/ListadoUsuarios";
		Optional<Usuario> user = usuarioService.findUserById(userId);
		if(user.isPresent()) {
			usuarioService.delete(user.get());
			modelMap.addAttribute("message", "Usuario eliminado correctamente");
			view = listadoUsuarios(modelMap);
		} else {
			modelMap.addAttribute("message", "Usuario no encontrado");
		}
		return view;
	}

}
