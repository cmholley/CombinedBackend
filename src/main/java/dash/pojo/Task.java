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
@Table(name = "tasks")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements IAclObject {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
	private Long id;

	@Column(name = "group_id")
	@XmlElement(name = "group_id")
	private Long group_id;

	@Column(name = "name")
	@XmlElement(name = "name")
	private String name;

	@Column(name = "description")
	@XmlElement(name = "description")
	private String description;

	@Column(name = "time")
	@XmlElement(name = "time")
	private Date time;

	@Column(name = "duration")
	@XmlElement(name = "duration")
	private int duration;

	@Column(name = "location")
	@XmlElement(name = "location")
	private String location;

	@GeneratedValue
	@Column(name = "creation_timestamp")
	@XmlElement(name = "creation_timestamp")
	private Date creation_timestamp;

	@Column(name = "finished")
	@XmlElement(name = "finished")
	private int finished;

	@Column(name = "badge_id")
	@XmlElement(name = "badge_id")
	private Long badge_id;

	public Task(Task taskEntity) {
		try {
			BeanUtils.copyProperties(this, taskEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

	public Task() {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreation_timestamp() {
		return creation_timestamp;
	}

	public void setCreation_timestamp(Date creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public Long getBadge_id() {
		return badge_id;
	}

	public void setBadge_id(Long badge_id) {
		this.badge_id = badge_id;
	}

}
