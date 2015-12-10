package com.cmsz.cloudplatform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hp.core.model.BaseEntity;

@Entity
@Table(name = "T_HPVM")
public class Hpvm extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1527951534132767673L;

	@Id
	@Column(name = "PK_HPVM_ID")
	private long id;

	private String cpucount;

	private Integer deprecated;

	private String hostname;

	private String ip;

	private String memory;

	private String os;

	private String patternpath;

	private String physicaldevice;

	private String serialnumber;

	@Column(name = "\"UUID\"")
	private String uuid;

	private String vmname;

	private String vmstatus;// 虚机是否过时 1:己过时 0:当前在用，未过时

	private String vmversion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCpucount() {
		return cpucount;
	}

	public void setCpucount(String cpucount) {
		this.cpucount = cpucount;
	}

	public Integer getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Integer deprecated) {
		this.deprecated = deprecated;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getPatternpath() {
		return patternpath;
	}

	public void setPatternpath(String patternpath) {
		this.patternpath = patternpath;
	}

	public String getPhysicaldevice() {
		return physicaldevice;
	}

	public void setPhysicaldevice(String physicaldevice) {
		this.physicaldevice = physicaldevice;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVmname() {
		return vmname;
	}

	public void setVmname(String vmname) {
		this.vmname = vmname;
	}

	public String getVmstatus() {
		return vmstatus;
	}

	public void setVmstatus(String vmstatus) {
		this.vmstatus = vmstatus;
	}

	public String getVmversion() {
		return vmversion;
	}

	public void setVmversion(String vmversion) {
		this.vmversion = vmversion;
	}

}
