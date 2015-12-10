package com.cmsz.cloudplatform.model.vo;

import java.util.Date;
import java.util.List;

import com.cmsz.cloudplatform.model.THba;

public class HostPropertyVO extends BaseVO {
	private static final long serialVersionUID = 8817457606942018340L;

	private Long id;

	private String type;

	private String vendor;

	private String model;

	private String code;

	private Date start_date;

	private Date end_date;

	private String cost;

	private String u_bit;

	private String u_high;

	private String position;

	private String position_desc;

	private String serial_num;

	private String owner;

	private String contractInfo;

	private String service_period;

	private Date expire_date;

	private String status;

	private String cpu_account;

	private String memory_size;

	private String hdd_size;

	private String section;

	private Long hostId;//

	private String manufacturer;

	private String productname;

	private String serialnumber;

	private String servername;

	private Integer cpucount;

	private Integer cpucores;

	private String cputype;

	private Integer memory;

	private Integer HostStatus;//

	private Integer saveStatus;

	private Integer hostType;//

	private String descript;

	private Integer nic;

	private Integer bayIndex;

	private String hostname;

	private String resourcepoolid;

	private List<THba> hbaList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getU_bit() {
		return u_bit;
	}

	public void setU_bit(String u_bit) {
		this.u_bit = u_bit;
	}

	public String getU_high() {
		return u_high;
	}

	public void setU_high(String u_high) {
		this.u_high = u_high;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition_desc() {
		return position_desc;
	}

	public void setPosition_desc(String position_desc) {
		this.position_desc = position_desc;
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(String contractInfo) {
		this.contractInfo = contractInfo;
	}

	public String getService_period() {
		return service_period;
	}

	public void setService_period(String service_period) {
		this.service_period = service_period;
	}

	public Date getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCpu_account() {
		return cpu_account;
	}

	public void setCpu_account(String cpu_account) {
		this.cpu_account = cpu_account;
	}

	public String getMemory_size() {
		return memory_size;
	}

	public void setMemory_size(String memory_size) {
		this.memory_size = memory_size;
	}

	public String getHdd_size() {
		return hdd_size;
	}

	public void setHdd_size(String hdd_size) {
		this.hdd_size = hdd_size;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Long getHostId() {
		return hostId;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public Integer getCpucount() {
		return cpucount;
	}

	public void setCpucount(Integer cpucount) {
		this.cpucount = cpucount;
	}

	public Integer getCpucores() {
		return cpucores;
	}

	public void setCpucores(Integer cpucores) {
		this.cpucores = cpucores;
	}

	public String getCputype() {
		return cputype;
	}

	public void setCputype(String cputype) {
		this.cputype = cputype;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getHostStatus() {
		return HostStatus;
	}

	public void setHostStatus(Integer hostStatus) {
		HostStatus = hostStatus;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public Integer getHostType() {
		return hostType;
	}

	public void setHostType(Integer hostType) {
		this.hostType = hostType;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getNic() {
		return nic;
	}

	public void setNic(Integer nic) {
		this.nic = nic;
	}

	public Integer getBayIndex() {
		return bayIndex;
	}

	public void setBayIndex(Integer bayIndex) {
		this.bayIndex = bayIndex;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getResourcepoolid() {
		return resourcepoolid;
	}

	public void setResourcepoolid(String resourcepoolid) {
		this.resourcepoolid = resourcepoolid;
	}

	public List<THba> getHbaList() {
		return hbaList;
	}

	public void setHbaList(List<THba> hbaList) {
		this.hbaList = hbaList;
	}

}
