package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.expression.IAttributeExpression;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ExpressionAttribute;

/**
 * ���ʽ����Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class ExpressionAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -1297463266733517714L;

	/**
	 * ���ʽ����
	 */
	private IAttributeExpression expression = null;

	/**
	 * ���ñ��ʽ����
	 * 
	 * @param expression
	 *            ���õ�ģ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #expression
	 */
	public void setExpression(IAttributeExpression expression) throws CacheException {
		checkSet();
		this.expression = expression;
	}

	/**
	 * ��ȡ���ʽ����
	 * 
	 * @return ���ʽ����
	 * @see #expression
	 */
	public IAttributeExpression getExpression() {
		return this.expression;
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		super.setArguments(arguments);
		try {
			JSONObject json = new JSONObject(arguments);
			String className = json.getString("class");
			IAttributeExpression expr = (IAttributeExpression)Class.forName(className).newInstance();
			setExpression(expr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("������ʽ����: " + this.getId() + "/" + this.getName());
		}
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
		return new ExpressionAttribute();
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
