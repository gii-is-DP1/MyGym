<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/sales/new" var="addSale"></spring:url>

<petclinic:layout pageName="sales">
	
	<div class="row no-gutters justify-content-between align-items-center mb-2">
	   	<h2>Sales</h2>
	   	<c:if test="${not empty sales}">
	   		<a href="${addSale}" class="btn btn-blue btn-md right">Create</a>
	   	</c:if>
    </div>
    
    <c:if test="${empty sales}">
    	<div class="card mt-4">
    		<div class="card-body">
		        <!--Title-->
		        <h4 class="card-title">No sales!</h4>
		        <!--Text-->
		        <p class="card-text">Please create a sale.</p>
		        <!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
		        <a href="${addSale}" class="btn btn-blue btn-md">Create</a>
	    	</div>
    	</div>
    </c:if>
    
    <c:forEach items="${sales}" var="sale">
    	<div class="row row-cols-1">
  			<div class="col mb-4">
			    <div class="card row no-gutters flex-row flex-nowrap justify-content-between">
			
			
			      <!--Card content-->
			      <div class="card-body col pl-4">
			
			        <!--Title-->
			        <h4 class="card-title"><c:out value="${sale.date}"/></h4>
			        <!--Text-->
			        <p class="card-text"><c:out value="${sale.total}"/></p>
			       

				    <spring:url value="sales/{saleId}/edit" var="editUrl">
				        <spring:param name="saleId" value="${sale.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editr</a>
				    
				    <spring:url value="sales/{saleId}/delete" var="deleteUrl">
				        <spring:param name="saleId" value="${sale.id}"/>
				    </spring:url>
				    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete</a>
			
			      </div>
				</div>
		    </div>
	    </div>
    </c:forEach>
</petclinic:layout>