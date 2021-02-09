package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.WorkoutTraining;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WorkoutTrainingCollectionEditorTests {

	private WorkoutTrainingCollectionEditor collectionEditor;
	
	@Autowired
	private WorkoutService workoutService;
	
	@BeforeEach
	void setup() {
		collectionEditor = new WorkoutTrainingCollectionEditor(Set.class, workoutService);
	}
	
	@Test
	void shouldParseCollectionSuccess() {
		String[] source = new String[] {
			"1;",
			"2;1",
			"4;2"
		};	
		
		collectionEditor.setValue(source);
		
		Object value = collectionEditor.getValue();
		
		assertNotNull(value);
		assertTrue(value instanceof Set);
		
		Set<?> set = (Set<?>) value;
		
		assertEquals(3, set.size(), "There must be 3 elements in the converted collection");
		
		Iterator<?> itr = set.iterator();
		
		Object element = itr.next();
		
		assertNull(element, "Invalid element must be null");
		
		element = itr.next();
		
		assertNotNull(element);
		assertTrue(element instanceof WorkoutTraining);
		
		WorkoutTraining workoutTraining = (WorkoutTraining) element;
		
		assertNotNull(workoutTraining.getTraining(), "There must not have null training");
		assertNull(workoutTraining.getTraining().getId(), "There must not have generic training ID");
		assertEquals(workoutTraining.getWeekDay(), 2, "There must have weekday = 1");
		
		workoutTraining = (WorkoutTraining) itr.next();
		
		assertNotNull(workoutTraining.getTraining(), "There must not have null training");
		assertEquals(workoutTraining.getTraining().getId(), 2, "There must have generic training ID");
		assertEquals(workoutTraining.getWeekDay(), 4, "There must have weekday = 1");
		
	}
	
}
