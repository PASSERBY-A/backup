/**
 * 
 */
package com.hp.idc.resm.expression;

import java.io.Serializable;

import com.hp.idc.resm.model.AttributeConst;
import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeOperator;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.service.ServiceManager;

/**
 * ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Condition implements Serializable {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = -4202995920794895691L;

	/**
	 * ģ��id
	 */
	private String modelId;

	/**
	 * ����id
	 */
	private String attrId;

	/**
	 * ������
	 */
	private String op;

	/**
	 * �Ƚ϶���
	 */
	private String value;

	/**
	 * ���Ų���
	 */
	private int comboLevel;

	/**
	 * ������ͣ�0=and, 1=or
	 */
	private int comboType;

	/**
	 * ��ȡģ��id
	 * 
	 * @return ģ��id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * ����ģ��id
	 * 
	 * @param modelId
	 *            ģ��id
	 * @see #modelId
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * ��ȡ����id
	 * 
	 * @return ����id
	 * @see #attrId
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * ��������id
	 * 
	 * @param attrId
	 *            ����id
	 * @see #attrId
	 */
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 * @see #op
	 */
	public String getOp() {
		return this.op;
	}

	/**
	 * ���ò�����
	 * 
	 * @param op
	 *            ������
	 * @see #op
	 */
	public void setOp(String op) {
		this.op = op;
	}

	/**
	 * ��ȡ�Ƚ϶���
	 * 
	 * @return �Ƚ϶���
	 * @see #value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * ���ñȽ϶���
	 * 
	 * @param value
	 *            �Ƚ϶���
	 * @see #value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * ��ȡ���Ų���
	 * 
	 * @return ���Ų���
	 * @see #comboLevel
	 */
	public int getComboLevel() {
		return this.comboLevel;
	}

	/**
	 * �������Ų���
	 * 
	 * @param comboLevel
	 *            ���Ų���
	 * @see #comboLevel
	 */
	public void setComboLevel(int comboLevel) {
		this.comboLevel = comboLevel;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return ������ͣ�0=and, 1=or
	 * @see #comboType
	 */
	public int getComboType() {
		return this.comboType;
	}

	/**
	 * �����������
	 * 
	 * @param comboType
	 *            ������ͣ�0=and, 1=or
	 * @see #comboType
	 */
	public void setComboType(int comboType) {
		this.comboType = comboType;
	}

	/**
	 * �жϱȽ϶����ǲ��ǳ���
	 * 
	 * @return true=�ǳ�����false=���ǳ���
	 */
	public boolean isConstValue() {
		return (this.value != null) && this.value.startsWith("$")
				&& !this.value.startsWith("$$");
	}

	/**
	 * ��ȡ�Ƚ϶���ֵ
	 * @param ad ��ص�����
	 * @return �Ƚ϶���ֵ
	 */
	public String getOpValue(AttributeDefine ad) {
		if (isConstValue()) {
			AttributeConst c = ad.getConst(this.value.substring(1));
			return c.getValue();
		}
		if (this.value.startsWith("$$"))
			return this.value.substring(1);
		return this.value;
	}
	
	@Override
	public String toString() {
		Model m = ServiceManager.getModelService().getModelById(this.modelId);
		ModelAttribute ma = m.getAttributeById(this.attrId);
		AttributeDefine ad = ServiceManager.getAttributeService()
				.getAttributeById(this.attrId);
		AttributeOperator ao = ad.getOperator(this.op);
		String val = "";
		if (ao.isNeedOpValue()) {
			if (isConstValue()) {
				AttributeConst c = ad.getConst(this.value.substring(1));
				val = c.getName();
			} else if (this.value.startsWith("$$"))
				val = this.value.substring(1);
			else
				val = this.value;
		}
		return ma.getName() + ao.getOpName() + val;
	}
}
