package com.hp.idc.itsm.security;

import com.hp.idc.itsm.ci.CIInfo;

public class LocalgroupInfo extends CIInfo {


	/**
	 * 所属的流程
	 */
	private String wfOid = "";

	public LocalgroupInfo(){
		
	}

	public String getWfOid() {
		return (wfOid == null || wfOid.equals(""))?"-1":wfOid;
	}

	public void setWfOid(String wfOid) {
		this.wfOid = wfOid;
	}

}
