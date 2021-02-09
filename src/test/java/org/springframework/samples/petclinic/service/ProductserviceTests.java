package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.service.exceptions.NoProductPurchaseAmountException;
import org.springframework.samples.petclinic.service.exceptions.NoProductSaleAmountException;
import org.springframework.samples.petclinic.service.exceptions.OutOfStockException;
import org.springframework.samples.petclinic.service.exceptions.PurchaseWithoutProductsException;
import org.springframework.samples.petclinic.service.exceptions.SaleWithoutProductsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductserviceTests {
	
	@Autowired
	protected ProductService service;
	
	@Test
	void shouldFindProductWithCorrectId() {
		Product p2 = this.service.findProductById(2);
		assertThat(p2).isNotNull();
		assertThat(p2.getName()).isEqualTo("Batido de fresa");
		assertThat(p2.getStockage().equals(23));
		assertThat(p2.getPrice().equals(3.50));
		assertThat(p2.getInactive()).isFalse();
	}
	
	@Test
	void shouldFindProductByName() {
		Collection<Product> products = this.service.findProductByName("Batido");
		assertThat(products.isEmpty()).isFalse();
		Product p = products.iterator().next();
		assertThat(p.getName()).isEqualTo("Batido de fresa");
		
		Collection<Product> allProducts = this.service.findAllProducts();
		Collection<Product> nullNameProducts = this.service.findProductByName(null);
		
		assertThat(allProducts).isNotNull();
		assertThat(nullNameProducts).isNotNull();
		assertThat(allProducts.size()).isEqualTo(nullNameProducts.size());
	}
	
	@Test
	@Transactional
	public void shouldUpdateProductName() throws Exception {
		Product p2 = this.service.findProductById(2);
		String oldName = p2.getName();

		String newName = oldName + "X";
		p2.setName(newName);
		this.service.saveProduct(p2);

		p2 = this.service.findProductById(2);
		assertThat(p2.getName()).isEqualTo(newName);
	}
	
	@Test
	@Transactional
	public void shouldInsertProduct() {
		Collection<Product> products = this.service.findAllProducts();
		int found = products.size();

		Product p = new Product();
		p.setName("Example");
		p.setDescription("Descp");
		p.setPrice(3.5);
                
		this.service.saveProduct(p);
		assertThat(p.getId().longValue()).isNotEqualTo(0);
		assertThat(p.getInactive()).isFalse();

		products = this.service.findAllProducts();
		assertThat(products.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	public void shouldDeleteProduct() {
		Collection<Product> products = this.service.findAllProducts();
		int found = products.size();
		
		Product p = products.iterator().next();
		this.service.deleteProduct(p);
		assertThat(p.getInactive()).isTrue();
		
		products = this.service.findAllProducts();
		assertThat(products.size()).isEqualTo(found - 1);
		
		Product deleted = this.service.findProductById(p.getId());
		assertThat(deleted).isNull();
	}
	
	/********************* PURCHASE **********/

	@Test
	@Transactional
	public void shouldThrowsExceptionWhenPurchaseHasNoProducts() {
		Purchase purchase = new Purchase();
		purchase.setDate(LocalDate.now());
		purchase.setTotal(10.0);
		purchase.setVat(4.0);
		
		Assertions.assertThrows(PurchaseWithoutProductsException.class, () -> {
			this.service.savePurchase(purchase);
		});
	}

	@Test
	@Transactional
	public void shouldThrowsExceptionWhenPurchaseHasNoProductAmount() {
		Purchase purchase = new Purchase();
		purchase.setDate(LocalDate.now());
		purchase.setTotal(10.0);
		purchase.setVat(4.0);
		
		Product p = this.service.findProductById(1);
		
		Assertions.assertThrows(NoProductPurchaseAmountException.class, () -> {
			ProductPurchase productPurchase = new ProductPurchase();
			productPurchase.setPrice(1.0);
			productPurchase.setAmount(0);
			productPurchase.setPurchase(purchase);
			productPurchase.setProduct(p);
			
			purchase.setProductPurchases(new HashSet<>());
			purchase.getProductPurchases().add(productPurchase);
			
			this.service.savePurchase(purchase);
		});
	}
	
	private Product createTempProduct(String name, Double price) {
		Product p = new Product();
		p.setName(name);
		p.setPrice(price);
		return p;
	}

	@Test
	@Transactional
	public void shouldUpdateProductAmountOnSave() throws PurchaseWithoutProductsException, NoProductPurchaseAmountException {
		Product temp = createTempProduct("first temp", 1.50);
		
		this.service.saveProduct(temp);
		
		Purchase purchase = new Purchase();
		purchase.setDate(LocalDate.now());
		purchase.setVat(4.0);
		purchase.setTotal(52.0);
		
		ProductPurchase productPurchase = new ProductPurchase();
		productPurchase.setPrice(5.0);
		productPurchase.setAmount(10);
		productPurchase.setPurchase(purchase);
		productPurchase.setProduct(temp);
		
		purchase.setProductPurchases(Sets.newHashSet());
		purchase.getProductPurchases().add(productPurchase);
		
		this.service.savePurchase(purchase);

		Collection<Product> products = this.service.findProductByName(temp.getName());
		temp = products.iterator().next();
		
		assertThat(temp.getStockage()).isEqualTo(productPurchase.getAmount());
		
		purchase = this.service.findPurchaseById(purchase.getId());
		productPurchase = purchase.getProductPurchases().iterator().next();
		productPurchase.setAmount(5);
		this.service.savePurchase(purchase);

		products = this.service.findProductByName(temp.getName());
		temp = products.iterator().next();
		
		assertThat(temp.getStockage()).isEqualTo(5);
		
		Product temp2 = createTempProduct("second temp", 1.60);
		this.service.saveProduct(temp2);
		
		ProductPurchase productPurchase2 = new ProductPurchase();
		productPurchase2.setPrice(5.0);
		productPurchase2.setAmount(10);
		productPurchase2.setPurchase(purchase);
		productPurchase2.setProduct(temp2);
		
		purchase.getProductPurchases().add(productPurchase2);
		
		this.service.savePurchase(purchase);
		
		productPurchase.setPurchase(null);
		this.service.deleteProductPurchase(productPurchase);
		
		products = this.service.findProductByName(temp.getName());
		temp = products.iterator().next();
		
		assertThat(temp.getStockage()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void shouldInsertPurchase() throws PurchaseWithoutProductsException, NoProductPurchaseAmountException {
		Collection<Purchase> purchases = this.service.findAllPurchases();
		int found = purchases.size();
		
		Product temp = createTempProduct("temporal", 1.50);
		
		this.service.saveProduct(temp);
		
		Purchase purchase = new Purchase();
		purchase.setDate(LocalDate.now());
		purchase.setVat(4.0);
		purchase.setTotal(52.0);
		
		ProductPurchase productPurchase = new ProductPurchase();
		productPurchase.setPrice(5.0);
		productPurchase.setAmount(10);
		productPurchase.setPurchase(purchase);
		productPurchase.setProduct(temp);
		
		purchase.setProductPurchases(Sets.newHashSet());
		purchase.getProductPurchases().add(productPurchase);
		
		this.service.savePurchase(purchase);
		
		assertThat(purchase.getId().longValue()).isNotEqualTo(0);

		purchases = this.service.findAllPurchases();
		assertThat(purchases.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	public void shouldUpdatePurchase() throws PurchaseWithoutProductsException, NoProductPurchaseAmountException {
		Collection<Purchase> purchases = this.service.findAllPurchases();
		Purchase purchase = purchases.iterator().next();
		
		double currentTotal = purchase.getTotal();
	 	purchase.setTotal(currentTotal + 1);
		this.service.savePurchase(purchase);
		
		purchases = this.service.findAllPurchases();
		purchase = purchases.iterator().next();
		
		assertThat(purchase.getTotal()).isEqualTo(currentTotal + 1);
	}

	@Test
	@Transactional
	public void shouldDeletePurchase() {
		Collection<Purchase> purchases = this.service.findAllPurchases();
		int size = purchases.size();
		Purchase purchase = purchases.iterator().next();
		
		this.service.deletePurchase(purchase);
		purchases = this.service.findAllPurchases();
		
		purchase = this.service.findPurchaseById(purchase.getId());
		
		assertThat(purchase).isNull();
		assertThat(purchases.size()).isEqualTo(size - 1);
	
	}
	
	/******************** SALE ********************/
	
	@Test
	@Transactional
	public void shouldThrowsExceptionWhenSaleHasNoProducts() {
		Sale sale= new Sale();
		sale.setDate(LocalDate.now());
		sale.setTotal(10.0);
		sale.setVat(4.0);
		
		Assertions.assertThrows(SaleWithoutProductsException.class, () -> {
			this.service.saveSale(sale);
		});
	}
	
	@Test
	@Transactional
	public void shouldThrowsExceptionWheSaleHasNoProductAmount() {
		Sale sale= new Sale();
		sale.setDate(LocalDate.now());
		sale.setTotal(10.0);
		sale.setVat(4.0);
		
		Product p = this.service.findProductById(1);
		
		Assertions.assertThrows(NoProductSaleAmountException.class, () -> {
			ProductSale productSale = new ProductSale();
			productSale.setPrice(1.0);
			productSale.setAmount(0);
			productSale.setSale(sale);
			productSale.setProduct(p);
			
			sale.setProductSales(new HashSet<>());
			sale.getProductSales().add(productSale);
			
			this.service.saveSale(sale);
		});
	}

	@Test
	@Transactional
	public void shouldThrowsExceptionWhenProductOutOfStock() {
		Sale sale= new Sale();
		sale.setDate(LocalDate.now());
		sale.setTotal(10.0);
		sale.setVat(4.0);
		
		Product p = this.service.findProductById(1);
		
		Assertions.assertThrows(OutOfStockException.class, () -> {
			ProductSale productSale = new ProductSale();
			productSale.setPrice(1.0);
			productSale.setAmount(p.getStockage() + 1);
			productSale.setSale(sale);
			productSale.setProduct(p);
			
			sale.setProductSales(new HashSet<>());
			sale.getProductSales().add(productSale);
			
			this.service.saveSale(sale);
		});
	}
	
	@Test
	@Transactional
	public void shouldUpdateProductStockageOnSave() throws OutOfStockException, NoProductSaleAmountException, SaleWithoutProductsException {
		Collection<Product> products = this.service.findAllProducts();
		Product product = products.iterator().next();		
		int firstStock = product.getStockage();
		
		Sale sale= new Sale();
		sale.setDate(LocalDate.now());
		sale.setTotal(10.0);
		sale.setVat(4.0);
		
		ProductSale productSale = new ProductSale();
		productSale.setPrice(1.0);
		productSale.setAmount(1);
		productSale.setSale(sale);
		productSale.setProduct(product);
		
		sale.setProductSales(Sets.newHashSet());
		sale.getProductSales().add(productSale);
		
		this.service.saveSale(sale);

		products = this.service.findProductByName(product.getName());
		product = products.iterator().next();
		
		assertThat(product.getStockage()).isEqualTo(firstStock - 1);
		
		sale = this.service.findSaleById(sale.getId());
		productSale = sale.getProductSales().iterator().next();
		productSale.setAmount(3);
		this.service.saveSale(sale);

		products = this.service.findProductByName(product.getName());
		Iterator<Product> itr = products.iterator();
		product = itr.next();
		
		assertThat(product.getStockage()).isEqualTo(firstStock - 3);
		
		Collection<Product> tmp = this.service.findProductByName("Batido");
		Product product2 = tmp.iterator().next();
		
		ProductSale productSale2 = new ProductSale();
		productSale2.setPrice(1.0);
		productSale2.setAmount(1);
		productSale2.setSale(sale);
		productSale2.setProduct(product2);
		
		sale.getProductSales().add(productSale2);
		
		this.service.saveSale(sale);
		
		productSale.setSale(null);
		this.service.deleteProductSale(productSale);
		
		products = this.service.findProductByName(product.getName());
		product = products.iterator().next();
		
		assertThat(product.getStockage()).isEqualTo(firstStock);
	}
	
	@Test
	@Transactional
	public void shouldInsertSale() throws OutOfStockException, NoProductSaleAmountException, SaleWithoutProductsException {
		Collection<Sale> sales = this.service.findAllSales();
		int found = sales.size();
		
		Iterator<Product> products = this.service.findAllProducts().iterator();
		Product product = products.next();
		
		Sale sale= new Sale();
		sale.setDate(LocalDate.now());
		sale.setTotal(10.0);
		sale.setVat(4.0);
		
		ProductSale productSale = new ProductSale();
		productSale.setPrice(1.0);
		productSale.setAmount(1);
		productSale.setSale(sale);
		productSale.setProduct(product);
		
		sale.setProductSales(Sets.newHashSet());
		sale.getProductSales().add(productSale);
		
		this.service.saveSale(sale);
		
		assertThat(sale.getId().longValue()).isNotEqualTo(0);
		
		sales = this.service.findAllSales();
		assertThat(sales.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	public void shouldUpdateSale() throws OutOfStockException, NoProductSaleAmountException, SaleWithoutProductsException {
		Collection<Sale> sales = this.service.findAllSales();
		Sale sale = sales.iterator().next();
		
		double currentTotal = sale.getTotal();
		sale.setTotal(currentTotal + 1);
		this.service.saveSale(sale);
		
		sales = this.service.findAllSales();
		sale = sales.iterator().next();
		
		assertThat(sale.getTotal()).isEqualTo(currentTotal + 1);
	}

	@Test
	@Transactional
	public void shouldDeleteSale() {
		Collection<Sale> sales = this.service.findAllSales();
		int size = sales.size();
		Sale sale = sales.iterator().next();
		
		this.service.deleteSale(sale);
		sales = this.service.findAllSales();
		
		sale = this.service.findSaleById(sale.getId());
		
		assertThat(sale).isNull();
		assertThat(sales.size()).isEqualTo(size - 1);
	
	}
	
}
