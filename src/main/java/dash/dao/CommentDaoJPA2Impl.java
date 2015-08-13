package dash.dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import dash.pojo.Comment;
import dash.pojo.Post;

@Component("commentDao")
public class CommentDaoJPA2Impl implements CommentDao {
	@PersistenceContext(unitName = "dashPersistenceCHW")
	private EntityManager entityManagerCHW;
	
	@PersistenceContext(unitName = "dashPersistenceVMA")
	private EntityManager entityManagerVMA;

	@Override
	public List<Comment> getComments(int numberOfComments, Long startIndex) {
		String sqlString = null;

		sqlString = "SELECT u FROM Comment u WHERE u.id < ?1 ORDER BY u.creation_timestamp DESC";

		TypedQuery<Comment> query = entityManagerCHW.createQuery(sqlString,
				Comment.class);

		if (startIndex == 0)
			startIndex = Long.MAX_VALUE;
		query.setParameter(1, startIndex);
		query.setMaxResults(numberOfComments);
		return query.getResultList();
	}

	@Override
	public List<Comment> getComments(int numberOfComments, Long startIndex, Post post) {
//		String qlString = "SELECT u FROM Comment u where u.post_id = ?1 AND u.id < ?2 ORDER BY u.creation_timestamp ASC";
		String qlString = "SELECT u FROM Comment u where u.post_id = ?1 ORDER BY u.creation_timestamp ASC";

		TypedQuery<Comment> query = entityManagerCHW.createQuery(qlString,
				Comment.class);
		query.setParameter(1, post.getId());

		return query.getResultList();
	}

	@Override
	public Comment getCommentById(Long id) {

		try {
			String qlString = "SELECT u FROM Comment u WHERE u.id = ?1";
			TypedQuery<Comment> query = entityManagerCHW.createQuery(
					qlString, Comment.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteCommentById(Comment commentPojo, int ds) {
		if(ds == 1){
			Comment post = entityManagerCHW.find(Comment.class,
					commentPojo.getId());
			entityManagerCHW.remove(post);	
		}
		if(ds == 2){
			Comment post = entityManagerVMA.find(Comment.class,
					commentPojo.getId());
			entityManagerVMA.remove(post);	
		}
		

	}

	@Override
	public Long createComment(Comment comment, int ds) {

		comment.setCreation_timestamp(new Date());
		comment.setLatest_activity_timestamp(new Date());
		if(ds == 1){
			entityManagerCHW.persist(comment);
			entityManagerCHW.flush();// force insert to receive the id of the post
		}
		if(ds == 2){
			entityManagerVMA.persist(comment);
			entityManagerVMA.flush();// force insert to receive the id of the post
		}

		// Give admin over new post to the new post

		return comment.getId();
	}

	@Override
	public void updateComment(Comment comment) {
		// TODO think about partial update and full update
		comment.setLatest_activity_timestamp(new Date());
		entityManagerCHW.merge(comment);
	}

	@Override
	public void deleteComments() {
		Query query = entityManagerCHW.createNativeQuery("TRUNCATE TABLE comment");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfComments() {
		try {
			String qlString = "SELECT COUNT(*) FROM comment";
			TypedQuery<Post> query = entityManagerCHW.createQuery(qlString,
					Post.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}
}
