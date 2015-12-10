package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CusServicePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "customer_id", nullable = false)
	private long customerId;
	
	@Column(name = "bill_month", nullable = false)
	private long billMonth;

	@Column(name = "acc_id", nullable = false)
	private long accId;

	@Column(name = "service_id", nullable = false)
	private long serviceId;

	@Column(name = "sub_id", nullable = false)
	private long subId;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(long billMonth) {
		this.billMonth = billMonth;
	}

	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getSubId() {
		return subId;
	}

	public void setSubId(long subId) {
		this.subId = subId;
	}
	
	
	
}
