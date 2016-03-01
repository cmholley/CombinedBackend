package dash.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import dash.service.CommentService;
import dash.service.GroupService;
import dash.service.PostService;

@Component("commentResource")
@Path("/comments")
public class CommentResource {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private GroupService groupService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createComments(Comment comment) throws AppException {
		Post post = postService.getPostById(comment.getPost_id());
		Group group = groupService.getGroupById(post.getGroup_id());

		Long createCommentId = commentService.createComment(comment, group);
		return Response.status(Response.Status.CREATED)
				// 201
				.entity("A new comment has been created")
				.header("Location", "http://localhost:8080/comments/" + String.valueOf(createCommentId)).build();
	}

	/**
	 * @param numberOfComments
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
	public List<Comment> getComments(@QueryParam("numberOfComments") @DefaultValue("25") int numberOfComments,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex, @QueryParam("post_id") Long post_id)
					throws IOException, AppException {
		if (post_id != null) {
			Post post = postService.getPostById(post_id);
			List<Comment> comments = commentService.getCommentsByPost(numberOfComments, startIndex, post);
			return comments;
		} else {
			return new ArrayList<Comment>();
		}
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCommentById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
			throws IOException, AppException {
		Comment commentById = commentService.getCommentById(id);
		return Response.status(200).entity(new GenericEntity<Comment>(commentById) {
		}).header("Access-Control-Allow-Headers", "X-extra-header,X-TenantId").allow("OPTIONS").build();
	}

	/************************ Update Methods *********************/
	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdatePost(@PathParam("id") Long id, Comment comment) throws AppException {
		comment.setId(id);
		Post post = new Post();
		if (comment.getPost_id() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Must have set post_id")
					.header("Location", "http://localhost:8080/services/posts/" + String.valueOf(post)).build();
		} else {
			post.setId(comment.getPost_id());
		}
		commentService.updatePartiallyComment(comment);
		return Response.status(Response.Status.OK)
				// 200
				.entity("The comment you specified has been successfully updated").build();
	}

	/*
	 * *********************************** DELETE
	 * ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteComment(@PathParam("id") Long id) throws AppException {
		Comment comment = commentService.getCommentById(id);
		commentService.deleteComment(comment);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Post successfully removed from database").build();
	}

}
