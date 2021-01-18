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
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Memory;

/**
 * Spring Data JPA specialization of the {@link MemoryRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface MemoryRepository extends Repository<Memory, Integer> {

	/**
	 * Retrieve a <code>Memory</code> from the data store by id.
	 * @param id the id to search for
	 * @return the <code>Memory</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Memory findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Memory</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Memory</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Memory memory) throws DataAccessException;
	
	/**
	 * Find a <code>Memory</code> list.
	 * return the <code>Memory</code> list
	 * @see BaseEntity#isNew
	 */
	Collection<Memory> findAll() throws DataAccessException;
	
	@Query("DELETE FROM Memory m Where m.id = :id")
	void deleteById(int id) throws DataAccessException;
	
	/**
	 * Delete a <code>Memory</code>
	 * @param memory
	 * @throws DataAccessException
	 */
	void delete(Memory memory) throws DataAccessException;

}
