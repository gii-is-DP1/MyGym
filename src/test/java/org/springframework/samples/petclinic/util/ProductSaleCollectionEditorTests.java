package org.springframework.samples.petclinic.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductSaleCollectionEditorTests {

	private ProductSaleCollectionEditor collectionEditor;
	
	@Autowired
	private ProductService productService;
	
	@BeforeEach
	void setup() {
		collectionEditor = new ProductSaleCollectionEditor(Set.class, productService);
	}
	
	@Test
	void shouldParseCollectionSuccess() {
		String[] source = new String[] {
			"1;1;2.50",
			"2;10;2.50;1"
		};	
		
		collectionEditor.setValue(source);
		
		Object value = collectionEditor.getValue();
		
		assertNotNull(value);
		assertTrue(value instanceof Set);
		
		Set<?> set = (Set<?>) value;
		
		assertEquals(2, set.size(), "There must be 2 elements in the converted collection");
		
		Iterator<?> itr = set.iterator();
		
		Object element = itr.next();
		
		assertTrue(element instanceof ProductSale);
		
		ProductSale productSale = (ProductSale) element;

		assertNotNull(productSale.getProduct(), "There must not be null");
		assertEquals(productSale.getProduct().getId(), 1, "There must have ID = 1");
		assertEquals(productSale.getPrice(), 2.50, "The product must cost 2.50");
		assertEquals(productSale.getAmount(), 1, "There must be 1 unit of product");

		productSale = (ProductSale) itr.next();
		assertEquals(productSale.getId(), 1, "Second element must have ID = 1");
	}
	
}
