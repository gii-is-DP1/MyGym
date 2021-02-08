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

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = ExerciseController.class,
			includeFilters = @ComponentScan.Filter(value = ExerciseTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class ExerciseControllerTests {

	private static final int TEST_EXERCISE_ID = 1;
	private static final int TEST_EXERCISE_TYPE_ID = 1;

	@Autowired
	private ExerciseController exerciseController;

	@MockBean
	private WorkoutService workoutService;

	@Autowired
	private MockMvc mockMvc;
	
	private Exercise exercise;
	
	private ExerciseType exerciseType;
	
	@BeforeEach
	void setup() {
		exerciseType = new ExerciseType();
		exerciseType.setId(TEST_EXERCISE_TYPE_ID);
		exerciseType.setName("repetitive");

		exercise = new Exercise();
		exercise.setId(TEST_EXERCISE_ID);
		exercise.setName("Ejercicio fitness");
		exercise.setDescription("Descripción del ejercicio fitness");
		exercise.setType(exerciseType);
		exercise.setNumReps(50);
		exercise.setIsGeneric(Boolean.TRUE);
		
		given(this.workoutService.findExerciseById(TEST_EXERCISE_ID)).willReturn(exercise);
		given(this.workoutService.findExerciseTypes()).willReturn(Sets.newLinkedHashSet(exerciseType));

	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewExerciseForm() throws Exception {
		mockMvc.perform(get("/exercises/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewExerciseFormSuccess() throws Exception {
		mockMvc.perform(
				post("/exercises/new")
					.with(csrf())
					.param("name", "Ejercicio fitness")
					.param("description", "Descripción del ejercicio fitness")
					.param("type", "repetitive")
					.param("numReps", "50")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/exercises"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewExerciseFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/exercises/new")
					.with(csrf())
					.param("description", "Un producto para fitness")
				)
				.andExpect(model().attributeHasErrors("exercise"))
				.andExpect(model().attributeHasFieldErrors("exercise", "name"))
				.andExpect(model().attributeHasFieldErrors("exercise", "type"))
				.andExpect(status().isOk())
				.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowExercises() throws Exception {
		mockMvc.perform(get("/exercises"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("selections"))
			.andExpect(view().name("exercises/exercisesList"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowExercisesByName() throws Exception {
		given(this.workoutService.findExercises("ejercicio")).willReturn(Lists.newArrayList(new Exercise()));

		mockMvc.perform(get("/exercises")
					.param("name", "ejercicio")
				)
			.andExpect(status().isOk())
			.andExpect(view().name("exercises/exercisesList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditExerciseForm() throws Exception {
		
		mockMvc.perform(get("/exercises/{id}/edit", TEST_EXERCISE_ID))
			.andExpect(model().attributeExists("exercise"))
			.andExpect(model().attribute("exercise", hasProperty("name", is("Ejercicio fitness"))))
			.andExpect(model().attribute("exercise", hasProperty("description", is("Descripción del ejercicio fitness"))))
			.andExpect(model().attribute("exercise", hasProperty("type", is(exerciseType))))
			.andExpect(model().attribute("exercise", hasProperty("numReps", is(50))))
			.andExpect(status().isOk())
			.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditExerciseNotFoundForm() throws Exception {
		given(this.workoutService.findExerciseById(5)).willReturn(null);
		
		mockMvc.perform(get("/exercises/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("exercise"))
			.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditExerciseFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/exercises/{id}/edit", TEST_EXERCISE_ID)
					.with(csrf())
					.param("name", "Ejercicio fitness 2")
					.param("description", "Descripción del ejercicio fitness 2")
					.param("type", "repetitive")
					.param("numReps", "25")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/exercises"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditExerciseFormError() throws Exception {		
		mockMvc.perform(
				post("/exercises/{id}/edit", TEST_EXERCISE_ID)
					.with(csrf())
					.param("name", "")
				)
				.andExpect(model().attributeHasErrors("exercise"))
				.andExpect(model().attributeHasFieldErrors("exercise", "name"))
				.andExpect(model().attributeHasFieldErrors("exercise", "type"))
				.andExpect(status().isOk())
				.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}

}
