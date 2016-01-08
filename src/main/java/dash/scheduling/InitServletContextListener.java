package dash.scheduling;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Timer dailyTimer = new Timer(true);// The timer thread needs to be a
											// daemon
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date midnightDate = cal.getTime();
		dailyTimer.scheduleAtFixedRate(new DailyEmailTask(sce), midnightDate,
				TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // Executes
																	// daily
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
