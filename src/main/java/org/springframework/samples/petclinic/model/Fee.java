package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Fee extends BaseEntity{

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate start_date;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate end_date;
	
	Double amount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rate")
	private Rate rate;
	
	@OneToOne(mappedBy = "fee")
	private User usuario;
}
