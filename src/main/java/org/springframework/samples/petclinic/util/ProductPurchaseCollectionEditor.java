package org.springframework.samples.petclinic.util;

import java.util.Collection;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.service.ProductService;

public class ProductPurchaseCollectionEditor extends CustomCollectionEditor {

	private final ProductService productService;
	
	public ProductPurchaseCollectionEditor(Class<? extends Collection> collectionType, ProductService productService) {
		super(collectionType);
		
		this.productService = productService;
	}
	
	@Override
	protected Object convertElement(Object element) {
		String value = (String) element;
		
		try {
			String[] splitted = value.split(";");
			
			Integer productId = parseInt(splitted[0]);
			Integer amount = 0;
			if (splitted.length > 1) {
				amount = parseInt(splitted[1]);
			}
			Double price = 0.0;
			if (splitted.length > 2) {
				price = parseDouble(splitted[2]);
			}
			
			Product product = productService.findProductById(productId);
			
			ProductPurchase productPurchase = new ProductPurchase();
			
			if (splitted.length > 3) {
				productPurchase.setId(parseInt(splitted[3]));
			}
			
			productPurchase.setProduct(product);
			productPurchase.setPrice(price);
			productPurchase.setAmount(amount);
			
			return productPurchase;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private Integer parseInt(String s) {
		if (s == null || s.trim().isEmpty()) {
			return 0;
		}
		try {
			return new Integer(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private Double parseDouble(String s) {
		if (s == null || s.trim().isEmpty()) {
			return 0.0;
		}
		try {
			return new Double(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
