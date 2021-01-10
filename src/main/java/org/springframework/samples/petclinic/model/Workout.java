package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "workout")
public class Workout extends BaseEntity {
	
	@Column
	String name;
	
	@Column
	String description;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@Column(name = "start_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate startDate;
	
	@Column(name = "end_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDate endDate;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "workout")
	List<WorkoutTraining> workoutTrainings = new ArrayList<>(0);
	
	protected List<WorkoutTraining> getWorkoutTrainingsInternal() {
		return this.workoutTrainings;
	}

	protected void setWorkoutTrainingsInternal(List<WorkoutTraining> workoutTrainings) {
		this.workoutTrainings = workoutTrainings;
	}
	
	public void setWorkoutTrainings(List<WorkoutTraining> workoutTrainings) {
		PropertyComparator.sort(workoutTrainings, new MutableSortDefinition("weekDay", true, true));
		this.workoutTrainings = workoutTrainings;
	}

	@XmlElement
	public List<WorkoutTraining> getWorkoutTrainings() {
		List<WorkoutTraining> workoutTrainings = new ArrayList<>(getWorkoutTrainingsInternal());
		PropertyComparator.sort(workoutTrainings, new MutableSortDefinition("weekDay", true, true));
		// return Collections.unmodifiableList(workoutTrainings);
		return workoutTrainings;
	}

	public int getWorkoutTrainingsSize() {
		return getWorkoutTrainingsInternal().size();
	}

	public void addWorkoutTraining(WorkoutTraining workoutTrainings) {
		getWorkoutTrainingsInternal().add(workoutTrainings);
	}
	
	public void removeExercise(WorkoutTraining workoutTraining) {
		getWorkoutTrainingsInternal().remove(workoutTraining);
	}
	
}
