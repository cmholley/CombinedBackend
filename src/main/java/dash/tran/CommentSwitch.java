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
import dash.pojo.Comment;
import dash.pojo.Group;
import dash.pojo.Location;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.CommentService;
import dash.service.LocationService;
import dash.service.ClassService;

public class CommentSwitch extends ApplicationObjectSupport{

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentTransactionImpl transaction;
	/*********************
	 * Create related methods implementation
	 ***********************/

	public Long createComment(Comment comment, Group group, int ds) throws AppException {
		switch (ds){ 
		case 1:
			return transaction.createComment1(comment, group, ds, commentService);
		case 2:
			return transaction.createComment2(comment, group, ds, commentService);
		default:
			return (long)00;
		}
	}
	
	public void deleteComment(Comment comment, int ds) throws AppException {
		switch (ds){ 
		case 1:
			transaction.deleteComment1(comment, ds, commentService);
		case 2:
			transaction.deleteComment2(comment, ds, commentService);
		default:
		}
	}
	
}
