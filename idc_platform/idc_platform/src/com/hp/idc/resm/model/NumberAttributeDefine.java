package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.NumberAttribute;
import com.hp.idc.resm.util.StringUtil;

/**
 * ��ֵ����Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class NumberAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 2176562464928055087L;

	/**
	 * ����������
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "������", "$0 != $1"),
		new AttributeOperator("==", "����", "$0 == $1"),
		new AttributeOperator(">", "����", "$0 > $1"),
		new AttributeOperator("<", "С��", "$0 < $1"),
		new AttributeOperator(">=", "���ڵ���", "$0 >= $1"),
		new AttributeOperator("<=", "С�ڵ���", "$0 <= $1"),
		new AttributeOperator("isnull", "Ϊ��", "$null.isNull($0)"),
		new AttributeOperator("notnull", "��Ϊ��", "$null.isNotNull($0)")
	};
	
	/**
	 * ��������
	 */
	private static AttributeConst[] CONSTS = {
	};
	
	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		int t = StringUtil.checkNumber(value);
		if (t != StringUtil.TYPE_INTEGER && t != StringUtil.TYPE_DOUBLE)
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
		return new NumberAttribute();
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
