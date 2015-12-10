package com.hp.idc.itsm.security.rule;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.security.IRuleInfo;

/**
 * 基于id的权限控制类
 * 
 * @author 梅园
 * 
 */
public class RuleInfo implements IRuleInfo {

	/**
	 * 存储相关的ID列表
	 */
	protected String[] ids;

	/**
	 * 存储ID列表个数
	 */
	protected int count = 0;

	/**
	 * 存储“允许”或“拒绝”<br>
	 * 在派生类中修改
	 */
	protected String desc = "";

	/**
	 * 检查id是否在列表中
	 * @param id 指定的id
	 * @return 返回id是否在列表中
	 */
	public boolean checkId(String id) {
		for (int i = 0; i < this.count; i++) {
			if (this.ids[i].equalsIgnoreCase(id))
				return true;
		}
		return false;
	}

	/**
	 * 对id进行分析，生成id列表
	 * v1:返回值由void更改为int,用于判断id的序列中规则有几个是合法(int型)的。
	 * 	  如果id序列都是非法的,则默认为此序列"为空"
	 * 
	 * @param id
	 *            以“,”分隔的ID列表
	 * @return 返回id的数量
	 */
	public int parse(String id) {
		String strIds[] = id.split(",");
		this.count = 0;
		this.ids = new String[strIds.length];
		for (int i = 0; i < strIds.length; i++) {
			if (strIds[i]==null || strIds[i].trim().length() == 0)
				continue;
			this.ids[this.count++] = strIds[i];
		}
		return this.count;
	}

	/**
	 * 获取规则的描述
	 * 
	 * @return 返回规则的描述
	 */
	public String getDesc() {
		
		String str = this.desc+" : ";
		if (this.count == 0)
			str += "没有限制";
		else {
			for (int i = 0; i < this.count; i++) {
				CIInfo info = CIManager.getCIById(this.ids[i]);
				String name = (info == null) ? "?" : info.getName();
				if (i > 0)
					str += ",";
				str +=  name;
			}
		}
		return str;
	}

	/**
	 * 返回是否允许操作
	 * 
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:表示没做限制；1:表示允许；-1:表示拒绝；
	 */
	public int valid(String user, String org, String[] group) {
		return 0;
	}

}
