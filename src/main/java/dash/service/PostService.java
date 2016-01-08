package dash.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Post;

public interface PostService {
	/*
	 * ******************** Create related methods **********************
	 */

	/**
	 * Create a new post and set the current user as owner and manager.
	 * 
	 * @param post
	 * @param group
	 * @return
	 * @throws AppException
	 */
	@PreAuthorize("hasPermission(#group, 'member') or hasPermission(#group, 'manager')")
	public Long createPost(Post post, Group group) throws AppException;

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

	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfPosts();

}
