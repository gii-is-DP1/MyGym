<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/jsp-utils.tld" prefix="util" %>

<petclinic:layout pageName="assignWorkout">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
            $(function () {
            	var $form = $('form#assign-workout-form');
            	var $startDate = $("input[name=startDate]");
            	var $endDate = $("input[name=endDate]");
            	
            	$startDate.datepicker({language:'es'});
            	$endDate.datepicker({language:'es'});
            	
            	/*
            	 * parse DD/MM/YYYY to Javascript Date
            	 */
            	function dateFromString(date) {
            		if (typeof date !== 'string') {
            			return null;
            		}
            		var splitted = date.trim().split('/');
            		return new Date(splitted[2], splitted[1] - 1, splitted[0]);
            	}
            	
            	function setMessage($input, message) {
            		var $messageContainer = $input.closest('.form-group');
            		if (typeof message !== 'string' || message === '') {
            			$messageContainer.removeClass('has-error');
            			$messageContainer.find('span').remove();
            		} else {
            			$messageContainer.addClass('has-error');
            			$messageContainer.append('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
						$messageContainer.append('<span class="help-inline">' + message + '</span>');
            		}
            	}
            	
            	function validateStartDate() {
            		var date = dateFromString($startDate.val());
            		var endDate = dateFromString($endDate.val());
            		var invalid = endDate != null && date > endDate;
            		if (invalid) {
            			setMessage($startDate, 'La fecha de inicio no puede ser posterior a la fecha de fin');
            		} else {
            			setMessage($startDate, '');
            		}
            		return !invalid;
            	}
            	
            	function validateEndDate() {
            		var date = dateFromString($endDate.val());
            		var startDate = dateFromString($startDate.val());
            		var invalid = startDate != null && date < startDate;
            		if (invalid) {
            			setMessage($endDate, 'La fecha de fin no puede ser anterior a la fecha de inicio');
            		} else {
            			setMessage($endDate, '');
            		}
            		return !invalid;
            	}
            	
            	$startDate.on('change', function() {
            		var valid = validateStartDate() && validateEndDate();
        			$form.find('button[type=submit]').prop('disabled', !valid);
            	});
            	
            	$endDate.on('change', function() {
            		var valid = validateEndDate() && validateStartDate();
            		$form.find('button[type=submit]').prop('disabled', !valid);
            	});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        Asignar Rutina
	    </h2>
	    
	    <c:set var="cssGroup" value="form-horizontal ${status.error ? 'was-validated invalid' : '' }"/>
	    <form:form modelAttribute="workout" class="${cssGroup}" id="assign-workout-form">
	        <div class="form-group has-feedback">
	            <petclinic:selectField label="Usuario" name="user" names="${users}" size="1" itemLabel="completeName"/>
	            <petclinic:inputField label="Nombre" name="name"/>
	            <petclinic:inputField label="Descripción" name="description"/>
	            <petclinic:inputField label="Fecha de inicio" name="startDate"/>
	            <petclinic:inputField label="Fecha de fin" name="endDate"/>
	        </div>
	        
	        <div class="form-group has-feedback mt-4">
	        	<h4 class="mb-4">Entrenamientos para los días de la semana</h4>
	        	<div class="row">
	        		<div class="col">Lunes</div>
	        		<div class="col">Martes</div>
	        		<div class="col">Miércoles</div>
	        		<div class="col">Jueves</div>
	        		<div class="col">Viernes</div>
	        		<div class="col">Sábado</div>
	        	</div>
	        	<div class="row mt-2">
	        		<c:forEach items="${workoutTrainings}" var="workoutTraining" varStatus="loop">
	        		<div class="col">
	        			<select name="wt-training-${workoutTraining.weekDay}" class="form-control">
	        				<option value="">Descanso></option>
	        				<c:forEach items="${trainings}" var="training">
	        					<c:choose>
	        						<c:when test="${not empty workoutTraining.training and workoutTraining.training.id == training.id}">
	        							<option value="${training.id}" selected>${training.name}</option>
	        						</c:when>
	        						<c:otherwise>
	        							<option value="${training.id}">${training.name}</option>
	        						</c:otherwise>
	        					</c:choose>
	        				</c:forEach>
	        			</select>
	        		</div>
	        		</c:forEach>
	        	</div>
	        	<c:set var="nameHasBindError">
			        <form:errors path="workoutTrainings"/>
			    </c:set>
			    ${nameHasBindError}
	        </div>
	        
	        
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
                    <button class="btn btn-default" type="submit">Asignar</button>       
	                
                    <a class="btn btn-default ml-2" data-back-btn>Volver</a>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>








