package whiteboard201;

import java.io.*;

//new for changing to ensure can dump directly into sql database
import java.sql.*;
import java.time.LocalDateTime;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

@WebServlet("/saveWhiteboard")
public class SaveWhiteboardServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get ID input from parameter input then check to make sure it works
		String whiteboardIdInput = request.getParameter("id");

		if (whiteboardIdInput == null || whiteboardIdInput.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing whiteboard ID"); // all responses I
																								// referenced this:
																								// https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletResponse.html
			return;
		}

		int whiteboardId;

		try {
			whiteboardId = Integer.parseInt(whiteboardIdInput);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid whiteboard ID");
			return;
		}

		// read raw JSON from request, use StringBuilder to get into a single string:
		// https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;

		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Failed to close BufferedReader:");
					e.printStackTrace();
				}
			}
		}

		String json = sb.toString();

		// check if json is valid by trying to parse
		try {
			gson.fromJson(json, JsonElement.class);
		} catch (JsonSyntaxException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
			return;
		}

		// save to sql database
		try {
			updateWBinDB(whiteboardId, json);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Database update failed: " + e.getMessage());
			return;
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = null;
		try {
			out = response.getWriter();
			gson.toJson(Map.of("message", "Whiteboard saved successfully!"), out);
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	// helper function that saves (updates) whiteboard in the database
	private void updateWBinDB(int whiteboardID, String json) throws SQLException {

		String updateQuery = "UPDATE whiteboards SET content = ?, updatedAt = ? WHERE whiteboardId = ?";

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, DBCreds.DB_PASS);
			stmt = conn.prepareStatement(updateQuery);

			stmt.setString(1, json);
			stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			stmt.setInt(3, whiteboardID);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				throw new SQLException("No whiteboard found with ID: " + whiteboardID);
			}

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.err.println("Failed to close PreparedStatement: ");
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Failed to close Connection: ");
					e.printStackTrace();
				}
			}
		}
	}
}
