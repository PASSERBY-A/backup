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
 * ��������Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class DateAttributeDefine extends AttributeDefine {

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
	
	/**
	 * ���л�UID
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
			return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
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
				return "���ԡ�" + this.getName() + "��ֵӦ����"+value1;
			}
			return "���ԡ�" + this.getName() + "��ֵӦ�������ԡ�"+ad.getName()+"��ֵ";
		}
		if (op.equals("<") && d.after(d1)) {
			AttributeDefine ad = ServiceManager.getAttributeService().getAttributeById(attrId);
			if (ad == null) {
				return "���ԡ�" + this.getName() + "��ֵӦС��"+value1;
			}
			return "���ԡ�" + this.getName() + "��ֵӦС�����ԡ�"+ad.getName()+"��ֵ";
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
