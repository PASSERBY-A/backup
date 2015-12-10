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
@Table(name = "TBS_BILL_CALL_RECORD")
public class BillCallRecordEntity {
	@Id
	@Column(name = "call_record_id")
	@SequenceGenerator(name = "BILL_CALL_RECORD_GENERATOR", sequenceName = "SEQ_TBS_BILL_CALL_RECORD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_CALL_RECORD_GENERATOR")
	private Long callRecordId;

	@Column(name = "year")
	private int year;

	@Column(name = "month")
	private int month;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_call")
	private Date dateOfCall;

	@Column(name = "calling_number", length = 40)
	private String callingNumber;

	@Column(name = "called_number", length = 40)
	private String calledNumber;

	@Column(name = "country_code", length = 20)
	private String countryCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "starting_time")
	private Date startingTime;

	@Column(name = "duration", length = 40)
	private String duration;

	@Column(name = "cost")
	private float cost;

	@Column(name = "communication_type", length = 100)
	private String communicationType;

	@Column(name = "location", length = 40)
	private String location;

	@Column(name = "private_purpose")
	private int privatePurpose;

	public String getCallingNumber() {
		return callingNumber;
	}

	public void setCallingNumber(String callingNumber) {
		this.callingNumber = callingNumber;
	}

	public String getCalledNumber() {
		return calledNumber;
	}

	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Date getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}

	public Long getCallRecordId() {
		return callRecordId;
	}

	public void setCallRecordId(Long callRecordId) {
		this.callRecordId = callRecordId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getCommunicationType() {
		return communicationType;
	}

	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPrivatePurpose() {
		return privatePurpose;
	}

	public void setPrivatePurpose(int privatePurpose) {
		this.privatePurpose = privatePurpose;
	}

	public Date getDateOfCall() {
		return dateOfCall;
	}

	public void setDateOfCall(Date dateOfCall) {
		this.dateOfCall = dateOfCall;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
