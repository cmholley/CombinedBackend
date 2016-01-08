package dash.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.User;

/**
 *
 * @author plindner
 * @see <a href=
 *      "http://www.codingpedia.org/ama/spring-mybatis-integration-example/">
 *      http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface UserService {

	/*
	 * ******************** Create related methods **********************
	 */
	public Long createUser(User user) throws AppException;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void createUsers(List<User> users) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying users
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for users,
	 *            null
	 * @return list with users corresponding to search criteria
	 * @throws AppException
	 */
	@PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN')")
	public List<User> getUsers(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException;

	@PostFilter("hasPermission(filterObject, 'READ')")
	public List<User> getMyUser(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException;

	/**
	 * Returns a user given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */

	public User getUserById(Long id) throws AppException;

	public List<String> getRole(User user);

	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updateFullyUser(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void updatePartiallyUser(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_ADMIN')")
	public void resetPassword(User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'WRITE') and hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public void setRoleUser(User user);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void setRoleModerator(User user);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void setRoleAdmin(User user);

	@PreAuthorize("hasPermission(#user, 'DELETE') or hasRole('ROLE_ADMIN')")
	public void deleteUser(User user);

	/*
	 * ******************** Delete related methods **********************
	 * 
	 * Deletions temporarily disabled while a deactivate method is worked on
	 */

	@PreAuthorize("hasPermission(#user, 'delete') or hasRole('ROLE_ADMIN')")
	public void deleteUploadFile(String uploadedFileLocation, User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'write') or hasRole('ROLE_ADMIN')")
	public void uploadFile(InputStream uploadedInputStream, String uploadedFileLocation, User user) throws AppException;

	@PreAuthorize("hasPermission(#user, 'read') or hasRole('ROLE_ADMIN')")
	public List<String> getFileNames(User user);

	// @PreAuthorize("hasPermission(#user, 'DELETE') or hasRole('ROLE_ADMIN')")
	// public void deleteUser(User user);
	// /** removes all users */
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// public void deleteUsers();

	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfUsers();

}
