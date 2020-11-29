package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.model.Usuario;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/editarUsuario";
	
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
	
	@PostMapping(path = "/new")
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
	
	@GetMapping(value = "/{userId}/edit")
	public String initUpdateUsuarioForm(@PathVariable("userId") int userId, Model model) {
		Optional<Usuario> usuario = this.usuarioService.findUserById(userId);
		if (usuario.isPresent()) {
			model.addAttribute("user",usuario.get());			
		} else {
			model.addAttribute("message", "Usuario no encontrado");
		}
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
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
	
	@PostMapping(value = "/{userId}/edit")
	public String processUpdateOwnerForm(@Valid Usuario usuario, BindingResult result,
			@PathVariable("userId") int userId) {
		if (result.hasErrors()) {
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		}
		else {
			usuario.setId(userId);
			this.usuarioService.save(usuario);
			return "redirect:/usuarios/{userId}";
		}
	}

	@GetMapping("/{userId}")
	public String showUser(@PathVariable("userId") int userId, Model model) {
		Optional<Usuario> usuario = this.usuarioService.findUserById(userId);
		if (usuario.isPresent()) {
			model.addAttribute("user",usuario.get());			
		} else {
			model.addAttribute("message", "Usuario no encontrado");
		}
		return "usuarios/detalleUsuario";
	}
	
	@ModelAttribute("types")
	public Collection<UserType> userTypes() {
		return this.usuarioService.findUserTypes();
	}
	
	@ModelAttribute("rates")
	public Collection<Rate> rate() {
		return this.usuarioService.findRates();
	}

}
