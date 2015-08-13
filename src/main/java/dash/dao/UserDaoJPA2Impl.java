package dash.dao;

import java.io.File;
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

import dash.pojo.User;

@Component("userDao")
public class UserDaoJPA2Impl implements
UserDao {

	@PersistenceContext(unitName = "dashPersistenceCHW")
	private EntityManager entityManagerCHW;
	
	@PersistenceContext(unitName = "dashPersistenceVMA")
	private EntityManager entityManagerVMA;

	@Override
	public List<User> getUsers(String orderByInsertionDate) {
		String sqlString = null;
		if(orderByInsertionDate != null){
			sqlString = "SELECT u FROM User u"
					+ " ORDER BY u.insertionDate " + orderByInsertionDate;
		} else {
			sqlString = "SELECT u FROM User u";
		}
		TypedQuery<User> query = entityManagerCHW.createQuery(sqlString,
				User.class);

		return query.getResultList();
	}

	@Override
	public List<User> getRecentUsers(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+6"));
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);//substract the number of days to look back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT u FROM User u where u.insertionDate > :dateToLookBackAfter ORDER BY u.insertionDate DESC";
		TypedQuery<User> query = entityManagerCHW.createQuery(qlString,
				User.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter, TemporalType.DATE);

		return query.getResultList();
	}

	@Override
	public User getUserById(Long id) {

		try {
			String qlString = "SELECT u FROM User u WHERE u.id = ?1";
			TypedQuery<User> query = entityManagerCHW.createQuery(qlString,
					User.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public User getUserByName(String name) {

		try {
			String qlString = "SELECT u FROM User u WHERE u.username = ?1";
			TypedQuery<User> query = entityManagerCHW.createQuery(qlString,
					User.class);
			query.setParameter(1, name);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public String getRoleByName(String username){
		
		try{
			String qlString = "SELECT u.authority FROM AuthorityEntity u  WHERE u.username= ?1";
			TypedQuery<String> query = entityManagerCHW.createQuery(qlString, String.class);
			query.setParameter(1, username);
			
			return query.getSingleResult();
		}catch(NoResultException e){
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void deleteUserById(User userPojo, int ds) {
		
		if(ds == 1){
			User user = entityManagerCHW
				.find(User.class, userPojo.getId());
		entityManagerCHW.remove(user);
		}
		else if(ds == 2){
			User user = entityManagerVMA
					.find(User.class, userPojo.getId());
			entityManagerVMA.remove(user);
		}
		

	}

	@Override
	public Long createUser(User user, int ds) {

		user.setInsertionDate(new Date());
		
		if(ds == 1){
			entityManagerCHW.persist(user);
		entityManagerCHW.flush();// force insert to receive the id of the user		
			}
		else if(ds == 2){
			entityManagerVMA.persist(user);
			entityManagerVMA.flush();// force insert to receive the id of the user		
			}
		// create hashed folder name for documents
				String fileName = user.getId().toString();
				int hashcode = fileName.hashCode();
				int mask = 255;
				int firstDir = hashcode & mask;
				int secondDir = (hashcode >> 8) & mask;
				StringBuilder path = new StringBuilder(File.separator);
				path.append(String.format("%03d", firstDir));
				path.append(File.separator);
				path.append(String.format("%03d", secondDir));
				path.append(File.separator);
				path.append(fileName);
				user.setPicture(path.toString());
				entityManagerCHW.merge(user);
		// Give admin over new user to the new user
		 if(ds == 1){
				entityManagerCHW.merge(user);
					}
		else if(ds == 2){
				entityManagerCHW.merge(user);		
					}
	return user.getId();
	}

	@Override
	public void updateUser(User user, int ds) {
		//TODO think about partial update and full update
		if(ds == 1){
			entityManagerCHW.merge(user);
		}
		else if(ds == 2){
			entityManagerVMA.merge(user);
		}
		
	}
	
	public void updateUserRole(String role, String username){
		String qlString = "SELECT u FROM Authority u WHERE u.username = ?1";
		TypedQuery<AuthorityEntity> query = entityManagerCHW.createQuery(qlString,
				AuthorityEntity.class);
		query.setParameter(1, username);

		AuthorityEntity authority= query.getSingleResult();
		authority.setAuthority(role);
		entityManagerCHW.merge(authority);
	}
	

	@Override
	public void deleteUsers() {
		Query query = entityManagerCHW.createNativeQuery("TRUNCATE TABLE users");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfUsers() {
		try {
			String qlString = "SELECT COUNT(*) FROM users";
			TypedQuery<User> query = entityManagerCHW.createQuery(qlString,
					User.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}



}
