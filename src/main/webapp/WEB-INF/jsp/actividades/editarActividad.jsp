<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="actividades">
    
    <jsp:body>
	    <h2>
	        <c:if test="${actividad['new']}">Nueva </c:if> Actividad
	    </h2>
	    <form:form modelAttribute="actividad" class="form-horizontal" action="/actividades/save">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Nombre" name="nombre"/>
	            <petclinic:inputField label="Descripcion" name="descripcion"/>
	            
	        </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                <c:choose>
	                    <c:when test="${actividad['new']}">
	                        <button class="btn btn-default" type="submit">Añadir actividad</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Actualizar actividad</button>
	                    </c:otherwise>
	                </c:choose>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>
