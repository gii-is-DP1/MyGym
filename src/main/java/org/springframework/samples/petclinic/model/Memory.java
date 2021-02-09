package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Audited
@Data
@Entity
@Table(name = "memory")
public class Memory extends AuditableEntity {

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate date;
	
	@NotEmpty
	String text;

	Double weight;

	@ManyToOne
	@JoinColumn(name = "training_id")
	private Training training;
	
}
