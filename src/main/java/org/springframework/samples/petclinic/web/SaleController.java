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
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.exceptions.NoProductSaleAmountException;
import org.springframework.samples.petclinic.service.exceptions.OutOfStockException;
import org.springframework.samples.petclinic.service.exceptions.SaleWithoutProductsException;
import org.springframework.samples.petclinic.util.ProductSaleCollectionEditor;
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
public class SaleController {

	private static final String VIEWS_SALES_CREATE_OR_UPDATE_FORM = "sales/createOrUpdateSaleForm";
	
	private static final String VIEWS_SALES_LIST = "sales/salesList";

	private final ProductService productService;
	
	public static final Double DEFAULT_VAT = 4.0;
	
	private static final String VIEWS_ERROR = "exception";
	
	private static final String VIEWS_SALES_DETAIL = "sales/saleDetails";

	@Autowired
	public SaleController(ProductService productService) {
		this.productService = productService;
	}

	@InitBinder("sale")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.registerCustomEditor(Set.class, "productSales", new ProductSaleCollectionEditor(Set.class, productService));
	}

	@ModelAttribute("products")
	public Collection<Product> populateProducts() {
		return (Collection<Product>) this.productService.findAllProducts(); 
	}

	@GetMapping(value = "/sales")
	public String processFindForm(Map<String, Object> model) {
		
		Collection<Sale> results = this.productService.findAllSales();

		model.put("selections", results);
		return VIEWS_SALES_LIST;
	}

	@GetMapping(value = "/sales/new")
	public String initCreationForm(Map<String, Object> model) {
		Sale sale = new Sale();
		sale.setVat(DEFAULT_VAT);
		model.put("sale", sale);
		return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/sales/new")
	public String processCreationForm(@Valid Sale sale, BindingResult result, ModelMap model) {
		if (sale.getProductSales() == null || sale.getProductSales().isEmpty()) {
			result.rejectValue("productSales", "invalid", "Debe incluir al menos un producto");
		}
		if (result.hasErrors()) {
			model.put("sale", sale);
			return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
		} else {
			sale.setTotal(getSaleTotal(sale));
			
			sale.getProductSales().forEach(productSale -> productSale.setSale(sale));
			
			try {
				this.productService.saveSale(sale);
				
				return "redirect:/sales";
			} catch (OutOfStockException e) {
				result.rejectValue("productSales", "invalid", "No hay stock suficiente para el producto " + e.getProduct().getName() + " (stock actual = " + e.getProduct().getStockage() + ")");
			} catch (NoProductSaleAmountException e) {
				result.rejectValue("productSales", "invalid", "Todos los productos deben incluir al menos una unidad");
			} catch (SaleWithoutProductsException e) {
				result.rejectValue("productSales", "invalid", "Debe incluir al menos un producto");
			}
			return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
		}
	}
	
	@GetMapping("/sales/{saleId}")
	public ModelAndView showTraining(@PathVariable("saleId") int saleId, Principal principal) {
		Sale sale = this.productService.findSaleById(saleId);
		ModelAndView mav;
		if (sale == null) {
			mav = new ModelAndView(VIEWS_ERROR);
			mav.addObject("exception", new Exception("La venta solicitada no existe"));
		} else {
			/*boolean allowed = hasAuthority(SecurityConfiguration.ADMIN);
			System.out.println("is admin: " + allowed);
			if (!allowed) {
				User user = new User();
				user.setUsername(principal.getName());
				Collection<Training> userTrainings = this.workoutService.findTrainingsByUser(user);
				allowed = userTrainings.stream().anyMatch(t -> t.getId().equals(saleId));
				System.out.println("training owned by user: " + allowed);
			}
			if (allowed) {*/
				mav = new ModelAndView(VIEWS_SALES_DETAIL);
				mav.addObject("sale", sale);
			/*} else {
				mav = new ModelAndView(VIEWS_ERROR);
				mav.addObject("exception", new Exception("No tiene permiso para visualizar el entrenamiento"));
			}*/
		}
		return mav;
	}
	
	@GetMapping(value = "/sales/{saleId}/delete")
	public String deleteTraining(@PathVariable("saleId") int saleId, ModelMap model) {
		Sale sale = this.productService.findSaleById(saleId);
		if (sale != null) {
			this.productService.deleteSale(sale);
		}
		return "redirect:/sales";
	}
	
	@GetMapping(value = "/sales/{saleId}/edit")
	public String initUpdateForm(@PathVariable("saleId") int saleId, ModelMap model) {
		Sale sale = this.productService.findSaleById(saleId);
		model.put("sale", sale);
		return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/sales/{saleId}/edit")
	public String processUpdateForm(@Valid Sale sale, BindingResult result, @PathVariable("saleId") int saleId, ModelMap model) {
		if (sale.getProductSales() == null || sale.getProductSales().isEmpty()) {
			result.rejectValue("productSales", "invalid", "Debe incluir al menos un producto");
		}
		if (result.hasErrors()) {
			model.put("sale", sale);
			return VIEWS_SALES_CREATE_OR_UPDATE_FORM;
		} else {
			Sale saleToUpdate = this.productService.findSaleById(saleId);
			
			Collection<ProductSale> entriesToBeRemoved = saleToUpdate.getProductSales().stream()
					.filter(productSale -> sale.getProductSales().stream()
							.allMatch(ps -> !productSale.getId().equals(ps.getId()))
					)
					.collect(Collectors.toList());
			
			entriesToBeRemoved.forEach(productSale -> {
				productSale.setSale(null);
				productService.deleteProductSale(productSale);
				saleToUpdate.getProductSales().remove(productSale);
			});
			
			BeanUtils.copyProperties(sale, saleToUpdate, "id", "isGeneric", "productSales");
			
			sale.getProductSales().stream()
				.filter(productSale -> productSale.getId() == null)
				.forEach(productSale -> {
					productSale.setSale(saleToUpdate);
					saleToUpdate.getProductSales().add(productSale);
				});

			saleToUpdate.setTotal(getSaleTotal(saleToUpdate));
			
			try {
				this.productService.saveSale(saleToUpdate);
				
			} catch (OutOfStockException e) {
				result.rejectValue("productSales", "invalid", "No hay stock suficiente para el producto " + e.getProduct().getName() + " (stock actual = " + e.getProduct().getStockage() + ")");
			} catch (NoProductSaleAmountException e) {
				result.rejectValue("productSales", "invalid", "Todos los productos deben incluir al menos una unidad");
			} catch (SaleWithoutProductsException e) {
				result.rejectValue("productSales", "invalid", "Debe incluir al menos un producto");
			}
			return "redirect:/sales/" + saleId;
		}
	}
	
	private Double getSaleTotal(Sale sale) {
		Double total = sale.getProductSales().stream()
				.mapToDouble(productSale -> productSale.getPrice() * productSale.getAmount())
				.sum();
		
		return total + (total * sale.getVat() / 100);
	}



}