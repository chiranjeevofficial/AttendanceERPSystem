package ServletClasses;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class RegistrationServ extends HttpServlet {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // database connection section
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/institute", "root", "admin@2023");
            if (con != null) {
                out.println("<h2>" + "You are Connected with Database" + "</h2>");
            } else {
                out.println("<h2>" + "Connectivity Error" + "</h2>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<h2>" + "MySQL JDBC Driver not found. Make sure you have the JDBC driver JAR file added to your project." + "</h2>");
        } catch (SQLException e) {
            out.println("<h2>" + "Database Connection Error: " + e.getMessage() + "</h2>");
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }

        if (con != null) {
            out.println("<h1>" + "Namaste, This is Registration Servlet" + "</h1>");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                stmt = con.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?");
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    // Valid credentials, redirect to success page
                    out.println("<h2>" + "success.html" + "</h2>");
                    out.println("<h2>" + "username: " + username + "</h2>");
                    out.println("<h2>" + "password: " + password + "</h2>");
                } else {
                    // Invalid credentials, redirect to failure page or display error message
                    out.println("<h2>" + "failure.html" + "</h2>");
                }

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                out.println("<h2>" + "in prepared statement block error occured" + "</h2>");
                out.println("<h2>" + ex.getMessage() + "</h2>");
            }
        } else {
            out.println("<h2> Connection Not Established </h2>");
        }
    }
}
