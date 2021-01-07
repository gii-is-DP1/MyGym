package org.springframework.samples.petclinic.repository;


import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Purchase;

public interface PurchaseRepository extends Repository<Purchase, Integer>{

	Purchase findById(int id) throws DataAccessException;
	
	void save(Purchase purchase) throws DataAccessException;
	
	Collection<Purchase> findAll() throws DataAccessException;
	
	void delete(Purchase purchase) throws DataAccessException;
	
}
