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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = ProductController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class ProductControllerTests {

	private static final int TEST_PRODUCT_ID = 1;

	@Autowired
	private ProductController productController;

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;
	
	private Product product;
	
	@BeforeEach
	void setup() {

		product = new Product();
		product.setId(TEST_PRODUCT_ID);
		product.setName("Producto fitness");
		product.setDescription("Descripción de un producto de fitness");
		product.setPrice(1.50);
		product.setStockage(10);
		product.setInactive(Boolean.FALSE);
		given(this.productService.findProductById(TEST_PRODUCT_ID)).willReturn(product);

	}

    @WithMockUser(value = "spring")
    @Test
	void testInitNewProductForm() throws Exception {
		mockMvc.perform(get("/products/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(
				post("/products/new")
					.with(csrf())
					.param("name", "Producto fitness")
					.param("description", "Un producto para fitness")
					.param("stockage", "0")
					.param("price", "5.70")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/products"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessNewProductFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/products/new")
					.with(csrf())
					.param("description", "Un producto para fitness")
				)
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "name"))
				.andExpect(model().attributeHasFieldErrors("product", "price"))
				.andExpect(status().isOk())
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowProducts() throws Exception {
		mockMvc.perform(get("/products"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("products"))
			.andExpect(view().name("products/productsList"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowProductsByName() throws Exception {
		given(this.productService.findProductByName("p")).willReturn(Lists.newArrayList(new Product()));

		mockMvc.perform(get("/products")
					.param("name", "p")
				)
			.andExpect(status().isOk())
			.andExpect(view().name("products/productsList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditProductForm() throws Exception {
		
		mockMvc.perform(get("/products/{id}/edit", TEST_PRODUCT_ID))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attribute("product", hasProperty("name", is("Producto fitness"))))
			.andExpect(model().attribute("product", hasProperty("description", is("Descripción de un producto de fitness"))))
			.andExpect(model().attribute("product", hasProperty("stockage", is(10))))
			.andExpect(model().attribute("product", hasProperty("price", is(1.5))))
			.andExpect(status().isOk())
			.andExpect(view().name("products/createOrUpdateProductForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testEditProductNotFoundForm() throws Exception {
		given(this.productService.findProductById(5)).willReturn(null);
		
		mockMvc.perform(get("/products/{id}/edit", 5))
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("product"))
			.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditProductFormSuccess() throws Exception {		
		mockMvc.perform(
				post("/products/{id}/edit", TEST_PRODUCT_ID)
					.with(csrf())
					.param("name", "Producto fitness")
					.param("description", "Un producto para fitness")
					.param("stockage", "0")
					.param("price", "5.70")
				)
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/products"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessEditProductFormError() throws Exception {		
		mockMvc.perform(
				post("/products/{id}/edit", TEST_PRODUCT_ID)
					.with(csrf())
					.param("name", "")
				)
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "name"))
				.andExpect(status().isOk())
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

}
