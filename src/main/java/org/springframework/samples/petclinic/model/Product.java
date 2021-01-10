package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotNull
	private Integer stockage;

	@NotNull
	private Double price;
	
	@Lob 
	private String image;
}
