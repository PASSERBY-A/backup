package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CusBussinessPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "order_id", nullable = false)
	private long orderId;
	
	@Column(name = "order_detail_id", nullable = false)
	private long orderDetailId;
	
	@Column(name = "done_code", nullable = false)
	private long doneCode;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public long getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(long doneCode) {
		this.doneCode = doneCode;
	}
	
	
}
