package com.hp.idc.common.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.hp.idc.common.core.bo.AbstractBaseBO;
/**
 * 基本实体抽象类
 * 并不是所有实体类都需要继承该类
 * 
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
@MappedSuperclass
public abstract class RemovableEntity extends AbstractBaseBO implements Serializable,Removable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5410253433567693733L;
	/**
	 * 构造时，默认为0，子类可调用次构造函数实现默认值
	 */
	public RemovableEntity(){
		this.removed=0;
	}
	
	/**
	 * 标记这个Entity是否是已经被删除
	 */
	private int removed;
	
	
	@Column(name="REMOVED",length=1,nullable=false)
	public Integer getRemoved() {
		return removed;
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.core.entity.Removable#setRemoved(int)
	 */
	@Override
	public void setRemoved(int removed) {
		this.removed = removed;
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.core.entity.Removable#isEntityRemoved()
	 */
	@Override
	@Transient
	public boolean isEntityRemoved(){
		if (this.removed == 1){
			return true;
		}
		return false;
	}
	
}