package com.hp.idc.itsm.util;

public class TemplateManager {

	public static Object createInstance(String className){
		try {
			Class c = Class.forName(className);
			if (c!=null)
				return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
