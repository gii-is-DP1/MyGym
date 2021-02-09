package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserTypeFormatterTests {

	@Mock
	private UserService userService;

	private UserTypeFormatter userTypeFormatter;

	@BeforeEach
	void setup() {
		userTypeFormatter = new UserTypeFormatter(userService);
	}

	@Test
	void testPrint() {
		UserType userType = new UserType();
		userType.setName("admin");
		String exerciseTypeName = userTypeFormatter.print(userType, Locale.ENGLISH);
		assertEquals("admin", exerciseTypeName);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(userService.findUserTypes()).thenReturn(makeUserTypes());
		UserType userType = userTypeFormatter.parse("client", Locale.ENGLISH);
		assertEquals("client", userType.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(userService.findUserTypes()).thenReturn(makeUserTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			userTypeFormatter.parse("anonymous", Locale.ENGLISH);
		});
	}

	/**
	 * Helper method to produce some sample pet types just for test purpose
	 * @return {@link Collection} of {@link ExerciseType}
	 */
	private Collection<UserType> makeUserTypes() {
		Collection<UserType> userTypes = new ArrayList<>();
		userTypes.add(new UserType() {
			{
				setName("admin");
			}
		});
		userTypes.add(new UserType() {
			{
				setName("client");
			}
		});
		return userTypes;
	}
}
