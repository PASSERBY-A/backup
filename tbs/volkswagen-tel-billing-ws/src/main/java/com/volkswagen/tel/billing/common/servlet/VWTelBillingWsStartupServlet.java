package com.volkswagen.tel.billing.common.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.volkswagen.tel.billing.common.UIConfiguration;
import com.volkswagen.tel.billing.common.util.CommonUtil;

public class VWTelBillingWsStartupServlet extends HttpServlet {
	private static final long serialVersionUID = -1L;
	
	private static final Logger log = LoggerFactory.getLogger(VWTelBillingWsStartupServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		loadUIConfig(config); 
		
	}

	private void loadUIConfig(ServletConfig config) {
		
		
		
		String webAppPath = config.getServletContext().getRealPath("/");

		String xmlContent = null;

		String uiConfigFilePath = webAppPath.concat("/config/ui-config.properties");
		
		log.info(">>> Server TempDirectoryPath: "+FileUtils.getTempDirectoryPath());
		
		log.info(">>> Reading ui-config file: " + uiConfigFilePath);
		
		xmlContent = CommonUtil.readFromFile(uiConfigFilePath);

		UIConfiguration.populateValueViaXML(xmlContent);
		
	}
	
	public static void main(String[] args) throws Exception {
		VWTelBillingWsStartupServlet svl = new VWTelBillingWsStartupServlet();
		svl.init(null);
	}
}
