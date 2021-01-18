package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "workout")
public class Workout extends BaseEntity {
	
	@NotEmpty
	@Column
	String name;
	
	@Column
	String description;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@NotNull
	@Column(name = "start_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate startDate;
	
	@NotNull
	@Column(name = "end_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate endDate;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "workout")
	Set<WorkoutTraining> workoutTrainings;
	
	protected Set<WorkoutTraining> getWorkoutTrainingsInternal() {
		if (this.workoutTrainings == null)
			this.workoutTrainings = new TreeSet<>(Comparator.comparing(WorkoutTraining::getWeekDay));
		return this.workoutTrainings;
	}

	protected void setWorkoutTrainingsInternal(Set<WorkoutTraining> workoutTrainings) {
		this.workoutTrainings = workoutTrainings;
	}

	@XmlElement
	public Set<WorkoutTraining> getWorkoutTrainings() {
		return getWorkoutTrainingsInternal();
	}

	public int getWorkoutTrainingsSize() {
		return getWorkoutTrainingsInternal().size();
	}

	public void addWorkoutTraining(WorkoutTraining workoutTraining) {
		workoutTraining.setWorkout(this);
		getWorkoutTrainingsInternal().add(workoutTraining);
	}
	
	public void removeWorkoutTraining(WorkoutTraining workoutTraining) {
		getWorkoutTrainingsInternal().remove(workoutTraining);
	}
	
}
