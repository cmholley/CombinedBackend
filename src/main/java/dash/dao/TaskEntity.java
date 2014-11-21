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

import dash.pojo.Task;

/*
 * Task entity
 * @author tswensen
 * 
 */
@Entity
@Table(name = "tasks")
public class TaskEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988487193956434387L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "group_id")
	private Long group_id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "time")
	private Date time;

	@Column(name = "duration")
	private int duration;

	@Column(name = "location")
	private String location;

	@GeneratedValue
	@Column(name = "creation_timestamp")
	private Date creation_timestamp;

	@Column(name = "finished")
	private int finished;

	@Column(name = "badge_id")
	private Long badge_id;

	public TaskEntity() {
	}

	public TaskEntity(Long id, Long group_id, String name, String description,
			Date time, int duration, String location, Date creation_timestamp,
			int finished, Long badge_id) {
		super();
		this.id = id;
		this.group_id = id;
		this.name = name;
		this.description = description;
		this.time = time;
		this.duration = duration;
		this.location = location;
		this.creation_timestamp = creation_timestamp;
		this.finished = finished;
		this.badge_id = badge_id;
	}

	public TaskEntity(Task task) {
		try {
			BeanUtils.copyProperties(this, task);
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
