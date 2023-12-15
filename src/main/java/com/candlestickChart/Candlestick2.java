package com.candlestickChart;

import org.jfree.data.xy.DefaultHighLowDataset;
import java.util.Date;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Calendar;

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

@WebServlet("/candlestick2")
public class Candlestick2 extends HttpServlet {
	Connection conn;
	Date[] datear;
	double[] highar, lowar, openar, closear, volumear;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		conn = (Connection) getServletContext().getAttribute("dbConn");
		
		Date startDate = null, endDate = null;
		try {
			if (request.getParameter("today") != null) {
				String date11 = request.getParameter("today");
				String date12 = request.getParameter("todayDate");
				SimpleDateFormat inputDateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
				startDate = inputDateFormat.parse(date11);
				endDate = inputDateFormat.parse(date12);
			} else {
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -10);
				Date tenDaysAgoDate = calendar.getTime();
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				String formattedDateago = dateFormat.format(tenDaysAgoDate);
				startDate = dateFormat.parse(formattedDateago);
				endDate = new Date();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DefaultHighLowDataset dataset;
		try {
			dataset = createDataset(startDate, endDate);
			String jsonData = JsonUtils.convertDatasetToJSON(dataset);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonData);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private DefaultHighLowDataset createDataset(Date date1, Date date2) throws ParseException {

		long t1 = date1.getTime();
		long t2 = date2.getTime();

		ArrayList<Date> date = new ArrayList<>();
		ArrayList<Double> high = new ArrayList<>();
		ArrayList<Double> low = new ArrayList<>();
		ArrayList<Double> open = new ArrayList<>();
		ArrayList<Double> close = new ArrayList<>();
		ArrayList<Double> volume = new ArrayList<>();
		String sql = "Select DateTimestamp,High,Open,Low,Close from ohlcDb where DateTimestamp between '" + t1
				+ "' and '" + t2 + "'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long timeD = rs.getLong("DateTimestamp");
				Date d1 = new Date(timeD);
				double h1 = rs.getDouble("High");
				double l1 = rs.getDouble("Low");
				double o1 = rs.getDouble("Open");
				double c1 = rs.getDouble("Close");
				date.add(d1);
				high.add(h1);
				low.add(l1);
				open.add(o1);
				close.add(c1);
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