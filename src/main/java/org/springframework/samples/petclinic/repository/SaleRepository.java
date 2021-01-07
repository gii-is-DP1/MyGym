package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Sale;

public interface SaleRepository extends Repository<Sale, Integer>{
	

	Sale findById(int id) throws DataAccessException;
		
	void save(Sale sale) throws DataAccessException;
		
	Collection<Sale> findAll() throws DataAccessException;
		
	void delete(Sale sale) throws DataAccessException;

}
