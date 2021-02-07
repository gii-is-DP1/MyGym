package org.springframework.samples.petclinic.service.exceptions;

import org.springframework.samples.petclinic.model.Product;

public class OutOfStockException extends Exception {
	
	private Product product;
		
	public OutOfStockException(String message, Product product) {
		super(message);
		this.product = product;
	}
	
	public Product getProduct() {
		return this.product;
	}

}
