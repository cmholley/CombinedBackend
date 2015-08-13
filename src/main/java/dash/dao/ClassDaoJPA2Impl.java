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

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import dash.pojo.Class;
import dash.pojo.Location;

public class ClassDaoJPA2Impl implements ClassDao {
	@PersistenceContext(unitName = "dashPersistenceCHW")
	private EntityManager entityManagerCHW;
	
	@PersistenceContext(unitName = "dashPersistenceVMA")
	private EntityManager entityManagerVMA;


	@Override
	public List<Class> getClasses(String orderByInsertionDate) {
		String sqlString = null;
		if(orderByInsertionDate != null){
			sqlString = "SELECT u FROM Class u"
					+ " ORDER BY u.creation_timestamp " + orderByInsertionDate;
		} else {
			sqlString = "SELECT u FROM Class u";
		}
		TypedQuery<Class> query = entityManagerCHW.createQuery(sqlString,
				Class.class);

		return query.getResultList();
	}

	@Override
	public List<Class> getRecentClasses(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+6"));
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);//substract the number of days to look back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT u FROM Class u where u.creation_timestamp > :dateToLookBackAfter ORDER BY u.creation_timestamp DESC";
		TypedQuery<Class> query = entityManagerCHW.createQuery(qlString,
				Class.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter, TemporalType.DATE);

		return query.getResultList();
	}

	@Override
	public Class getClassById(Long id) {

		try {
			String qlString = "SELECT u FROM Class u WHERE u.id = ?1";
			TypedQuery<Class> query = entityManagerCHW.createQuery(qlString,
					Class.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Class getClassByName(String name) {

		try {
			String qlString = "SELECT u FROM Class u WHERE u.name = ?1";
			TypedQuery<Class> query = entityManagerCHW.createQuery(qlString,
					Class.class);
			query.setParameter(1, name);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<Class> getClassesByLocation(Location location) {
		
		String qlString = "SELECT u FROM Class u where u.location_id = ?1";
		TypedQuery<Class> query = entityManagerCHW.createQuery(qlString,
				Class.class);
		query.setParameter(1, location.getId() );

		return query.getResultList();
	}


	@Override
	public void deleteClass(Class classPojo, int ds) {
		
		
		if(ds == 1){
			Class clas = entityManagerCHW
					.find(Class.class, classPojo.getId());
			entityManagerCHW.remove(clas);
			}
			else if (ds == 2){
				Class clas = entityManagerVMA
						.find(Class.class, classPojo.getId());
				entityManagerVMA.remove(clas);
			}
		
	}

	@Override
	public Long createClass(Class clas, int ds) {

		clas.setCreation_timestamp(new Date());
		if(ds == 1){
		entityManagerCHW.persist(clas);
		entityManagerCHW.flush();// force insert to receive the id of the user
		}
		else if (ds == 2){
			entityManagerVMA.persist(clas);
			entityManagerVMA.flush();
		}
		// Give admin over new group to the new group
		return clas.getId();
	}

	@Override
	public void updateClass(Class clas, int ds) {
		entityManagerCHW.merge(clas);
	}

	@Override
	public void deleteClasses() {
		Query query = entityManagerCHW.createNativeQuery("TRUNCATE TABLE classes");
		query.executeUpdate();
	}

	@Override
	public List<Class> getTodaysClasses() {
		String qlString = "SELECT u FROM Class u WHERE u.time BETWEEN :startTime AND :endTime";
		TypedQuery<Class> query = entityManagerCHW.createQuery(qlString, Class.class);
		
		Calendar cal;
		Date startTime, endTime;
		cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
		
        startTime = cal.getTime();
		
        cal.add(Calendar.DAY_OF_MONTH, 1);
		
        endTime = cal.getTime();
        
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		
		return query.getResultList();
	}

	@Override
	public List<String> getMembersForClass(Class clas) {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		
		String qlString = "SELECT acl_sid.sid FROM acl_entry"
				+ " JOIN acl_object_identity ON acl_entry.acl_object_identity = acl_object_identity.id"
				+ " JOIN acl_sid ON acl_entry.sid = acl_sid.id"
				+ " WHERE acl_object_identity.object_id_identity = :classId AND acl_object_identity.object_id_class = 5 AND acl_entry.mask = 64";
		SQLQuery query = session.createSQLQuery(qlString);
		query.setLong("classId", clas.getId());
		List<String> userNames = query.list();
		return userNames;
	}		
}
