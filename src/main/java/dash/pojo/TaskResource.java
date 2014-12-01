package dash.pojo;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.service.TaskService;
import dash.service.UserService;
import dash.pojo.Group;

@Component("taskResource")
@Path("/tasks")
public class TaskResource {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createTask(Task task) throws AppException {
		Group group = new Group();
		group.setId(task.getGroup_id());
		Long createTaskId = taskService.createTask(task, group);
		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity(String.valueOf(createTaskId))
				.header("Location", String.valueOf(createTaskId))
				.header("ObjectId", String.valueOf(createTaskId)).build();
	}

	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createTasks(List<Task> tasks) throws AppException {
		taskService.createTasks(tasks);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of tasks was successfully created").build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Task> getTasks(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Task> tasks = taskService.getTasks(orderByInsertionDate,
				numberDaysToLookBack);
		return tasks;
	}

	// TODO: Modify so it filters out completed tasks by default
	@GET
	@Path("byMembership")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Task> getTasksByMembership(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Task> tasks = taskService.getTasksByMembership(
				orderByInsertionDate, numberDaysToLookBack);
		return tasks;
	}

	// TODO: Modify so it filters out completed tasks by default
	@GET
	@Path("byManager")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Task> getTasksByManager(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Task> tasks = taskService.getTasksByManager(orderByInsertionDate,
				numberDaysToLookBack);
		return tasks;
	}

	@GET
	@Path("byGroup/{groupId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Task> getTasksByGroup(@PathParam("groupId") Long id)
			throws IOException, AppException {
		Group group = new Group();
		group.setId(id);
		List<Task> tasks = taskService.getTasksByGroup(group);
		return tasks;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTaskById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Task taskById = taskService.getTaskById(id);
		return Response.status(200).entity(taskById)
				.header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}

	/************************ Update Methods *********************/

	// Full update or creation in not already existing
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putTaskById(@PathParam("id") Long id, Task task)
			throws AppException {
		task.setId(id);
		Group group = new Group();
		Task taskById = taskService.verifyTaskExistenceById(id);

		if (taskById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createTaskId = taskService.createTask(task, group);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new task has been created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(createTaskId)).build();
		} else {
			// resource is existent and a full update should occur
			task.setGroup_id(taskById.getGroup_id());
			group.setId(task.getGroup_id());
			taskService.updateFullyTask(task, group);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The task you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/services/tasks/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateTask(@PathParam("id") Long id, Task task)
			throws AppException {
		task.setId(id);
		Group group = new Group();
		Task taskById = taskService.verifyTaskExistenceById(id);

		if (taskById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location", String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}
		taskService.updatePartiallyTask(task, group);
		return Response.status(Response.Status.OK)
				// 200
				.entity("The task you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteTask(@PathParam("id") Long id) throws AppException {
		Group group = new Group();
		Task task = taskService.verifyTaskExistenceById(id);
		if (task.getGroup_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}

		taskService.deleteTask(task, group);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Task successfully removed from database").build();
	}

	@DELETE
	@Path("admin")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteTasks() {
		taskService.deleteTasks();
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("All tasks have been successfully removed").build();
	}

	@PUT
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response resetManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		Task task = taskService.verifyTaskExistenceById(id);
		if (task.getGroup_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}
		taskService.resetManager(user, task);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER RESET: User " + user.getUsername()
						+ " set as sole MANAGER for task " + task.getId())
				.build();
	}

	@POST
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		Task task = taskService.verifyTaskExistenceById(id);
		if (task.getGroup_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}
		taskService.addManager(user, task, group);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER ADDED: User " + user.getUsername()
						+ " added as a MANAGER for task " + task.getId())
				.build();
	}

	@DELETE
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		Task task = taskService.verifyTaskExistenceById(id);
		if (task.getGroup_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}
		taskService.deleteManager(user, task, group);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER DELETED: User " + user.getUsername()
						+ " removed as MANAGER for task " + task.getId())
				.build();
	}

	// TODO: Implement mechanism to limit the number of people that can sign up
	// for a task.
	@POST
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Task task = taskService.verifyTaskExistenceById(id);
		taskService.addMember(user, task);
		return Response
				.status(Response.Status.OK)
				.entity("MEMBER ADDED: User " + user.getUsername()
						+ " set as MEMBER for task " + task.getId()).build();
	}

	// TODO: Implement mechanism to limit the number of people that can sign up
	// for a task.
	@DELETE
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		Task task = taskService.verifyTaskExistenceById(id);
		if (task.getId() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Task Id not found")
					.header("Location",
							"http://localhost:8080/services/tasks/"
									+ String.valueOf(task)).build();
		} else {
			group.setId(task.getGroup_id());
		}
		taskService.deleteMember(user, task, group);
		return Response
				.status(Response.Status.OK)
				.entity("MEMBER DELETED: User " + user.getUsername()
						+ " removed as MEMBER from task " + task.getId())
				.build();
	}

}
