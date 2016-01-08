package dash;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class ContextFinalizer implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextFinalizer.class);

	public void contextInitialized(ServletContextEvent sce) {
	}

	public void contextDestroyed(ServletContextEvent sce) {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		Driver d = null;
		while (drivers.hasMoreElements()) {
			try {
				d = drivers.nextElement();
				DriverManager.deregisterDriver(d);
				LOGGER.warn(String.format("Driver %s deregistered", d));
			} catch (SQLException ex) {
				LOGGER.warn(String.format("Error deregistering driver %s", d), ex);
			}
		}
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			LOGGER.warn("SEVERE problem cleaning up: " + e.getMessage());
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Exception thrown in " + this.getClass().getName(), e);
		}
	}

}