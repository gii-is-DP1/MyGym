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

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Workout;

/**
 * Spring Data JPA specialization of the {@link WorkoutRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface WorkoutRepository extends Repository<Workout, Integer> {
	
	/**
	 * Retrieve a <code>Workout</code> from the data store by id.
	 * @param id the id to search for
	 * @return the <code>Workout</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Workout findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Workout</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Workout</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Workout workout) throws DataAccessException;
	
	/**
	 * Find a <code>Workout</code> list.
	 * @param name the <code>Workout</code> name
	 * @return the <code>Workout</code> list filtered by name
	 * @see BaseEntity#isNew
	 */
	@Query("SELECT DISTINCT workout FROM Workout workout inner join workout.user user WHERE user.username = :username")
	Collection<Workout> findByUser(@Param("username") String username) throws DataAccessException;
	
	/**
	 * Find a <code>Workout</code> list into a date range.
	 * @param startDate the <code>Workout</code> start date
	 * @param endDate the <code>Workout</code> end date
	 * @return the <code>Workout</code> list filtered
	 */
	@Query("SELECT workout FROM Workout workout WHERE user = :user and (startDate between :start and :end or endDate between :start and :end)")
	Collection<Workout> findActiveWorkoutsForUser(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end) throws DataAccessException;
	
	/**
	 * Find a <code>Workout</code> list.
	 * @return the <code>Workout</code> list
	 * @see BaseEntity#isNew
	 */
	Collection<Workout> findAll() throws DataAccessException;
	
	/**
	 * Delete a <code>Workout</code>
	 * @param exercise
	 * @throws DataAccessException
	 */
	void delete(Workout workout) throws DataAccessException;

}
