package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.transaction.Transactional;

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
	
	
	@Transactional
	public Collection<Product> findAllProducts() {
		return productRepository.findAll();
	}
	
	@Transactional
	public void saveProduct(Product product) {
		if (product.isNew()) {
			product.setStockage(0);
		}
		productRepository.save(product);
	}
	
	@Transactional
	public void deleteProduct(Product product) {
		
		// TODO si falla el borrado revisar que hacer con product purchase
		
		productRepository.delete(product);
	}
	
	@Transactional
	public Product findProductById(int productId) {
		return productRepository.findById(productId);
	}
	
	@Transactional
	public Collection<Product> findProductByName(String name) {
		if (name == null) {
			return productRepository.findAll();
		}
		return productRepository.findByName(name);
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
		if (purchase.getProductPurchases().isEmpty()) {
			throw new PurchaseWithoutProductsException();
		}
		
		for (ProductPurchase productPurchase : purchase.getProductPurchases()) {
			Product product = productPurchase.getProduct();
			if (productPurchase.getAmount() == null || productPurchase.getAmount() == 0) {
				throw new NoProductPurchaseAmountException("invalid product amount for product " + product.getId(), product);				
			}
			
			if (!productPurchase.isNew()) {
				ProductPurchase pp = this.productPurchaseRepository.findById(productPurchase.getId());
				product.setStockage(product.getStockage() - pp.getAmount());
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
		if (sale.getProductSales().isEmpty()) {
			throw new SaleWithoutProductsException();
		}
		// RN5
		for (ProductSale productSale : sale.getProductSales()) {
			Product product = productSale.getProduct();
			if (productSale.getAmount() == null || productSale.getAmount() == 0) {
				throw new NoProductSaleAmountException("invalid product amount for product " + product.getId(), product);
			} else if (productSale.getAmount() > product.getStockage()) {
				throw new OutOfStockException("there's no stockage of product " + product.getId() + " to cover the sale", product);
			}
			// check product price
		}
		saleRepository.save(sale);
	}
	
	@Transactional
	public void deleteSale(Sale sale) {
		saleRepository.delete(sale);
	}
	
	@Transactional
	public Purchase findPurchaseById(int purchaseId) {
		return purchaseRepository.findById(purchaseId);
	}
	
	/* @Transactional
	public void saveProductPurchase(Purchase purchase) {
		purchaseRepository.save(purchase);
	}
	
	@Transactional
	public void deleteProductPurchase(Purchase purchase) {
		purchaseRepository.delete(purchase);
	} */
	
}
