package com.cmsz.cloudplatform.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Info")
public class Host {
	
	private long hostId;
	private String manufacturer;
	private String productName;
	private String serialNumber;
	private String serverName;
	private int cpuCount;
	private int cpuCores;
	private String cpuType;
	private int memory;
	//private int status;
	private int type;
	private String descript;
	private int nic;
	private Integer bay_index;
	private String hostName;
	private String resorucepoolid;
	private List<HBA> hbas;
	
	public List<HBA> getHbas() {
		return hbas;
	}
	public void setHbas(List<HBA> hbas) {
		this.hbas = hbas;
	}
	public long getHostId() {
		return hostId;
	}
	public void setHostId(long hostId) {
		this.hostId = hostId;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	public int getCpuCores() {
		return cpuCores;
	}
	public void setCpuCores(int cpuCores) {
		this.cpuCores = cpuCores;
	}
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
/*	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}*/
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public int getNic() {
		return nic;
	}
	public void setNic(int nic) {
		this.nic = nic;
	}
	public Integer getBay_index() {
		return bay_index;
	}
	public void setBay_index(Integer bay_index) {
		this.bay_index = bay_index;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getResorucepoolid() {
		return resorucepoolid;
	}
	public void setResorucepoolid(String resorucepoolid) {
		this.resorucepoolid = resorucepoolid;
	}
	
	
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	@Override
	public String toString() {
		return String
				.format("Host [hostId=%s, manufacturer=%s, productName=%s, serialNumber=%s, serverName=%s, cpuCount=%s, cpuCores=%s, cpuType=%s, memory=%s, type=%s, descript=%s, nic=%s, bay_index=%s, hostName=%s, resorucepoolid=%s, hbas=%s]",
						hostId, manufacturer, productName, serialNumber,
						serverName, cpuCount, cpuCores, cpuType, memory, type,
						descript, nic, bay_index, hostName, resorucepoolid,
						hbas);
	}
	
	
	
	
}
