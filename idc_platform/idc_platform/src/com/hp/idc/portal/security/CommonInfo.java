package com.hp.idc.portal.security;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;


public class CommonInfo {

	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_DELETE = 1;

	private String name;
	private String id;
	private int oid;
	private int parentOid;
	private String parentId;

	// ------------ϵͳ����---------------//
	public CommonInfo() {
		name = "";
		id = "";
		oid = -1;
		parentOid = -1;
		parentId = "";
	}

	public String getName() {
		return name != null ? name : "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id != null ? id : "";
	}

	public void setId(String id) {
//		if (id !=null) {
//			id = id.toLowerCase().trim();
//			this.id = id;
//		}
		this.id = id;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getParentOid() {
		return parentOid;
	}

	public void setParentOid(int parentOid) {
		this.parentOid = parentOid;
	}

	public String getParentId() {
		return parentId != null ? parentId : "";
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * ��ȡ���ĺ��������
	 * 
	 * @param cInfo
	 *            ���ĺ�Ķ���
	 * @return
	 */
	public String getDiffrenceStr(CommonInfo cInfo) {
		StringBuffer sb = new StringBuffer();
		if (!this.name.equals(cInfo.getName()))
			sb.append("������\"" + this.name + "\"����Ϊ\"" + cInfo.getName() + "\"\n");
		if (!this.getParentId().equals(cInfo.getParentId()))
			sb.append("��������\"" + this.getParentId() + "\"����Ϊ\"" + cInfo.getParentId() + "\"\n");
		return sb.toString();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("id", this.id);
		jo.put("oid", this.oid + "");
		jo.put("name", this.name);
		jo.put("parentId", this.parentId);
		jo.put("parentOid", this.parentOid + "");
		return jo;
	}

	public boolean equals(Object obj) {
		CommonInfo ci = (CommonInfo) obj;
		if (this.id.equals(ci.getId()))
			return true;
		if (this.oid == ci.getOid())
			return true;
		return false;
	}
}