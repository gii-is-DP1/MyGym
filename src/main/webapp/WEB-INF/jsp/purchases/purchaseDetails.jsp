<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="purchases">

    <h2>Purchase info</h2>


    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <td><b><c:out value="${purchase.date}"/></b></td>
        </tr>
        <tr>
            <th>Total</th>
            <td><c:out value="${purchase.total}"/></td>
        </tr>
        <tr>
            <th>IVA</th>
            <td><b><c:out value="${purchase.iva}"/></b></td>
        </tr>
        <tr>
            <th>Total</th>
            <td><c:out value="${purchase.quantity}"/></td>
        </tr>
        <tr>
            <th>Purchase Price</th>
            <td><c:out value="${purchase.purchasePrice}"/></td>
        </tr>
    </table>
    
    <spring:url value="/purchases" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Back</a>

    <spring:url value="{purchaseId}/edit" var="editUrl">
        <spring:param name="purchaseId" value="${purchase.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit</a>

</petclinic:layout>