package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {
	
	@Column(unique = true)
	String username;
	
	String password;
	
	boolean enabled;
	
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
