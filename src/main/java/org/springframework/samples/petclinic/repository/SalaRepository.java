package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Sala;

public interface SalaRepository extends CrudRepository<Sala, Integer>{

}
