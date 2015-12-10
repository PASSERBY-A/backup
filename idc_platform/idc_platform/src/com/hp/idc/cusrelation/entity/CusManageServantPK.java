/**
 * 客户订购关系表主键实体
 * 2011-9-14
 */
package com.hp.idc.cusrelation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CusManageServantPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3362572852173646612L;
	

	@Column(name = "customer_id", nullable = false)
	private long customerId;// 集团编号

	@Column(name = "service_id", nullable = false)
	private long serviceId;// 服务标识

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
