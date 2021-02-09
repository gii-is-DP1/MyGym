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

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Memory;
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
@WebMvcTest(controllers = MemoryController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class MemoryControllerTests {

	private static final int TEST_TRAINING_ID = 1;
	
	private static final int TEST_MEMORY_ID = 1;

	@Autowired
	private MemoryController memoryController;

	@MockBean
	private WorkoutService workoutService;

	@Autowired
	private MockMvc mockMvc;
	
	private Training training;
	
	private Memory memory;
	
	@BeforeEach
	void setup() {
		
		memory = new Memory();
		memory.setId(TEST_MEMORY_ID);
		memory.setDate(LocalDate.of(2021, 1, 10));
		memory.setText("texto de la memoria");
		memory.setWeight(80.0);
		
		training = new Training();
		training.setId(TEST_TRAINING_ID);
		training.setIsGeneric(Boolean.FALSE);
		training.setName("Entrenamiento de prueba");
		training.setMemories(Sets.newSet(memory));
		
		given(this.workoutService.findTrainingById(TEST_TRAINING_ID)).willReturn(training);
		given(this.workoutService.findMemoryById(TEST_MEMORY_ID)).willReturn(memory);
	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewMemoryForm() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/memories/new", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("memories/createOrUpdateMemoryForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewMemoryFormSuccess() throws Exception {
		mockMvc.perform(
				post("/trainings/{trainingId}/memories/new", TEST_TRAINING_ID)
					.with(csrf())
					.param("text", "Texto de la memoria")
					.param("date", "10/01/2021")
					.param("weight", "80.0")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/{trainingId}"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewMemoryFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/trainings/{trainingId}/memories/new", TEST_TRAINING_ID)
					.with(csrf())
					.param("weight", "80.0")
				)
				.andExpect(model().attributeHasErrors("memory"))
				.andExpect(model().attributeHasFieldErrors("memory", "text"))
				.andExpect(model().attributeHasFieldErrors("memory", "date"))
				.andExpect(status().isOk())
				.andExpect(view().name("memories/createOrUpdateMemoryForm"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
	void testEditMemoryForm() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/memories/{id}/edit", TEST_TRAINING_ID, TEST_MEMORY_ID))
			.andExpect(model().attributeExists("memory"))
			.andExpect(model().attribute("memory", hasProperty("text", is(memory.getText()))))
			.andExpect(model().attribute("memory", hasProperty("date", is(memory.getDate()))))
			.andExpect(model().attribute("memory", hasProperty("weight", is(memory.getWeight()))))
			.andExpect(status().isOk())
			.andExpect(view().name("memories/createOrUpdateMemoryForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditMemoryNotFoundForm() throws Exception {
		given(this.workoutService.findMemoryById(5)).willReturn(null);
		
		mockMvc.perform(get("/trainings/{trainingId}/memories/{id}/edit", TEST_TRAINING_ID, 50))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("memory"))
			.andExpect(view().name("memories/createOrUpdateMemoryForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditMemoryFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/trainings/{trainingId}/memories/{id}/edit", TEST_TRAINING_ID, TEST_MEMORY_ID)
					.with(csrf())
					.param("text", "Texto de la memoria")
					.param("date", "10/01/2021")
					.param("weight", "80.0")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/{trainingId}"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditMemoryFormError() throws Exception {		
		mockMvc.perform(
					post("/trainings/{trainingId}/memories/{id}/edit", TEST_TRAINING_ID, TEST_MEMORY_ID)
						.with(csrf())
						.param("weight", "80.0")
				)
				.andExpect(model().attributeHasErrors("memory"))
				.andExpect(model().attributeHasFieldErrors("memory", "text"))
				.andExpect(model().attributeHasFieldErrors("memory", "date"))
				.andExpect(status().isOk())
				.andExpect(view().name("memories/createOrUpdateMemoryForm"));
	}

}
