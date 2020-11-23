<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="exercises">
    <h2>
        <c:if test="${exercise['new']}">Crear</c:if> Ejercicio
    </h2>
    <form:form modelAttribute="exercise" class="form-horizontal" id="add-exercise-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="name"/>
            <petclinic:inputField label="Description" name="description"/>
            <petclinic:inputField label="NumReps" name="numReps"/>
            <petclinic:inputField label="Time" name="time"/>
            <petclinic:selectField label="Tipo" name="type" names="${types}" size="1"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${exercise['new']}">
                        <button class="btn btn-default" type="submit">Crear</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Guardar cambios</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>








