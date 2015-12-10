package com.hp.idc.bulletin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.hp.idc.common.core.bo.AbstractBaseBO;



/**
 * 
 * Create Date 2011-3-28
 * 
 * @version 1.0
 */

@Entity(name="BulletinInfo")
@Table(name = "SYS_BULLETIN_INFO")
public class BulletinInfo extends AbstractBaseBO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8160218524314536080L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_sys_bullietin_info")
    @SequenceGenerator(name="seq_sys_bullietin_info",sequenceName="seq_sys_bullietin_info")
	@Column(name = "bullietin_id", length = 19)
	private Long id;
	
	@Column(name="create_oper_id",length=10)
	private String creator;
	
	@Column(name="begin_time")
	private Date beginTime;
	
	@Column(name="end_time")
	private Date endTime;
	
	@Column(name = "bulletin_title", length = 200)
	private String title;
	
	@Column(name = "bulletin_content", length = 2000)
	private String content;

	@Column(name = "created_date")
	private Date createdDate;

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}	
}