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
@Table(name = "group_data")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Group implements IAclObject {

	@Id
	@GeneratedValue
	@XmlElement(name = "id")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	@XmlElement(name = "name")
	private String name;

	@Column(name = "description")
	@XmlElement(name = "description")
	private String description;

	@GeneratedValue
	@Column(name = "creation_timestamp")
	@XmlElement(name = "creation_timestamp")
	private Date creation_timestamp;

	@Override
	public Long getId() {
		return id;
	}

	public Group() {
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreation_timestamp() {
		return creation_timestamp;
	}

	public void setCreation_timestamp(Date creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}
}
