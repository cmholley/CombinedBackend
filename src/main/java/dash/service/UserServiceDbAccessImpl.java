package dash.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.UserDao;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.User;
import dash.security.UserLoginController;

/**
 * An implementation of the user service for the VMA Core.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */

@Component("userService")
public class UserServiceDbAccessImpl extends ApplicationObjectSupport implements
		UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private UserLoginController authoritiesController;
	public static final String userRole = "ROLE_USER";

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createUser(User user) throws AppException {

		validateInputForCreation(user);

		//verify existence of resource in the db (feed must be unique)

		User userByName = userDao.getUserByName(user.getUsername());
		if (userByName != null) {
			throw new AppException(
					Response.Status.CONFLICT.getStatusCode(),
					409,
					"User with username already existing in the database with the id "
							+ userByName.getId(),
					"Please verify that the username and password are properly generated",
					AppConstants.DASH_POST_URL);
		}

		long userId = userDao.createUser(user);
		user.setId(userId);
		authoritiesController.create(user, userRole);
		createUserACL(user, new PrincipalSid(user.getUsername()));

		return userId;
	}

	private void validateInputForCreation(User user) throws AppException {
		if (user.getUsername() == null) {
			throw new AppException(
					Response.Status.BAD_REQUEST.getStatusCode(),
					400,
					"Provided data not sufficient for insertion",
					"Please verify that the username is properly generated/set",
					AppConstants.DASH_POST_URL);
		}
		if (user.getPassword() == null) {
			throw new AppException(
					Response.Status.BAD_REQUEST.getStatusCode(),
					400,
					"Provided data not sufficient for insertion",
					"Please verify that the password is properly generated/set",
					AppConstants.DASH_POST_URL);
		}
		// etc...
	}

	@Override
	@Transactional
	public void createUsers(List<User> users) throws AppException {
		for (User user : users) {
			createUser(user);
		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<User> getUsers(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException {

		//verify optional parameter numberDaysToLookBack first
		if(numberDaysToLookBack!=null){
			List<User> recentUsers = userDao
					.getRecentUsers(numberDaysToLookBack);
			return getUsersFromEntities(recentUsers);
		}

		if (isOrderByInsertionDateParameterValid(orderByInsertionDate)) {
			throw new AppException(
					Response.Status.BAD_REQUEST.getStatusCode(),
					400,
					"Please set either ASC or DESC for the orderByInsertionDate parameter",
					null, AppConstants.DASH_POST_URL);
		}
		List<User> users = userDao.getUsers(orderByInsertionDate);

		return getUsersFromEntities(users);
	}

	@Override
	public List<User> getMyUser(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException {
		return getUsers(orderByInsertionDate, numberDaysToLookBack);

	}

	private boolean isOrderByInsertionDateParameterValid(
			String orderByInsertionDate) {
		return orderByInsertionDate != null
				&& !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC"
						.equalsIgnoreCase(orderByInsertionDate));
	}

	@Override
	public User getUserById(Long id) throws AppException {
		User userById = userDao.getUserById(id);
		if (userById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The user you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the user with the id " + id
							+ " in the database", AppConstants.DASH_POST_URL);
		}

		return userDao.getUserById(id);
	}

	private List<User> getUsersFromEntities(List<User> userEntities) {
		List<User> response = new ArrayList<User>();
		for (User user : userEntities) {
			response.add(user);
		}

		return response;
	}

	public List<User> getRecentUsers(int numberOfDaysToLookBack) {
		List<User> recentUsers = userDao
				.getRecentUsers(numberOfDaysToLookBack);

		return getUsersFromEntities(recentUsers);
	}

	@Override
	public int getNumberOfUsers() {
		int totalNumber = userDao.getNumberOfUsers();

		return totalNumber;

	}

	@Override
	public List<String> getRole(User user) {
		ArrayList<String> tempRole = new ArrayList<String>();
		tempRole.add(userDao.getRoleByName(user.getUsername()));
		return tempRole;
	}

	protected String getUsername() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth.getPrincipal() instanceof UserDetails) {
			return ((UserDetails) auth.getPrincipal()).getUsername();
		} else {
			return auth.getPrincipal().toString();
		}
	}

	/********************* UPDATE-related methods implementation ***********************/
	@Override
	@Transactional
	public void updateFullyUser(User user) throws AppException {
		
		try {
			//verify existence of user
			User verifyUserExistenceById = getUserById(user.getId());
			
			copyAllProperties(verifyUserExistenceById, user);
			userDao.updateUser(verifyUserExistenceById);
			
		} catch (AppException ex) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ user.getId(),
							AppConstants.DASH_POST_URL);
		}		
	}

	/**
	 * Allows for merging bean with object does not ignore null properties.
	 * 
	 * 
	 */
	private void copyAllProperties(User verifyUserExistenceById, User user) {

		BeanUtilsBean withNull = new BeanUtilsBean();
		try {
			withNull.copyProperty(verifyUserExistenceById, "firstName",
					user.getFirstName());
			withNull.copyProperty(verifyUserExistenceById, "lastName",
					user.getLastName());
			withNull.copyProperty(verifyUserExistenceById, "city",
					user.getCity());
			withNull.copyProperty(verifyUserExistenceById, "homePhone",
					user.getHomePhone());
			withNull.copyProperty(verifyUserExistenceById, "cellPhone",
					user.getCellPhone());
			withNull.copyProperty(verifyUserExistenceById, "email",
					user.getEmail());
			withNull.copyProperty(verifyUserExistenceById, "picture",
					user.getPicture());
		} catch (IllegalAccessException e) {
			logger.debug("debugging info for exception: ", e); 
		} catch (InvocationTargetException e) {
			logger.debug("debugging info for exception: ", e); 
		}

	}

	/*********************
	 * DELETE-related methods implementation **********************
	 * 
	 * 
	 * */

	@Override
	@Transactional
	public void deleteUser(User user) {
		userDao.deleteUserById(user);
		deleteACL(user);
	}



	@Override
	@Transactional
	public void updatePartiallyUser(User user) throws AppException {
		
		try {
			// do a validation to verify existence of the resource
			User verifyUserExistenceById = getUserById(user.getId());
			copyPartialProperties(verifyUserExistenceById, user);
			userDao.updateUser(verifyUserExistenceById);
		} catch (AppException ex) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ user.getId(), AppConstants.DASH_POST_URL);
		}
	}

	private void copyPartialProperties(User verifyUserExistenceById, User user) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyUserExistenceById, user);
		} catch (IllegalAccessException e) {
			logger.debug("debugging info for exception: ", e); 
		} catch (InvocationTargetException e) {
			logger.debug("debugging info for exception: ", e); 
		}

	}

	@Override
	@Transactional
	public void resetPassword(User user) throws AppException {
		try {
			//verify existence of user
			getUserById(user.getId());
			
			authoritiesController.passwordReset(user);
		} catch (AppException ex) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ user.getId(), AppConstants.DASH_POST_URL);
		}
	}

	//****************** Methods for Acl *****************/

	/**
	 * Creates/Updates the ACL of user
	 * @param user 
	 * @param recipient
	 */
	public void createUserACL(User user, Sid recipient) {
		MutableAcl acl;
		ObjectIdentity oid = new ObjectIdentityImpl(User.class, user.getId());

		try {
			acl = (MutableAcl) mutableAclService.readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(oid);
		}
		acl.insertAce(acl.getEntries().size(), BasePermission.READ, recipient,
				true);
		acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, recipient,
				true);
		acl.insertAce(acl.getEntries().size(), BasePermission.DELETE,
				recipient, true);
		mutableAclService.updateAcl(acl);
		acl.setOwner(new PrincipalSid("Root"));
		mutableAclService.updateAcl(acl);

		logger.debug("Added permission " + "Read, Write, Delete" + " for Sid "
				+ recipient + " contact " + user);

	}

	public void deleteACL(User user) {

		ObjectIdentity oid = new ObjectIdentityImpl(User.class, user.getId());
		mutableAclService.deleteAcl(oid, false);

	}

	/*----------------------------------------------------------
	 * The Following Methods are to manipulate uploaded files
	 * ------------------------------------------------------------
	 */

	public void uploadFile(InputStream uploadedInputStream,
			String uploadedFileLocation, User user) throws AppException {

		try {
			File file = new File(uploadedFileLocation);
			file.getParentFile().mkdirs();
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 500,
					"Could not upload file due to IOException", "\n\n"
							+ e.getMessage(), AppConstants.DASH_POST_URL);
		}

	}

	@Override
	public void deleteUploadFile(String uploadedFileLocation, User user)
			throws AppException {
		Path path = Paths.get(uploadedFileLocation);
		try {
			Files.delete(path);
		} catch (NoSuchFileException x) {
			x.printStackTrace();
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "NoSuchFileException thrown, Operation unsuccesful.",
					"Please ensure the file you are attempting to"
							+ " delete exists at " + path + ".",
					AppConstants.DASH_POST_URL);

		} catch (DirectoryNotEmptyException x) {
			x.printStackTrace();
			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					404,
					"DirectoryNotEmptyException thrown, operation unsuccesful.",
					"This method should not attempt to delete,"
							+ " This should be considered a very serious error. Occured at "
							+ path + ".", AppConstants.DASH_POST_URL);
		} catch (IOException x) {
			x.printStackTrace();
			throw new AppException(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
					500,
					"IOException thrown and the designated file was not deleted.",
					" Permission problems occured at " + path + ".",
					AppConstants.DASH_POST_URL);
		}

	}

	@Override
	public List<String> getFileNames(User user) {
		List<String> results = new ArrayList<String>();

		//TODO:Switch statement for datasources
		File[] files = new File(AppConstants.APPLICATION_UPLOAD_LOCATION_FOLDER_CHW
				+ "/" + user.getPicture()).listFiles();
		// If this pathname does not denote a directory, then listFiles()
		// returns null.

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					results.add(file.getName());
				}
			}
		}
		return results;
	}

	/*-------------------------------------------------------------
	 * The Following methods are to modify User roles for the app.
	 * ------------------------------------------------------------
	 */

	@Override
	@Transactional
	public void setRoleUser(User user) {
		userDao.updateUserRole("ROLE_USER", user.getUsername());

	}

	@Override
	@Transactional
	public void setRoleModerator(User user) {
		userDao.updateUserRole("ROLE_MODERATOR", user.getUsername());

	}

	@Override
	@Transactional
	public void setRoleAdmin(User user) {
		userDao.updateUserRole("ROLE_ADMIN", user.getUsername());
		
	}

}
