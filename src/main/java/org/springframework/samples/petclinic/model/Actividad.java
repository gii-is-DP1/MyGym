package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Data
@Entity
public class Actividad extends AuditableEntity{

	@NotBlank
	String nombre;
	
	@NotBlank
	String descripcion;
}
