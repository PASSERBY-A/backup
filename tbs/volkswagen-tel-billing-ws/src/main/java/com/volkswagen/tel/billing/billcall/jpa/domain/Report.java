package com.volkswagen.tel.billing.billcall.jpa.domain;

import java.io.Serializable;
import java.util.List;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	public String phoneNumber;
	
	public String date;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
}
