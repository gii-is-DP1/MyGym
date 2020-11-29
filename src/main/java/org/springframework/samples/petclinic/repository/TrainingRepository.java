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

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Training;

/**
 * Spring Data JPA specialization of the {@link TrainingRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface TrainingRepository extends Repository<Training, Integer> {
	
	/**
	 * Retrieve a <code>Training</code> from the data store by id.
	 * @param id the id to search for
	 * @return the <code>Training</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Training findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Training</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Training</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Training training) throws DataAccessException;
	
	/**
	 * Find a <code>Training</code> list.
	 * @param name the <code>Training</code> name
	 * @return the <code>Training</code> list filtered by name
	 * @see BaseEntity#isNew
	 */
	@Query("SELECT DISTINCT training FROM Training training WHERE training.name LIKE %:name%")
	Collection<Training> findByName(@Param("name") String name) throws DataAccessException;
	
	/**
	 * Find a <code>Training</code> list.
	 * @return the <code>Training</code> list
	 * @see BaseEntity#isNew
	 */
	Collection<Training> findAll() throws DataAccessException;
	
	/**
	 * Delete a <code>Training</code>
	 * @param exercise
	 * @throws DataAccessException
	 */
	void delete(Training training) throws DataAccessException;

}
