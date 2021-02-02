package org.springframework.samples.petclinic.util;

import java.util.Collection;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.service.ProductService;

public class ProductSaleCollectionEditor extends CustomCollectionEditor {

	private final ProductService productService;
	
	public ProductSaleCollectionEditor(Class<? extends Collection> collectionType, ProductService productService) {
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
			
			ProductSale productSale = new ProductSale();
			productSale.setProduct(product);
			productSale.setPrice(price);
			productSale.setAmount(amount);
			
			System.out.println("Build product sale: " + productSale);
			
			return productSale;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
