<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/sales" var="salesList"></spring:url>

<petclinic:layout pageName="sales">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
        	$(function() {
            	var $date = $("input[name=date]");
            	$date.datepicker({language:'es'});
            	
            	var rowTemplate = '<div class="col col-sm-6 col-lg-4 px-4 mt-4"><div class="card"><input type="hidden" name="productSales" value="#PRODUCT#;#AMOUNT#;#PRICE#" /><div class="card-title"><h5 class="px-3 pt-3">#NAME#</h5></div><div class="card-body"><p>Amount: #AMOUNT#</p><p>Price: #PRICE#</p><button class="btn btn-danger waves-effect waves-light">Delete</button></div></div></div>';
				
				$('#add-sale-form').on('click', '.btn-danger', function deleteItem(evt) {
					evt.preventDefault();
					var $item = $(evt.target).closest('.card').parent();
					$item.remove();
				});
				
				$('#add-btn').on('click', function addItem(evt) {
					evt.preventDefault();
					evt.stopPropagation();
					
					var amount = $('#amount').val();
					var price = $('#price').val();
					var productOption = $('#product > option:selected');

					var newEl = rowTemplate
						.replace(new RegExp('#NAME#', 'ig'), productOption.text())
						.replace(new RegExp('#AMOUNT#', 'ig'), amount)
						.replace(new RegExp('#PRICE#', 'ig'), price)
						.replace(new RegExp('#PRODUCT#', 'ig'), productOption.attr('value'))
						
					$('#products-container').append(newEl)
					

					var amount = $('#amount').val('');
					var price = $('#price').val('');
					
					return false;
				})
				
        	});
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${sale['new']}">New</c:if> Sale
	    </h2>
	    
	    <form:form modelAttribute="sale" class="form-horizontal" id="add-sale-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Sale date" name="date"/>
	            <petclinic:inputField label="VAT(%)" name="vat"/>
	            
		        <div id="products" class="form-group has-feedback">
		        	<div class="col col-sm-10 col-md-6 pl-0">
		        		<label class="control-label">Amount</label>
		        		<input class="form-control" type="number" id="amount">
		        	</div>
		        	<div class="col col-sm-10 col-md-6 pl-0">
		        		<label class="control-label">Price</label>
		        		<input class="form-control" type="number" id="price">
		        	</div>
		        	<div class="col col-sm-10 col-md-6 pl-0">
		        	<label class="control-label">Producto</label>
			        	<select id="product" class="form-control">
			        		<option value="" selected>Select product</option>
			        		<c:forEach items="${products}" var="product">
			        			<option value="${product.id}"><c:out value="${product.name}" /></option>
			        		</c:forEach>		        		
			        	</select>
		        	</div>
		        	<div>
		        		<button class="btn btn-default" id="add-btn">Add product</button>
		        	</div>
		        </div>
		        
	            <div id="products-container" class="row">
	            	<c:forEach items="${sale.productSales}" var="productSale">
	            		<div class="col col-sm-6 col-lg-4 px-4 mt-4">
		            		<div class="card">
		            			<input type="hidden" name="productSales" value="${productSale.product.id};${productSale.amount};${productSale.price};${productSale.id}" />
		            			<div class="card-title">
		            				<h5 class="px-3 pt-3"><c:out value="${productSale.product.name}" /></h5>
		            			</div>
		            			<div class="card-body">
		            				<p>Amount: <c:out value="${productSale.amount}" /></p>
		            				<p>Price: <c:out value="${productSale.price}" /></p>
		            				<button class="btn btn-danger waves-effect waves-light" type="button">Delete</button>
		            			</div>
		            		</div>
	            		</div>
	            	</c:forEach>
		        </div>
	        </div>
	        
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${sale['new']}">
	                        <button class="btn btn-default" type="submit">Create</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Save changes</button>
	                    </c:otherwise>
	                </c:choose>	          
	                
                    <a class="btn btn-default ml-2" data-back-btn>Back</a>      
	            </div>
	        </div>
	    </form:form>
			    
    </jsp:body>
</petclinic:layout>








