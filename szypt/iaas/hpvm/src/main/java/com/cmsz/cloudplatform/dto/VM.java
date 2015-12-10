package com.cmsz.cloudplatform.dto;

import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.OS;

public class VM {
	private String name;
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	private String local_id;
	
	private String guest_label;
	
	
	
	public String getGuest_label() {
		return guest_label;
	}
	public void setGuest_label(String guest_label) {
		this.guest_label = guest_label;
	}
	public String getLocal_id() {
		return local_id;
	}
	public void setLocal_id(String local_id) {
		this.local_id = local_id;
	}

	private String model_name;
	private String serial_number;
	private String vm_version;
	private String vm_config_version;
	private String vm_version_label;
	private String vm_config_label;
	private String guest_type;
	private String resources_reserved;
	private String configuration_is_active;
	private OS os;
	private String guest_hostname;
	private String guest_vnic_ip_preference;
	private String appctrls;
	private String persistent_tunables;
	private String vm_state;
	private String vm_condition;
	private String boot_type;
	private String console_type;
	private String guest_ipv4_address;
	private String efi_path;
	private String pattern_path;
	private String guest_rev;
	private String run_serverid;
	private String run_pid;
	private String distributed;
	private String effective_serverid;
	private String graceful_stop_timeout;
	private String runnable_status;
	private String modify_status;
	private String visible_status;
	private String direct_io;
	private VCpu vcpu;
	private Memory memory;
	private Storage storage;
	private String hostName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getVm_version_label() {
		return vm_version_label;
	}
	public void setVm_version_label(String vm_version_label) {
		this.vm_version_label = vm_version_label;
	}
	public String getVm_config_label() {
		return vm_config_label;
	}
	public void setVm_config_label(String vm_config_label) {
		this.vm_config_label = vm_config_label;
	}
	public String getGuest_type() {
		return guest_type;
	}
	public void setGuest_type(String guest_type) {
		this.guest_type = guest_type;
	}
	public String getResources_reserved() {
		return resources_reserved;
	}
	public void setResources_reserved(String resources_reserved) {
		this.resources_reserved = resources_reserved;
	}
	public String getConfiguration_is_active() {
		return configuration_is_active;
	}
	public void setConfiguration_is_active(String configuration_is_active) {
		this.configuration_is_active = configuration_is_active;
	}
	public OS getOs() {
		return os;
	}
	public void setOs(OS os) {
		this.os = os;
	}
	public String getGuest_hostname() {
		return guest_hostname;
	}
	public void setGuest_hostname(String guest_hostname) {
		this.guest_hostname = guest_hostname;
	}
	public String getGuest_vnic_ip_preference() {
		return guest_vnic_ip_preference;
	}
	public void setGuest_vnic_ip_preference(String guest_vnic_ip_preference) {
		this.guest_vnic_ip_preference = guest_vnic_ip_preference;
	}
	public String getAppctrls() {
		return appctrls;
	}
	public void setAppctrls(String appctrls) {
		this.appctrls = appctrls;
	}
	public String getPersistent_tunables() {
		return persistent_tunables;
	}
	public void setPersistent_tunables(String persistent_tunables) {
		this.persistent_tunables = persistent_tunables;
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
	public String getGuest_ipv4_address() {
		return guest_ipv4_address;
	}
	public void setGuest_ipv4_address(String guest_ipv4_address) {
		this.guest_ipv4_address = guest_ipv4_address;
	}
	public String getEfi_path() {
		return efi_path;
	}
	public void setEfi_path(String efi_path) {
		this.efi_path = efi_path;
	}
	public String getPattern_path() {
		return pattern_path;
	}
	public void setPattern_path(String pattern_path) {
		this.pattern_path = pattern_path;
	}
	public String getGuest_rev() {
		return guest_rev;
	}
	public void setGuest_rev(String guest_rev) {
		this.guest_rev = guest_rev;
	}
	public String getRun_serverid() {
		return run_serverid;
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
	public String getGraceful_stop_timeout() {
		return graceful_stop_timeout;
	}
	public void setGraceful_stop_timeout(String graceful_stop_timeout) {
		this.graceful_stop_timeout = graceful_stop_timeout;
	}
	public String getRunnable_status() {
		return runnable_status;
	}
	public void setRunnable_status(String runnable_status) {
		this.runnable_status = runnable_status;
	}
	public String getModify_status() {
		return modify_status;
	}
	public void setModify_status(String modify_status) {
		this.modify_status = modify_status;
	}
	public String getVisible_status() {
		return visible_status;
	}
	public void setVisible_status(String visible_status) {
		this.visible_status = visible_status;
	}
	public String getDirect_io() {
		return direct_io;
	}
	public void setDirect_io(String direct_io) {
		this.direct_io = direct_io;
	}
	public VCpu getVcpu() {
		return vcpu;
	}
	public void setVcpu(VCpu vcpu) {
		this.vcpu = vcpu;
	}
	public Memory getMemory() {
		return memory;
	}
	public void setMemory(Memory memory) {
		this.memory = memory;
	}
	public Storage getStorage() {
		return storage;
	}
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	
}
