package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "exercise")
public class Exercise extends BaseEntity {
	
	String name;
	
	String description;
	
	@Lob
	String image;
	
	Integer numReps;
	
	Integer time;
	
	@ManyToOne
	@JoinColumn(name = "type")
	private ExerciseType type;
	
}
