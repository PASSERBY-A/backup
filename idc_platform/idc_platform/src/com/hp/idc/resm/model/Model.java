package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.resource.rule.BasicRule;
import com.hp.idc.resm.resource.rule.IResourceRule;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ICompareKeyGetter;

/**
 * 资源模型
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Model extends CacheableObject {

	/**
	 * 模型下的资源类型：从父类型继承
	 */
	public static final int TYPE_INHERIT_FROM_PARENT = -1;
	/**
	 * 模型下的资源类型：其它
	 */
	public static final int TYPE_OTHER = 0;
	/**
	 * 模型下的资源类型：物理
	 */
	public static final int TYPE_PHYSICS = 1;
	/**
	 * 模型下的资源类型：业务
	 */
	public static final int TYPE_BUSSINESS = 2;
	/**
	 * 模型下的资源类型：应用
	 */
	public static final int TYPE_APPLICATION = 3;
	/**
	 * 模型下的资源类型：逻辑
	 */
	public static final int TYPE_LOGIC = 4;

	/**
	 * 定义根模型的父ID
	 */
	public final static String ROOT_PARENT_ID = "";

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -2043609521921687209L;

	/**
	 * 模型ID
	 */
	private String id;

	/**
	 * 父模型ID
	 */
	private String parentId;

	/**
	 * 模型名称
	 */
	private String name;

	/**
	 * 标明只是一个目录
	 */
	private boolean directoryOnly;

	/**
	 * 图标16*16
	 */
	private int icon;

	/**
	 * 大图标32*32
	 */
	private int largeIcon;

	/**
	 * 使用状态
	 */
	private boolean enabled;

	/**
	 * 维度模型
	 */
	private boolean dimModel;

	/**
	 * 模型下的资源对象类型
	 */
	private int type;

	/**
	 * 模型属性的缓存
	 */
	private List<ModelAttribute> attributeCache = null;

	/**
	 * 资源规则
	 */
	private IResourceRule rule = null;

	/**
	 * 模型下对象的类型
	 */
	private Class<? extends ResourceObject> objectClass = ResourceObject.class;

	/**
	 * 资源展示时的表头信息，序列化时使用
	 */
	protected List<AttributeDefine> header;

	/**
	 * 获取模型下的资源对象类型
	 * 
	 * @return type 模型下的资源对象类型
	 * @see #TYPE_OTHER
	 * @see #TYPE_PHYSICS
	 * @see #TYPE_BUSSINESS
	 */
	public int getResourceType() {
		if (this.type == TYPE_INHERIT_FROM_PARENT) {
			Model m = this.getParentModel();
			if (m != null)
				return m.getType();
			return TYPE_OTHER;
		}
		return this.type;
	}
	
	/**
	 * 获取模型下的资源对象类型
	 * 
	 * @return type 模型下的资源对象类型
	 * @see #type
	 * @see #TYPE_INHERIT_FROM_PARENT
	 * @see #TYPE_OTHER
	 * @see #TYPE_PHYSICS
	 * @see #TYPE_BUSSINESS
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置模型下的资源对象类型
	 * 
	 * @param type
	 *            模型下的资源对象类型
	 * @see #type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取资源展示时的表头信息，在派生类中重载
	 * 
	 * @return 表头包含的属性信息
	 */
	public List<AttributeDefine> getHeader() {
		List<AttributeBase> list = this.createObject().getHeader();
		List<AttributeDefine> ret = new ArrayList<AttributeDefine>();
		for (AttributeBase attr : list) {
			ret.add(attr.getAttribute().getDefine());
		}
		return ret;
	}

	/**
	 * 设置资源展示时的表头信息，此函数不生效，在flex序列化时使用
	 * 
	 * @param header
	 *            资源展示时的表头信息
	 */
	public void setHeader(List<AttributeDefine> header) {
		// 此处不做任何事
	}

	/**
	 * 判断是否另一个模型的子模型（可以判定多级）
	 * 
	 * @param modelId
	 *            另一个模型的id
	 * @return true/false
	 */
	public boolean isChildOf(String modelId) {
		if (modelId.equals(this.id))
			return true;
		Model m = this;
		while (m != null) {
			if (m.parentId.equals(modelId))
				return true;
			m = m.getParentModel();
		}
		return false;
	}

	/**
	 * 创建资源对象实例
	 * 
	 * @return 资源对象实例
	 */
	public ResourceObject createObject() {
		try {
			ResourceObject obj = this.objectClass.newInstance();
			try {
				obj.setModelId(this.id);
			} catch (CacheException e) {
				e.printStackTrace();
			}
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查资源对象是否本模型的实例
	 * 
	 * @param obj
	 *            资源对象
	 * @return true=是,false=否
	 */
	public boolean isInstance(ResourceObject obj) {
		return this.objectClass.isInstance(obj);
	}

	/**
	 * 获取子模型
	 * 
	 * @param userId
	 *            操作用户id
	 * 
	 * @return 子模型列表
	 */
	public List<Model> getChilds(int userId) {
		return ServiceManager.getModelService().getChildModelsById(this.id,
				userId);
	}

	/**
	 * 获取资源规则
	 * 
	 * @return 资源规则
	 */
	public IResourceRule getRule() {
		return this.rule;
	}

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", this.id);
		m.put("parentid", this.parentId);
		m.put("name", this.name);
		m.put("directoryonly", this.directoryOnly ? "1" : "0");
		m.put("iconid", "" + this.icon);
		m.put("largeiconid", "" + this.largeIcon);
		m.put("enabled", this.enabled ? "1" : "0");
		m.put("dimflag", this.dimModel ? "1" : "0");

		return m;
	}

	/**
	 * 是否有父模型
	 * 
	 * @return true表示有父模型，false表示没有
	 */
	public boolean hasParent() {
		return (this.parentId != null) && !this.parentId.equals(ROOT_PARENT_ID);
	}

	/**
	 * 获取父模型的对象
	 * 
	 * @return 父模型的对象
	 */
	public Model getParentModel() {
		return ServiceManager.getModelService().getModelById(this.parentId);
	}

	/**
	 * 获取模型所有属性
	 * 
	 * @return 模型所有属性
	 */
	public List<ModelAttribute> getAttributes() {
		if (this.attributeCache == null)
			this.attributeCache = ServiceManager.getModelService()
					.getModelAttributesByModelId(this.id);
		return this.attributeCache;
	}

	/**
	 * 根据id获取模型的属性
	 * 
	 * @param attrId
	 *            属性id
	 * @return 模型的属性
	 */
	public ModelAttribute getAttributeById(String attrId) {
		List<ModelAttribute> list = getAttributes();
		return getAttributeById(list, attrId);
	}

	/**
	 * 返回是否有指定的属性
	 * 
	 * @param attrId
	 *            属性id
	 * @return true=有，false=无
	 */
	public boolean hasAttribute(String attrId) {
		return getAttributeById(attrId) != null;
	}

	/**
	 * 根据id在指定的列表中获取模型的属性
	 * 
	 * @param list
	 *            属性列表
	 * 
	 * @param attrId
	 *            属性id
	 * @return 模型的属性
	 */
	private ModelAttribute getAttributeById(List<ModelAttribute> list,
			String attrId) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getAttrId().equals(attrId))
				return list.get(i);
		}
		return null;
	}

	/**
	 * 获取资源模型的id
	 * 
	 * @return 资源模型的id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置资源模型的id
	 * 
	 * @param id
	 *            资源模型的id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @throws Exception
	 *             id为空时产生
	 */
	@SuppressWarnings("unchecked")
	public void setId(String id) throws CacheException, Exception {
		checkSet();
		if (id == null || id.length() == 0)
			throw new Exception("id不能为空");
		this.id = id.toLowerCase().trim();

		String clsName = "com.hp.idc.resm.resource.rule.RuleOf_" + this.id;
		Class<? extends IResourceRule> cls = null;
		try {
			cls = (Class<? extends IResourceRule>) Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			cls = BasicRule.class;
		}
		try {
			this.rule = cls.newInstance();
		} catch (InstantiationException e) {
			// 不用处理
		} catch (IllegalAccessException e) {
			// 不用处理
		}
	}

	/**
	 * 获取图标的id
	 * 
	 * @return 图标的id
	 */
	public int getIcon() {
		return this.icon;
	}

	/**
	 * 设置图标的id
	 * 
	 * @param icon
	 *            图标的id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setIcon(int icon) throws CacheException {
		checkSet();
		this.icon = icon;
	}

	/**
	 * 获取大图标的id
	 * 
	 * @return 图标的id
	 */
	public int getLargeIcon() {
		return this.largeIcon;
	}

	/**
	 * 设置大图标的id
	 * 
	 * @param largeIcon
	 *            图标的id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setLargeIcon(int largeIcon) throws CacheException {
		checkSet();
		this.largeIcon = largeIcon;
	}

	/**
	 * 获取父模型的id
	 * 
	 * @return 父模型的id
	 */
	public String getParentId() {
		if (this.parentId == null)
			return Model.ROOT_PARENT_ID;
		return this.parentId;
	}

	/**
	 * 设置父模型的id
	 * 
	 * @param parentId
	 *            父模型的id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setParentId(String parentId) throws CacheException {
		checkSet();
		if (parentId == null || parentId.length() == 0)
			this.parentId = ROOT_PARENT_ID;
		else
			this.parentId = parentId;
	}

	/**
	 * 获取资源模型名称
	 * 
	 * @return 资源模型名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置资源模型名称
	 * 
	 * @param name
	 *            资源模型名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取资源模型是否在使用中
	 * 
	 * @return true表示使用中，false表示已禁用
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置资源模型是否在使用中
	 * 
	 * @param enabled
	 *            true表示使用中，false表示已禁用
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * 是否为维度模型
	 * 
	 * @return true表示此模型下的所有对象都是维度，false则不是
	 */
	public boolean isDimModel() {
		return this.dimModel;
	}

	/**
	 * 设置是否为维度模型
	 * 
	 * @param dimModel
	 *            true表示此模型下的所有对象都是维度，false则不是
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setDimModel(boolean dimModel) throws CacheException {
		checkSet();
		this.dimModel = dimModel;
	}

	/**
	 * 设置是否只是一个目录
	 * 
	 * @param directoryOnly
	 *            true表示只是一个目录，此时模型下不能新建资源对象。false则反之。
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setDirectoryOnly(boolean directoryOnly) throws CacheException {
		checkSet();
		this.directoryOnly = directoryOnly;
	}

	/**
	 * 表示是否只是一个目录
	 * 
	 * @return true表示只是一个目录，此时模型下不能新建资源对象。false则反之。
	 */
	public boolean isDirectoryOnly() {
		return this.directoryOnly;
	}

	@Override
	public String getPrimaryKey() {
		return getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_model";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateString("id", this.id);
		rs.updateString("parentid", this.parentId);
		rs.updateString("name", this.name);
		rs.updateInt("directoryonly", this.directoryOnly ? 1 : 0);
		rs.updateInt("iconid", this.icon);
		rs.updateInt("largeiconid", this.largeIcon);
		rs.updateInt("enabled", this.enabled ? 1 : 0);
		rs.updateInt("dimflag", this.dimModel ? 1 : 0);
		rs.updateInt("type", this.type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setId(rs.getString("id"));
		setParentId(rs.getString("parentid"));
		this.name = rs.getString("name");
		this.directoryOnly = rs.getInt("directoryonly") == 1;
		this.icon = rs.getInt("iconid");
		this.largeIcon = rs.getInt("largeiconid");
		this.enabled = rs.getInt("enabled") == 1;
		this.dimModel = rs.getInt("dimflag") == 1;
		this.type = rs.getInt("type");

		String clsName = rs.getString("objectclass");
		if (clsName == null || clsName.length() == 0)
			clsName = "com.hp.idc.resm.resource.ResourceObject";
		try {
			this.objectClass = (Class<? extends ResourceObject>) Class
					.forName(clsName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			this.objectClass = ResourceObject.class;
		}
	}

	@Override
	public void dump() {
		System.out.println("模型(" + this.id + ") = " + this.name);
		System.out.println("enabled = " + this.enabled);
		List<ModelAttribute> attributes = getAttributes();
		Iterator<ModelAttribute> it = attributes.iterator();
		while (it.hasNext()) {
			ModelAttribute ab = it.next();
			System.out.println("attribute(" + ab.getAttrId() + ") = "
					+ ab.getName());
		}
	}

	/**
	 * 生成模型的parentId
	 * 
	 * @author 梅园
	 * 
	 */
	public static class ModelParentIdGetter implements
			ICompareKeyGetter<String, Model> {

		public String getCompareKey(Model obj) {
			return obj.getParentId();
		}

	}

}
