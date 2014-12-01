package dash.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import dash.pojo.Group;
import dash.pojo.Task;

@Component("taskDao")
public class TaskDaoJPA2Impl implements TaskDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<TaskEntity> getTasks(String orderByInsertionDate) {
		String sqlString = null;
		if (orderByInsertionDate != null) {
			sqlString = "SELECT u FROM TaskEntity u"
					+ " ORDER BY u.creation_timestamp " + orderByInsertionDate;
		} else {
			sqlString = "SELECT u FROM TaskEntity u";
		}
		TypedQuery<TaskEntity> query = entityManager.createQuery(sqlString,
				TaskEntity.class);

		return query.getResultList();
	}

	@Override
	public List<TaskEntity> getRecentTasks(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+6"));
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);// substract the
																// number of
																// days to look
																// back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT u FROM TaskEntity u where u.creation_timestamp > :dateToLookBackAfter ORDER BY u.creation_timestamp DESC";
		TypedQuery<TaskEntity> query = entityManager.createQuery(qlString,
				TaskEntity.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter,
				TemporalType.DATE);

		return query.getResultList();
	}

	@Override
	public TaskEntity getTaskById(Long id) {

		try {
			String qlString = "SELECT u FROM TaskEntity u WHERE u.id = ?1";
			TypedQuery<TaskEntity> query = entityManager.createQuery(qlString,
					TaskEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public TaskEntity getTaskByName(String name) {

		try {
			String qlString = "SELECT u FROM TaskEntity u WHERE u.name = ?1";
			TypedQuery<TaskEntity> query = entityManager.createQuery(qlString,
					TaskEntity.class);
			query.setParameter(1, name);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<TaskEntity> getTasksByGroup(Group group) {

		String qlString = "SELECT u FROM TaskEntity u where u.group_id = ?1";
		TypedQuery<TaskEntity> query = entityManager.createQuery(qlString,
				TaskEntity.class);
		query.setParameter(1, group.getId());

		return query.getResultList();
	}

	@Override
	public void deleteTaskById(Task groupPojo) {

		TaskEntity group = entityManager.find(TaskEntity.class,
				groupPojo.getId());
		entityManager.remove(group);

	}

	@Override
	public Long createTask(TaskEntity group) {

		group.setCreation_timestamp(new Date());
		entityManager.persist(group);
		entityManager.flush();// force insert to receive the id of the group

		// Give admin over new group to the new group

		return group.getId();
	}

	@Override
	public void updateTask(TaskEntity group) {
		// TODO think about partial update and full update
		entityManager.merge(group);
	}

	@Override
	public void deleteTasks() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE group");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfTasks() {
		try {
			String qlString = "SELECT COUNT(*) FROM group";
			TypedQuery<TaskEntity> query = entityManager.createQuery(qlString,
					TaskEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

}
