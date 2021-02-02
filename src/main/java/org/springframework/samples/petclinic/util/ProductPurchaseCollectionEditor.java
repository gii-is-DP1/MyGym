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
		
		System.out.println("converting element: " + value);
		
		try {
			String[] splitted = value.split(";");
			
			Integer productId = new Integer(splitted[0]);
			Integer amount = new Integer(splitted[1]);
			Double price = new Double(splitted[2]);
			
			Product product = productService.findProductById(productId);
			
			ProductPurchase productPurchase = new ProductPurchase();
			productPurchase.setProduct(product);
			productPurchase.setPrice(price);
			productPurchase.setAmount(amount);
			
			System.out.println("Build product purchase: " + productPurchase);
			
			return productPurchase;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
