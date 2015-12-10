/**
 * 
 */
package com.hp.idc.cas.auc;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * �û�ӳ����
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class AUCMappingInfo {

	//��ϵͳ�û�ID
	private String userId;
	
	//������ϵͳ��ʶ
	private String thirdSystem;
	
	//������ϵͳ�û�ID
	private String thirdUserId;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the thirdSystem
	 */
	public String getThirdSystem() {
		return thirdSystem;
	}

	/**
	 * @param thirdSystem the thirdSystem to set
	 */
	public void setThirdSystem(String thirdSystem) {
		this.thirdSystem = thirdSystem;
	}

	/**
	 * @return the thirdUserId
	 */
	public String getThirdUserId() {
		return thirdUserId;
	}

	/**
	 * @param thirdUserId the thirdUserId to set
	 */
	public void setThirdUserId(String thirdUserId) {
		this.thirdUserId = thirdUserId;
	}
	
	public String toString(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("lu", this.userId);
			jo.put("ts", this.thirdSystem);
			jo.put("tu", this.thirdUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
}
