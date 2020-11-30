<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="salas">
	<jsp:attribute name="customScript">
        
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${sala['new']}">Crear</c:if> Sala
	    </h2>
	    <form:form modelAttribute="sala" class="form-horizontal" id="add-sala-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Nombre" name="nombre"/>
	            <petclinic:inputField label="Aforo" name="aforo"/>
	            
	        </div>
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${sala['new']}">
	                        <button class="btn btn-default" type="submit">Crear</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Guardar cambios</button>
	                    </c:otherwise>
	                </c:choose>	          
	                
                    <a class="btn btn-default ml-2" data-back-btn>Volver</a>      
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>
