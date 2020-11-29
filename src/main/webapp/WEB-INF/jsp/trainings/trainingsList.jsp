<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/trainings/new" var="addTraining"></spring:url>

<petclinic:layout pageName="trainings">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Entrenamientos</h2>
	   	<c:if test="${not empty selections}">
	   		<a href="${addTraining}" class="btn btn-blue btn-md right">Crear</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty selections}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No hay entrenamientos!</h4>
		        <!--Text-->
		        <p class="card-text">Hemos detectado que aún no has creado ningún entrenamiento, pulsa en crear para añadir el primero.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
	   			<a href="${addTraining}" class="btn btn-blue btn-md">Crear</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${selections}" var="training">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${training.name}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${training.description}"/></p>

				    <spring:url value="trainings/{trainingId}" var="detailUrl">
				        <spring:param name="trainingId" value="${training.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(detailUrl)}" class="btn btn-default">Ver</a>
				    
				    <spring:url value="trainings/{trainingId}/delete" var="deleteUrl">
				        <spring:param name="trainingId" value="${training.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>
