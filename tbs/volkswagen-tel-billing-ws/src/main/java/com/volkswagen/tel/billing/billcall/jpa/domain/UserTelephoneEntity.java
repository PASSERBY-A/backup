package com.volkswagen.tel.billing.billcall.jpa.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "TBS_USER_TELEPHONE")
public class UserTelephoneEntity {
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "USER_TELEPHONE_GENERATOR", sequenceName = "SEQ_TBS_USER_TELEPHONE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_TELEPHONE_GENERATOR")
	private Long id;
	
	@Column(name = "user_id", length=20)
	private String userId;
	
	@Column(name = "telephone_number", length=45)
	private String telephoneNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_starting_time")
	private Date validStartingTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_end_time")
	private Date validEndTime;
	
	@Column(name = "status", length=10)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public Date getValidStartingTime() {
		return validStartingTime;
	}

	public void setValidStartingTime(Date validStartingTime) {
		this.validStartingTime = validStartingTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
