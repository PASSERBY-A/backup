package com.hp.idc.cas.auc;

import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
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
