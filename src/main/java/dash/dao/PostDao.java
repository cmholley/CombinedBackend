package dash.dao;

import java.util.List;
import dash.pojo.Group;
import dash.pojo.Post;

/*
 * @Author tswensen
 */
public interface PostDao {

	public List<Post> getPosts(int numberOfPosts, Long startIndex);

	public List<Post> getPosts(int numberOfPosts, Long startIndex, Group group);

	public int getNumberOfPosts();

	/**
	 * Returns a post given its id
	 *
	 * @param id
	 * @return
	 */
	public Post getPostById(Long id);

	public void deletePostById(Post post);

	public Long createPost(Post post);

	public void updatePost(Post post);

	/** removes all posts */
	public void deletePosts();

}
