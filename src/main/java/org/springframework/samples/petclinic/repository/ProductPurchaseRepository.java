package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.Purchase;

public interface ProductPurchaseRepository extends Repository<ProductPurchase, Integer> {
	
	public ProductPurchase findById(int id);
	
	public Collection<ProductPurchase> findAll() throws DataAccessException;

	public Collection<ProductPurchase> findByPurchase(Purchase Purchase);

	public Collection<ProductPurchase> findByProduct(Product product);

	void save(ProductPurchase productPurchase) throws DataAccessException;
	
	void delete(ProductPurchase productPurchase) throws DataAccessException;
}
