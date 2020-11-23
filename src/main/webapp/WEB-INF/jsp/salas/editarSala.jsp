<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="salas">
    
    <jsp:body>
	    <h2>
	        <c:if test="${sala['new']}">Nueva </c:if> Sala
	    </h2>
	    <form:form modelAttribute="sala" class="form-horizontal" action="/salas/save">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Nombre" name="nombre"/>
	            <petclinic:inputField label="Aforo" name="aforo"/>
	            
	        </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                <c:choose>
	                    <c:when test="${sala['new']}">
	                        <button class="btn btn-default" type="submit">Añadir sala</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Actualizar sala</button>
	                    </c:otherwise>
	                </c:choose>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>
