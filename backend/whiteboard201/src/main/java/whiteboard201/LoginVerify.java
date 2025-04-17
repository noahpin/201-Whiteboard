/*
package whiteboard201;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login/verify")
public class LoginVerify extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(DBCreds.DB_URL, DBCreds.DB_USER, DBCreds.DB_PASS);
             PreparedStatement st = conn.prepareStatement("SELECT * FROM whiteboard201.users WHERE username = ? AND password = ? LIMIT 1")) {

            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();
            boolean success = false;

            while (rs.next()) {
                int userId = rs.getInt("userId");
                out.println("{\"userId\": " + userId + "}");
                success = true;
            }

            if (!success) {
                out.println("{\"userId\": -1}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5174"); // Svelte dev server
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
*/