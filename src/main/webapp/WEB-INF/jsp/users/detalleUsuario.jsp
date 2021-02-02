<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>

<petclinic:layout pageName="users">

    <h2>Ficha de usuario</h2>
    
    <table class="table table-striped">
        <tr>
            <th>Usuario</th>
            <td><c:out value="${user.username}"/></td>
        </tr>
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${user.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Apellidos</th>
            <td><c:out value="${user.apellidos}"/></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${user.email}"/></td>
        </tr>
        <tr>
            <th>Dni</th>
            <td><c:out value="${user.dni}"/></td>
        </tr>
        <tr>
            <th>Fecha de nacimiento</th>
            <td><c:out value="${user.fecha_nacimiento}"/></td>
        </tr>
        <tr>
            <th>Tipo</th>
            <td><c:out value="${user.type}"/></td>
        </tr>
    </table>
    
    <h3> Cuota</h3>
    <table class="table table-striped">
        <tr>
            <th>Fecha inicio</th>
            <td><b><c:out value="${user.fee.start_date}"/></b></td>
        </tr>
        <tr>
            <th>Fecha fin</th>
            <td><c:out value="${user.fee.end_date}"/></td>
        </tr>
        <tr>
            <th>Precio</th>
            <td><c:out value="${user.fee.amount}"/></td>
        </tr>
        <tr>
            <th>Tipo</th>
            <td><c:out value="${user.fee.rate}"/></td>
        </tr>
    </table>
    <spring:url value="/usuarios/{userId}/edit" var="editUrl">
        <spring:param name="userId" value="${user.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar usuario</a>
<!-- 
    <spring:url value="{userId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>
    <h2>Pets and Visits</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
 -->
</petclinic:layout>
