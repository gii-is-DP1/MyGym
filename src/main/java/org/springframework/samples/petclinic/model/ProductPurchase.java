package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_purchase")
public class ProductPurchase extends BaseEntity {

	private Integer amount;
	
	private Double price;

	@ManyToOne(cascade= CascadeType.REFRESH)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;
}
