package dash.pojo;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import dash.service.ClassService;
import dash.service.LocationService;
import dash.service.UserService;
import dash.tran.ClassSwitch;

@Component
@Path("/classes")
public class ClassResource {

	@Autowired
	private ClassService classService;
	
	@Autowired
	private ClassSwitch classTran;
	
	@Autowired
	private UserService userService;

	@Autowired
	private LocationService locationService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createClass(Class clas, @QueryParam(value = "ds") int ds) throws AppException {
		Location verifyLocation = locationService
				.verifyLocationExistenceById(clas.getLocation_id());
		if (verifyLocation != null) {
			Long createClassId = classService.createClass(clas, verifyLocation, ds);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new class has been created")
					.header("Location", String.valueOf(createClassId))
					.header("ObjectId", String.valueOf(createClassId)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("The location specified does not exist").build();
		}
	}

	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createClasses(List<Class> classes) throws AppException {
		Location verifyLocation = locationService
				.verifyLocationExistenceById(classes.get(0).getLocation_id());
		if (verifyLocation != null) {
			classService.createClasses(classes, verifyLocation);
			return Response.status(Response.Status.CREATED)
					.entity("List of classes was successfully created").build();
		} else {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("The location specified does not exist").build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Class> getClasses(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Class> classes = classService.getClasses(orderByInsertionDate,
				numberDaysToLookBack);
		return classes;
	}

	@GET
	@Path("byLocation")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Class> getClassesByLocation(
			@QueryParam("location") Location location) throws IOException,
			AppException {
		List<Class> classes = classService.getClassesByLocation(location);
		return classes;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClassById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed) throws IOException,
			AppException {
		Class classById = classService.getClassById(id);
		return Response.status(Response.Status.OK)
				.entity(new GenericEntity<Class>(classById) {
				}).header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}

	// TODO: Modify so it filters out completed classes by default
	@GET
	@Path("byMembership")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Class> getClassesByMembership(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException, AppException {
		List<Class> classes = classService.getClassesByMembership(
				orderByInsertionDate, numberDaysToLookBack);
		return classes;
	}

	/************************ Update Methods *********************/

	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateTask(@PathParam("id") Long id, Class clas, @QueryParam(value = "ds") int ds)
			throws AppException {
		clas.setId(id);
		try {
			classService.getClassById(id);
		} catch (AppException ex) {

			// resource not existent yet, and should be created
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Class Id not found")
					.header("Location", String.valueOf(clas)).build();
		}

		Location verifyLocation = locationService
				.verifyLocationExistenceById(clas.getLocation_id());
		if (verifyLocation != null) {
			classTran.updatePartiallyClass(clas, verifyLocation, ds);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The class you specified has been successfully updated")
					.build();
		} else {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("The location specified does not exist").build();
		}
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteClass(@PathParam("id") Long id, @QueryParam(value = "ds") int ds) throws AppException {

		try {
			Class clas = classService.getClassById(id);

			Location verifyLocation = locationService
					.verifyLocationExistenceById(clas.getLocation_id());
			if (verifyLocation != null) {
				classTran.deleteClass(clas, verifyLocation, ds);
				return Response.status(Response.Status.NO_CONTENT)
						// 204
						.entity("Class successfully removed from database")
						.build();
			} else {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("The location specified does not exist")
						.build();
			}
		} catch (AppException ex) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Class with Id: " + id + " not found.").build();
		}

	}

	@POST
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response addMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);

		try {
			Class clas = classService.getClassById(id);
			classService.addMember(user, clas);
			return Response
					.status(Response.Status.OK)
					.entity("MEMBER ADDED: User " + user.getUsername()
							+ " set as MEMBER for class " + clas.getId())
					.build();
		} catch (AppException ex) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Class with Id: " + id + " not found.").build();
		}
	}

	@DELETE
	@Path("{id}/MEMBER/{user}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteMember(@PathParam("user") Long userId,
			@PathParam("id") Long id) throws AppException {
		User user = userService.getUserById(userId);

		try {
			Class clas = classService.getClassById(id);
			classService.deleteMember(user, clas);
			return Response
					.status(Response.Status.OK)
					.entity("MEMBER deleted: User " + user.getUsername()
							+ " removed as MEMBER from class id " + clas.getId())
					.build();

		} catch (AppException ex) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Class with Id: " + id + " not found.").build();
		}

	}

}
