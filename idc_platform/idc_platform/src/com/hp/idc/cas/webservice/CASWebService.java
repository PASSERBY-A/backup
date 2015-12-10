package com.hp.idc.cas.webservice;
import java.util.List;

import com.hp.idc.cas.auc.AUCMapping;
import com.hp.idc.cas.auc.AUCMappingInfo;
import com.hp.idc.cas.auc.OrganizationInfo;
import com.hp.idc.cas.auc.OrganizationManager;
import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.cas.auc.WorkgroupInfo;
import com.hp.idc.cas.auc.WorkgroupManager;
import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONObject;


public class CASWebService {

	/**
	 * ������Աid������Ա��Ϣ
	 * @param personId ��Աid ֧�ֶ��� ���ŷָ�
	 * @return json ���鴮<code>[{},{}...]</code><br>
	 * ������ҵ������ص�������ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"������Ա<br>
	 * ���ڲ鲻����Ӧid����Ա����Ӧ�Ķ���ֻ����һ��idΪ{id:��������ID}
	 * @throws Exception 
	 */
	public static String getPersonsJSONStr(String personId){
		if (personId == null || personId.equals(""))
			return "";
		String[] ps = personId.split(",");
		JSONArray ret = new JSONArray();
		try {
			for (int i = 0; i < ps.length; i++) {
				if (ps[i] == null ||  ps[i].equals(""))
					continue;
				PersonInfo pi = PersonManager.getPersonById(ps[i]);
				if (pi != null) {
					ret.put(pi.toJSON());
				} else {
					JSONObject jo = new JSONObject();
					jo.put("id", ps[i]);
					ret.put(jo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ������֯�µ���Ա�б�
	 * @param orgId
	 * @return json���鴮 <code>[{},{}...]</code><br>
	 * ���ص�������ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"������Ա
	 * @throws Exception
	 */
	public static String getPersonsJSONStrOfOrganization(String orgId){
		if (orgId == null ||  orgId.equals(""))
			return "";
		JSONArray ret = new JSONArray();
		try {
			List<PersonInfo> pList = PersonManager.getPersonsOfOrganization(orgId, false);
			for (int i = 0; i < pList.size(); i++) {
				PersonInfo pi = pList.get(i);
				ret.put(pi.toJSON());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ���ع������µ���Ա�б�
	 * @param wogrId
	 * @return json ���鴮 <code>[{},{}...]</code><br>
	 * ���ص�������ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"������Ա
	 * @throws Exception
	 */
	public static String getPersonsJSONStrOfWorkgroup(String wogrId) {
		JSONArray ret = new JSONArray();
		try {
			List<PersonInfo> pList = PersonManager.getPersonsOfWorkgroup(wogrId, false);
			for (int i = 0; i < pList.size(); i++) {
				PersonInfo pi = pList.get(i);
				ret.put(pi.toJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ��ȡ��֯��Ϣ
	 * @param organizationId ��֯id ֧�ֶ�� ���ŷָ�
	 * @return json���鴮 [{},{}...]<br>
	 * ������ҵ������ض���ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"������֯<br>
	 * ����鲻����Ӧ����֯�����Ӧ�ķ��ض���ֻ����һ��id����{id:��������id}
	 * @throws Exception
	 */
	public static String getOrganizationsJSONStr(String organizationId) {
		if (organizationId == null || organizationId.equals(""))
			return "";
		String[] os = organizationId.split(",");
		JSONArray ret = new JSONArray();
		try {
			for (int i = 0; i < os.length; i++) {
				if (os[i] == null ||  os[i].equals(""))
					continue;
				OrganizationInfo oi = OrganizationManager.getOrganizationById(os[i]);
				if (oi != null) {
					ret.put(oi.toJSON());
				} else{
					JSONObject jo = new JSONObject();
					jo.put("id", os[i]);
					ret.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ������Ա����֯
	 * @param personId
	 * @return json ����,����ֻ��һ��json����Ԫ��<br>
	 * ������ҵ������ض���ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"������֯
	 * @throws Exception
	 */
	public static String getOrganizationJSONStrOfPerson(String personId){
		if (personId == null || personId.equals(""))
			return "";
		JSONArray ret = new JSONArray();
		try {
			OrganizationInfo oi = OrganizationManager.getOrganizationOfPerson(personId, false);
			if (oi!=null)
				ret.put(oi.toJSON());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}	
	
	/**
	 * ��ȡ��֯������֯ �б�
	 * @param parentId
	 * @return json���鴮 [{},{}]<br>
	 * ��������ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"������֯
	 * @throws Exception
	 */
	public static String getOrganizationsJSONStrOfParent(String parentId){
		JSONArray ret = new JSONArray();
		try {		
			List<OrganizationInfo> oList = OrganizationManager.getOrganizationByParentId(parentId);
			for (int i = 0; i < oList.size(); i++) {
				OrganizationInfo  oInfo =  oList.get(i);
				ret.put(oInfo.toJSON());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ��ȡ��������Ϣ
	 * @param workgroupId ������id  ֧�ֶ�� ���ŷָ�
	 * @return json ���鴮 [{},{}...]<br>
	 * ������ҵ������ض���ʾ����<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W�������ǹ�����<br>
	 * ����Ҳ�����Ӧ�Ĺ����飬���Ӧ�ķ��ض���ֻ����һ��id����{id:��������id}
	 * @throws Exception
	 */
	public static String getWorkgroupsJSONStr(String workgroupId){
		if (workgroupId == null || workgroupId.equals(""))
			return "";
		String[] ws = workgroupId.split(",");
		JSONArray ret = new JSONArray();
		try  {
			for (int i = 0; i < ws.length; i++) {
				if (ws[i] == null ||  ws[i].equals(""))
					continue;
				WorkgroupInfo oi = WorkgroupManager.getWorkgroupById(ws[i]);
				if (oi != null) {
					ret.put(oi.toJSON());
				} else {
					JSONObject jo = new JSONObject();
					jo.put("id", ws[i]);
					ret.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

	/**
	 * ��ȡ���������ӹ������б�
	 * @param parentId
	 * @return json ���鴮 [{},{}...]
	 * ���ض���ʾ����<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W�������ǹ�����<br>
	 * @throws Exception
	 */
	public static String getWorkgroupsJSONStrOfParent(String parentId) {
		JSONArray ret = new JSONArray();
		try {
			List<WorkgroupInfo> oList = WorkgroupManager.getWorkgroupByParentId(parentId);
			for (int i = 0; i < oList.size(); i++) {
				WorkgroupInfo  oInfo =  oList.get(i);
				ret.put(oInfo.toJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * ������Ա�����Ĺ�����
	 * @param personId
	 * @return json ���鴮 [{},{}...]
	 * ���ض���ʾ����<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W�������ǹ�����<br>
	 * @throws Exception
	 */
	public static String getWorkgroupsJSONStrOfPerson(String personId){
		if (personId == null || personId.equals(""))
			return "";
		JSONArray ret = new JSONArray();
		try  {
			List<WorkgroupInfo> oList = WorkgroupManager.getWorkgroupsOfPerson(personId, false);
			for (int i = 0; i < oList.size(); i++) {
				WorkgroupInfo  oInfo =  oList.get(i);
				ret.put(oInfo.toJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}	

	
	/**
	 * ���ݴ�������id�����Ҷ�Ӧ����Ϣ������ѭ����Ա-��֯-������
	 * @param aucId ����id ֧�ֹ��� ���ŷָ�
	 * @return json ���鴮 [{},{}...]<br>
	 * ���ض���ʾ����<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * <code>AUCType:W</code>�������ǹ�����;<code>AUCType:O</code>��������֯;<code>AUCType:P</code>��������Ա<br>
	 * @throws Exception
	 */
	public static String getAucObjectJSONStr(String aucId){
		if(aucId == null || aucId.equals(""))
			return "";
		String[] as = aucId.split(",");
		JSONArray ret = new JSONArray();
		try {
			for (int i = 0; i < as.length; i++) {
				if (as[i] == null || as[i].equals(""))
					continue;
				CommonInfo ci = PersonManager.getPersonById(as[i]);
				if (ci ==null)
					ci = OrganizationManager.getOrganizationById(as[i]);
				if (ci == null)
					ci = WorkgroupManager.getWorkgroupById(as[i]);
				
				if (ci != null)
					ret.put(ci.toJSON());
				else {
					JSONObject jo = new JSONObject();
					jo.put("id", as[i]);
					ret.put(jo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * �ж���Ա�Ƿ�����֯��
	 * @param personId ��ԱID
	 * @param orgId ��֯ID
	 * @param includeParent �Ƿ�ݹ��жϸ���֯
	 * @return �ڷ���"true" ���ڷ���"false"
	 * @throws Exception 
	 */
	public static String personIsInOrganization(String personId, String orgId ,boolean includeParent){
		if (personId == null || personId.equals(""))
			return "false";
		if (orgId == null || orgId.equals(""))
			return "false";
		try {
			OrganizationInfo oInfo = OrganizationManager.getOrganizationOfPerson(personId, false);
			if (oInfo == null)
				return "false";
			if (oInfo.getId().equals(orgId))
				return "true";
			
			if (includeParent) {
				while (oInfo != null) {
					if (oInfo.getId().equals(orgId))
						return "true";
					oInfo = OrganizationManager.getOrganizationById(oInfo.getParentId());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 * �ж���Ա�Ƿ��ڹ�������
	 * @param personId
	 * @param workId
	 * @return �ڷ���"true" ���ڷ���"false"
	 * @throws Exception
	 */
	public static String personIsInWorkgroup(String personId, String workId){
		if (personId == null || personId.equals(""))
			return "false";
		if (workId == null || workId.equals(""))
			return "false";
		try {
			List<WorkgroupInfo> workList = WorkgroupManager.getWorkgroupsOfPerson(personId, false);
			if (workList == null || workList.size() == 0)
				return "false";
			for (int i = 0; i < workList.size(); i++) {
				WorkgroupInfo workInfo = workList.get(i);
				if (workInfo.getId().equals(workId))
					return "true";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 * ����ӳ��ı����û��б�
	 * @param system ϵͳ��ʶ
	 * @param thirdUserId ������ϵͳ�û�ID
	 * @return json��ʽ�Ĵ�<code>[{},{}...]</code><br>
	 * ��ʽʵ����<code>{"lu":"userId1","ts":"OA","tu":"thirdUserId1"}</code><br>
	 * 	<code>lu:</code>��ϵͳ�û�ID��<br>
	 * 	<code>ts:</code>ϵͳ��ʶ,<br>
	 * 	<code>tu:</code>������ϵͳ�û�ID
	 */
	public static String getMappingUserOfLocal(String system,String thirdUserId){
		List<AUCMappingInfo> userList = AUCMapping.getUserId(system, thirdUserId);
		return userList.toString();
	}
	
	/**
	 * ����ӳ��ĵ�����ϵͳ�û��б�
	 * @param system ϵͳ��ʶ
	 * @param localUserId �����û�ID
	 * @return json��ʽ�Ĵ�<code>[{},{}...]</code><br>
	 * ��ʽʵ����<code>{"lu":"userId1","ts":"OA","tu":"thirdUserId1"}</code><br>
	 * 	<code>lu:</code>��ϵͳ�û�ID��<br>
	 * 	<code>ts:</code>ϵͳ��ʶ,<br>
	 * 	<code>tu:</code>������ϵͳ�û�ID
	 */
	public static String getMappingUserOfRemote(String system,String localUserId){
		List<AUCMappingInfo> userList = AUCMapping.getThirdUserId(localUserId, system);
		return userList.toString();
	}

}
