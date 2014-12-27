package dash.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dash.dao.GroupDao;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Group;
import dash.pojo.Task;
import dash.pojo.User;
import dash.security.CustomPermission;
import dash.security.GenericAclController;

@Component("groupService")
public class GroupServiceDbAccessImpl extends ApplicationObjectSupport
		implements GroupService {

	@Autowired
	GroupDao groupDao;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private GenericAclController<Group> aclController;

	@Autowired
	private GenericAclController<Task> taskAclController;

	@Autowired
	private TaskService taskService;

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createGroup(Group group) throws AppException {

		validateInputForCreation(group);

		// verify existence of resource in the db (feed must be unique)
		Group groupByName = groupDao.getGroupByName(group.getName());
		if (groupByName != null) {
			throw new AppException(
					Response.Status.CONFLICT.getStatusCode(),
					409,
					"Group with groupname already existing in the database with the id "
							+ groupByName.getId(),
					"Please verify that the groupname and password are properly generated",
					AppConstants.DASH_POST_URL);
		}

		long groupId = groupDao.createGroup(new Group(group));
		group.setId(groupId);
		aclController.createACL(group);
		aclController.createAce(group, CustomPermission.MANAGER);
		return groupId;
	}

	private void validateInputForCreation(Group group) throws AppException {
		if (group.getName() == null) {
			throw new AppException(
					Response.Status.BAD_REQUEST.getStatusCode(),
					400,
					"Provided data not sufficient for insertion",
					"Please verify that the groupname is properly generated/set",
					AppConstants.DASH_POST_URL);
		}

		// etc...
	}

	@Override
	@Transactional
	public void createGroups(List<Group> groups) throws AppException {
//		for (Group group : groups) {
//			 createGroup(group);
//			
//		}
	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<Group> getGroups(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException {

		// verify optional parameter numberDaysToLookBack first
		if (numberDaysToLookBack != null) {
			List<Group> recentGroups = groupDao
					.getRecentGroups(numberDaysToLookBack);
			return getGroupsFromEntities(recentGroups);
		}

		if (isOrderByInsertionDateParameterValid(orderByInsertionDate)) {
			throw new AppException(
					Response.Status.BAD_REQUEST.getStatusCode(),
					400,
					"Please set either ASC or DESC for the orderByInsertionDate parameter",
					null, AppConstants.DASH_POST_URL);
		}
		List<Group> groups = groupDao.getGroups(orderByInsertionDate);

		return getGroupsFromEntities(groups);
	}

	@Override
	public List<Group> getGroupsByMembership(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException {

		return getGroups(orderByInsertionDate, numberDaysToLookBack);
	}

	@Override
	public List<Group> getGroupsByManager(String orderByInsertionDate,
			Integer numberDaysToLookBack) throws AppException {

		return getGroups(orderByInsertionDate, numberDaysToLookBack);
	}

	private boolean isOrderByInsertionDateParameterValid(
			String orderByInsertionDate) {
		return orderByInsertionDate != null
				&& !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC"
						.equalsIgnoreCase(orderByInsertionDate));
	}

	@Override
	public Group getGroupById(Long id) throws AppException {
		Group groupById = groupDao.getGroupById(id);
		if (groupById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The group you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the group with the id " + id
							+ " in the database", AppConstants.DASH_POST_URL);
		}

		return new Group(groupDao.getGroupById(id));
	}

	private List<Group> getGroupsFromEntities(List<Group> groupEntities) {
		List<Group> response = new ArrayList<Group>();
		for (Group groupEntity : groupEntities) {
			response.add(new Group(groupEntity));
		}

		return response;
	}

	public List<Group> getRecentGroups(int numberOfDaysToLookBack) {
		List<Group> recentGroups = groupDao
				.getRecentGroups(numberOfDaysToLookBack);

		return getGroupsFromEntities(recentGroups);
	}

	@Override
	public int getNumberOfGroups() {
		int totalNumber = groupDao.getNumberOfGroups();

		return totalNumber;

	}

	/********************* UPDATE-related methods implementation ***********************/
	@Override
	@Transactional
	public void updateFullyGroup(Group group) throws AppException {

		Group verifyGroupExistenceById = verifyGroupExistenceById(group.getId());
		if (verifyGroupExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ group.getId(), AppConstants.DASH_POST_URL);
		}
		copyAllProperties(verifyGroupExistenceById, group);
		groupDao.updateGroup(new Group(group));
	}

	private void copyAllProperties(Group verifyGroupExistenceById, Group group) {

		BeanUtilsBean withNull = new BeanUtilsBean();
		try {
			withNull.copyProperty(verifyGroupExistenceById, "name",
					group.getName());
			withNull.copyProperty(verifyGroupExistenceById, "description",
					group.getDescription());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/********************* DELETE-related methods implementation ***********************/

	@Override
	@Transactional
	public void deleteGroup(Group group) {

		groupDao.deleteGroupById(group);
		aclController.deleteACL(group);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// Groups to delete
	public void deleteGroups() {
		groupDao.deleteGroups();
	}

	@Override
	// TODO: This doesnt need to exist. It is the exact same thing as
	// getGroupById(Long)
	public Group verifyGroupExistenceById(Long id) {
		Group groupById = groupDao.getGroupById(id);
		if (groupById == null) {
			return null;
		} else {
			return new Group(groupById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyGroup(Group group) throws AppException {
		// do a validation to verify existence of the resource
		Group verifyGroupExistenceById = verifyGroupExistenceById(group.getId());
		if (verifyGroupExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ group.getId(), AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyGroupExistenceById, group);
		groupDao.updateGroup(new Group(verifyGroupExistenceById));

	}

	private void copyPartialProperties(Group verifyGroupExistenceById,
			Group group) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyGroupExistenceById, group);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ACL related methods
	 */
	// Adds an additional manager to the group
	@Override
	@Transactional
	public void addManager(User user, Group group) throws AppException {

		aclController.createAce(group, CustomPermission.MANAGER,
				new PrincipalSid(user.getUsername()));
		if (aclController.hasPermission(group, CustomPermission.MEMBER,
				new PrincipalSid(user.getUsername())))
			aclController.deleteACE(group, CustomPermission.MEMBER,
					new PrincipalSid(user.getUsername()));

	}

	// Removes all managers and sets new manager to user
	@Override
	@Transactional
	public void resetManager(User user, Group group) throws AppException {
		aclController.clearPermission(group, CustomPermission.MANAGER);
		aclController.createAce(group, CustomPermission.MANAGER,
				new PrincipalSid(user.getUsername()));
		if (aclController.hasPermission(group, CustomPermission.MEMBER,
				new PrincipalSid(user.getUsername())))
			aclController.deleteACE(group, CustomPermission.MEMBER,
					new PrincipalSid(user.getUsername()));

	}

	// Removes a single manager from a group
	@Override
	@Transactional
	public void deleteManager(User user, Group group) throws AppException {
		aclController.deleteACE(group, CustomPermission.MANAGER,
				new PrincipalSid(user.getUsername()));
		aclController.createAce(group, CustomPermission.MEMBER,
				new PrincipalSid(user.getUsername()));
	}

	// Adds a member to the group
	@Override
	@Transactional
	public void addMember(User user, Group group) throws AppException {
		aclController.createAce(group, CustomPermission.MEMBER,
				new PrincipalSid(user.getUsername()));
		if (aclController.hasPermission(group, CustomPermission.MANAGER,
				new PrincipalSid(user.getUsername())))
			aclController.deleteACE(group, CustomPermission.MANAGER,
					new PrincipalSid(user.getUsername()));

	}

	// Removes single member
	@Override
	@Transactional
	public void deleteMember(User user, Group group) throws AppException {
		List<Task> tasks = taskService.getTasksByGroup(group);
		for (int i = 0; i < tasks.size(); i++) {
			taskAclController.deleteACE(tasks.get(i), CustomPermission.MEMBER,
					new PrincipalSid(user.getUsername()));
			taskAclController.deleteACE(tasks.get(i), CustomPermission.MANAGER,
					new PrincipalSid(user.getUsername()));
		}
		aclController.deleteACE(group, CustomPermission.MEMBER,
				new PrincipalSid(user.getUsername()));
	}

}
