package dash.dao;

import java.util.List;
import dash.pojo.Comment;
import dash.pojo.Post;


/*
 * @Author CarlSteven
 */
public interface CommentDao {
	
	public List<Comment> getComments(int numberOfPosts, Long startIndex);
	
	public List<Comment> getComments(int numberOfPosts, Long startIndex, Post post);

	public int getNumberOfComments();

	/**
	 * Returns a post given its id
	 *
	 * @param id
	 * @return
	 */
	public Comment getCommentById(Long id);
	
	public void deleteCommentById(Comment comment, int ds);

	public Long createComment(Comment comment, int ds);

	public void updateComment(Comment comment);

	/** removes all posts */
	public void deleteComments();

}
