package com.hp.idc.resm.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.DateAttribute;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 日期型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class DateAttributeDefine extends AttributeDefine {

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
	 * 序列化UID
	 */
	private static final long serialVersionUID = -7004014027735136838L;

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.trim().length() == 0)
			return null;
		String format = DateAttribute.DATE_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = sdf.parse(value);
		} catch (ParseException e) {
			return "属性“" + this.getName() + "”无效的取值:" + value;
		}
		if (value1 == null || value1.trim().length() == 0) {
			return null;
		}
		Date d1 = null;
		try {
			d1 = sdf.parse(value1);
		} catch (ParseException e) {
			return null;
		}
		if (op.equals(">") && d.before(d1)) {
			AttributeDefine ad = ServiceManager.getAttributeService().getAttributeById(attrId);
			if (ad == null) {
				return "属性“" + this.getName() + "”值应大于"+value1;
			}
			return "属性“" + this.getName() + "”值应大于属性“"+ad.getName()+"”值";
		}
		if (op.equals("<") && d.after(d1)) {
			AttributeDefine ad = ServiceManager.getAttributeService().getAttributeById(attrId);
			if (ad == null) {
				return "属性“" + this.getName() + "”值应小于"+value1;
			}
			return "属性“" + this.getName() + "”值应小于属性“"+ad.getName()+"”值";
		}
		return null;
	}

	@Override
	public String getDataType() {
		return "varchar2";
	}

	@Override
	public int getLength() {
		return 10;
	}

	@Override
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		this.length = getLength();
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new DateAttribute();
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
