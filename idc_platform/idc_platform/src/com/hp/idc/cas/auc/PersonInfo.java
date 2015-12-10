package com.hp.idc.cas.auc;

import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class PersonInfo extends CommonInfo {
	public static final int PERSON_STATUS_NORMAL = 0;
	public static final int PERSON_STATUS_EVECTION = 1;
	public static final int PERSON_STATUS_OFF = 2;

	private String mobile;
	private String email;
	private String password;
	private int personStatus;

	private long createTime;

	/**
	 * ��ʷ����
	 */
	private String password_his;

	/**
	 * ����޸�ʱ��
	 */
	private long password_lastUpdate;

	public String getPassword_his() {
		return password_his == null ? "" : password_his;
	}

	public void setPassword_his(String password_his) {
		this.password_his = password_his;
	}

	public PersonInfo() {
		super();
		mobile = "";
		email = "";
		password = "";
		personStatus=0;
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

	public int getPersonStatus() {
		return personStatus;
	}

	public void setPersonStatus(int personStatus) {
		this.personStatus = personStatus;
	}

	public String getPassword() {
		return password != null ? password : "";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getPassword_lastUpdate() {
		return password_lastUpdate;
	}

	public void setPassword_lastUpdate(long password_lastUpdate) {
		this.password_lastUpdate = password_lastUpdate;
	}

	public String getDiffrenceStr(CommonInfo pInfo) {
		PersonInfo pi = (PersonInfo) pInfo;
		StringBuffer sb = new StringBuffer();
		sb.append(super.getDiffrenceStr(pInfo));
		if (!this.getMobile().equals(pi.getMobile()))
			sb.append("�ֻ�����\"" + this.getMobile() + "\"����Ϊ\"" + pi.getMobile()
					+ "\"\n");
		if (!this.getEmail().equals(pi.getEmail()))
			sb.append("������\"" + this.getEmail() + "\"����Ϊ\"" + pi.getEmail()
					+ "\"\n");
		if (this.getPassword_lastUpdate() != pi.getPassword_lastUpdate()) {
			if (pi.getPassword().equals(""))
				sb.append("��������\n");
			else
				sb.append("�޸�����\n");
		}

		return sb.toString();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("mobile", this.getMobile());
		jo.put("email", this.getEmail());
		jo.put("AUCType", "P");
		jo.put("personStatus", this.personStatus + "");
		return jo;
	}
}
