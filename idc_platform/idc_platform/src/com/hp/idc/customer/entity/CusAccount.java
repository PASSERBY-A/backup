package com.hp.idc.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 客户账务费用信息持久层
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * 
 */

@Entity(name = "CusAccount")
@Table(name = "customer_account_fee")
public class CusAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CusAccountPK id;

	@Column(name = "pay_flag", nullable = false)
	private int payFlag;

	@Column(name = "total_fee", precision = 12, scale = 2, nullable = false)
	private Double totalFee;

	@Column(name = "remarks", length = 100)
	private String remarks;

	public CusAccountPK getId() {
		return id;
	}

	public void setId(CusAccountPK id) {
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
