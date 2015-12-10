package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.BooleanAttribute;

/**
 * 布尔型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class BooleanAttributeDefine extends AttributeDefine {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * “是”的描述
	 */
	private String yes = "是";

	/**
	 * “否”的描述
	 */
	private String no = "否";

	/**
	 * 操作符定义
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "不等于", "$0 != $1"),
		new AttributeOperator("==", "等于", "$0 == $1"),
		new AttributeOperator("isnull", "为空", "$null.isNull($0)"),
		new AttributeOperator("notnull", "不为空", "$null.isNotNull($0)")
	};
	
	/**
	 * 常量定义
	 */
	private static AttributeConst[] CONSTS = {
		new AttributeStringConst("true", "真", "true"),
		new AttributeStringConst("false", "假", "false")
	};

	/**
	 * 获取“是”的描述
	 * 
	 * @return “是”的描述
	 * @see #yes
	 */
	public String getYes() {
		return this.yes;
	}

	/**
	 * 设置“是”的描述
	 * 
	 * @param yes
	 *            “是”的描述
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #yes
	 */
	public void setYes(String yes) throws CacheException {
		checkSet();
		this.yes = yes;
	}

	/**
	 * 获取“否”的描述
	 * 
	 * @return “否”的描述
	 * @see #no
	 */
	public String getNo() {
		return this.no;
	}

	/**
	 * 设置“否”的描述
	 * 
	 * @param no
	 *            “否”的描述
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #no
	 */
	public void setNo(String no) throws CacheException {
		checkSet();
		this.no = no;
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		try {
			JSONObject json = new JSONObject(arguments);
			if (json.has("yes"))
				setYes(json.getString("yes"));
			json.put("yes", this.yes);
			if (json.has("no"))
				setNo(json.getString("no"));
			json.put("no", this.no);
			super.setArguments(arguments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("参数格式错误: " + this.getId() + "/"
					+ this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (this.yes.equals(value) || this.no.equals(value))
			return null;
		return "属性“" + this.getName() + "”无效的取值:" + value;
	}

	@Override
	public String getDataType() {
		return "varchar2";
	}

	@Override
	public int getLength() {
		return 8;
	}

	@Override
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		this.length = getLength();
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new BooleanAttribute();
	}

	@Override
	public List<AttributeOperator> getOperators() {
		List<AttributeOperator> list = new ArrayList<AttributeOperator>(
				OPERATORS.length);
		for (AttributeOperator op : OPERATORS)
			list.add(op);
		return list;
	}

	@Override
	public List<AttributeConst> getConsts() {
		List<AttributeConst> list = new ArrayList<AttributeConst>(
				CONSTS.length);
		for (AttributeConst c : CONSTS)
			list.add(c);
		return list;
	}
}
