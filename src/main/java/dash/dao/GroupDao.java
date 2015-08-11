package dash.dao;

import java.util.List;
import dash.pojo.Group;


/*
 * @Author tswensen
 */
public interface GroupDao {
	public List<Group> getGroups(String orderByInsertionDate);

	public List<Group> getRecentGroups(int numberOfDaysToLookBack);

	public int getNumberOfGroups();

	/**
	 * Returns a group given its id
	 *
	 * @param id
	 * @return
	 */
	public Group getGroupById(Long id);

	/**
	 * Find group by name
	 *
	 * @param group
	 * @return the group with the name specified or null if not existent
	 */
	public Group getGroupByName(String name);


	public void deleteGroupById(Group group);

	public Long createGroup(Group group);

	public void updateGroup(Group group);

	/** removes all groups */
	public void deleteGroups();

}
