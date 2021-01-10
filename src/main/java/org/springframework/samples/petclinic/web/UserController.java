package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.StartDateAfterEndDateException;
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
public class UserController {
	
	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "users/editarUsuario";
	private static final String VIEWS_USUARIO_LIST = "users/listadoUsuarios";
	private static final String VIEWS_USUARIO_DETAIL = "users/detalleUsuario";
	
	@Autowired
	private UserService usuarioService;
	
	@GetMapping()
	public String listadoUsers(ModelMap modelMap) {
		Iterable<User> users = usuarioService.findAll();
		modelMap.addAttribute("users", users);
		return VIEWS_USUARIO_LIST;
	}
	
	@GetMapping("/new")
	public String crearUser(ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(path = "/new")
	public String guardarUser(@Valid User user, BindingResult result, ModelMap modelMap) {
		if(result.hasErrors()) {
			modelMap.addAttribute("user", user);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		}
		try {
			usuarioService.save(user);
		} catch (StartDateAfterEndDateException e) {
			result.rejectValue("fee.start_date", "invalidDate", "La fecha de inicio es posterior a la fecha final de la cuota");
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		}
		modelMap.addAttribute("message", "usuario guardado correctamente");
		return listadoUsers(modelMap);
	}
	
	@GetMapping(value = "/{userId}/edit")
	public String initUpdateUserForm(@PathVariable("userId") int userId, Model model) {
		Optional<User> usuario = this.usuarioService.findUserById(userId);
		if (usuario.isPresent()) {
			model.addAttribute("user",usuario.get());			
		} else {
			model.addAttribute("message", "User no encontrado");
		}
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}
	
	@GetMapping(path = "/delete/{userId}")
	public String borrarUser(@PathVariable("userId") int userId, ModelMap modelMap) {
		String view = VIEWS_USUARIO_LIST;
		Optional<User> user = usuarioService.findUserById(userId);
		if(user.isPresent()) {
			usuarioService.delete(user.get());
			modelMap.addAttribute("message", "User eliminado correctamente");
			view = listadoUsers(modelMap);
		} else {
			modelMap.addAttribute("message", "User no encontrado");
		}
		return view;
	}
	
	@PostMapping(value = "/{userId}/edit")
	public String processUpdateOwnerForm(@Valid User usuario, BindingResult result,
			@PathVariable("userId") int userId) {
		if (result.hasErrors()) {
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		}
		else {
			usuario.setId(userId);
			try {
				this.usuarioService.save(usuario);
			} catch (StartDateAfterEndDateException e) {
				result.rejectValue("fee.start_date", "invalidDate", "La fecha de inicio es posterior a la fecha final de la cuota");
				return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
			}
			return "redirect:/usuarios/{userId}";
		}
	}

	@GetMapping("/{userId}")
	public String showUser(@PathVariable("userId") int userId, Model model) {
		Optional<User> usuario = this.usuarioService.findUserById(userId);
		if (usuario.isPresent()) {
			model.addAttribute("user",usuario.get());			
		} else {
			model.addAttribute("message", "User no encontrado");
		}
		return VIEWS_USUARIO_DETAIL;
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
