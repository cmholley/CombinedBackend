package dash.tran;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Post;
import dash.pojo.Task;
import dash.service.PostService;
import dash.service.TaskService;

public class TaskTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public Long createTask1(Task task, Group group, int ds, TaskService taskService) throws AppException {
		return taskService.createTask(task, group, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public Long createTask2(Task task, Group group, int ds, TaskService taskService) throws AppException {
		return taskService.createTask(task, group, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteTask1(Task task, Group group, int ds, TaskService taskService) throws AppException {
		taskService.deleteTask(task, group, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void deleteTask2(Task task, Group group, int ds, TaskService taskService) throws AppException {
		taskService.deleteTask(task, group, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updatePartiallyTask1(Task task, Group group, int ds, TaskService taskService) throws AppException {
		taskService.updatePartiallyTask(task, group, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void updatePartiallyTask2(Task task, Group group, int ds, TaskService taskService) throws AppException {
		taskService.updatePartiallyTask(task, group, ds);
	}
}
