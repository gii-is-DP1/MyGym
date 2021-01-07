package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Sale;
import org.springframework.samples.petclinic.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Transactional
	public Iterable<Sale> findAll() {
		return saleRepository.findAll();
	}
	
	@Transactional
	public void save(Sale sale) {
		saleRepository.save(sale);
	}
	
	@Transactional
	public void delete(Sale sale) {
		saleRepository.delete(sale);
	}
	
	@Transactional
	public Sale findSaleById(int saleId) {
		return saleRepository.findById(saleId);
	}

}
