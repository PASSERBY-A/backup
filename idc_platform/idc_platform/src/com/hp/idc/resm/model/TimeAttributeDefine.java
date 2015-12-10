package com.hp.idc.resm.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.TimeAttribute;

/**
 * ʱ������Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class TimeAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 551210213336872369L;
	
	/**
	 * ����������
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "������", "$0 != $1"),
		new AttributeOperator("==", "����", "$0 == $1"),
		new AttributeOperator("isnull", "Ϊ��", "$null.isNull($0)"),
		new AttributeOperator("notnull", "��Ϊ��", "$null.isNotNull($0)")
	};
	
	/**
	 * ��������
	 */
	private static AttributeConst[] CONSTS = {
		new AttributeStringConst("true", "��", "true"),
		new AttributeStringConst("false", "��", "false")
	};

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		String format = TimeAttribute.TIME_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			sdf.parse(value);
		} catch (ParseException e) {
			return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
		}
		return null;
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
		return new TimeAttribute();
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
