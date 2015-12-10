package com.hp.idc.itsm.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.hp.idc.itsm.common.Consts;

public class TemplateUtil {

	/**
	 * 工单节点显示模版缓存Map[文件名,org.apache.velocity.Template]
	 */
	public static Map Templates = new HashMap();
	
	/**
	 * 获取一个节点显示模版，如果缓存（this.Templates）有返回缓存的，否则读取文件返回
	 * @param fileName 模版文件名
	 * @return org.apache.velocity.Template对象
	 */
	public static Template getTemplate(String fileName){
		Template t = (Template) Templates.get(fileName);
		if (t != null)
			return t;
		
		String appHome = null;
		try {
			String currentPath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
			appHome = currentPath.substring(0, currentPath.indexOf("idc_platform")-1);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		//String cataHome = System.getProperty("catalina.home");
		//String templatePath = cataHome + "/webapps"+Consts.ITSM_HOME+"/configure/template";
		String templatePath = appHome + Consts.ITSM_HOME+"/configure/template";
		
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatePath);
			ve.init();
			/* next, get the Template */
			t = ve.getTemplate(fileName, "GBK");
			//Templates.put(fileName, t);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 清空模版
	 */
	public static void clearCache(){
		Templates = new HashMap();
	}
	
	public static String getHTMLStrFromTemplate(String temlateName,Map params){
		try {
			Template t = getTemplate(temlateName);
			VelocityContext context = new VelocityContext();
			Object[] keys = params.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				context.put((String) keys[i], params.get(keys[i]));
			}
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
