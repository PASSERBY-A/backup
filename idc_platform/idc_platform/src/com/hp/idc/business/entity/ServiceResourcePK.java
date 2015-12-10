package com.hp.idc.business.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ServiceResourcePK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="service_id",nullable=false)
	private Service service;
	
	@Column(name="res_model_code",nullable=false, length=64)
	private String resModelId;
	
	public ServiceResourcePK(){};
	
	public ServiceResourcePK(Service service,String resModelId){
		this.service=service;
		this.resModelId=resModelId;
	}
	
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getResModelId() {
		return resModelId;
	}

	public void setResModelId(String resModelId) {
		this.resModelId = resModelId;
	}
	
	@Override
	public int hashCode(){
		int result = 1;
		final int prime=31;
		result = (int) (prime * result + ((service==null&&service.getId()>0)?0:service.getId()));
		result = (int) (prime * result + resModelId ==null?0:resModelId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object object){
		if (ServiceResourcePK.class.isInstance(object)) {
			ServiceResourcePK sr = (ServiceResourcePK)object;
		    if (sr.getService().getId()==(getService().getId())
		    		&&sr.getResModelId().equals(getResModelId())) {
		    	return true;
		    }
		}
		return false;
	}


}
