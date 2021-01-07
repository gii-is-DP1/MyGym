package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.service.SaleService;
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
public class SaleController {
	private static final String VIEWS_SALES_CREATE_OR_UPDATE_FORM = "sales/createOrUpdateSaleForm";

	private final SaleService saleService;

	@Autowired
	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}

	

	@InitBinder("sale")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	
	
	@GetMapping(value = "/sales")
	public String processFindForm(Sale sale, BindingResult result, Map<String, Object> model) {

		
		Iterable<Sale> results = this.saleService.findAll();
		
			model.put("sales", results);
			return "sales/salesList";
		
	}

	@GetMapping(value = "/sales/new")
	public String initCreationForm(Map<String, Object> model) {
		Sale sale = new Sale();
		model.put("sale", sale);
		return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/sales/new")
	public String processCreationForm(@Valid Sale sale, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("sale", sale);
			return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
		} else {
			this.saleService.save(sale);
			return "redirect:/sales";
		}
	}

	
	@GetMapping("/sales/{saleId}")
	public ModelAndView showSale(@PathVariable("saleId") int saleId) {
		ModelAndView mav = new ModelAndView("sales/saleDetails");
		mav.addObject(this.saleService.findSaleById(saleId));
		return mav;
	}

	@GetMapping(value = "/sales/{saleId}/edit")
	public String initUpdateForm(@PathVariable("saleId") int saleId, ModelMap model) {
		Sale sale = this.saleService.findSaleById(saleId);
		model.put("sale", sale);
		return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(value = "/sales/{saleId}/delete")
	public String deleteSale(@PathVariable("saleId") int saleId, ModelMap model) {
		Sale sale = this.saleService.findSaleById(saleId);
		if (sale != null) {
			this.saleService.delete(sale);
		}
		return "redirect:/sales";
	}

	
	@PostMapping(value = "/sales/{saleId}/edit")
	public String processUpdateForm(@Valid Sale sale, BindingResult result,
			@PathVariable("saleId") int saleId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("sale", sale);
			return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
		} else {
			Sale saleToUpdate = this.saleService.findSaleById(saleId);
			BeanUtils.copyProperties(sale, saleToUpdate, "id");
			this.saleService.save(saleToUpdate);
			return "redirect:/sales";
		}
	}

}
