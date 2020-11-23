<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="actividades">
    <h2>Actividades</h2>
	
    <table id="actividadesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">Descripción</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${actividades}" var="actividad">
            <tr>
                <td>
                    <c:out value="${actividad.nombre}"/>
                </td>
                <td>
                    <c:out value="${actividad.descripcion}"/>
                </td>
               
                <td>
                   	<spring:url value="/actividades/delete/{actividadId}" var="actividadUrl">
                        <spring:param name="actividadId" value="${actividad.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(actividadUrl)}">Borrar</a>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/actividades/new" var="addUrl">
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Crear actividad</a>
</petclinic:layout>
