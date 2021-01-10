package org.springframework.samples.petclinic.util;

import java.util.List;

import org.springframework.samples.petclinic.model.WorkoutTraining;

public class JSPUtils {

	public static Object trainingForDay(List list, int day) {
		System.out.println("getting training for day " + day);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<WorkoutTraining> workoutTrainings = (List<WorkoutTraining>) list;
		return workoutTrainings.stream().filter(wt -> wt.getWeekDay() == day).findFirst().orElse(null);	
	}
	
}
