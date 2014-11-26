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

import dash.dao.MessageDao;
import dash.dao.MessageEntity;
import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.helpers.NullAwareBeanUtilsBean;
import dash.pojo.Message;
import dash.pojo.Task;
import dash.security.CustomPermission;
import dash.security.GenericAclController;

/*
 * Message Service DB Access Impl
 * @Author CarlSteven
 */
public class MessageServiceDbAccessImpl extends ApplicationObjectSupport
		implements MessageService {

	@Autowired
	MessageDao messageDao;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private GenericAclController<Message> aclController;

	/********************* Create related methods implementation ***********************/
	@Override
	@Transactional
	public Long createMessage(Message message, Task task) throws AppException {
		long messageId = messageDao.createMessage(new MessageEntity(message));
		message.setId(messageId);
		aclController.createACL(message);
		aclController.createAce(message, CustomPermission.READ);
		aclController.createAce(message, CustomPermission.WRITE);
		aclController.createAce(message, CustomPermission.DELETE);
		return messageId;
	}

	// Inactive
	@Override
	@Transactional
	public void createMessages(List<Message> messages) throws AppException {

	}

	// ******************** Read related methods implementation
	// **********************
	@Override
	public List<Message> getMessagesByTask(int numberOfPosts, Long startIndex,
			Task task) throws AppException {
		List<MessageEntity> messages = messageDao.getMessages(numberOfPosts,
				startIndex, task);
		return getMessagesFromEntities(messages);
	}

	@Override
	public Message getMessageById(Long id) throws AppException {
		MessageEntity messageById = messageDao.getMessageById(id);
		if (messageById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404, "The post you requested with id " + id
							+ " was not found in the database",
					"Verify the existence of the post with the id " + id
							+ " in the database", AppConstants.DASH_POST_URL);
		}

		return new Message(messageDao.getMessageById(id));
	}

	private List<Message> getMessagesFromEntities(List<MessageEntity> groupPosts) {
		List<Message> response = new ArrayList<Message>();
		for (MessageEntity postEntity : groupPosts) {
			response.add(new Message(postEntity));
		}

		return response;
	}

	// public List<Post> getRecentPosts(int numberOfDaysToLookBack) {
	// List<PostEntity> recentPosts = postDao
	// .getRecentPosts(numberOfDaysToLookBack);
	//
	// return getPostsFromEntities(recentPosts);
	// }

	@Override
	public int getNumberOfMessages() {
		int totalNumber = messageDao.getNumberOfMessages();

		return totalNumber;

	}

	/********************* UPDATE-related methods implementation ***********************/
	@Override
	@Transactional
	public void updateFullyMessage(Message message) throws AppException {
		// do a validation to verify FULL update with PUT
		if (isFullUpdate(message)) {
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
					400, "Please specify all properties for Full UPDATE",
					"required properties - name, description",
					AppConstants.DASH_POST_URL);
		}

		Message verifyMessageExistenceById = verifyMessageExistenceById(message
				.getId());
		if (verifyMessageExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ message.getId(), AppConstants.DASH_POST_URL);
		}

		messageDao.updateMessage(new MessageEntity(message));
	}

	/**
	 * Verifies the "completeness" of post resource sent over the wire
	 *
	 * @param Post
	 * @return
	 */
	private boolean isFullUpdate(Message post) {
		return post.getId() == null || post.getContent() == null;
	}

	/********************* DELETE-related methods implementation ***********************/

	@Override
	@Transactional
	public void deleteMessage(Message message) {

		messageDao.deleteMessageById(message);
		aclController.deleteACL(message);

	}

	@Override
	@Transactional
	// TODO: This shouldn't exist? If it must, then it needs to accept a list of
	// Posts to delete
	public void deleteMessages() {
		messageDao.deleteMessages();
	}

	@Override
	public Message verifyMessageExistenceById(Long id) {
		MessageEntity messageById = messageDao.getMessageById(id);
		if (messageById == null) {
			return null;
		} else {
			return new Message(messageById);
		}
	}

	@Override
	@Transactional
	public void updatePartiallyMessage(Message message, Task task)
			throws AppException {
		// do a validation to verify existence of the resource
		Message verifyMessageExistenceById = verifyMessageExistenceById(message
				.getId());
		if (verifyMessageExistenceById == null) {
			throw new AppException(
					Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - "
							+ message.getId(), AppConstants.DASH_POST_URL);
		}
		if (verifyMessageExistenceById.getSender_id() != message.getSender_id()) {
			throw new AppException(Response.Status.FORBIDDEN.getStatusCode(),
					404, "Not allowed to change sender_id in the database.", ""
							+ message.getId(), AppConstants.DASH_POST_URL);
		}
		copyPartialProperties(verifyMessageExistenceById, message);
		messageDao.updateMessage(new MessageEntity(verifyMessageExistenceById));
	}

	private void copyPartialProperties(Message verifyPostExistenceById,
			Message post) {

		BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyPostExistenceById, post);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
