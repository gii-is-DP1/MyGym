package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductserviceTests {
	
	@Autowired
	protected ProductService service;
	
	@Test
	void shouldFindProductWithCorrectId() {
		Product p2 = this.service.findById(2);
		assertThat(p2.getName()).startsWith("Batidito de Fresa");
		assertThat(p2.getStockage().equals(23));
		assertThat(p2.getPrice().equals(3.50));

	}
	
	@Test
	@Transactional
	public void shouldUpdateProductName() throws Exception {
		Product p2 = this.service.findById(2);
		String oldName = p2.getName();

		String newName = oldName + "X";
		p2.setName(newName);
		this.service.save(p2);

		p2 = this.service.findById(2);
		assertThat(p2.getName()).isEqualTo(newName);
	}
	
	@Test
	@Transactional
	public void shouldInsertProduct() {
		Collection<Product> products = this.service.findAll();
		int found = products.size();

		Product p = new Product();
		p.setName("Example");
		p.setDescription("Descp");
		p.setStockage(12);
		p.setPrice(3.5);
                
		this.service.save(p);
		assertThat(p.getId().longValue()).isNotEqualTo(0);

		products = this.service.findAll();
		assertThat(products.size()).isEqualTo(found + 1);
	}
	
	


}
