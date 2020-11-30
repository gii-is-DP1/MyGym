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
public class Usuario extends BaseEntity{

	String nombre;
	String apellidos;
	String email;
	String dni;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate fecha_nacimiento;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fee")
	private Fee fee;
	
	@ManyToOne
	@JoinColumn(name = "type")
	private UserType type;
}
