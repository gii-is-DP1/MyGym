package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.samples.petclinic.model.Fee;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserFeeValidator implements Validator {
	
	private UserService userService;
	
	public UserFeeValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if (user.getFee() == null) {
			return;
		}
		Fee fee = user.getFee();
		LocalDate startDate = fee.getStart_date();
		LocalDate endDate = fee.getEnd_date();
		Double amount = fee.getAmount();
		Rate rate = fee.getRate();
		
		
		if (startDate == null) {
			errors.rejectValue("fee.start_date", "notEmpty", "No puede estar vacío");
		}
		
		if (endDate == null) {
			errors.rejectValue("fee.end_date", "notEmpty", "No puede estar vacío");
		}
		
		if (amount == null || amount <= 0.0) {
			errors.rejectValue("fee.amount", "required");
		} 
		
		if (rate == null) {
			errors.rejectValue("fee.rate", "required");
		} else {
			Collection<Rate> rates = userService.findRates();
			if (!rates.isEmpty() && !rates.stream().anyMatch(r -> r.getName().equals(rate.getName()))) {
				errors.rejectValue("fee.rate", "invalid", "El tipo de cuota especificado no existe");
			}
		}
		
	}
}
