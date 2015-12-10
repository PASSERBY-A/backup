package com.hp.idc.itsm.configure.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.CodeInfo;
import com.hp.idc.itsm.ci.CodeTypeInfo;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ��ʾ�������͵�����Դ
 * 
 * @author ÷԰
 * 
 */
public class CodeTypeDataSource extends FieldDataSource {
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
		for (int i = 0; i < list.size(); i++) {
			CodeTypeInfo info = CIManager.getCodeTypeByOid(Integer
					.parseInt((String) list.get(i)));
			if (i > 0)
				sb.append(",");
			sb.append("[");
			sb.append("\"" + info.getOid() + "\",\"" + info.getName() + "\"");
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * ��ȡ����������������м�ֵ
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @return ��������������������м�ֵ List<String>
	 */
	public List getKeys(String filter) {
		List list = CIManager.getCodeTypes();
		Collections.sort(list, new Comparator() {
			public int compare(Object a, Object b) {
				String id1 = ((CodeTypeInfo)a).getName();
				String id2 = ((CodeTypeInfo)b).getName();
				return id1.compareTo(id2);
			}
		});
		List ret = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CodeTypeInfo t = (CodeTypeInfo) list.get(i);
			ret.add("" + t.getOid());
		}
		return ret;
	}

	/**
	 * ��ȡָ����ֵ����ʾ����
	 * 
	 * @param id
	 *            ��ֵ
	 * @return ����ָ����ֵ����ʾ����
	 */
	public String getDisplayText(String id) {
		int oid = StringUtil.parseInt(id, -1);
		CodeInfo info = CIManager.getCodeByOid(oid);
		if (info == null)
			return id;
		return info.getDisplayName();
	}
}
