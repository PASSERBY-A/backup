package com.hp.idc.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 客户基本信息持久层
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * 
 */

@Entity(name="CusInfo")
@Table(name = "customer_info")
public class CusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "customer_id")
	private long id;

	@Column(name = "customer_name", length = 128)
	private String name;

	@Column(name = "customer_abbrname", length = 24)
	private String abbrName;

	@Column(name = "customer_loginname", length = 32)
	private String longinName;

	@Column(name = "customer_password", length = 256)
	private String password;

	@Column(name = "customer_manager_name", length = 64)
	private String managerName;

	@Column(name = "customer_type_id")
	private int typeId;

	@Column(name = "vocation")
	private int vocation;

	@Column(name = "customer_status")
	private int status;

	@Column(name = "icp_cert")
	private int icpCert;

	@Column(name = "customer_billaddress", length = 128)
	private String billAddress;

	@Column(name = "customer_address", length = 128)
	private String address;

	@Column(name = "customer_zipcode", length = 8)
	private String zipCode;

	@Column(name = "major_contact_person", length = 20)
	private String majorContact;

	@Column(name = "customer_email", length = 64)
	private String email;

	@Column(name = "customer_fax", length = 16)
	private String fax;

	@Column(name = "phone_no", length = 40)
	private String phoneNo;

	@Column(name = "dt_opentime")
	@Temporal(value = TemporalType.DATE)
	private Date openTime;

	@Column(name = "dt_canceltime")
	@Temporal(value = TemporalType.DATE)
	private Date cancelTime;

	@Column(name = "dt_activetime")
	@Temporal(value = TemporalType.DATE)
	private Date activeTime;

	@Column(name = "vc_remarks", length = 128)
	private String remarks;
	
	
	@Column(name = "customer_appendix1", length = 128)
	private String appendix1;

	@Column(name = "customer_appendix2", length = 128)
	private String appendix2;
	
	@Column(name = "customer_appendix3", length = 128)
	private String appendix3;
	
	@Column(name = "customer_appendix4", length = 128)
	private String appendix4;
	
	@Column(name = "customer_appendix5", length = 128)
	private String appendix5;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getVocation() {
		return vocation;
	}

	public void setVocation(int vocation) {
		this.vocation = vocation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIcpCert() {
		return icpCert;
	}

	public void setIcpCert(int icpCert) {
		this.icpCert = icpCert;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMajorContact() {
		return majorContact;
	}

	public void setMajorContact(String majorContact) {
		this.majorContact = majorContact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAppendix1() {
		return appendix1;
	}

	public void setAppendix1(String appendix1) {
		this.appendix1 = appendix1;
	}

	public String getAppendix2() {
		return appendix2;
	}

	public void setAppendix2(String appendix2) {
		this.appendix2 = appendix2;
	}

	public String getAppendix3() {
		return appendix3;
	}

	public void setAppendix3(String appendix3) {
		this.appendix3 = appendix3;
	}

	public String getAppendix4() {
		return appendix4;
	}

	public void setAppendix4(String appendix4) {
		this.appendix4 = appendix4;
	}

	public String getAppendix5() {
		return appendix5;
	}

	public void setAppendix5(String appendix5) {
		this.appendix5 = appendix5;
	}

	
}
