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
import dash.pojo.Group;
import dash.pojo.Location;
import dash.pojo.Post;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.LocationService;
import dash.service.PostService;
import dash.service.ClassService;

public class PostSwitch extends ApplicationObjectSupport{

	@Autowired
	private PostService postService;

	@Autowired
	private PostTransactionImpl transaction;
	
	public Long createPost(Post post, Group group, int ds) throws AppException {
		switch(ds){
		case 1:
			return transaction.createPost1(post, group, ds, postService);
		case 2:
			return transaction.createPost2(post, group, ds, postService);
		default:
			return (long)00;
		}
	}
	
	public void deletePost(Post post, int ds) throws AppException{
		switch(ds){
		case 1:
			transaction.deletePost1(post, ds, postService);
		case 2:
			transaction.deletePost2(post, ds, postService);
		default:
		}
	}
	
	public void updateFullyPost(Post post, int ds) throws AppException {
		switch(ds){
		case 1:
			transaction.updateFullyPost1(post, ds, postService);
		case 2:
			transaction.updateFullyPost2(post, ds, postService);
		default:
		}
	}
}
