package com.hp.idc.itsm.security;

import com.hp.idc.itsm.ci.CIInfo;

public class RoleInfo extends CIInfo{

	private String moId = "";
	
	private int level = 0;
	
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
}
