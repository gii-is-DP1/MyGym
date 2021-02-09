package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Memory;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

@ExtendWith(MockitoExtension.class)
public class MemoryValidatorTests {

	private MemoryValidator validator;
	
	@BeforeEach
	void setup() {
		validator = new MemoryValidator();
	}

	@Test
	void shouldValidate() {
		Memory memory = new Memory();
		memory.setText("texto de la entrada de memoria");
		memory.setDate(LocalDate.now());
		memory.setWeight(20.0);
        
        BindException errors = new BindException(memory, "memory");
        ValidationUtils.invokeValidator(validator, memory, errors);
        
        assertFalse(errors.hasErrors());
	}

	@Test
	void shouldRejectInvaliText() {
		Memory memory = new Memory();

        BindException errors = new BindException(memory, "memory");
        ValidationUtils.invokeValidator(validator, memory, errors);
        
        memory.setText("");
        ValidationUtils.invokeValidator(validator, memory, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("text"));
	}

	@Test
	void shouldRejectInvalidDate() {
		Memory memory = new Memory();

        BindException errors = new BindException(memory, "memory");
        ValidationUtils.invokeValidator(validator, memory, errors);

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("date"));
	}

	@Test
	void shouldRejectInvalidWeight() {
		Memory memory = new Memory();

        BindException errors = new BindException(memory, "memory");
        ValidationUtils.invokeValidator(validator, memory, errors);
        
        memory.setWeight(0.0);
        ValidationUtils.invokeValidator(validator, memory, errors);
        
        memory.setWeight(-1.0);
        ValidationUtils.invokeValidator(validator, memory, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("weight"));
	}
}
