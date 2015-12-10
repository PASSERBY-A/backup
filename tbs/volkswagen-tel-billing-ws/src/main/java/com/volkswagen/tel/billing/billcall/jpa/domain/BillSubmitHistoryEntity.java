package com.volkswagen.tel.billing.billcall.jpa.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "TBS_BILL_SUBMIT_HISTORY")
public class BillSubmitHistoryEntity {
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "BILL_SUBMIT_HISTORY_GENERATOR", sequenceName = "SEQ_TBS_BILL_SUBMIT_HISTORY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_SUBMIT_HISTORY_GENERATOR")
	private Long id;

	@Column(name = "user_id", length = 20)
	private String userId;

	@Column(name = "staff_code", length = 20)
	private String staffCode;

	@Column(name = "telephone_number", length = 40)
	private String telephoneNumber;

	@Column(name = "billing_year")
	private int billingYear;

	@Column(name = "billing_month")
	private int billingMonth;

	@Column(name = "billing_value")
	private float billingValue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_submit")
	private Date dateOfSubmit;

	@Column(name = "type_of_submit", length = 10)
	private String typeOfSubmit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBillingYear() {
		return billingYear;
	}

	public void setBillingYear(int billingYear) {
		this.billingYear = billingYear;
	}

	public int getBillingMonth() {
		return billingMonth;
	}

	public void setBillingMonth(int billingMonth) {
		this.billingMonth = billingMonth;
	}

	public float getBillingValue() {
		return billingValue;
	}

	public void setBillingValue(float billingValue) {
		this.billingValue = billingValue;
	}

	public Date getDateOfSubmit() {
		return dateOfSubmit;
	}

	public void setDateOfSubmit(Date dateOfSubmit) {
		this.dateOfSubmit = dateOfSubmit;
	}

	public String getTypeOfSubmit() {
		return typeOfSubmit;
	}

	public void setTypeOfSubmit(String typeOfSubmit) {
		this.typeOfSubmit = typeOfSubmit;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
