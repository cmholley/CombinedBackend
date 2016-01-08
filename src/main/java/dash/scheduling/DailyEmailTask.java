package dash.scheduling;

import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dash.pojo.Class;
import dash.service.ClassService;

//This TimerTask is designed to run every day at midnight. 
//It will retrieve all of the studies with a start date
//Of the current day and then initialize the job for them based of off the survey.

public class DailyEmailTask extends TimerTask {

	private ServletContextEvent servletContextEvent;

	public DailyEmailTask(ServletContextEvent servletContextEvent) {
		this.servletContextEvent = servletContextEvent;
	}

	@Override
	public void run() {
		ApplicationContext springContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
		AutowireCapableBeanFactory factory = springContext.getAutowireCapableBeanFactory();

		ClassService classService = factory.getBean(ClassService.class);
		List<Class> todaysClasses = classService.getTodaysClasses();

		for (Class clas : todaysClasses) {
			List<String> membersForClass = classService.getMembersForClass(clas);
			classService.sendAlertEmail(membersForClass, clas);
		}
	}

}
