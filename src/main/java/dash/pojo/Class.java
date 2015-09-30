package dash.pojo;


import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dash.helpers.SimpleDateAdapter;
import dash.security.IAclObject;

@Entity
@Table(name = "class")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Class implements  IAclObject{
	
	public static enum Cores{
		Advocacy,
		Capacity_Building, 
		Communication_Skills, 
		Community_Service,
		Coordination,
		Interpersonal_Communication, 
		Knowledge_Base, 
		Organizational, 
		Service_Coordination,
		Skills,
		Teaching_Skills;
	}
	
	@Column(name="id")
	@XmlElement(name="id")
	private Long id;
	
	@Column(name="location_id")
	@XmlElement(name="location_id")
	private Long location_id;
	
	@Column(name="name")
	@XmlElement(name="name")
	private String name;
	
	@Column(name="description")
	@XmlElement(name="description")
	private String description;
	
	@Column(name="time")
	@XmlElement(name="time")
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Date time;
	
	@Column(name="duration")
	@XmlElement(name="duration")
	private Integer duration;
	
	@Column(name="room")
	@XmlElement(name="room")
	private String room;
	
	@Column(name="address")
	@XmlElement(name="address")
	private String address;
	
	@Column(name="creation_timestamp")
	@XmlElement(name="creation_timestamp")
	private Date creation_timestamp;
	
	@Column(name="finished")
	@XmlElement(name="finished")
	private Integer finished;
	
	@Column(name="cores")
	@XmlElement(name="cores")
	private Set<Cores> cores; //enum
	
	@Column(name="forCHW")
	@XmlElement(name="forCHW")
	private Integer forCHW;
	
	@Column(name="forCredit")
	@XmlElement(name="forCredit")
	private Integer forCredit;
	
	@Column(name="active")
	@XmlElement(name="active")
	private Integer active;
	
	
	public Class(){}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getLocation_id() {
		return location_id;
	}

	public void setLocation_id(Long location_id) {
		this.location_id = location_id;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreation_timestamp() {
		return creation_timestamp;
	}

	public void setCreation_timestamp(Date creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}

	public Integer getFinished() {
		return finished;
	}

	public void setFinished(Integer finished) {
		this.finished = finished;
	}
	
	
	public Integer getForCHW() {
		return forCHW;
	}

	public Integer getForCredit() {
		return forCredit;
	}

	public Integer getActive() {
		return active;
	}

	public void setForCHW(Integer forCHW) {
		this.forCHW = forCHW;
	}

	public void setForCredit(Integer forCredit) {
		this.forCredit = forCredit;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Set<Cores> getCores() {
		return cores;
	}

	public void setCores(Set<Cores> cores) {
		this.cores = cores;
	}
}
