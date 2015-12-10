package com.hp.idc.resm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.ModelRelationCache;
import com.hp.idc.resm.cache.RelationDefineCache;
import com.hp.idc.resm.cache.ResourceRelationCache;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.model.RelationDefine;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.resource.ResourceRelation;
import com.hp.idc.resm.util.XmlUtil;

/**
 * 关联关系服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedRelationService implements IRelationService {

	/**
	 * 关联关系定义缓存
	 */
	private RelationDefineCache relationDefineCache = null;

	/**
	 * 模型关系定义缓存
	 */
	private ModelRelationCache modelRelationCache = null;

	/**
	 * 资源对象关系缓存
	 */
	private ResourceRelationCache resourceRelationCache = null;

	/**
	 * 构造函数，初始化缓存
	 */
	public CachedRelationService() {
		this.relationDefineCache = new RelationDefineCache();
		this.modelRelationCache = new ModelRelationCache();
		this.resourceRelationCache = new ResourceRelationCache();

		try {
			this.relationDefineCache.initCache();
			this.modelRelationCache.initCache();
			this.resourceRelationCache.initCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取关联关系定义缓存
	 * 
	 * @return 关联关系定义缓存
	 * @see #relationDefineCache
	 */
	public RelationDefineCache getRelationDefineCache() {
		return this.relationDefineCache;
	}

	/**
	 * 获取模型关系定义缓存
	 * 
	 * @return 模型关系定义缓存
	 * @see #modelRelationCache
	 */
	public ModelRelationCache getModelRelationCache() {
		return this.modelRelationCache;
	}

	/**
	 * 获取资源对象关系缓存
	 * 
	 * @return 资源对象关系缓存
	 * @see #resourceRelationCache
	 */
	public ResourceRelationCache getResourceRelationCache() {
		return this.resourceRelationCache;
	}

	@Override
	public RelationDefine getRelationDefineById(String id) {
		return this.relationDefineCache.get(id);
	}

	@Override
	public List<RelationDefine> getAllRelationDefines() {
		return this.relationDefineCache.getAll();
	}

	@Override
	public List<ModelRelation> getAllModelRelations() {
		return this.modelRelationCache.getAll();
	}

	public List<ResourceRelation> getAllResourceRelations(){
		return this.resourceRelationCache.getAll();
	}
	
	/**
	 * 根据模型id返回所有关联关系
	 * 
	 * @param modelId
	 *            资源模型id
	 * @return 所有关联关系的列表
	 */
	@Override
	public List<ModelRelation> getModelRelationsByModelId(String modelId) {
		List<ModelRelation> list = new ArrayList<ModelRelation>();
		List<ModelRelation> all = getAllModelRelations();
		for (ModelRelation mr : all) {
			if (mr.getModelId().equals(modelId) || mr.getModelId2().equals(modelId)) {
				list.add(mr);
			}
		}
		return list;
	}
	
	/**
	 * 生成指定模型关联关系拓扑图数据
	 * 
	 * @param modelId 指定模型ID 
	 * @return 拓扑图数据
	 */
	public String getModelRelationXml(String modelId) {
		List<ModelRelation> list = getModelRelationsByModelId(modelId);
		return getModelRelationXml(list);
	}
	
	/**
	 * 生成所有模型关联关系拓扑图数据
	 * @return 拓扑图数据
	 */
	public String getModelRelationXml() {
		return getModelRelationXml(getAllModelRelations());
	}
	
	/**
	 * 生成模型关联关系拓扑图数据
	 * 
	 * @return 拓扑图数据
	 */
	private String getModelRelationXml(List<ModelRelation> list) {
		Document doc = null;
		try {
			doc = XmlUtil.parseString("<Graph/>");
		} catch (DocumentException e) {
			// 此处不可能发生
			e.printStackTrace();
			return "";
		}
		IModelService ms = ServiceManager.getModelService();
		Map<String, Model> map = new HashMap<String, Model>();
		for (int i = 0; i < list.size(); i++) {
			ModelRelation mr = list.get(i);
			Model m1 = ms.getModelById(mr.getModelId());
			Model m2 = ms.getModelById(mr.getModelId2());
			map.put(m1.getId(), m1);
			map.put(m2.getId(), m2);
		}
		Model[] models = map.values().toArray(new Model[] {});
		for (int i = 0; i < models.length; i++) {
			Model m = models[i];
			Element el = doc.getRootElement().addElement("Node");
			el.addAttribute("id", m.getId());
			el.addAttribute("name", m.getName());
			el.addAttribute("nodeIcon", "" + m.getLargeIcon());
			el.addAttribute("nodeSize", "32");
		}
		for (int i = 0; i < list.size(); i++) {
			ModelRelation mr = list.get(i);
			Element el = doc.getRootElement().addElement("Edge");
			el.addAttribute("fromID", mr.getModelId());
			el.addAttribute("toID", mr.getModelId2());
			el.addAttribute("edgeLabel",
					getRelationDefineById(mr.getRelationId()).getName());
		}
		return XmlUtil.getXmlString(doc);
	}
	
	/**
	 * 生成指定资源关联关系拓扑图数据
	 * @param resId 指定资源ID
	 * @return 拓扑图数据
	 */
	public String getResourceRelationXml(int resId){
		List<ResourceRelation> list = new ArrayList<ResourceRelation>();
		list.addAll(getRelationsByResourceId(resId));
		ResourceRelation rr = null;
		if (list.size() == 0) {
			rr = new ResourceRelation();
			try {
				rr.setItemId(resId);
				rr.setItemId2(-1);
			} catch (CacheException e) {
				e.printStackTrace();
			}
		}
		return getResourceRelationXml(list, rr, resId);
	}

	/**
	 * 生成所有资源关联关系拓扑图数据
	 * 
	 * @return 拓扑图数据
	 */
	public String getResourceRelationXml(){
		return getResourceRelationXml(getAllResourceRelations(), null, -1);
	}
	
	/**
	 * 生成资源关联关系拓扑图数据
	 * @param list 资源关联关系
	 * @param def 如果list.size=0, 默认显示的节点
	 * @return 拓扑图数据
	 */
	private String getResourceRelationXml(List<ResourceRelation> list, ResourceRelation def, int resId){
		Document doc = null;
		try {
			doc = XmlUtil.parseString("<Graph/>");
		} catch (DocumentException e) {
			// 此处不可能发生
			e.printStackTrace();
			return "";
		}
		boolean f = false;
		if(list.size() == 0 && def != null){
			f = true;
			list.add(def);
		}
		Map<Integer, ResourceObject> m = new HashMap<Integer, ResourceObject>();
		IResourceService is = ServiceManager.getResourceService();
		for(ResourceRelation r : list){
			ResourceObject ro = is.getResourceById(r.getItemId());
			ResourceObject ro1 = is.getResourceById(r.getItemId2());
			if(ro != null)
				m.put(ro.getId(), ro);
			if(ro1 != null)
				m.put(ro1.getId(), ro1);
		}
		for(ResourceObject ro : m.values()){
			Element el = doc.getRootElement().addElement("Node");
			el.addAttribute("id", String.valueOf(ro.getId()));
			el.addAttribute("name", ro.getName());
			if(resId > 0 && ro.getId() == resId)
				el.addAttribute("nodeSize", "50");
			else
				el.addAttribute("nodeSize", "32");
			el.addAttribute("nodeIcon", String.valueOf(ro.getModel().getIcon()));
		}
		for(ResourceRelation r : list){
			if(f)
				break;			
			Element el = doc.getRootElement().addElement("Edge");
			el.addAttribute("fromID", String.valueOf(r.getItemId()));
			el.addAttribute("toID", String.valueOf(r.getItemId2()));
			RelationDefine rd = getRelationDefineById(r.getRelationId());
			if(rd != null){
				el.addAttribute("edgeLabel",
						rd.getName());
				if(rd.getIcon() != null&& rd.getIcon().length() > 0)
					el.addAttribute("edgeClass", rd.getIcon());
				if(rd.getColor() != null && rd.getColor().length() > 0)
					el.addAttribute("color", rd.getColor());
			} else {
				el.addAttribute("edgeLabel","关系未定义");
			}
		}
		return XmlUtil.getXmlString(doc);
	}
	
	@Override
	public List<ResourceRelation> getRelationsByResourceId(int id) {
		return this.resourceRelationCache.getRelationsByResourceId(id);
	}

	@Override
	public ModelRelation getModelRelationById(int id) {
		return this.modelRelationCache.get("" + id);
	}

	/**
	 * 获取指定资源的关联资源集合
	 * @param resId 指定资源ID
	 * @return
	 */
	public List<ResourceObject> getResourceRelations(int resId){
		List<ResourceObject> list = new ArrayList<ResourceObject>();
		IResourceService is = ServiceManager.getResourceService();
		List<ResourceRelation> l = this.getRelationsByResourceId(resId);
		for(ResourceRelation r : l){
			if(r.getItemId() == resId){
				list.add(is.getResourceById(r.getItemId2()));
			} else if(r.getItemId2() == resId) {
				list.add(is.getResourceById(r.getItemId()));
			}
		}
		return list;
	}

	/**
	 * 获取某个资源有可能会有关联关系的资源集合.(包含本身)
	 * 即取出指定资源所属模型的所有关联模型下的所有资源.
	 * @param resId 指定资源ID
	 * @return
	 */
	public List<ResourceObject> getRelationsWithModelByResId(int resId, int userId){
		List<ResourceObject> list = new ArrayList<ResourceObject>();
		IResourceService is = ServiceManager.getResourceService();
		ResourceObject r = is.getResourceById(resId);
		Map<String, String> m = new HashMap<String, String>();
		for(ModelRelation mr : getModelRelationsByModelId(r.getModelId())){
			if(!mr.getModelId().equals(r.getModelId()))
				m.put(mr.getModelId(), "1");
			if(!mr.getModelId2().equals(r.getModelId()))
				m.put(mr.getModelId2(), "1");
		}
		for(String modelId : m.keySet()){
			list.addAll(is.getResourcesByModelId(modelId, userId));	
		}
		list.add(r);
		return list;
	}
}
