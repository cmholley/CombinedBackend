package dash.pojo;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dash.security.IAclObject;

@Entity
@Table(name="post")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Post implements IAclObject {

	@Id
	@GeneratedValue	
	@XmlElement(name="id")
    @Column(name="id")
	private Long id;
	
	@XmlElement(name="group_id")
    @Column(name="group_id")
	private Long group_id;
	
	@XmlElement(name="user_id")
    @Column(name="user_id")
	private Long user_id;
	
	@XmlElement(name="content")
    @Column(name="content")
	private String content;
	
	@XmlElement(name="image")
    @Column(name="image")
	private String image;
	
	
	@XmlElement(name="creation_timestamp")
    @Column(name="creation_timestamp")
	private Date creation_timestamp;
	
	@XmlElement(name="latest_activity_timestamp")
    @Column(name="latest_activity_timestamp")
	private Date latest_activity_timestamp;
	
	@XmlElement(name="like_counts")
    @Column(name="like_counts")
	private Integer like_count;
	
	@XmlElement(name="task_link_id")
    @Column(name="task_link_id")
	private Long task_link_id;


	public Post(){}

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
