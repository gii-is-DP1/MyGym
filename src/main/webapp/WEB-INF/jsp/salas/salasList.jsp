<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/salas/new" var="addSala"></spring:url>

<petclinic:layout pageName="salas">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Salas</h2>
	   	<c:if test="${not empty salas}">
	   		<a href="${addSala}" class="btn btn-blue btn-md right">Crear</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty salas}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No hay salas!</h4>
		        <!--Text-->
		        <p class="card-text">Hemos detectado que aún no has creado ningna sala, pulsa en crear para añadir la primera.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
		        <a href="${addSala}" class="btn btn-blue btn-md">Crear</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${salas}" var="sala">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${sala.nombre}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${sala.aforo}"/></p>
			       

				    <spring:url value="salas/{salaId}/edit" var="editUrl">
				        <spring:param name="salaId" value="${sala.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
				    
				    <spring:url value="salas/{salaId}/delete" var="deleteUrl">
				        <spring:param name="salaId" value="${sala.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>