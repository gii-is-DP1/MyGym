<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/purchases/new" var="addPurchase"></spring:url>

<petclinic:layout pageName="purchases">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Purchases</h2>
	   	<c:if test="${not empty selections}">
	   		<a href="${addPurchase}" class="btn btn-blue btn-md right">Nueva</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty selections}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No se ha encontrado ninguna compra!</h4>
		       
	        	<p class="card-text">Por favor, realice una compra para aumentar el stockage de los productos.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
	   			<a href="${addPurchase}" class="btn btn-blue btn-md">Crear nueva</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${selections}" var="purchase">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title">Compra realizada el <c:out value="${purchase.date}"/></h4>
			        <!--Text-->
			        <p class="card-text">
						<b class="mr-2">Total:</b><c:out value="${purchase.total}" />€
						<b class="ml-4 mr-2">Número de productos:</b><c:out value="${purchase.productsSize}" />
					</p>

				    <spring:url value="/purchases/{purchaseId}" var="detailUrl">
				        <spring:param name="purchaseId" value="${purchase.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(detailUrl)}" class="btn btn-default">Detalles</a>
				    
				    <spring:url value="/purchases/{purchaseId}/delete" var="deleteUrl">
				        <spring:param name="purchaseId" value="${purchase.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>
