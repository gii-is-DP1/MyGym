package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductPurchase;
import org.springframework.samples.petclinic.model.ProductSale;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.repository.ProductPurchaseRepository;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.samples.petclinic.repository.ProductSaleRepository;
import org.springframework.samples.petclinic.repository.PurchaseRepository;
import org.springframework.samples.petclinic.repository.SaleRepository;
import org.springframework.samples.petclinic.service.exceptions.NoProductPurchaseAmountException;
import org.springframework.samples.petclinic.service.exceptions.NoProductSaleAmountException;
import org.springframework.samples.petclinic.service.exceptions.OutOfStockException;
import org.springframework.samples.petclinic.service.exceptions.PurchaseWithoutProductsException;
import org.springframework.samples.petclinic.service.exceptions.SaleWithoutProductsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private ProductPurchaseRepository productPurchaseRepository;
	
	@Autowired
	private ProductSaleRepository productSaleRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Transactional
	public Collection<Product> findAllProducts() {
		return productRepository.findAllByInactiveFalse();
	}
	
	@Transactional
	public void saveProduct(Product product) {
		if (product.isNew()) {
			product.setStockage(0);
		}
		product.setInactive(Boolean.FALSE);
		productRepository.save(product);
	}
	
	@Transactional
	public void deleteProduct(Product product) {
		product.setInactive(Boolean.TRUE);
		productRepository.save(product);
	}
	
	@Transactional
	public Product findProductById(int productId) {
		Product product = productRepository.findById(productId);
		if (Boolean.TRUE.equals(product.getInactive())) {
			return null;
		}
		return product;
	}
	
	@Transactional
	public Collection<Product> findProductByName(String name) {
		if (name == null) {
			return productRepository.findAllByInactiveFalse();
		}
		return productRepository.findByNameContainingAndInactiveFalse(name);
	}
	
	@Transactional
	public Collection<Purchase> findAllPurchases() {
		return purchaseRepository.findAll();
	}
	
	@Transactional
	public void deleteProductPurchase(ProductPurchase productPurchase) {
		Product product = productPurchase.getProduct();
		product.setStockage(product.getStockage() - productPurchase.getAmount());
		if (product.getStockage() < 0) {
			product.setStockage(0);
		}
		this.saveProduct(product);
		this.productPurchaseRepository.delete(productPurchase);
	}
	
	
	@Transactional
	public void savePurchase(Purchase purchase) throws PurchaseWithoutProductsException, NoProductPurchaseAmountException {
		if (purchase.getProductPurchases() == null || purchase.getProductPurchases().isEmpty()) {
			throw new PurchaseWithoutProductsException();
		}
		
		for (ProductPurchase productPurchase : purchase.getProductPurchases()) {
			Product product = productPurchase.getProduct();
			if (productPurchase.getAmount() == null || productPurchase.getAmount() == 0) {
				throw new NoProductPurchaseAmountException("invalid product amount for product " + product.getId(), product);				
			}
			
			if (!productPurchase.isNew()) {
				ProductPurchase old = this.productPurchaseRepository.findAll()
						.stream()
						.filter(pp -> pp.getId().equals(productPurchase.getId()))
						.findFirst().orElse(null);
				
				product.setStockage(product.getStockage() - old.getAmount());
				
			}
			
			product.setStockage(productPurchase.getAmount() + product.getStockage());
			
			if (product.getStockage() < 0) {
				product.setStockage(0);
			}
			
			this.saveProduct(product);
		}
		
		purchaseRepository.save(purchase);
	}
	
	@Transactional
	public void deletePurchase(Purchase purchase) {
		purchaseRepository.delete(purchase);
	}

	@Transactional
	public Sale findSaleById(int saleId) {
		return saleRepository.findById(saleId);
	}
	
	@Transactional
	public Collection<Sale> findAllSales() {
		return saleRepository.findAll();
	}
	
	@Transactional
	public void saveSale(Sale sale) throws OutOfStockException, NoProductSaleAmountException, SaleWithoutProductsException {
		if (sale.getProductSales() == null || sale.getProductSales().isEmpty()) {
			throw new SaleWithoutProductsException();
		}
		// RN5
		for (ProductSale productSale : sale.getProductSales()) {
			Product product = productSale.getProduct();
			if (productSale.getAmount() == null || productSale.getAmount() == 0) {
				throw new NoProductSaleAmountException("invalid product amount for product " + product.getId(), product);
			}
			
			if (!productSale.isNew()) {
				ProductSale old = this.productSaleRepository.findAll()
						.stream()
						.filter(ps -> ps.getId().equals(productSale.getId()))
						.findFirst().orElse(null);
				
				product.setStockage(product.getStockage() - old.getAmount());
			}
			
			if (productSale.getAmount() > product.getStockage()) {
				throw new OutOfStockException("there's no stockage of product " + product.getId() + " to cover the sale", product);
			}
			
			product.setStockage(product.getStockage() - productSale.getAmount());
			
			if (product.getStockage() < 0) {
				product.setStockage(0);
			}
			
			this.saveProduct(product);
			
		}
		saleRepository.save(sale);
	}
	
	@Transactional
	public void deleteProductSale(ProductSale productSale) {
		Product product = productSale.getProduct();
		product.setStockage(product.getStockage() + productSale.getAmount());
		this.saveProduct(product);
		this.productSaleRepository.delete(productSale);
	}
	
	@Transactional
	public void deleteSale(Sale sale) {
		saleRepository.delete(sale);
	}

	@Transactional
	public Purchase findPurchaseById(int purchaseId) {
		return purchaseRepository.findById(purchaseId);
	}
	
	/*@Transactional
	private ProductPurchase getCurrentProductPurchase(int id) {
		Session session = entityManager.unwrap(Session.class);
	    session.clear(); // clears session cache
		ProductPurchase currentDatabaseProductPurchase = session.get(ProductPurchase.class, id);
		return currentDatabaseProductPurchase;
	}
	
	@Transactional
	private ProductSale getCurrentProductSale(int id) {
		Session session = entityManager.unwrap(Session.class);
	    session.clear(); // clears session cache
	    ProductSale currentDatabaseProductSale = session.get(ProductSale.class, id);
		return currentDatabaseProductSale;
	}*/
	
}
