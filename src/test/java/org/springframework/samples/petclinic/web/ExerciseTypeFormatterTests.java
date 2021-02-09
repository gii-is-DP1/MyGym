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
import org.springframework.samples.petclinic.service.WorkoutService;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */

@ExtendWith(MockitoExtension.class)
class ExerciseTypeFormatterTests {

	@Mock
	private WorkoutService workoutService;

	private ExerciseTypeFormatter exerciseTypeFormatter;

	@BeforeEach
	void setup() {
		exerciseTypeFormatter = new ExerciseTypeFormatter(workoutService);
	}

	@Test
	void testPrint() {
		ExerciseType exerciseType = new ExerciseType();
		exerciseType.setName("repetitive");
		String exerciseTypeName = exerciseTypeFormatter.print(exerciseType, Locale.ENGLISH);
		assertEquals("repetitive", exerciseTypeName);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(workoutService.findExerciseTypes()).thenReturn(makeExerciseTypes());
		ExerciseType exerciseType = exerciseTypeFormatter.parse("temporary", Locale.ENGLISH);
		assertEquals("temporary", exerciseType.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(workoutService.findExerciseTypes()).thenReturn(makeExerciseTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			exerciseTypeFormatter.parse("assertive", Locale.ENGLISH);
		});
	}

	/**
	 * Helper method to produce some sample pet types just for test purpose
	 * @return {@link Collection} of {@link ExerciseType}
	 */
	private Collection<ExerciseType> makeExerciseTypes() {
		Collection<ExerciseType> excerciseTypes = new ArrayList<>();
		excerciseTypes.add(new ExerciseType() {
			{
				setName("repetitive");
			}
		});
		excerciseTypes.add(new ExerciseType() {
			{
				setName("temporary");
			}
		});
		return excerciseTypes;
	}
	
}
