package whiteboard201;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// Code run when the Tomcat server starts.
// Source: https://mkyong.com/servlet/what-is-listener-servletcontextlistener-example/
@WebListener
public class StartupListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			// Instantiate MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
