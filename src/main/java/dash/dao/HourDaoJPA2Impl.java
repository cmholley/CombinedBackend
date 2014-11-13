package dash.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dash.pojo.Group;
import dash.pojo.Hour;
import dash.pojo.Task;

public class HourDaoJPA2Impl implements HourDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<HourEntity> getHours(int numberOfHours, Long startIndex,
			boolean onlyPending, String orderBy) {
		String sqlString = null;
		String qPending = new String();
		if (onlyPending) {
			qPending = "where u.pending = 1";
		}
		sqlString = "SELECT u FROM HourEntity u " + qPending
				+ " ORDER BY u.id " + orderBy;

		TypedQuery<HourEntity> query = entityManager.createQuery(sqlString,
				HourEntity.class);
		query.setFirstResult(startIndex.intValue());
		query.setMaxResults(numberOfHours);
		return query.getResultList();
	}

	@Override
	public List<HourEntity> getHours(int numberOfHours, Long startIndex,
			Task task, boolean onlyPending) {
		String qPending = new String();
		if (onlyPending) {
			qPending = "AND u.pending = 1";
		}
		String qlString = "SELECT u FROM HourEntity u where u.task_id = ?1 "
				+ qPending + " ORDER BY u.id ASC";
		TypedQuery<HourEntity> query = entityManager.createQuery(qlString,
				HourEntity.class);
		query.setFirstResult(startIndex.intValue());
		query.setMaxResults(numberOfHours);
		query.setParameter(1, task.getId());

		return query.getResultList();
	}

	@Override
	public HourEntity getHourById(Long id) {

		try {
			String qlString = "SELECT u FROM HourEntity u WHERE u.id = ?1";
			TypedQuery<HourEntity> query = entityManager.createQuery(qlString,
					HourEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteHourById(Hour hourPojo) {

		HourEntity hour = entityManager
				.find(HourEntity.class, hourPojo.getId());
		entityManager.remove(hour);

	}

	@Override
	public Long createHour(HourEntity hour) {

		entityManager.persist(hour);
		entityManager.flush();// force insert to receive the id of the hour

		return hour.getId();
	}

	@Override
	public void updateHour(HourEntity hour) {

		entityManager.merge(hour);
	}

	@Override
	public void deleteHours() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE hour");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfHours() {
		try {
			String qlString = "SELECT COUNT(*) FROM hour";
			TypedQuery<HourEntity> query = entityManager.createQuery(qlString,
					HourEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}
}
