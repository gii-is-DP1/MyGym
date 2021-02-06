package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Data
@Entity
@Table(name = "user_type")
public class UserType {
	
	@Id
	String name;
}
