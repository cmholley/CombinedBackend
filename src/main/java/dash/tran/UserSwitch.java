package dash.tran;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
import dash.pojo.Location;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.LocationService;
import dash.service.UserService;
import dash.service.ClassService;

public class UserSwitch extends ApplicationObjectSupport{

	@Autowired
	private UserService userService;

	@Autowired
	private UserTransactionImpl transaction;
	/*********************
	 * Create related methods implementation
	 ***********************/
	
	public Long createUser(User user, int ds) throws AppException {
		switch(ds){
			case 1:
				return transaction.createUser1(user, ds, userService);
			case 2:
				return transaction.createUser2(user, ds, userService);
			default:
				return (long)00;
		}
	}
	
	public void deleteUser(User user, int ds) throws AppException{
		switch(ds){
			case 1:
				transaction.deleteUser1(user, ds, userService);
			case 2:
				transaction.deleteUser2(user, ds, userService);
			default:
		}
	}
	
	public void createUsers(List<User> users, int ds) throws AppException{
		switch(ds){
		case 1:
			transaction.createUsers1(users, ds, userService);
		case 2:
			transaction.createUsers2(users, ds, userService);
		default:
		}
	}
	
	public void updateFullyUser(User user, int ds) throws AppException {
		switch(ds){
		case 1:
			transaction.updateFullyUser1(user, ds, userService);
		case 2:
			transaction.updateFullyUser2(user, ds, userService);
		default:
		}
	}
	
	public void updatePartiallyUser(User user, int ds) throws AppException {
		switch(ds){
		case 1:
			transaction.updatePartiallyUser1(user, ds, userService);
		case 2:
			transaction.updatePartiallyUser2(user, ds, userService);
		default:
		}
	}
}
