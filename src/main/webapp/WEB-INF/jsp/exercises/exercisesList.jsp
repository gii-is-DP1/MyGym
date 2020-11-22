<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/exercises/new" var="addExercise"></spring:url>

<petclinic:layout pageName="exercises">
    <h2>Ejercicios</h2>
    
    <c:if test="${empty selections}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No hay ejercicios!</h4>
		        <!--Text-->
		        <p class="card-text">Hemos detectado que aún no has creado ningún ejercicio, pulsa en crear para añadir el primero.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
		        <a href="${addExercise}" class="btn btn-blue btn-md">Crear</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${selections}" var="exercise">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card">
			
			      <!--Card image
			      <div class="view overlay">
			        <img class="card-img-top" src="https://mdbootstrap.com/img/Photos/Others/images/16.jpg"
			          alt="Card image cap">
			        <a href="#!">
			          <div class="mask rgba-white-slight"></div>
			        </a>
			      </div>-->
			
			      <!--Card content-->
			      <div class="card-body">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${exercise.name}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${exercise.description}"/></p>
			        <p class="card-text">
			        	<c:if test="${exercise.type.name == 'repetitive'}">
			        		<strong class="mr-2">Num reps:</strong>
			        		<c:out value="${exercise.numReps}"/>
			        	</c:if>
			        	<c:if test="${exercise.type.name == 'temporary'}">
			        		<strong class="mr-2">Time:</strong>
							<c:out value="${exercise.time}"/>
			        	</c:if>
			        </p>
			        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
			        

				    <spring:url value="exercises/{exerciseId}/edit" var="editUrl">
				        <spring:param name="exerciseId" value="${exercise.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
				    
				    <a href="#" class="btn btn-default">Eliminar</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>
