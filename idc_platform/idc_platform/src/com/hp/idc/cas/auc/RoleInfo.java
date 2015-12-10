package com.hp.idc.cas.auc;

import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 工作组/组织里，所要包含特殊角色，比如科长/副科长/组长，等等
 * 此类就是这些角色的信息类
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class RoleInfo extends CommonInfo{

	/**
	 * 此角色所属的工作组/组织ID
	 */
	private String moId="";

	/**
	 * 级别，正数情况下，数字越小，级别越大
	 */
	private int level = -1;
	
	public RoleInfo() {
		super();
		moId = "";
		level = -1;
	}

	public String getMoId() {
		return moId;
	}

	public void setMoId(String moId) {
		this.moId = moId;
	}
	
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("AUCType", "R");
		return jo;
	}
}
