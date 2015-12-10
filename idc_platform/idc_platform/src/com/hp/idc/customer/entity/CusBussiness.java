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
 * BOSS IDC业务信息持久层
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * @primary Key:orderId+orderDetailId+doneCode
 */

@Entity(name = "CusBussiness")
@Table(name = "boss_idc_business")
public class CusBussiness implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CusBussinessPK id;

	@Column(name = "customer_id", nullable = false)
	private long customerId;

	@Column(name = "rec_status", nullable = false)
	private int status;

	@Column(name = "idc_num")
	private long idcNum;

	@Column(name = "ip_num")
	private long ipNum;

	@Column(name = "fee_ip_num")
	private long feeIpNum;

	@Column(name = "device_num")
	private long deviceNum;

	@Column(name = "domain_name", length = 100)
	private String domainName;

	@Column(name = "device_desc", length = 300)
	private String deviceDesc;

	@Column(name = "twice_port_num")
	private long twicePortNum;

	@Column(name = "private_port_num")
	private long privatePortNum;

	@Column(name = "vpn_num")
	private long vpnNum;

	@Column(name = "power_num")
	private long powerNum;

	@Column(name = "is_access")
	private long isAccess;

	@Column(name = "access_type")
	private long accessType;

	@Column(name = "create_date", nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date createDate;

	@Column(name = "valid_date", nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date validDate;

	@Column(name = "expire_date", nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date expireDate;

	@Column(name = "done_date", nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date doneDate;

	@Column(name = "notes", length = 255)
	private String notes;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getIdcNum() {
		return idcNum;
	}

	public void setIdcNum(long idcNum) {
		this.idcNum = idcNum;
	}

	public long getIpNum() {
		return ipNum;
	}

	public void setIpNum(long ipNum) {
		this.ipNum = ipNum;
	}

	public long getFeeIpNum() {
		return feeIpNum;
	}

	public void setFeeIpNum(long feeIpNum) {
		this.feeIpNum = feeIpNum;
	}

	public long getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(long deviceNum) {
		this.deviceNum = deviceNum;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDeviceDesc() {
		return deviceDesc;
	}

	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}

	public long getTwicePortNum() {
		return twicePortNum;
	}

	public void setTwicePortNum(long twicePortNum) {
		this.twicePortNum = twicePortNum;
	}

	public long getPrivatePortNum() {
		return privatePortNum;
	}

	public void setPrivatePortNum(long privatePortNum) {
		this.privatePortNum = privatePortNum;
	}

	public long getVpnNum() {
		return vpnNum;
	}

	public void setVpnNum(long vpnNum) {
		this.vpnNum = vpnNum;
	}

	public long getPowerNum() {
		return powerNum;
	}

	public void setPowerNum(long powerNum) {
		this.powerNum = powerNum;
	}

	public long getIsAccess() {
		return isAccess;
	}

	public void setIsAccess(long isAccess) {
		this.isAccess = isAccess;
	}

	public long getAccessType() {
		return accessType;
	}

	public void setAccessType(long accessType) {
		this.accessType = accessType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Date getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public CusBussinessPK getId() {
		return id;
	}

	public void setId(CusBussinessPK id) {
		this.id = id;
	}

}
