package org.springframework.samples.petclinic.util;

import org.h2.util.StringUtils;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExerciseValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Exercise.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Exercise exercise = (Exercise) target;
		
		String name = exercise.getName();
		ExerciseType type = exercise.getType();
		Integer numReps = exercise.getNumReps();
		Integer time = exercise.getTime();
		
		if (StringUtils.isNullOrEmpty(name)) {
			errors.rejectValue("name", "notEmpty", "No puede estar vacío");
		}
		
		if (type == null) {
			errors.rejectValue("type", "required");
		} else if (type.getName().equals("repetitive")) {
			if (numReps == null) {
				errors.rejectValue("numReps", "required", "El campo es obligatorio");
			} else if (numReps <= 0) {
				errors.rejectValue("numReps", "invalid", "El valor no puede ser menor o igual a cero");
			}
		} else if (type.getName().equals("temporary")) {
			if (time == null) {
				errors.rejectValue("time", "required", "El campo es obligatorio");
			} else if (time <= 0) {
				errors.rejectValue("time", "invalid", "El valor no puede ser menor o igual a cero");
			}
		}
		
	}
}
