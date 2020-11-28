package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	@Query("SELECT product FROM Product product  WHERE product.id =:id")
	public Product findById(@Param("id") int id);
	
}
