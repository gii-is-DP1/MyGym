package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class UserControllerTests {
	
	private static final int TEST_USER_ID = 6;

	@Autowired
	private UserController userController;

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;
	
	private User user;
	
	@BeforeEach
	void setup() {

		user = new User();
		user.setId(TEST_USER_ID);
		user.setNombre("Borja");
		user.setApellidos("Vera Casal");
		user.setDni("11111111H");
		user.setEmail("borvercas@alum.us.es");
		given(this.userService.findUserById(TEST_USER_ID)).willReturn(Optional.of(user));

	}

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
	void testInitNewUserForm() throws Exception {
		mockMvc.perform(get("/usuarios/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("users/editarUsuario"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewUserFormSuccess() throws Exception {
		mockMvc.perform(
				post("/usuarios/new")
					.with(csrf())
					.param("nombre", "Juan")
					.param("apellidos", "Garcia Perez")
					.param("dni", "24242157C")
					.param("email", "juangarciaperez@gmail.com")
				)
                .andExpect(status().isOk())
				.andExpect(view().name("users/listadoUsuarios"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewUserFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/usuarios/new")
					.with(csrf())
					.param("apellidos", "Garcia Perez")
				)
				.andExpect(model().attributeHasErrors("user"))
				.andExpect(model().attributeHasFieldErrors("user", "nombre"))
				.andExpect(model().attributeHasFieldErrors("user", "dni"))
				.andExpect(status().isOk())
				.andExpect(view().name("users/editarUsuario"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowUsers() throws Exception {
		mockMvc.perform(get("/usuarios"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("users"))
			.andExpect(view().name("users/listadoUsuarios"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditUserForm() throws Exception {
		
		mockMvc.perform(get("/usuarios/{userId}/edit", TEST_USER_ID))
			.andExpect(model().attributeExists("user"))
			.andExpect(model().attribute("user", hasProperty("nombre", is("Borja"))))
			.andExpect(model().attribute("user", hasProperty("apellidos", is("Vera Casal"))))
			.andExpect(model().attribute("user", hasProperty("email", is("borvercas@alum.us.es"))))
			.andExpect(model().attribute("user", hasProperty("dni", is("11111111H"))))
			.andExpect(status().isOk())
			.andExpect(view().name("users/editarUsuario"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditUserNotFoundForm() throws Exception {
		given(this.userService.findUserById(900)).willReturn(Optional.empty());
		
		mockMvc.perform(get("/usuarios/{userId}/edit", 900))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("user"))
			.andExpect(view().name("users/editarUsuario"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditUserFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/usuarios/{userId}/edit", TEST_USER_ID)
					.with(csrf())
					.param("nombre", "Juan")
					.param("apellidos", "Garcia Perez")
					.param("dni", "24242157C")
					.param("email", "juangarciaperez@gmail.com")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/usuarios/{userId}"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditUserFormError() throws Exception {		
		mockMvc.perform(
				post("/usuarios/{userId}/edit", TEST_USER_ID)
					.with(csrf())
					.param("nombre", "")
				)
				.andExpect(model().attributeHasErrors("user"))
				.andExpect(model().attributeHasFieldErrors("user", "nombre"))
				.andExpect(status().isOk())
				.andExpect(view().name("users/editarUsuario"));
	}

}
