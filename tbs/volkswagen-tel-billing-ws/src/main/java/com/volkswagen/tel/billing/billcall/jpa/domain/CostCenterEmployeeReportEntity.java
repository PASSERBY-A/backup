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
@Table(name = "TBS_COSTCENTER_EMPLOYEE_REPORT")
public class CostCenterEmployeeReportEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "R3_GENERATOR", sequenceName = "SEQ_TBS_R3_GENERATOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R3_GENERATOR")
	private Long id;
	
	@Column(name = "user_id", length=48)
	private String userId;
	
	@Column(name = "name_", length=128)
	private String  name;
	
	@Column(name = "staffCode", length=48)
	private String staffCode;
	
	@Column(name = "cost_center", length=48)
	private String  costCenter;

	@Column(name = "cost_center_type", length=48)
	private String costCenterType;
	
	@Column(name = "telephone_number", length=48)
	private String telephoneNumber;
	
	@Column(name = "mobile_number", length=84)
	private String mobilePhone;
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
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

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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
		return "CostCenterEmployeeReportEntity [id=" + id + ", userId="
				+ userId + ", name=" + name + ", staffCode=" + staffCode
				+ ", costCenter=" + costCenter + ", costCenterType="
				+ costCenterType + ", telephoneNumber=" + telephoneNumber
				+ ", mobilePhone=" + mobilePhone + ", date=" + date
				+ ", fixedPhoneCost=" + fixedPhoneCost + ", cellphoneCost="
				+ cellphoneCost + ", lastUpdateTime=" + lastUpdateTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		CostCenterEmployeeReportEntity other = (CostCenterEmployeeReportEntity) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	 
 
}
