package com.hp.idc.itsm.configure;

public class ViewSearchInfo {

	protected String nameEN = "";
	protected String nameZH = "";
	
	//所属工单系统
	protected String origin = "";

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getNameZH() {
		return nameZH;
	}

	public void setNameZH(String nameZH) {
		this.nameZH = nameZH;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
