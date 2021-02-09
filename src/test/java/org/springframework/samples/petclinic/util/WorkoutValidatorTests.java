package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

@ExtendWith(MockitoExtension.class)
public class WorkoutValidatorTests {

	private WorkoutValidator validator;
	
	@BeforeEach
	void setup() {
		validator = new WorkoutValidator();
	}

	@Test
	void shouldValidate() {
		Workout workout = new Workout();
		workout.setName("Nombre de la rutina");
		workout.setStartDate(LocalDate.now());
		workout.setEndDate(LocalDate.now().plusDays(1));
        
        BindException errors = new BindException(workout, "workout");
        ValidationUtils.invokeValidator(validator, workout, errors);
        
        assertFalse(errors.hasErrors());
	}

	@Test
	void shouldRejectInvalidName() {
		Workout workout = new Workout();

		BindException errors = new BindException(workout, "workout");
        ValidationUtils.invokeValidator(validator, workout, errors);
        
        workout.setName("");
        ValidationUtils.invokeValidator(validator, workout, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("name"));
	}

	@Test
	void shouldRejectStartDateAfterEndDate() {
		Workout workout = new Workout();
		workout.setName("Nombre de la rutina");
		workout.setEndDate(LocalDate.now().minusDays(1));

        BindException errors = new BindException(workout, "workout");
        ValidationUtils.invokeValidator(validator, workout, errors);

		workout.setStartDate(LocalDate.now());
        ValidationUtils.invokeValidator(validator, workout, errors);
		
        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("startDate"));
	}

	@Test
	void shouldRejectEndDateBeforeToday() {
		Workout workout = new Workout();
		workout.setName("Nombre de la rutina");

        BindException errors = new BindException(workout, "workout");
        ValidationUtils.invokeValidator(validator, workout, errors);

		workout.setEndDate(LocalDate.now().minusDays(1));
        ValidationUtils.invokeValidator(validator, workout, errors);
		
        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("endDate"));
	}
}
