package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.BooleanAttribute;

/**
 * ��������Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class BooleanAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ���ǡ�������
	 */
	private String yes = "��";

	/**
	 * ���񡱵�����
	 */
	private String no = "��";

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
	 * ��ȡ���ǡ�������
	 * 
	 * @return ���ǡ�������
	 * @see #yes
	 */
	public String getYes() {
		return this.yes;
	}

	/**
	 * ���á��ǡ�������
	 * 
	 * @param yes
	 *            ���ǡ�������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #yes
	 */
	public void setYes(String yes) throws CacheException {
		checkSet();
		this.yes = yes;
	}

	/**
	 * ��ȡ���񡱵�����
	 * 
	 * @return ���񡱵�����
	 * @see #no
	 */
	public String getNo() {
		return this.no;
	}

	/**
	 * ���á��񡱵�����
	 * 
	 * @param no
	 *            ���񡱵�����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
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
			throw new CacheException("������ʽ����: " + this.getId() + "/"
					+ this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (this.yes.equals(value) || this.no.equals(value))
			return null;
		return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
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
