<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
	
	<jsp:attribute name="customScript">
		<script type="text/javascript">
			$(function() {
				var canvasSelector = "#weight-chart";
        		var memoriesURL = '/memories';
        		var chart = null;
				
				function searchUserMemories(filtersP, done) {
        			var filters = typeof filtersP === 'object' && filtersP != null ? filtersP : {};
        			
					var url = memoriesURL + '?' + Object.keys(filters)
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
				
				function createChart(memories) {
					var $canvas = $(canvasSelector);
					var ctx = $canvas.get(0).getContext('2d');
					ctx.canvas.width = 1000;
					ctx.canvas.height = 300;
					
					chart = new Chart(ctx, {
					    type: 'line',
					    data: memoriesToChartData(memories),
					    options: {
							scales: {
								xAxes: [{
									type: 'time',
									distribution: 'series',
									offset: true,
									ticks: {
										major: {
											enabled: true,
											fontStyle: 'bold'
										},
										source: 'data',
										maxRotation: 0,
										sampleSize: 100
									},
									afterBuildTicks: function(scale, ticks) {
										var majorUnit = scale._majorUnit;
										var firstTick = ticks[0];
										var i, ilen, val, tick, currMajor, lastMajor;

										firstTick.major = true;
										lastMajor = firstTick.value;

										for (i = 1, ilen = ticks.length; i < ilen; i++) {
											tick = ticks[i];
											val = tick.value;
											tick.major = val !== lastMajor;
											lastMajor = currMajor;
										}
										return ticks;
									}
								}],
								yAxes: [{
									gridLines: {
										drawBorder: false
									},
									scaleLabel: {
										display: true,
										labelString: 'Peso en Kg'
									}
								}]
							},
							tooltips: {
								intersect: false,
								mode: 'index',
								callbacks: {
									label: function(tooltipItem, myData) {
										var label = myData.datasets[tooltipItem.datasetIndex].label || '';
										if (label) {
											label += ': ';
										}
										label += parseFloat(tooltipItem.value).toFixed(2);
										label += 'kg';
										return label;
									}
								}
							}
						}
					});
				}
				
				function memoriesToChartData(memories) {					
					var dataset = {
						label: 'Evolución del peso',
						data: []
					};
					
					for (memory of memories) {
						if (typeof memory.weight !== 'number') {
							continue;
						}
						var date = new Date(memory.date);						
						dataset.data.push({
							t: date,
							y: memory.weight,
						});
					}
					
					return {
						labels: [],
						datasets: [dataset]
					};
				}
				
				searchUserMemories(null, function buildChart(result) {
					createChart(result ? result.memoriesList : [])
				});
			});
		</script>
	</jsp:attribute>
	
	<jsp:body>
	    <h2><fmt:message key="welcome"/></h2>
	    <div class="row">
	        <div class="col-md-12">
	            <spring:url value="/resources/images/logoPNG_3.png" htmlEscape="true" var="logoImage"/>
	            <img class="img-responsive" src="${logoImage}"/>
	        </div>
	        <canvas id="weight-chart"></canvas>
    	</div>
    </jsp:body>
</petclinic:layout>
