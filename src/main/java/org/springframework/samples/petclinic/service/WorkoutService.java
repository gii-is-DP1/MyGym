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
import org.springframework.samples.petclinic.model.Memory;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.model.WorkoutTraining;
import org.springframework.samples.petclinic.repository.ExerciseRepository;
import org.springframework.samples.petclinic.repository.MemoryRepository;
import org.springframework.samples.petclinic.repository.TrainingRepository;
import org.springframework.samples.petclinic.repository.WorkoutRepository;
import org.springframework.samples.petclinic.repository.WorkoutTrainingRepository;
import org.springframework.samples.petclinic.service.exceptions.ExistingWorkoutInDateRangeException;
import org.springframework.samples.petclinic.service.exceptions.MemoryOutOfTimeException;
import org.springframework.samples.petclinic.service.exceptions.NoNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Slf4j
@Service
public class WorkoutService {

	private ExerciseRepository exerciseRepository;

	private TrainingRepository trainingRepository;
	
	private WorkoutTrainingRepository workoutTrainingRepository;
	
	private WorkoutRepository workoutRepository;
	
	private MemoryRepository memoryRepository;
	
 
	@Autowired
	public WorkoutService(ExerciseRepository exerciseRepository, TrainingRepository trainingRepository,
			WorkoutTrainingRepository workoutTrainingRepository, WorkoutRepository workoutRepository,
			MemoryRepository memoryRepository) {
		this.exerciseRepository = exerciseRepository;
		this.trainingRepository = trainingRepository;
		this.workoutRepository = workoutRepository;
		this.workoutTrainingRepository = workoutTrainingRepository;
		this.memoryRepository = memoryRepository;
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
		log.info("saved training with ID = " + training.getId());
	}
	
	@Transactional
	public void deleteTraining(Training training) throws DataAccessException {
		Collection<WorkoutTraining> workoutTrainings = workoutTrainingRepository.findByTraining(training);
		workoutTrainings.forEach(wt -> {
			System.out.println("deleting workout training " + wt);
			workoutTrainingRepository.delete(wt);
		});
		
		training.getMemories().forEach(m -> memoryRepository.delete(m));
		trainingRepository.delete(training);
	}

	@Transactional(readOnly = true)
	public Training findTrainingById(int id) throws DataAccessException {
		return trainingRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Training> findTrainingsByUser(User user) throws DataAccessException {
		log.info("getting trainings for user " + user.getUsername());
		return trainingRepository.findByUsername(user.getUsername());
	}
	
	public Collection<Training> findTrainingsGenericOrByworkout(Workout workout) throws DataAccessException {
		if (workout == null || workout.getId() == null) {
			return trainingRepository.findByIsGenericTrue();
		}
		return trainingRepository.findGenericOrByWorkout(workout.getId());
	}
	
	public Collection<Training> findTrainingsByName(String name) {
		if (name == null)
			return trainingRepository.findByIsGenericTrue();
		return trainingRepository.findByName(name);
	}

	public Collection<Exercise> findExercises() {
		return exerciseRepository.findByIsGenericTrue();
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

	@Transactional(rollbackFor = ExistingWorkoutInDateRangeException.class)
	public void saveWorkout(Workout workout) throws DataAccessException, ExistingWorkoutInDateRangeException {
		
		Collection<Workout> existingWorkoutsInDateRange = workoutRepository.findActiveWorkoutsForUser(workout.getUser(), workout.getStartDate(), workout.getEndDate());
		if (!existingWorkoutsInDateRange.isEmpty()) {
			Workout existingWorkout = existingWorkoutsInDateRange.iterator().next();
			if (workout.getId() == null || !existingWorkout.getId().equals(workout.getId())) {
				throw new ExistingWorkoutInDateRangeException(existingWorkout.getStartDate(), existingWorkout.getEndDate());
			}
		}
		
		workoutRepository.save(workout);
	}
	
	public void deleteWorkout(Workout workout) {
		workoutRepository.delete(workout);
	}

	public Workout findWorkoutById(int workoutId) {
		return workoutRepository.findById(workoutId);
	}
	
	public Memory findMemoryById(int memoryId) {
		return memoryRepository.findById(memoryId);
	}
	
	public void saveMemory(Memory memory) throws MemoryOutOfTimeException {
		Workout workout = memory.getTraining().getWorkoutTraining().getWorkout();
		
		if (memory.getDate().isBefore(workout.getStartDate()) || memory.getDate().isAfter(workout.getEndDate())) {
			throw new MemoryOutOfTimeException();
		}
		this.memoryRepository.save(memory);
	}
	
	@Transactional
	public void deleteMemory(Memory memory) {
		this.memoryRepository.delete(memory);
	}
	
	@Transactional
	public void deleteWorkoutTraining(WorkoutTraining workoutTraining) {
		this.workoutTrainingRepository.delete(workoutTraining);
	}
}
