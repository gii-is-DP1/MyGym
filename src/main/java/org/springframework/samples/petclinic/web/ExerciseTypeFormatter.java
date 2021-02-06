/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.ExerciseType;
import org.springframework.samples.petclinic.service.WorkoutService;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Instructs Spring MVC on how to parse and print elements of type 'ExerciseType'. Starting
 * from Spring 3.0, Formatters have come as an improvement in comparison to legacy
 * PropertyEditors. See the following links for more details: - The Spring ref doc:
 * http://static.springsource.org/spring/docs/current/spring-framework-reference/html/validation.html#format-Formatter-SPI
 * - A nice blog entry from Gordon Dickens:
 * http://gordondickens.com/wordpress/2010/09/30/using-spring-3-0-custom-type-converter/
 * <p/>
 * Also see how the bean 'conversionService' has been declared inside
 * /WEB-INF/mvc-core-config.xml
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Michael Isvy
 */
@Slf4j
@Component
public class ExerciseTypeFormatter implements Formatter<ExerciseType> {

	private final WorkoutService workoutService;

	@Autowired
	public ExerciseTypeFormatter(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@Override
	public String print(ExerciseType workoutService, Locale locale) {
		return workoutService.getName();
	}

	@Override
	public ExerciseType parse(String text, Locale locale) throws ParseException {
		Collection<ExerciseType> findExerciseTypes = this.workoutService.findExerciseTypes();
		for (ExerciseType type : findExerciseTypes) {
			if (type.getName().equals(text)) {
				log.debug("parsed ExerciseType from String=" + text + " to ExerciseType[ID=" + type.getId() + "]");
				return type;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
