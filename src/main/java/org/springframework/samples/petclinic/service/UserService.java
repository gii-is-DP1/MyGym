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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.service.exceptions.StartDateAfterEndDateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(rollbackFor =  StartDateAfterEndDateException.class)
	public void save(User user) throws DataAccessException, StartDateAfterEndDateException {
		if(user.getFee().getStart_date().isAfter(user.getFee().getEnd_date())) {
			throw new StartDateAfterEndDateException();
		}
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	public Optional<User> findUser(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Transactional
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}
	
	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	@Transactional
	public Optional<User> findUserById(int userId) {
		return userRepository.findById(userId);
	}
	
	@Transactional(readOnly = true)
	public Collection<UserType> findUserTypes() throws DataAccessException {
		return userRepository.findUserTypes();
	}
	
	@Transactional(readOnly = true)
	public Collection<Rate> findRates() throws DataAccessException {
		return userRepository.findRates();
	}
}
