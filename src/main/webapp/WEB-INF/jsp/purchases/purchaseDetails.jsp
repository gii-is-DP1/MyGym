<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="purchaseDetails">

    <h2>Información de la compra</h2>


    <table class="table table-striped">
        <tr>
            <th>Fecha compra</th>
            <td><b><c:out value="${purchase.date}"/></b></td>
        </tr>
        <tr>
            <th>IVA</th>
            <td><c:out value="${purchase.vat}"/></td>
        </tr>
    </table>
    
    <c:if test="${not empty purchase.productPurchases}">
	    <h3>Productos</h3>
	    <c:forEach var="productPurchase" items="${purchase.productPurchases}" varStatus="loop">
	    	<div class="row row-cols-1">
	  			<div class="col mb-4">
		          	<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
					
					    <!--Card image -->
					    <c:if test="${not empty productPurchase.product.image}">
						    <div class="view overlay p-1">
						    	<img class="card-img-top" src="${productPurchase.product.image}"
						          alt="Card image cap">
						        <a href="#!">
						        	<div class="mask rgba-white-slight"></div>
						        </a>
							</div>
					    </c:if>
					
					      <!--Card content-->
					    <div class="card-body col pl-4">
					
							<!--Title-->
							<h4 class="card-title"><c:out value="${productPurchase.product.name}"/></h4>
							<!--Text-->
							<p class="card-text"><c:out value="${productPurchase.product.description}"/></p>
							<!--Text-->
							<p class="card-text">cantidad:<c:out value="${productPurchase.amount}"/></p>
							<!--Text-->
							<p class="card-text">precio:<c:out value="${productPurchase.price}"/></p>
				   		</div>
		       		</div>
	       		</div>
	       	</div>
	    </c:forEach>
    </c:if>
    
    
	<sec:authorize access="!hasAnyAuthority('admin', 'trainer')">
	    <a href="#" data-back-btn class="btn btn-default">Volver</a>
    </sec:authorize>
    
    <sec:authorize access="hasAnyAuthority('admin', 'trainer')">
	    <spring:url value="/purchases" var="backUrl">
	    </spring:url>
	    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Volver</a>
    </sec:authorize>
	
	
	<sec:authorize access="hasAnyAuthority('admin', 'trainner')">
	
	    <spring:url value="{purchaseId}/edit" var="editUrl">
	        <spring:param name="purchaseId" value="${purchase.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
	
	    <spring:url value="{purchaseId}/delete" var="deleteUrl">
	        <spring:param name="purchaseId" value="${purchase.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar</a>
    
    </sec:authorize>

</petclinic:layout>
