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

import org.apache.commons.beanutils.BeanUtils;

import dash.security.IAclObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Entity
@Table(name = "comment")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Comment implements IAclObject {

	@Id
	@GeneratedValue
	@XmlElement(name = "id")
	@Column(name = "id")
	private Long id;

	@XmlElement(name = "post_id")
	@Column(name = "post_id")
	private Long post_id;

	@XmlElement(name = "user_id")
	@Column(name = "user_id")
	private Long user_id;

	@XmlElement(name = "content")
	@Column(name = "content")
	private String content;

	@XmlElement(name = "image")
	@Column(name = "image")
	private String image;

	@XmlElement(name = "creation_timestamp")
	@Column(name = "creation_timestamp")
	private Date creation_timestamp;

	@XmlElement(name = "latest_activity_timestamp")
	@Column(name = "latest_activity_timestamp")
	private Date latest_activity_timestamp;

	public Comment() {
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
}
