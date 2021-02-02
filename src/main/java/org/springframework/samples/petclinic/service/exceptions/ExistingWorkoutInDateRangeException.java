/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.service.exceptions;

import java.time.LocalDate;

/**
 *
 * @author japarejo
 */
public class ExistingWorkoutInDateRangeException extends Exception{
    
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ExistingWorkoutInDateRangeException() {
		// TODO Auto-generated constructor stub
	}
	
	public ExistingWorkoutInDateRangeException(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}
}
