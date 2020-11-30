package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Data;

@Data
@Entity
@Table(name = "training")
public class Training extends BaseEntity {
	
	String name;
	
	String description;
	
	Integer weekDay;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "training_exercises", joinColumns = @JoinColumn(name = "training_id"),
		inverseJoinColumns = @JoinColumn(name = "exercise_id"))
	private Set<Exercise> exercises;
	
	protected Set<Exercise> getExercisesInternal() {
		if (this.exercises == null) {
			this.exercises = new HashSet<>();
		}
		return this.exercises;
	}

	protected void setExercisesInternal(Set<Exercise> exercisesList) {
		this.exercises = exercisesList;
	}

	@XmlElement
	public List<Exercise> getExercises() {
		List<Exercise> exercises = new ArrayList<>(getExercisesInternal());
		PropertyComparator.sort(exercises, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(exercises);
	}

	public int getExercisesSize() {
		return getExercisesInternal().size();
	}

	public void addExercise(Exercise exercise) {
		getExercisesInternal().add(exercise);
	}
	
	public void removeExercise(Exercise exercise) {
		getExercisesInternal().remove(exercise);
	}
	
}
