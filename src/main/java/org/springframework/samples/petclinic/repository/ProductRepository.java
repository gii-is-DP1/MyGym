package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	@Query("SELECT product FROM Product product  WHERE product.id =:id")
	public Product findById(@Param("id") int id);
	
	Collection<Product> findAll() throws DataAccessException;

	@Query("SELECT product FROM Produtc product WHERE product.name LIKE :name%")
	public Collection<Product> findByName(String name);
	
}
