<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/purchases" var="purchasesList"></spring:url>

<petclinic:layout pageName="purchases">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
        	$(function() {
            	var $date = $("input[name=date]");
            	$date.datepicker({language:'es'});
            	
            	var rowTemplate = '<div class="card col mx-4 mt-4"><input type="hidden" name="productPurchases" value="#PRODUCT#;#AMOUNT#;#PRICE#" /><div class="card-body"><div class="card-title">#NAME#</div><p class="card-text">Amount: #AMOUNT#</p><p class="card-text">Price: #PRICE#</p><button class="btn btn-danger">Delete</button></div></div>';
				console.log('rowTemplate', rowTemplate);
				
				$('.delete-btn').on('click', function click(evt) {
					var $item = $(evt.target).closest('.card');
					$item.remove();
				})
				
				$('#add-btn').on('click', function addItem(evt) {
					evt.preventDefault();
					evt.stopPropagation();
					
					var amount = $('#amount').val();
					var price = $('#price').val();
					var productOption = $('#product > option:selected');

					console.log('amount', amount);
					console.log('price', price);					
					console.log('productOption', productOption);
					
					var newEl = rowTemplate
						.replace(new RegExp('#NAME#', 'ig'), productOption.text())
						.replace(new RegExp('#AMOUNT#', 'ig'), amount)
						.replace(new RegExp('#PRICE#', 'ig'), price)
						.replace(new RegExp('#PRODUCT#', 'ig'), productOption.attr('value'))
						
					$('#products').append(newEl)
					

					var amount = $('#amount').val('');
					var price = $('#price').val('');
					
					return false;
				})
				
        	});
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${purchase['new']}">New</c:if> Purchase
	    </h2>
	    
	    <form:form modelAttribute="purchase" class="form-horizontal" id="add-purchase-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Purchase date" name="date"/>
	            <petclinic:inputField label="VAT(%)" name="vat"/>
	            
	            <div class="row">
	            	<c:forEach items="${purchase.productPurchases}" var="productPurchase">
	            		<div class="card col mx-4 mt-4">
	            			<input type="hidden" name="productPurchases" value="${productPurchase.product.id};${productPurchase.amount};${productPurchase.price};${productPurchase.id}" />
	            			<div class="card-title"><c:out value="${productPurchase.product.name}" /></div>
	            			<div class="card-body">
	            				<p>Amount: <c:out value="${productPurchase.amount}" /></p>
	            				<p>Price: <c:out value="${productPurchase.price}" /></p>
	            				<button class="btn btn-danger" type="button">Delete</button>
	            			</div>
	            		</div>
	            	</c:forEach>
	            	<!--  <input type="hidden" name="productPurchases" value="2;3;2.75" /> -->
		        </div>
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
	        </div>
	        
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${purchase['new']}">
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








