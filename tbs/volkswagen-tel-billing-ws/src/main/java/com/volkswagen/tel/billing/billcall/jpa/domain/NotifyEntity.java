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
@Table(name = "TBS_NOTIFY")
public class NotifyEntity {
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "NOTIFY_GENERATOR", sequenceName = "SEQ_TBS_NOTIFY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFY_GENERATOR")
	private Long id;


	@Column(name = "receiver_", length = 256)
	private String receiver;
	
	@Column(name = "receiver_mail", length = 256)
	private String mail;
	
	@Column(name = "mobile_", length = 64)
	private String mobile;
	
	@Column(name = "telephone_", length = 64)
	private String telephone;
	
	@Column(name = "send_flag")
	private boolean sendFlag;
	
	@Column(name = "content_", length = 10240)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time")
	private Date sendTime;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	
	public Long getId() {
		return id;
	}






	public void setId(Long id) {
		this.id = id;
	}






	public String getReceiver() {
		return receiver;
	}






	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}






	public String getMail() {
		return mail;
	}






	public void setMail(String mail) {
		this.mail = mail;
	}






	public String getMobile() {
		return mobile;
	}






	public void setMobile(String mobile) {
		this.mobile = mobile;
	}






	public String getTelephone() {
		return telephone;
	}






	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}






	public boolean isSendFlag() {
		return sendFlag;
	}






	public void setSendFlag(boolean sendFlag) {
		this.sendFlag = sendFlag;
	}






	public String getContent() {
		return content;
	}






	public void setContent(String content) {
		this.content = content;
	}






	public Date getSendTime() {
		return sendTime;
	}






	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
