package dash.dao;

import java.util.List;

import dash.pojo.Class;
import dash.pojo.Location;


/*
 * @Author tswensen
 */
public interface ClassDao {
	public List<Class> getClasses(String orderByInsertionDate);

	public List<Class> getRecentClasses(int numberOfDaysToLookBack);

	public Class getClassById(Long id);

	/**
	 * Find class by name
	 *
	 * @param class
	 * @return the class with the name specified or null if not existent
	 */
	public Class getClassByName(String name);
	
	/**
	 * Returns all of the classes by a given location
	 * 
	 * @param location
	 * @return the list of classess
	 * 
	 */
	public List<Class> getClassesByLocation(Location location);

	public void deleteClass(Class clas);

	public Long createClass(Class classentity);

	public void updateClass(Class classentity);

	/** removes all classes */
	public void deleteClasses();

	public List<Class> getTodaysClasses();

	public List<String> getMembersForClass(Class clas);

}
