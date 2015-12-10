package com.hp.idc.itsm.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.PersonManagerInterface;
import com.hp.idc.json.JSONArray;

/**
 * ��Ա��Ϣ�Ĺ�����
 * @author ÷԰
 *
 */
public class PersonManager{

	/**
	 * �̳��������["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map<String, String> classInsab = new HashMap<String, String>();
	
	/**
	 * �̳����map����["ITSM"=TaskManagerInterface..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map<String, Object> classIns = new HashMap<String, Object>();
	
	protected static PersonManagerInterface pmi = null;

	private static PersonManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		PersonManagerInterface ret = (PersonManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("�Ҳ���ע�����PersonManagerInterface:"+origin);
		return ret;
	}
	/**
	 * ����id��ȡ��Ա��Ϣ
	 * @param id ��Աid
	 * @return �����ҵ�����Ա����
	 */
	public static PersonInfo getPersonById(String id) {
		PersonManagerInterface pm = (PersonManagerInterface)classIns.get("ITSM");
		PersonInfoInterface pif = pm.getPersonById(id);
		if (pif !=null)
			return (PersonInfo)pm.getPersonById(id);
		else
			return null;
	}
	
	public static PersonInfoInterface getPersonById(String origin,String id) {
		pmi = getClassInstance(origin);
		return pmi.getPersonById(id);
	}
	/**
	 * ����id��ȡ��Ա����
	 * @param id ��Աid
	 * @return �����ҵ�����Ա����,�Ҳ����ķ���ԭid����
	 */
	public static String getPersonNameById(String id) {
		PersonInfoInterface info = getPersonById(id);
		if (info == null)
			return id;
		return info.getName();
	}
	
	public static String getPersonNameById(String origin,String id) {
		PersonInfoInterface info = getPersonById(origin,id);
		if (info == null)
			return id;
		return info.getName();
	}
	
	/**
	 * ����sql��ѯ��Ա��Ϣ
	 * @param sql
	 * @return ����sql�Ľ����������Ӧ��JSON����
	 */
	public static JSONArray getPersonsBySQL(String sql) {
		PersonManagerInterface pm = (PersonManagerInterface)classIns.get("ITSM");
		return pm.getPersonsBySQL(sql);
	}
	public static JSONArray getPersonsBySQL(String origin,String sql){
		pmi = getClassInstance(origin);
		return pmi.getPersonsBySQL(sql);
	}
	
	/**
	 * ��ȡ������Ա��Ϣ
	 * @return ����������Ա��ϢList<PersonInfo>
	 */
	public static List getAllPersons() {
		PersonManagerInterface pm = (PersonManagerInterface)classIns.get("ITSM");
		return pm.getAllPersons();
	}
	
	public static List getAllPersons(String origin) {
		pmi = getClassInstance(origin);
		return pmi.getAllPersons();
	}
	
	public static List getPersonsByOrganizationId(String origin,String organizationId,boolean includeChildren){
		pmi = getClassInstance(origin);
		return pmi.getPersonsByOrganizationId(organizationId, includeChildren);
	}
	
	public static List getPersonsByWorkgoupId(String origin,String workgroupId,boolean includeChildren){
		pmi = getClassInstance(origin);
		return pmi.getPersonsByWorkgoupId(workgroupId, includeChildren);
	}

	
	/**
	 * ��������
	 * @param userId �û�id
	 * @param newPasswd ������
	 * @return �ɹ�ʱ����null, ʧ�ܷ��ش�����Ϣ
	 */
	public static String resetPassword(String userId, 
			String newPasswd) {
		pmi = getClassInstance("ITSM");
		return pmi.resetPassword(userId, newPasswd);
	}
	
	public static String resetPassword(String origin,String userId, 
			String newPasswd) {
		pmi = getClassInstance(origin);
		return pmi.resetPassword(userId, newPasswd);
	}

	/**
	 * �����û�����
	 * @param userId �û�����
	 * @param oldPasswd ԭ����
	 * @param newPasswd ������
	 * @return �ɹ�ʱ����null, ʧ�ܷ��ش�����Ϣ
	 */
	public static String changePassword(String userId, String oldPasswd,
			String newPasswd) {
		pmi = getClassInstance("ITSM");
		return pmi.changePassword(userId, oldPasswd, newPasswd);
	}
	
