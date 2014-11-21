package dash.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.beanutils.BeanUtils;
import dash.dao.TaskEntity;
import dash.security.IAclObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements IAclObject {

	@XmlElement(name = "id")
	private Long id;

	@XmlElement(name = "group_id")
	private Long group_id;

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "description")
	private String description;

	@XmlElement(name = "time")
	private Date time;

	@XmlElement(name = "duration")
	private int duration;

	@XmlElement(name = "location")
	private String location;

	@XmlElement(name = "creation_timestamp")
	private Date creation_timestamp;

	@XmlElement(name = "finished")
	private int finished;

	@XmlElement(name = "badge_id")
	private Long badge_id;

	public Task(Long id, Long group_id, String name, String description,
			Date time, int duration, String location, Date creation_timestamp,
			int finished, Long badge_id) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.time = time;
		this.duration = duration;
		this.location = location;
		this.creation_timestamp = creation_timestamp;
		this.finished = finished;
		this.badge_id = badge_id;
	}

	public Task(TaskEntity taskEntity) {
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

	@Override
	public String toString() {
		return "Task ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (group_id != null ? "group_id=" + group_id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (time != null ? "time=" + time + ", " : "")
				+ "duration="
				+ duration
				+ ", "
				+ (location != null ? "location=" + location + ", " : "")
				+ (creation_timestamp != null ? "creation_timestamp="
						+ creation_timestamp + ", " : "") + "finished="
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
