package dash.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.HourDao;
import dash.dao.HourEntity;

import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Group;
import dash.pojo.Hour;
import dash.pojo.Task;
import dash.security.CustomPermission;
import dash.security.GenericAclController;

public class HourServiceDbAccessImpl extends ApplicationObjectSupport implements
		HourService {

	@Autowired
	HourDao hourDao;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private GenericAclController<Hour> aclController;

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createHour(Hour hour) throws AppException {
		hour.setPending(true);
		long hourId = hourDao.createHour(new HourEntity(hour));
		hour.setId(hourId);
		aclController.createACL(hour);
		aclController.createAce(hour, CustomPermission.READ);
		aclController.createAce(hour, CustomPermission.WRITE);
		aclController.createAce(hour, CustomPermission.DELETE);
		return hourId;
	}

	// Inactive
	@Override
	@Transactional
	public void createHours(List<Hour> hours) throws AppException {
		for (Hour hour : hours) {
			// createHour(hour);
		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<Hour> getHours(int numberOfHours, Long startIndex,
			boolean onlyPending) throws AppException {
		List<HourEntity> hours = hourDao.getHours(numberOfHours, startIndex,
				onlyPending, "ASC");
		return getHoursFromEntities(hours);
	}

	@Override
	public List<Hour> getHoursByGroup(int numberOfHours, Long startIndex,
			Group group, boolean onlyPending) throws AppException {

		// verify optional parameter numberDaysToLookBack first
		List<Task> tasksByGroup = taskService.getTasksByGroup(group);
		List<HourEntity> groupHours = new ArrayList<HourEntity>();
		for (Task task : tasksByGroup) {
			groupHours.addAll(hourDao.getHours(numberOfHours, startIndex, task,
					onlyPending));
		}

		return getHoursFromEntities(groupHours);
	}

	@Override
	public List<Hour> getHoursByMyUser(int numberOfHours, Long startIndex,
			boolean onlyPending) throws AppException {

		List<HourEntity> hours = hourDao.getHours(numberOfHours, startIndex,
				onlyPending, "DESC");
		return getHoursFromEntities(hours);

	}

	@Override
	public Hour getHourById(Long id) throws AppException {
		HourEntity hourById = hourDao.getHourById(id);
		if (hourById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The hour you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the hour with the id " + id
							+ " in the database", AppConstants.DASH_POST_URL);
		}

		return new Hour(hourDao.getHourById(id));
	}

	private List<Hour> getHoursFromEntities(List<HourEntity> hourEntities) {
		List<Hour> response = new ArrayList<Hour>();
		for (HourEntity hourEntity : hourEntities) {
			response.add(new Hour(hourEntity));
		}

		return response;
	}

	// public List<Hour> getRecentHours(int numberOfDaysToLookBack) {
	// List<HourEntity> recentHours = hourDao
	// .getRecentHours(numberOfDaysToLookBack);
	//
	// return getHoursFromEntities(recentHours);
	// }

	@Override
	public int getNumberOfHours() {
		int totalNumber = hourDao.getNumberOfHours();

		return totalNumber;

	}

	/********************* UPDATE-related methods implementation ***********************/
	@Override
	@Transactional
	public void updateFullyHour(Hour hour) throws AppException {
		Hour verifyHourExistenceById = verifyHourExistenceById(hour.getId());
		if (verifyHourExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ hour.getId(), AppConstants.DASH_POST_URL);
		}
		copyAllProperties(verifyHourExistenceById, hour);
		verifyHourExistenceById.setPending(true);
		verifyHourExistenceById.setApproved(false);

		hourDao.updateHour(new HourEntity(verifyHourExistenceById));
	}

	@Override
	@Transactional
	public void updateFullyHour(Hour hour, Group group) throws AppException {
		updateFullyHour(hour);
	}

	private void copyAllProperties(Hour verifyHourExistenceById, Hour hour) {

		BeanUtilsBean withNull = new BeanUtilsBean();
		try {
			withNull.copyProperty(verifyHourExistenceById, "title",
					hour.getTitle());
			withNull.copyProperty(verifyHourExistenceById, "start_time",
					hour.getStart_time());
			withNull.copyProperty(verifyHourExistenceById, "end_time",
					hour.getEnd_time());
			withNull.copyProperty(verifyHourExistenceById, "duration",
					hour.getDuration());

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/********************* DELETE-related methods implementation ***********************/

	@Override
	@Transactional
	public void deleteHour(Hour hour) {

		hourDao.deleteHourById(hour);
		aclController.deleteACL(hour);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// Hours to delete
	public void deleteHours() {
		hourDao.deleteHours();
	}

	@Override
	public Hour verifyHourExistenceById(Long id) {
		HourEntity hourById = hourDao.getHourById(id);
		if (hourById == null) {
			return null;
		} else {
			return new Hour(hourById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyHour(Hour hour) throws AppException {
		// do a validation to verify existence of the resource
		Hour verifyHourExistenceById = verifyHourExistenceById(hour.getId());
		if (verifyHourExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ hour.getId(), AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyHourExistenceById, hour);
		verifyHourExistenceById.setApproved(false);
		verifyHourExistenceById.setPending(true);
		hourDao.updateHour(new HourEntity(verifyHourExistenceById));

	}

	@Override
	@Transactional
	public void updatePartiallyHour(Hour hour, Group group) throws AppException {
		updatePartiallyHour(hour);
	}

	private void copyPartialProperties(Hour verifyHourExistenceById, Hour hour) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyHourExistenceById, hour);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional
	public void approveHour(Hour hour, boolean approved) throws AppException {

		hour.setApproved(approved);
		hour.setPending(false);
		hourDao.updateHour(new HourEntity(hour));

	}

	@Override
	@Transactional
	public void approveHour(Hour hour, Group group, boolean approved)
			throws AppException {
		approveHour(hour, approved);
	}

}
