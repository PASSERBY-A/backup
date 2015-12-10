package com.hp.idc.itsm.ci;

/**
 * ��ʾ������ϵ����Ϣ
 * 
 * @author ÷԰
 * 
 */
public class RelationInfo {
	/**
	 * �洢��������A
	 */
	protected CIInfo objectA;

	/**
	 * �洢��������B
	 */
	protected CIInfo objectB;

	/**
	 * �洢������ϵ����
	 */
	protected RelationTypeInfo type;

	/**
	 * ��ȡ������ϵ��id���� ����oid + "-" + A����oid + "-" + B����oid �Զ�����
	 * 
	 * @return ���ع�����ϵ��id
	 */
	public String getId() {
		return "" + this.type.getOid() + "-" + this.objectA.getOid() + "-"
				+ this.objectB.getOid();
	}

	/**
	 * ��ȡ������ϵ��������Ϣ
	 * 
	 * @return ���ع�����ϵ��������Ϣ
	 */
	public String getCaption() {
		return this.objectA.getName() + this.type.getCaption(this.objectB);
	}

	/**
	 * ��ȡ��������A
	 * 
	 * @return ���ع�������A
	 */
	public CIInfo getObjectA() {
		return this.objectA;
	}

	/**
	 * ���ù�������A
	 * 
	 * @param objectA
	 *            ��������A
	 */
	public void setObjectA(CIInfo objectA) {
		this.objectA = objectA;
	}

	/**
	 * ��ȡ��������A
	 * 
	 * @return ���ع�������B
	 */
	public CIInfo getObjectB() {
		return this.objectB;
	}

	/**
	 * ���ù�������B
	 * 
	 * @param objectB
	 *            ��������B
	 */
	public void setObjectB(CIInfo objectB) {
		this.objectB = objectB;
	}

	/**
	 * ��ȡ������ϵ����
	 * 
	 * @return ���ع�����ϵ����
	 */
	public RelationTypeInfo getType() {
		return this.type;
	}

	/**
	 * ���ù�����ϵ����
	 * 
	 * @param type
	 *            ������ϵ����
	 */
	public void setType(RelationTypeInfo type) {
		this.type = type;
	}

}
