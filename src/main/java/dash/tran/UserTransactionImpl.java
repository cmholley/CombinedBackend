package dash.tran;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.User;
import dash.service.UserService;

public class UserTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public Long createUser1(User user, int ds, UserService userService) throws AppException {
		return userService.createUser(user, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public Long createUser2(User user, int ds, UserService userService) throws AppException {
		return userService.createUser(user, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteUser1(User user, int ds, UserService userService) throws AppException{
		userService.deleteUser(user, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void deleteUser2(User user, int ds, UserService userService) throws AppException{
		userService.deleteUser(user, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void createUsers1(List<User> users, int ds, UserService userService) throws AppException{
		userService.createUsers(users, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void createUsers2(List<User> users, int ds, UserService userService) throws AppException{
		userService.createUsers(users, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updateFullyUser1(User user, int ds, UserService userService) throws AppException {
		userService.updateFullyUser(user, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updateFullyUser2(User user, int ds, UserService userService) throws AppException {
		userService.updateFullyUser(user, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updatePartiallyUser1(User user, int ds, UserService userService) throws AppException {
		userService.updatePartiallyUser(user, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updatePartiallyUser2(User user, int ds, UserService userService) throws AppException {
		userService.updatePartiallyUser(user, ds);
	}
}
