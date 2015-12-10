package com.hp.idc.common.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.hp.idc.common.core.bo.AbstractBaseBO;
/**
 * ����ʵ�������
 * ����������ʵ���඼��Ҫ�̳и���
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
	 * ����ʱ��Ĭ��Ϊ0������ɵ��ôι��캯��ʵ��Ĭ��ֵ
	 */
	public RemovableEntity(){
		this.removed=0;
	}
	
	/**
	 * ������Entity�Ƿ����Ѿ���ɾ��
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