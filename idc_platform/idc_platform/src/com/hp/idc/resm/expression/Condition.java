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
 * 条件
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Condition implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -4202995920794895691L;

	/**
	 * 模型id
	 */
	private String modelId;

	/**
	 * 属性id
	 */
	private String attrId;

	/**
	 * 操作符
	 */
	private String op;

	/**
	 * 比较对象
	 */
	private String value;

	/**
	 * 括号层数
	 */
	private int comboLevel;

	/**
	 * 组合类型，0=and, 1=or
	 */
	private int comboType;

	/**
	 * 获取模型id
	 * 
	 * @return 模型id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * 设置模型id
	 * 
	 * @param modelId
	 *            模型id
	 * @see #modelId
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * 获取属性id
	 * 
	 * @return 属性id
	 * @see #attrId
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * 设置属性id
	 * 
	 * @param attrId
	 *            属性id
	 * @see #attrId
	 */
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	/**
	 * 获取操作符
	 * 
	 * @return 操作符
	 * @see #op
	 */
	public String getOp() {
		return this.op;
	}

	/**
	 * 设置操作符
	 * 
	 * @param op
	 *            操作符
	 * @see #op
	 */
	public void setOp(String op) {
		this.op = op;
	}

	/**
	 * 获取比较对象
	 * 
	 * @return 比较对象
	 * @see #value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 设置比较对象
	 * 
	 * @param value
	 *            比较对象
	 * @see #value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取括号层数
	 * 
	 * @return 括号层数
	 * @see #comboLevel
	 */
	public int getComboLevel() {
		return this.comboLevel;
	}

	/**
	 * 设置括号层数
	 * 
	 * @param comboLevel
	 *            括号层数
	 * @see #comboLevel
	 */
	public void setComboLevel(int comboLevel) {
		this.comboLevel = comboLevel;
	}

	/**
	 * 获取组合类型
	 * 
	 * @return 组合类型，0=and, 1=or
	 * @see #comboType
	 */
	public int getComboType() {
		return this.comboType;
	}

	/**
	 * 设置组合类型
	 * 
	 * @param comboType
	 *            组合类型，0=and, 1=or
	 * @see #comboType
	 */
	public void setComboType(int comboType) {
		this.comboType = comboType;
	}

	/**
	 * 判断比较对象是不是常量
	 * 
	 * @return true=是常量，false=不是常量
	 */
	public boolean isConstValue() {
		return (this.value != null) && this.value.startsWith("$")
				&& !this.value.startsWith("$$");
	}

	/**
	 * 获取比较对象值
	 * @param ad 相关的属性
	 * @return 比较对象值
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
