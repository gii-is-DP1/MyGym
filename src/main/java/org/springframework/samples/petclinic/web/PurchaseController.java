package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.exceptions.NoProductPurchaseAmountException;
import org.springframework.samples.petclinic.service.exceptions.PurchaseWithoutProductsException;
import org.springframework.samples.petclinic.util.ProductPurchaseCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PurchaseController {

	private static final String VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM = "purchases/createOrUpdatePurchaseForm";
	
	private static final String VIEWS_PURCHASES_LIST = "purchases/purchasesList";

	private final ProductService productService;
	
	public static final Double DEFAULT_VAT = 4.0;
	
	private static final String VIEWS_ERROR = "exception";
	
	private static final String VIEWS_PURCHASES_DETAIL = "purchases/purchaseDetails";

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
		if (purchase.getProductPurchases() == null || purchase.getProductPurchases().isEmpty()) {
			result.rejectValue("productPurchases", "invalid", "Debe incluir al menos un producto");
		}
		if (result.hasErrors()) {
			model.put("purchase", purchase);
			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		} else {
			purchase.setTotal(getPurchaseTotal(purchase));
			
			purchase.getProductPurchases().forEach(productPurchase -> productPurchase.setPurchase(purchase));
			
			try {
				this.productService.savePurchase(purchase);
				
				return "redirect:/purchases";
			} catch (PurchaseWithoutProductsException e) {
				result.rejectValue("productPurchases", "invalid", "Debe incluir al menos un producto");
			} catch (NoProductPurchaseAmountException e) {
				result.rejectValue("productPurchases", "invalid", "Todos los productos deben incluir al menos una unidad");
			}

			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		}
	}
	
	@GetMapping("/purchases/{purchaseId}")
	public ModelAndView showTraining(@PathVariable("purchaseId") int purchaseId, Principal principal) {
		Purchase purchase = this.productService.findPurchaseById(purchaseId);
		ModelAndView mav;
		if (purchase == null) {
			mav = new ModelAndView(VIEWS_ERROR);
			mav.addObject("exception", new Exception("La compra solicitada no existe"));
		} else {
			/*boolean allowed = hasAuthority(SecurityConfiguration.ADMIN);
			System.out.println("is admin: " + allowed);
			if (!allowed) {
				User user = new User();
				user.setUsername(principal.getName());
				Collection<Training> userTrainings = this.workoutService.findTrainingsByUser(user);
				allowed = userTrainings.stream().anyMatch(t -> t.getId().equals(purchaseId));
				System.out.println("training owned by user: " + allowed);
			}
			if (allowed) {*/
				mav = new ModelAndView(VIEWS_PURCHASES_DETAIL);
				mav.addObject("purchase", purchase);
			/*} else {
				mav = new ModelAndView(VIEWS_ERROR);
				mav.addObject("exception", new Exception("No tiene permiso para visualizar el entrenamiento"));
			}*/
		}
		return mav;
	}
	
	@GetMapping(value = "/purchases/{purchaseId}/delete")
	public String deleteTraining(@PathVariable("purchaseId") int purchaseId, ModelMap model) {
		Purchase purchase = this.productService.findPurchaseById(purchaseId);
		if (purchase != null) {
			this.productService.deletePurchase(purchase);
		}
		return "redirect:/purchases";
	}
	
	@GetMapping(value = "/purchases/{purchaseId}/edit")
	public String initUpdateForm(@PathVariable("purchaseId") int purchaseId, ModelMap model) {
		Purchase purchase = this.productService.findPurchaseById(purchaseId);
		model.put("purchase", purchase);
		return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/purchases/{purchaseId}/edit")
	public String processUpdateForm(@Valid Purchase purchase, BindingResult result, @PathVariable("purchaseId") int purchaseId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("purchase", purchase);
			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		} else {
			Purchase purchaseToUpdate = this.productService.findPurchaseById(purchaseId);
			
			Collection<ProductPurchase> entriesToBeRemoved = purchaseToUpdate.getProductPurchases().stream()
					.filter(productPurchase -> purchase.getProductPurchases().stream()
							.allMatch(pp -> !productPurchase.getId().equals(pp.getId()))
					)
					.collect(Collectors.toList());
			
			entriesToBeRemoved.forEach(productPurchase -> {
					productPurchase.setPurchase(null);
					productService.deleteProductPurchase(productPurchase);
					purchaseToUpdate.getProductPurchases().remove(productPurchase);
				});
			
			BeanUtils.copyProperties(purchase, purchaseToUpdate, "id", "isGeneric", "productPurchases");
			
			purchase.getProductPurchases().stream()
				.filter(productPurchase -> productPurchase.getId() == null)
				.forEach(productPurchase -> {
					productPurchase.setPurchase(purchaseToUpdate);
					purchaseToUpdate.getProductPurchases().add(productPurchase);
				});
			
			purchaseToUpdate.setTotal(getPurchaseTotal(purchaseToUpdate));
			
			try {
				this.productService.savePurchase(purchaseToUpdate);

				return "redirect:/purchases/" + purchaseId;
			} catch (PurchaseWithoutProductsException e) {
				result.rejectValue("productPurchases", "invalid", "Debe incluir al menos un producto");
			} catch (NoProductPurchaseAmountException e) {
				result.rejectValue("productPurchases", "invalid", "Todos los productos deben incluir al menos una unidad");
			}	
			
			return "redirect:/purchases/";
		}
	}
	
	private Double getPurchaseTotal(Purchase purchase) {
		Double total = purchase.getProductPurchases().stream()
				.mapToDouble(productPurchase -> productPurchase.getPrice() * productPurchase.getAmount())
				.sum();
		
		return total + (total * purchase.getVat() / 100);
	}

}
