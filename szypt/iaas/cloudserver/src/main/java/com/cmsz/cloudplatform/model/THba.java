package com.cmsz.cloudplatform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;

@Entity(name = "T_HBA")
public class THba extends BaseEntity {
	private static final long serialVersionUID = -2690928667823327080L;

	@SequenceGenerator(sequenceName = "t_hba_id_seq", name = "t_hba_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_hba_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "pk_hba_id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "wwn")
	private String wwn;

	@ManyToOne
	@JoinColumn(name = "fk_host_sn", referencedColumnName="SERIALNUMBER")
	private THost fkHost;

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

	public String getWwn() {
		return wwn;
	}

	public void setWwn(String wwn) {
		this.wwn = wwn;
	}

	public THost getFkHost() {
		return fkHost;
	}

	public void setFkHost(THost fkHost) {
		this.fkHost = fkHost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fkHost == null) ? 0 : fkHost.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((wwn == null) ? 0 : wwn.hashCode());
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
		THba other = (THba) obj;
		if (fkHost == null) {
			if (other.fkHost != null)
				return false;
		} else if (!fkHost.equals(other.fkHost))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (wwn == null) {
			if (other.wwn != null)
				return false;
		} else if (!wwn.equals(other.wwn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "THba [id=" + id + ", type=" + type + ", wwn=" + wwn + ", fkHost=" + fkHost + "]";
	}

}
