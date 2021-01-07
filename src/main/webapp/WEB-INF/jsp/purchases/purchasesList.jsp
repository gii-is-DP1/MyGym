<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/purchases/new" var="addPurchase"></spring:url>

<petclinic:layout pageName="purchases">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Purchases</h2>
	   	<c:if test="${not empty purchases}">
	   		<a href="${addPurchase}" class="btn btn-blue btn-md right">Create</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty purchases}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">¡No purchases!</h4>
		        <!--Text-->
		        <p class="card-text">Please create a purchase.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
		        <a href="${addPurchase}" class="btn btn-blue btn-md">Create</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${purchases}" var="purchase">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${purchase.date}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${purchase.total}"/></p>
			       

				    <spring:url value="purchases/{purchaseId}/edit" var="editUrl">
				        <spring:param name="purchaseId" value="${purchase.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit</a>
				    
				    <spring:url value="purchases/{purchaseId}/delete" var="deleteUrl">
				        <spring:param name="purchaseId" value="${purchase.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>