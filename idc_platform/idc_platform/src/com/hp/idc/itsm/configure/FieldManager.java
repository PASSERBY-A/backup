package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.inter.FieldManagerInterface;

/**
 * 08-02-27:为了整合OVSD，改成接口的方式,又为了兼容，页面少量改动，保持此类
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * @since 2007-06-05
 */
public class FieldManager{
	
	public static final String ITSM = "ITSM";

	/**
	 * 继承类的类名["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	public static Map classInsab = new HashMap();
	
	/**
	 * 继承类的map数组["ITSM"=TaskManagerInterface..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	public static Map classIns = new HashMap();

	protected static FieldManagerInterface fmi = null;
	
	public static FieldManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		FieldManagerInterface ret = (FieldManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("找不到注册的类FieldManagerInterface:"+origin);
		return ret;
	}
	
	public static void initCache(){
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			try {
				fmi = getClassInstance((String)keys[i]);
				fmi.initCache();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void initCache(String origin){
		try {
			fmi = getClassInstance(origin);
			fmi.initCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getTypeDesc(String origin,String type) {
		fmi = getClassInstance(origin);
		return fmi.getTypeDesc(type);
	}

	public static Map getFieldTypes() {
		Map retMap = new HashMap();
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			fmi = getClassInstance((String)keys[i]);
			try {
				Map m_ = fmi.getFieldTypes();
				if (m_!=null && m_.size()>0)
					retMap.putAll(m_);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retMap;
	}
	
	public static Map getFieldTypes(String origin) {
		fmi = getClassInstance(origin);
		return fmi.getFieldTypes();
	}
	
	/**
	 * 返回具有指定模块名称的所有字段
	 * @param moduleName  参见ModuleName定义
	 * @param includeAll 是否包含公共的
	 * @return List<FieldInfo>
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getFieldsOfModule(String moduleName,boolean includeAll) throws SQLException,IOException {
		return getFieldsOfModule(ModuleName.moduleName2Oid(moduleName),includeAll);
	}

	
	/**
	 * 
	 * @param moduleOid
	 * @param includeAll 是否包含公共的
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getFieldsOfModule(int moduleOid,boolean includeAll) throws SQLException,IOException {
		List retList = new ArrayList();
		
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			fmi = getClassInstance((String)keys[i]);
			try {
				retList.addAll(fmi.getFieldsOfModule(moduleOid,includeAll));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return retList;
	}
	
	/**
	 * 
	 * @param origin 工单系统
	 * @param moduleOid
	 * @param includeAll 是否包含公共的
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getFieldsOfModule(String origin, int moduleOid,boolean includeAll) throws SQLException,IOException {
		List retList = new ArrayList();
		try {
			fmi = getClassInstance(origin);
			retList = fmi.getFieldsOfModule(moduleOid, includeAll);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}

	synchronized public static OperationResult updateField(Map map, String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		String origin = (String)map.get("fld_origin");
		try {
			fmi = getClassInstance(origin);
			ret = fmi.updateField(map, operName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 单条信息
	 * @param oid
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @deprecated
	 */
	public static FieldInfo getFieldByOid(String origin,int oid) {
		fmi = getClassInstance(origin);
		return fmi.getFieldByOid(oid);

	}

	/**
	 * @deprecated
	 */
	public static FieldInfo getFieldByOid(int oid) {
		fmi = getClassInstance(ITSM);
		return fmi.getFieldByOid(oid);
	}
	
	/**
	 * 
	 * @param origin 系统标识，本系统传"ITSM"
	 * @param oid 
	 * @param clone 是否返回原对象的拷贝，（因为拷贝对象比较耗时）
	 * @return
	 */
	public static FieldInfo getFieldByOid(String origin,int oid,boolean clone) {
		fmi = getClassInstance(origin);
		return fmi.getFieldByOid(oid,clone);

	}
	/**
	 * @deprecated
	 */
	public static FieldInfo getFieldById(String origin,String id) {
		fmi = getClassInstance(origin);
		return fmi.getFieldById(id);
	}
	/**
	 * @deprecated
	 * @param id
	 * @return
	 */
	public static FieldInfo getFieldById(String id) {
		fmi = getClassInstance(ITSM);
		return fmi.getFieldById(id);
	}
	
	/**
	 * 
	 * @param origin 系统标识，本系统传"ITSM"
	 * @param oid 
	 * @param clone 是否返回原对象的拷贝，（因为拷贝对象比较耗时）
	 * @return
	 */
	public static FieldInfo getFieldById(String origin,String id,boolean clone) {
		fmi = getClassInstance(origin);
		return fmi.getFieldById(id);
	}

	/**
	 * 获取所有
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getFields() throws SQLException, IOException {
		List retList = new ArrayList();
		Object[] keys = classIns.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			try {
				fmi = getClassInstance((String)keys[i]);
				retList.addAll(fmi.getFields());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;
	}
	
	/**
	 * 
	 * @param origin 工单系统
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getFields(String origin) throws SQLException, IOException {
		List retList = new ArrayList();
		try {
			fmi = getClassInstance(origin);
			retList = fmi.getFields();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
}
