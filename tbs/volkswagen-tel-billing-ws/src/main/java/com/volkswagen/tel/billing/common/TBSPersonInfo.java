package com.volkswagen.tel.billing.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TBSPersonInfo implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String enumber;
	
	private String name;
	
	private String username;
	
	private String telephonenumber;
	
	private String mobile="";

	private String email;
	
	private String noBillYearAndMonth;
	
	private String costcenter;
	
	
	//mobile   有可能有多个。 
	private List<String> mobiles = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephonenumber() {
		return telephonenumber;
	}

	public void setTelephonenumber(String telephonenumber) {
		this.telephonenumber = telephonenumber;
	}

	public synchronized String getMobile() {
		StringBuffer sb = new StringBuffer();
		
		
		for(String mobile:mobiles)
		{
			
			sb.append(mobile+",");
			
		}
		
		if(mobiles.size()>1)
			return sb.substring(0, sb.length()-1);
		
		 return mobiles.size()>0?this.mobiles.get(0):null;
		
		
	}

	public synchronized void setMobile(String mobile) {
		 
		//this.mobile = mobile;
				if(mobiles.contains(mobile))
				{
					return;
				}
				if(StringUtils.isNotEmpty(mobile))
				{
					mobiles.add(mobile);
				}
				
		
		
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getNoBillYearAndMonth() {
		return noBillYearAndMonth;
	}

	public void setNoBillYearAndMonth(String noBillYearAndMonth) {
		this.noBillYearAndMonth = noBillYearAndMonth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TBSPersonInfo other = (TBSPersonInfo) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "TBSPersonInfo [enumber=" + enumber + ", name=" + name
				+ ", username=" + username + ", telephonenumber="
				+ telephonenumber + ", mobile=" + mobile + ", email=" + email
				+ ", noBillYearAndMonth=" + noBillYearAndMonth
				+ ", costcenter=" + costcenter + ", mobiles=" + mobiles + "]";
	}

	public String getEnumber() {
		return enumber;
	}

	public void setEnumber(String enumber) {
		this.enumber = enumber;
	}


	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public static void main(String[] args) {
		
		
		TBSPersonInfo t = new TBSPersonInfo();
		
		t.setMobile("13900000000");
		t.setMobile("13900000001");
		System.out.println(t.getMobile());
		
		
		
	}
	 
	
	
	
	

}