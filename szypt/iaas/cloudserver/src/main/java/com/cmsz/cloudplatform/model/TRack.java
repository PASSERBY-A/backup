package com.cmsz.cloudplatform.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;

@Entity(name = "T_RACK")
public class TRack extends BaseEntity {
	private static final long serialVersionUID = 2225055241064405828L;

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

	@SequenceGenerator(sequenceName = "t_rack_id_seq", name = "t_rack_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_rack_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "T_RACK_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "SERIALNUMBER")
	private String serialnumber;

	@Column(name = "UUID")
	private String uuid;
	
	@Column(name="RACKTYPE")
	private String type;

	@Column(name = "BAYCOUNT")
	private Long baycount;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "SAVESTATUS")
	private Integer saveStatus;

	@Column(name = "OAIP")
	private String oaip;

	@OneToMany(mappedBy = "fkRack", cascade = CascadeType.ALL)
	@OrderBy("bayindex")
	private List<TBayinfo> bayinfos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBaycount() {
		return baycount;
	}

	public void setBaycount(Long baycount) {
		this.baycount = baycount;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((baycount == null) ? 0 : baycount.hashCode());
		result = prime * result + ((bayinfos == null) ? 0 : bayinfos.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((oaip == null) ? 0 : oaip.hashCode());
		result = prime * result + ((saveStatus == null) ? 0 : saveStatus.hashCode());
		result = prime * result + ((serialnumber == null) ? 0 : serialnumber.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		TRack other = (TRack) obj;
		if (baycount == null) {
			if (other.baycount != null)
				return false;
		} else if (!baycount.equals(other.baycount))
			return false;
		if (bayinfos == null) {
			if (other.bayinfos != null)
				return false;
		} else if (!bayinfos.equals(other.bayinfos))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (oaip == null) {
			if (other.oaip != null)
				return false;
		} else if (!oaip.equals(other.oaip))
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
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String
				.format("TRack [id=%s, name=%s, serialnumber=%s, uuid=%s, type=%s, baycount=%s, status=%s, saveStatus=%s, oaip=%s, bayinfos=%s]",
						id, name, serialnumber, uuid, type, baycount, status,
						saveStatus, oaip, bayinfos);
	}

	
}