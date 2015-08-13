package dash.tran;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.Comment;
import dash.pojo.Group;
import dash.service.CommentService;

public class CommentTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public Long createComment1(Comment comment, Group group, int ds, CommentService commentService) throws AppException {
		return commentService.createComment(comment, group, ds);
	}

	@Transactional("transactionManagerVMA")
	public Long createComment2(Comment comment, Group group, int ds, CommentService commentService) throws AppException {
		return commentService.createComment(comment, group, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteComment1(Comment comment, int ds, CommentService commentService) throws AppException {
		commentService.deleteComment(comment, ds);
	}

	@Transactional("transactionManagerVMA")
	public void deleteComment2(Comment comment, int ds, CommentService commentService) throws AppException {
		commentService.deleteComment(comment, ds);
	}
}
