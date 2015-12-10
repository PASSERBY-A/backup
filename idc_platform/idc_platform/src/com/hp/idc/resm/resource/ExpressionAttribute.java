package com.hp.idc.resm.resource;

import com.hp.idc.resm.model.ExpressionAttributeDefine;
import com.hp.idc.resm.service.ServiceManager;

/**
 * ��ʾ�ַ����͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ExpressionAttribute extends AttributeBase {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;
	
	/**
	 * ����ֵ�������л�ʹ��
	 */
	@SuppressWarnings("unused")
	private Object value;
	
	/**
	 * ������Դ����id
	 */
	private int resId;

	/**
	 * ��ȡ������Դ����id
	 * @return ������Դ����id
	 * @see #resId
	 */
	public int getResId() {
		return this.resId;
	}

	/**
	 * ���ù�����Դ����id
	 * @param resId ������Դ����id
	 * @see #resId
	 */
	public void setResId(int resId) {
		this.resId = resId;
	}

	@Override
	public Object getValueAsObject() {
		ExpressionAttributeDefine ad = (ExpressionAttributeDefine) this
				.getAttribute().getDefine();
		ResourceObject res = ServiceManager.getResourceService().getResourceById(this.resId);
		Object obj = ad.getExpression().getAttribute(res);
		if (obj == null)
			return null;
		if (obj instanceof AttributeBase)
			return ((AttributeBase) obj).getValueAsObject();
		return obj;
	}
	
	@Override
	public String getText() {
		Object obj = getValueAsObject();
		if (obj == null)
			return null;
		if (obj instanceof ResourceObject)
			return ((ResourceObject) obj).getName();
		return obj.toString();
	}

	/**
	 * ��ȡ����ֵ
	 * @return ����ֵ 
	 * @see #value
	 */
	public Object getValue() {
		return getValueAsObject();
	}

	/**
	 * ��������ֵ
	 * @param value ����ֵ
	 * @see #value
	 */
	public void setValue(Object value) {
		// ���ʽֵ�Ǽ�������ģ���֧�ֵ��ֶ�����
	}

	@Override
	public void setText(String value) {
		// ���ʽֵ�Ǽ�������ģ���֧�ֵ��ֶ�����
	}

	@Override
	public boolean isNull() {
		String text1 = getText();
		return (text1 == null) || (text1.length() == 0);
	}

	@Override
	public AttributeBase clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
