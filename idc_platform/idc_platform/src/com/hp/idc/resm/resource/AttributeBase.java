package com.hp.idc.resm.resource;

import java.io.Serializable;

import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.CheckerUtil;

/**
 * ��Դ�ֶ�(����)
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class AttributeBase implements Serializable, Cloneable {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 9004281135107564198L;

	/**
	 * �ֶι�����ģ������
	 */
	private ModelAttribute attribute;
	
	/**
	 * �ֶε���ʾֵ�����л�ʹ��
	 */
	protected String text;

	/**
	 * ��ȡ�ֶε���ʾֵ
	 * 
	 * @return �ֶε���ʾֵ
	 */
	public String getText() {
		// TODO ������, text��valueȡ�Ļ���һ��ֵ. ���Ƕ���select��tree�ͽṹ, ��Ҫ�ֿ�ȡֵ.��δ��, Ԥ������
		Object obj = getValueAsObject();
		if (obj != null)
			return getValueAsObject().toString();
		return null;
	}

	/**
	 * ��������ֵ��������������д�˺���
	 * 
	 * @param value
	 *            ����ֵ
	 * @throws Exception
	 *             ��������ȷʱ����
	 */
	public abstract void setText(String value) throws Exception;
	
	
	/**
	 * ��ȡ�ֶ�ֵ��������������д�˺���
	 * 
	 * @return �ֶ�ֵ
	 */
	public abstract Object getValueAsObject();


	
	/**
	 * ���л�
	 * @return ���
	 * 
	 */
	public String serialize() {
		return getText();
	}

	/**
	 * ��ȡ����
	 * 
	 * @param value
	 *            ����ֵ
	 * @throws Exception
	 *             ��������ȷʱ����
	 */
	public void deserialize(String value) throws Exception {
		setText(value);
	}

	/**
	 * �ֶ��Ƿ�Ϊ�գ�������������д�˺���
	 * 
	 * @return true��ʾΪ�գ�false�򲻿�
	 */
	public abstract boolean isNull();



	/**
	 * �����ֶ��Ƿ�Ϸ�
	 * 
	 * @return �Ƿ���ԭ���������null�����ʾ����ͨ��
	 */
	public String isValid() {
		if (isNull()) {
			if (!this.attribute.isNullable())
				return "��" + this.attribute.getName() + "������Ϊ��";
			return null;
		} else {
			if (this.attribute.getDefine().getChecker() != null) {
				return CheckerUtil.isValid(this.attribute.getName()+";"+this.attribute.getDefine().getChecker(),
						getValueAsObject());
			}
		}
		return null;
	}
	
	/**
	 * �����ֶ��Ƿ�Ϸ�
	 * 
	 * @return �Ƿ���ԭ���������null�����ʾ����ͨ��
	 */
	public String isValid(ResourceObject resourceObject) {
		String valid = this.isValid();
		if (valid != null) {
			return valid;
		}
		String condition = this.attribute.getCondition();//ֻ֧��>{222}����>2�����ĸ�ʽ
		if (condition == null || condition.length() == 0) {
			return null;
		}
		if(condition.indexOf("{") > -1 && condition.indexOf("}") > -1){
			String attrId = condition.substring(2, condition.lastIndexOf("}"));
			return this.attribute.getDefine().valid(this.getText(), resourceObject.getAttributeValue(attrId), attrId, condition.substring(0,1));
		} else {
			return this.attribute.getDefine().valid(this.getText(), condition.substring(1), "", condition.substring(0,1));
		}
	}

	/**
	 * ���ù�����ģ������
	 * 
	 * @param attribute
	 *            ������ģ������
	 */
	public void setAttribute(ModelAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * ��ȡ������ģ������
	 * 
	 * @return ������ģ������
	 */
	public ModelAttribute getAttribute() {
		return this.attribute;
	} 
	
	/**
	 * ��������
	 */
	public abstract AttributeBase clone();
}
