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
 * ��Դģ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Model extends CacheableObject {

	/**
	 * ģ���µ���Դ���ͣ��Ӹ����ͼ̳�
	 */
	public static final int TYPE_INHERIT_FROM_PARENT = -1;
	/**
	 * ģ���µ���Դ���ͣ�����
	 */
	public static final int TYPE_OTHER = 0;
	/**
	 * ģ���µ���Դ���ͣ�����
	 */
	public static final int TYPE_PHYSICS = 1;
	/**
	 * ģ���µ���Դ���ͣ�ҵ��
	 */
	public static final int TYPE_BUSSINESS = 2;
	/**
	 * ģ���µ���Դ���ͣ�Ӧ��
	 */
	public static final int TYPE_APPLICATION = 3;
	/**
	 * ģ���µ���Դ���ͣ��߼�
	 */
	public static final int TYPE_LOGIC = 4;

	/**
	 * �����ģ�͵ĸ�ID
	 */
	public final static String ROOT_PARENT_ID = "";

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -2043609521921687209L;

	/**
	 * ģ��ID
	 */
	private String id;

	/**
	 * ��ģ��ID
	 */
	private String parentId;

	/**
	 * ģ������
	 */
	private String name;

	/**
	 * ����ֻ��һ��Ŀ¼
	 */
	private boolean directoryOnly;

	/**
	 * ͼ��16*16
	 */
	private int icon;

	/**
	 * ��ͼ��32*32
	 */
	private int largeIcon;

	/**
	 * ʹ��״̬
	 */
	private boolean enabled;

	/**
	 * ά��ģ��
	 */
	private boolean dimModel;

	/**
	 * ģ���µ���Դ��������
	 */
	private int type;

	/**
	 * ģ�����ԵĻ���
	 */
	private List<ModelAttribute> attributeCache = null;

	/**
	 * ��Դ����
	 */
	private IResourceRule rule = null;

	/**
	 * ģ���¶��������
	 */
	private Class<? extends ResourceObject> objectClass = ResourceObject.class;

	/**
	 * ��Դչʾʱ�ı�ͷ��Ϣ�����л�ʱʹ��
	 */
	protected List<AttributeDefine> header;

	/**
	 * ��ȡģ���µ���Դ��������
	 * 
	 * @return type ģ���µ���Դ��������
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
	 * ��ȡģ���µ���Դ��������
	 * 
	 * @return type ģ���µ���Դ��������
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
	 * ����ģ���µ���Դ��������
	 * 
	 * @param type
	 *            ģ���µ���Դ��������
	 * @see #type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * ��ȡ��Դչʾʱ�ı�ͷ��Ϣ����������������
	 * 
	 * @return ��ͷ������������Ϣ
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
	 * ������Դչʾʱ�ı�ͷ��Ϣ���˺�������Ч����flex���л�ʱʹ��
	 * 
	 * @param header
	 *            ��Դչʾʱ�ı�ͷ��Ϣ
	 */
	public void setHeader(List<AttributeDefine> header) {
		// �˴������κ���
	}

	/**
	 * �ж��Ƿ���һ��ģ�͵���ģ�ͣ������ж��༶��
	 * 
	 * @param modelId
	 *            ��һ��ģ�͵�id
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
	 * ������Դ����ʵ��
	 * 
	 * @return ��Դ����ʵ��
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
	 * �����Դ�����Ƿ�ģ�͵�ʵ��
	 * 
	 * @param obj
	 *            ��Դ����
	 * @return true=��,false=��
	 */
	public boolean isInstance(ResourceObject obj) {
		return this.objectClass.isInstance(obj);
	}

	/**
	 * ��ȡ��ģ��
	 * 
	 * @param userId
	 *            �����û�id
	 * 
	 * @return ��ģ���б�
	 */
	public List<Model> getChilds(int userId) {
		return ServiceManager.getModelService().getChildModelsById(this.id,
				userId);
	}

	/**
	 * ��ȡ��Դ����
	 * 
	 * @return ��Դ����
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
	 * �Ƿ��и�ģ��
	 * 
	 * @return true��ʾ�и�ģ�ͣ�false��ʾû��
	 */
	public boolean hasParent() {
		return (this.parentId != null) && !this.parentId.equals(ROOT_PARENT_ID);
	}

	/**
	 * ��ȡ��ģ�͵Ķ���
	 * 
	 * @return ��ģ�͵Ķ���
	 */
	public Model getParentModel() {
		return ServiceManager.getModelService().getModelById(this.parentId);
	}

	/**
	 * ��ȡģ����������
	 * 
	 * @return ģ����������
	 */
	public List<ModelAttribute> getAttributes() {
		if (this.attributeCache == null)
			this.attributeCache = ServiceManager.getModelService()
					.getModelAttributesByModelId(this.id);
		return this.attributeCache;
	}

	/**
	 * ����id��ȡģ�͵�����
	 * 
	 * @param attrId
	 *            ����id
	 * @return ģ�͵�����
	 */
	public ModelAttribute getAttributeById(String attrId) {
		List<ModelAttribute> list = getAttributes();
		return getAttributeById(list, attrId);
	}

	/**
	 * �����Ƿ���ָ��������
	 * 
	 * @param attrId
	 *            ����id
	 * @return true=�У�false=��
	 */
	public boolean hasAttribute(String attrId) {
		return getAttributeById(attrId) != null;
	}

	/**
	 * ����id��ָ�����б��л�ȡģ�͵�����
	 * 
	 * @param list
	 *            �����б�
	 * 
	 * @param attrId
	 *            ����id
	 * @return ģ�͵�����
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
	 * ��ȡ��Դģ�͵�id
	 * 
	 * @return ��Դģ�͵�id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ������Դģ�͵�id
	 * 
	 * @param id
	 *            ��Դģ�͵�id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @throws Exception
	 *             idΪ��ʱ����
	 */
	@SuppressWarnings("unchecked")
	public void setId(String id) throws CacheException, Exception {
		checkSet();
		if (id == null || id.length() == 0)
			throw new Exception("id����Ϊ��");
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
			// ���ô���
		} catch (IllegalAccessException e) {
			// ���ô���
		}
	}

	/**
	 * ��ȡͼ���id
	 * 
	 * @return ͼ���id
	 */
	public int getIcon() {
		return this.icon;
	}

	/**
	 * ����ͼ���id
	 * 
	 * @param icon
	 *            ͼ���id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setIcon(int icon) throws CacheException {
		checkSet();
		this.icon = icon;
	}

	/**
	 * ��ȡ��ͼ���id
	 * 
	 * @return ͼ���id
	 */
	public int getLargeIcon() {
		return this.largeIcon;
	}

	/**
	 * ���ô�ͼ���id
	 * 
	 * @param largeIcon
	 *            ͼ���id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setLargeIcon(int largeIcon) throws CacheException {
		checkSet();
		this.largeIcon = largeIcon;
	}

	/**
	 * ��ȡ��ģ�͵�id
	 * 
	 * @return ��ģ�͵�id
	 */
	public String getParentId() {
		if (this.parentId == null)
			return Model.ROOT_PARENT_ID;
		return this.parentId;
	}

	/**
	 * ���ø�ģ�͵�id
	 * 
	 * @param parentId
	 *            ��ģ�͵�id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setParentId(String parentId) throws CacheException {
		checkSet();
		if (parentId == null || parentId.length() == 0)
			this.parentId = ROOT_PARENT_ID;
		else
			this.parentId = parentId;
	}

	/**
	 * ��ȡ��Դģ������
	 * 
	 * @return ��Դģ������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ������Դģ������
	 * 
	 * @param name
	 *            ��Դģ������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ȡ��Դģ���Ƿ���ʹ����
	 * 
	 * @return true��ʾʹ���У�false��ʾ�ѽ���
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * ������Դģ���Ƿ���ʹ����
	 * 
	 * @param enabled
	 *            true��ʾʹ���У�false��ʾ�ѽ���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * �Ƿ�Ϊά��ģ��
	 * 
	 * @return true��ʾ��ģ���µ����ж�����ά�ȣ�false����
	 */
	public boolean isDimModel() {
		return this.dimModel;
	}

	/**
	 * �����Ƿ�Ϊά��ģ��
	 * 
	 * @param dimModel
	 *            true��ʾ��ģ���µ����ж�����ά�ȣ�false����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setDimModel(boolean dimModel) throws CacheException {
		checkSet();
		this.dimModel = dimModel;
	}

	/**
	 * �����Ƿ�ֻ��һ��Ŀ¼
	 * 
	 * @param directoryOnly
	 *            true��ʾֻ��һ��Ŀ¼����ʱģ���²����½���Դ����false��֮��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setDirectoryOnly(boolean directoryOnly) throws CacheException {
		checkSet();
		this.directoryOnly = directoryOnly;
	}

	/**
	 * ��ʾ�Ƿ�ֻ��һ��Ŀ¼
	 * 
	 * @return true��ʾֻ��һ��Ŀ¼����ʱģ���²����½���Դ����false��֮��
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
		System.out.println("ģ��(" + this.id + ") = " + this.name);
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
	 * ����ģ�͵�parentId
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class ModelParentIdGetter implements
			ICompareKeyGetter<String, Model> {

		public String getCompareKey(Model obj) {
			return obj.getParentId();
		}

	}

}
