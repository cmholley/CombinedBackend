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
import dash.pojo.Hour;
import dash.pojo.Location;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;
import dash.service.ClassServiceDbAccessImpl;
import dash.service.HourService;
import dash.service.LocationService;
import dash.service.ClassService;

public class HourSwitch extends ApplicationObjectSupport{

	@Autowired
	private HourService hourService;

	@Autowired
	private HourTransactionImpl transaction;
	
	public Long createHour(Hour hour, int ds) throws AppException {
		switch (ds){ 
		case 1:
			return transaction.createHour1(hour, ds, hourService);
		case 2:
			return transaction.createHour2(hour, ds, hourService);
		default:
			return (long)00;
		}
	}
	
	public void deleteHour(Hour hour, int ds) throws AppException {

		switch (ds){ 
		case 1:
			transaction.deleteHour1(hour, ds, hourService);
		case 2:
			transaction.deleteHour2(hour, ds, hourService);
		default:
		};

	}
	
	public void approveHour(Hour hour, boolean approved, int ds) throws AppException {
		switch (ds){ 
		case 1:
			transaction.approveHour1(hour, ds, hourService);
		case 2:
			transaction.approveHour2(hour, ds, hourService);
		default:
		};
		
		
	}
}
