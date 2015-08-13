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
import dash.pojo.Message;
import dash.pojo.Task;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.LocationService;
import dash.service.MessageService;
import dash.service.ClassService;

public class MessageSwitch extends ApplicationObjectSupport{

	@Autowired
	private MessageService messageService;

	@Autowired
	private MessageTransactionImpl transaction;
	
	public Long createMessage(Message message, Task task, int ds) throws AppException {
		switch(ds){
		case 1:
			return transaction.createMessage1(message, task, ds, messageService);
		case 2:
			return transaction.createMessage2(message, task, ds, messageService);
		default:
			return (long)00;
		}
	}
	
	public void deleteMessage(Message message, int ds) {
		switch(ds){
		case 1:
			transaction.deleteMessage1(message, ds, messageService);
		case 2:
			transaction.deleteMessage2(message, ds, messageService);
		default:
		}
	}
	
	public void updatePartiallyMessage(Message message, Task task, int ds) throws AppException {
		switch(ds){
		case 1:
			transaction.updatePartiallyMessage1(message, task, ds, messageService);
		case 2:
			transaction.updatePartiallyMessage2(message, task, ds, messageService);
		default:
		}
	}
	
	public void updateFullyMessage(Message message, int ds) throws AppException {
		switch(ds){
		case 1:
			transaction.updateFullyMessage1(message, ds, messageService);
		case 2:
			transaction.updateFullyMessage2(message, ds, messageService);
		default:
		}
	}
}
