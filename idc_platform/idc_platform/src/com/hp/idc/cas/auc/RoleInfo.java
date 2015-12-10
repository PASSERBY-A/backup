package com.hp.idc.cas.auc;

import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * ������/��֯���Ҫ���������ɫ������Ƴ�/���Ƴ�/�鳤���ȵ�
 * ���������Щ��ɫ����Ϣ��
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class RoleInfo extends CommonInfo{

	/**
	 * �˽�ɫ�����Ĺ�����/��֯ID
	 */
	private String moId="";

	/**
	 * ������������£�����ԽС������Խ��
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
