package com.volkswagen.tel.billing.billcall.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.common.GenericConfig;
import com.volkswagen.tel.billing.common.UIConfiguration;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Service("uiConfigurationService")
@Path("/uiConfigurationService")
public class UIConfigurationService {
	private static final Logger log = LoggerFactory
			.getLogger(UIConfigurationService.class);

	@Context
	HttpServletRequest request;
	
	@GET
	@Path("getUIConfigurationJson")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUIConfigurationJson() {
		log.info("---------- getUIConfigurationJson strart.");

		String userId = "abc"; // - got from session
		JSONObject jObj = null;

		jObj = new JSONObject();
		UIConfiguration config = UIConfiguration.getInstance();
		jObj.put("configContent", config);

		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		log.info("---------- getUIConfigurationJson end.");
		return jObj;
	}
	
	@GET
	@Path("getUIConfiguration")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUIConfiguration() {
		log.info("---------- getUIConfiguration strart.");

		String userId = "abc"; // - got from session
		JSONObject jObj = null;

		jObj = new JSONObject();
		UIConfiguration uicInstance = UIConfiguration.getInstance();
		String configContent = CommonUtil.prettyXMLFormat(uicInstance.convertToXML(), 4);
		jObj.put("configContent", configContent);

		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		log.info("---------- getUIConfiguration end.");
		return jObj;
	}

	@POST
	@Path("updateUIConfiguration")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updateUIConfiguration(
			@FormParam("configContent") String configContent) {
		log.info("---------- updateUIConfiguration strart.");
		JSONObject jObj = new JSONObject();
		
		UIConfiguration.populateValueViaXML(configContent);
		
		String webAppPath = request.getSession().getServletContext().getRealPath("/");
		String uiConfigFilePath = webAppPath.concat("/config/ui-config.properties");
		log.info(">>> update ui-config file: "+uiConfigFilePath);
		CommonUtil.writeToFile(uiConfigFilePath, configContent);
		
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");
		
		log.info("---------- updateUIConfiguration end.");
		return jObj;
	}
	
	@POST
	@Path("updateSapSubmitConfig")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updateSapSubmitConfig(
			@FormParam("sapSubmitFlag") String sapSubmitFlag,
			@FormParam("sapSubmitDirectory") String sapSubmitDirectory) {
		log.info("---------- updateSapSubmitConfig strart.");
		JSONObject jObj = new JSONObject();
		
		String returnMsg = "";
		if ("YES".equals(sapSubmitFlag)) {
			//GenericConfig.enableSapSubmit = true;
			GenericConfig.write("system.enablesap", "true");
			
		} else {
			//GenericConfig.enableSapSubmit = false;  //write sysConfig.properties 
			GenericConfig.write("system.enablesap", "false");
			
		}
		
		//GenericConfig.sapSubmitDirectory = sapSubmitDirectory;
		GenericConfig.write("system.sapsubmitdirectory", sapSubmitDirectory);
		
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "Configuration saved successfully.");
		
		log.info("---------- updateSapSubmitConfig end.");
		return jObj;
	}
}
