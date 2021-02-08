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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = TrainingController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class TrainingControllerTests {

	private static final int TEST_TRAINING_ID = 1;
	private static final int TEST_EXERCISE_ID = 1;

	@Autowired
	private TrainingController trainingController;

	@MockBean
	private WorkoutService workoutService;

	@Autowired
	private MockMvc mockMvc;
	
	private Training training;
	
	private Exercise exercise;
	
	@BeforeEach
	void setup() {
		ExerciseType exerciseType = new ExerciseType();
		exerciseType.setId(1);
		exerciseType.setName("repetitive");
		
		exercise = new Exercise();
		exercise.setId(TEST_EXERCISE_ID);
		exercise.setName("Flexiones de triceps");
		exercise.setDescription("Flexiones con los codos pegados al cuerpo");
		exercise.setIsGeneric(Boolean.TRUE);
		exercise.setType(exerciseType);
		exercise.setNumReps(10);
		
		training = new Training();
		training.setId(TEST_TRAINING_ID);
		training.setName("Entrenamiento genérico");
		training.setDescription("Circuito de ejercicios");
		training.setIsGeneric(Boolean.TRUE);
		training.setExercises(Sets.newSet(exercise));
		
		given(this.workoutService.findTrainingById(TEST_TRAINING_ID)).willReturn(training);
	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewTrainingForm() throws Exception {
		mockMvc.perform(get("/trainings/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewTrainingFormSuccess() throws Exception {
		mockMvc.perform(
				post("/trainings/new")
					.with(csrf())
					.param("name", "Entrenamiento genérico")
					.param("description", "Circuito de ejercicios")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewTrainingFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/trainings/new")
					.with(csrf())
					.param("description", "Un producto para fitness")
				)
				.andExpect(model().attributeHasErrors("training"))
				.andExpect(model().attributeHasFieldErrors("training", "name"))
				.andExpect(status().isOk())
				.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowTrainings() throws Exception {
		given(this.workoutService.findTrainingsByName(null)).willReturn(Lists.newArrayList(new Training()));
		
		mockMvc.perform(get("/trainings"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("selections"))
			.andExpect(view().name("trainings/trainingsList"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowTrainingsByName() throws Exception {
		given(this.workoutService.findTrainingsByName("entrenamiento")).willReturn(Lists.newArrayList(new Training(), training));

		mockMvc.perform(get("/trainings")
					.param("name", "entrenamiento")
				)
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/trainingsList"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowUniqueTrainingByName() throws Exception {
		given(this.workoutService.findTrainingsByName("entrenamiento")).willReturn(Lists.newArrayList(training));

		mockMvc.perform(get("/trainings")
					.param("name", "entrenamiento")
				)
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/trainings/" + TEST_TRAINING_ID));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditTrainingForm() throws Exception {
		
		mockMvc.perform(get("/trainings/{id}/edit", TEST_TRAINING_ID))
			.andExpect(model().attributeExists("training"))
			.andExpect(model().attribute("training", hasProperty("name", is("Entrenamiento genérico"))))
			.andExpect(model().attribute("training", hasProperty("description", is("Circuito de ejercicios"))))
			.andExpect(model().attribute("training", hasProperty("exercises", is(training.getExercises()))))
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void tesNotAllowedTrainingDetailView() throws Exception {
		mockMvc.perform(get("/trainings/{id}", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("exception"))
			.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring", authorities = {"admin"})
    @Test
	void tesAllowedTrainingDetailView() throws Exception {
		mockMvc.perform(get("/trainings/{id}", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("training"))
			.andExpect(model().attribute("training", hasProperty("name", is("Entrenamiento genérico"))))
			.andExpect(model().attribute("training", hasProperty("description", is("Circuito de ejercicios"))))
			.andExpect(model().attribute("training", hasProperty("exercises", is(training.getExercises()))))
			.andExpect(view().name("trainings/trainingDetails"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void tesTrainingDetailError() throws Exception {
		given(this.workoutService.findTrainingById(5)).willReturn(null);
		
		mockMvc.perform(get("/trainings/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("training"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditTrainingNotFoundForm() throws Exception {
		given(this.workoutService.findTrainingById(5)).willReturn(null);
		
		mockMvc.perform(get("/trainings/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("training"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditTrainingFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/trainings/{id}/edit", TEST_TRAINING_ID)
					.with(csrf())
					.param("name", "Entrenamiento genérico")
					.param("description", "Circuito de ejercicios")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/" + TEST_TRAINING_ID));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditTrainingFormError() throws Exception {		
		mockMvc.perform(
				post("/trainings/{id}/edit", TEST_TRAINING_ID)
					.with(csrf())
				)
				.andExpect(model().attributeHasErrors("training"))
				.andExpect(model().attributeHasFieldErrors("training", "name"))
				.andExpect(status().isOk())
				.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}

}
