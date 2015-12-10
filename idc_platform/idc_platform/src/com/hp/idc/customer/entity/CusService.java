package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 客户消费记录信息持久层
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * 
 */

@Entity(name="CusService")
@Table(name = "customer_service_rec")
public class CusService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CusServicePK id;

	@Column(name = "pay_flag", nullable = false)
	private int payFlag;

	@Column(name = "total_fee", precision = 12, scale = 2, nullable = false)
	private Double totalFee;

	

	public CusServicePK getId() {
		return id;
	}

	public void setId(CusServicePK id) {
		this.id = id;
	}

	public int getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(int payFlag) {
		this.payFlag = payFlag;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

}
