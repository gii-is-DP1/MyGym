/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class WorkoutServiceTests {        
    
	@Autowired
	protected WorkoutService workoutService;
    
    @Test
	void shouldFindExerciseWithCorrectId() {
    	Exercise exercise = this.workoutService.findExerciseById(1);
    	
    	assertThat(exercise.getName()).startsWith("Abdominales");
    	assertThat(exercise.getType().getName()).isEqualTo("repetitive");
	}
    
    @Test
	void shouldFindAllExerciseTypes() {
    	Collection<ExerciseType> exerciseTypes = this.workoutService.findExerciseTypes();
    	
    	ExerciseType type1 = EntityUtils.getById(exerciseTypes, ExerciseType.class, 1);
    	ExerciseType type2 = EntityUtils.getById(exerciseTypes, ExerciseType.class, 2);
    	
		assertThat(type1.getName()).isEqualTo("temporary");
		assertThat(type2.getName()).isEqualTo("repetitive");
	}
    
    @Test
	@Transactional
	public void shouldInsertExerciseIntoDatabaseAndGenerateId() {
    	Collection<ExerciseType> exerciseTypes = this.workoutService.findExerciseTypes();

		Exercise exercise = new Exercise();
		exercise.setName("Burpees round");
		exercise.setDescription("Ronda completa de burpees sin descanso intermedio");
		exercise.setType(EntityUtils.getById(exerciseTypes, ExerciseType.class, 2));
		exercise.setNumReps(20);
		
        try {
            this.workoutService.saveExercise(exercise);
        } catch (Exception ex) {
            Logger.getLogger(WorkoutServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }

		Collection<Exercise> searchResultCollection = this.workoutService.findExercises("Burpees round");
		assertThat(searchResultCollection.size()).isEqualTo(1);
		assertThat(searchResultCollection.iterator().next().getId()).isNotNull();
	}
    
    @Test
	void shouldFindTrainingWithCorrectId() {
    	Training training = this.workoutService.findTrainingById(1);
    	
    	assertThat(training.getName()).startsWith("Circuito");
	}
    
    @Test
	@Transactional
	public void shouldInsertTrainingIntoDatabaseAndGenerateId() {
		Exercise exercise = this.workoutService.findExerciseById(1);

		Training training = new Training();
		training.setName("Rutina de iniciación");
		training.setDescription("Circuito de iniciación para usuarios principiantes");
		
		training.addExercise(exercise);
		
		assertThat(training.getExercises().size()).isEqualTo(1);
		try {
			this.workoutService.saveTraining(training);
		} catch (NoNameException e) {
			e.printStackTrace();
		}


		training = this.workoutService.findTrainingById(training.getId());
		assertThat(training.getExercises().size()).isEqualTo(1);
		assertThat(training.getId()).isNotNull();
	}
    
    @Test
	@Transactional
	public void shouldFailInsertingTrainingWithoutName() {
		Exercise exercise = this.workoutService.findExerciseById(1);

		Training training = new Training();
		training.setDescription("Circuito de iniciación para usuarios principiantes");
		
		training.addExercise(exercise);
		
		assertThat(training.getExercises().size()).isEqualTo(1);

		Assertions.assertThrows(NoNameException.class, () -> {
			this.workoutService.saveTraining(training);
		});
	}
    	
    @Test
	@Transactional
	public void shouldFindTrainingsByName() {
		Exercise exercise = this.workoutService.findExerciseById(1);

		Training training = new Training();
		training.setName("Rutina de iniciación");
		training.setDescription("Circuito de iniciación para usuarios principiantes");
		training.addExercise(exercise);
		try {
			this.workoutService.saveTraining(training);
		} catch (NoNameException e) {
			e.printStackTrace();
		}

		Collection<Training> searchResultCollection = this.workoutService.findTrainingsByName("Rutina");
		assertThat(searchResultCollection.size()).isGreaterThan(0);

		searchResultCollection = this.workoutService.findTrainingsByName("anInventedWorkoutName");
		assertThat(searchResultCollection.size()).isEqualTo(0);
	}
	
    @Test
	@Transactional
	public void shouldFindWorkoutsByName() {
		// TODO 
	}
	
    @Test
	@Transactional
	public void shouldAssignWorkout() {
		// TODO 
	}
	
    @Test
	@Transactional
	public void shouldFailInsertingWorkoutIntoExistingRangeDate() {
		// TODO 
	}
	
    @Test
	@Transactional
	public void shouldCreateWorkoutWithTrainingCopy() {
		// TODO 
	}

}
