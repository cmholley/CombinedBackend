package dash.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;

import dash.security.IAclObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Entity
@Table(name = "post")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Post implements IAclObject {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	@Column(name = "group_id", updatable = false)
	@XmlElement(name = "group_id")
	private Long group_id;

	@Column(name = "user_id", updatable = false)
	@XmlElement(name = "user_id")
	private Long user_id;

	@Column(name = "content")
	@XmlElement(name = "content")
	private String content;

	@Column(name = "image")
	@XmlElement(name = "image")
	private String image;

	@GeneratedValue
	@Column(name = "creation_timestamp", updatable = false)
	@XmlElement(name = "creation_timestamp")
	private Date creation_timestamp;

	@GeneratedValue
	@Column(name = "latest_activity_timestamp")
	@XmlElement(name = "latest_activity_timestamp")
	private Date latest_activity_timestamp;

	@Column(name = "like_count")
	@XmlElement(name = "like_count")
	private int like_count;

	@Column(name = "task_link_id")
	@XmlElement(name = "task_link_id")
	private Long task_link_id;

	public Post(Post groupEntity) {
		try {
			BeanUtils.copyProperties(this, groupEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

	public Post() {

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
}
