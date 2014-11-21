package dash.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.beanutils.BeanUtils;

import dash.pojo.Comment;

/*
 * Post entity
 * @author tswensen
 * 
 */
@Entity
@Table(name = "comment")
public class CommentEntity implements Serializable {

	private static final long serialVersionUID = 2980705067408394223L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "post_id")
	private Long post_id;

	@Column(name = "user_id")
	private Long user_id;

	@Column(name = "content")
	private String content;

	@Column(name = "image")
	private String image;

	@GeneratedValue
	@Column(name = "creation_timestamp")
	private Date creation_timestamp;

	@GeneratedValue
	@Column(name = "latest_activity_timestamp")
	private Date latest_activity_timestamp;

	public CommentEntity() {
	}

	public CommentEntity(Long id, Long group_id, Long user_id, String content,
			String image, Date creation_timestamp,
			Date latest_activity_timestamp, int like_count, Long task_link_id,
			Long post_id) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.post_id = post_id;
		this.content = content;
		this.image = image;
		this.creation_timestamp = creation_timestamp;
		this.latest_activity_timestamp = latest_activity_timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPost_id() {
		return post_id;
	}

	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreation_timestamp() {
		return creation_timestamp;
	}

	public void setCreation_timestamp(Date creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}

	public Date getLatest_activity_timestamp() {
		return latest_activity_timestamp;
	}

	public void setLatest_activity_timestamp(Date latest_activity_timestamp) {
		this.latest_activity_timestamp = latest_activity_timestamp;
	}

	public CommentEntity(Comment comment) {
		try {
			BeanUtils.copyProperties(this, comment);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
