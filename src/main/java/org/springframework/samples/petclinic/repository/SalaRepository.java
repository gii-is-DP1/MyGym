package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Sala;

public interface SalaRepository extends CrudRepository<Sala, Integer>{
	
	@Query("SELECT sala FROM Sala sala  WHERE sala.id =:id")
	public Sala findById(@Param("id") int id);

}
