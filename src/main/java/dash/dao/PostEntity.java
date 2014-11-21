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

import dash.pojo.Post;

/*
 * Post entity
 * @author tswensen
 * 
 */
@Entity
@Table(name = "post")
public class PostEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988487193956434387L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "group_id", updatable = false)
	private Long group_id;

	@Column(name = "user_id", updatable = false)
	private Long user_id;

	@Column(name = "content")
	private String content;

	@Column(name = "image")
	private String image;

	@GeneratedValue
	@Column(name = "creation_timestamp", updatable = false)
	private Date creation_timestamp;

	@GeneratedValue
	@Column(name = "latest_activity_timestamp")
	private Date latest_activity_timestamp;

	@Column(name = "like_count")
	private int like_count;

	@Column(name = "task_link_id")
	private Long task_link_id;

	public PostEntity() {
	}

	public PostEntity(Long id, Long group_id, Long user_id, String content,
			String image, Date creation_timestamp,
			Date latest_activity_timestamp, int like_count, Long task_link_id) {
		super();
		this.id = id;
		this.group_id = group_id;
		this.user_id = user_id;
		this.content = content;
		this.image = image;
		this.creation_timestamp = creation_timestamp;
		this.latest_activity_timestamp = latest_activity_timestamp;
		this.like_count = like_count;
		this.task_link_id = task_link_id;
	}

	public PostEntity(Post post) {
		try {
			BeanUtils.copyProperties(this, post);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
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

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public Long getTask_link_id() {
		return task_link_id;
	}

	public void setTask_link_id(Long task_link_id) {
		this.task_link_id = task_link_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
