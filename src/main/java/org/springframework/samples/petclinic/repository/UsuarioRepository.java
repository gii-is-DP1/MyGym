package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
	

	@Query("SELECT utype FROM UserType utype ORDER BY utype.name")
	List<UserType> findUserTypes() throws DataAccessException;
	

	@Query("SELECT rate FROM Rate rate ORDER BY rate.name")
	List<Rate> findRates() throws DataAccessException;

}
