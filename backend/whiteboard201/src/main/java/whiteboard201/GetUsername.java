package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetUsername
 */
@WebServlet("/GetUsername")
public class GetUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetUsername() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String userId = request.getParameter("userId");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs;
		
		if(userId == null) {
			out.println("{\"message\": \"Please provide a userId.\"}");
			out.close();
		}
		try {
			conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, DBCreds.DB_PASS);
            st = conn.prepareStatement("SELECT username FROM users WHERE userId = ?");
            st.setInt(1, Integer.parseInt(userId));
            rs = st.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String json = gson.toJson(new UserResponse(userId, username));
                out.println(json);
            } else {
                out.println("{\"message\": \"User not found.\"}");
            }
		} catch (SQLException | NumberFormatException e) {
			out.println("{\"message\": \"Server Error\"}");
            e.printStackTrace();
		} finally {
			out.close();
		}
		
	}
	
	private class UserResponse {
		String userId;
		String username;
		
		UserResponse(String userId, String username) {
			this.userId = userId;
			this.username = username;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
