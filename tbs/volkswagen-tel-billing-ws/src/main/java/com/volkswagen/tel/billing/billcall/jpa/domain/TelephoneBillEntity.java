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

@Entity
@Table(name = "TBS_TELEPHONE_BILL")
public class TelephoneBillEntity {
	@Id
	@Column(name = "bill_id")
	@SequenceGenerator(name = "TELEPHONE_BILL_GENERATOR", sequenceName = "SEQ_TBS_TELEPHONE_BILL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TELEPHONE_BILL_GENERATOR")
	private Long billId;
	
	@Column(name = "telephone_number", length=20)
	private String telephoneNumber;

	@Column(name = "year")
	private int year;
	
	private int month;
	
	@Column(name = "vendor_name", length=100)
	private String vendorName;

	@Column(name = "status", length=20)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "TelephoneBillEntity [billId=" + billId + ", telephoneNumber="
				+ telephoneNumber + ", year=" + year + ", month=" + month
				+ ", vendorName=" + vendorName + ", status=" + status
				+ ", lastUpdateTime=" + lastUpdateTime + "]";
	}
	
	

}
