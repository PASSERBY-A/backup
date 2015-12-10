package com.hp.idc.itsm.configure.datasource;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.CodeInfo;
import com.hp.idc.itsm.common.TreeObject;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 表示指定代码类型的所有代码的数据源
 * 
 * @author 梅园
 * 
 */
public class CodeDataSource extends FieldDataSource {
	/**
	 * 存储代码类型
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
		return getData(filter,style,"ID");
	}
	
	public String getData(String filter, String style, String idType){
		StringBuffer sb = new StringBuffer();
		List list = getKeys(filter);
		if (style.equals("list")) {
			for (int i = 0; i < list.size(); i++) {
				CodeInfo info = CIManager.getCodeByOid(Integer
						.parseInt((String) list.get(i)));
				if (i > 0)
					sb.append(",");
				sb.append("[");
				if (idType != null && idType.equals("ID"))
					sb.append("\"" + info.getCodeId() + "\",\"" + info.getName()
						+ "\",\""+StringUtil.escapeJavaScript(info.getAlertMsg())+"\"");
				else
					sb.append("\"" + info.getOid() + "\",\"" + info.getName()
							+ "\",\""+StringUtil.escapeJavaScript(info.getAlertMsg())+"\"");
				sb.append("]");
			}
		} else /* if (style.equals("list")) */{
			for (int i = 0, count = 0; i < list.size(); i++) {
				CodeInfo info = CIManager.getCodeByOid(Integer
						.parseInt((String) list.get(i)));
				if (info.getParent() != null)
					continue;
				if (count++ > 0)
					sb.append(",");
				sb.append(info.getData(idType));
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
		List list = CIManager.getCodesByTypeOid(this.type,false);
		List l2 = new ArrayList();
		getKeys2(list, l2);
		//ItsmUtil.filter(l2, "name", filter);
		List l3 = new ArrayList();
		for (int i = 0; i < l2.size(); i++) {
			l3.add("" + ((TreeObject) (l2.get(i))).getOid());
		}
		return l3;
	}

	/**
	 * 递归查找所有代码
	 * 
	 * @param list
	 *            代码列表
	 * @param ret
	 *            结果
	 */
	protected void getKeys2(List list, List ret) {
		for (int i = 0; i < list.size(); i++) {
			TreeObject t = (TreeObject) list.get(i);
			ret.add(t);
			getKeys2(t.getSubItems(), ret);
		}
	}

	/**
	 * 获取指定键值的显示数据
	 * 
	 * @param id
	 *            键值
	 * @return 返回指定键值的显示数据
	 */
	public String getDisplayText(String id) {
		return getDisplayText(id,"ID");
	}
	
	public String getDisplayText(String id,String idType) {
		CodeInfo info = null;
		if (idType.equals("ID")) {
			info = CIManager.getCodeById(id,this.type);
		} else {
			int oid = StringUtil.parseInt(id, -1);
			info = CIManager.getCodeByOid(oid);
		}
		if (info == null)
			return id;
		return info.getDisplayName();
	}
	
	
	public String getIdByText(String text){
		CodeInfo ci= CIManager.getCodeByText(text,this.type);
		if (ci != null) {
			return ci.getCodeId();
		}
		return "";
	}
	
}
