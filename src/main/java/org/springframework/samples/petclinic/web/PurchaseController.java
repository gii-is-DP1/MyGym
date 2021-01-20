package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.util.ProductPurchaseCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PurchaseController {

	private static final String VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM = "purchases/createOrUpdatePurchaseForm";
	
	private static final String VIEWS_PURCHASES_LIST = "purchases/purchasesList";

	private final ProductService productService;
	
	public static final Double DEFAULT_VAT = 4.0;

	@Autowired
	public PurchaseController(ProductService productService) {
		this.productService = productService;
	}

	@InitBinder("purchase")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.registerCustomEditor(Set.class, "productPurchases", new ProductPurchaseCollectionEditor(Set.class, productService));
	}

	@ModelAttribute("products")
	public Collection<Product> populateProducts() {
		return (Collection<Product>) this.productService.findAllProducts(); 
	}

	@GetMapping(value = "/purchases")
	public String processFindForm(Map<String, Object> model) {
		
		Collection<Purchase> results = this.productService.findAllPurchases();

		model.put("selections", results);
		return VIEWS_PURCHASES_LIST;
	}

	@GetMapping(value = "/purchases/new")
	public String initCreationForm(Map<String, Object> model) {
		Purchase purchase = new Purchase();
		purchase.setVat(DEFAULT_VAT);
		model.put("purchase", purchase);
		return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/purchases/new")
	public String processCreationForm(@Valid Purchase purchase, BindingResult result, ModelMap model) {
		System.out.println("result.hasErrors: " + result.hasErrors());
		System.out.println("result: " + result);
		System.out.println("productPurchases: " + purchase.getProductPurchases());
		if (result.hasErrors()) {
			model.put("purchase", purchase);
			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		} else {
			Double total = purchase.getProductPurchases().stream()
					.mapToDouble(productPurchase -> productPurchase.getPrice() * productPurchase.getAmount())
					.sum();
			
			purchase.setTotal(total + (total * purchase.getVat() / 100));
			
			purchase.getProductPurchases().forEach(productPurchase -> productPurchase.setPurchase(purchase));
			
			this.productService.savePurchase(purchase);
			return "redirect:/purchases";
		}
	}


}
