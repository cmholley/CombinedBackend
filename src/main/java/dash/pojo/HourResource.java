package dash.pojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dash.errorhandling.AppException;
import dash.filters.AppConstants;
import dash.service.GroupService;
import dash.service.HourService;
import dash.service.TaskService;
import dash.service.UserService;

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
	
	private static final String hourPicturePathCHW = AppConstants.APPLICATION_UPLOAD_LOCATION_FOLDER_CHW
			+ "/hours";
	
	private static final String hourPicturePathVMA = AppConstants.APPLICATION_UPLOAD_LOCATION_FOLDER_CHW
			+ "/hours";

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	public Response createHour(Hour hour) throws AppException {
		Long createHourId = hourService.createHour(hour);
		hour.setId(createHourId);
		return Response.status(Response.Status.CREATED)
				// 201
				.entity(new GenericEntity<Hour>(hour) {
				}).header("Location", String.valueOf(createHourId))
				.header("ObjectId", String.valueOf(createHourId)).build();
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
	@Produces({ MediaType.APPLICATION_JSON })
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
	@Produces({ MediaType.APPLICATION_JSON })
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
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getHourById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Hour hourById = hourService.getHourById(id);
		return Response.status(200).entity(new GenericEntity<Hour>(hourById) {
		}).header("Access-Control-Allow-Headers", "X-extra-header")

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

		try {
			Hour hourById = hourService.getHourById(id);
			// hour entry already existent and a full update should occur
			hour.setId(id);
			try {
				Task task = taskService.getTaskById(hourById.getTask_id());
				Group group = new Group();
				group.setId(task.getGroup_id());
				hourService.updateFullyHour(hour, group);
			} catch (AppException e) {
				hourService.updateFullyHour(hour);
			}
		} catch (AppException ex) {
			if (ex.getStatus() == 404) {
				// resource not existent yet, and should be created
				Long createHourId = hourService.createHour(hour);
				return Response
						.status(Response.Status.CREATED)
						// 201
						.entity("A new hour entry with id " + createHourId
								+ " has been created").build();
			}
		}

		return Response.status(Response.Status.OK)
				// 200
				.entity("The hour with id " + id + " has been fully updated.")
				.build();

	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateHour(@PathParam("id") Long id, Hour hour)
			throws AppException {
		hour.setId(id);
		// verify whether hour entry exists
		hourService.getHourById(id);

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
		Hour hour = hourService.getHourById(id);
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
		Hour hour = hourService.getHourById(id);

		hourService.deleteHour(hour);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Hour successfully removed from database").build();
	}

	@POST
	@Path("/upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadFile(@QueryParam("id") Long id,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@HeaderParam("Content-Length") final long fileSize)
			throws AppException {

		Hour hour = hourService.getHourById(id);

		// TODO: Generate directory if not set
		if (hour.getPicturePath() == null) {
			String fileName = hour.getId().toString();
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
			hour.setPicturePath(path.toString().toLowerCase());
			partialUpdateHour(hour.getId(), hour);
		}

		if (!hourService.getFileNames(hour).isEmpty()) {
			List<String> files = hourService.getFileNames(hour);
			for (String file : files) {
				deleteUpload(hour.getId(), file);
			}
		}
		//TODO: Switch statement for datasources to select proper path
		String uploadedFileLocation = hourPicturePathCHW + "/"
				+ hour.getPicturePath() + "/"
				+ fileDetail.getFileName().replaceAll("%20", "_").toLowerCase();
		;
		// save it
		hourService.uploadFile(uploadedInputStream, uploadedFileLocation, hour);

		String output = "File uploaded to : " + uploadedFileLocation;
		hour.setProfile_picture_filename(fileDetail.getFileName().toLowerCase());
		hourService.updatePartiallyHour(hour);
		return Response.status(200).entity(output).build();

	}

	@GET
	@Path("/upload")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getFileNames(@QueryParam("hourId") Long id)
			throws AppException {

		Hour hour = hourService.getHourById(id);
		JaxbList<String> fileNames = new JaxbList<String>(
				hourService.getFileNames(hour));
		return Response.status(200).entity(fileNames).build();
	}

	// //Gets a specific file and allows the hour to download the pdf
	// @GET
	// @Path("/upload")
	// public Response getFile(@QueryParam("hourId") Long id,
	// @QueryParam("fileName") String fileName) throws AppException {
	//
	// Hour hour= hourService.getHourById(id);
	//
	// if(hour==null){
	// return Response.status(Response.Status.BAD_REQUEST)
	// .entity("Invalid hourId, unable to locate hour with id: "+id).build();
	// }
	//
	// String uploadedFileLocation =
	// AppConstants.APPLICATION_UPLOAD_LOCATION_FOLDER+hour.getPicture()+"/" +
	// fileName;
	//
	//
	// return Response.ok(hourService.getUploadFile(uploadedFileLocation, hour))
	// .type("application/pdf").build();
	// }

	@DELETE
	@Path("/upload")
	public Response deleteUpload(@QueryParam("hourId") Long id,
			@QueryParam("fileName") String fileName) throws AppException {

		Hour hour = hourService.getHourById(id);

		//TODO: Switch statement for datasources to select proper path
		String uploadedFileLocation = hourPicturePathCHW + "/"
				+ hour.getPicturePath() + "/" + fileName;
		// save it
		hourService.deleteUploadFile(uploadedFileLocation, hour);

		String output = "File removed from: " + uploadedFileLocation;
		hour.setProfile_picture_filename("");
		hourService.updatePartiallyHour(hour);
		return Response.status(200).entity(output).build();
	}

	public void sethourService(HourService hourService) {
		this.hourService = hourService;
	}

	@XmlRootElement(name = "fileNames")
	public static class JaxbList<T> {
		protected List<T> list;

		public JaxbList() {
		}

		public JaxbList(List<T> list) {
			this.list = list;
		}

		@XmlElement(name = "fileName")
		public List<T> getList() {
			return list;
		}
	}

}
