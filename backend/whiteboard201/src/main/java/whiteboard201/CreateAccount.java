package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/account/create")
public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		String inputUsername = request.getParameter("username");
		String inputPassword = request.getParameter("password");

		response.setContentType("application/json");
	
		Connection conn = null;
		PreparedStatement st = null;
		PrintWriter out = response.getWriter();
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/whiteboard201?user=root&password=root");
			st = conn.prepareStatement("INSERT INTO whiteboard201.users (username, password)"
					+ " VALUES (?, ?)");
			st.setString(1, inputUsername);
			st.setString(2, inputPassword);
			
			st.executeUpdate();
			
			out.println("{\"message\": \"New account created successfully.\"}");
			
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			out.println("{\"message\": \"Server Error\"}");
		} finally {
			out.close();
		}
	}
}
