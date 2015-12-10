package com.hp.idc.resm.resource;

import java.io.Serializable;

import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.CheckerUtil;

/**
 * 资源字段(基类)
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class AttributeBase implements Serializable, Cloneable {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 9004281135107564198L;

	/**
	 * 字段关联的模型属性
	 */
	private ModelAttribute attribute;
	
	/**
	 * 字段的显示值，序列化使用
	 */
	protected String text;

	/**
	 * 获取字段的显示值
	 * 
	 * @return 字段的显示值
	 */
	public String getText() {
		// TODO 在这里, text和value取的还是一个值. 但是对于select和tree型结构, 需要分开取值.暂未做, 预留下来
		Object obj = getValueAsObject();
		if (obj != null)
			return getValueAsObject().toString();
		return null;
	}

	/**
	 * 设置属性值，在派生类中重写此函数
	 * 
	 * @param value
	 *            属性值
	 * @throws Exception
	 *             参数不正确时发生
	 */
	public abstract void setText(String value) throws Exception;
	
	
	/**
	 * 获取字段值，在派生类中重写此函数
	 * 
	 * @return 字段值
	 */
	public abstract Object getValueAsObject();


	
	/**
	 * 序列化
	 * @return 结果
	 * 
	 */
	public String serialize() {
		return getText();
	}

	/**
	 * 读取数据
	 * 
	 * @param value
	 *            数据值
	 * @throws Exception
	 *             参数不正确时发生
	 */
	public void deserialize(String value) throws Exception {
		setText(value);
	}

	/**
	 * 字段是否为空，在派生类中重写此函数
	 * 
	 * @return true表示为空，false则不空
	 */
	public abstract boolean isNull();



	/**
	 * 较验字段是否合法
	 * 
	 * @return 非法的原因。如果返回null，则表示较验通过
	 */
	public String isValid() {
		if (isNull()) {
			if (!this.attribute.isNullable())
				return "“" + this.attribute.getName() + "”不能为空";
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
	 * 较验字段是否合法
	 * 
	 * @return 非法的原因。如果返回null，则表示较验通过
	 */
	public String isValid(ResourceObject resourceObject) {
		String valid = this.isValid();
		if (valid != null) {
			return valid;
		}
		String condition = this.attribute.getCondition();//只支持>{222}或者>2这样的格式
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
	 * 设置关联的模型属性
	 * 
	 * @param attribute
	 *            关联的模型属性
	 */
	public void setAttribute(ModelAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * 获取关联的模型属性
	 * 
	 * @return 关联的模型属性
	 */
	public ModelAttribute getAttribute() {
		return this.attribute;
	} 
	
	/**
	 * 复制自身
	 */
	public abstract AttributeBase clone();
}
