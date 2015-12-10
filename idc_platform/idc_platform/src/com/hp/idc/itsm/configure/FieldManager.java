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
 * 08-02-27:Ϊ������OVSD���ĳɽӿڵķ�ʽ,��Ϊ�˼��ݣ�ҳ�������Ķ������ִ���
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * @since 2007-06-05
 */
public class FieldManager{
	
	public static final String ITSM = "ITSM";

	/**
	 * �̳��������["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classInsab = new HashMap();
	
	/**
	 * �̳����map����["ITSM"=TaskManagerInterface..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classIns = new HashMap();

	protected static FieldManagerInterface fmi = null;
	
	public static FieldManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		FieldManagerInterface ret = (FieldManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("�Ҳ���ע�����FieldManagerInterface:"+origin);
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
	 * ���ؾ���ָ��ģ�����Ƶ������ֶ�
	 * @param moduleName  �μ�ModuleName����
	 * @param includeAll �Ƿ����������
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
	 * @param includeAll �Ƿ����������
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
	 * @param origin ����ϵͳ
	 * @param moduleOid
	 * @param includeAll �Ƿ����������
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
	 * ������Ϣ
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
	 * @param origin ϵͳ��ʶ����ϵͳ��"ITSM"
	 * @param oid 
	 * @param clone �Ƿ񷵻�ԭ����Ŀ���������Ϊ��������ȽϺ�ʱ��
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
	 * @param origin ϵͳ��ʶ����ϵͳ��"ITSM"
	 * @param oid 
	 * @param clone �Ƿ񷵻�ԭ����Ŀ���������Ϊ��������ȽϺ�ʱ��
	 * @return
	 */
	public static FieldInfo getFieldById(String origin,String id,boolean clone) {
		fmi = getClassInstance(origin);
		return fmi.getFieldById(id);
	}

	/**
	 * ��ȡ����
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
	 * @param origin ����ϵͳ
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
