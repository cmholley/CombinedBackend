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
import javax.ws.rs.DefaultValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.service.GroupService;
import dash.service.HourService;
import dash.service.TaskService;
import dash.service.UserService;
import dash.pojo.Group;

@Component("hourResource")
@Path("/hours")
public class HourResource {

	@Autowired
	private HourService hourService;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private TaskService taskService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createHour(Hour hour) throws AppException {
		Long createHourId = hourService.createHour(hour);
		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity("A new hour has been created")
				.header("Location", String.valueOf(createHourId))
				.header("ObjectId", String.valueOf(createHourId)).build();
	}

	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createHours(List<Hour> hours) throws AppException {
		hourService.createHours(hours);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of hours was successfully created").build();
	}

	/**
	 * @param numberOfHours
	 *            -optional -default is 25 -the size of the List to be returned
	 *
	 * @param startIndex
	 *            -optional -default is 0 -the id of the hour you would like to
	 *            start reading from
	 *
	 * @param group_id
	 *            -optional -if set will attempt to get the requested number of
	 *            hours from a group.
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Hour> getHours(
			@QueryParam("numberOfHours") @DefaultValue("25") int numberOfHours,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex,
			@QueryParam("group_id") Long group_id,
			@QueryParam("onlyPending") @DefaultValue("true") boolean onlyPending)
			throws IOException, AppException {
		if (group_id != null) {
			Group group = groupService.getGroupById(group_id);
			List<Hour> hours = hourService.getHoursByGroup(numberOfHours,
					startIndex, group, onlyPending);
			return hours;
		}

		List<Hour> hours = hourService.getHours(numberOfHours, startIndex,
				onlyPending);
		return hours;
	}

	// Gets the specified number of hours from each of the groups the user is a
	// part of.
	@GET
	@Path("myHours")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Hour> getMyHours(
			@QueryParam("numberOfHours") @DefaultValue("25") int numberOfHours,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex,
			@QueryParam("onlyPending") @DefaultValue("true") boolean onlyPending)
			throws IOException, AppException {

		List<Hour> hours = hourService.getHoursByMyUser(numberOfHours,
				startIndex, onlyPending);
		return hours;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getHourById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Hour hourById = hourService.getHourById(id);
		return Response.status(200).entity(hourById)
				.header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}

	/************************ Update Methods *********************/

	// Full update or creation in not already existing
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putHourById(@PathParam("id") Long id, Hour hour)
			throws AppException {
		Hour hourById = hourService.verifyHourExistenceById(id);

		if (hourById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createHourId = hourService.createHour(hour);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new hour has been created")
					.header("Location", String.valueOf(createHourId))
					.header("ObjectId", String.valueOf(createHourId)).build();
		} else {
			// resource is existent and a full update should occur
			hour.setId(id);
			try {
				Task task = taskService.getTaskById(hourById.getTask_id());
				Group group = new Group();
				group.setId(task.getGroup_id());
				hourService.updateFullyHour(hour, group);
			} catch (AppException e) {
				hourService.updateFullyHour(hour);
			}

			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The hour you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/services/hours/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateHour(@PathParam("id") Long id, Hour hour)
			throws AppException {
		hour.setId(id);
		Hour hourById = hourService.verifyHourExistenceById(id);
		if (hourById == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource with this id does not exist in the database",
					"Please verify existence of hours in the database for the id - "
							+ hour.getTask_id(), AppConstants.DASH_POST_URL);
		}
		try {
			Task task = taskService.getTaskById(hour.getTask_id());
			Group group = new Group();
			group.setId(task.getGroup_id());
			hourService.updatePartiallyHour(hour, group);
		} catch (AppException e) {
			hourService.updateFullyHour(hour);
		}
		return Response.status(Response.Status.OK)
				// 200
				.entity("The hour you specified has been successfully updated")
				.build();
	}

	@POST
	@Path("approve/{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response approveHour(@PathParam("id") Long id,
			@QueryParam("isApproved") boolean isApproved) throws AppException {
		Hour hour = hourService.verifyHourExistenceById(id);
		if (hour == null) {
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
					404,
					"The resource with this id does not exist in the database",
					"Please verify existence of hours in the database for the id - "
							+ id, AppConstants.DASH_POST_URL);
		}
		try {
			Task task = taskService.getTaskById(hour.getTask_id());
			Group group = new Group();
			group.setId(task.getGroup_id());
			hourService.approveHour(hour, group, isApproved);
		} catch (AppException e) {
			hourService.approveHour(hour, isApproved);
		}

		return Response.status(Response.Status.OK)
				// 200
				.entity("The hour you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteHour(@PathParam("id") Long id) throws AppException {
		Hour hour = hourService.verifyHourExistenceById(id);

		hourService.deleteHour(hour);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Hour successfully removed from database").build();
	}

	@DELETE
	@Path("admin")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteHours() {
		hourService.deleteHours();
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("All hours have been successfully removed").build();
	}

}
