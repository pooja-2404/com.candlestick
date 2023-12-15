package com.candlestickChart;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Main")
public class Main extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String url ="jdbc:mysql://localhost:3306/db1";
			String user = "root";
			String pass = "Pooja@240494";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pass);
			if (conn != null) {
				System.out.println("Connected");
			}
			getServletContext().setAttribute("dbConn",conn);
			response.sendRedirect("Dashboard2.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
