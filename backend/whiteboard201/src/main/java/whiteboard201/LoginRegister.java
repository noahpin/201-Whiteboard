package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.sql.*;

@WebServlet("/login/verify")
public class LoginRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init() {

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		setCorsHeaders(response);
		
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
			conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, "password");
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

				// Get the new userId (assumes username is unique)
				st2 = conn.prepareStatement("SELECT userId FROM whiteboard201.users WHERE username = ? LIMIT 1");
				st2.setString(1, inputUsername);
				rs = st2.executeQuery();

				if (rs.next()) {
				    int userId = rs.getInt("userId");
				    out.println("{\"userId\": " + userId + "}");
				} else {
				    out.println("{\"userId\": -1}"); // fallback if somehow not found
				}

				
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
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // Svelte dev server
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
