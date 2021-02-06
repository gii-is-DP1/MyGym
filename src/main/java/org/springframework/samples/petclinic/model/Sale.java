package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlElement;

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
	

	protected Set<Product> getProductsInternal() {
		if (this.products == null) {
			this.products = new HashSet<>();
		}
		return this.products;
	}

	protected void setProductsInternal(Set<Product> productsList) {
		this.products = productsList;
	}

	@XmlElement
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>(getProductsInternal());
		PropertyComparator.sort(products, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(products);
	}

	public int getProductsSize() {
		return getProductsInternal().size();
	}

	public void addProduct(Product product) {
		getProductsInternal().add(product);
	}
	
	public void removeProduct(Product product) {
		getProductsInternal().remove(product);
	}

}
