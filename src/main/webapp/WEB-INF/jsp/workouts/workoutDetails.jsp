<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/jsp-utils.tld" prefix="util" %>

<petclinic:layout pageName="workoutDetails">
    <jsp:body>
	    <h2 class="mb-2">
	       	<c:out value="${workout.name}" />
	    </h2>
	    <c:if test="${not empty workout.description}">
            <h5><c:out value="${workout.description}"/></h5>
        </c:if>
        
	    <div class="card mt-4">
		    <div class="card-body col">
				<p class="card-text">
					<b class="mr-2">Fecha de inicio:</b><c:out value="${workout.startDate}"/>
				</p>
				<p class="card-text">
		    		<b class="mr-2">Fecha de fin:</b><c:out value="${workout.endDate}"/>
				</p>
			</div>
	    </div>
	    
	    <div class="row no-gutters mt-4">
	    	<c:forEach items="${workout.workoutTrainings}" var="workoutTraining">
		    <div class="card ml-2 mb-2">
		    	<div class="card-title px-3 pt-3 mb-0">
		    		<h3 class="mb-0"><c:choose>
		    			<c:when test="${workoutTraining.weekDay == 1}">Lunes</c:when>
		    			<c:when test="${workoutTraining.weekDay == 2}">Martes</c:when>
		    			<c:when test="${workoutTraining.weekDay == 3}">Miércoles</c:when>
		    			<c:when test="${workoutTraining.weekDay == 4}">Jueves</c:when>
		    			<c:when test="${workoutTraining.weekDay == 5}">Viernes</c:when>
		    			<c:when test="${workoutTraining.weekDay == 6}">Sábado</c:when>
		    			<c:otherwise></c:otherwise>
		    		</c:choose></h3>
		    	</div>
			    <div class="card-body">
					<h5><c:out value="${workoutTraining.training.name}" /></h5>

				    <spring:url value="/trainings/{trainingId}" var="detailUrl">
				        <spring:param name="trainingId" value="${workoutTraining.training.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(detailUrl)}" class="btn btn-default">Ver</a>			
				</div>
		    </div>
		    </c:forEach>
	    </div>
	    
	    <div class="mt-4">
		    <spring:url value="/workouts?username={username}" var="backUrl">
		        <spring:param name="username" value="${workout.user.username}"/>		    
		    </spring:url>
		    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default ml-0">Volver</a>
		    
		    <c:if test="${assignWorkoutAllowed}">
			    <spring:url value="/workouts/{workoutId}/edit" var="editUrl">
			        <spring:param name="workoutId" value="${workout.id}"/>
			    </spring:url>
			    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
			    
			    <spring:url value="/workouts/{workoutId}/delete" var="deleteUrl">
			        <spring:param name="workoutId" value="${workout.id}"/>
			    </spring:url>
			    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar</a>
		    </c:if>
	    </div>
	    
    </jsp:body>
</petclinic:layout>








