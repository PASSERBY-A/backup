package com.cmsz.cloudplatform.model;

public class TemplateCredentials {
	String templateId;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getOsuserName() {
		return osuserName;
	}
	public void setOsuserName(String osuserName) {
		this.osuserName = osuserName;
	}
	public String getFtpuserName() {
		return ftpuserName;
	}
	public void setFtpuserName(String ftpuserName) {
		this.ftpuserName = ftpuserName;
	}
	public String getOspasswd() {
		return ospasswd;
	}
	public void setOspasswd(String ospasswd) {
		this.ospasswd = ospasswd;
	}
	public String getFtppasswd() {
		return ftppasswd;
	}
	public void setFtppasswd(String ftppasswd) {
		this.ftppasswd = ftppasswd;
	}
	String osuserName;
	String ftpuserName;
	String ospasswd;
	String ftppasswd;
	
}
