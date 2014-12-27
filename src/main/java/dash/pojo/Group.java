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
@Table(name = "group_data")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Group implements IAclObject {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@XmlElement(name = "id")
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

	public Group(Group groupEntity) {
		try {
			BeanUtils.copyProperties(this, groupEntity);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

	public Group() {

	}

	@Override
	public Long getId() {
		return id;
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
