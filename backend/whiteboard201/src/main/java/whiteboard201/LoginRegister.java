package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.*;

import java.sql.*;

@WebServlet("/login/verify")
public class LoginRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init() {

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		String inputPassword = request.getParameter("password");
		String inputUsername = request.getParameter("username");
		String option = request.getParameter("action"); // register, login, guest.
		
		response.setContentType("application/json");
		
		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		
		if (option == null) {
			out.println("{\"message\": \"Please provide an option.\"}");
			out.close();
			return;
		}
		
		if (!option.equals("guest") && (inputUsername == null || inputPassword == null)) {
			out.println("{\"message\": \"Please provide all fields.\"}");
			out.close();
			return;
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/whiteboard201?user=root&password=root");
			if (option.equals("login")) {
				st = conn.prepareStatement("SELECT * FROM whiteboard201.users WHERE username = ? AND password = ? LIMIT 1");
				st.setString(1, inputUsername);
				st.setString(2, inputPassword);
				rs = st.executeQuery();
				
				boolean success = false;
				while (rs.next()) {
					if (rs.getString("username").equals(inputUsername) && rs.getString("password").equals(inputPassword)) {
						out.println("{\"userId\": " + rs.getInt("userId") + "}");
						success = true;
						break;
					}
				}
				if (!success) {
					out.println("{\"userId\": " + "-1" + "}");
				}
			} else if (option.equals("register")) {
				
				// Check if username already in database
				st2 = conn.prepareStatement("SELECT * FROM whiteboard201.users WHERE username = ?");
				st2.setString(1, inputUsername);
				
				rs = st2.executeQuery();
				
				if (rs.next()) {
					out.println("{\"message\": \"Error: Username already in use.\"}");
					out.close();
					return;
				}
				
				// Insert the new profile.
				st = conn.prepareStatement("INSERT INTO whiteboard201.users (username, password)"
						+ " VALUES (?, ?)");
				st.setString(1, inputUsername);
				st.setString(2, inputPassword);
				
				st.executeUpdate();
				
				out.println("{\"message\": \"New account created successfully.\"}");
				
			} else if (option.equals("guest")) {
				// TODO
			} else {
				out.println("{\"message\": \"Please provide a valid option.\"}");
				out.close();
				return;
			}
			
			
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			out.println("{\"message\": \"Server Error\"}");
		} finally {
			out.close();
		}
	}
}
