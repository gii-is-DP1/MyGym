package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Usuario extends BaseEntity{

	String nombre;
	String apellidos;
	String email;
	String dni;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	LocalDate fecha_nacimiento;
}
