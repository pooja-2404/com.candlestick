package com.candlestickChart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/tradingindicator")
public class TradingIndicator extends HttpServlet {
	Connection conn;
	ArrayList<Float> listClose = new ArrayList<>();
	ArrayList<Float> listHigh = new ArrayList<>();
	ArrayList<Float> listLow = new ArrayList<>();
	ArrayList<Date> date = new ArrayList<>();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		listClose.clear();
		listHigh.clear();
		listLow.clear();
		String option = request.getParameter("option");
		conn = (Connection) getServletContext().getAttribute("dbConn");
		
		try {

			String sql = "SELECT DateTimestamp,close,high,low from ohlcDb";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long timeD = rs.getLong("DateTimestamp");
				float close = rs.getFloat("close");
				float high = rs.getFloat("high");
				float low = rs.getFloat("low");
				Date d1 = new Date(timeD);
				date.add(d1);
				listClose.add(close);
				listHigh.add(high);
				listLow.add(low);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Date[] datear = date.toArray(new Date[0]);
		String jsonData = null;

		if (option.trim().equals("RSI")) {
			jsonData = RSIDataset.convertRSItoJson(datear, listClose);
		} else if (option.trim().equals("MACD")) {
			jsonData = MACDDataset.convertMACDtoJson(datear, listClose);
		} else if (option.trim().equals("Stochastic-Oscillator")) {
			jsonData = StochasticDataset.convertS_OscillatortoJson(datear, listClose, listHigh, listLow);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonData);
	}

}