package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.IntegerAttribute;
import com.hp.idc.resm.util.StringUtil;

/**
 * 整数型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class IntegerAttributeDefine extends AttributeDefine {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 253560893497008064L;
	
	/**
	 * 操作符定义
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "不等于", "$0 != $1"),
		new AttributeOperator("==", "等于", "$0 == $1"),
		new AttributeOperator(">", "大于", "$0 > $1"),
		new AttributeOperator("<", "小于", "$0 < $1"),
		new AttributeOperator(">=", "大于等于", "$0 >= $1"),
		new AttributeOperator("<=", "小于等于", "$0 <= $1"),
		new AttributeOperator("isnull", "为空", "$null.isNull($0)"),
		new AttributeOperator("notnull", "不为空", "$null.isNotNull($0)")
	};
	
	/**
	 * 常量定义
	 */
	private static AttributeConst[] CONSTS = {
	};

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (StringUtil.checkNumber(value) != StringUtil.TYPE_INTEGER)
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
		return new IntegerAttribute();
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
