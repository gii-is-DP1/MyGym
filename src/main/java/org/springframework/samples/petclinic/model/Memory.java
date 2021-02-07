package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Audited
@Data
@Entity
@Table(name = "memory")
public class Memory extends AuditableEntity {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate date;

	String text;

	Double weight;

	@ManyToOne
	@JoinColumn(name = "training_id")
	private Training training;
	
}
