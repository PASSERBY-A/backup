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
 * 表示代码类型的数据源
 * 
 * @author 梅园
 * 
 */
public class CodeTypeDataSource extends FieldDataSource {
	/**
	 * 获取数据源的数据
	 * 
	 * @param filter
	 *            过滤表达式
	 * @param style
	 *            生成的数据样式
	 * @return 返回数据源的数据
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
	 * 获取满足过滤条件的所有键值
	 * 
	 * @param filter
	 *            过滤表达式
	 * @return 返回满足过滤条件的所有键值 List<String>
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
	 * 获取指定键值的显示数据
	 * 
	 * @param id
	 *            键值
	 * @return 返回指定键值的显示数据
	 */
	public String getDisplayText(String id) {
		int oid = StringUtil.parseInt(id, -1);
		CodeInfo info = CIManager.getCodeByOid(oid);
		if (info == null)
			return id;
		return info.getDisplayName();
	}
}
