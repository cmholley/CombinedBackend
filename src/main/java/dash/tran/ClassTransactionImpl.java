package dash.tran;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.ClassDao;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Class;
import dash.pojo.Comment;
import dash.pojo.Group;
import dash.pojo.Location;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.CommentService;
import dash.service.LocationService;
import dash.service.TaskService;
import dash.service.ClassService;

public class ClassTransactionImpl extends ApplicationObjectSupport{


	/*********************
	 * Create related methods implementation
	 ***********************/

	@Transactional("transactionManagerCHW")
	public Long createClass1(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		System.out.println("Called one");
		return classService.createClass(clas, loc, ds);
	}

	@Transactional("transactionManagerVMA")
	public Long createClass2(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		System.out.println("Called two");
		return classService.createClass(clas, loc, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteClass1(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		classService.createClass(clas, loc, ds);
	}

	@Transactional("transactionManagerVMA")
	public void deleteClass2(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		classService.createClass(clas, loc, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updatePartiallyClass1(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		classService.updatePartiallyClass(clas, loc, ds);
	}

	@Transactional("transactionManagerVMA")
	public void updatePartiallyClass2(Class clas, Location loc, int ds, ClassService classService) throws AppException {
		classService.updatePartiallyClass(clas, loc, ds);
	}
	
//	@Transactional("transactionManagerCHW")
//	public void createTask1(Group group, int ds, TaskService taskService) throws AppException {
//		taskService.createTask(group, ds);
//	}
//
//	@Transactional("transactionManagerVMA")
//	public void createTask2(Group group, int ds, TaskService taskService) throws AppException {
//		taskService.createTask(group, ds);
//	}
}
