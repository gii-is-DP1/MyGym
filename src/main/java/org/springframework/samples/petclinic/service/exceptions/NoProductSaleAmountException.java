package org.springframework.samples.petclinic.service.exceptions;

import org.springframework.samples.petclinic.model.Product;

public class NoProductSaleAmountException extends Exception {
	
	private Product product;
		
	public NoProductSaleAmountException(String message, Product product) {
		super(message);
		this.product = product;
	}

}
