package org.springframework.samples.petclinic.util;

import java.time.LocalDate;

import org.h2.util.StringUtils;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WorkoutValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Workout.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Workout workout = (Workout) target;
		
		String name = workout.getName();
		LocalDate startDate = workout.getStartDate();
		LocalDate endDate = workout.getEndDate();
		
		if (StringUtils.isNullOrEmpty(name)) {
			errors.rejectValue("name", "notEmpty", "No puede estar vacío");
		}
		
		if (startDate == null) {
			errors.rejectValue("startDate", "required");
		} else if (startDate.isAfter(endDate)) {
			errors.rejectValue("startDate", "invalidDate", "La fecha de inicio no puede ser posterior a la de fin");
		}
		
		if (endDate == null) {
			errors.rejectValue("endDate", "required");
		} else if (endDate.isBefore(LocalDate.now())) {
			errors.rejectValue("endDate", "invalidDate", "La fecha de fin no puede ser anterior a la fecha actual");
		}
		
	}

}
