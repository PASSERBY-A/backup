package com.hp.idc.resm.resource;

import com.hp.idc.resm.model.ExpressionAttributeDefine;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 表示字符类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ExpressionAttribute extends AttributeBase {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;
	
	/**
	 * 数据值，供序列化使用
	 */
	@SuppressWarnings("unused")
	private Object value;
	
	/**
	 * 关联资源对象id
	 */
	private int resId;

	/**
	 * 获取关联资源对象id
	 * @return 关联资源对象id
	 * @see #resId
	 */
	public int getResId() {
		return this.resId;
	}

	/**
	 * 设置关联资源对象id
	 * @param resId 关联资源对象id
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
	 * 获取数据值
	 * @return 数据值 
	 * @see #value
	 */
	public Object getValue() {
		return getValueAsObject();
	}

	/**
	 * 设置数据值
	 * @param value 数据值
	 * @see #value
	 */
	public void setValue(Object value) {
		// 表达式值是计算得来的，不支持的手动设置
	}

	@Override
	public void setText(String value) {
		// 表达式值是计算得来的，不支持的手动设置
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
