package dash.service;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Class;
import dash.pojo.Location;
import dash.pojo.User;

public interface ClassService {
	/* ******************** Create related methods ***********************/
	
	/**
	 * Create a new class and set the current user as owner and manager.
	 * @param clas The new class to be created.
	 * @param loc The location that offers the class.
	 * @return The new class ID. 
	 * @throws AppException if class has to be generated does not contain a name.
	 */
	@PreAuthorize("hasPermission(#loc, 'MANAGER') or hasRole('ROLE_ADMIN')")
	public Long createClass(Class clas, Location loc) throws AppException;

	/**
	 * Create multiple groups, only available for MANAGER.
	 * @param classes The list of classes to be created
	 * @param location The location that offers the classes.
	 * @throws AppException if classes to be created do not contain at least a name.
	 */
	@PreAuthorize("hasPermission(#location, 'MANAGER') or hasRole('ROLE_ADMIN')")
	public void createClasses(List<Class> classes, Location location)
			throws AppException;

	/* ******************* Read related methods *********************/
	/**
	 * Get all classes.
	 * @param orderByInsertionDate Define in what order the classes should be returned 
	 * @param numberDaysToLookBack The number of days to look back.
	 * @return A list of all classes.
	 * @throws AppException orderByInsertionDate must be ASC or DESC
	 */
	public List<Class> getClasses(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException;

	/**
	 * Get all classes offered at a certain location.
	 * @param location The location that classes should be listed for.
	 * @return A list of classes offered at location.
	 */
	public List<Class> getClassesByLocation(Location location)
			throws AppException;

	/**
	 * Get classes by its ID.
	 * @param id Class id. 
	 * @return The class specified by id..
	 * @throws AppException if class with specified id can't be found.
	 */
	public Class getClassById(Long id) throws AppException;

	/**
	 * Get classes by membership.
	 * @param orderByInsertionDate The order in which the classes should be returned.
	 * @param numberDaysToLookBack The number of days to go back.
	 * @return List of classes that user is a member.
	 * @throws AppException if orderByInserationDate is not "ASC" or "DESC"
	 */
	@PostFilter("hasPermission(filterObject, 'MEMBER')")
	public List<Class> getClassesByMembership(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#clas, 'MANAGER') or hasPermission(#loc, 'MANAGER') or hasRole('ROLE_MODERATOR')")
	public void updatePartiallyClass(Class clas, Location loc)
			throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */
	@PreAuthorize("hasPermission(#clas, 'MANAGER') or hasPermission(#loc, 'MANAGER') or hasRole('ROLE_MODERATOR')")
	public void deleteClass(Class clas, Location loc) throws AppException;

	/*
	 * ******************** Membership related methods **********************
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void addMember(User user, Class clas) throws AppException;

	// Removes member
	@PreAuthorize("hasPermission(#user, 'WRITE') or hasRole('ROLE_MODERATOR') or hasPermission(#group, 'MANAGER')")
	public void deleteMember(User user, Class clas) throws AppException;

	/*
	 * ******************** Helper methods **********************
	 */
	public void sendAlertEmail(List<String> membersForClass, Class clas);

	public List<String> getMembersForClass(Class clas);

	public List<Class> getTodaysClasses();

}
