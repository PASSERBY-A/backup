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
 * ��������Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class ReferenceAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -1297463266733517714L;

	/**
	 * ���õ�ģ��id
	 */
	private String refModelId = null;
	
	/**
	 * ����ģ���µ����е���Դ,ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private List<ResourceObject> res = null;

	/**
	 * �������õ�ģ��id
	 * 
	 * @param refModelId
	 *            ���õ�ģ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #refModelId
	 */
	public void setRefModelId(String refModelId) throws CacheException {
		checkSet();
		this.refModelId = refModelId;
	}

	/**
	 * ��ȡ���õ�ģ��id
	 * 
	 * @return ���õ�ģ��id
	 * @see #refModelId
	 */
	public String getRefModelId() {
		return this.refModelId;
	}

	/**
	 * ��ȡģ����Դ
	 * @return
	 */
	public List<ResourceObject> getRes() {
		List<ResourceObject> l = new ArrayList<ResourceObject>();
		CachedResourceService cs = (CachedResourceService) ServiceManager.getResourceService();
		l =  cs.getResourcesByModelId(this.refModelId, 1);
		return l;
	}

	/**
	 * �˷�����Ч����Դ�Ǵӻ����ж�ȡ��
	 * @param res
	 */
	public void setRes(List<ResourceObject> res) {
		// �պ���
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		super.setArguments(arguments);
		try {
			JSONObject json = new JSONObject(arguments);
			setRefModelId(json.getString("refModelId"));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CacheException("������ʽ����: " + this.getId() + "/" + this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (StringUtil.checkNumber(value) != StringUtil.TYPE_INTEGER)
			return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
		int id = StringUtil.parseInt(value, 0);
		ResourceObject obj = ServiceManager.getResourceService().getResourceById(id);
		if (obj == null || !obj.getModelId().equals(this.refModelId))
			return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
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
