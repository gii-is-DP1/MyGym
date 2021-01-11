<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>

<petclinic:layout pageName="users">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
            	$("input[name='fee.start_date'],#fecha_nacimiento,input[name='fee.end_date']").datepicker({language:'es'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2>
	        <c:if test="${user['new']}">Nuevo </c:if> Usuario
	    </h2>
	    <form:form modelAttribute="user" class="form-horizontal" >
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Usuario" name="username"/>
	            <petclinic:inputField label="Contraseña" name="password" type="password"/>
	            <petclinic:inputField label="Nombre" name="nombre"/>
	            <petclinic:inputField label="Apellidos" name="apellidos"/>
	            <petclinic:inputField label="Email" name="email"/>
	            <petclinic:inputField label="Dni" name="dni"/>
	            <petclinic:inputField label="Fecha de nacimiento" name="fecha_nacimiento"/>
	            <petclinic:selectField label="Tipo" name="type" names="${types}" size="1"/>
	        </div>
	   	    <h3>
		        Cuota
		    </h3>
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Fecha de inicio" name="fee.start_date"/>
	            <petclinic:inputField label="Fecha fin" name="fee.end_date"/>
	            <petclinic:inputField label="Precio" name="fee.amount"/>
	            <petclinic:selectField label="Tipo" name="fee.rate" names="${rates}" size="1"/>
	        </div>
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                <c:choose>
	                    <c:when test="${user['new']}">
	                        <button class="btn btn-default" type="submit">Añadir usuario</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Actualizar usuario</button>
	                    </c:otherwise>
	                </c:choose>
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>
