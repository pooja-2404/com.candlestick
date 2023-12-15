<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<style>
body, html {
	height: 100%;
	margin: 0;
	padding: 0;
}

#chart-container {
	width: 100%;
	height: 97%;
}
#rightbutton{
position: relative;
top:1.5vh;
  left: 90vw;}
  #leftbutton{
  position: relative;
  top: 102vh;
  left: 8vw;}
</style>
<meta charset="ISO-8859-1">
<title>Candlestick Chart</title>

<script src="https://code.highcharts.com/stock/highstock.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function() {
		var minDate, maxDate;
		var initialRangeMin, initialRangeMax;
		var candleChart;
		var series;

		function processData() {
			$.ajax({
				url : "candlestick2",
				method : "GET",
				data : $("#mainform").serialize(),

				success : function(response) {
					console.log(response);

					response.forEach(function(dataPoint) {
						
						dataPoint.x = new Date(dataPoint.x);
					});
					
					candleChart = Highcharts.chart('chart-container', {
						rangeSelector : {
							selected : 1
						},
						title : {
							text : 'Candlestick Chart'
						},
						plotOptions : {
							candlestick : {
								color : 'red',
								upColor : 'green',
								lineColor : 'red',
								upLineColor : 'green'
							}
						},
						

						chart : {

							zoomType : 'x'
						},
						yAxis : {
							gridLineWidth : 1,
							title : {
								text : 'OHLC Values'
							},
							scrollbar : {
								enabled : true
							},
							crosshair : {
								snap : false,
								color : 'grey',
								dashStyle : 'dash',
								label : {
									enabled : true,
									format : '{value:.2f}'
								}
							}
						},
						legend : {
							enabled : true
						},
						scrollbar : {
							enabled : true
						},
						xAxis : {
							
							gridLineWidth : 1,
							title : {
								text : 'DateTime'
							},
							type : 'datetime',
							crosshair : {
								snap : false,
								color : 'grey',
								dashStyle : 'dash',
								label : {
									enabled : true
								}
							}

						},
						series : [ {
							type : 'candlestick',
							name : 'Candlestick',
							data : response,

							tooltip : {
								valueDecimals : 3
							},
							pointWidth: 10
						} ]
					});

					var today = new Date();
					initialRangeMax = new Date();
					initialRangeMax.setDate(today.getDate());
					initialRangeMin = new Date();
					initialRangeMin.setDate(today.getDate() - 10);
					minDate = initialRangeMin.getTime();
					maxDate = initialRangeMax.getTime();
					candleChart.xAxis[0].setExtremes(minDate, maxDate);
				},
			});
		}

		function loadingAdditionalData(response) {

			response.forEach(function(dataPoint) {
				dataPoint.x = new Date(dataPoint.x);
			});
			candleChart = Highcharts.chart('chart-container', {
				rangeSelector : {
					selected : 1
				},
				title : {
					text : 'Candlestick Chart'
				},
				plotOptions : {
					candlestick : {
						color : 'red',
						upColor : 'green',
						lineColor : 'red',
						upLineColor : 'green'
					}
				},

				chart : {

					zoomType : 'x'

				},
				yAxis : {
					gridLineWidth : 1,
					title : {
						text : 'OHLC Values'
					},
					scrollbar : {
						enabled : true
					},
					crosshair : {
						snap : false,
						color : 'grey',
						dashStyle : 'dash',
						label : {
							enabled : true,
							format : '{value:.2f}'
						}
					}
				},
				chart:{
					zoomType:'x'
				},
				
				scrollbar : {
					enabled : true
				},
				xAxis : {
					gridLineWidth : 1,
					title : {
						text : 'DateTime'
					},
					type : 'datetime',
					crosshair : {
						snap : false,
						color : 'grey',
						dashStyle : 'dash',
						label : {
							enabled : true
						}
					}

				},
				series : [ {
					type : 'candlestick',
					name : 'Candlestick',
					data : response,

					tooltip : {
						valueDecimals : 3
					},
					pointWidth: 10
				} ]
			});
			var originalDate = new Date(response[0].x);

			// Calculate the date 10 days later
			var DaysLater = new Date(originalDate);
			DaysLater.setDate(originalDate.getDate() + 10);
			minDate = originalDate.getTime();
			maxDate = DaysLater.getTime();
			candleChart.xAxis[0].setExtremes(minDate, maxDate);

			return minDate;
		}

		$("#leftbutton").click(function() {
			var todayDate = new Date();
			var DaysAgo = new Date(minDate);
			DaysAgo.setDate(DaysAgo.getDate() - 10);
			$.ajax({
				url : 'candlestick2',
				method : "GET",
				data : {
					today : new Date(DaysAgo),
					todayDate : todayDate
				},
				success : function(newdata) {
					 loadingAdditionalData(newdata);
					console.log(new Date(1683120600000));

				},

			});

		});
		$("#rightbutton").click(function() {
			var todayDate = new Date();
			var DaysAgo = new Date(todayDate);
			DaysAgo.setDate(DaysAgo.getDate() - 10);
			$.ajax({
				url : 'candlestick2',
				method : "GET",
				data : {
					today : new Date(DaysAgo),
					todayDate : todayDate
				},
				success : function(newdata) {
					 loadingAdditionalData(newdata);

				},

			});

		});
		processData();
	});
</script>
</head>
<body>
	<div>
		<button id="leftbutton"><<</button>

	</div>
	<div id="chart-container"></div>
	<div id="right">
		<button id="rightbutton">>></button>
	</div>
	<!--  <div>
<input type="button" id="fiveY" value="5Y">
<input type="button" id="oneY" value="1Y">
<input type="button" id="threeM" value="3M">
<input type="button" id="oneM" value="1M">
<input type="button" id="fiveD" value="5D">
<input type="button" id="oneD" value="1D">
</div>-->

</body>
</html>
