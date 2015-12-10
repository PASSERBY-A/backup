package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.json.JSONArray;

public interface PersonManagerInterface {

	/**
	 * ����id��ȡ��Ա��Ϣ
	 * @param id ��Աid
	 * @return �����ҵ�����Ա����
	 */
	public PersonInfoInterface getPersonById(String id);
	/**
	 * ����id��ȡ��Ա����
	 * @param id ��Աid
	 * @return �����ҵ�����Ա����,�Ҳ����ķ���ԭid����
	 */
	public String getPersonNameById(String id);
	
	/**
	 * ����sql��ѯ��Ա��Ϣ
	 * @param sql
	 * @return ����sql�Ľ����������Ӧ��JSON����
	 */
	public JSONArray getPersonsBySQL(String sql);
	/**
	 * ��ȡ������Ա��Ϣ
	 * @return ����������Ա��ϢList<PersonInfo>
	 */
	public List getAllPersons();
	
	/**
	 * ��������
	 * @param userId �û�id
	 * @param newPasswd ������
	 * @return �ɹ�ʱ����null, ʧ�ܷ��ش�����Ϣ
	 */
	public String resetPassword(String userId, 
			String newPasswd);

	/**
	 * �����û�����
	 * @param userId �û�����
	 * @param oldPasswd ԭ����
	 * @param newPasswd ������
	 * @return �ɹ�ʱ����null, ʧ�ܷ��ش�����Ϣ
	 */
	public String changePassword(String userId, String oldPasswd,
			String newPasswd);

	/**
	 * �û���¼
	 * @param userId �û�id
	 * @param passwd ����
	 * @return �ɹ�����null, ʧ�ܷ��ش�����Ϣ
	 */
	public String login(String userId, String passwd);
		
	public List getPersonsByWorkgoupId(String workgroupId,boolean includeChildren);
		
	public List getPersonsByOrganizationId(String organizationId,boolean includeChildren);

	/**
	 * ��������ϵͳ����Աid��Ӧ��ϵͳ��id����OVSD��system��Ӧ��ϵͳ��ROOT��
	 * ����ϵͳ�Ľӿ������Լ�ʵ��
	 * @param id
	 * @return
	 */
	public String getLocalId(String id);
	
	/**
	 * ���ر�ϵͳ��Ӧ���ϵͳ��ID���籾ϵͳ��root��ӦOVSD��system��
	 * @param id
	 * @return
	 */
	public String getRemoteId(String id);
}
