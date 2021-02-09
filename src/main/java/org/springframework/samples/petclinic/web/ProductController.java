package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductController {

	private static final String VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM = "products/createOrUpdateProductForm";

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@InitBinder("product")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/products")
	public String processFindForm(Product product, BindingResult result, Map<String, Object> model) {
		
		if (product == null) {
			product = new Product();
		}

		log.debug("getting products by name = " + product.getName());
		Collection<Product> results = this.productService.findProductByName(product.getName());

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
	public String processCreationForm(@Valid Product product, BindingResult result, ModelMap model, Principal principal) {
		if (result.hasErrors()) {
			model.put("product", product);
			return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
		} else {
			this.productService.saveProduct(product);
			log.info("Product with ID = " + product.getId() + " created by " + principal.getName());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/{productId}")
	public ModelAndView showProduct(@PathVariable("productId") int productId) {
		ModelAndView mav = new ModelAndView("products/productDetails");
		mav.addObject(this.productService.findProductById(productId));
		return mav;
	}

	@GetMapping(value = "/products/{productId}/delete")
	public String deleteSala(@PathVariable("productId") int productId, ModelMap model, Principal principal) {
		Product product = this.productService.findProductById(productId);
		if (product != null) {
			log.info("Product with ID = " + product.getId() + " deleted by " + principal.getName());
			this.productService.deleteProduct(product);
		}
		return "redirect:/products";
	}

	@GetMapping(value = "/products/{productId}/edit")
	public String initUpdateForm(@PathVariable("productId") int productId, ModelMap model) {
		Product product = this.productService.findProductById(productId);
		model.put("product", product);
		return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/products/{productId}/edit")
	public String processUpdateForm(@Valid Product product, BindingResult result,
			@PathVariable("productId") int productId, ModelMap model, Principal principal) {
		if (result.hasErrors()) {
			model.put("product", product);
			return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
		} else {
			Product productToUpdate = this.productService.findProductById(productId);
			BeanUtils.copyProperties(product, productToUpdate, "id", "stockage");
			this.productService.saveProduct(productToUpdate);
			log.info("Product with ID = " + product.getId() + " updated by " + principal.getName());
			return "redirect:/products";
		}
	}

}
