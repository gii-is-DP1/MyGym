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
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.ExerciseType;

/**
 * Spring Data JPA specialization of the {@link ExerciseRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface ExerciseRepository extends Repository<Exercise, Integer> {

	/**
	 * Retrieve all <code>PetType</code>s from the data store.
	 * @return a <code>Collection</code> of <code>PetType</code>s
	 */
	@Query("SELECT etype FROM ExerciseType etype ORDER BY etype.name")
	List<ExerciseType> findExerciseTypes() throws DataAccessException;
	
	/**
	 * Retrieve a <code>Pet</code> from the data store by id.
	 * @param id the id to search for
	 * @return the <code>Pet</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Exercise findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Pet</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Pet</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Exercise exercise) throws DataAccessException;
	
	/**
	 * Find a <code>Exercise</code> list.
	 * return the <code>Exercise</code> list
	 * @see BaseEntity#isNew
	 */
	@Query("SELECT exercise FROM Exercise exercise inner join fetch exercise.type")
	List<Exercise> find() throws DataAccessException;

}
