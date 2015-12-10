package com.hp.idc.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 客户订购信息持久层 注：该表实际为复合主键，该模块仅用于查询，因此设置主订购编号为逻辑主键
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * 
 */

@Entity(name = "CusServant")
@Table(name = "customer_servant_info")
public class CusServant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CusServantPK id;

	@Column(name = "customer_id", nullable = false)
	private long customerId;

	@Column(name = "product_id", nullable = false)
	private long productId;

	@Column(name = "service_value", length = 40)
	private String serviceValue;

	@Column(name = "create_date")
	@Temporal(value = TemporalType.DATE)
	private Date createDate;

	@Column(name = "done_date")
	@Temporal(value = TemporalType.DATE)
	private Date doneDate;

	@Column(name = "valid_date")
	@Temporal(value = TemporalType.DATE)
	private Date validDate;

	@Column(name = "expire_date")
	@Temporal(value = TemporalType.DATE)
	private Date expireDate;

	@Column(name = "notes", length = 255)
	private String notes;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getServiceValue() {
		return serviceValue;
	}

	public void setServiceValue(String serviceValue) {
		this.serviceValue = serviceValue;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public CusServantPK getId() {
		return id;
	}

	public void setId(CusServantPK id) {
		this.id = id;
	}

}
