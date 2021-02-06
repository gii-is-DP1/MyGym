<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<spring:url value="/products/new" var="addProduct"></spring:url>

<petclinic:layout pageName="products">

	<div
		class="row no-gutters justify-content-between align-items-center mb-2">
		<h2>Products</h2>
		<c:if test="${not empty products}">
			<a href="${addProduct}" class="btn btn-blue btn-md right">Create</a>
		</c:if>
	</div>

	<c:if test="${empty products}">
		<div class="card mt-4">
			<div class="card-body">
				<!--Title-->
				<h4 class="card-title">¡No products!</h4>
				<!--Text-->
				<p class="card-text">Please crate a product.</p>
				<!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
				<a href="${addProduct}" class="btn btn-blue btn-md">Create</a>
			</div>
		</div>
	</c:if>

	<c:forEach items="${products}" var="product">
		<div class="row row-cols-1">
			<div class="col mb-4">
				<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
				    <!--Card image -->
				    <c:if test="${not empty product.image}">
					    <div class="view overlay p-1">
					    	<img class="card-img-top" src="${product.image}"
					          alt="Card image cap">
					        <a href="#!">
					        	<div class="mask rgba-white-slight"></div>
					        </a>
						</div>
				    </c:if>
				
				      <!--Card content-->
				    <div class="card-body col pl-4">
				    
						<!--Title-->
						<h4 class="card-title"><c:out value="${product.name}" /></h4>
						<!--Text-->
						<p class="card-text">
							<b class="mr-2">Precio:</b><c:out value="${product.price}" />
						</p>
						<p class="card-text">
							<b class="mr-2">Stockage:</b><c:out value="${product.stockage}" />
						</p>


						<spring:url value="products/{productId}/edit" var="editUrl">
							<spring:param name="productId" value="${product.id}" />
						</spring:url>
						<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit</a>

						<spring:url value="products/{productId}/delete" var="deleteUrl">
							<spring:param name="productId" value="${product.id}" />
						</spring:url>
						<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete</a>
			   		</div>
				</div>
			</div>
		</div>
	</c:forEach>
</petclinic:layout>