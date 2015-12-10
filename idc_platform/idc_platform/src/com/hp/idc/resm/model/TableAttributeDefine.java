package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.TableAttribute;

/**
 * �������Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class TableAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 4301130830867441475L;

	/**
	 * �������ֶ���
	 */
	private String primaryField = "";

	/**
	 * ����Ϣ
	 */
	private List<ModelAttribute> columns = new ArrayList<ModelAttribute>();

	/**
	 * ��ȡ�������ֶ���
	 * 
	 * @return �������ֶ���
	 * @see #primaryField
	 */
	public String getPrimaryField() {
		return this.primaryField;
	}

	/**
	 * �����������ֶ���
	 * 
	 * @param primaryField
	 *            �������ֶ���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #primaryField
	 */
	public void setPrimaryField(String primaryField) throws CacheException {
		checkSet();
		this.primaryField = primaryField;
	}

	/**
	 * ��ȡ����Ϣ
	 * 
	 * @return ����Ϣ
	 * @see #columns
	 */
	public List<ModelAttribute> getColumns() {
		return this.columns;
	}

	/**
	 * ��������Ϣ
	 * 
	 * @param columns
	 *            ����Ϣ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #columns
	 */
	public void setColumns(List<ModelAttribute> columns) throws CacheException {
		checkSet();
		this.columns = columns;
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		try {
			JSONObject json = new JSONObject(arguments);
			if (json.has("primaryField"))
				setPrimaryField(json.getString("primaryField"));
			json.put("primaryField", this.primaryField);
			JSONArray arr = json.getJSONArray("columns");
			if (arr.length() == 0)
				throw new CacheException("������ʽ����: " + this.getId() + "/"
						+ this.getName());
			for (int i = 0; i < arr.length(); i++) {
				ModelAttribute ma = new ModelAttribute();
				JSONObject o = arr.getJSONObject(i);
				ma.setAttrId(o.getString("id"));
				if (o.has("default"))
					ma.setDefaultValue(o.getString("default"));
				if (o.has("name"))
					ma.setName(o.getString("name"));
				if (o.has("remark"))
					ma.setRemark(o.getString("remark"));
				this.columns.add(ma);
			}
			json.put("columns", arr);
			super.setArguments(arguments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("������ʽ����: " + this.getId() + "/"
					+ this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		try {
			JSONArray arr = new JSONArray(value);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				for (ModelAttribute ma : this.columns) {
					AttributeDefine ad = ma.getDefine();
					String val = null;
					if (obj.has(ad.getId())) {
						val = obj.getString(ad.getId());
						String s = ad.valid(val, value1, attrId, op);
						if (s != null)
							return s;
					}
					if (!ma.isNullable() && (val == null || val.length() == 0))
						return ma.getName() + "����Ϊ��";
				}
			}
		} catch (Exception e) {
			return "��ʽ����ȷ:" + this.getName();
		}
		return null;
	}
	
	@Override
	public String getDataType() {
		return null;
	}

	@Override
	public int getLength() {
		return -1;
	}
	
	@Override
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		this.length = getLength();
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new TableAttribute();
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
