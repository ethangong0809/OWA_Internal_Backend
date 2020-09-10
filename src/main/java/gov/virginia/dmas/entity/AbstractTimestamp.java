package gov.virginia.dmas.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class AbstractTimestamp {

	private Timestamp createdTime;
	
	private Timestamp updatedTime;

	@Column(name="CREATED_TIME", insertable=true, updatable=false)
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name="UPDATED_TIME", insertable=false, updatable=true)
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	@PrePersist
	void onCreate() {
		this.setCreatedTime(new Timestamp(new Date().getTime()));
	}
	
	@PreUpdate
	void onUpdate() {
		this.setUpdatedTime(new Timestamp(new Date().getTime()));
	}
	
}
