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
	height: 100%;
}
</style>
<meta charset="ISO-8859-1">
<title>Candlestick Chart</title>

<script src="https://code.highcharts.com/stock/highstock.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document)
			.ready(
					function() {
						var minDate, maxDate;
						var initialRangeMin, initialRangeMax;
						var candleChart;
						var series;

						function processData() {
							$
									.ajax({
										url : "candlestick",
										method : "GET",
										data : $("#mainform").serialize(),

										success : function(response) {
											//console.log(typeof (response));

											response
													.forEach(function(dataPoint) {
														dataPoint.x = new Date(
																dataPoint.x);
													});
											candleChart = Highcharts
													.chart(
															'chart-container',
															{
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

																	panning : {
																		enabled : true,
																		type : 'x',
																		panKey : 'shift'
																	}

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
																	},
																	events : {
																		afterSetExtremes : function(
																				e) {
																			if ((e.trigger === 'pan')
																					&& (e.min < minDate)) {
																				console
																						.log("left: "
																								+ new Date(
																										e.min)
																								+ "   "
																								+ new Date(
																										minDate));
																				loadMoreDataLeft(e.min);
																				console
																						.log("hello! I am back in left");
																				/*if ((e.trigger === 'pan')&& (e.min < minDate)) {
																					console.log("inside leftpan");
																					loadMoreDataLeft(e.min);
																					
																				}else if ((e.trigger === 'pan')&& (e.max > maxDate)) {
																					console.log("insisde left maxspan");
																					loadMoreDataRight(maxDate);
																				}*/
																			} else if ((e.trigger === 'pan')
																					&& (e.max > maxDate)) {
																				console
																						.log("right: "
																								+ new Date(
																										e.max)
																								+ "   "
																								+ new Date(
																										maxDate));
																				loadMoreDataRight(maxDate);
																				console
																						.log("hello! I am back in right");

																			}

																		}
																	}
																},
																series : [ {
																	type : 'candlestick',
																	name : 'Candlestick',
																	data : response,

																	tooltip : {
																		valueDecimals : 3
																	}
																} ]
															});

											var today = new Date();
											initialRangeMax = new Date();
											initialRangeMax.setDate(today
													.getDate());
											initialRangeMin = new Date();
											initialRangeMin.setDate(today
													.getDate() - 10);
											minDate = initialRangeMin.getTime();
											maxDate = initialRangeMax.getTime();
											//console.log("min main and : "+new Date(minDate)+"max main:  "+new Date(maxDate));
											candleChart.xAxis[0].setExtremes(
													minDate, maxDate);
										},
									});
						}
						// ajax call for loading additional data
						function loadMoreDataLeft(min) {

							console.log("leftdate:" + new Date(min));
							$.ajax({
								url : 'candlestick',
								method : "GET",
								data : {
									today : new Date(min)
								},
								success : function(newdata) {
									//console.log("calling left xx");
									minDate = xx(newdata);

									/*var tenDaysLater = new Date(minDate);
									tenDaysLater.setDate(tenDaysLater.getDate() + 10);

									console.log("Original Date:", new Date(minDate));
									console.log("Date 10 days later:", new Date(tenDaysLater));
									maxDate=tenDaysLater.getTime();
									if (candleChart) {
									    console.log("candleChart is defined");
									    
									    if (candleChart.xAxis && candleChart.xAxis[0]) {
									        console.log("xAxis[0] is defined");
									        candleChart.xAxis[0].setExtremes(minDate, maxDate, { trigger: 'afterSetExtremes' });
									    } else {
									        console.log("xAxis[0] is undefined");
									    }
									} else {
									    console.log("candleChart is undefined");
									}*/

									//console.log("inside leftscroll function");
									//console.log("mindate:"+new Date(minDate));

								},

							});

						}
						function loadMoreDataRight(min) {
							console.log("Rightdate:" + new Date(min));
							$
									.ajax({
										url : 'candlestick',
										method : "GET",
										data : {
											today : new Date(min)
										},
										success : function(newdata) {
											console.log("calling right xx");
											minDate = xx(newdata);
											console
													.log("inside rightscroll function");
											console.log("mindate:"
													+ new Date(minDate));

										},

									});

						}

						processData();
					});

	function xx(response) {

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

				panning : {
					enabled : true,
					type : 'x',
					panKey : 'shift'
				}

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
				}
			} ]
		});

		// Assuming response[0].x contains a timestamp
		var originalDate = new Date(response[0].x);

		// Calculate the date 10 days later
		var tenDaysLater = new Date(originalDate);
		tenDaysLater.setDate(originalDate.getDate() + 10);

		console.log("Original Date :", new Date(originalDate));
		console.log("Date 10 days later :", new Date(tenDaysLater));
		minDate = originalDate.getTime();
		maxDate = tenDaysLater.getTime();
		candleChart.xAxis[0].setExtremes(minDate, maxDate, {
			trigger : 'afterSetExtremes'
		});

		return minDate;
	}
</script>
</head>
<body>
	<div id="left">
		<input type="button" id="" value="left"  />
	</div>
	<div id="chart-container"></div>
	<div id="right">
		<input type="button" value="right" />
	</div>
  

</body>
</html>
