package com.hp.idc.common.core.entity;

/**
 * 逻辑删除实体接口
 * 所有逻辑删除的实体类应该实现该接口
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public abstract interface Removable{
	/**
	 * 判断实体是否删除的方法
	 */
	public abstract boolean isEntityRemoved();
	/**
	 * 设置实体是否删除的方法
	 */
	public abstract void setRemoved(int removed);
}