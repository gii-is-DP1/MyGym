package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Audited
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditableEntity {
	
	@Column(unique = true)
	String username;
	
	String password;
	
	boolean enabled;
	
	@NotBlank
	String nombre;
	String apellidos;
	String email;
	@NotBlank
	String dni;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate fecha_nacimiento;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fee")
	private Fee fee;
	
	@ManyToOne
	@JoinColumn(name = "type")
	private UserType type;
	
	@Transient
	public String getCompleteName() {
		return this.nombre + " " + this.apellidos;
	}
}
