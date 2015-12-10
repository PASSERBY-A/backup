package com.hp.idc.network.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 网络告警状态数据结构
 * 
 * @author Wang Rui
 * @version 1.0,
 * 
 */
@Entity
@Table(name = "NET_ALERT")
public class NetworkAlertShowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "MONITOR_NAME")
	private String monitorName;

	@Column(name = "\"STATE\"")
	private String state;
	
	@Column(name = "SEVERITY")
	private String severity;

	@Column(name = "EVENT_STATUS")
	private String eventStatus;
	
	@Column(name = "EVENT_CONFIRMER")
	private String eventConfirmer;
	
	@Id
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name = "HAPPEN_TIME")
	private Date timed;

	public NetworkAlertShowEntity() {
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMonitorName() {
		return this.monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getTimed() {
		return timed;
	}

	public void setTimed(Date timed) {
		this.timed = timed;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventConfirmer() {
		return eventConfirmer;
	}

	public void setEventConfirmer(String eventConfirmer) {
		this.eventConfirmer = eventConfirmer;
	}
}
