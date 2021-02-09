package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.GenericIdToEntityConverter;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = WorkoutController.class,
			includeFilters = @ComponentScan.Filter(value = GenericIdToEntityConverter.class, type = FilterType.ASSIGNABLE_TYPE),
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class WorkoutControllerTests {

	private static final int TEST_WORKOUT_ID = 1;
	private static final int TEST_USER_ID = 1;

	@Autowired
	private WorkoutController workoutController;

	@MockBean
	private WorkoutService workoutService;

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;
	
	private Workout workout;

	private User user;
	
	@BeforeEach
	void setup() {
		workout = new Workout();
		workout.setId(TEST_WORKOUT_ID);
		workout.setStartDate(LocalDate.now());
		workout.setEndDate(LocalDate.now().plusDays(14));
		workout.setName("Rutina de testing");
		workout.setDescription("Descripción de la rutina de testing");
				
		user = new User();
		user.setId(TEST_USER_ID);
		user.setNombre("NombreUsuario");
		user.setApellidos("Apellido1 Apellido2");
		user.setUsername("usuarioprueba");
		user.setEnabled(Boolean.TRUE);
		
		workout.setUser(user);
		
		given(this.workoutService.findWorkoutById(TEST_WORKOUT_ID)).willReturn(workout);
	}
	
	@WithMockUser(value = "spring", authorities = { "admin" })
    @Test
	void testQueryCurrentUserWorkouts() throws Exception {
		given(this.workoutService.findWorkoutsByUser("spring")).willReturn(Lists.newArrayList(workout));

		mockMvc.perform(get("/workouts"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("current"))
			.andExpect(view().name("workouts/workoutsList"));
	}

	@WithMockUser(value = "spring", authorities = { "admin" })
    @Test
	void testQueryOtherUserWorkoutsAsAdmin() throws Exception {
		given(this.workoutService.findWorkoutsByUser("usuarioprueba")).willReturn(Lists.newArrayList(workout));
		
		mockMvc.perform(get("/workouts")
				.param("username", "usuarioprueba")
			)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("current"))
		.andExpect(view().name("workouts/workoutsList"));
		
	}

	@WithMockUser(value = "spring", authorities = { "trainer" })
    @Test
	void testQueryOtherUserWorkoutsAsTrainer() throws Exception {
		given(this.workoutService.findWorkoutsByUser("usuarioprueba")).willReturn(Lists.newArrayList(workout));
		
		mockMvc.perform(get("/workouts")
				.param("username", "usuarioprueba")
			)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("current"))
		.andExpect(view().name("workouts/workoutsList"));
		
	}

	@WithMockUser(value = "spring", authorities = { "client" })
    @Test
	void testQueryNotAllowedOtherUserWorkoutsAsClient() throws Exception {
		given(this.workoutService.findWorkoutsByUser("spring")).willReturn(Lists.newArrayList(workout));

		mockMvc.perform(get("/workouts")
				.param("username", "usuarioprueba") // the controller must ignore this param value
			)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("current"))
		.andExpect(view().name("workouts/workoutsList"));
	}

	@WithMockUser(value = "spring", authorities = { "client" })
    @Test
	void testWorkoutsListDistribution() throws Exception {
		Workout done = new Workout();
		done.setStartDate(LocalDate.now().minusDays(14));
		done.setEndDate(LocalDate.now().minusDays(1));
		
		Workout next = new Workout();
		next.setStartDate(LocalDate.now().plusDays(15));
		next.setEndDate(LocalDate.now().plusDays(30));
		
		given(this.workoutService.findWorkoutsByUser("spring")).willReturn(Lists.newArrayList(workout, done, next));

		mockMvc.perform(get("/workouts")
				.param("username", "usuarioprueba") // the controller must ignore this param value
			)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("current"))
		.andExpect(model().attribute("next", hasSize(1)))
		.andExpect(model().attribute("done", hasSize(1)))
		.andExpect(view().name("workouts/workoutsList"));
	}
	
	@WithMockUser(value = "spring", authorities = { "trainer" })
    @Test
	void testInitNewWorkoutForm() throws Exception {
		mockMvc.perform(get("/workouts/assign"))
			.andExpect(status().isOk())
			.andExpect(view().name("workouts/assignWorkout"));
	}
	
	@WithMockUser(value = "spring", authorities = { "trainer" })
    @Test
	void testNewWorkoutSuccessForm() throws Exception {		
		Training training = new Training();
		training.setId(1);
		training.setIsGeneric(Boolean.FALSE);
		given(this.workoutService.findTrainingById(1)).willReturn(training);
		
		mockMvc.perform(
				post("/workouts/assign")
					.with(csrf())
					.param("name", "Rutina de ejemplo")
					.param("user", String.valueOf(TEST_USER_ID))
					.param("startDate", "05/02/2021")
					.param("endDate", "15/02/2021")
					.param("workoutTrainings", "1;1;")
					.param("workoutTrainings", "2;;")
					.param("workoutTrainings", "3;;")
					.param("workoutTrainings", "4;;")
					.param("workoutTrainings", "5;;")
					.param("workoutTrainings", "6;;")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/workouts"));
	}
	
	@WithMockUser(value = "spring", authorities = { "trainer" })
    @Test
	void testNewWorkoutErrorForm() throws Exception {		
		Training training = new Training();
		training.setId(1);
		training.setIsGeneric(Boolean.FALSE);
		given(this.workoutService.findTrainingById(1)).willReturn(training);
		
		mockMvc.perform(
				post("/workouts/assign")
					.with(csrf())
					.param("description", "Descripción de la rutina")
			)
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("workout"))
			.andExpect(model().attributeHasFieldErrors("workout", "name"))
			.andExpect(model().attributeHasFieldErrors("workout", "startDate"))
			.andExpect(model().attributeHasFieldErrors("workout", "endDate"))
			.andExpect(model().attributeHasFieldErrors("workout", "workoutTrainings"))
			.andExpect(view().name("workouts/assignWorkout"));
	}
	
	@WithMockUser(value = "spring", authorities = { "admin" })
    @Test
	void testViewWorkoutDetailAsAdmin() throws Exception {

		mockMvc.perform(get("/workouts/{workoutId}", TEST_WORKOUT_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("workout"))
			.andExpect(view().name("workouts/workoutDetails"));
	}
	
	@WithMockUser(value = "spring", authorities = { "trainer" })
    @Test
	void testViewWorkoutDetailAsTrainer() throws Exception {

		mockMvc.perform(get("/workouts/{workoutId}", TEST_WORKOUT_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("workout"))
			.andExpect(view().name("workouts/workoutDetails"));
	}
	
	@WithMockUser(value = "spring", authorities = { "client" })
    @Test
	void testViewWorkoutDetailNotAllowed() throws Exception {

		mockMvc.perform(get("/workouts/{workoutId}", TEST_WORKOUT_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("message"))
			.andExpect(view().name("workouts/forbidden"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessEditWorkoutForm() throws Exception {		
		mockMvc.perform(get("/workouts/{id}/edit", TEST_WORKOUT_ID))
			.andExpect(model().attributeExists("workout"))
			.andExpect(model().attribute("workout", hasProperty("name", is("Rutina de testing"))))
			.andExpect(model().attribute("workout", hasProperty("description", is("Descripción de la rutina de testing"))))
			.andExpect(model().attribute("workout", hasProperty("startDate", is(workout.getStartDate()))))
			.andExpect(model().attribute("workout", hasProperty("endDate", is(workout.getEndDate()))))
			.andExpect(model().attribute("workout", hasProperty("user", is(user))))
            .andExpect(status().isOk())
			.andExpect(view().name("workouts/assignWorkout"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessEditWorkoutFormSuccess() throws Exception {		
		Training training = new Training();
		training.setId(1);
		training.setIsGeneric(Boolean.FALSE);
		given(this.workoutService.findTrainingById(1)).willReturn(training);
		
		mockMvc.perform(
				post("/workouts/{id}/edit", TEST_WORKOUT_ID)
					.with(csrf())
					.param("name", "Entrenamiento genérico")
					.param("description", "Circuito de ejercicios")
					.param("user", String.valueOf(TEST_USER_ID))
					.param("startDate", "05/02/2021")
					.param("endDate", "15/02/2021")
					.param("workoutTrainings", "1;1;")
					.param("workoutTrainings", "2;;")
					.param("workoutTrainings", "3;;")
					.param("workoutTrainings", "4;;")
					.param("workoutTrainings", "5;;")
					.param("workoutTrainings", "6;;")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/workouts/" + TEST_WORKOUT_ID));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditWorkoutFormError() throws Exception {		
		mockMvc.perform(
				post("/workouts/{id}/edit", TEST_WORKOUT_ID)
					.with(csrf())
					.param("name", "")
					.param("startDate", "")
					.param("endDate", "")
					.param("workoutTrainings", "1;;")
					.param("workoutTrainings", "2;;")
					.param("workoutTrainings", "3;;")
					.param("workoutTrainings", "4;;")
					.param("workoutTrainings", "5;;")
					.param("workoutTrainings", "6;;")
				)
				.andExpect(model().attributeHasErrors("workout"))
				.andExpect(model().attributeHasFieldErrors("workout", "startDate"))
				.andExpect(model().attributeHasFieldErrors("workout", "endDate"))
				.andExpect(model().attributeHasFieldErrors("workout", "workoutTrainings"))
				.andExpect(status().isOk())
				.andExpect(view().name("workouts/assignWorkout"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessDeleteWorkoutSuccess() throws Exception {		
		mockMvc.perform(
					get("/workouts/{id}/delete", TEST_WORKOUT_ID)
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/workouts"));
	}

}
