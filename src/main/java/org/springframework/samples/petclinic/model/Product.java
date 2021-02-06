package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;

@Audited
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends AuditableEntity {
	
	@NotBlank
	private String name;
	
	private String description;
	
	@NotNull
	private Integer stockage;

	@NotNull
	private Double price;
	
	@Lob 
	private String image;
}
