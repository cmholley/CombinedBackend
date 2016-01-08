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
@Table(name = "tasks")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements IAclObject {

	@Id
	@GeneratedValue
	@XmlElement(name = "id")
	@Column(name = "id")
	private Long id;

	@XmlElement(name = "group_id")
	@Column(name = "group_id")
	private Long group_id;

	@XmlElement(name = "name")
	@Column(name = "name")
	private String name;

	@XmlElement(name = "description")
	@Column(name = "description")
	private String description;

	@XmlElement(name = "time")
	@Column(name = "time")
	private Date time;

	@XmlElement(name = "duration")
	@Column(name = "duration")
	private int duration;

	@XmlElement(name = "location")
	@Column(name = "location")
	private String location;

	@XmlElement(name = "creation_timestamp")
	@Column(name = "creation_timestamp")
	private Date creation_timestamp;

	@XmlElement(name = "finished")
	@Column(name = "finished")
	private boolean finished;

	@XmlElement(name = "badge_id")
	@Column(name = "badge_id")
	private Long badge_id;

	public Task() {
	}

	@Override
	public String toString() {
		return "Task [" + (id != null ? "id=" + id + ", " : "")
				+ (group_id != null ? "group_id=" + group_id + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (time != null ? "time=" + time + ", " : "") + "duration=" + duration + ", "
				+ (location != null ? "location=" + location + ", " : "")
				+ (creation_timestamp != null ? "creation_timestamp=" + creation_timestamp + ", " : "") + "finished="
				+ finished + ", badge_id=" + badge_id + "]";
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

	public boolean getFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Long getBadge_id() {
		return badge_id;
	}

	public void setBadge_id(Long badge_id) {
		this.badge_id = badge_id;
	}

}
