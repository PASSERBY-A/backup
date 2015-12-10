package com.volkswagen.tel.billing.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;

public class NotifyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public final String info = "This month billing data has been imported, please submit your private phone records as soon as possible.";

	private String telephoneNoBillYearAndMonth;

	private String mobileNoBillYearAndMonth;

	private String oldtelephoneNoBillYearAndMonth;

	private Map<String, String> oldtelephoneNoBillYearAndMonthMap = new HashMap<String, String>();

	private Map<String, String> mobileNoBillYearAndMonthMap = new HashMap<String, String>();

	private String receiver;

	private String mail;

	private String mobile = "";

	private String telephone;

	private String oldTelephone;

	private List<String> mobiles = new ArrayList<String>();

	private List<String> oldTelephones = new ArrayList<String>();

	public String getTelephoneNoBillYearAndMonth() {
		return telephoneNoBillYearAndMonth;
	}

	public void setTelephoneNoBillYearAndMonth(
			String telephoneNoBillYearAndMonth) {
		this.telephoneNoBillYearAndMonth = telephoneNoBillYearAndMonth;
	}

	public String getMobileNoBillYearAndMonth(String mobile) {

		return mobileNoBillYearAndMonthMap.get(mobile);
	}

	public void setMobileNoBillYearAndMonth(String mobile,
			String mobileNoBillYearAndMonth) {

		if (StringUtils.isNotEmpty(mobileNoBillYearAndMonth)) {
			mobileNoBillYearAndMonthMap.put(mobile, mobileNoBillYearAndMonth);
		}

		// this.mobileNoBillYearAndMonth = mobileNoBillYearAndMonth;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public synchronized String getMobile() {
		StringBuffer sb = new StringBuffer();

		for (String mobile : mobiles) {
			sb.append(mobile + ",");
		}

		if (mobiles.size() > 1)
			return sb.substring(0, sb.length() - 1);

		return mobiles.size() > 0 ? this.mobiles.get(0) : null;

	}

	public void setMobile(String mobile) {
		// this.mobile = mobile;
		if (mobiles.contains(mobile)) {
			return;
		}
		if (StringUtils.isNotEmpty(mobile)) {
			mobiles.add(mobile);
		}

	}

	public String getOldTelephone() {
		
		StringBuffer sb = new StringBuffer();

		for (String oldTelephone : oldTelephones) {
			sb.append(oldTelephone + ",");
		}

		if (oldTelephones.size() > 1)
			return sb.substring(0, sb.length() - 1);

		return oldTelephones.size() > 0 ? this.oldTelephones.get(0) : null;
	}

	public void setOldTelephone(String oldTelephone) {
		// this.mobile = mobile;
		if (oldTelephones.contains(oldTelephone)) {
			return;
		}
		if (StringUtils.isNotEmpty(oldTelephone)) {
			oldTelephones.add(oldTelephone);
		}
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobileNoBillYearAndMonth() {
		return mobileNoBillYearAndMonth;
	}

	public void setMobileNoBillYearAndMonth(String mobileNoBillYearAndMonth) {
		this.mobileNoBillYearAndMonth = mobileNoBillYearAndMonth;
	}

	// ------------------

	public String getOldtelephoneNoBillYearAndMonth(String key) {
		return this.oldtelephoneNoBillYearAndMonthMap.get(key);
	}

	public void setOldtelephoneNoBillYearAndMonth(String oldTelphone,
			String oldtelephoneNoBillYearAndMonth) {
		this.oldtelephoneNoBillYearAndMonthMap.put(oldTelphone,
				oldtelephoneNoBillYearAndMonth);
	}

 

	@Override
	public String toString() {
		return "NotifyDTO [telephoneNoBillYearAndMonth="
				+ telephoneNoBillYearAndMonth + ", mobileNoBillYearAndMonth="
				+ mobileNoBillYearAndMonth
				+ ", oldtelephoneNoBillYearAndMonth="
				+ oldtelephoneNoBillYearAndMonth
				+ ", oldtelephoneNoBillYearAndMonthMap="
				+ oldtelephoneNoBillYearAndMonthMap
				+ ", mobileNoBillYearAndMonthMap="
				+ mobileNoBillYearAndMonthMap + ", receiver=" + receiver
				+ ", mail=" + mail + ", mobile=" + mobile + ", telephone="
				+ telephone + ", oldTelephone=" + oldTelephone + ", mobiles="
				+ mobiles + ", oldTelephones=" + oldTelephones + "]";
	}

	public static void main(String[] args) {

		NotifyDTO n = new NotifyDTO();
/*		n.setMobile("13900000000");
		n.setMobile("13900000000");
		n.setMobile("13911111111");
		n.setMobile("13922222222");
		n.setMobile("13922222222");
		n.setMobile("13922222222");

		n.setMobileNoBillYearAndMonth("13900000000", "201401,201402");*/

		n.setOldTelephone("11111111111111111");
		n.setOldTelephone("222222222222222");
		n.setOldTelephone("33333333333333333333333");
		
		System.out.println(n);

	}

}
