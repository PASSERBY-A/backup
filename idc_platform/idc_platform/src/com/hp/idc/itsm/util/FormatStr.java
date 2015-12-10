package com.hp.idc.itsm.util;

public class FormatStr {

	public static String getStr(String str){  
		try{
			String temp_p=str;
			byte[] temp_t=temp_p.getBytes("ISO8859-1");
			String temp=new String(temp_t);
			return temp;
		}
		catch(Exception e){}
			return "NULL";
	} 
  
}
