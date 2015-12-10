package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CusServantPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "sub_id", nullable = false)
	private long subId;// Ö÷¶©¹º±àºÅ

	@Column(name = "service_id", nullable = false)
	private long serviceId;

	@Column(name = "done_code", nullable = false)
	private long doneCode;

	

	public long getSubId() {
		return subId;
	}

	public void setSubId(long subId) {
		this.subId = subId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(long doneCode) {
		this.doneCode = doneCode;
	}

}
