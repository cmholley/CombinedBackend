package dash.tran;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Message;
import dash.pojo.Post;
import dash.pojo.Task;
import dash.service.MessageService;
import dash.service.PostService;

public class PostTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public Long createPost1(Post post, Group group, int ds, PostService postService) throws AppException {
		return postService.createPost(post, group, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public Long createPost2(Post post, Group group, int ds, PostService postService) throws AppException {
		return postService.createPost(post, group, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deletePost1(Post post, int ds, PostService postService) throws AppException{
		postService.deletePost(post, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void deletePost2(Post post, int ds, PostService postService) throws AppException{
		postService.deletePost(post, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void updateFullyPost1(Post post, int ds, PostService postService) throws AppException {
		postService.updateFullyPost(post, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void updateFullyPost2(Post post, int ds, PostService postService) throws AppException {
		postService.updateFullyPost(post, ds);
	}
}
