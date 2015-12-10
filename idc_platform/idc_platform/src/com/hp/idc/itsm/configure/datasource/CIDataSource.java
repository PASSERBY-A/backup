package com.hp.idc.itsm.configure.datasource;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 表示指定配置项类别的所有配置项的数据源
 * 
 * @author 梅园
 * 
 */
public class CIDataSource extends FieldDataSource {
	/**
	 * 存储配置项类型
	 */
	protected int type;

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
	 * 加载数据源的配置信息
	 * 
	 * @param data
	 *            数据源的配置信息
	 */
	public void load(String data) {
		this.type = Integer.parseInt(data);
	}

	/**
	 * 获取满足过滤条件的所有键值
	 * 
	 * @param filter
	 *            过滤表达式
	 * @return 返回满足过滤条件的所有键值 List<String>
	 */
	public List getKeys(String filter) {
		List list = CIManager.getCICategoryByOid(this.type).getCIs();
		ItsmUtil.filter(list, "id", filter);
		return list;
	}

	/**
	 * 获取指定键值的显示数据
	 * 
	 * @param id
	 *            键值
	 * @return 返回指定键值的显示数据
	 */
	public String getDisplayText(String id) {
		CIInfo info = CIManager.getCIById(id);
		if (info == null)
			return id;
		return info.getName();
	}
}