	public static String changePassword(String origin,String userId, String oldPasswd,
			String newPasswd) {
		pmi = getClassInstance(origin);
		return pmi.changePassword(userId, oldPasswd, newPasswd);
	}

	/**
	 * �û���¼
	 * @param userId �û�id
	 * @param passwd ����
	 * @return �ɹ�����null, ʧ�ܷ��ش�����Ϣ
	 */
	public static String login(String userId, String passwd) {
		pmi = getClassInstance("ITSM");
		return pmi.login(userId, passwd);
	}
	public static String login(String origin,String userId, String passwd) {
		pmi = getClassInstance(origin);
		return pmi.login(userId, passwd);
	}
	

	public static String getLocalId(String origin,String id){
		pmi = getClassInstance(origin);
		return pmi.getLocalId(id);
	}
	
	public static String getRemoteId(String origin,String id){
		pmi = getClassInstance(origin);
		return pmi.getRemoteId(id);
	}
	
	
	/**
	 * �������������ñ����ؽ�����
	 * @throws SQLException
	 */
	public static void loadFactors() throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_FACTOR";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				FactorInfo fi = new FactorInfo();
				fi.setOperName(rs.getString("operName"));
				fi.setFactors(rs.getString("factor"));
				fi.setSendSMS(rs.getInt("sendsms")==1);
				Cache.Factors.put(fi.getOperName(),fi);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	/**
	 * ���¼��d������
	 * @param userId �ˆTID
	 * @throws SQLException
	 */
	public static void reloadFactor(String userId) throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_FACTOR where OPERNAME=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				FactorInfo fi = new FactorInfo();
				fi.setOperName(rs.getString("operName"));
				fi.setFactors(rs.getString("factor"));
				fi.setSendSMS(rs.getInt("sendsms")==1);
				Cache.Factors.put(fi.getOperName(),fi);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	/**
	 * ���ù��������ˣ���������˴����ˣ����˵����й������ɴ����˴���
	 * @param operName �����ˣ���������
	 * @param factor ������
	 * @throws Exception 
	 */
	public static void setFactor(String operName,String factor,int sendSMS) throws Exception{

		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_FACTOR", operName);
		boolean isNew = false;
		if (Cache.Factors.get(operName)==null)
			isNew = true;
		
		try {
			if (isNew)
				u.setColumnData("operName", new ColumnData(ColumnData.TYPE_STRING,
					operName));
			u.setColumnData("factor", new ColumnData(ColumnData.TYPE_STRING,
					factor));
			u.setColumnData("SENDSMS", new ColumnData(ColumnData.TYPE_INTEGER,
					new Integer(sendSMS)));
			
			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("operName=?");
				ps.setString(1, operName);
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		FactorInfo fi = new FactorInfo();
		fi.setOperName(operName);
		fi.setFactors(factor);
		fi.setSendSMS(sendSMS==1);
		Cache.Factors.put(operName,fi);

		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("FACTOR", operName);
	}
	
	/**
	 * ȡ��¼�˵Ĵ�����
	 * @param operName ��¼��ID
	 * @return
	 */
	public static String getFactor(String operName){
		FactorInfo fi = Cache.Factors.get(operName);
		String ret = "";
		if (fi!=null) {
			String os = fi.getFactors();
			if (os!=null && !os.equals("")){
				String[] factors = os.split(",");
				for (int i = 0; i < factors.length; i++){
					if (factors[i]==null || factors.equals(""))
						continue;
					int pos = factors[i].lastIndexOf("/");
					ret += "/"+factors[i].substring(pos+1);
				}
			}
			
			if (!ret.equals(""))
				ret += "/";
		}
		return ret;
	}
	
	/**
	 * �жϵ�¼���ǲ��ǹ��������˵Ĵ�����
	 * @param operName ����������
	 * @param userId ��¼��
	 * @return
	 */
	public static boolean isFactor(String operName,String userId){
		String factors = getFactor(operName);
		if (!factors.equals("") && factors.indexOf("/"+userId+"/")!=-1)
			return true;
		
		return false;
	}
	
}
