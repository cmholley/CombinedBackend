package dash.service;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import dash.errorhandling.AppException;
import dash.pojo.Comment;
import dash.pojo.Group;
import dash.pojo.Post;

public interface CommentService {
	/*
	 * ******************** Create related methods **********************
	 * 
	 * Create a new comment and set the current user as owner and manager.
	 */
	@PreAuthorize("hasPermission(#group, 'member') or hasPermission(#group, 'manager') or hasRole('ROLE_MODERATOR')")
	public Long createComment(Comment comment, Group group) throws AppException;

	/*
	 * Create multiple posts as ROOT, testing purposes only.
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void createComments(List<Comment> comments) throws AppException;

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
	public List<Comment> getCommentsByPost(int numberOfPosts, Long startIndex,
			Post post) throws AppException;

	/**
	 * Returns a post given its id
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Comment getCommentById(Long id) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	@PreAuthorize("hasPermission(#comment, 'write') or hasRole('ROLE_MODERATOR')")
	public void updateFullyComment(Comment comment) throws AppException;

	@PreAuthorize("hasPermission(#comment, 'write') or hasRole('ROLE_MODERATOR')")
	public void updatePartiallyComment(Comment comment) throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */
	@PreAuthorize("hasPermission(#comment, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deleteComment(Comment comment);

	/**
	 * removes all posts DO NOT USE, IMPROPERLY UPDATES ACL_OBJECT table
	 * Functional but does not destroy old acl's which doesnt hurt anything but
	 * they will take up space if this is commonly used
	 */
	@PreAuthorize("hasRole('ROLE_ROOT')")
	public void deleteComments();

	/*
	 * ******************** Helper methods **********************
	 */
	// TODO: This also should not exist, or it should be changed to
	// private/protected. Redundant
	// Could be made a boolean so it was not a security vulnerability
	public Comment verifyCommentExistenceById(Long id);

	public int getNumberOfPosts();

}
