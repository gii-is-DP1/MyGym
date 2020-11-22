package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Sala;
import org.springframework.samples.petclinic.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaService {

	@Autowired
	private SalaRepository salaRepository;
	
	@Transactional
	public Iterable<Sala> findAll() {
		return salaRepository.findAll();
	}
	
	@Transactional
	public void save(Sala sala) {
		salaRepository.save(sala);
	}
	
	@Transactional
	public void delete(Sala sala) {
		salaRepository.delete(sala);
	}
	
	@Transactional
	public Optional<Sala> findSalaById(int salaId) {
		return salaRepository.findById(salaId);
	}
}
