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
	 * 根据人员id查找人员信息
	 * @param personId 人员id 支持多人 逗号分隔
	 * @return json 数组串<code>[{},{}...]</code><br>
	 * 如果查找到，返回单个对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"代表人员<br>
	 * 对于查不到对应id的人员，对应的对象串只包含一个id为{id:传进来的ID}
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
	 * 返回组织下的人员列表
	 * @param orgId
	 * @return json数组串 <code>[{},{}...]</code><br>
	 * 返回单个对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"代表人员
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
	 * 返回工作组下的人员列表
	 * @param wogrId
	 * @return json 数组串 <code>[{},{}...]</code><br>
	 * 返回单个对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"P"}</code><br>
	 * AUCType:"P"代表人员
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
	 * 获取组织信息
	 * @param organizationId 组织id 支持多个 逗号分隔
	 * @return json数组串 [{},{}...]<br>
	 * 如果查找到，返回对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"代表组织<br>
	 * 如果查不到对应的组织，则对应的返回对象串只包含一个id属性{id:传进来的id}
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
	 * 返回人员的组织
	 * @param personId
	 * @return json 对象串,数组只有一个json对象元素<br>
	 * 如果查找到，返回对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"代表组织
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
	 * 获取组织下子组织 列表
	 * @param parentId
	 * @return json数组串 [{},{}]<br>
	 * 单个对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"O"}</code><br>
	 * AUCType:"O"代表组织
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
	 * 获取工作组信息
	 * @param workgroupId 工作组id  支持多个 逗号分隔
	 * @return json 数组串 [{},{}...]<br>
	 * 如果查找到，返回对象示例：<code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W，代表是工作组<br>
	 * 如果找不到对应的工作组，则对应的返回对象只包含一个id属性{id:传进来的id}
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
	 * 获取工作组下子工作组列表
	 * @param parentId
	 * @return json 数组串 [{},{}...]
	 * 返回对象示例：<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W，代表是工作组<br>
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
	 * 返回人员所属的工作组
	 * @param personId
	 * @return json 数组串 [{},{}...]
	 * 返回对象示例：<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * AUCType:W，代表是工作组<br>
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
	 * 根据传进来的id，查找对应的信息，查找循序：人员-组织-工作组
	 * @param aucId 对象id 支持过个 逗号分隔
	 * @return json 数组串 [{},{}...]<br>
	 * 返回对象示例：<br><code>{id:"",name:"",oid:"",parentId:"",parentOid:"":status:"",AUCType:"W"}</code><br>
	 * <code>AUCType:W</code>，代表是工作组;<code>AUCType:O</code>，代表组织;<code>AUCType:P</code>，代表人员<br>
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
	 * 判断人员是否在组织里
	 * @param personId 人员ID
	 * @param orgId 组织ID
	 * @param includeParent 是否递归判断父组织
	 * @return 在返回"true" 不在返回"false"
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
	 * 判断人员是否在工作组里
	 * @param personId
	 * @param workId
	 * @return 在返回"true" 不在返回"false"
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
	 * 返回映射的本地用户列表
	 * @param system 系统标识
	 * @param thirdUserId 第三方系统用户ID
	 * @return json格式的串<code>[{},{}...]</code><br>
	 * 格式实例：<code>{"lu":"userId1","ts":"OA","tu":"thirdUserId1"}</code><br>
	 * 	<code>lu:</code>本系统用户ID，<br>
	 * 	<code>ts:</code>系统标识,<br>
	 * 	<code>tu:</code>第三方系统用户ID
	 */
	public static String getMappingUserOfLocal(String system,String thirdUserId){
		List<AUCMappingInfo> userList = AUCMapping.getUserId(system, thirdUserId);
		return userList.toString();
	}
	
	/**
	 * 返回映射的第三方系统用户列表
	 * @param system 系统标识
	 * @param localUserId 本地用户ID
	 * @return json格式的串<code>[{},{}...]</code><br>
	 * 格式实例：<code>{"lu":"userId1","ts":"OA","tu":"thirdUserId1"}</code><br>
	 * 	<code>lu:</code>本系统用户ID，<br>
	 * 	<code>ts:</code>系统标识,<br>
	 * 	<code>tu:</code>第三方系统用户ID
	 */
	public static String getMappingUserOfRemote(String system,String localUserId){
		List<AUCMappingInfo> userList = AUCMapping.getThirdUserId(localUserId, system);
		return userList.toString();
	}

}
