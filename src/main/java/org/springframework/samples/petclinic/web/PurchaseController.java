package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.service.PurchaseService;
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
public class PurchaseController {
	
	private static final String VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM = "purchases/createOrUpdatePurchaseForm";

	private final PurchaseService purchaseService;

	@Autowired
	public PurchaseController(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	

	@InitBinder("purchase")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	
	
	@GetMapping(value = "/purchases")
	public String processFindForm(Purchase purchase, BindingResult result, Map<String, Object> model) {

		
		Iterable<Purchase> results = this.purchaseService.findAll();
		
			model.put("purchases", results);
			return "purchases/purchasesList";
		
	}

	@GetMapping(value = "/purchases/new")
	public String initCreationForm(Map<String, Object> model) {
		Purchase purchase = new Purchase();
		model.put("purchase", purchase);
		return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/purchaseS/new")
	public String processCreationForm(@Valid Purchase purchase, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("purchase", purchase);
			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		} else {
			this.purchaseService.save(purchase);
			return "redirect:/purchases";
		}
	}

	
	@GetMapping("/purchaseS/{purchaseId}")
	public ModelAndView showPurchase(@PathVariable("purchaseId") int purchaseId) {
		ModelAndView mav = new ModelAndView("purchaseS/purchaseDetails");
		mav.addObject(this.purchaseService.findPurchaseById(purchaseId));
		return mav;
	}

	@GetMapping(value = "/purchases/{purchaseId}/edit")
	public String initUpdateForm(@PathVariable("purchaseId") int purchaseId, ModelMap model) {
		Purchase purchase = this.purchaseService.findPurchaseById(purchaseId);
		model.put("purchase", purchase);
		return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/purchases/{purchaseId}/delete")
	public String deletePurchase(@PathVariable("purchaseId") int purchaseId, ModelMap model) {
		Purchase purchase = this.purchaseService.findPurchaseById(purchaseId);
		if (purchase != null) {
			this.purchaseService.delete(purchase);
		}
		return "redirect:/purchases";
	}

	
	@PostMapping(value = "/purchases/{purchaseId}/edit")
	public String processUpdateForm(@Valid Purchase purchase, BindingResult result,
			@PathVariable("purchaseId") int purchaseId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("purchase", purchase);
			return VIEWS_PURCHASES_CREATE_OR_UPDATE_FORM;
		} else {
			Purchase purchaseToUpdate = this.purchaseService.findPurchaseById(purchaseId);
			BeanUtils.copyProperties(purchase, purchaseToUpdate, "id");
			this.purchaseService.save(purchaseToUpdate);
			return "redirect:/purchases";
		}
	}

}
