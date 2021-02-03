package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ExerciseController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class
)
public class ExerciseControllerTests {

	@SuppressWarnings("unused")
	@Autowired
	private ExerciseController exerciseController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	protected WorkoutService workoutService;
	
	
	private static final Integer TEST_EXERCISE_ID = 1; 
	private static final Integer TEST_EXERCISE_TYPE_ID = 1; 
	
	Exercise pushUps;
	
	@BeforeEach
	public void setup() {
		pushUps = new Exercise();
		pushUps.setId(TEST_EXERCISE_ID);
		pushUps.setIsGeneric(Boolean.TRUE);
		pushUps.setName("Push ups");
		pushUps.setDescription("Works chest with your own weight");
		
		ExerciseType repetitiveExerciseType = new ExerciseType();
		repetitiveExerciseType.setId(TEST_EXERCISE_TYPE_ID);
		repetitiveExerciseType.setName("repetitive");
		pushUps.setType(repetitiveExerciseType);
		
		pushUps.setNumReps(20);
		
		given(this.workoutService.findExerciseById(TEST_EXERCISE_ID)).willReturn(pushUps);
	}
	
	@WithMockUser(username="admin", authorities={"admin"})
	@Test
	public void testGetExercisesList() throws Exception {
		when(workoutService.findExercises()).thenReturn(new HashSet<Exercise>());
		
		mockMvc.perform(get("/exercises"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("exercises/exercisesList"))
			.andReturn().getModelAndView().getModel().containsKey("selections");
	}
	
	@WithMockUser(username="admin", authorities={"admin"})
	@Test
	public void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/exercises/new"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("exercises/createOrUpdateExerciseForm"))
			.andReturn().getModelAndView().getModel().containsKey("exercise");
	}

	@WithMockUser(username="admin", authorities={"admin"})
	@Test
	public void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/exercises/new")
							.with(csrf())
							.param("name", "Exercise sample")
							.param("description", "this is a random text as exercise description")
							.param("image", "base64imageData")
							.param("type", "repetitive")
							.param("numReps", "10")
							.param("time", "1 min"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(username="admin", authorities={"admin"})
	@Test
	public void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/exercises/new")
							.with(csrf())
							.param("description", "this is a random text as exercise description")
							.param("image", "base64imageData")
							.param("numReps", "10")
							.param("time", "1 min"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("exercise"))
				.andExpect(model().attributeHasFieldErrors("exercise", "name"))
				.andExpect(model().attributeHasFieldErrors("exercise", "type"))
				.andExpect(view().name("exercises/createOrUpdateExerciseForm"));
	}
	
}
