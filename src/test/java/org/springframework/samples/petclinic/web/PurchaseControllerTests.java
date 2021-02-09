package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = PurchaseController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class PurchaseControllerTests {

	private static final int TEST_PURCHASE_ID = 1;

	@Autowired
	private PurchaseController purchaseController;

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;
	
	private Purchase purchase;
	
	@BeforeEach
	void setup() {
		
		purchase = new Purchase();
		purchase.setId(TEST_PURCHASE_ID);
		purchase.setDate(LocalDate.of(2021, 02, 20));
		purchase.setTotal(10.5);
		purchase.setVat(4.0);
		purchase.setProductPurchases(Sets.newHashSet());
		given(this.productService.findPurchaseById(TEST_PURCHASE_ID)).willReturn(purchase);

	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewPurchaseForm() throws Exception {
		mockMvc.perform(get("/purchases/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("purchases/createOrUpdatePurchaseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(
				post("/purchases/new")
					.with(csrf())
					.param("date", "26/02/2021")
					.param("vat", "4.0")
					.param("total", "10.5")
					.param("productPurchases", "1;1;1")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/purchases"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewPurchaseFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/purchases/new")
					.with(csrf())
				)
				.andExpect(model().attributeHasErrors("purchase"))
				.andExpect(model().attributeHasFieldErrors("purchase", "date"))
				.andExpect(model().attributeHasFieldErrors("purchase", "vat"))
				.andExpect(model().attributeHasFieldErrors("purchase", "productPurchases"))
				.andExpect(status().isOk())
				.andExpect(view().name("purchases/createOrUpdatePurchaseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowPurchases() throws Exception {
		mockMvc.perform(get("/purchases"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("selections"))
			.andExpect(view().name("purchases/purchasesList"));
	}

	
	@WithMockUser(value = "spring")
    @Test
	void testEditPurchaseForm() throws Exception {
		
		mockMvc.perform(get("/purchases/{id}/edit", TEST_PURCHASE_ID))
			.andExpect(model().attributeExists("purchase"))
			.andExpect(model().attribute("purchase", hasProperty("date", is(LocalDate.of(2021, 02, 20)))))
			.andExpect(model().attribute("purchase", hasProperty("vat", is(4.0))))
			.andExpect(model().attribute("purchase", hasProperty("total", is(10.5))))
			.andExpect(status().isOk())
			.andExpect(view().name("purchases/createOrUpdatePurchaseForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditPurchaseNotFoundForm() throws Exception {
		given(this.productService.findPurchaseById(5)).willReturn(null);
		
		mockMvc.perform(get("/purchases/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("purchase"))
			.andExpect(view().name("purchases/createOrUpdatePurchaseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditPurchaseFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/purchases/{id}/edit", TEST_PURCHASE_ID)
					.with(csrf())
					.param("date", "26/02/2021")
					.param("vat", "5.0")
					.param("total", "11.5")
					.param("productPurchases", "1;1;1")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/purchases/" + TEST_PURCHASE_ID));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditPurchaseFormError() throws Exception {		
		mockMvc.perform(
				post("/purchases/{id}/edit", TEST_PURCHASE_ID)
					.with(csrf())
					.param("date", "")
				)
				.andExpect(model().attributeHasErrors("purchase"))
				.andExpect(model().attributeHasFieldErrors("purchase", "date"))
				.andExpect(status().isOk())
				.andExpect(view().name("purchases/createOrUpdatePurchaseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testViewPurchaseDetailSuccess() throws Exception {		
		mockMvc.perform(get("/purchases/{id}", TEST_PURCHASE_ID))
				.andExpect(model().attributeExists("purchase"))
				.andExpect(model().attribute("purchase", hasProperty("id", is(TEST_PURCHASE_ID))))
                .andExpect(status().isOk())
				.andExpect(view().name("purchases/purchaseDetails"));
	}


}
