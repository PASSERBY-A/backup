package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ReferenceAttribute;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.CachedResourceService;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * 引用型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class ReferenceAttributeDefine extends AttributeDefine {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -1297463266733517714L;

	/**
	 * 引用的模型id
	 */
	private String refModelId = null;
	
	/**
	 * 关联模型下的所有的资源,只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private List<ResourceObject> res = null;

	/**
	 * 设置引用的模型id
	 * 
	 * @param refModelId
	 *            引用的模型id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #refModelId
	 */
	public void setRefModelId(String refModelId) throws CacheException {
		checkSet();
		this.refModelId = refModelId;
	}

	/**
	 * 获取引用的模型id
	 * 
	 * @return 引用的模型id
	 * @see #refModelId
	 */
	public String getRefModelId() {
		return this.refModelId;
	}

	/**
	 * 获取模型资源
	 * @return
	 */
	public List<ResourceObject> getRes() {
		List<ResourceObject> l = new ArrayList<ResourceObject>();
		CachedResourceService cs = (CachedResourceService) ServiceManager.getResourceService();
		l =  cs.getResourcesByModelId(this.refModelId, 1);
		return l;
	}

	/**
	 * 此方法无效，资源是从缓存中读取的
	 * @param res
	 */
	public void setRes(List<ResourceObject> res) {
		// 空函数
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		super.setArguments(arguments);
		try {
			JSONObject json = new JSONObject(arguments);
			setRefModelId(json.getString("refModelId"));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CacheException("参数格式错误: " + this.getId() + "/" + this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (StringUtil.checkNumber(value) != StringUtil.TYPE_INTEGER)
			return "属性“" + this.getName() + "”无效的取值:" + value;
		int id = StringUtil.parseInt(value, 0);
		ResourceObject obj = ServiceManager.getResourceService().getResourceById(id);
		if (obj == null || !obj.getModelId().equals(this.refModelId))
			return "属性“" + this.getName() + "”无效的取值:" + value;
		return null;
	}
	
	@Override
	public String getDataType() {
		return "number";
	}

	@Override
	public int getLength() {
		return 0;
	}
	
	@Override
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		this.length = getLength();
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new ReferenceAttribute();
	}
	
	@Override
	public List<AttributeOperator> getOperators() {
		return new ArrayList<AttributeOperator>();
	}
	
	@Override
	public List<AttributeConst> getConsts() {
		return new ArrayList<AttributeConst>();
	}
}
