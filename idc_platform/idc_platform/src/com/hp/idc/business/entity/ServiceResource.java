package com.hp.idc.business.entity;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.idc.resm.model.Model;

@Entity(name="ServiceResource")
@Table(name="business_service_resource")
@AssociationOverride(name="service", joinColumns=@JoinColumn(name="service_id"))
@AttributeOverride(name="resModelId", column=@Column(name="res_model_code"))
public class ServiceResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ServiceResourcePK id;
	
	@Column(name="res_amount",length=5)
	private Integer amount;
	
	@Column(name="remark",length=200)
	private String remark;
	
	@Transient
	private Model resModel;

	public ServiceResourcePK getId() {
		return id;
	}

	public void setId(ServiceResourcePK id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int hashCode(){
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		if (ServiceResource.class.isInstance(object)) {
			ServiceResource sr = (ServiceResource)object;
		    if (sr.getId().equals(getId())) {
		    	return true;
		    }
		}
		return false;
	}

	public Model getResModel() {
		return resModel;
	}

	public void setResModel(Model resModel) {
		this.resModel = resModel;
	}
}
