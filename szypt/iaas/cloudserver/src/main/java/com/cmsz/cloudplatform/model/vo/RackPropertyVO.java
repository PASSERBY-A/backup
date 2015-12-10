package com.cmsz.cloudplatform.model.vo;

import java.util.Date;
import java.util.List;

import com.cmsz.cloudplatform.model.TBayinfo;

public class RackPropertyVO extends BaseVO {

	private static final long serialVersionUID = 7685961565445913276L;

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

	private Long rackId;

	private String name;

	private String serialnumber;

	private String uuid;

	private String rackType;

	private Long baycount;

	private Integer rackStatus;

	private Integer saveStatus;

	private String oaip;

	private List<TBayinfo> bayinfos;

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

	public Long getRackId() {
		return rackId;
	}

	public void setRackId(Long rackId) {
		this.rackId = rackId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getRackType() {
		return rackType;
	}

	public void setRackType(String rackType) {
		this.rackType = rackType;
	}

	public Long getBaycount() {
		return baycount;
	}

	public void setBaycount(Long baycount) {
		this.baycount = baycount;
	}

	public Integer getRackStatus() {
		return rackStatus;
	}

	public void setRackStatus(Integer rackStatus) {
		this.rackStatus = rackStatus;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public String getOaip() {
		return oaip;
	}

	public void setOaip(String oaip) {
		this.oaip = oaip;
	}

	public List<TBayinfo> getBayinfos() {
		return bayinfos;
	}

	public void setBayinfos(List<TBayinfo> bayinfos) {
		this.bayinfos = bayinfos;
	}

}
