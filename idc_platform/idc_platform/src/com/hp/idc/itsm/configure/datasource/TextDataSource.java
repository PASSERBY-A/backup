package com.hp.idc.itsm.configure.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ��ʾ�Էָ����ָ����ı�����Դ
 * 
 * @author ÷԰
 * 
 */
public class TextDataSource extends FieldDataSource {
	/**
	 * �洢���м�ֵ���б�
	 */
	protected List keys = new ArrayList();

	/**
	 * �洢��ֵ���Ӧ���ݵı�
	 */
	protected Map map = new HashMap();

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
		if ("tree".equals(style)) {
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(",");
				String k = (String) list.get(i);
				String v = (String) this.map.get(k);
				sb.append("{");
				sb.append("icon : Ext.BLANK_IMAGE_URL,");
				sb.append("_click: true,");
				sb.append("id : \"" + k + "\",");
				sb.append("text : \"" + StringUtil.escapeHtml(v) + "\"}");
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(",");
				String k = (String) list.get(i);
				String v = (String) this.map.get(k);
				sb.append("[\"" + StringUtil.escapeHtml(k) + "\",\""
						+ StringUtil.escapeHtml(v) + "\"]");
			}
		}
		return sb.toString();
	}

	/**
	 * ��������Դ��������Ϣ<Br>
	 * ��ʽkey1[=value1]|key2[=value2]|....|keyn[=valuen]
	 * 
	 * @param data
	 *            ����Դ��������Ϣ
	 */
	public void load(String data) {
		if (data == null || data.length() == 0)
			return;
		String vals[] = data.split("\\|");
		for (int i = 0; i < vals.length; i++) {
			int pos = vals[i].indexOf("=");
			if (pos == -1)
				pos = vals[i].indexOf(";");
			if (pos == -1)
				pos = vals[i].indexOf(",");
			String k, v;
			if (pos != -1) {
				k = vals[i].substring(0, pos);
				v = vals[i].substring(pos + 1);
			} else {
				k = v = vals[i];
			}
			if (k.length() > 0) {
				this.keys.add(k);
				this.map.put(k, v);
			}
		}
	}

	/**
	 * ��ȡ����������������м�ֵ
	 * 
	 * @param filter
	 *            ���˱��ʽ,�������ڴ˴�û��ʹ��
	 * @return ��������������������м�ֵ List<String>
	 */
	public List getKeys(String filter) {
		return this.keys;
	}

	/**
	 * ��ȡָ����ֵ����ʾ����
	 * 
	 * @param id
	 *            ��ֵ
	 * @return ����ָ����ֵ����ʾ����
	 */
	public String getDisplayText(String id) {
		String ret = (String) this.map.get(id);
		if (ret == null)
			return id;
		return ret;
	}
}
