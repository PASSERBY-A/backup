package com.hp.idc.itsm.configure.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 表示以分隔符分隔的文本数据源
 * 
 * @author 梅园
 * 
 */
public class TextDataSource extends FieldDataSource {
	/**
	 * 存储所有键值的列表
	 */
	protected List keys = new ArrayList();

	/**
	 * 存储键值与对应数据的表
	 */
	protected Map map = new HashMap();

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
	 * 加载数据源的配置信息<Br>
	 * 格式key1[=value1]|key2[=value2]|....|keyn[=valuen]
	 * 
	 * @param data
	 *            数据源的配置信息
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
	 * 获取满足过滤条件的所有键值
	 * 
	 * @param filter
	 *            过滤表达式,本参数在此处没有使用
	 * @return 返回满足过滤条件的所有键值 List<String>
	 */
	public List getKeys(String filter) {
		return this.keys;
	}

	/**
	 * 获取指定键值的显示数据
	 * 
	 * @param id
	 *            键值
	 * @return 返回指定键值的显示数据
	 */
	public String getDisplayText(String id) {
		String ret = (String) this.map.get(id);
		if (ret == null)
			return id;
		return ret;
	}
}
