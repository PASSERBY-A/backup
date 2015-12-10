package com.hp.idc.itsm.configure.datasource;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ��ʾָ���������������������������Դ
 * 
 * @author ÷԰
 * 
 */
public class CIDataSource extends FieldDataSource {
	/**
	 * �洢����������
	 */
	protected int type;

	/**
	 * ��ȡ����Դ������
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @param style
	 *            ���ɵ�������ʽ
	 * @return ��������Դ������
	 */
	public String getData(String filter, String style) {
		StringBuffer sb = new StringBuffer();
		List list = getKeys(filter);
		ItsmUtil.sort(list, "name", false);
		if (style.equals("tree")) {
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(",");
				CIInfo info = (CIInfo)list.get(i);
				sb.append("{");
				sb.append("_click : true,");
				sb.append("icon : Ext.BLANK_IMAGE_URL,");
				sb.append("id : \"" + info.getId() + "\",");
				sb.append("text : \"" + StringUtil.escapeHtml(info.getName())
						+ "\"}");
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				CIInfo info = (CIInfo)list.get(i);
				if (i > 0)
					sb.append(",");
				sb.append("[");
				sb.append("\"" + info.getId() + "\",\"" + info.getDisplayName()
						+ "\"");
				sb.append("]");
			}
		}
		return sb.toString();
	}

	/**
	 * ��������Դ��������Ϣ
	 * 
	 * @param data
	 *            ����Դ��������Ϣ
	 */
	public void load(String data) {
		this.type = Integer.parseInt(data);
	}

	/**
	 * ��ȡ����������������м�ֵ
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @return ��������������������м�ֵ List<String>
	 */
	public List getKeys(String filter) {
		List list = CIManager.getCICategoryByOid(this.type).getCIs();
		ItsmUtil.filter(list, "id", filter);
		return list;
	}

	/**
	 * ��ȡָ����ֵ����ʾ����
	 * 
	 * @param id
	 *            ��ֵ
	 * @return ����ָ����ֵ����ʾ����
	 */
	public String getDisplayText(String id) {
		CIInfo info = CIManager.getCIById(id);
		if (info == null)
			return id;
		return info.getName();
	}
}
