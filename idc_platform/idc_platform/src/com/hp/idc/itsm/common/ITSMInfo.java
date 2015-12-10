/**
 * 
 */
package com.hp.idc.itsm.common;

/**
 * ����������
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class ITSMInfo {

	/**
	 * ״̬������
	 */
	public static int STATUS_NORMAL = 0;

	/**
	 * ״̬����ɾ��
	 */
	public static int STATUS_DELETED = 1;
	
	//OID
	protected int oid = -1;

	//id
	protected String id;
	
	//����
	protected String name;
	
	//ϵͳ��¼
	protected boolean isSystem = false;
	
	/**
	 * @return the oid
	 */
	public int getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isSystem
	 */
	public boolean isSystem() {
		return isSystem;
	}

	/**
	 * @param isSystem the isSystem to set
	 */
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
}
