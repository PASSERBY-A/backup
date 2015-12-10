package com.hp.idc.resm.resource;

import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * ��ʾ�������͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ReferenceAttribute extends AttributeBase {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -6994314835229490486L;

	/**
	 * ����ֵ
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
	 * ��ȡ����ֵ
	 * 
	 * @return ����ֵ
	 * @see #value
	 */
	public Integer getValue() {
		return this.value;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param value
	 *            ����ֵ
	 * @see #value
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public void setText(String text) {
		this.text = (text == null?"":text);
		// TODO �˴��߼���Ҫ����
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
	 * ��ȡ������Դ
	 * @return ������Դ
	 */
	public ResourceObject getValueOfResouceObject(){
		if (this.value == null)
			return null;
		return ServiceManager.getResourceService().getResourceById(
				this.value.intValue());
	}
}
