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
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = SaleController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class SaleControllerTests {

	private static final int TEST_SALE_ID = 1;

	@Autowired
	private SaleController saleController;

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;
	
	private Sale sale;
	
	@BeforeEach
	void setup() {
		
		sale = new Sale();
		sale.setId(TEST_SALE_ID);
		sale.setDate(LocalDate.of(2021, 02, 20));
		sale.setTotal(10.5);
		sale.setVat(4.0);
		sale.setProductSales(Sets.newHashSet());
		given(this.productService.findSaleById(TEST_SALE_ID)).willReturn(sale);

	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewSaleForm() throws Exception {
		mockMvc.perform(get("/sales/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("sales/createOrUpdateSaleForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(
				post("/sales/new")
					.with(csrf())
					.param("date", "26/02/2021")
					.param("vat", "4.0")
					.param("total", "10.5")
					.param("productSales", "1;1;1")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/sales"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewSaleFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/sales/new")
					.with(csrf())
				)
				.andExpect(model().attributeHasErrors("sale"))
				.andExpect(model().attributeHasFieldErrors("sale", "date"))
				.andExpect(model().attributeHasFieldErrors("sale", "vat"))
				.andExpect(model().attributeHasFieldErrors("sale", "productSales"))
				.andExpect(status().isOk())
				.andExpect(view().name("sales/createOrUpdateSaleForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowSales() throws Exception {
		mockMvc.perform(get("/sales"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("selections"))
			.andExpect(view().name("sales/salesList"));
	}

	
	@WithMockUser(value = "spring")
    @Test
	void testEditSaleForm() throws Exception {
		
		mockMvc.perform(get("/sales/{id}/edit", TEST_SALE_ID))
			.andExpect(model().attributeExists("sale"))
			.andExpect(model().attribute("sale", hasProperty("date", is(LocalDate.of(2021, 02, 20)))))
			.andExpect(model().attribute("sale", hasProperty("vat", is(4.0))))
			.andExpect(model().attribute("sale", hasProperty("total", is(10.5))))
			.andExpect(status().isOk())
			.andExpect(view().name("sales/createOrUpdateSaleForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditSaleNotFoundForm() throws Exception {
		given(this.productService.findSaleById(5)).willReturn(null);
		
		mockMvc.perform(get("/sales/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("purchase"))
			.andExpect(view().name("sales/createOrUpdateSaleForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditSaleFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/sales/{id}/edit", TEST_SALE_ID)
					.with(csrf())
					.param("date", "26/02/2021")
					.param("vat", "5.0")
					.param("total", "11.5")
					.param("productSales", "1;1;1")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/sales/" + TEST_SALE_ID));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditSaleFormError() throws Exception {		
		mockMvc.perform(
				post("/sales/{id}/edit", TEST_SALE_ID)
					.with(csrf())
					.param("date", "")
				)
				.andExpect(model().attributeHasErrors("sale"))
				.andExpect(model().attributeHasFieldErrors("sale", "date"))
				.andExpect(status().isOk())
				.andExpect(view().name("sales/createOrUpdateSaleForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testViewSaleDetailSuccess() throws Exception {		
		mockMvc.perform(get("/sales/{id}", TEST_SALE_ID))
				.andExpect(model().attributeExists("sale"))
				.andExpect(model().attribute("sale", hasProperty("id", is(TEST_SALE_ID))))
                .andExpect(status().isOk())
				.andExpect(view().name("sales/saleDetails"));
	}


}
