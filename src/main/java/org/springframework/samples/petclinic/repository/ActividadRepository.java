package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Actividad;

public interface ActividadRepository extends CrudRepository<Actividad, Integer>{

}
