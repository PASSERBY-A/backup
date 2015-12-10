package com.volkswagen.tel.billing.billcall.jpa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "TBS_COSTCENTER_REPORT")
public class CostCenterReportEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "R4_GENERATOR", sequenceName = "SEQ_TBS_R4_GENERATOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R4_GENERATOR")
	private Long id;
	
	@Column(name = "cost_center", length=48)
	private String  costCenter;

	@Column(name = "cost_center_type", length=48)
	private String costCenterType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_")
	private Date date;
	
	@Column(name = "fixed_phone_cost")
	private Double fixedPhoneCost;
	
	@Column(name = "cell_phone_cost")
	private Double cellphoneCost;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		
		this.costCenter = costCenter;
		
		if(costCenter.startsWith("8"))
		{
			this.costCenterType = CostCenterType.VCIC.toString();
		}
		else if(costCenter.startsWith("7"))
		{
			this.costCenterType = CostCenterType.VGIC.toString();
		}
		else if(costCenter.startsWith("6"))
		{
			this.costCenterType = CostCenterType.AUDI.toString();
		}
		else
		{
			this.costCenterType = CostCenterType.VCRA.toString();
		}
		
	}

	public String getCostCenterType() {
	
		return costCenterType;
	
	}

	public void setCostCenterType(String costCenterType) {
		this.costCenterType = costCenterType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getFixedPhoneCost() {
		return fixedPhoneCost;
	}

	public void setFixedPhoneCost(Double fixedPhoneCost) {
		this.fixedPhoneCost = fixedPhoneCost;
	}

	public Double getCellphoneCost() {
		return cellphoneCost;
	}

	public void setCellphoneCost(Double cellphoneCost) {
		this.cellphoneCost = cellphoneCost;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "CostCenterReportEntity [id=" + id + ", costCenter="
				+ costCenter + ", costCenterType=" + costCenterType + ", date="
				+ date + ", fixedPhoneCost=" + fixedPhoneCost
				+ ", cellphoneCost=" + cellphoneCost + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((costCenter == null) ? 0 : costCenter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CostCenterReportEntity other = (CostCenterReportEntity) obj;
		if (costCenter == null) {
			if (other.costCenter != null)
				return false;
		} else if (!costCenter.equals(other.costCenter))
			return false;
		return true;
	}

	 
	 
	 
 

 
}
