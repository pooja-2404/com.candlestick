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

#rightbutton {
	position: relative;
	top: 1.5vh;
	left: 90vw;
}

#leftbutton {
	position: relative;
	top: 102vh;
	left: 8vw;
}
</style>
<meta charset="ISO-8859-1">
<title>Candlestick Chart</title>

<script src="https://code.highcharts.com/stock/highstock.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(
			function() {
				var minDate, maxDate;
				var initialRangeMin, initialRangeMax;
				var candleChart;
				var series;
var chart;
				var selectedOption;

				function processData() {
					$.ajax({
						url : "candlestick2",
						method : "GET",
						data : $("#mainform").serialize(),

						success : function(response) {
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
									pointWidth : 10
								} ]
							});

							var today = new Date();
							initialRangeMax = new Date();
							initialRangeMax.setDate(today.getDate());
							initialRangeMin = new Date();
							initialRangeMin.setDate(today.getDate() - 10);
							minDate = initialRangeMin.getTime();
							maxDate = initialRangeMax.getTime();
							//console.log("extremes initial:"+new Date(minDate),new Date(maxDate));
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

							zoomType : 'xy'

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
						chart : {
							zoomType : 'x'
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
								valueDecimals : 2
							},
							pointWidth : 10
						} ]
					});
					var originalDate = new Date(response[0].x);
					var DaysLater = new Date(originalDate);
					DaysLater.setDate(originalDate.getDate() + 10);
					minDate = originalDate.getTime();
					maxDate = DaysLater.getTime();
					candleChart.xAxis[0].setExtremes(minDate, maxDate);
					

					return minDate;
				}

				$("#leftbutton").click(
						function() {
							//console.log("extremes:"+new Date(minDate),new Date(maxDate));
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

								},

							});
							
							if (selectedOption ==="RSI" || selectedOption ==="MACD" || selectedOption ==="Stochastic-Oscillator") {
								$.ajax({
									url : 'tradingindicator',
									type : 'POST',            
									data : {
										option : selectedOption
									},
									success : function(response) {
										response.forEach(function(dataPoint) {
											dataPoint.x = new Date(dataPoint.x);
										});
										  Chart = Highcharts.chart('linechart-container', {
											rangeSelector : {
												selected : 1
											},
											scrollbar : {
												enabled : true
											},
											series: [{
										        name: 'rsi',
										        data: response.relativestrength
										    }, {
										        name: 'price',
										        data: response.close
										    }]
										});

									},
								});
							}

						});
				$("#rightbutton").click(
						function() {
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
							
							if (selectedOption ==="RSI" || selectedOption ==="MACD" || selectedOption ==="Stochastic-Oscillator") {
								$.ajax({
									url : 'tradingindicator',
									type : 'POST',             
									data : {
										option : selectedOption
									},
									success : function(response) {
										response.forEach(function(dataPoint) {
											dataPoint.x = new Date(dataPoint.x);
										});
										  Chart = Highcharts.chart('linechart-container', {
											rangeSelector : {
												selected : 1
											},
											scrollbar : {
												enabled : true
											},
											series : [ {
											type:'line',
											name : selectedOption,
											data : response,
											tooltip : {
												valueDecimals :2
											},
											pointWidth: 10
										} ]
										});

									},
								});
							}

						});

				$("#indicators").change(function() {
					selectedOption = $(this).val();
					if (selectedOption !== "indicator") {
						$.ajax({
							url : 'tradingindicator',
							type : 'POST',            
							data : {

								option : selectedOption
							},
							success : function(response) {
								response.forEach(function(dataPoint) {
									dataPoint.x = new Date(dataPoint.x);
								});
								var labelsX = response.map(function (item) {
									
									return (new Date(item.x)).toLocaleString('en-US', { month: 'short', day: 'numeric' });

					                
					            });								
								var closeprice = response.map(function (item) {
					                return item.close;
					            });
								var relativestrength = response.map(function (item) {
					                return item.relativestrength;
					            });
								var overbought = response.map(function (item) {
					                return item.overbought;
					            });
								var undersell = response.map(function (item) {
					                return item.undersell;
					            });
								
								var percentK = response.map(function (item) {
					                return item.Kvalue;
					            });
								var percentD = response.map(function (item) {
					                return item.Dvalue;
					            });
								var signalLine = response.map(function (item) {
					                return item.signalline;
					            });
								var macdLine = response.map(function (item) {
					                return item.macdline;
					            });
								
								 Chart = Highcharts.chart('linechart-container', {
									rangeSelector : {
										selected : 1
									},
									scrollbar : {
										enabled : true
									},
									title:{
										text:selectedOption
									},
									xAxis: {
								        categories:labelsX,								       
								    },
								    chart:{
								    	zoomType:'x'
								    },
								    
									series: [{
								        name: 'rsi',
								        data: relativestrength
								    }, {
								        name: 'price',
								        data: closeprice
								    },{
								        name: 'overbought',
								        data: overbought
								    },{
								        name: 'undersell',
								        data: undersell
								    },{
								        name: 'MACD',
								        data: macdLine
								    },{
								        name: 'signal',
								        data: signalLine
								    },{
								        name: '%K',
								        data: percentK
								    },{
								        name: '%D',
								        data: percentD
								    }]
								});

							},
						});
					}
				});

				processData();

			});
</script>
</head>
<body>
	<div>
		<button id="leftbutton"><<</button>

	</div>
	<div id="chart-container">
		
	</div>
	<div id="linechart-container"></div>
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
	<label for="Select-option">Select Option...</label>
	<select id="indicators" name="indicators">
		<option value="indicator" selected>Indicators...</option>
		<option value="RSI">RSI</option>
		<option value="MACD">MACD</option>
		<option value="Stochastic-Oscillator">Stochastic Oscillator</option>
	</select>
	<div id="result"></div>
</body>
</html>
