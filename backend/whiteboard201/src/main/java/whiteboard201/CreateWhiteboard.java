package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class CreateWhiteboard
 */
@WebServlet("/whiteboard/create")
public class CreateWhiteboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateWhiteboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		int userId;
		String name;
		
		try {
			userId = Integer.parseInt(request.getParameter("userId"));
			name = request.getParameter("name");
		} catch (NumberFormatException nfe) {
			out.println("{\"message\": \"Please input a valid integer.\"}");
			out.close();
			return;
		}
		setCorsHeaders(response);
		
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, DBCreds.DB_PASS);
			
			
			st = conn.prepareStatement("INSERT INTO whiteboard201.whiteboards (userId, name, content, updatedAt) "
					+ "VALUES (?, ?, '[]', ?)");
			st.setInt(1, userId);
			st.setString(2, name);
			st.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			
			st.executeUpdate();
			
			out.println("{\"message\": \"Success\"}");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
