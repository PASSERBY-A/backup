package com.hp.idc.portal.security;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.portal.security.rule.AllowAllRuleInfo;
import com.hp.idc.portal.security.rule.AllowOrganizationInfo;
import com.hp.idc.portal.security.rule.AllowPersonInfo;
import com.hp.idc.portal.security.rule.AllowWorkgroupInfo;
import com.hp.idc.portal.security.rule.RejectAllRuleInfo;
import com.hp.idc.portal.security.rule.RejectOrganizationInfo;
import com.hp.idc.portal.security.rule.RejectPersonInfo;
import com.hp.idc.portal.security.rule.RejectWorkgroupInfo;
import com.hp.idc.portal.security.rule.RuleInfo;

/**
 * 权限控制管理
 * @author 李会争
 *
 */
public class RuleManager {
	
	/**
	 * 存储规则缓存
	 */
	public static Map<String, List<RuleInfo>> ruleCache = new HashMap<String, List<RuleInfo>>();
	
	/**
	 * 判断是否有权限，ture:有，false:没权限<br>
	 * 规则：写在前面的具有优先权<br>
	 * ap: 允许个人(不在列表的则拒绝,返回-1;列表为空返回0)<Br>
	 * rp: 拒绝个人(不在列表的则允许,返回1;列表为空返回0)<br>
	 * ao: 允许组织(不在列表的则拒绝,返回-1;列表为空返回0)<Br>
	 * ro: 拒绝组织(不在列表的则允许,返回1;列表为空返回0)<br>
	 * ag: 允许工作组(不在列表的则拒绝,返回-1;列表为空返回0)<Br>
	 * rg: 拒绝工作组(不在列表的则允许,返回1;列表为空返回0)<br>
	 * 
	 * al: 允许本地工作组(不在列表的则拒绝,返回-1;列表为空返回0)<Br>
	 * rl: 拒绝本地工作组(不在列表的则允许,返回1;列表为空返回0)<br>
	 * allow: 允许所有<br>
	 * reject: 拒绝所有<br>
	 * @param user 人员
	 * @param rule "ap=1,2;rp=3,4;ao=5,6;ro=7,8;ag=9;rg=10;allow;reject"
	 * 		或者："ap:1,2;rp:3,4;ao:5,6;ro:7,8;ag:9;rg:10;allow;reject"
	 * @param def 如果没做限制，默认是否有权限
	 * @return 返回是否有权限
	 */
	public static boolean valid(String user, String rule, boolean def) {
		PersonInfo pInfo = PersonManager.getPersonById(user);
		if (pInfo == null)
			return def;
		if (pInfo.getId().equalsIgnoreCase("root")){
			return true;
		}
		List<RuleInfo> ruleinfos = analyseRule(rule);
		
		if (ruleinfos.size() == 0)
			return def;

		OrganizationInfo orgInfo = null;
		try {
			orgInfo = OrganizationManager.getOrganizationOfPerson(pInfo.getId()+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String orgId = "";
		if (orgInfo != null)
			orgId = orgInfo.getId();
		List<WorkgroupInfo> wgs = new ArrayList<WorkgroupInfo>();
		try {
			wgs = WorkgroupManager.getWorkgroupsOfPerson(pInfo.getId(), true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String ids[] = new String[wgs.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = wgs.get(i).getId();
		}
		for (int i = 0; i < ruleinfos.size(); i++) {
			RuleInfo rInfo = (RuleInfo)ruleinfos.get(i);
			int ruleRet = rInfo.valid(pInfo.getId(), orgId, ids);
			if (ruleRet != 0) {
				return (ruleRet == 1);
			}
		}
		//如果所有得验证返回都为0，判断默认值；
		return def;
	}
	
	/**
	 * 分析rule
	 * <br>
	 * v1:增加了判断，比如：如果ap=没有值列表的情况，直接跳过
	 * @param rule "ap=1,2;rp=3,4;ao=5,6;ro=7,8"
	 * @return List<RuleInfo>
	 */
	public static List<RuleInfo> analyseRule(String rule) {
		if (rule == null || rule.equals("") || rule.trim().equals(""))
			return new ArrayList<RuleInfo>();
		List<RuleInfo> ruleList = ruleCache.get(rule);
		if (ruleList != null)
			return ruleList;
		ruleList = new ArrayList<RuleInfo>();
		String[] rules = rule.split(";");
		for (int i = 0; i < rules.length; i++) {
			if (rules[i].equals(""))
				continue;
			if (rules[i].equals("allow")) {
				ruleList.add(new AllowAllRuleInfo());
				continue;
			}
			if (rules[i].equals("reject")) {
				ruleList.add(new RejectAllRuleInfo());
				continue;
			}
			int pos = rules[i].indexOf("=");
			if (pos == -1) {
				pos = rules[i].indexOf(":");
				if (pos == -1)
					continue;
			}
			String ruleName = rules[i].substring(0, pos);
			String ruleValue = rules[i].substring(pos + 1);
			if (ruleValue==null || ruleValue.trim().length()==0)
				continue;
			RuleInfo rInfo = null;
			if (ruleName.equalsIgnoreCase("ap")) {//允许人员列表
				rInfo = new AllowPersonInfo();
			} else if (ruleName.equalsIgnoreCase("rp")) {//拒绝人员列表
				rInfo = new RejectPersonInfo();
			} else if (ruleName.equalsIgnoreCase("ao")) {//允许组织列表
				rInfo = new AllowOrganizationInfo();
			} else if (ruleName.equalsIgnoreCase("ro")) {//拒绝组织列表
				rInfo = new RejectOrganizationInfo();
			} else if (ruleName.equalsIgnoreCase("ag")) {//允许工作组列表
				rInfo = new AllowWorkgroupInfo();
			} else if (ruleName.equalsIgnoreCase("rg")) {//拒绝工作组列表
				rInfo = new RejectWorkgroupInfo();
			} else {
				continue;
			}
			int count = rInfo.parse(ruleValue);
			if (count>0)
				ruleList.add(rInfo);
		}
		ruleCache.put(rule, ruleList);
		return ruleList;
	}
	
	
	/**
	 * 把规则字符串格式为:Map['ap':'name1,name2','ro':'o1,o2..']
	 * @param ruleStr
	 * @return
	 */
	public static Map<String, String> parseRuleStrByCategoryToMap(String ruleStr) {
		if (ruleStr==null || ruleStr.length()==0) {
			return null;
		}
		String[] rules = ruleStr.split(";");
		Map<String, String> retMap = new HashMap<String, String>();
		for (int i = 0; i < rules.length; i++) {
			if (rules[i].equals(""))
				continue;
			if (rules[i].equals("allow") || rules[i].equals("reject")) {
				retMap.put(rules[i], "true");
				continue;
			}
			int pos = rules[i].indexOf("=");
			if (pos == -1) {
				pos = rules[i].indexOf(":");
				if (pos==-1)
					continue;
			}
			
			String ruleName = rules[i].substring(0, pos);
			String ruleValue = rules[i].substring(pos + 1);
			retMap.put(ruleName, ruleValue);
		}
		return retMap;
	}
	
	/**
	 * 把规则字符串格式为:List[String['ap','name1,name2'],String['ro','o1,o2..']]
	 * @param ruleStr
	 * @return
	 */
	public static List<String[]> parseRuleStrByCategoryToList(String ruleStr) {
		if (ruleStr==null || ruleStr.length()==0) {
			return null;
		}
		String[] rules = ruleStr.split(";");
		List<String[]> retList = new ArrayList<String[]>();
		for (int i = 0; i < rules.length; i++) {
			String rule[] = new String[2];
			if (rules[i].equals(""))
				continue;
			if (rules[i].equals("allow") || rules[i].equals("reject")) {
				rule[0] = rules[i];
				rule[1] = "true";
				retList.add(rule);
				continue;
			}
			int pos = rules[i].indexOf("=");
			if (pos == -1) {
				pos = rules[i].indexOf(":");
				if (pos==-1)
					continue;
			}
			
			String ruleName = rules[i].substring(0, pos);
			String ruleValue = rules[i].substring(pos + 1);
			rule[0] = ruleName;
			rule[1] = ruleValue;
			retList.add(rule);
		}
		return retList;
	}
		
	/**
	 * 对权限规则进行文字翻译描述
	 * @param rule
	 * @return 文字描述
	 */
	public static String ruleDesc(String rule) {
		String retStr = "";
		List<RuleInfo> ruleList = analyseRule(rule);
		if (ruleList.size()==0)
			return "没做限制";
		for (int i = 0; i < ruleList.size(); i++) {
			IRuleInfo rInfo = (IRuleInfo)ruleList.get(i);
			retStr += rInfo.getDesc()+"<br>";
		}
		return retStr;
	}
}
