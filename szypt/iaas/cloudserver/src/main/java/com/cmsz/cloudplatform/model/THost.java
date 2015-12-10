package com.cmsz.cloudplatform.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;

@Entity(name = "T_HOST")
public class THost extends BaseEntity {
	private static final long serialVersionUID = 1606205145046074804L;

	// 主机类型：
	// x86机架式服务器--1;
	// x86刀片服务器--2;
	// hp小型机--3
	public static final Integer TYPE_X86_RACK = 1;

	public static final Integer TYPE_X86_ENCLOSURE = 2;

	public static final Integer TYPE_HP = 3;

	// 状态:
	// 1:初始化
	// 2：归档（己保存到资产）
	// 3：废弃
	// 4：删除
	public static final Integer STATUS_INIT = 1;

	public static final Integer STATUS_ARCHIVE = 2;

	public static final Integer STATUS_ERASE = 3;

	public static final Integer STATUS_DELETE = 4;

	// 0：未处理
	// 1：己处理
	public static final Integer SAVE_STATUS_DEAL = 1;

	public static final Integer SAVE_STATUS_UNDEAL = 0;

	@SequenceGenerator(sequenceName = "t_host_id_seq", name = "t_host_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_host_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "PK_HOST_ID")
	private Long id;

	@Column(name = "MANUFACTURER")
	private String manufacturer;

	@Column(name = "PRODUCTNAME")
	private String productname;

	@Column(name = "SERIALNUMBER")
	private String serialnumber;

	@Column(name = "SERVERNAME")
	private String servername;

	@Column(name = "CPUCOUNT")
	private Integer cpucount;

	@Column(name = "CPUCORES")
	private Integer cpucores;

	@Column(name = "CPUTYPE")
	private String cputype;

	@Column(name = "MEMORY")
	private Integer memory;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "SAVESTATUS")
	private Integer saveStatus;

	@Column(name = "TYPE")
	private Integer type;

	@Column(name = "DESCRIPT")
	private String descript;

	@Column(name = "NIC")
	private Integer nic;

	@Column(name = "BAY_INDEX")
	private Integer bayIndex;

	@Column(name = "HOSTNAME")
	private String hostname;

	@Column(name = "RESOURCEPOOLID")
	private String resourcepoolid;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="fkHost")
	//@OrderBy("pk_hba_id")
	private List<THba> hbaList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bayIndex == null) ? 0 : bayIndex.hashCode());
		result = prime * result + ((cpucores == null) ? 0 : cpucores.hashCode());
		result = prime * result + ((cpucount == null) ? 0 : cpucount.hashCode());
		result = prime * result + ((cputype == null) ? 0 : cputype.hashCode());
		result = prime * result + ((descript == null) ? 0 : descript.hashCode());
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((memory == null) ? 0 : memory.hashCode());
		result = prime * result + ((nic == null) ? 0 : nic.hashCode());
		result = prime * result + ((productname == null) ? 0 : productname.hashCode());
		result = prime * result + ((resourcepoolid == null) ? 0 : resourcepoolid.hashCode());
		result = prime * result + ((saveStatus == null) ? 0 : saveStatus.hashCode());
		result = prime * result + ((serialnumber == null) ? 0 : serialnumber.hashCode());
		result = prime * result + ((servername == null) ? 0 : servername.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		THost other = (THost) obj;
		if (bayIndex == null) {
			if (other.bayIndex != null)
				return false;
		} else if (!bayIndex.equals(other.bayIndex))
			return false;
		if (cpucores == null) {
			if (other.cpucores != null)
				return false;
		} else if (!cpucores.equals(other.cpucores))
			return false;
		if (cpucount == null) {
			if (other.cpucount != null)
				return false;
		} else if (!cpucount.equals(other.cpucount))
			return false;
		if (cputype == null) {
			if (other.cputype != null)
				return false;
		} else if (!cputype.equals(other.cputype))
			return false;
		if (descript == null) {
			if (other.descript != null)
				return false;
		} else if (!descript.equals(other.descript))
			return false;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (memory == null) {
			if (other.memory != null)
				return false;
		} else if (!memory.equals(other.memory))
			return false;
		if (nic == null) {
			if (other.nic != null)
				return false;
		} else if (!nic.equals(other.nic))
			return false;
		if (productname == null) {
			if (other.productname != null)
				return false;
		} else if (!productname.equals(other.productname))
			return false;
		if (resourcepoolid == null) {
			if (other.resourcepoolid != null)
				return false;
		} else if (!resourcepoolid.equals(other.resourcepoolid))
			return false;
		if (saveStatus == null) {
			if (other.saveStatus != null)
				return false;
		} else if (!saveStatus.equals(other.saveStatus))
			return false;
		if (serialnumber == null) {
			if (other.serialnumber != null)
				return false;
		} else if (!serialnumber.equals(other.serialnumber))
			return false;
		if (servername == null) {
			if (other.servername != null)
				return false;
		} else if (!servername.equals(other.servername))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "THost [id=" + id + ", manufacturer=" + manufacturer + ", productname=" + productname + ", serialnumber="
				+ serialnumber + ", servername=" + servername + ", cpucount=" + cpucount + ", cpucores=" + cpucores
				+ ", cputype=" + cputype + ", memory=" + memory + ", status=" + status + ", saveStatus=" + saveStatus
				+ ", type=" + type + ", descript=" + descript + ", nic=" + nic + ", bayIndex=" + bayIndex + ", hostname="
				+ hostname + ", resourcepoolid=" + resourcepoolid + "]";
	}

}