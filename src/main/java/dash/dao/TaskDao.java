package dash.dao;

import java.util.List;
import dash.pojo.Group;
import dash.pojo.Task;

/*
 * @Author tswensen
 */
public interface TaskDao {
	public List<Task> getTasks(String orderByInsertionDate);

	public List<Task> getRecentTasks(int numberOfDaysToLookBack);

	public int getNumberOfTasks();

	/**
	 * Returns a task given its id
	 *
	 * @param id
	 * @return
	 */
	public Task getTaskById(Long id);

	/**
	 * Find task by name
	 *
	 * @param task
	 * @return the task with the name specified or null if not existent
	 */
	public Task getTaskByName(String name);

	/**
	 * Returns all of the taskes owned by a given group
	 * 
	 * @param group
	 * @return the list of tasks
	 * 
	 */
	public List<Task> getTasksByGroup(Group group);

	public void deleteTaskById(Task task);

	public Long createTask(Task task);

	public void updateTask(Task task);

	/** removes all tasks */
	public void deleteTasks();

}
