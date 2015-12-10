package com.cmsz.cloudplatform.dto;

import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.OS;

public class VmSummary {
	
	private String hostName;
	
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	private String name;
	
	 private String uuid;
	
	private String local_id;
	
	private String guest_label;
	
	
	private String model_name;
	
	
	private String serial_number;
	
	
	private String vm_version;
	
	
	private String vm_config_version;
	
	
	private String vm_config_label;
	
	
	private String vm_version_label;
	
	private OS os;
	
	private String guest_type;
	private String direct_io_number;
	
	
	
	public String getGuest_type() {
		return guest_type;
	}
	public void setGuest_type(String guest_type) {
		this.guest_type = guest_type;
	}
	public String getDirect_io_number() {
		return direct_io_number;
	}
	public void setDirect_io_number(String direct_io_number) {
		this.direct_io_number = direct_io_number;
	}
	private String vm_state;
	
	
	private String vm_condition;
	
	
	private String boot_type;
	
	
	private String console_type;
	
	
	private String vcpu_number;
	
	
	private String device_number;
	
	
	private String networks_number;
	
	//private String direct_io_number;
	
	
	private Memory memory;
	
	private String run_serverid;
	
	
	private String run_pid;
	
	
	private String appctrls;
	
	
	private String distributed;
	
	
	private String effective_serverid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getLocal_id() {
		return local_id;
	}
	public void setLocal_id(String local_id) {
		this.local_id = local_id;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public String getVm_version() {
		return vm_version;
	}
	public void setVm_version(String vm_version) {
		this.vm_version = vm_version;
	}
	public String getVm_config_version() {
		return vm_config_version;
	}
	public void setVm_config_version(String vm_config_version) {
		this.vm_config_version = vm_config_version;
	}
	public String getVm_config_label() {
		return vm_config_label;
	}
	public void setVm_config_label(String vm_config_label) {
		this.vm_config_label = vm_config_label;
	}
	public String getVm_version_label() {
		return vm_version_label;
	}
	public void setVm_version_label(String vm_version_label) {
		this.vm_version_label = vm_version_label;
	}
	
	public OS getOs() {
		return os;
	}
	public void setOs(OS os) {
		this.os = os;
	}
	public String getVm_state() {
		return vm_state;
	}
	public void setVm_state(String vm_state) {
		this.vm_state = vm_state;
	}
	public String getVm_condition() {
		return vm_condition;
	}
	public void setVm_condition(String vm_condition) {
		this.vm_condition = vm_condition;
	}
	public String getBoot_type() {
		return boot_type;
	}
	public void setBoot_type(String boot_type) {
		this.boot_type = boot_type;
	}
	public String getConsole_type() {
		return console_type;
	}
	public void setConsole_type(String console_type) {
		this.console_type = console_type;
	}
	public String getVcpu_number() {
		return vcpu_number;
	}
	public void setVcpu_number(String vcpu_number) {
		this.vcpu_number = vcpu_number;
	}
	public String getDevice_number() {
		return device_number;
	}
	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}
	public String getNetworks_number() {
		return networks_number;
	}
	public void setNetworks_number(String networks_number) {
		this.networks_number = networks_number;
	}
	/*public String getDirect_io_number() {
		return direct_io_number;
	}
	public void setDirect_io_number(String direct_io_number) {
		this.direct_io_number = direct_io_number;
	}*/
	
	public String getRun_serverid() {
		return run_serverid;
	}
	public Memory getMemory() {
		return memory;
	}
	public void setMemory(Memory memory) {
		this.memory = memory;
	}
	public void setRun_serverid(String run_serverid) {
		this.run_serverid = run_serverid;
	}
	public String getRun_pid() {
		return run_pid;
	}
	public void setRun_pid(String run_pid) {
		this.run_pid = run_pid;
	}
	public String getAppctrls() {
		return appctrls;
	}
	public void setAppctrls(String appctrls) {
		this.appctrls = appctrls;
	}
	public String getDistributed() {
		return distributed;
	}
	public void setDistributed(String distributed) {
		this.distributed = distributed;
	}
	public String getEffective_serverid() {
		return effective_serverid;
	}
	public void setEffective_serverid(String effective_serverid) {
		this.effective_serverid = effective_serverid;
	}
	public String getGuest_label() {
		return guest_label;
	}
	public void setGuest_label(String guest_label) {
		this.guest_label = guest_label;
	}
	
	
}
