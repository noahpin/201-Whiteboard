package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetUserWhiteboards
 */
@WebServlet("/whiteboards/get")
public class GetUserWhiteboards extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Gson gson = new Gson();
		
		String userId = request.getParameter("userId");
		
		response.setContentType("application/json");
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs;
		PrintWriter out = response.getWriter();
		
		if (userId == null) {
			out.println("{\"message\": \"Please provide an ID.\"}");
			out.close();
			return;
		}
		
		try {
			// Name, most recent update, owner, whiteboardId.
			conn = DriverManager.getConnection("jdbc:mysql://localhost/whiteboard201?user=root&password=root");
			st = conn.prepareStatement("SELECT whiteboardId, username, updatedAt FROM whiteboard201.whiteboards\r\n"
					+ "LEFT JOIN users ON users.userId = whiteboards.userId\r\n"
					+ "WHERE users.userId = ?");
			st.setInt(1, Integer.parseInt(userId));
			rs = st.executeQuery();
			
			ArrayList<Whiteboard> boards = new ArrayList<>();
			
			while (rs.next()) {
				Whiteboard wb = new Whiteboard(rs.getInt("whiteboardId"), rs.getString("username"), rs.getTimestamp("updatedAt"));
				boards.add(wb);
			}
			String res = gson.toJson(boards);
			out.println(res);
			
		} catch (SQLException sqle) {
			out.println("{\"message\": \"Server Error\"}");
			System.out.println(sqle.getMessage());
		} catch (NumberFormatException nfe) {
			// TODO: handle exception
			out.println("{\"message\": \"Please provide a valid integer.\"}");
			System.out.println(nfe.getMessage());
		} finally {
			out.close();
		}
	}

}
