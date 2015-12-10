package com.hp.idc.itsm.configure;

import com.hp.idc.itsm.common.IObjectWithAttribute;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.inter.FieldManagerInterface;

/**
 * 视图过滤器
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class FilterInfo {

	protected String fieldId;
	
	//工单系统
	protected String origin;

	protected String relation;

	protected int operationCode;

	protected String value;
	
	

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
		operationCode = OperationCode.parse(relation);
	}

	public String getValue() {
		return this.value;

	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean match(String val, String operName) {
//		String val2 = MacroManager.replaceMacro(this.value, operName);
		try {
//			if (operationCode == OperationCode.MATCH)
//				return Pattern.matches(this.value, val);
//			if (operationCode == OperationCode.NOT_MATCH)
//				return !Pattern.matches(this.value, val);
			FieldManagerInterface fmi = FieldManager.getClassInstance(origin);
			FieldInfo field = fmi.getFieldById(this.fieldId,false);
			if (field == null)
				return true;
			boolean result = field.match(operationCode, val, this.value);

			boolean r = field.match(operationCode,val,field.getHtmlCode(this.value));

			return result || r;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public boolean match(IObjectWithAttribute obj, String operName) {
		String value = obj.getAttribute(this.fieldId);
		if (value == null)
			value = "";
		return match(value, operName);
	}

	public String getOrigin() {
		return origin == null || origin.equals("") ?"ITSM":origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public FilterInfo cloneFilterInfo(){
		FilterInfo f = new FilterInfo();
		f.setFieldId(this.fieldId);
		f.setOrigin(this.origin);
		f.setRelation(this.relation);
		f.setValue(this.value);
		return f;
	}

}
