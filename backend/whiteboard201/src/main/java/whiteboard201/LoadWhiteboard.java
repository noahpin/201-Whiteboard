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

/**
 * Servlet implementation class LoadWhiteboard
 */
@WebServlet("/whiteboard/get")
public class LoadWhiteboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadWhiteboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		int whiteboardId;
		
		try {
			whiteboardId = Integer.parseInt(request.getParameter("whiteboardId"));
			
		} catch (NumberFormatException nfe) {
			out.println("{\"message\": \"Please input a valid integer.\"}");
			out.close();
			return;
		}
		
		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, DBCreds.DB_PASS);
			
			
			st = conn.prepareStatement("SELECT userId, content FROM whiteboard201.whiteboards WHERE whiteboardId = ?");
			st.setInt(1, whiteboardId);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				out.println("{\"userId\": " + rs.getInt("userId") + ", ");
				out.println("\"content\": " + rs.getString("content") + ", ");
				out.println("\"sharedUsers\": [");
			} else {
				out.println("{\"message\": \"Whiteboard not found.\"}");
				return;
			}
			
			st2 = conn.prepareStatement("SELECT userId, permissionLevel FROM whiteboard201.permissions WHERE whiteboardId = ?");
			st2.setInt(1, whiteboardId);
			
			rs = st.executeQuery();
			
			String json = "";
			
			while (rs.next()) {
				json += "{\"userId\": " + rs.getInt("userId") + " , " + "\"permissionLevel\": " + rs.getInt("permissionLevel") + "},";
			}
			if (!json.isEmpty()) {
				json = json.substring(0, json.length()-1);
			}
			out.println(json);
			out.println("]}");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

}
