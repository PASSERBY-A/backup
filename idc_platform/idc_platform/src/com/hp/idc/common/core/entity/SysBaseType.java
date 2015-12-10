package com.hp.idc.common.core.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name="SysBaseType")
@Table(name="sys_base_type")
@AttributeOverrides(value = { 
		@AttributeOverride(name="id", column=@Column(name="code_id")),
		@AttributeOverride(name="type", column=@Column(name="code_type"))
})
public class SysBaseType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9144292923975303979L;

	@EmbeddedId
	private SysBaseTypePK id;
	
	@Column(name="code_name", length=100)
	private String name;
	
	@Column(name="code_name_nls", length=100)
	private String nls;
	
	@Column(name="sort_id", length=3)
	private long sortId;
	
	@Column(name="is_used", length=1)
	private long isUsed;
	
	@Column(name="code_desc", length=200)
	private String desc;

	public SysBaseTypePK getId() {
		return id;
	}

	public void setId(SysBaseTypePK id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNls() {
		return nls;
	}

	public void setNls(String nls) {
		this.nls = nls;
	}

	public long getSortId() {
		return sortId;
	}

	public void setSortId(long sortId) {
		this.sortId = sortId;
	}

	public long getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(long isUsed) {
		this.isUsed = isUsed;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode(){
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		if (SysBaseType.class.isInstance(object)) {
			SysBaseType sr = (SysBaseType)object;
		    if (sr.getId().equals(getId())) {
		    	return true;
		    }
		}
		return false;
	}

}
