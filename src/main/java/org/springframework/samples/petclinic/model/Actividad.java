package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Actividad extends BaseEntity{

	@NotBlank
	String nombre;
	
	@NotBlank
	String descripcion;
}
