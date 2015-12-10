/*
 * @(#)JsqlBuildByMap.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common.core;

import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 7:50:54 PM Jul 21, 2011
 * 
 */

public class JsqlBuildByMap {

	public static String parseMap(String entity,Map<String, Object> paramMap){
		
		StringBuffer sb = new StringBuffer("select o from ").append(entity).append(" o ");
		if (paramMap != null && paramMap.size() > 0) {
			sb.append(" where ");
			for (String field : paramMap.keySet()) {
				sb.append(field + " = :" + field);
			}
		}
		return sb.toString();
	}
	
	public static String parseMap(String entity,Map<String, Object> paramMap, Map<String, String> exp){
		if (exp == null) {
			return parseMap(entity, paramMap);
		}
		StringBuffer sb = new StringBuffer("select o from ").append(entity).append(" o ");
		if (paramMap != null && paramMap.size() > 0) {
			sb.append(" where 1=1 ");
			for (String field : paramMap.keySet()) {
				sb.append(" and " +field + " "+(exp.get(field) == null?"=":exp.get(field))+" :" + field);
			}
		}
		return sb.toString();
	}
	
	public static String parseMap(String sql, String pk){
		return sql.replace("select o", "select count(o."+pk+")");
	}
}
