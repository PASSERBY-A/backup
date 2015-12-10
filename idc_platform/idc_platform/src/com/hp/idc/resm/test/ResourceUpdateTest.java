/**
 * 
 */
package com.hp.idc.resm.test;

import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.Model.ModelParentIdGetter;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ChineseStringCompareHandler;
import com.hp.idc.resm.util.IndexedList;
import com.hp.idc.resm.util.SortedArrayList;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceUpdateTest {

	/**
	 * TEST
	 * @throws Exception TEST
	 */
	public static void testCache() throws Exception {
		ModelParentIdGetter getter = new Model.ModelParentIdGetter();
		ChineseStringCompareHandler compare = new ChineseStringCompareHandler();
		List<Model> models = ServiceManager.getModelService().getAllModels(1);
		IndexedList<String, Model> list = new SortedArrayList<String, Model>(
				getter, compare, models);

		for (int i = 0; i < list.size(); i++) {
			Model m = list.getAt(i);
			System.out.println("model=" + m.getParentId() + "/" + m.getId()
					+ "/" + m.getName());
		}
	}

	/**
	 * TEST
	 */
	public static void test()  {
		List<Model> models = ServiceManager.getModelService()
				.getChildModelsById("", 1);
		for (int i = 0; i < models.size(); i++) {
			Model m = models.get(i);
			System.out.println("model=" + m.getParentId() + "/" + m.getId()
					+ "/" + m.getName());
		}
		System.out.println("----------------------------------");
		models = ServiceManager.getModelService().getChildModelsById("", true, 1);
		for (int i = 0; i < models.size(); i++) {
			Model m = models.get(i);
			System.out.println("model=" + m.getParentId() + "/" + m.getId()
					+ "/" + m.getName());
		}
	}
	
	/**
	 * test
	 * @param args test
	 * @throws Exception test
	 */
	public static void main(String[] args) throws Exception {
		JSONObject a = new JSONObject();
		a.put("aa", 1);
		System.out.println(a.toString());
		
	}

}
