package com.hp.idc.cusrelation.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

@Entity(name = "CusManageServant")
@Table(name = "customer_servant_info")
public class CusManageServant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 860783080370125093L;

	/**
	 * 
	 */
	

	@EmbeddedId
	private CusManageServantPK id;

	@Column(name = "sub_id")
	private long subId;// not use

	@Column(name = "done_code")
	private long doneCode;// not use

	@Column(name = "product_id")
	private long productId;// not use

	@Column(name = "service_value", length = 40)
	private String serviceValue;// not use

	@Column(name = "create_date")
	@Temporal(value = TemporalType.DATE)
	private Date createDate;

	@Column(name = "done_date")
	@Temporal(value = TemporalType.DATE)
	private Date doneDate;// not use

	@Column(name = "valid_date")
	@Temporal(value = TemporalType.DATE)
	private Date validDate;// not use

	@Column(name = "expire_date")
	@Temporal(value = TemporalType.DATE)
	private Date expireDate;// not use

	@Column(name = "notes", length = 255)
	private String notes;// not use

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

	public CusManageServantPK getId() {
		return id;
	}

	public void setId(CusManageServantPK id) {
		this.id = id;
	}

	public long getSubId() {
		return subId;
	}

	public void setSubId(long subId) {
		this.subId = subId;
	}

	public long getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(long doneCode) {
		this.doneCode = doneCode;
	}

}
