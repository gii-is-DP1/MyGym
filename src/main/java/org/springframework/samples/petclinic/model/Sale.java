package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Getter;
import lombok.Setter;

@Audited
@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale extends AuditableEntity {
	
	private Date date;
	
	@NotBlank
	private Double total;
	
	@NotBlank
	private Double iva;
	
	@NotBlank
	private Integer quantity;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sale_product", joinColumns = @JoinColumn(name = "product_id"),
		inverseJoinColumns = @JoinColumn(name = "sale_id"))
	private Set<Product> products;
	

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;

	private Double total;

	private Double vat;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "sale")
	private Set<ProductSale> productSales;
	
	public int getProductsSize() {
		return productSales.size();
	}
}
