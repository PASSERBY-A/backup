package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CusAccountPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "bill_month", nullable = false)
	private long billMonth;

	@Column(name = "customer_id", nullable = false)
	private long customerId;

	@Column(name = "service_id", nullable = false)
	private long serviceId;

	public long getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(long billMonth) {
		this.billMonth = billMonth;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

}
