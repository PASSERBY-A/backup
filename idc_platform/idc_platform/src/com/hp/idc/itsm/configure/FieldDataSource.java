package com.hp.idc.itsm.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示字段的数据源
 * 
 * @author 梅园
 * 
 */
public class FieldDataSource {
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
		// 在派生类中重写此函数
		return "";
	}
	
	/**
	 * 
	 * @param filter
	 * @param style 生成数据的样式，列表型"list"，还是树状"tree"
	 * @param idType 生成的node节点的id是取OID，还是取ID<br>
	 * 比如代码配置里，代码有ID，也有OID，此参数就是制定以ID，还是OID，生成节点的ID<br>
	 * 取值："OID","ID"
	 * @return
	 */
	public String getData(String filter, String style, String idType){
		return getData(filter,style);
	}

	/**
	 * 加载数据源的配置信息
	 * 
	 * @param data
	 *            数据源的配置信息
	 */
	public void load(String data) {
		// 在派生类中重写此函数
	}

	/**
	 * 获取满足过滤条件的所有键值
	 * 
	 * @param filter
	 *            过滤表达式
	 * @return 返回满足过滤条件的所有键值 List<String>
	 */
	public List getKeys(String filter) {
		return new ArrayList();
	}

	/**
	 * 获取指定键值的显示数据
	 * @param id 键值
	 * @return 返回指定键值的显示数据
	 */
	public String getDisplayText(String id) {
		// 在派生类中重写此函数
		return id;
	}
	
	public String getIdByText(String text) {
		// 在派生类中重写此函数
		return text;
	}
	/**
	 * 
	 * @param id
	 * @param idType id的类型，取值为ID/OID
	 * @return
	 */
	public String getDisplayText(String id,String idType) {
		return getDisplayText(id);
	}

	/**
	 * 根据类名和配置数据生成FieldDataSource的实例
	 * @param type 类名,如com.hp.idc.itsm.configure.datasource.TextDataSource
	 * @param data 配置数据
	 * @return 返回生成的实例
	 */
	static public FieldDataSource create(String type, String data) {
		FieldDataSource ds = null;
		try {
			Class c = Class.forName(type);
			ds = (FieldDataSource) c.newInstance();
			ds.load(data);
			return ds;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取数据源类型的列表
	 * @return 返回数据源类型的列表Map<String(类名), String(描述)>
	 */
	static public Map getDataSourceTypes() {
		Map m = new HashMap();
		m.put("com.hp.idc.itsm.configure.datasource.TextDataSource",
				"带分隔符的文本数据");
		m.put("com.hp.idc.itsm.configure.datasource.CodeDataSource",
				"代码数据");
		m.put("com.hp.idc.itsm.configure.datasource.CIDataSource",
				"配置项数据");
		// m.put("com.hp.idc.itsm.configure.datasource.CodeTypeDataSource",
		// "代码类别数据");
		return m;
	}

	/**
	 * 获取以分隔符分隔的所有数据源类型列表的字符串
	 * @return 返回以分隔符分隔的所有数据源类型列表的字符串
	 */
	static public String getDataSourceTypeText() {
		Map m = getDataSourceTypes();
		Object objs[] = m.keySet().toArray();
		String ret = "";
		for (int i = 0; i < objs.length; i++) {
			if (i > 0)
				ret += "|";
			ret += objs[i].toString() + ";" + m.get(objs[i]).toString();
		}
		return ret;
	}
}
