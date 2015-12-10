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
 * Ȩ�޿��ƹ���
 * @author �����
 *
 */
public class RuleManager {
	
	/**
	 * �洢���򻺴�
	 */
	public static Map<String, List<RuleInfo>> ruleCache = new HashMap<String, List<RuleInfo>>();
	
	/**
	 * �ж��Ƿ���Ȩ�ޣ�ture:�У�false:ûȨ��<br>
	 * ����д��ǰ��ľ�������Ȩ<br>
	 * ap: �������(�����б����ܾ�,����-1;�б�Ϊ�շ���0)<Br>
	 * rp: �ܾ�����(�����б��������,����1;�б�Ϊ�շ���0)<br>
	 * ao: ������֯(�����б����ܾ�,����-1;�б�Ϊ�շ���0)<Br>
	 * ro: �ܾ���֯(�����б��������,����1;�б�Ϊ�շ���0)<br>
	 * ag: ��������(�����б����ܾ�,����-1;�б�Ϊ�շ���0)<Br>
	 * rg: �ܾ�������(�����б��������,����1;�б�Ϊ�շ���0)<br>
	 * 
	 * al: �����ع�����(�����б����ܾ�,����-1;�б�Ϊ�շ���0)<Br>
	 * rl: �ܾ����ع�����(�����б��������,����1;�б�Ϊ�շ���0)<br>
	 * allow: ��������<br>
	 * reject: �ܾ�����<br>
	 * @param user ��Ա
	 * @param rule "ap=1,2;rp=3,4;ao=5,6;ro=7,8;ag=9;rg=10;allow;reject"
	 * 		���ߣ�"ap:1,2;rp:3,4;ao:5,6;ro:7,8;ag:9;rg:10;allow;reject"
	 * @param def ���û�����ƣ�Ĭ���Ƿ���Ȩ��
	 * @return �����Ƿ���Ȩ��
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
		//������е���֤���ض�Ϊ0���ж�Ĭ��ֵ��
		return def;
	}
	
	/**
	 * ����rule
	 * <br>
	 * v1:�������жϣ����磺���ap=û��ֵ�б�������ֱ������
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
			if (ruleName.equalsIgnoreCase("ap")) {//������Ա�б�
				rInfo = new AllowPersonInfo();
			} else if (ruleName.equalsIgnoreCase("rp")) {//�ܾ���Ա�б�
				rInfo = new RejectPersonInfo();
			} else if (ruleName.equalsIgnoreCase("ao")) {//������֯�б�
				rInfo = new AllowOrganizationInfo();
			} else if (ruleName.equalsIgnoreCase("ro")) {//�ܾ���֯�б�
				rInfo = new RejectOrganizationInfo();
			} else if (ruleName.equalsIgnoreCase("ag")) {//���������б�
				rInfo = new AllowWorkgroupInfo();
			} else if (ruleName.equalsIgnoreCase("rg")) {//�ܾ��������б�
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
	 * �ѹ����ַ�����ʽΪ:Map['ap':'name1,name2','ro':'o1,o2..']
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
	 * �ѹ����ַ�����ʽΪ:List[String['ap','name1,name2'],String['ro','o1,o2..']]
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
	 * ��Ȩ�޹���������ַ�������
	 * @param rule
	 * @return ��������
	 */
	public static String ruleDesc(String rule) {
		String retStr = "";
		List<RuleInfo> ruleList = analyseRule(rule);
		if (ruleList.size()==0)
			return "û������";
		for (int i = 0; i < ruleList.size(); i++) {
			IRuleInfo rInfo = (IRuleInfo)ruleList.get(i);
			retStr += rInfo.getDesc()+"<br>";
		}
		return retStr;
	}
}
