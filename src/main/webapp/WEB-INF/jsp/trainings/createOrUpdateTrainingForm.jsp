<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<spring:url value="/exercises" var="exercisesList"></spring:url>

<petclinic:layout pageName="trainings">
	<jsp:attribute name="customScript">
        <script type="text/javascript">
        	$(function() {
        		var exercisesURL = '<c:out value="${exercisesList}"></c:out>';

        		var exercisesContainerSelector = '#exercises';
        		var modalSelector = '#modalAssignForm';
        		
    			var listItemTemplate = '<li class="list-group-item c-pointer fade">%name</li>';
    			
    			var exerciseTemplate = '<div class="col col-sm-6 col-lg-4 mt-2"><div class="card"><input class="form-control" name="exercisesList" type="hidden" value="%id"/><div class="card-body"><h4 class="card-title">%name</h4><p class="card-text">%description</p><a href="#" class="btn btn-primary">Eliminar</a></div></div></div>';
    			
    			var $form = $('#add-training-form');
        		var $modal = $(modalSelector);
        		var $resultsContainer = $modal.find('ul');
        		var $loader = $modal.find('li[data-loader]');
        		var $searchInput = $modal.find('#assignForm-search');
        		var $searchButton = $modal.find('#search-btn');
        		var $exercisesContainer = $form.find(exercisesContainerSelector);
        		
        		function searchExercises(filtersP, done) {
        			var filters = typeof filtersP === 'object' && filtersP != null ? filtersP : {};
        			
					var url = exercisesURL + '?' + Object.keys(filters)
						.filter(function f(key) {
							return typeof filters[key] === 'string' && filters[key].trim() !== '';
						})
						.map(function m(key) {
							return key.trim().toLowerCase() + '=' + filters[key].trim().toLowerCase();
						})
						.join('&');
						
        			$.ajax({
        			    beforeSend: function(request) {
        			        request.setRequestHeader("Content-Type", 'application/json;charset=utf-8');
        			    },
        			    dataType: "json",
        			    url: url,
        			    success: done
        			});
        		}
        		
        		function buildItem(exercise) {
        			return listItemTemplate
        				.replace('%name', exercise.name);
        		}
        		
        		function buildCard(exercise) {
        			return exerciseTemplate
        				.replace('%id', exercise.id)
        				.replace('%name', exercise.name)
    					.replace('%description', exercise.description);
        		}
        		
        		function toggleLoader() {
        			if (typeof $loader.attr('hidden') !== 'string') {
        				$loader.attr('hidden', '');
        			} else {
        				$loader.removeAttr('hidden');
        			}
        		}
        		
        		function clear() {
        			$resultsContainer.find('li:not(li[data-loader])').remove();
        		}
        		
        		function search() {
        			clear();
        			toggleLoader();
        			
        			searchExercises({
        				name: $searchInput.val()
        			}, function done(data) {
        				console.log('data', data);
        				if (data != null) {
            				(data.exerciseList || []).forEach(function each(exercise, idx) {
            					$resultsContainer.prepend(buildItem(exercise));
            					$resultsContainer.find('li:not(li[data-loader])').get(0).__exercise = exercise;
            					setTimeout(function() {
            						$($resultsContainer.find('li:not(li[data-loader])').get(idx)).addClass('show');
            					}, idx * 100);
            				});
        				}        				
        				
        				toggleLoader();
        			});        			
        		}
        		
        		function addExercise(exercise, done) {
        			$.ajax({
    					type: 'PUT',
        			    beforeSend: function(request) {
        			        request.setRequestHeader("Content-Type", 'application/json;charset=utf-8');
        			    },
        			    dataType: "json",
        			    url: './exercise/' + exercise.id,
        			    success: done
        			});
        		}
        		
        		$modal.on('show.bs.modal', search);
        		$searchButton.on('click', search);

        		$resultsContainer.on('click', 'li:not(li[data-loader])', function setItem() {
        			console.log('set item', this, this.__exercise);
        			if (typeof this.__exercise !== 'undefined') {
        				var exercise = JSON.parse(JSON.stringify(this.__exercise));
        				var $form = $(this).closest('form');
        				$form.attr('action', './addExercise/' + exercise.id);
        				$form.get(0).submit();
        				/* addExercise(exercise, function done() {
        					var exerciseCard = buildCard(texercise);
            				$exercisesContainer.append(exerciseCard);
        				});*/
        			}
        		})
        		
        	});
        </script>
    </jsp:attribute>
    <jsp:body>
	    <h2 class="mb-5">
	        <c:if test="${training['new']}">Crear</c:if> Entrenamiento
	    </h2>
	    <form:form modelAttribute="training" class="form-horizontal" id="add-training-form">
	        <div class="form-group has-feedback">
	            <petclinic:inputField label="Nombre" name="name"/>
	            <petclinic:inputField label="Descripción" name="description"/>
	        </div>
	        
	        <div class="form-group mt-5">
	            <div class="col pl-0 ml-0">
	                <c:choose>
	                    <c:when test="${training['new']}">
	                        <button class="btn btn-default" type="submit">Crear</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit">Guardar cambios</button>
	                    </c:otherwise>
	                </c:choose>	          
	                
                    <a class="btn btn-default ml-2" data-back-btn>Volver</a>      
	            </div>
	        </div>
	    </form:form>
	    
	    <c:if test="${not training['new']}">
	        <div class="form-group">
	        	<div class="col col-sm-10 col-md-6 pl-0 row no-gutters justify-content-between align-items-center">
	        		<label class="control-label mb-0">Ejercicios del entrenamiento</label>
				   	<a href="" data-toggle="modal" data-target="#modalAssignForm" class="btn btn-blue btn-md">Añadir</a>
			    </div>
		        
	           	<div id="exercises" class="row">
		            <c:forEach var="exercise" items="${training.exercises}" varStatus="loop">
		            	<div class="col col-sm-6 col-lg-4 mt-2">
				        	<div class="card">
								<input class="form-control" name="exercisesList" type="hidden" value="${exercise.id}"/>
				        		<div class="card-body">
								    <!-- Title -->
								    <h4 class="card-title"><c:out value="${exercise.name}"/></h4>
								    <!-- Text -->
								    <p class="card-text"><c:out value="${exercise.description}"/></p>
								    <!-- Button -->
								    
									<spring:url value="./deleteExercise/${exerciseId}" var="deleteExercise">
										<spring:param name="exerciseId" value="${exercise.id}"></spring:param>
									</spring:url>
					    			<form:form modelAttribute="exercise" action="./deleteExercise/${exercise.id}" class="form-horizontal" id="add-exercise-form">
					    				<input type="hidden" name="exerciseId" value="${exercise.id}">
									    <button type="submit" class="btn btn-primary">Eliminar</button>
									</form:form>
								</div>
				        	</div>
			        	</div>
		            </c:forEach>
	        	</div>
	        	
	            <c:if test="${empty training.exercises}">
		            <div class="col col-sm-10 col-md-6 pl-0 mt-2">
		            	<div class="card">
		            		<div class="card-body">
		            			<p class="card-text">Ningún ejercicio asignado</p>
		            		</div>
		            	</div>
	            	</div>
	            </c:if>
	        </div>
				    
			<div class="modal fade" id="modalAssignForm" tabindex="-1" role="dialog" aria-labelledby="modalAssignFormLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header text-center">
							<h4 class="modal-title w-100 font-weight-bold">Añadir ejercicio</h4>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body mx-3">
							<div class="md-form mb-5 d-flex flex-row justify-content-space-around align-items-center">
								<input type="text" id="assignForm-search" class="col form-control" placeholder="Texto de búsqueda...">
								<button id="search-btn" type="button" class="btn btn-outline-default waves-effect px-3 py-2 mt-0 mb-2">
									<i class="fas fa-search" aria-hidden="true"></i>
								</button>
							</div>
							
							
						    <form:form modelAttribute="training" class="form-horizontal" id="add-exercise-form">
							<ul class="list-group mdb-color darken-2 p-2" style="height: 250px;">
								<li data-loader hidden class="list-group-item">
									<div class="spinner-border mr-2" role="status">
										<span class="sr-only">Loading...</span>
									</div>
									Cargando...
								</li>
							</ul>
							</form:form>
						</div>
					</div>
				</div>
			</div>
        </c:if>
			    
    </jsp:body>
</petclinic:layout>








