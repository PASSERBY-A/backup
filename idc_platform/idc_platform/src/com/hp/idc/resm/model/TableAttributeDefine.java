package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.TableAttribute;

/**
 * 表格型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class TableAttributeDefine extends AttributeDefine {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 4301130830867441475L;

	/**
	 * 主键的字段名
	 */
	private String primaryField = "";

	/**
	 * 列信息
	 */
	private List<ModelAttribute> columns = new ArrayList<ModelAttribute>();

	/**
	 * 获取主键的字段名
	 * 
	 * @return 主键的字段名
	 * @see #primaryField
	 */
	public String getPrimaryField() {
		return this.primaryField;
	}

	/**
	 * 设置主键的字段名
	 * 
	 * @param primaryField
	 *            主键的字段名
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #primaryField
	 */
	public void setPrimaryField(String primaryField) throws CacheException {
		checkSet();
		this.primaryField = primaryField;
	}

	/**
	 * 获取列信息
	 * 
	 * @return 列信息
	 * @see #columns
	 */
	public List<ModelAttribute> getColumns() {
		return this.columns;
	}

	/**
	 * 设置列信息
	 * 
	 * @param columns
	 *            列信息
	 * @throws CacheException
	 *             对象已被缓存时发生
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
				throw new CacheException("参数格式错误: " + this.getId() + "/"
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
			throw new CacheException("参数格式错误: " + this.getId() + "/"
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
						return ma.getName() + "不能为空";
				}
			}
		} catch (Exception e) {
			return "格式不正确:" + this.getName();
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
