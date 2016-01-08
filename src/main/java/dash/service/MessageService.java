package dash.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Message;
import dash.pojo.Task;

/*
 * Message Service
 * @Author CarlSteven
 */
public interface MessageService {
	/*
	 * ******************** Create related methods **********************
	 * 
	 * Create a new post and set the current user as owner and manager.
	 */
	@PreAuthorize("hasPermission(#task, 'member') or hasPermission(#task, 'manager')")
	public Long createMessage(Message message, Task task) throws AppException;

	/*
	 * Create multiple posts as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createMessages(List<Message> messages) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	// public List<Post> getMessagesByMyTasks(int numberOfPosts, Long
	// startIndex) throws AppException;

	@PreAuthorize("hasPermission(#task, 'member') or hasPermission(#task, 'manager')")
	public List<Message> getMessagesByTask(int numberOfPosts, Long startIndex, Task task) throws AppException;

	/**
	 * Returns a post given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Message getMessageById(Long id) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#message, 'write') or hasRole('ROLE_MODERATOR')")
	public void updateFullyMessage(Message message) throws AppException;

	@PreAuthorize("(hasPermission(#message, 'write') or hasRole('ROLE_MODERATOR')) and (hasPermission(#task, 'member') or hasPermission(#task, 'manager'))")
	public void updatePartiallyMessage(Message message, Task task) throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */
	@PreAuthorize("hasPermission(#message, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deleteMessage(Message message);

	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfMessages();

}
