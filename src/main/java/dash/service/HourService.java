package dash.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Hour;

public interface HourService {

	/*
	 * ******************** Create related methods **********************
	 */
	
	/**
	 * Create a new hour and set the current user as owner and manager.
	 * @param hour
	 * @return
	 * @throws AppException
	 */
	public Long createHour(Hour hour) throws AppException;

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
	public List<Hour> getHoursByMyUser(int numberOfHours, Long startIndex, boolean onlyPending) throws AppException;


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
	@PreAuthorize("hasPermission(#hour, 'write') or hasPermission(#group, 'manager') or hasRole('ROLE_ADMIN_APPROVER')")
	public void updateFullyHour(Hour hour, Group group) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasRole('ROLE_ADMIN_APPROVER')")
	public void updateFullyHour(Hour hour) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasPermission(#group, 'manager') or hasRole('ROLE_ADMIN_APPROVER')")
	public void updatePartiallyHour(Hour hour, Group group) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasRole('ROLE_ADMIN_APPROVER')")
	public void updatePartiallyHour(Hour hour) throws AppException;

	@PreAuthorize("hasPermission(#group, 'manager') or hasRole('ROLE_MODERATOR')")
	public void approveHour(Hour hour, Group group, boolean approved) throws AppException;
	
	/*
	 * ******************** Delete related methods **********************
	 */
	@PreAuthorize("hasPermission(#hour, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deleteHour(Hour hour);
	
	/*
	 * ******************** Upload related methods **********************
	 */
	@PreAuthorize("hasPermission(#hour, 'delete') or hasRole('ROLE_ADMIN')")
	public void deleteUploadFile(String uploadedFileLocation, Hour hour)
			throws AppException;

	@PreAuthorize("hasPermission(#hour, 'write') or hasRole('ROLE_ADMIN')")
	public void uploadFile(InputStream uploadedInputStream,
			String uploadedFileLocation, Hour hour) throws AppException;

	@PreAuthorize("hasPermission(#hour, 'read') or hasRole('ROLE_ADMIN')")
	public List<String> getFileNames(Hour hour);
	
	@PreAuthorize("hasRole('ROLE_ADMIN_APPROVER')")
	public void approveHour(Hour hour, boolean approved) throws AppException;

	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfHours();

}
