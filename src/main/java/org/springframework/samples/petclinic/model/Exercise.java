package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Data
@Entity
@Table(name = "exercise")
public class Exercise extends AuditableEntity {
	
	String name;
	
	String description;
	
	@Lob
	String image;

	@Column(name = "num_reps")
	Integer numReps;
	
	Integer time;
	
	@Column(name = "is_generic")
	Boolean isGeneric;
	
	@ManyToOne
	@JoinColumn(name = "type")
	private ExerciseType type;
	
}
