package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "workout_training")
public class WorkoutTraining extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "workout_id")
	Workout workout;
	
	@ManyToOne
	@JoinColumn(name = "training_id")
	Training training;
	
	@Column(name = "week_day")
	Integer weekDay;
	
}
