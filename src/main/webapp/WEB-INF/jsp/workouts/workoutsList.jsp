<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/workouts/assign" var="assignWorkout"></spring:url>

<petclinic:layout pageName="workouts">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Rutinas</h2>
	   	<c:if test="${assignWorkoutsAllowed}">
	   		<a href="${assignWorkout}" class="btn btn-blue btn-md right">Asignar</a>
	   	</c:if>
    </div>
    
    <!--<c:if test="${empty selections}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <h4 class="card-title">¡Ninguna rutina asignada!</h4>
		        <p class="card-text">No tienes ninguna rutina asignada, habla con algún monitor para que te asigne una.</p>
	    	</div>
    	</div>
    </c:if>-->
    
    <c:if test="${viewUsersWorkoutsAllowed}">
    	<form:form method="get" modelAttribute="user" class="${cssGroup}" id="assign-workout-form">
	    	<div class="row no-gutters mt-4">
	    		<h4 class="mb-4">Ver las rutinas de un usuario</h4>
	    		<div class="d-flex flexrow flex-nowrap align-items-center" style="width:100%;">
		    		<form:select class="form-control" path="username" size="1">
		       			<form:option value="" label="Ninguno" />
		       			<form:options items="${users}" itemValue="username" itemLabel="completeName" />
		    		</form:select>
		    		<button type="submit" class="btn btn-blue btn-md" style="white-space: nowrap;">Ver</button>
	    		</div> 
	    	</div>
    	</form:form>
    </c:if>
    
    <div class="row no-gutters mt-4">
 
    	<div class="col">
    		<c:if test="${not empty current}">
    		<h4 class="mb-4">Rutina actual</h4>
    		
    		<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
    			<div class="card-body col pl-4">
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${current.name}" /></h4>
			        <!--Text-->
			        <c:if test="${current.description}">
			        <p class="card-text"><c:out value="${current.description}" /></p>
			        </c:if>
			        <p class="card-text text-nowrap">
		        		<strong>La empezaste el día:</strong>
		        		<c:out value="${current.startDate}" />
			        </p>
			        <p class="card-text text-nowrap">
		        		<strong>La terminas el día:</strong>
		        		<c:out value="${current.endDate}" />
			        </p>

				    <spring:url value="workouts/{workoutId}" var="detailUrl">
				        <spring:param name="workoutId" value="${current.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(detailUrl)}" class="btn btn-default">Detalles</a>
			    </div>
			    <div class="card-body col-4 pr-4">
			    	
			    </div>
    		</div>
    		</c:if>
    		
    		<c:if test="${empty current}">
   			<h6>No hemos encontrado ninguna rutina activa en este momento.</h6>
    		</c:if>
    	</div>
    </div>
    
    <div class="row no-gutters mt-4">
    
    	<c:if test="${not empty done}">
    	<div class="col col-12 col-md-6 pr-5">
    		<h4 class="mb-4">Rutinas realizadas</h4>
    		
    		
    		<c:forEach items="${done}" var="workout">
    		<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
    			<div class="card-body col pl-4">
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${workout.name}" /></h4>
			        <!--Text-->
			        <c:if test="${not empty workout.description}">
			        <p class="card-text"><c:out value="${workout.description}" /></p>
			        </c:if>
			        
			        <p class="card-text text-nowrap">
			        	<strong>Estuvo vigente en el periodo:</strong>
			        	<c:out value="${workout.startDate}" /> - <c:out value="${workout.endDate}" />
			        </p>

				    <spring:url value="workouts/{workoutId}" var="detailUrl">
				        <spring:param name="workoutId" value="${workout.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(detailUrl)}" class="btn btn-default">Detalles</a>
			    </div>
    		</div>
    		</c:forEach>
    	</div>
		</c:if>
    
    
    	<c:if test="${not empty next}">
    	<div class="col col-12 col-md-6">
    		<h4 class="mb-4">Próximas rutinas</h4>
    		
    		<c:forEach items="${next}" var="workout">
    		<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
    			<div class="card-body col pl-4">
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${workout.name}" /></h4>
			        <!--Text-->
			        <c:if test="${not empty workout.description}">
			        <p class="card-text"><c:out value="${workout.description}" /></p>
			        </c:if>
			        
			        <p class="card-text text-nowrap">
			        	<strong>Emperzarás con esta rutina el:</strong>
			        	<c:out value="${workout.startDate}" />
			        </p>
			    </div>
    		</div>
    		</c:forEach>
    	</div>
    	</c:if>
    </div>
    
</petclinic:layout>
