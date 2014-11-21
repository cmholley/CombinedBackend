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

import dash.pojo.Hour;

/*
 * Hour entity
 * @author tswensen
 * 
 */
@Entity
@Table(name = "entered_hours")
public class HourEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988487193956434387L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long user_id;

	@Column(name = "task_id")
	private Long task_id;

	@Column(name = "title")
	private String title;

	@Column(name = "start_time")
	private Date start_time;

	@Column(name = "end_time")
	private Date end_time;

	@Column(name = "duration")
	private int duration;

	@Column(name = "approved")
	private boolean approved;

	@Column(name = "pending")
	private boolean pending;

	public HourEntity() {
	}

	public HourEntity(Hour hour) {
		try {
			BeanUtils.copyProperties(this, hour);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public HourEntity(Long id, Long user_id, Long task_id, String title,
			Date start_time, Date end_time, int duration, boolean approved,
			boolean pending) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.task_id = task_id;
		this.title = title;
		this.start_time = start_time;
		this.end_time = end_time;
		this.duration = duration;
		this.approved = approved;
		this.pending = pending;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}