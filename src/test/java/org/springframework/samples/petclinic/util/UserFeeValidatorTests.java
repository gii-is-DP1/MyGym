package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Fee;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserFeeValidatorTests {

	private UserFeeValidator validator;
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	private Fee fee;
	
	private Rate daily;
	
	@BeforeEach
	void setup() {
		validator = new UserFeeValidator(userService);
		
		user = new User();
		fee = new Fee();
		
		daily = new Rate();
		daily.setName("daily");
	}

	@Test
	void shouldNotValidateIfNullFee() {
        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        assertFalse(errors.hasErrors());
	}

	@Test
	void shouldValidate() {
		fee.setStart_date(LocalDate.now());
		fee.setEnd_date(LocalDate.now());
		fee.setRate(daily);
		fee.setAmount(3.0);
		
		user.setFee(fee);

        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        assertFalse(errors.hasErrors());
	}

	@Test
	void shouldRejectInvalidStartDate() {
		user.setFee(fee);

        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("fee.start_date"));
	}

	@Test
	void shouldRejectInvalidEndDate() {
		user.setFee(fee);

        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("fee.end_date"));
	}

	@Test
	void shouldRejectInvalidAmount() {
		user.setFee(fee);

        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);
        
        fee.setAmount(0.0);
        ValidationUtils.invokeValidator(validator, user, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("fee.amount"));
	}

	@Test
	void shouldRejectInvalidRate() {
		user.setFee(fee);

        BindException errors = new BindException(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);
        
        Rate rate = new Rate();
        rate.setName("gratuita");
        fee.setRate(rate);
        ValidationUtils.invokeValidator(validator, user, errors);

        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getFieldErrorCount("fee.rate"));
	}
}
