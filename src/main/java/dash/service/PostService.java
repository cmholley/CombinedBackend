package dash.service;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Post;

public interface PostService {
	/*
	 * ******************** Create related methods **********************
	 *
	 *Create a new post and set the current user as owner and manager.
	 */
	@PreAuthorize("hasPermission(#group, 'member') or hasPermission(#group, 'manager')")
	public Long createPost(Post post, Group group) throws AppException;

	/*
	 * Create multiple posts as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createPosts(List<Post> posts) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	/**
	 *
	 * @param orderByInsertionDate
	 *            - if set, it represents the order by criteria (ASC or DESC)
	 *            for displaying posts
	 * @param numberDaysToLookBack
	 *            - if set, it represents number of days to look back for posts,
	 *            null
	 * @return list with posts corresponding to search criteria
	 * @throws AppException
	 */
	
	public List<Post> getPosts(int numberOfPosts, Long startIndex) throws AppException;
	
	public List<Post> getPostsByMyGroups(int numberOfPosts, Long startIndex) throws AppException;
	
	public List<Post> getPostsByGroup(int numberOfPosts, Long startIndex, Group group) throws AppException;
	/**
	 * Returns a post given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	
	
	
	public Post getPostById(Long id) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#post, 'write') or hasRole('ROLE_MODERATOR')")
	public void updateFullyPost(Post post) throws AppException;

	@PreAuthorize("hasPermission(#post, 'write') or hasRole('ROLE_MODERATOR')")
	public void updatePartiallyPost(Post post) throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */


	@PreAuthorize("hasPermission(#post, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deletePost(Post post);
	/** removes all posts
	 * DO NOT USE, IMPROPERLY UPDATES ACL_OBJECT table
	 * Functional but does not destroy old acl's which doesnt hurt anything
	 * but they will take up space if this is commonly used */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void deletePosts();
	
	
	/*
	 * ******************** Helper methods **********************
	 */
	// TODO: This also should not exist, or it should be changed to
	// private/protected. Redundant
	// Could be made a boolean so it was not a security vulnerability
	public Post verifyPostExistenceById(Long id);

	public int getNumberOfPosts();

}
