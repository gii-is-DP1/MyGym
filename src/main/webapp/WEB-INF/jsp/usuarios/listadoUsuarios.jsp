<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <h2>Usuarios</h2>
	
    <table id="usersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">Apellidos</th>
            <th style="width: 200px;">Email</th>
            <th style="width: 120px">Dni</th>
            <th style="width: 120px">Fecha de nacimiento</th>
            <th style="width: 120px">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>
                    <c:out value="${user.nombre}"/>
                </td>
                <td>
                    <c:out value="${user.apellidos}"/>
                </td>
                <td>
                    <c:out value="${user.email}"/>
                </td>
                <td>
                    <c:out value="${user.dni}"/>
                </td>
				<td>
                    <c:out value="${user.fecha_nacimiento}"/>
                </td>
                <td>
                   	<spring:url value="/usuarios/delete/{userId}" var="userUrl">
                        <spring:param name="userId" value="${user.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(userUrl)}">Borrar</a>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/usuarios/new" var="addUrl">
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Crear usuario</a>
</petclinic:layout>
