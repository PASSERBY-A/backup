package com.hp.idc.itsm.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示操作符
 * @author 梅园
 *
 */
public class OperationCode {
	/**
	 * 未知
	 */
	public static int UNKNOW = -1;
	
	/**
	 * 等于
	 */
	public static int EQUAL = 0;
	
	/**
	 * 不等于
	 */
	public static int NOT_EQUAL = 1;
	
	/**
	 * 小于
	 */
	public static int LESS_THAN = 2;
	
	/**
	 * 大于
	 */
	public static int GREATE_THAN = 3;
	
	/**
	 * 小于等于
	 */
	public static int LESS_OR_EQUAL = 4;
	
	/**
	 * 大于等于
	 */
	public static int GREATE_OR_EQUAL = 5;
	
	/**
	 * 包含
	 */
	public static int INCLUDE = 6;
	
	/**
	 * 不包含
	 */
	public static int NOT_INCLUDE = 7;

	/**
	 * 匹配
	 */
	public static int MATCH = 8;

	/**
	 * 不匹配
	 */
	public static int NOT_MATCH = 9;
	
	/**
	 * 为空
	 */
	public static int NULL = 10;
	
	/**
	 * 不为空
	 */
	public static int NOT_NULL = 11;
	
	/**
	 * 介于
	 */
	public static int BETWEEN = 12;
	
	/**
	 * 本周内
	 */
	public static int CURRENT_WEEK=13;
	
	/**
	 * 本月内
	 */
	public static int CURRENT_MONTH=14;
	
	/**
	 * 本年内
	 */
	public static int CURRENT_YEAR=15;
	
	/**
	 * 开始于
	 */
	public static int STRING_BEGIN=16;
	
	/**
	 * 结束于
	 */
	public static int STRING_END=17;

	/**
	 * 分析操作符描述并返回相应代码
	 * @param str 操作符描述
	 * @return 返回相应代码
	 */
	public static int parse(String str) {
		if (str == null || str.length() == 0)
			return UNKNOW;
		else if (str.equals("等于"))
			return EQUAL;
		else if (str.equals("不等于"))
			return NOT_EQUAL;
		else if (str.equals("小于"))
			return LESS_THAN;
		else if (str.equals("大于"))
			return GREATE_THAN;
		else if (str.equals("小于等于") || str.equals("不大于"))
			return LESS_OR_EQUAL;
		else if (str.equals("大于等于") || str.equals("不小于"))
			return GREATE_OR_EQUAL;
		else if (str.equals("包含"))
			return INCLUDE;
		else if (str.equals("不包含"))
			return NOT_INCLUDE;
		else if (str.equals("匹配"))
			return MATCH;
		else if (str.equals("不匹配"))
			return NOT_MATCH;
		else if (str.equals("为空"))
			return NULL;
		else if (str.equals("不为空") || str.equals("非空"))
			return NOT_NULL;
		else if (str.equals("介于"))
			return BETWEEN;
		else if (str.equals("本周内"))
			return CURRENT_WEEK;
		else if (str.equals("本月内"))
			return CURRENT_MONTH;
		else if (str.equals("本年内"))
			return CURRENT_YEAR;
		else if (str.equals("开始于"))
			return STRING_BEGIN;
		else if (str.equals("结束于"))
			return STRING_END;
		return UNKNOW;
	}
	
	/**
	 * 获取操作符列表
	 * @return 返回操作符列表List<String>
	 */
	public static List getOperations(){
		List ret = new ArrayList();
		ret.add("等于");
		ret.add("不等于");
		ret.add("小于");
		ret.add("大于");
		ret.add("不大于");
		ret.add("不小于");
		ret.add("包含");
		ret.add("不包含");
		ret.add("匹配");
		ret.add("不匹配");
		ret.add("为空");
		ret.add("不为空");
		ret.add("介于");
		ret.add("本周内");
		ret.add("本月内");
		ret.add("本年内");
		ret.add("开始于");
		ret.add("结束于");
		
		return ret;
	}
}
