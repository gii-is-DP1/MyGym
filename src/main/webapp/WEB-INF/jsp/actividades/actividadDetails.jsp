<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="actividades">

    <h2>Información de actividad</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${actividad.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Descripción</th>
            <td><c:out value="${actividad.descripcion}"/></td>
        </tr>
    </table>
    
    <spring:url value="/actividades" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Volver</a>

    <spring:url value="{actividadId}/edit" var="editUrl">
        <spring:param name="actividadId" value="${actividad.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>

</petclinic:layout>