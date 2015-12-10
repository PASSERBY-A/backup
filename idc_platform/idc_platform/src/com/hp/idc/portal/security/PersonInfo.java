package com.hp.idc.portal.security;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

public class PersonInfo extends CommonInfo {
	private String mobile;
	private String email;

	public PersonInfo() {
		super();
		mobile = "";
		email = "";
	}

	public String getMobile() {
		return mobile != null ? mobile : "";
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email != null ? email : "";
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("mobile", this.getMobile());
		jo.put("email", this.getEmail());
		jo.put("AUCType", "P");
		return jo;
	}
}
