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
import dash.service.GroupService;
import dash.service.PostService;
import dash.service.UserService;
import dash.pojo.Group;

@Component("postResource")
@Path("/posts")
public class PostResource {

	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired GroupService groupService;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createPost(Post post) throws AppException {
		Group group= new Group();
		group.setId(post.getGroup_id());
		Long createPostId = postService.createPost(post, group);
		return Response.status(Response.Status.CREATED)
				// 201
				.entity("A new post has been created")
				.header("Location", String.valueOf(createPostId))
				.header("ObjectId", String.valueOf(createPostId)).build();
	}
	
	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createPosts(List<Post> posts) throws AppException {
		postService.createPosts(posts);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of posts was successfully created").build();
	}
	
	
	/**
	 *@param numberOfPosts
	 *-optional
	 *-default is 25
	 *-the size of the List to be returned
	 *
	 *@param startIndex
	 *-optional
	 *-default is 0
	 *-the id of the post you would like to start reading from
	 *
	 *@param group_id
	 *-optional
	 *-if set will attempt to get the requested number of posts from a group.
	 * 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Post> getPosts(
			@QueryParam("numberOfPosts") @DefaultValue("25") int numberOfPosts,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex,
			@QueryParam("group_id") Long group_id)
			throws IOException,	AppException 
	{
		if(group_id!=null){
			Group group = groupService.getGroupById(group_id);
			List<Post> posts= postService.getPostsByGroup(numberOfPosts, startIndex, group);
			return posts;
		}
		
		List<Post> posts = postService.getPosts(
				numberOfPosts, startIndex);
		return posts;
	}
	
	//Gets the specified number of posts from each of the groups the user is a part of.
	@GET
	@Path("myPosts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Post> getMyPosts(
			@QueryParam("numberOfPosts") @DefaultValue("25") int numberOfPosts,
			@QueryParam("startIndex") @DefaultValue("0") Long startIndex)
			throws IOException,	AppException 
	{
		
		
		List<Post> posts = postService.getPostsByMyGroups(numberOfPosts, startIndex);
		return posts;
	}
	
	
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPostById(@PathParam("id") Long id,
			@QueryParam("detailed") boolean detailed)
					throws IOException,	AppException {
		Post postById = postService.getPostById(id);
		return Response
				.status(200)
				.entity(postById)
						.header("Access-Control-Allow-Headers", "X-extra-header")
						.allow("OPTIONS").build();
	}
	
	/************************ Update Methods *********************/
	
	
	//Full update or creation in not already existing
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putPostById(@PathParam("id") Long id, Post post)
			throws AppException {

		Group group = new Group();
		Post postById = postService.verifyPostExistenceById(id);
		
		if (postById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createPostId = postService.createPost(post, group);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new post has been created AT THE LOCATION you specified")
					.header("Location", String.valueOf(createPostId))
					.header("ObjectId", String.valueOf(createPostId)).build();
		} else {
			// resource is existent and a full update should occur
			post.setId(id);
			postService.updateFullyPost(post);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The post you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/services/posts/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdatePost(@PathParam("id") Long id, Post post)
			throws AppException {
		post.setId(id);
		post.setGroup_id(postService.verifyPostExistenceById(id).getGroup_id());
		Group group = new Group();
		group.setId(post.getGroup_id());
		postService.updatePartiallyPost(post);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The post you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePost(@PathParam("id") Long id)
			throws AppException {
		Post post = postService.verifyPostExistenceById(id);
		
		
		postService.deletePost(post);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Post successfully removed from database").build();
	}

	@DELETE
	@Path("admin")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePosts() {
		postService.deletePosts();
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("All posts have been successfully removed").build();
	}
	
}
