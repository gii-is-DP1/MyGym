<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainingDetails">

    <h2>Información de entrenamiento</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${training.name}"/></b></td>
        </tr>
        <tr>
            <th>Descripción</th>
            <td><c:out value="${training.description}"/></td>
        </tr>
    </table>
    
    <c:if test="${not empty training.exercises}">
	    <h3>Ejercicios</h3>
	    <c:forEach var="exercise" items="${training.exercises}" varStatus="loop">
	    	<div class="row row-cols-1">
	  			<div class="col mb-4">
		          	<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
					
					    <!--Card image -->
					    <c:if test="${not empty exercise.image}">
						    <div class="view overlay p-1">
						    	<img class="card-img-top" src="${exercise.image}"
						          alt="Card image cap">
						        <a href="#!">
						        	<div class="mask rgba-white-slight"></div>
						        </a>
							</div>
					    </c:if>
					
					      <!--Card content-->
					    <div class="card-body col pl-4">
					
							<!--Title-->
							<h4 class="card-title"><c:out value="${exercise.name}"/></h4>
							<!--Text-->
							<p class="card-text"><c:out value="${exercise.description}"/></p>
							<p class="card-text">
								<c:if test="${exercise.type.name == 'repetitive' and not empty exercise.numReps}">
									<strong class="mr-2">Num reps:</strong>
									<c:out value="${exercise.numReps}"/>
								</c:if>
								<c:if test="${exercise.type.name == 'temporary' and not empty exercise.time}">
									<strong class="mr-2">Time:</strong>
									<c:out value="${exercise.time}"/>
								</c:if>
							</p>
				   		</div>
		       		</div>
	       		</div>
	       	</div>
	    </c:forEach>
    </c:if>
    
    <spring:url value="/trainings" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Volver</a>

    <spring:url value="{trainingId}/edit" var="editUrl">
        <spring:param name="trainingId" value="${training.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>

</petclinic:layout>
