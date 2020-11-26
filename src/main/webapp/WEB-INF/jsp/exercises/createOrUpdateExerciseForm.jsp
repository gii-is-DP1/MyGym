<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="exercises">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
            $(function () {
            	var $type = $('#type');
            	
            	var onTypeSelect = function onTypeSelect(opts) {
            		var value = $type.find('option:selected').attr('value');
            		
            		// clear and hide divs
            		$('div[data-type]').attr('hidden', '');
            		
            		if (!opts.skipClear) {
            			$('div[data-type]').find('input').val('');
            		}
            		
            		
            		if (typeof value === 'string') {
            			$('div[data-type=' + value + ']').removeAttr('hidden');
            		}
            	};  	
            	
            	$type.change(onTypeSelect);
            	$(window).on('load', onTypeSelect.bind(this, { skipClear: true }));
            });
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${exercise['new']}">Crear</c:if> Ejercicio
	    </h2>
	    <form:form modelAttribute="exercise" class="form-horizontal" id="add-exercise-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="First Name" name="name"/>
	            <petclinic:inputField label="Description" name="description"/>
	            <petclinic:imageField label="Imagen" name="image" />
	            <petclinic:selectField label="Tipo" name="type" names="${types}" size="1"/>
	            <div data-type="repetitive">
	            	<petclinic:inputField label="NumReps" name="numReps"/>
	            </div>
	            <div data-type="temporary">
	            	<petclinic:inputField label="Time" name="time"/>
	            </div>
	        </div>
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
                    <button class="btn btn-default mr-3" data-back-btn>Volver</button>
                    
	                <c:choose>
	                    <c:when test="${exercise['new']}">
	                        <button class="btn btn-default" type="submit">Crear</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Guardar cambios</button>
	                    </c:otherwise>
	                </c:choose>	                
	            </div>
	        </div>
	    </form:form>
    </jsp:body>
</petclinic:layout>








