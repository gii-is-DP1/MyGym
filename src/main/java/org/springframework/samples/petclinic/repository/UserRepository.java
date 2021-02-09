package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.UserType;


public interface UserRepository extends CrudRepository<User, Integer>{

	@Query("SELECT DISTINCT user FROM User user WHERE user.username = :username")
	Optional<User> findByUsername(String username) throws DataAccessException;

	@Query("SELECT utype FROM UserType utype ORDER BY utype.name")
	List<UserType> findUserTypes() throws DataAccessException;
	

	@Query("SELECT rate FROM Rate rate ORDER BY rate.name")
	List<Rate> findRates() throws DataAccessException;

}
