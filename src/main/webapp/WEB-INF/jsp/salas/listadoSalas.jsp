<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="salas">
    <h2>Salas</h2>
	
    <table id="salasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">Aforo</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${salas}" var="sala">
            <tr>
                <td>
                    <c:out value="${sala.nombre}"/>
                </td>
                <td>
                    <c:out value="${sala.aforo}"/>
                </td>
               
                <td>
                   	<spring:url value="/salas/delete/{salaId}" var="salaUrl">
                        <spring:param name="salaId" value="${sala.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(salaUrl)}">Borrar</a>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/salas/new" var="addUrl">
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Crear sala</a>
</petclinic:layout>
