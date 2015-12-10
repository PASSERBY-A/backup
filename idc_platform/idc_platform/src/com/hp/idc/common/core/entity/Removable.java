package com.hp.idc.common.core.entity;

/**
 * �߼�ɾ��ʵ��ӿ�
 * �����߼�ɾ����ʵ����Ӧ��ʵ�ָýӿ�
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public abstract interface Removable{
	/**
	 * �ж�ʵ���Ƿ�ɾ���ķ���
	 */
	public abstract boolean isEntityRemoved();
	/**
	 * ����ʵ���Ƿ�ɾ���ķ���
	 */
	public abstract void setRemoved(int removed);
}