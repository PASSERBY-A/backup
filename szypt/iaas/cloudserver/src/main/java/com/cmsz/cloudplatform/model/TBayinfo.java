package com.cmsz.cloudplatform.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;

@Entity(name = "T_BAYINFO")
public class TBayinfo extends BaseEntity {
	private static final long serialVersionUID = -6288376384574408250L;

//	private static final String STATUS_OK = "OK";
//
//	private static final String STATUS_ = "";

	// private static String STATUS_
	// private static String STATUS_
	//
//	private static final String HOST_STATUS_OK = "OK";
//
//	private static final String HOST_STATUS_ = "";

	@SequenceGenerator(sequenceName = "t_bayinfo_id_seq", name = "t_bayinfo_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_bayinfo_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "PK_BAYINFO_ID")
	private Long id;

	@Column(name = "BAYINDEX")
	private Integer bayindex;

	@Column(name = "ILONAME")
	private String iloname;

	@Column(name = "ILOIP")
	private String iloip;

	@Column(name = "SERIALNUM")
	private String serialnum;

	@Column(name = "SERVERNAME")
	private String servername;

	@Column(name = "HOST_STATUS")
	private String hostStatus;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "POWERFLAG")
	private String powerflag;

	@ManyToOne
	@JoinColumn(name = "FK_RACK_ID")
	private TRack fkRack;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBayindex() {
		return bayindex;
	}

	public void setBayindex(Integer bayindex) {
		this.bayindex = bayindex;
	}

	public String getIloname() {
		return iloname;
	}

	public void setIloname(String iloname) {
		this.iloname = iloname;
	}

	public String getIloip() {
		return iloip;
	}

	public void setIloip(String iloip) {
		this.iloip = iloip;
	}

	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getHostStatus() {
		return hostStatus;
	}

	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPowerflag() {
		return powerflag;
	}

	public void setPowerflag(String powerflag) {
		this.powerflag = powerflag;
	}

	public TRack getFkRackId() {
		return fkRack;
	}

	public void setFkRack(TRack fkRack) {
		this.fkRack = fkRack;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bayindex == null) ? 0 : bayindex.hashCode());
		result = prime * result + ((fkRack == null) ? 0 : fkRack.hashCode());
		result = prime * result + ((hostStatus == null) ? 0 : hostStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((iloip == null) ? 0 : iloip.hashCode());
		result = prime * result + ((iloname == null) ? 0 : iloname.hashCode());
		result = prime * result + ((powerflag == null) ? 0 : powerflag.hashCode());
		result = prime * result + ((serialnum == null) ? 0 : serialnum.hashCode());
		result = prime * result + ((servername == null) ? 0 : servername.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		TBayinfo other = (TBayinfo) obj;
		if (bayindex == null) {
			if (other.bayindex != null)
				return false;
		} else if (!bayindex.equals(other.bayindex))
			return false;
		if (fkRack == null) {
			if (other.fkRack != null)
				return false;
		} else if (!fkRack.equals(other.fkRack))
			return false;
		if (hostStatus == null) {
			if (other.hostStatus != null)
				return false;
		} else if (!hostStatus.equals(other.hostStatus))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (iloip == null) {
			if (other.iloip != null)
				return false;
		} else if (!iloip.equals(other.iloip))
			return false;
		if (iloname == null) {
			if (other.iloname != null)
				return false;
		} else if (!iloname.equals(other.iloname))
			return false;
		if (powerflag == null) {
			if (other.powerflag != null)
				return false;
		} else if (!powerflag.equals(other.powerflag))
			return false;
		if (serialnum == null) {
			if (other.serialnum != null)
				return false;
		} else if (!serialnum.equals(other.serialnum))
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
		return true;
	}

	@Override
	public String toString() {
		return "TBayinfo [id=" + id + ", bayindex=" + bayindex + ", iloname=" + iloname + ", iloip=" + iloip
				+ ", serialnum=" + serialnum + ", servername=" + servername + ", hostStatus=" + hostStatus + ", status="
				+ status + ", powerflag=" + powerflag + ", fkRackId=" + fkRack + "]";
	}

}