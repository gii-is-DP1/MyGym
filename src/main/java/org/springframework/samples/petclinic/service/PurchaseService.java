package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Purchase;
import org.springframework.samples.petclinic.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {

	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Transactional
	public Iterable<Purchase> findAll() {
		return purchaseRepository.findAll();
	}
	
	@Transactional
	public void save(Purchase purchase) {
		purchaseRepository.save(purchase);
	}
	
	@Transactional
	public void delete(Purchase purchase) {
		purchaseRepository.delete(purchase);
	}
	
	@Transactional
	public Purchase findPurchaseById(int purchaseId) {
		return purchaseRepository.findById(purchaseId);
	}
	
}
