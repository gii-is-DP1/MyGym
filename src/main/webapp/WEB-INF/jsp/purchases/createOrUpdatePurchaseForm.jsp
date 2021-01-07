<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="purchases">
	<jsp:attribute name="customScript">
        
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${purchase['new']}">Create</c:if> Purchase
	    </h2>
	    <form:form modelAttribute="purchase" class="form-horizontal" id="add-purchase-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Date" name="date"/>
	            <petclinic:inputField label="Total" name="total"/>
	            <petclinic:inputField label="IVA" name="iva"/>
	            <petclinic:inputField label="Quantity" name="quantity"/>
	            <petclinic:inputField label="Price" name="purchasePrice"/>
	            
	        </div>
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${total['new']}">
	                        <button class="btn btn-default" type="submit">Create</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Save</button>
	                    </c:otherwise>
	                </c:choose>	          
	                
                    <a class="btn btn-default ml-2" data-back-btn>Back</a>      
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>
