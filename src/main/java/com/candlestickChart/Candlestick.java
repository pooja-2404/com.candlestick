package com.candlestickChart;

import org.jfree.data.xy.DefaultHighLowDataset;
import java.util.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import JsonUtils.java;
@WebServlet("/candlestick")
public class Candlestick extends HttpServlet {
	Connection conn;
	Date[] datear;
	double[] highar, lowar, openar, closear, volumear;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		conn = (Connection) getServletContext().getAttribute("dbConn");
		if (conn != null) {
			System.err.println("connected");
		}
		String startDate = null, endDate = null;
		try {
			if (request.getParameter("today") != null) {
				String date1 = request.getParameter("today");
				String date2 = request.getParameter("todayDate");
				SimpleDateFormat inputDateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
				 Date date11 = inputDateFormat.parse(date1);
				 Date date22 = inputDateFormat.parse(date2);
				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				startDate = outputDateFormat.format(date11);
				endDate = outputDateFormat.format(date22);
				
			} else {
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime tenDaysAgo = now.minusDays(10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
				String formatDate = now.format(formatter);
				String formatDate2 = tenDaysAgo.format(formatter);
				SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMM dd, yyyy");
				Date date1 = inputDateFormat.parse(formatDate);
				Date date2 = inputDateFormat.parse(formatDate2);
				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				endDate = outputDateFormat.format(date1);
				startDate = outputDateFormat.format(date2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DefaultHighLowDataset dataset = createDataset(startDate, endDate);
		String jsonData = JsonUtils.convertDatasetToJSON(dataset);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonData);

	}

	private DefaultHighLowDataset createDataset(String startDate, String endDate) {
		ArrayList<Date> date = new ArrayList<>();
		ArrayList<Double> high = new ArrayList<>();
		ArrayList<Double> low = new ArrayList<>();
		ArrayList<Double> open = new ArrayList<>();
		ArrayList<Double> close = new ArrayList<>();
		ArrayList<Double> volume = new ArrayList<>();
		String sql = "Select date,high,open,close,low from candlestick where date between '" + startDate + "' and '"
				+ endDate + "'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Date d1 = rs.getTimestamp("date");
				//Date d1 = rs.getDate("date").getTime();
				double h1 = rs.getDouble("high");
				double l1 = rs.getDouble("low");
				double o1 = rs.getDouble("open");
				double c1 = rs.getDouble("close");

				date.add(d1);
				high.add((double) h1);
				low.add((double) l1);
				open.add((double) o1);
				close.add((double) c1);
				volume.add((double) 0);
			}
			datear = date.toArray(new Date[0]);
			highar = high.stream().mapToDouble(Double::doubleValue).toArray();
			openar = open.stream().mapToDouble(Double::doubleValue).toArray();
			lowar = low.stream().mapToDouble(Double::doubleValue).toArray();
			closear = close.stream().mapToDouble(Double::doubleValue).toArray();
			volumear = volume.stream().mapToDouble(Double::doubleValue).toArray();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new DefaultHighLowDataset("Series", datear, highar, lowar, openar, closear, volumear);
	}
}