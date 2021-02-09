package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Purchase;

public interface PurchaseRepository extends Repository<Purchase, Integer> {
	
	public Purchase findById(int id);
	
	public Collection<Purchase> findAll() throws DataAccessException;

	void save(Purchase purchase) throws DataAccessException;
	
	void delete(Purchase purchase) throws DataAccessException;
	
	@Query("SELECT DISTINCT purchase FROM ProductPurchase productPurchase "
			+ "INNER JOIN productPurchase.purchase purchase "
			+ "INNER JOIN productPurchase.product product "
			+ "WHERE product = :product")
	Collection<Purchase> findByProduct(Product product) throws DataAccessException;
}
