package com.hp.idc.portal.security;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

public class WorkgroupInfo extends CommonInfo {

	public WorkgroupInfo() {
		super();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("AUCType", "W");
		return jo;
	}

}
