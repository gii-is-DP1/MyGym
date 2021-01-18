package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Sala;

public interface ActividadRepository extends CrudRepository<Actividad, Integer>{

	@Query("SELECT actividad FROM Actividad actividad  WHERE actividad.id =:id")
	public Actividad findById(@Param("id") int id);
}
