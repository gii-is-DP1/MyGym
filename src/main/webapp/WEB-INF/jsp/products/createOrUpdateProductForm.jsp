<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">
	<jsp:attribute name="customScript">
        
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${product['new']}">Create</c:if> Product
	    </h2>
	    <form:form modelAttribute="product" class="form-horizontal" id="add-product-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Name" name="name"/>
	            <petclinic:inputField label="Description" name="description"/>
	            <petclinic:inputField label="Stockage" name="stockage"/>
	            <petclinic:inputField label="Price" name="price"/>
	            
	        </div>
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${product['new']}">
	                        <button class="btn btn-default" type="submit">Create</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Update</button>
	                    </c:otherwise>
	                </c:choose>	          
	                
                    <a class="btn btn-default ml-2" data-back-btn>Back</a>      
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>