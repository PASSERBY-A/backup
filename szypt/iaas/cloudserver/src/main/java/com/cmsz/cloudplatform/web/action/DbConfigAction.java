package com.cmsz.cloudplatform.web.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.config.model.DbConfig;
import com.hp.config.service.DbConfigManager;

public class DbConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1143427426418106590L;
	private String configKey = "";
	
	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	@Autowired
	private DbConfigManager dbconfigManager;
	
	public String listConfig() {
		DbConfig example = new DbConfig();
		example.setKey(configKey);
		List<DbConfig> configs = (List<DbConfig>) dbconfigManager.findByExample(example);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("configList", configs);
		this.renderText(jsonObj.toString());
		return NONE;
	}
}
