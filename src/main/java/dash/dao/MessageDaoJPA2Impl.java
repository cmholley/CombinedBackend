package dash.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dash.pojo.Message;
import dash.pojo.Task;

/*
 * Message MessageDAO JPA2 Impl
 * @author CarlSteven
 */
public class MessageDaoJPA2Impl implements MessageDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<MessageEntity> getMessages(int numberOfMessages, Long startIndex) {
		String sqlString = null;

		sqlString = "SELECT u FROM MessageEntity u"
				+ " ORDER BY u.creation_timestamp ASC";

		TypedQuery<MessageEntity> query = entityManager.createQuery(sqlString,
				MessageEntity.class);
		query.setFirstResult(startIndex.intValue());
		query.setMaxResults(numberOfMessages);

		return query.getResultList();
	}

	@Override
	public List<MessageEntity> getMessages(int numberOfMessages,
			Long startIndex, Task task) {
		String qlString = "SELECT u FROM MessageEntity u where u.task_id = ?1 AND u.id > ?2 ORDER BY u.id ASC";
		TypedQuery<MessageEntity> query = entityManager.createQuery(qlString,
				MessageEntity.class);

		query.setMaxResults(numberOfMessages);
		query.setParameter(1, task.getId());
		query.setParameter(2, startIndex);

		List<MessageEntity> resultList = query.getResultList();

		return resultList;
	}

	@Override
	public int getNumberOfMessages() {
		try {
			String qlString = "SELECT COUNT(*) FROM message";
			TypedQuery<MessageEntity> query = entityManager.createQuery(
					qlString, MessageEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

	@Override
	public MessageEntity getMessageById(Long id) {
		try {
			String qlString = "SELECT u FROM MessageEntity u WHERE u.id = ?1";
			TypedQuery<MessageEntity> query = entityManager.createQuery(
					qlString, MessageEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteMessageById(Message message) {
		MessageEntity entity = entityManager.find(MessageEntity.class,
				message.getId());
		entityManager.remove(entity);
	}

	@Override
	public Long createMessage(MessageEntity message) {
		message.setCreation_timestamp(new Date());
		entityManager.persist(message);
		entityManager.flush();// force insert to receive the id of the post

		// Give admin over new post to the new post

		return message.getId();
	}

	@Override
	public void updateMessage(MessageEntity message) {
		// TODO think about partial update and full update
		// message.setCreation_timestamp(new Date());
		entityManager.merge(message);
	}

	@Override
	public void deleteMessages() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE message");
		query.executeUpdate();
	}

}
