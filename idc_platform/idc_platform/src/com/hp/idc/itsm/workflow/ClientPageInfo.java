/**
 * 
 */
package com.hp.idc.itsm.workflow;

import org.dom4j.Element;

import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class ClientPageInfo {

	/**
	 * 展示页面模式（远程模式）
	 */
	public static final String MODE_REMOTE = "remote";
	
	/**
	 * 展示页面模式（本地模式）
	 */
	public static final String MODE_LOCAL = "local";

	/**
	 * 展示页面模式
	 */
	private String mode;
	
	/**
	 * 远程展示页面（当mode=ClientPageInfo.MODE_REMOTE时有效）
	 */
	private String remotePage;
	
	/**
	 * 本地表单对象（当mode=ClientPageInfo.MODE_LOCAL时有效）
	 */
	private FormInfo localForm;
	
	/**
	 * 本地表单展示模板（当mode=ClientPageInfo.MODE_LOCAL时有效）
	 */
	private String localTemplate;
	
	public void parse(Element node){
		 mode = node.attributeValue("mode");
		 Element el = (Element)node.selectSingleNode("./local");
		 String localFormId = el.attributeValue("fromId");
		 localForm = FormManager.getFormByOid(Integer.parseInt(localFormId));
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRemotePage() {
		return remotePage;
	}

	public void setRemotePage(String remotePage) {
		this.remotePage = remotePage;
	}

	public FormInfo getLocalForm() {
		return localForm;
	}

	public void setLocalForm(FormInfo localForm) {
		this.localForm = localForm;
	}

	public String getLocalTemplate() {
		return localTemplate;
	}

	public void setLocalTemplate(String localTemplate) {
		this.localTemplate = localTemplate;
	}

}
