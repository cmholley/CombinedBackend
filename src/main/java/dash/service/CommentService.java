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
	public List<Comment> getCommentsByPost(int numberOfPosts, Long startIndex, Post post) throws AppException;


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
	public void updatePartiallyComment(Comment comment) throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */
	@PreAuthorize("hasPermission(#comment, 'delete') or hasRole('ROLE_MODERATOR')")
	public void deleteComment(Comment comment);

	/*
	 * ******************** Helper methods **********************
	 */
	public int getNumberOfPosts();

}
