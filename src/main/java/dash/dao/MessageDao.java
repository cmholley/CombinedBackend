package dash.dao;

import java.util.List;

import dash.pojo.Message;
import dash.pojo.Task;

/*
 * Message DAO
 * @Author CarlSteven
 */
public interface MessageDao {

	public List<Message> getMessages(int numberOfMessages, Long startIndex);

	public List<Message> getMessages(int numberOfMessages, Long startIndex, Task task);

	public int getNumberOfMessages();

	/**
	 * Returns a message given its id
	 *
	 * @param id
	 * @return
	 */
	public Message getMessageById(Long id);

	public void deleteMessageById(Message message);

	public Long createMessage(Message message);

	public void updateMessage(Message message);

	/** removes all posts */
	public void deleteMessages();

}
