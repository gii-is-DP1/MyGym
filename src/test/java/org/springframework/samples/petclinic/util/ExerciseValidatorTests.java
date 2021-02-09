package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

@ExtendWith(MockitoExtension.class)
public class ExerciseValidatorTests {

	private ExerciseValidator validator;
	
	private ExerciseType temporaryType;
	
	private ExerciseType repetitiveType;
	
	@BeforeEach
	void setup() {
		validator = new ExerciseValidator();
		
		temporaryType = new ExerciseType();
		temporaryType.setName("temporary");
        
        repetitiveType = new ExerciseType();
        repetitiveType.setName("repetitive");
	}

	@Test
	void shouldValidate() {
		Exercise exercise = new Exercise();
        exercise.setName("Nombre del ejercicio");
        exercise.setNumReps(15);
        exercise.setTime(6);
        exercise.setType(repetitiveType);
        
        BindException errors = new BindException(exercise, "exercise");
        ValidationUtils.invokeValidator(validator, exercise, errors);
        
        exercise.setType(temporaryType);
        ValidationUtils.invokeValidator(validator, exercise, errors);
        
        assertFalse(errors.hasErrors());
	}

	@Test
	void shouldRejectInvalidName() {
		Exercise exercise = new Exercise();

        BindException errors = new BindException(exercise, "exercise");
        ValidationUtils.invokeValidator(validator, exercise, errors);
        
        exercise.setName("");
        ValidationUtils.invokeValidator(validator, exercise, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("name"));
	}

	@Test
	void shouldRejectInvalidType() {
		Exercise exercise = new Exercise();
        exercise.setName("Nombre del ejercicio");

        BindException errors = new BindException(exercise, "exercise");
        ValidationUtils.invokeValidator(validator, exercise, errors);
		
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("type"));
	}

	@Test
	void shouldRejectInvalidNumRepsForRepetitiveType() {
		Exercise exercise = new Exercise();
        exercise.setName("Nombre del ejercicio");
        exercise.setType(repetitiveType);

        BindException errors = new BindException(exercise, "exercise");
        ValidationUtils.invokeValidator(validator, exercise, errors);
        
        exercise.setNumReps(-1);
        ValidationUtils.invokeValidator(validator, exercise, errors);
		
        exercise.setNumReps(0);
        ValidationUtils.invokeValidator(validator, exercise, errors);

        assertTrue(errors.hasErrors());
        assertEquals(3, errors.getFieldErrorCount("numReps"));
	}

	@Test
	void shouldRejectInvalidTimeForTemporaryType() {
		Exercise exercise = new Exercise();
        exercise.setName("Nombre del ejercicio");
        exercise.setType(temporaryType);

        BindException errors = new BindException(exercise, "exercise");
        ValidationUtils.invokeValidator(validator, exercise, errors);
        
        exercise.setTime(-1);
        ValidationUtils.invokeValidator(validator, exercise, errors);
		
        exercise.setTime(0);
        ValidationUtils.invokeValidator(validator, exercise, errors);

        assertTrue(errors.hasErrors());
        assertEquals(3, errors.getFieldErrorCount("time"));
	}
}
