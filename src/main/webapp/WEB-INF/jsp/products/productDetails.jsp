<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">

    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${product.name}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${product.description}"/></td>
        </tr>
        <tr>
            <th>Stockage</th>
            <td><c:out value="${product.stockage}"/></td>
        </tr>
        <tr>
            <th>Price</th>
            <td><c:out value="${product.price}"/></td>
        </tr>
    </table>
    
    <spring:url value="/product" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Back</a>

    <spring:url value="{productId}/edit" var="editUrl">
        <spring:param name="productId" value="${product.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit</a>

</petclinic:layout>