package com.hp.idc.common.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SysBaseTypePK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8036522941741708216L;

	@Column(name="code_id",nullable=false, length=5)
	private long id;
	
	@Column(name="code_type",nullable=false, length=5)
	private long type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}
	
	
	@Override
	public int hashCode(){
		int result = 1;
		final int prime=31;
		result = (int) (prime * result + id);
		result = (int) (prime * result + type);
		return result;
	}
	
	@Override
	public boolean equals(Object object){
		if (SysBaseTypePK.class.isInstance(object)) {
			SysBaseTypePK sr = (SysBaseTypePK)object;
		    if (sr.getId()==getId()
		    		&&sr.getType()==getType()) {
		    	return true;
		    }
		}
		return false;
	}
}
