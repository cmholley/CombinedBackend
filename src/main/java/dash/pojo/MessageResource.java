package dash.pojo;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.service.MessageService;
import dash.service.TaskService;
import dash.service.UserService;

/*
 * Message Resource
 * @Author CarlSteven
 */

@Component("messageResource")
@Path("/messages")
public class MessageResource {

	@Autowired
	private MessageService messageService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createMessage(Message message) throws AppException {
		Task task = new Task();
		task.setId(message.getTask_id());
		Long createMessageId = messageService.createMessage(message, task);
		return Response.status(Response.Status.CREATED)
				// 201
				.entity("A new message has been created")
				.header("Location",
						"http://localhost:8080/messages/"
								+ String.valueOf(createMessageId)).build();
	}

	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createPosts(List<Message> messages) throws AppException {
		messageService.createMessages(messages);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of messages was successfully created").build();
	}

	/**
	 * @param numberOfPosts
	 *            -optional -default is 25 -the size of the List to be returned
	 *
	 * @param startIndex
	 *            -optional -default is 0 -the id of the post you would like to
	 *            start reading from
	 *
	 * @param group_id
	 *            -optional -if set will attempt to get the requested number of
	 *            posts from a group.
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Message> getMessages(
			@QueryParam("numberOfMessages") @DefaultValue("25") int numberOfMessages,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex,
			@QueryParam("task_id") Long task_id) throws IOException,
			AppException {
		Task task = taskService.getTaskById(task_id);
		List<Message> messages = messageService.getMessagesByTask(
				numberOfMessages, startIndex, task);
		return messages;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPostById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Message messageById = messageService.getMessageById(id);
		return Response.status(200)
				.entity(new GenericEntity<Message>(messageById) {
				}).header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}

	/************************ Update Methods *********************/

	// Full update or creation in not already existing
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putPostById(@PathParam("id") Long id, Message post)
			throws AppException {

		Task task = new Task();
		try {
			//check whether message exists
			messageService.getMessageById(id);
		} catch (AppException ex) {

			if (ex.getStatus() == 404) {
				// resource not existent yet, and should be created
				Long createPostId = messageService.createMessage(post, task);
				return Response
						.status(Response.Status.CREATED)
						// 201
						.entity("A new post has been created AT THE LOCATION you specified")
						.header("Location",
								"http://localhost:8080/services/posts/"
										+ String.valueOf(createPostId)).build();
			}

		}
		// message is existent and a full update should occur
		messageService.updateFullyMessage(post);
		return Response.status(Response.Status.OK)
				// 200
				.entity("The post with id: " + id + " has been fully updated.")
				.build();

	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdatePost(@PathParam("id") Long id, Message message)
			throws AppException {
		message.setId(id);
		Task task = new Task();
		if (message.getTask_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Must have set task_id")
					.header("Location",
							"http://localhost:8080/services/posts/"
									+ String.valueOf(message)).build();
		} else {
			task.setId(message.getTask_id());
		}
		messageService.updatePartiallyMessage(message, task);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The message you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePost(@PathParam("id") Long id) throws AppException {
		Task task = new Task();
		Message message = messageService.getMessageById(id);
		if (message.getTask_id() == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Post Id not found")
					.header("Location",
							"http://localhost:8080/services/messages/"
									+ String.valueOf(message)).build();
		} else {
			task.setId(message.getTask_id());
		}

		messageService.deleteMessage(message);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Post successfully removed from database").build();
	}

}
