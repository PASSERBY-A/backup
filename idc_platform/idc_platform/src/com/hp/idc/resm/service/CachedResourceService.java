package com.hp.idc.resm.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.Document;
import org.dom4j.Element;

import com.hp.idc.resm.cache.ResourceObjectCache;
import com.hp.idc.resm.cache.ResourceObjectCacheStore;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.security.Person;
import com.hp.idc.resm.ui.PageInfo;
import com.hp.idc.resm.ui.PageQueryInfo;
import com.hp.idc.resm.ui.TreeBuilder;
import com.hp.idc.resm.ui.TreeItem;
import com.hp.idc.resm.util.ICompareHandler;
import com.hp.idc.resm.util.MaxHeap;
import com.hp.idc.resm.util.RoleUtil;
import com.hp.idc.resm.util.StringUtil;
import com.hp.idc.resm.util.XmlUtil;

/**
 * ��Դ�������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedResourceService implements IResourceService {

	/**
	 * ��Դ���󻺴�
	 */
	private ResourceObjectCache resourceObjectCache = null;

	/**
	 * ���캯������ʼ������
	 */
	public CachedResourceService() {
		this.resourceObjectCache = new ResourceObjectCache();
		try {
			this.resourceObjectCache.initCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��Դ���󻺴�
	 * 
	 * @return ��Դ���󻺴�
	 */
	public ResourceObjectCache getCache() {
		return this.resourceObjectCache;
	}

	public ResourceObject getResourceById(int id) {
		return this.resourceObjectCache.get("" + id);
	}

	public List<ResourceObject> getResourcesByModelId(String id, int userId) {
		return getResourcesByModelId(id, false, userId);
	}

	public List<ResourceObject> getResourcesByModelId(String id,
			boolean includeChilds, int userId) {
		ResourceObjectCacheStore cs = (ResourceObjectCacheStore) this.resourceObjectCache
				.getCacheStore();
		List<ResourceObject> list = cs.getListByModelId(id);
		// TODO Ȩ���Ż�
		if (includeChilds) {
			List<Model> models = ServiceManager.getModelService()
					.getChildModelsById(id, true, userId);
			for (Model m : models) {
				list.addAll(cs.getListByModelId(m.getId()));
			}
		}
		return filterByUser(list, userId);
	}

	/**
	 * ������Դ�õ�VelocityContext
	 * 
	 * @author Silence
	 * 
	 */
	class ResourceFinder extends VelocityContext {
		/**
		 * ��ǰ����
		 */
		private ResourceObject currentResource = null;

		/**
		 * ��Ź��˽��
		 */
		public List<ResourceObject> result = new ArrayList<ResourceObject>();

		@Override
		public Object get(String key) {
			if (!super.containsKey(key) && this.currentResource != null) {
				AttributeBase ab = this.currentResource.getAttribute(key);
				if (ab == null)
					return null;
				return ab.getValueAsObject();
			}
			return super.get(key);
		}

		@Override
		public Object put(String key, Object value) {
			if (key.equals("ret")) {
				this.result.add(this.currentResource);
				return null;
			}
			if (key.equals("res"))
				this.currentResource = (ResourceObject) value;
			return super.put(key, value);
		}

	}

	public List<ResourceObject> findResource(String expr, int userId)
			throws Exception {
		if (expr == null || expr.trim().length() == 0)
			throw new Exception("���ʽ����Ϊ��");

		long l = System.currentTimeMillis();
		VelocityEngine ve = new VelocityEngine();
		StringWriter writer = new StringWriter();
		ResourceFinder context = new ResourceFinder();
		List<ResourceObject> ll = getAllResources(userId);
		context.put("list", ll);
		String str = "#foreach ($res in $list)\n" + "#if (" + expr + ")\n"
				+ "#set ($ret = 1)\n" + "#end\n" + "#end\n";
		boolean result = ve.evaluate(context, writer, "", str);
		System.out.println("��" + expr + "����" + ll.size() + "����¼����"
				+ (System.currentTimeMillis() - l) + "����, ���Ϊ" + result);
		return context.result;
	}
	
	//just for the count of ResourceHome about total,isEnable and 30days change
	public String getCountStr() throws Exception{
		String count = "0.0.0";
		List<ResourceObject> ll = getAllResources(0);
		count = ll.size()+"."+findResource(ll, "$res.isEnabled()", 0).size()+"."+findResource(ll, "$res.isChange(\"30\")", 0).size();
		return count;
	}

	public List<ResourceObject> findResource(List<ResourceObject> ll,
			String expr, int userId) throws Exception {
		if (expr == null || expr.trim().length() == 0)
			throw new Exception("���ʽ����Ϊ��");

		long l = System.currentTimeMillis();
		VelocityEngine ve = new VelocityEngine();
		StringWriter writer = new StringWriter();
		ResourceFinder context = new ResourceFinder();
		context.put("list", ll);
		String str = "#foreach ($res in $list)\n" + "#if (" + expr + ")\n"
				+ "#set ($ret = 1)\n" + "#end\n" + "#end\n";
		boolean result = ve.evaluate(context, writer, "", str);
		System.out.println("��" + expr + "����" + ll.size() + "����¼����"
				+ (System.currentTimeMillis() - l) + "����, ���Ϊ" + result);
		return context.result;
	}

	public List<ResourceObject> getAllResources(int userId) {
		// TODO Ȩ���Ż�
		return filterByUser(this.resourceObjectCache.getAll(), userId);
	}

	public ResourceObject getResourceByAttribute(String attrId, String value)
			throws Exception {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) getCache()
				.getCacheStore();
		List<ResourceObject> l = store.findInGlobal(attrId, value);
		if (l.size() > 1)
			throw new Exception("����" + attrId + "=" + value + "ʱ�����ز�Ψһ��");
		if (l.size() == 1)
			return l.get(0);
		return null;
	}

	public List<AttributeBase> getResourceAttributes(int id) {
		List<AttributeBase> list = new ArrayList<AttributeBase>();
		ResourceObject object = getResourceById(id);
		if (object != null) {
			List<ModelAttribute> attrs = object.getModel().getAttributes();
			for (ModelAttribute ma : attrs) {
				list.add(object.getAttribute(ma.getAttrId()));
			}
		}
		return list;
	}

	/**
	 * �ȽϽӿ�
	 * 
	 */
	class ResourceSortCompareHandler implements ICompareHandler<ResourceObject> {
		/**
		 * �����id
		 */
		private String[] id;

		/**
		 * �洢�����Ƿ���
		 */
		private int[] order;

		/**
		 * ���캯��
		 * 
		 * @param sort
		 *            ������Ϣ���Զ��ŷָ�������Ļ�ǰ��Ӽ��ţ���"id,-name,...."
		 */
		public ResourceSortCompareHandler(String sort) {
			this.id = sort.split(",");
			this.order = new int[this.id.length];
			for (int i = 0; i < this.order.length; i++) {
				if (this.id[i].startsWith("-")) {
					this.order[i] = -1;
					this.id[i] = this.id[i].substring(1);
				} else {
					this.order[i] = 1;
				}
			}
		}

		public int compare(ResourceObject a, ResourceObject b) {
			for (int i = 0; i < this.order.length; i++) {
				if (this.id[i].length() == 0)
					continue;
				AttributeBase ab1 = a.getAttribute(this.id[i]);
				if (ab1 == null)
					return -1 * this.order[i];
				if (!(ab1 instanceof Comparable<?>)) { // ���ܱȽϣ�������
					continue;
				}
				AttributeBase ab2 = b.getAttribute(this.id[i]);
				if (ab2 == null)
					return this.order[i];
				@SuppressWarnings("unchecked")
				int r = ((Comparable<AttributeBase>) ab1).compareTo(ab2);
				if (r == 0) // �����Ƚ���һ���ֶ�
					continue;
				return this.order[i] * r;
			}
			return 0;
		}
	}

	public PageInfo<ResourceObject> listResource(String modelId, String str,
			PageQueryInfo queryInfo, int userId) {
		List<ResourceObject> list = this.getResourcesByModelId(modelId, userId);
		
		// ����
		if (str != null) {
			String[] strArr=str.split(",");
			if(strArr!=null){
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).isMatch(strArr)) {
						list.remove(i);
						i--;
					}
				}
			}
		}
		
		// ����
		MaxHeap<ResourceObject> m = new MaxHeap<ResourceObject>();
		list = m.sort(list, new ResourceSortCompareHandler(queryInfo
				.getSortBy()));

		PageInfo<ResourceObject> ret = new PageInfo<ResourceObject>();
		ret.setTotalPage((list.size() - 1) / queryInfo.getPageCount() + 1);
		ret.setPageCount(queryInfo.getPageCount());
		if (queryInfo.getStartPage() > ret.getTotalPage())
			ret.setCurrentPage(ret.getTotalPage());
		else
			ret.setCurrentPage(queryInfo.getStartPage());
		ret.setIndex((ret.getCurrentPage() - 1) * ret.getPageCount() + 1);
		ret.setTotalCount(list.size());

		List<ResourceObject> list2 = new ArrayList<ResourceObject>();
		for (int i = 0; i < ret.getPageCount(); i++) {
			int index = ret.getIndex() + i - 1;
			if (index >= list.size())
				break;
			list2.add(list.get(index));
		}
		ret.setData(list2);
		return ret;
	}

	/**
	 * ��ȡ��Դ��
	 * 
	 * @param modelId
	 *            ģ��id
	 * @param parentAttrId
	 *            ������id
	 * @return ��Դ����xml����flexʹ��
	 */
	public String getResourceTree(String modelId, String parentAttrId) {
		List<ResourceObject> list = this.getResourcesByModelId(modelId,
				Person.ADMIN_ID);
		TreeBuilder tb = new TreeBuilder();
		for (ResourceObject res : list) {
			Object parent = res.getAttribute(parentAttrId).getValueAsObject();
			String parentId = "";
			if (parent != null && parent instanceof ResourceObject)
				parentId = "" + ((ResourceObject) parent).getId();
			tb.addItem(new TreeItem("" + res.getId(), res.getName(), parentId));
		}
		return tb.toString();
	}

	public List<ResourceObject> getResourcesById(String id) {
		List<ResourceObject> list = new ArrayList<ResourceObject>();
		if (id == null || id.length() == 0)
			return list;
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			int n = StringUtil.parseInt(ids[i], -1);
			if (n <= 0)
				continue;
			ResourceObject res = this.getResourceById(n);
			if (res != null)
				list.add(res);
		}
		return list;
	}

	public int[] filterByUser(int[] idArray, int userId) {
		if (RoleUtil.isSystemAdmin(userId))
			return idArray;
		List<ResourceObject> l = new ArrayList<ResourceObject>();
		for (int id : idArray) {
			ResourceObject res = getResourceById(id);
			if (res != null)
				l.add(res);
		}
		l = filterByUser(l, userId);
		int[] r = new int[l.size()];
		for (int i = 0; i < l.size(); i++)
			r[i] = l.get(i).getId();
		return r;
	}

	public boolean hasAccessPermission(int id, int userId) {
		if (RoleUtil.isSystemAdmin(userId))
			return true;
		return filterByUser(new int[] { id }, userId).length > 0;
	}

	/**
	 * ���û�Ȩ�޶��б��е���Դ������й���
	 * 
	 * @param list
	 *            ��Դ�����б�
	 * @param userId
	 *            �û�id
	 * @return �û��з�����Ȩ����Դ����
	 */
	public List<ResourceObject> filterByUser(List<ResourceObject> list,
			int userId) {
		List<ResourceObject> ret = new ArrayList<ResourceObject>();
		// �Ȱ��ɷ��ʵ�ģ�͹���
		List<Model> models = ServiceManager.getModelService().getAllModels(
				userId);
		String lastModelId = null;
		boolean ok = false; // �Ƿ���ģ�͵ķ���Ȩ�ޣ�
		boolean ok2 = false; // �Ƿ��з���ģ����������Դ�����Ȩ��
		for (int i = 0; i < list.size(); i++) {
			ResourceObject res = list.get(i);
			if (!res.getModelId().equals(lastModelId)) {
				lastModelId = res.getModelId();
				ok = ok2 = false;
				for (int j = 0; j < models.size(); j++) {
					if (models.get(j).getId().equals(lastModelId)) {
						ok = true;
						break;
					}
				}
				if (ok) {
					try {
						ok2 = RoleUtil.checkUserPermission(userId,
								"access_resource_by_model", lastModelId, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (ok2) { // ��Ȩ��
				ret.add(list.remove(i));
				i--;
			}
			if (!ok) { // ûȨ��
				list.remove(i);
				i--;
			}
		}

		// TODO Ȩ�޿���
		return ret;
	}

	@Override
	public PageInfo<ResourceObject> listResource(String modelId,
			boolean recursion, String str, PageQueryInfo queryInfo, int userId) {
		List<ResourceObject> list = getResourcesByModelId(modelId, recursion,
				userId);
		// ����
		if (str != null && str.trim().length() > 0) {
			String[] strArr=str.split(",");
			if(strArr!=null){
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).isMatch(strArr)) {
						list.remove(i);
						i--;
					}
				}
			}
		}
		

		// ����
		MaxHeap<ResourceObject> m = new MaxHeap<ResourceObject>();
		list = m.sort(list, new ResourceSortCompareHandler(queryInfo
				.getSortBy()));

		PageInfo<ResourceObject> ret = new PageInfo<ResourceObject>();
		ret.setTotalPage((list.size() - 1) / queryInfo.getPageCount() + 1);
		ret.setPageCount(queryInfo.getPageCount());
		if (queryInfo.getStartPage() > ret.getTotalPage())
			ret.setCurrentPage(ret.getTotalPage());
		else
			ret.setCurrentPage(queryInfo.getStartPage());
		ret.setIndex((ret.getCurrentPage() - 1) * ret.getPageCount() + 1);
		ret.setTotalCount(list.size());

		List<ResourceObject> list2 = new ArrayList<ResourceObject>();
		for (int i = 0; i < ret.getPageCount(); i++) {
			int index = ret.getIndex() + i - 1;
			if (index >= list.size())
				break;
			list2.add(list.get(index));
		}
		ret.setData(list2);
		return ret;
	}

	@Override
	public PageInfo<ResourceObject> listResource(String modelId, boolean recursion, 
			Map<String,Object> paramMap, PageQueryInfo queryInfo, int userId){
		
		List<ResourceObject> list = getResourcesByModelId(modelId, recursion,
				userId);
		// ����
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isMatch(paramMap)) {
				list.remove(i);
				i--;
			}
		}
		// ����
		MaxHeap<ResourceObject> m = new MaxHeap<ResourceObject>();
		list = m.sort(list, new ResourceSortCompareHandler(queryInfo
				.getSortBy()));

		PageInfo<ResourceObject> ret = new PageInfo<ResourceObject>();
		ret.setTotalPage((list.size() - 1) / queryInfo.getPageCount() + 1);
		ret.setPageCount(queryInfo.getPageCount());
		if (queryInfo.getStartPage() > ret.getTotalPage())
			ret.setCurrentPage(ret.getTotalPage());
		else
			ret.setCurrentPage(queryInfo.getStartPage());
		ret.setIndex((ret.getCurrentPage() - 1) * ret.getPageCount() + 1);
		ret.setTotalCount(list.size());

		List<ResourceObject> list2 = new ArrayList<ResourceObject>();
		for (int i = 0; i < ret.getPageCount(); i++) {
			int index = ret.getIndex() + i - 1;
			if (index >= list.size())
				break;
			list2.add(list.get(index));
		}
		ret.setData(list2);
		return ret;
	}

	@Override
	public List<ResourceObject> getResourceByAttribute(String modelId, String exp, int userId) throws Exception{
		List<ResourceObject> list = this.getResourcesByModelId(modelId, userId);
		List<ResourceObject> l = new ArrayList<ResourceObject>();
		l = findResource(list, exp, userId);
		return l;
	}
	
	/**
	 * ����ָ����������Ϣ��, ��flexչʾ
	 * @param resId ������ԴID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getRoomDetail(int resId, int userId) throws Exception{
		Document doc = XmlUtil.parseString("<room/>");
		ResourceObject room = getResourceById(resId);
		if(room == null)
			return XmlUtil.getXmlString(doc);
		List<ResourceObject> list = getResourceByAttribute("rack", "$res.getAttributeValue('ref_room')=='"+resId+"'", userId);
		for(ResourceObject r : list){
			Element el = doc.getRootElement().addElement("rack");
			List<ResourceObject> l = getResourceByAttribute("slot", "$res.getAttributeValue('ref_rack')=='"+r.getId()+"'", userId);
			el.addElement("id").setText(String.valueOf(r.getId()));
			el.addElement("name").setText(r.getName());
			el.addElement("address").setText(r.getAttributeValue("rack_point"));
			el.addElement("total").setText(l.size()+"");
			el.addElement("valiable").setText(findResource(l, "$res.getAttributeValue('status')=='����'", userId).size()+"");
			String status = r.getAttributeValue("status");
			el.addElement("status").setText(status);
			el.addElement("room").setText(room.getName());
			if(status != null && status.equals("ʹ����"))
				el.addElement("detail").setText("��������: "+r.getAttributeValue("task_link")+";�ͻ����: "+r.getAttributeValue("customer_id")+";��ͬ����ʱ��: "+r.getAttributeValue("contract_end"));
			else
				el.addElement("detail").setText(r.getAttributeValue("description"));
		}
		return XmlUtil.getXmlString(doc);
	}
	
	/**
	 * ����ָ�����ܵ���Ϣ��, ��flexչʾ
	 * @param resId ������ԴID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getRackDetail(int resId, int userId) throws Exception{
		Document doc = XmlUtil.parseString("<rack/>");
		ResourceObject rack = getResourceById(resId);
		if(rack == null)
			return XmlUtil.getXmlString(doc);
		List<ResourceObject> list = getResourceByAttribute("slot", "$res.getAttributeValue('ref_rack')=='"+resId+"'", userId);
		Collections.sort(list, new Comparator<ResourceObject>() {
			@Override
			public int compare(ResourceObject o1, ResourceObject o2) {
				int _o1 = StringUtil.parseInt(o1.getAttributeValue("slot_orderno"), 0);
				int _o2 = StringUtil.parseInt(o2.getAttributeValue("slot_orderno"), 0);
				if(_o1 > _o2)
					return 1;
				else if(_o1 < _o2)
					return -1;
				else
					return 0;
			}
		});
		for(ResourceObject r : list){
			Element el = doc.getRootElement().addElement("slot");
			el.addElement("id").setText(String.valueOf(r.getId()));
			el.addElement("order").setText(r.getAttributeValue("slot_orderno"));
			String status = r.getAttributeValue("status");
			el.addElement("status").setText(status);
			el.addElement("rack").setText(rack.getName());
			if(status != null && status.equals("ʹ����"))
				el.addElement("detail").setText("��������: "+r.getAttributeValue("task_link")+";�ͻ����: "+r.getAttributeValue("customer_id")+";��ͬ����ʱ��: "+r.getAttributeValue("contract_end"));
			else
				el.addElement("detail").setText(r.getAttributeValue("description"));
		}
		return XmlUtil.getXmlString(doc);
	}
	
	public String getSlotDetail(int resId,int userId) throws Exception{
		Document doc = XmlUtil.parseString("<racks/>");
		ResourceObject slot = getResourceById(resId);
		if(slot == null)
			return XmlUtil.getXmlString(doc);
		ResourceObject r = getResourceById(Integer.valueOf(slot.getAttributeValue("ref_rack")));
		if(r == null)
			return XmlUtil.getXmlString(doc);
		Element el = doc.getRootElement().addElement("rack");
		List<ResourceObject> l = getResourceByAttribute("slot", "$res.getAttributeValue('ref_rack')=='"+r.getId()+"'", userId);
		el.addElement("id").setText(String.valueOf(r.getId()));
		el.addElement("name").setText(r.getName());
		el.addElement("address").setText(r.getAttributeValue("rack_point"));
		el.addElement("total").setText(l.size()+"");
		el.addElement("valiable").setText(findResource(l, "$res.getAttributeValue('status')=='����'", userId).size()+"");
		String status = r.getAttributeValue("status");
		el.addElement("status").setText(status);
		if(status != null && status.equals("ʹ����"))
			el.addElement("detail").setText("��������: "+r.getAttributeValue("task_link")+";�ͻ����: "+r.getAttributeValue("customer_id")+";��ͬ����ʱ��: "+r.getAttributeValue("contract_end"));
		else
			el.addElement("detail").setText(r.getAttributeValue("description"));		
		return XmlUtil.getXmlString(doc);
	}
}
