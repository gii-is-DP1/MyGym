<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="saleDetails">

    <h2>Información de la venta</h2>


    <table class="table table-striped">
        <tr>
            <th>Fecha venta</th>
            <td><b><c:out value="${sale.date}"/></b></td>
        </tr>
        <tr>
            <th>IVA</th>
            <td><c:out value="${sale.vat}"/>%</td>
        </tr>
        <tr>
            <th>Total</th>
            <td><c:out value="${sale.total}"/>€</td>
        </tr>
    </table>
    
    <c:if test="${not empty sale.productSales}">
	    <h3>Productos</h3>
	    <c:forEach var="productSale" items="${sale.productSales}" varStatus="loop">
	    	<div class="row row-cols-1">
	  			<div class="col mb-4">
		          	<div class="card row no-gutters flex-row flex-nowrap justify-content-between">
					
					    <!--Card image -->
					    <c:if test="${not empty productSale.product.image}">
						    <div class="view overlay p-1">
						    	<img class="card-img-top" src="${productSale.product.image}"
						          alt="Card image cap">
						        <a href="#!">
						        	<div class="mask rgba-white-slight"></div>
						        </a>
							</div>
					    </c:if>
					
					      <!--Card content-->
					    <div class="card-body col pl-4">
					
							<!--Title-->
							<h4 class="card-title"><c:out value="${productSale.product.name}"/></h4>
							
							<c:if test="${not empty productSale.product.description}">
								<!--Text-->
								<p class="card-text"><c:out value="${productSale.product.description}"/></p>
							</c:if>
							<!--Text-->
							<p class="card-text">
								<b class="mr-2">Precio de venta:</b><c:out value="${productSale.price}" />€
								<b class="ml-4 mr-2">Cantidad:</b><c:out value="${productSale.amount}" />
							</p>
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
	    <spring:url value="/sales" var="backUrl">
	    </spring:url>
	    <a href="${fn:escapeXml(backUrl)}" class="btn btn-default">Volver</a>
    </sec:authorize>
	
	
	<sec:authorize access="hasAnyAuthority('admin', 'trainner')">
	
	    <spring:url value="{saleId}/edit" var="editUrl">
	        <spring:param name="saleId" value="${sale.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar</a>
	
	    <spring:url value="{saleId}/delete" var="deleteUrl">
	        <spring:param name="saleId" value="${sale.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar</a>
    
    </sec:authorize>

</petclinic:layout>
