package com.cmsz.cloudplatform.service.schedule;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.SAManager;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.service.impl.SAAsyncHelper;
import com.cmsz.cloudplatform.service.impl.SACapabilityHelper;
import com.cmsz.cloudplatform.utils.ServiceOptionUtil;

@Service(value = "saSchedule")
public class SASchedule {

	private final Logger log = LoggerFactory
			.getLogger(SASchedule.class);

	public void work() {

		log.info("-----------------------------------服务开通  begin--------------------------------------------------------");

		 active();
		log.info("-----------------------------------服务开通  end--------------------------------------------------------");
		;

	}

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	@Autowired
	private SAAsyncHelper saAsyncHelper;
	@Autowired
	private SACapabilityHelper saCapabilityHelper;

	public SACapabilityHelper getSaCapabilityHelper() {
		return saCapabilityHelper;
	}

	public void setSaCapabilityHelper(SACapabilityHelper saCapabilityHelper) {
		this.saCapabilityHelper = saCapabilityHelper;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SASchedule saSchedule = (SASchedule) applicationContext
				.getBean("saSchedule");
		saSchedule.active();
	}

	private Response loginCloudStack() {
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("command", new Object[] { "login" });
		param.put("username",
				new Object[] { ServiceOptionUtil.obtainCloudStackUsername() });
		param.put("password",
				new Object[] { ServiceOptionUtil.obtainCloudStackPassword() });
		param.put("response", new Object[] { "json" });
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;

	}

	public void active() {

		Response loginResponse = loginCloudStack();

		Map<String, Object[]> param = new HashMap<String, Object[]>();

		System.out.println("here is the response " + loginResponse.toString());
	
		JSONObject jo = JSONObject
				.fromObject(loginResponse.getResponseString());
		String userId = "";
		String sessionkey = "";
		try {
			jo = JSONObject.fromObject(jo.getString("loginresponse"));
			userId = jo.getString("userid");
			sessionkey = jo.getString("sessionkey");
		} catch (JSONException e) {
			log.error("login info is error, "
					+ loginResponse.getResponseString());
			return;
		}

		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("id", new Object[] { userId });

		Response listUsersResponse = genericCloudServerManager.get(
				listUsersParams, false);

		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			jo = JSONObject.fromObject(listUsersResponse.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
		Map<String, Object[]> cloudStackParams = new HashMap<String, Object[]>();
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("sessionkey", new Object[] { sessionkey });
		cloudStackParams.put("response", new Object[] { "json" });

		saCapabilityHelper.active(cloudStackParams);
		saAsyncHelper.active(cloudStackParams);

	}

}
