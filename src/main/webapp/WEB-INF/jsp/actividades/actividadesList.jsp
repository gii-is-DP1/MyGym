<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/actividades/new" var="addActividad"></spring:url>

<petclinic:layout pageName="actividades">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Actividades</h2>
	   	<c:if test="${not empty actividades}">
	   		<a href="${addActividad}" class="btn btn-blue btn-md right">Crear</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty actividades}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No hay actividades!</h4>
		        <!--Text-->
		        <p class="card-text">Hemos detectado que aún no has creado ninguna actividad, pulsa en crear para añadir la primera.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
		        <a href="${addActividad}" class="btn btn-blue btn-md">Crear</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${actividades}" var="actividad">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${actividad.nombre}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${actividad.descripcion}"/></p>
			       

				    <spring:url value="actividades/{actividadId}/edit" var="editUrl">
				        <spring:param name="actividadId" value="${actividad.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
				    
				    <spring:url value="actividades/{actividadId}/delete" var="deleteUrl">
				        <spring:param name="actividadId" value="${actividad.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>