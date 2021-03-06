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
 * 客户联系人信息持久层
 * 
 * @author Fancy
 * @version 1.0, 2:48:52 PM Jul 29, 2011
 * 
 */

@Entity(name="CusContact")
@Table(name = "customer_contact_info")
public class CusContact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "cust_contact_id")
	private long id;
	@Column(name = "customer_id")
	private long customerId;
	@Column(name = "contact_name", length = 32)
	private String contactName;
	@Column(name = "card_type")
	private int cardType;
	@Column(name = "card_id", length = 64)
	private String cardId;
	@Column(name = "contact_position", length = 40)
	private String position;
	@Column(name = "contact_phone", length = 30)
	private String phone;
	@Column(name = "contact_mobile", length = 30)
	private String mobile;
	@Column(name = "home_phone", length = 30)
	private String homePhone;
	@Column(name = "office_phone", length = 30)
	private String officePhone;
	@Column(name = "contact_email", length = 64)
	private String email;
	@Column(name = "contact_address", length = 64)
	private String address;
	@Column(name = "postcode", length = 6)
	private String postcode;
	@Column(name = "done_date")
	@Temporal(value = TemporalType.DATE)
	private Date doneDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Date getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

}
