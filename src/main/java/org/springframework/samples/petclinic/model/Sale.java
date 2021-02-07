package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale extends BaseEntity{

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;

	private Double total;

	@NotNull
	private Double vat;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "sale")
	private Set<ProductSale> productSales;
	
	public int getProductsSize() {
		return productSales.size();
	}
}
