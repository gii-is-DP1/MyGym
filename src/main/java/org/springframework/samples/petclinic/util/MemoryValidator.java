package org.springframework.samples.petclinic.util;

import java.time.LocalDate;

import org.h2.util.StringUtils;
import org.springframework.samples.petclinic.model.Memory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MemoryValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Memory.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Memory memory = (Memory) target;
		
		String text = memory.getText();
		LocalDate date = memory.getDate();
		
		if (StringUtils.isNullOrEmpty(text)) {
			errors.rejectValue("text", "notEmpty", "No puede estar vacío");
		}
		
		if (date == null) {
			errors.rejectValue("date", "required");
		}
	}
}
