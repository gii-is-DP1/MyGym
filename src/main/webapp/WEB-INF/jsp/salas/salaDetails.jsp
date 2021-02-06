<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="salas">

    <h2>Información de sala</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${sala.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Aforo</th>
            <td><c:out value="${sala.aforo}"/></td>
        </tr>
    </table>
    
    <spring:url value="/salas" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Volver</a>

    <spring:url value="{salaId}/edit" var="editUrl">
        <spring:param name="salaId" value="${sala.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>

</petclinic:layout>