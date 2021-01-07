<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="sales">

    <h2>Sale's info</h2>


    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <td><b><c:out value="${sale.date}"/></b></td>
        </tr>
        <tr>
            <th>Total</th>
            <td><c:out value="${sale.total}"/></td>
        </tr>
        <tr>
            <th>IVA</th>
            <td><c:out value="${sale.iva}"/></td>
        </tr>
        <tr>
            <th>Quantity</th>
            <td><c:out value="${sale.quantity}"/></td>
        </tr>
    </table>
    
    <spring:url value="/sales" var="backUrl">
    </spring:url>
    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Back</a>

    <spring:url value="{saleId}/edit" var="editUrl">
        <spring:param name="saleId" value="${sale.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit</a>

</petclinic:layout>