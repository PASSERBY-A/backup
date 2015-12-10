package com.hp.idc.itsm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
	
	public final String yyyyMMddHHmmss="yyyyMMddHHmmss";
    public final String yyyyMMdd="yyyyMMdd";
    public final String yyyy_MM_ddHH_mm_ss="yyyy-MM-dd HH:mm:ss";
    public final String yyyy_MM_dd="yyyy-MM-dd";
    
    
    public static String getFormatTime(Date date,String formatStyle) {

    	SimpleDateFormat formatter = new SimpleDateFormat(formatStyle);
    	return formatter.format(date);
    }

    public static Date fromStrToDate(String strDate,String formatStyle) {
    	SimpleDateFormat formatter = new SimpleDateFormat(formatStyle);
    	Date returnDate = null;
    	try {
    		returnDate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
    }
    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
    }
    
}
