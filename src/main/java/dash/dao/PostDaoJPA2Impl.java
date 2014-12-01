package dash.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import dash.pojo.Group;
import dash.pojo.Post;

@Component("postDao")
public class PostDaoJPA2Impl implements PostDao {
	@PersistenceContext(unitName = "dashPersistence")
	private EntityManager entityManager;

	@Override
	public List<PostEntity> getPosts(int numberOfPosts, Long startIndex) {
		String sqlString = null;

		sqlString = "SELECT u FROM PostEntity u WHERE u.id < ?1 ORDER BY u.creation_timestamp DESC";

		TypedQuery<PostEntity> query = entityManager.createQuery(sqlString,
				PostEntity.class);
		if (startIndex == 0)
			startIndex = Long.MAX_VALUE;
		query.setParameter(1, startIndex);
		query.setMaxResults(numberOfPosts);

		return query.getResultList();
	}

	@Override
	public List<PostEntity> getPosts(int numberOfPosts, Long startIndex,
			Group group) {

		String qlString = "SELECT u FROM PostEntity u WHERE u.group_id = ?1 AND u.id < ?2 ORDER BY u.creation_timestamp DESC";

		TypedQuery<PostEntity> query = entityManager.createQuery(qlString,
				PostEntity.class);
		if (startIndex == 0)
			startIndex = Long.MAX_VALUE;
		query.setParameter(1, group.getId());
		query.setParameter(2, startIndex);
		query.setMaxResults(numberOfPosts);

		return query.getResultList();
	}

	@Override
	public PostEntity getPostById(Long id) {

		try {
			String qlString = "SELECT u FROM PostEntity u WHERE u.id = ?1";
			TypedQuery<PostEntity> query = entityManager.createQuery(qlString,
					PostEntity.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deletePostById(Post postPojo) {

		PostEntity post = entityManager
				.find(PostEntity.class, postPojo.getId());
		entityManager.remove(post);

	}

	@Override
	public Long createPost(PostEntity post) {

		post.setCreation_timestamp(new Date());
		post.setLatest_activity_timestamp(new Date());
		entityManager.persist(post);
		entityManager.flush();// force insert to receive the id of the post

		// Give admin over new post to the new post

		return post.getId();
	}

	@Override
	public void updatePost(PostEntity post) {
		// TODO think about partial update and full update
		post.setLatest_activity_timestamp(new Date());
		entityManager.merge(post);
	}

	@Override
	public void deletePosts() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE post");
		query.executeUpdate();
	}

	@Override
	public int getNumberOfPosts() {
		try {
			String qlString = "SELECT COUNT(*) FROM post";
			TypedQuery<PostEntity> query = entityManager.createQuery(qlString,
					PostEntity.class);

			return query.getFirstResult();
		} catch (NoResultException e) {
			return 0;
		}
	}
}
