package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "sale")
public class Sale extends BaseEntity{
	
	private Date date;
	
	@NotBlank
	private Double total;
	
	@NotBlank
	private Double iva;
	
	@NotBlank
	private Integer quantity;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product")
	private Product product;

}
