package dash.pojo;


import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dash.helpers.SimpleDateAdapter;
import dash.security.IAclObject;

@Entity
@Table(name = "classes")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Class implements  IAclObject{
	

	
	@Id
	@GeneratedValue
	@XmlElement(name="id")
	@Column(name="id")
	private Long id;
	
	@XmlElement(name="location_id")
	@Column(name="location_id")
	private Long location_id;
	
	@XmlElement(name="name")
	@Column(name="name")
	private String name;
	
	@XmlElement(name="description")
	@Column(name="description")
	private String description;
	
	@XmlElement(name="time")
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	@Column(name="time")
	private Date time;
	
	@XmlElement(name="duration")
	@Column(name="duration")
	private int duration;
	
	@XmlElement(name="room")
	@Column(name="room")
	private String room;
	
	@XmlElement(name="address")
	@Column(name="address")
	private String address;
	
	@XmlElement(name="creation_timestamp")
	@Column(name="creation_timestamp")
	private Date creation_timestamp;
	
	@XmlElement(name="finished")
	@Column(name="finished")
	private int finished;
	
	/*
	 * This attribute contains a list of Longs that corresponds
	 * to the core competencies for the class. 
	 *  0 = Advocacy
	 *	1 = Capacity_Building, 
	 *	2 = Communication_Skills, 
	 *	3 = Community_Service,
	 *	4 = Coordination,
	 *	5 = Interpersonal_Communication, 
	 *	6 = Knowledge_Base, 
	 *	7 = Organizational, 
	 *	8 = Service_Coordination,
	 *	9 = Skills,
	 *	10 = Teaching_Skills,
	 */
	
	@XmlElement(name="cores")
	@ElementCollection(fetch= FetchType.EAGER)
	@CollectionTable(name = "class_cores", joinColumns = {@JoinColumn(name="class_id")})
	private Set<Long> cores; //enum
	
	@XmlElement(name="forCHW")
	@Column(name="forCHW")
	private int forCHW;
	
	@XmlElement(name="forCredit")
	@Column(name="forCredit")
	private int forCredit;
	
	@XmlElement(name="active")
	@Column(name="active")
	private int active;
	
	
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
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

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}
	
	
	public int getForCHW() {
		return forCHW;
	}

	public int getForCredit() {
		return forCredit;
	}

	public int getActive() {
		return active;
	}

	public void setForCHW(int forCHW) {
		this.forCHW = forCHW;
	}

	public void setForCredit(int forCredit) {
		this.forCredit = forCredit;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Set<Long> getCores() {
		return cores;
	}

	public void setCores(Set<Long> cores) {
		this.cores = cores;
	}
}
