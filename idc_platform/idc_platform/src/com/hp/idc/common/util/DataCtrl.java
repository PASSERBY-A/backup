package com.hp.idc.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.hibernate.SQLQuery;

public class DataCtrl {
	public static void main(String[] args) {
		DataCtrl util = new DataCtrl();
		float a = 0;
		System.out.print(a);
	}

	public Date findSystemDate() {
		Date sysdate = null;
		try {

			// 获取系统时间
			java.text.SimpleDateFormat date = new java.text.SimpleDateFormat(
					"yyyyMMdd");

			String sysstr = date.format(new Date(System.currentTimeMillis()));

			sysdate = date.parse(sysstr);
			System.out.print(sysdate.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sysdate;
	}
}
