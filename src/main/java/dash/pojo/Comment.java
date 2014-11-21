package dash.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.beanutils.BeanUtils;
import dash.dao.CommentEntity;
import dash.security.IAclObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Comment implements IAclObject {

	@XmlElement(name = "id")
	private Long id;

	@XmlElement(name = "post_id")
	private Long post_id;

	@XmlElement(name = "user_id")
	private Long user_id;

	@XmlElement(name = "content")
	private String content;

	@XmlElement(name = "image")
	private String image;

	@XmlElement(name = "creation_timestamp")
	private Date creation_timestamp;

	@XmlElement(name = "latest_activity_timestamp")
	private Date latest_activity_timestamp;

	public Comment(Long id, Long group_id, Long user_id, String content,
			String image, Date creation_timestamp,
			Date latest_activity_timestamp, int like_count, Long task_link_id,
			Long post_id) {
		super();
		this.id = id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.content = content;
		this.image = image;
		this.creation_timestamp = creation_timestamp;
		this.latest_activity_timestamp = latest_activity_timestamp;
	}

	public Comment(CommentEntity groupEntity) {
		try {
			BeanUtils.copyProperties(this, groupEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

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
