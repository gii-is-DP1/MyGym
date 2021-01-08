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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.repository.ExerciseRepository;
import org.springframework.samples.petclinic.repository.TrainingRepository;
import org.springframework.samples.petclinic.repository.WorkoutRepository;
import org.springframework.samples.petclinic.repository.WorkoutTrainingRepository;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class WorkoutService {

	private ExerciseRepository exerciseRepository;

	private TrainingRepository trainingRepository;
	
	private WorkoutTrainingRepository workoutTrainingRepository;
	
	private WorkoutRepository workoutRepository;
	
 
	@Autowired
	public WorkoutService(ExerciseRepository exerciseRepository, TrainingRepository trainingRepository,
			WorkoutTrainingRepository workoutTrainingRepository, WorkoutRepository workoutRepository) {
		this.exerciseRepository = exerciseRepository;
		this.trainingRepository = trainingRepository;
		this.workoutRepository = workoutRepository;
		this.workoutTrainingRepository = workoutTrainingRepository;
	}

	@Transactional(readOnly = true)
	public Collection<ExerciseType> findExerciseTypes() throws DataAccessException {
		return exerciseRepository.findExerciseTypes();
	}
	
	@Transactional
	public void saveExercise(Exercise exercise) throws DataAccessException {
		exerciseRepository.save(exercise);
	}
	
	@Transactional
	public void deleteExercise(Exercise exercise) throws DataAccessException {
		exerciseRepository.delete(exercise);
	}

	@Transactional(readOnly = true)
	public Exercise findExerciseById(int id) throws DataAccessException {
		return exerciseRepository.findById(id);
	}
	
	@Transactional(rollbackFor = NoNameException.class)
	public void saveTraining(Training training) throws DataAccessException, NoNameException {
		if (training.getName() == null || training.getName().trim().isEmpty()) {
			throw new NoNameException();
		}
		trainingRepository.save(training);
	}
	
	@Transactional
	public void deleteTraining(Training training) throws DataAccessException {
		trainingRepository.delete(training);
	}

	@Transactional(readOnly = true)
	public Training findTrainingById(int id) throws DataAccessException {
		return trainingRepository.findById(id);
	}
	
	public Collection<Training> findTrainingsByName(String name) {
		if (name == null)
			return trainingRepository.findAll();
		return trainingRepository.findByName(name);
	}

	/* @Transactional()
	public void savePet(Pet pet) throws DataAccessException, DuplicatedPetNameException {
			Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
            if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && otherPet.getId()!=pet.getId())) {            	
            	throw new DuplicatedPetNameException();
            }else
                petRepository.save(pet);                
	} */


	public Collection<Exercise> findExercises() {
		return exerciseRepository.findAll();
	}

	public Collection<Exercise> findExercises(String name) {
		if (name == null)
			return this.findExercises();
		return exerciseRepository.findByName(name);
	}
	
	public Collection<Workout> findWorkouts() {
		return workoutRepository.findAll();
	}
	
	public Collection<Workout> findWorkoutsByUser(User user) {
		return workoutRepository.findByUser(user.getUsername());
	}
	
	public Collection<Workout> findWorkoutsByUser(String username) {
		return workoutRepository.findByUser(username);
	}
	
	public void saveWorkout(Workout workout) {
		workoutRepository.save(workout);
	}
	
	public void deleteWorkout(Workout workout) {
		workoutRepository.delete(workout);
	}

	public Workout findWorkoutById(int workoutId) {
		return workoutRepository.findById(workoutId);
	}

}
