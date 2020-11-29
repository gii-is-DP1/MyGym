package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {
	
	private static final String VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM = "products/createOrUpdateProductForm";
	
	private final ProductService service;
	
	@Autowired
	public ProductController(ProductService service) {
		this.service = service;
	}
	
	@InitBinder("product")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/products")
	public String processFindForm(Product product, BindingResult result, Map<String, Object> model) {

		Iterable<Product> results = this.service.findAll();
		
			model.put("products", results);
			return "products/productsList";
		
	}
	
	@GetMapping(value = "/products/new")
	public String initCreationForm(Map<String, Object> model) {
		Product product = new Product();
		model.put("product", product);
		return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/products/new")
	public String processCreationForm(@Valid Product product, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("product", product);
			return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
		} else {
			this.service.save(product);
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/{productId}")
	public ModelAndView showSala(@PathVariable("productId") int productId) {
		ModelAndView mav = new ModelAndView("products/productDetails");
		mav.addObject(this.service.findById(productId));
		return mav;
	}
	
	@GetMapping(value = "/products/{productId}/edit")
	public String initUpdateForm(@PathVariable("productId") int productId, ModelMap model) {
		Product product = this.service.findById(productId);
		model.put("product", product);
		return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/products/{productId}/delete")
	public String deleteSala(@PathVariable("productId") int productId, ModelMap model) {
		Product product = this.service.findById(productId);
		if (product != null) {
			this.service.delete(product);
		}
		return "redirect:/products";
	}

	
	@PostMapping(value = "/products/{productId}/edit")
	public String processUpdateForm(@Valid Product product, BindingResult result,
			@PathVariable("productId") int productId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("product", product);
			return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
		} else {
			Product productToUpdate = this.service.findById(productId);
			BeanUtils.copyProperties(product, productToUpdate, "id");
			this.service.save(productToUpdate);
			return "redirect:/products";
		}
	}
	
}
