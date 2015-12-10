package com.cmsz.cloudplatform;

import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hp.config.dao.DbConfigDao;
import com.hp.config.service.DbConfigManager;

public class BaseTestCase {
	private static ApplicationContext context = null;
	
	public static ApplicationContext getApplicationContext() {
		if (null == context) { 
			context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		}
		return context;
	}

	public static void main(String[] args) throws SQLException {
//		DataSource bean = (DataSource) getApplicationContext().getBean("dataSource");
//		System.out.println(bean);
//		Connection con = bean.getConnection();
//		System.out.println(con);
//		

//		org.springframework.orm.hibernate4.LocalSessionFactoryBean bean2 = (org.springframework.orm.hibernate4.LocalSessionFactoryBean) getApplicationContext().getBean("sessionFactory");
//		System.out.println(bean2);

		DbConfigManager bean1 = (DbConfigManager) getApplicationContext().getBean("dbConfigManager");
		List a = bean1.getAll();
//		DbConfigDao bean2 = (DbConfigDao) getApplicationContext().getBean("dbConfigDao");
//		List a2 = bean2.getAll();
//		System.out.println(bean1.getSe());
		
//		org.hibernate.internal.SessionFactoryImpl b = (org.hibernate.internal.SessionFactoryImpl) getApplicationContext().getBean("sessionFactory");
//		System.out.println(b.getCurrentSession());
//		System.out.println(a);
		
//		test();
	}
	
	public static void test() {
		String str = "{ \"listusexrsresponse\" : { \"count\":1 ,\"user\" : [  {\"id\":\"49160ab4-65b4-11e3-8275-e4115b493a9b\",\"username\":\"admin\",\"firstname\":\"Admin\",\"lastname\":\"User\",\"email\":\"admin@mailprovider.com\",\"created\":\"2013-12-16T10:11:15+0800\",\"state\":\"enabled\",\"account\":\"admin\",\"accounttype\":1,\"domainid\":\"485f3b4d-65b4-11e3-8275-e4115b493a9b\",\"domain\":\"ROOT\",\"apikey\":\"2krDORHjIDE_Ed3DIKsYmA2Snn5WS1BUzylyzyuTgGQHX3_r98VBbThPKr-GGTaIIE4uzEe_JSyawJ4S2QXVJA\",\"secretkey\":\"MyUzEpKVVxoWziZhMsYXYpSpwcA1xRRDP5OmyTjFT2Ct5v8r72zkxguPnnf0-NvDeC3dH0CXUKWHKPsk4jWwXg\",\"accountid\":\"485f9162-65b4-11e3-8275-e4115b493a9b\",\"iscallerchilddomain\":false,\"isdefault\":true} ] } }";
		JSONObject jo = JSONObject.fromObject(str);
		jo = JSONObject.fromObject(jo.getString("listusersresponse"));
		System.out.println(jo.toString());
		JSONArray jos = jo.getJSONArray("user");
		
		System.out.println(jos.toString());
		System.out.println(jos.get(0));
		jo = JSONObject.fromObject(jos.get(0));
		System.out.println(jo.get("apikey"));
//		JSONObject.fromObject(jo.getString("listusersresponse"));
	}
	

}
