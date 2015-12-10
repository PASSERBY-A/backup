package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * 工作计划
 * 2011.02.17
 * @author chengqp
 *
 */
public class WorkPlan {
	private String oid;
	private int userId;
	private String title="";
	private String type="";
	private Date planTime;
	private Date finishTime;
	private Date createTime=new Date();
	private String description="";
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return StringUtil.removeNull(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return StringUtil.removeNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return StringUtil.removeNull(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
