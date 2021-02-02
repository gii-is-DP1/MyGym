package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.model.Sale;

public interface ProductSaleRepository extends Repository<ProductSale, Integer> {
	
	public ProductPurchase findById(int id);
	
	public Collection<ProductSale> findAll() throws DataAccessException;

	public Collection<ProductSale> findBySale(Sale sale);

	void save(ProductSale productSale) throws DataAccessException;
	
	void delete(ProductSale productSale) throws DataAccessException;
}
