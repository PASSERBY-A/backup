package com.hp.idc.resm.resource;

import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * 表示引用类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ReferenceAttribute extends AttributeBase {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -6994314835229490486L;

	/**
	 * 数据值
	 */
	protected Integer value = null;

	@Override
	public String getText() {
//		if (this.value == null)
//			return "";
//		ResourceObject obj = ServiceManager.getResourceService()
//				.getResourceById(this.value.intValue());
//		if (obj == null)
//			return "";
//		return obj.getName();
		
		if (!text.matches("\\d*"))
			return "";
		return this.value.toString();
	}

//	@Override
//	public ResourceObject getValueAsObject() {
//		if (this.value == null)
//			return null;
//		return ServiceManager.getResourceService().getResourceById(
//				this.value.intValue());
//	}
	@Override
	public Integer getValueAsObject() {
		if (this.value == null)
			return null;
		return getValue();
	}

	/**
	 * 获取数据值
	 * 
	 * @return 数据值
	 * @see #value
	 */
	public Integer getValue() {
		return this.value;
	}

	/**
	 * 设置数据值
	 * 
	 * @param value
	 *            数据值
	 * @see #value
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public void setText(String text) {
		this.text = (text == null?"":text);
		// TODO 此处逻辑需要完善
		this.value = StringUtil.parseInt(text, 0);
	}

	@Override
	public boolean isNull() {
		return (this.value == null);
	}

	@Override
	public String serialize() {
		if (this.value == null)
			return "";
		return "" + this.value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReferenceAttribute))
			return false;
		ReferenceAttribute a = (ReferenceAttribute) obj;
		if (!this.getAttribute().getAttrId().equals(
				a.getAttribute().getAttrId()))
			return false;

		if (this.value == null) {
			if (a.value == null)
				return true;
			return false;
		} else if (a.value == null)
			return false;
		return (this.value.compareTo(a.value) == 0);
	}

	@Override
	public AttributeBase clone() {
		ReferenceAttribute attr = new ReferenceAttribute();
		attr.setAttribute(this.getAttribute());
		attr.setText(this.getText());
		return attr;
	}
	
	/**
	 * 获取关联资源
	 * @return 关联资源
	 */
	public ResourceObject getValueOfResouceObject(){
		if (this.value == null)
			return null;
		return ServiceManager.getResourceService().getResourceById(
				this.value.intValue());
	}
}
