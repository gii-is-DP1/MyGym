<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="exercises">
    <h2>Exercises</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Description</th>
            <th>Image</th>
            <th style="width: 120px">Num reps</th>
            <th>Time</th>
	
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="exercise">
            <tr>
                <td>
                    <spring:url value="/exercises/{exerciseId}" var="exerciseUrl">
                        <spring:param name="exerciseUrl" value="${exercise.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(exerciseUrl)}"><c:out value="${exercise.name}"/></a>
                </td>
                <td>
                    <c:out value="${exercise.description}"/>
                </td>
                <td>
                    <c:out value="${exercise.image}"/>
                </td>
                <td>
                    <c:out value="${exercise.numReps}"/>
                </td>
                <td>
                    <c:out value="${exercise.time}"/>
                </td>
<!--
                <td> 
                    <c:out value="${owner.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${owner.user.password}"/> 
                </td> 
-->
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
