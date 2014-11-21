package dash.service;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Hour;

public interface HourService {
	/*
	 * ******************** Create related methods **********************
	 * 
	 * Create a new hour and set the current user as owner and manager.
	 */

	public Long createHour(Hour hour) throws AppException;

	/*
	 * Create multiple hours as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createHours(List<Hour> hours) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying hours
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for hours,
	 *            null
	 * @return list with hours corresponding to search criteria
	 * @throws AppException
	 */

	public List<Hour> getHours(int numberOfHours, Long startIndex,
			boolean onlyPending) throws AppException;

	public List<Hour> getHoursByGroup(int numberOfHours, Long startIndex,
			Group group, boolean onlyPending) throws AppException;

	@PostFilter("hasPermission(filterObject, 'WRITE')")
	public List<Hour> getHoursByMyUser(int numberOfHours, Long startIndex,
			boolean onlyPending) throws AppException;

	/**
	 * Returns a hour given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */

	public Hour getHourById(Long id) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#hour, 'write') or hasPermission(#group, 'manager') or hasRole('ROLE_MODERATOR')")
	public void updateFullyHour(Hour hour, Group group) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasRole('ROLE_MODERATOR')")
	public void updateFullyHour(Hour hour) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasPermission(#group, 'manager') or hasRole('ROLE_MODERATOR')")
	public void updatePartiallyHour(Hour hour, Group group) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasRole('ROLE_MODERATOR')")
	public void updatePartiallyHour(Hour hour) throws AppException;

	@PreAuthorize("hasRole('ROLE_MODERATOR')")
	public void approveHour(Hour hour, boolean approved) throws AppException;

	@PreAuthorize("hasPermission(#group, 'manager') or hasRole('ROLE_MODERATOR')")
	public void approveHour(Hour hour, Group group, boolean approved)
			throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */

	@PreAuthorize("hasPermission(#hour, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deleteHour(Hour hour);

	/**
	 * removes all hours DO NOT USE, IMPROPERLY UPDATES ACL_OBJECT table
	 * Functional but does not destroy old acl's which doesnt hurt anything but
	 * they will take up space if this is commonly used
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void deleteHours();

	/*
	 * ******************** Helper methods **********************
	 */
	// TODO: This also should not exist, or it should be changed to
	// private/protected. Redundant
	// Could be made a boolean so it was not a security vulnerability
	public Hour verifyHourExistenceById(Long id);

	public int getNumberOfHours();

}
