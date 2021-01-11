package org.springframework.samples.petclinic.web;

import java.util.List;

import org.springframework.samples.petclinic.model.Workout;
import org.springframework.samples.petclinic.model.WorkoutTraining;

import lombok.Data;

@Data
public class WorkoutForm {
	
	private Workout workout;
	private List<WorkoutTraining> workoutTrainings;
	
	public WorkoutForm(Workout workout, List<WorkoutTraining> workoutTrainings) {
		this.workout = workout;
		this.workoutTrainings = workoutTrainings;
	}
	
}
