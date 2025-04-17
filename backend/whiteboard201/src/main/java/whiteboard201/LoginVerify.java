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
public class LoginVerify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init() {

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		String inputPassword = request.getParameter("password");
		String inputUsername = request.getParameter("username");
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		
		try {
			
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost/whiteboard201?user=root&password=root");
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
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			out.println("{\"message\": \"Server Error\"}");
		} finally {
			out.close();
		}
	}
}
