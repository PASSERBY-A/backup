package com.hp.idc.itsm.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.security.OrganizationManager;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.security.WorkgroupManager;

public class MacroManager {

	public static Map MacroList = new HashMap();

	/**
	 * 
	 * @param str Ҫ�滻�ģ�����������ģ��ַ���
	 * @param userId �û�ID
	 * @return
	 */
	public static String replaceMacro(String str, String userId) {
		if (str == null || str.equals(""))
			return str;
		if (str.indexOf("$") == -1)
			return str;
		Set keys = MacroList.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object o = MacroList.get(key);
			String value = "";
			if (o instanceof String)
				value = (String) o;
			else
				continue;
			key = key.replaceAll("\\$", "\\\\\\$");
			str = str.replaceAll(key, value);
		}
		try {
			if (str.indexOf("$") == -1)
				return str;
			if (userId != null){
				str = str.replaceAll("\\$\\{user\\}", userId);
				str = str.replaceAll("\\$USER_ID", userId);
				str = str.replaceAll("\\$\\{USER_ID\\}", userId);
				String userName = PersonManager.getPersonNameById("ITSM", userId);
				userName = userName == null?"":userName;
				str = str.replaceAll("\\$\\{userName\\}", userName);

			}
			
			str = dealDateMacro(str);
			str = dealMethodMacro(str,userId);
			if (userId != null && !userId.equals("")) {

				List wgInfoList = WorkgroupManager.getWorkgroupsByPersonId(
						"ITSM", userId);
				String groupIdPathStr = "";
				String groupIdStr = "";
				if (wgInfoList != null) {
					for (int i = 0; i < wgInfoList.size(); i++) {
						WorkgroupInfoInterface wInfo = (WorkgroupInfoInterface) wgInfoList
								.get(i);
						if (groupIdStr.equals(""))
							groupIdStr = wInfo.getId();
						else
							groupIdStr = groupIdStr + ";" + wInfo.getId();
						String gs_ = "*";
						while (wInfo != null) {
							gs_ = wInfo.getId() + "/" + gs_;
							if (!wInfo.getParentId().equals(""))
								wInfo = WorkgroupManager.getWorkgroupById(
										"ITSM", wInfo.getParentId());
							else if (wInfo.getParentOid() != -1) {
								CIInfo c_ = CIManager.getCIByOid(wInfo
										.getParentOid());
								if (c_ instanceof WorkgroupInfoInterface)
									wInfo = (WorkgroupInfoInterface) c_;
								else
									wInfo = null;

							} else
								wInfo = null;
						}
						if (!gs_.equals("*")) {
							/**
							 * _PATH_��ͷ���涨Ϊ�ǰ�·��ƥ�䣬������ʵ��ͬ���£��е��á�ȡ���У����е�ֻ��ȡ��һ���ӽڵ�
							 */
							gs_ = "_PATH_" + gs_;
							if (groupIdPathStr.equals(""))
								groupIdPathStr = gs_;
							else
								groupIdPathStr = groupIdPathStr + ";" + gs_;
						}
					}
				}
				if (groupIdPathStr.equals(""))// ���û�������Ĺ����飬Ӧ��ȫ�����˵������滻��һ��_����_����ID�����ǲ����ڵģ�
					groupIdPathStr = "_PATH__";
				str = str.replaceAll("\\$USER_WORKGROUP", groupIdPathStr);//��·����
				str = str.replaceAll("\\$\\{USER_WORKGROUP\\}", groupIdStr);//û��·����

				String orgaIdPathStr = "*";
				String orgaIdStr = "";
				OrganizationInfoInterface oInfo = OrganizationManager
						.getOrganizationByPersonId("ITSM", userId);
				if (oInfo!=null)
					orgaIdStr = oInfo.getId();
				while (oInfo != null) {
					orgaIdPathStr = oInfo.getId() + "/" + orgaIdPathStr;
					if (!oInfo.getParentId().equals(""))
						oInfo = OrganizationManager.getOrganizationById("ITSM",
								oInfo.getParentId());
					else if (oInfo.getParentOid() != -1) {
						CIInfo c_ = CIManager.getCIByOid(oInfo.getParentOid());
						if (c_ instanceof OrganizationInfoInterface)
							oInfo = (OrganizationInfoInterface) c_;
						else
							oInfo = null;

					} else
						oInfo = null;
				}
				if (orgaIdPathStr.equals("*"))// ���û�������Ĺ����飬Ӧ��ȫ�����˵������滻��һ��_����_����ID�����ǲ����ڵģ�
					orgaIdPathStr = "_PATH__";
				else
					orgaIdPathStr = "_PATH_" + orgaIdPathStr;
				str = str.replaceAll("\\$USER_ORGANIZATION", orgaIdPathStr);//��·����
				str = str.replaceAll("\\$\\{USER_ORGANIZATION\\}", orgaIdStr);//����·����

				// ��֯|������
				String grIdPathStr_ = orgaIdPathStr;
				
				if (orgaIdPathStr.equals(""))
					grIdPathStr_ = groupIdPathStr;
				else if (groupIdPathStr.equals(""))
					grIdPathStr_ = orgaIdPathStr;
				else
					grIdPathStr_ = orgaIdPathStr + ";" + groupIdPathStr;
				str = str.replaceAll("\\$USER_GROUP", grIdPathStr_);//��·����
				
				String grIdStr_ = orgaIdStr;
				if (orgaIdStr.equals(""))
					grIdStr_ = groupIdStr;
				else if (groupIdStr.equals(""))
					grIdStr_ = orgaIdStr;
				else
					grIdStr_ = orgaIdStr + ";" + groupIdStr;
				
				str = str.replaceAll("\\$\\{USER_GROUP\\}", grIdStr_);//����·����
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String replaceMacro(String str, HttpServletRequest req) {
		if (req == null)
			return replaceMacro(str, "");
		else {
			String userId = (String) req.getSession().getAttribute("USER_ID");
			return replaceMacro(str, userId);
		}
	}
	
	private static String dealDateMacro(String str){
		if (str == null || str.equals(""))
			return "";
		String valClone = str;
		
		//ƥ��${date}-1����${date}������ʽ�Ĵ�
		String regx = "(\\$\\{[a-z]+\\})([-\\+]?)(\\d*)";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(valClone);
		while (matcher.find()) {
			String ret = matcher.group(1);
			String rpValue = "";

			if (ret.equalsIgnoreCase("${date}")||ret.equalsIgnoreCase("${datetime}") ||ret.equalsIgnoreCase("${month}")||ret.equalsIgnoreCase("${year}")) {
				int subValue = 0;
				if (!matcher.group(3).equals(""))
					subValue = Integer.parseInt(matcher.group(3));
				String opS = matcher.group(2);
				
				//��ȡ��ǰʱ�䣬�����${date}-3������ʽ���ڶ�-3��������
				Calendar cal = Calendar.getInstance();
				
				if (!opS.equals("")) {
					int subObject = Calendar.DATE;
					if (ret.equalsIgnoreCase("${month}"))
						subObject = Calendar.MONTH;
					else if (ret.equalsIgnoreCase("${year}"))
						subObject = Calendar.YEAR;
					
					int date = cal.get(subObject);
					if (opS.equals("-"))
						cal.set(subObject, date-subValue);
					if (opS.equals("+"))
						cal.set(subObject, date+subValue);
				}
				SimpleDateFormat sdf = null;
				if (ret.equalsIgnoreCase("${date}"))
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				else if (ret.equalsIgnoreCase("${datetime}"))
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				else if (ret.equalsIgnoreCase("${month}"))
					sdf = new SimpleDateFormat("yyyy-MM-01");
				else if (ret.equalsIgnoreCase("${year}"))
					sdf = new SimpleDateFormat("yyyy-01-01");
				else // default
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					rpValue = sdf.format(cal.getTime());
				} catch (Exception e) {
					return "";
				}
			}
			String matchStr = matcher.group(0);
			matchStr = matchStr.replaceAll("\\$", "\\\\\\$");
			matchStr = matchStr.replaceAll("\\}", "\\\\\\}");
			matchStr = matchStr.replaceAll("\\{", "\\\\\\{");
			matchStr = matchStr.replaceAll("\\+", "\\\\\\+");
			
			valClone = valClone.replaceAll(matchStr, rpValue);
		}
		return valClone;
	}
	
	
	private static String dealMethodMacro(String strin,String userId){
		String str = strin;
		if (str == null || str.equals(""))
			return "";
		
		//�滻������֯��ȡ��Ա�ĺ꺯����
		String regx = "(\\$getPersonsOfOrg\\(\"([a-zA-Z0-9_]+)\"\\))";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		while(m.find()) {
			String orgId = m.group(2);
			String ret1 = m.group(1);
			ret1 = ret1.replaceAll("\\$", "\\\\\\$");
			ret1 = ret1.replaceAll("\\(", "\\\\\\(");
			ret1 = ret1.replaceAll("\\)", "\\\\\\)");
			List pl = PersonManager.getPersonsByOrganizationId("ITSM", orgId, true);
			String pStr = "";
			for (int i = 0; i < pl.size(); i++) {
				PersonInfo pi = (PersonInfo)pl.get(i);
				if (!pStr.equals(""))
					pStr += "|";
				pStr += "/"+pi.getId()+"/";
			}
			
			str = str.replaceAll(ret1, pStr);
		}
		
		//�滻���ݵ�ǰ��¼��������֯��ȡ��Ա�ĺ꺯����
		regx = "(\\$getPersonsOfMyOrg\\(\\))";
		p = Pattern.compile(regx);
		m = p.matcher(str);
		while(m.find()) {
			OrganizationInfoInterface orgInfo = OrganizationManager.getOrganizationByPersonId("ITSM", userId);
			String orgId = "";
			if (orgInfo!=null)
				orgId = orgInfo.getId();
			String ret1 = m.group(1);
			ret1 = ret1.replaceAll("\\$", "\\\\\\$");
			ret1 = ret1.replaceAll("\\(", "\\\\\\(");
			ret1 = ret1.replaceAll("\\)", "\\\\\\)");
			List pl = PersonManager.getPersonsByWorkgoupId("ITSM", orgId, true);
			String pStr = "";
			for (int i = 0; i < pl.size(); i++) {
				PersonInfo pi = (PersonInfo)pl.get(i);
				if (!pStr.equals(""))
					pStr += "|";
				pStr += "/"+pi.getId()+"/";
			}
			str = str.replaceAll(ret1, pStr);
		}
		
		//�滻����ָ���������ȡ��Ա�ĺ꺯����
		regx = "(\\$getPersonsOfWog\\(\"([a-zA-Z0-9_]+)\"\\))";
		p = Pattern.compile(regx);
		m = p.matcher(str);
		while(m.find()) {
			String wogId = m.group(2);
			String ret1 = m.group(1);
			ret1 = ret1.replaceAll("\\$", "\\\\\\$");
			ret1 = ret1.replaceAll("\\(", "\\\\\\(");
			ret1 = ret1.replaceAll("\\)", "\\\\\\)");
			List pl = PersonManager.getPersonsByWorkgoupId("ITSM", wogId, true);
			String pStr = "";
			for (int i = 0; i < pl.size(); i++) {
				PersonInfo pi = (PersonInfo)pl.get(i);
				if (!pStr.equals(""))
					pStr += "|";
				pStr += "/"+pi.getId()+"/";
			}
			
			str = str.replaceAll(ret1, pStr);
		}
		
		
		return str;
	}

	public static Object getMacro(String macro) {
		return MacroList.get(macro);
	}

	public static void setMacro(String macro, Object value) {
		if (macro != null)
			MacroList.put(macro, value);
	}
	
	public static void main(String[] args) {
		String str = "$getPersonsOfWog(\"sdf\")/$getPersonsOfOrg(\"sdf\")";
		//System.out.println(dealMethodMacro(str));
		
		System.out.println(MacroManager.replaceMacro("${user}", ""));
	}

}
