package com.hp.idc.itsm.security.rule;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.security.IRuleInfo;

/**
 * ����id��Ȩ�޿�����
 * 
 * @author ÷԰
 * 
 */
public class RuleInfo implements IRuleInfo {

	/**
	 * �洢��ص�ID�б�
	 */
	protected String[] ids;

	/**
	 * �洢ID�б����
	 */
	protected int count = 0;

	/**
	 * �洢�������򡰾ܾ���<br>
	 * �����������޸�
	 */
	protected String desc = "";

	/**
	 * ���id�Ƿ����б���
	 * @param id ָ����id
	 * @return ����id�Ƿ����б���
	 */
	public boolean checkId(String id) {
		for (int i = 0; i < this.count; i++) {
			if (this.ids[i].equalsIgnoreCase(id))
				return true;
		}
		return false;
	}

	/**
	 * ��id���з���������id�б�
	 * v1:����ֵ��void����Ϊint,�����ж�id�������й����м����ǺϷ�(int��)�ġ�
	 * 	  ���id���ж��ǷǷ���,��Ĭ��Ϊ������"Ϊ��"
	 * 
	 * @param id
	 *            �ԡ�,���ָ���ID�б�
	 * @return ����id������
	 */
	public int parse(String id) {
		String strIds[] = id.split(",");
		this.count = 0;
		this.ids = new String[strIds.length];
		for (int i = 0; i < strIds.length; i++) {
			if (strIds[i]==null || strIds[i].trim().length() == 0)
				continue;
			this.ids[this.count++] = strIds[i];
		}
		return this.count;
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return ���ع��������
	 */
	public String getDesc() {
		
		String str = this.desc+" : ";
		if (this.count == 0)
			str += "û������";
		else {
			for (int i = 0; i < this.count; i++) {
				CIInfo info = CIManager.getCIById(this.ids[i]);
				String name = (info == null) ? "?" : info.getName();
				if (i > 0)
					str += ",";
				str +=  name;
			}
		}
		return str;
	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:��ʾû�����ƣ�1:��ʾ����-1:��ʾ�ܾ���
	 */
	public int valid(String user, String org, String[] group) {
		return 0;
	}

}
