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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.service.GroupService;
import dash.service.UserService;

@Component("groupResource")
@Path("/groups")
public class GroupResource {

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createGroup(Group group) throws AppException {
		Long createGroupId = groupService.createGroup(group);
		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity(String.valueOf(createGroupId))
				.header("Location", String.valueOf(createGroupId))
				.header("ObjectId", String.valueOf(createGroupId)).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Group> getGroups(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Group> groups = groupService.getGroups(orderByInsertionDate,
				numberDaysToLookBack);
		return groups;
	}

	@GET
	@Path("byMembership")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Group> getGroupsByMembership(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Group> groups = groupService.getGroupsByMembership(
				orderByInsertionDate, numberDaysToLookBack);
		return groups;
	}

	@GET
	@Path("byManager")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Group> getGroupsByManager(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Group> groups = groupService.getGroupsByManager(
				orderByInsertionDate, numberDaysToLookBack);
		return groups;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getGroupById(@PathParam("id") Long id)
					throws IOException,	AppException {
		Group groupById = groupService.getGroupById(id);
		return Response
				.status(200)
				.entity(new GenericEntity<Group>(groupById) {
				})
						.header("Access-Control-Allow-Headers", "X-extra-header")
						.allow("OPTIONS").build();

	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putGroupById(@PathParam("id") Long id, Group group)
			throws AppException {

		Group groupById = groupService.getGroupById(id);

		if (groupById == null) {
			// resource not existent yet, and should be created under the specified URI
			Long createGroupId = groupService.createGroup(group);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new group has been created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8080/services/groups/"
									+ String.valueOf(createGroupId)).build();
		} else {
			// resource is existent and a full update should occur
			groupService.updateFullyGroup(group);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The group you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/services/groups/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateGroup(@PathParam("id") Long id, Group group)
			throws AppException {
		group.setId(id);
		groupService.updatePartiallyGroup(group);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The group you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteGroup(@PathParam("id") Long id) throws AppException {
		Group group = new Group();
		group.setId(id);
		groupService.deleteGroup(group);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Group successfully removed from database").build();
	}

	@PUT
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response resetManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		group.setId(id);
		groupService.resetManager(user, group);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER RESET: User " + user.getUsername()
						+ " set as sole MANAGER for group " + group.getId())
				.build();
	}

	@POST
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		group.setId(id);
		groupService.addManager(user, group);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER ADDED: User " + user.getUsername()
						+ " added as a MANAGER for group " + group.getId())
				.build();
	}

	@DELETE
	@Path("{id}/MANAGER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteManager(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		group.setId(id);
		groupService.deleteManager(user, group);
		return Response
				.status(Response.Status.OK)
				.entity("MANAGER DELETED: User " + user.getUsername()
						+ " removed as MANAGER for group " + group.getId())
				.build();
	}

	@POST
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		group.setId(id);
		groupService.addMember(user, group);
		return Response
				.status(Response.Status.OK)
				.entity("MEMBER ADDED: User " + user.getUsername()
						+ " set as MEMBER for group " + group.getId()).build();
	}

	@DELETE
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);
		Group group = new Group();
		group.setId(id);
		groupService.deleteMember(user, group);
		return Response
				.status(Response.Status.OK)
				.entity("MEMBER DELETED: User " + user.getUsername()
						+ " removed as MEMBER from group " + group.getId())
				.build();
	}

}
