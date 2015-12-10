/**
 * 
 */
package com.hp.idc.resm.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.RemoteResourceService;
import com.hp.idc.resm.service.RemoteResourceUpdateService;
import com.hp.idc.resm.util.ResmConfig;
import com.hp.idc.resm.util.StringUtil;
import com.hp.idc.resm.util.UniqueIndexedList;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelCacheTest {
	/**
	 * test
	 * @param args test
	 * @throws Exception test
	 */
	public static void main1(String[] args) throws Exception {
		BeanInfo info = Introspector.getBeanInfo(Model.class, Object.class);
		PropertyDescriptor[] pp = info.getPropertyDescriptors();
		for (PropertyDescriptor p : pp) {
			System.out.println(p.getName() + ",");
		}
		
		ResmConfig config = new ResmConfig();
		config.setServer("127.0.0.1:10096");
		
		List<AttributeBase> list = new ArrayList<AttributeBase>();
		ResourceObject obj = new RemoteResourceService().getResourceById(175);
		AttributeBase a = obj.getAttribute("name");
		a.setText("¸ÄÒ»ÏÂ1");
		list.add(a);
		new RemoteResourceUpdateService().updateResource(175, list, 0);
	}
	public static void main(String[] args) throws Exception {
		String[] ids = new String[] { "im_tf_data_month#", "im_tf_data_week",
				"sdfsfd" ,
				"im_tf_data_test" ,
				"im_tf_data_aaaaaaaaa" ,
				"im_tf_data_day" };
		StringUtil.initCodePage();
		List<Model> list = new ArrayList<Model>();
		for (String id : ids) {
		Model m = new Model();
		m.setId(id);
		list.add(m);
		}
		UniqueIndexedList<Model> idx = new UniqueIndexedList<Model>(list);
		list = idx.values();
		for (Model m : list) {
			System.out.println(m.getId());
			System.out.println("---" + idx.get(m.getId()).getId());
		}
	}
	
	
}
