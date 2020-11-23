package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.repository.ActividadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActividadService {

	@Autowired
	private ActividadRepository actividadRepository;
	
	@Transactional
	public Iterable<Actividad> findAll() {
		return actividadRepository.findAll();
	}
	
	@Transactional
	public void save(Actividad act) {
		actividadRepository.save(act);
	}
	
	@Transactional
	public void delete(Actividad act) {
		actividadRepository.delete(act);
	}
	
	@Transactional
	public Optional<Actividad> findActividadById(int actId) {
		return actividadRepository.findById(actId);
	}
}
