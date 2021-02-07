package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Data
@Entity
public class Sala extends AuditableEntity {

	@NotBlank
	String nombre;
	
	@Min(1)
	Integer aforo;
}
