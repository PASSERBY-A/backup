package com.hp.idc.common.util;

public class StringUtil {
	
	public static String escapeLikeSql(String param,String escape){
		param=param.replaceAll("_", escape+"_");
		param=param.replaceAll("%", escape+"%");
		return param;
	}

}
