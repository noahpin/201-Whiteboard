package login;

import javax.servlet.http.*;                      
import javax.servlet.*;                           
import javax.servlet.annotation.WebServlet;       
import java.io.IOException;                       
import java.sql.*;                              

// Map this servlet to handle requests sent to /login
@WebServlet("/login")

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database connection constants (adjust password if necessary)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/whiteboard201";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // doPost() handles form submissions
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Get form input values 
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //Basic null/empty check
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
            return;
        }

        //database objects set to null initially
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Prepare SQL query to check if the user exists with the provided email
            String sql = "SELECT userId, password FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            //execute  query
            rs = stmt.executeQuery();

            if (rs.next()) {
                //user found; check if password matches
                String dbPassword = rs.getString("password");
                int userId = rs.getInt("userId");

                if (dbPassword.equals(password)) {
                    //login successful ; create a session and store userId
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", userId);

                    //redirect to the whiteboard editing page
                    response.sendRedirect("edit-whiteboard.jsp");
                } else {
                    //password doesn't match
                    request.setAttribute("error", "Invalid email or password.");
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    rd.forward(request, response);
                }
            } else {
                //no user with that email found
                request.setAttribute("error", "Invalid email or password.");
                RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                rd.forward(request, response);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //jbdc driver not found
            request.setAttribute("error", "Internal error: JDBC Driver not found.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace(); //database error
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } finally {
            //close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); //cleanup error
            }
        }
    }
}
