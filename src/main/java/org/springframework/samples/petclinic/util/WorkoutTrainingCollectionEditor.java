package org.springframework.samples.petclinic.util;

import java.util.Collection;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.samples.petclinic.model.Exercise;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.WorkoutTraining;
import org.springframework.samples.petclinic.service.WorkoutService;

public class WorkoutTrainingCollectionEditor extends CustomCollectionEditor {

	private final WorkoutService workoutService;
	
	public WorkoutTrainingCollectionEditor(Class<? extends Collection> collectionType, WorkoutService workoutService) {
		super(collectionType);
		
		this.workoutService = workoutService;
	}
	
	@Override
	protected Object convertElement(Object element) {
		String value = (String) element;
		WorkoutTraining workoutTraining = null;
		
		String[] splitted = value.split(";");

		if (splitted.length >= 2) {
			workoutTraining = new WorkoutTraining();
			workoutTraining.setWeekDay(parseInt(splitted[0]));
			Integer trainingId = parseInt(splitted[1]);
	
			if (trainingId != null) {
				Training training = workoutService.findTrainingById(trainingId);
				if (training.getIsGeneric()) {
					training = cloneTraining(training);
				}
				workoutTraining.setTraining(training);
			}
		}
		
		return workoutTraining;
	}
	
	private Training cloneTraining(Training original) {
		Training training = new Training();
		BeanUtils.copyProperties(original, training, "id");
		training.setIsGeneric(Boolean.FALSE);
		
		training.getExercises().forEach(ex -> {
			Exercise exercise = new Exercise();
			BeanUtils.copyProperties(ex, exercise, "id");
			exercise.setIsGeneric(Boolean.FALSE);
			
			training.addExercise(exercise);
		});
		
		return training;
	}
	
	private Integer parseInt(String str) {
		if (str == null) {
			return null;
		}
		try {
			return new Integer(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
