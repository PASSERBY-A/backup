package com.hp.idc.itsm.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.CodeInfo;
import com.hp.idc.itsm.common.Consts;

/** 
 * 模块名称
 * @author Administrator
 * 
 */
public class ModuleName {
	
	/**
	 * 事件
	 */ 
	public static int INCIDENT = 7;
	
	/**
	 * 问题
	 */
	public static int PROBLEM = 8;
	
	/**
	 * 变更
	 */
	public static int CHANGE = 9;
	
	/**
	 * 配置
	 */
	public static int CONFIGURE = 10;
	
	/**
	 * 需求
	 */
	public static int REQUIRE = 11;

	/**
	 * 其它
	 */
	public static int OTHER = 12;

	/**
	 * 所有
	 */
	public static int ALL = 6;
	
	private static Map modulesM = null;
	
	public static int moduleName2Oid(String name) {
		if (name.equals("all"))
			return ALL;
		if (name.equals("inc"))
			return INCIDENT;
		if (name.equals("prb"))
			return PROBLEM;
		if (name.equals("chg"))
			return CHANGE;
		if (name.equals("cfg"))
			return CONFIGURE;
		if (name.equals("req"))
			return REQUIRE;
		int n = ALL;
		try {
			n = Integer.parseInt(name);
		}
		catch (Exception e) {
		}
		return n;
	}
	
	/**
	 * 验证ModuleName是不是正确
	 * @param ModuleName
	 * @return
	 */
	public static boolean moduleNameIsRight(String name) {
		return (getModuleName(name) != null);
	}
	
	/**
	 * 获取模块列表
	 * @return
	 */
	public static Map getModulesM() {
		//if (modulesM != null)
		//	return modulesM;
		List list = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			CodeInfo info = (CodeInfo)list.get(i);
			map.put(new Integer(info.getOid()), info.getName());
			for (int j = 0; j < info.getSubItems().size(); j++)
				list.add(info.getSubItems().get(j));
		}
		modulesM = map;
		return modulesM;
	}
	
	/**
	 * 获取模块列表
	 * @return List[String{id,name},......]
	 */
	public static List getModulesL() {
		List returnList = new ArrayList();
		List list = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
		for (int i = 0; i < list.size(); i++) {
			CodeInfo info = (CodeInfo)list.get(i);
			returnList.add(new String[] 
			     { "" + info.getOid(), info.getName()});
			for (int j = 0; j < info.getSubItems().size(); j++)
				list.add(info.getSubItems().get(j));
		}
		return returnList;
	}
	
	/**
	 * 根据模块ID，获取名称
	 * @param id
	 * @return
	 */
	public static String getModuleName(String id) {
		String[] ids = id.split(",");
		String ret = "";
		Map map = getModulesM();
		for (int i = 0; i < ids.length; i++) {
			int oid = moduleName2Oid(ids[i]);
			String str = (String)map.get(new Integer(oid));
			if (str != null && str.length() > 0) {
				if (ret.length() > 0)
					ret += ",";
				ret += str;
			}
		}
		return ret;
	}

	public static String getModuleName(int oid) {
		Map map = getModulesM();
		String str = (String)map.get(new Integer(oid));
		if (str == null)
			str = "";
		return str;
	}
}
