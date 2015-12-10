package com.volkswagen.tel.billing.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIConfiguration {
	private static final Logger log = LoggerFactory
	.getLogger(UIConfiguration.class);
	
	private static UIConfiguration instance = new UIConfiguration();
	private String searchButtonText = "Search";
	private String saveAndPrintButtonText = "Go to summary";

	private String titleColumn1 = "Date";
	private String titleColumn2 = "Starting Time";
	private String titleColumn3 = "Duration";
	private String titleColumn4 = "Called Number";
	private String titleColumn5 = "Type";
	private String titleColumn6 = "Location";
	private String titleColumn7 = "Amount";
	private String titleColumn8 = "Private";

	public String getSearchButtonText() {
		return searchButtonText;
	}

	public void setSearchButtonText(String searchButtonText) {
		this.searchButtonText = searchButtonText;
	}

	public String getSaveAndPrintButtonText() {
		return saveAndPrintButtonText;
	}

	public void setSaveAndPrintButtonText(String saveAndPrintButtonText) {
		this.saveAndPrintButtonText = saveAndPrintButtonText;
	}

	public String getTitleColumn1() {
		return titleColumn1;
	}

	public void setTitleColumn1(String titleColumn1) {
		this.titleColumn1 = titleColumn1;
	}

	public String getTitleColumn2() {
		return titleColumn2;
	}

	public void setTitleColumn2(String titleColumn2) {
		this.titleColumn2 = titleColumn2;
	}

	public String getTitleColumn3() {
		return titleColumn3;
	}

	public void setTitleColumn3(String titleColumn3) {
		this.titleColumn3 = titleColumn3;
	}

	public String getTitleColumn4() {
		return titleColumn4;
	}

	public void setTitleColumn4(String titleColumn4) {
		this.titleColumn4 = titleColumn4;
	}

	public String getTitleColumn5() {
		return titleColumn5;
	}

	public void setTitleColumn5(String titleColumn5) {
		this.titleColumn5 = titleColumn5;
	}

	public String getTitleColumn6() {
		return titleColumn6;
	}

	public void setTitleColumn6(String titleColumn6) {
		this.titleColumn6 = titleColumn6;
	}

	public String getTitleColumn7() {
		return titleColumn7;
	}

	public void setTitleColumn7(String titleColumn7) {
		this.titleColumn7 = titleColumn7;
	}

	public String getTitleColumn8() {
		return titleColumn8;
	}

	public void setTitleColumn8(String titleColumn8) {
		this.titleColumn8 = titleColumn8;
	}

	public static UIConfiguration getInstance() {
		return instance;
	}
	
	public static void populateValueViaXML(String xmlContent) {
		if (xmlContent == null || xmlContent.trim().length() <= 0) {
			return;
		}

		org.json.JSONObject jObj = org.json.XML.toJSONObject(xmlContent);
		org.json.JSONObject rootObj = jObj.getJSONObject("root");
		log.info(">>> " + rootObj);
		
		instance.setSearchButtonText(rootObj.getString("searchButtonText"));
		instance.setSaveAndPrintButtonText(rootObj.getString("saveAndPrintButtonText"));
		instance.setTitleColumn1(rootObj.getString("titleColumn1"));
		instance.setTitleColumn2(rootObj.getString("titleColumn2"));
		instance.setTitleColumn3(rootObj.getString("titleColumn3"));
		instance.setTitleColumn4(rootObj.getString("titleColumn4"));
		instance.setTitleColumn5(rootObj.getString("titleColumn5"));
		instance.setTitleColumn6(rootObj.getString("titleColumn6"));
		instance.setTitleColumn7(rootObj.getString("titleColumn7"));
		instance.setTitleColumn8(rootObj.getString("titleColumn8"));
	}
	
	public static String convertToXML() {
		org.json.JSONObject rootObj = new org.json.JSONObject();
		rootObj.append("searchButtonText", instance.getSearchButtonText());
		rootObj.append("saveAndPrintButtonText", instance.getSaveAndPrintButtonText());
		rootObj.append("titleColumn1", instance.getTitleColumn1());
		rootObj.append("titleColumn2", instance.getTitleColumn2());
		rootObj.append("titleColumn3", instance.getTitleColumn3());
		rootObj.append("titleColumn4", instance.getTitleColumn4());
		rootObj.append("titleColumn5", instance.getTitleColumn5());
		rootObj.append("titleColumn6", instance.getTitleColumn6());
		rootObj.append("titleColumn7", instance.getTitleColumn7());
		rootObj.append("titleColumn8", instance.getTitleColumn8());
				
		String xmlStr = org.json.XML.toString(rootObj, "root");
		return xmlStr;
	}
	
	public static void main(String[] args) {
		String str = UIConfiguration.convertToXML();
		log.info(">>> " + str);
	}
}
