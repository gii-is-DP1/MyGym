package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity{
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private Integer stockage;
	
	@NotBlank
	private Double price;
	
	/*Falta imagen
	 * @Lob 
	 * private String image;
	 */
}
