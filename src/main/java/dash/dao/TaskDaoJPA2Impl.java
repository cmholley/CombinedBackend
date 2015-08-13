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
	@PersistenceContext(unitName = "dashPersistenceCHW")
	private EntityManager entityManagerCHW;
	
	@PersistenceContext(unitName = "dashPersistenceVMA")
	private EntityManager entityManagerVMA;

	@Override
	public List<Task> getTasks(String orderByInsertionDate) {
		String sqlString = null;

		if (orderByInsertionDate != null) {
			sqlString = "SELECT u FROM Task u"
					+ " ORDER BY u.creation_timestamp " + orderByInsertionDate;
		} else {
			sqlString = "SELECT u FROM Task u";
		}
		TypedQuery<Task> query = entityManagerCHW.createQuery(sqlString,
				Task.class);

		return query.getResultList();
	}

	@Override
	public List<Task> getRecentTasks(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+6"));
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);// substract the
																// number of
																// days to look
																// back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT u FROM Task u where u.creation_timestamp > :dateToLookBackAfter ORDER BY u.creation_timestamp DESC";
		TypedQuery<Task> query = entityManagerCHW
				.createQuery(qlString, Task.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter,
				TemporalType.DATE);

		return query.getResultList();
	}

	@Override
	public Task getTaskById(Long id) {

		try {
			String qlString = "SELECT u FROM Task u WHERE u.id = ?1";
			TypedQuery<Task> query = entityManagerCHW.createQuery(qlString,
					Task.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Task getTaskByName(String name) {
		try {
			String qlString = "SELECT u FROM Task u WHERE u.name = ?1";
			TypedQuery<Task> query = entityManagerCHW.createQuery(qlString,
					Task.class);
			query.setParameter(1, name);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Task> getTasksByGroup(Group group) {
		String qlString = "SELECT u FROM Task u where u.group_id = ?1";
		TypedQuery<Task> query = entityManagerCHW
				.createQuery(qlString, Task.class);
		query.setParameter(1, group.getId());
		return query.getResultList();
	}

	@Override
	public void deleteTaskById(Task groupPojo, int ds) {
		
		if(ds == 1){
			Task group = entityManagerCHW.find(Task.class, groupPojo.getId());
			entityManagerCHW.remove(group);
		}
		else if(ds == 2){
			Task group = entityManagerVMA.find(Task.class, groupPojo.getId());
			entityManagerVMA.remove(group);
		}
		
	}

	@Override
	public Long createTask(Task group, int ds) {
		group.setCreation_timestamp(new Date());
		if(ds == 1){
			entityManagerCHW.persist(group);
		entityManagerCHW.flush();// force insert to receive the id of the group
		}
		if(ds == 2){
			entityManagerVMA.persist(group);
			entityManagerVMA.flush();// force insert to receive the id of the group
		}
		
		// Give admin over new group to the new group
		return group.getId();
	}

	@Override
	public void updateTask(Task group, int ds) {
		// TODO think about partial update and full update
		if(ds == 1){
			entityManagerCHW.merge(group);
		}
		else if(ds == 2){
			entityManagerVMA.merge(group);
		}

		
	}

	@Override
	public void deleteTasks() {
		Query query = entityManagerCHW.createNativeQuery("TRUNCATE TABLE group");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfTasks() {
		try {
			String qlString = "SELECT COUNT(*) FROM group";
			TypedQuery<Task> query = entityManagerCHW.createQuery(qlString,
					Task.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

}
