package org.springframework.samples.petclinic.service.exceptions;

import org.springframework.samples.petclinic.model.Product;

public class NoProductPurchaseAmountException extends Exception {
	
	private Product product;
		
	public NoProductPurchaseAmountException(String message, Product product) {
		super(message);
		this.product = product;
	}

}
