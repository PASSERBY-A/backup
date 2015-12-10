// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   OrganizationInfo.java

package com.hp.idc.portal.security;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;
import com.hp.idc.portal.security.CommonInfo;

public class OrganizationInfo extends CommonInfo {

	public OrganizationInfo() {
		super();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("AUCType", "O");
		return jo;
	}
}
