package com.cmsz.cloudplatform.web.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.cmsz.cloudplatform.model.response.Response;

public class ServiceOfferingsAction extends BaseAction {

	private static final long serialVersionUID = 286524240364386504L;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	public String listServiceOfferings() {

		Object[] commandObj = this.requestParams.get("command");

		if (commandObj != null) {
			Response response = null;
			if ("GET".equalsIgnoreCase(this.request.getMethod())) {
				response = this.genericCloudServerManager.get(requestParams);
			} else {
				response = this.genericCloudServerManager.post(requestParams);
			}
			JSONObject jo = JSONObject.fromObject(response.getResponseString());
			jo = JSONObject.fromObject(jo.get("listserviceofferingsresponse"));
			try {
				jo.get("errorcode");
			} catch (Exception e) {
				log.error(jo.toString());
				return NONE;
			}
			this.writeResponse(response);

		} else {
			// TODO error

		}

		return NONE;
	}

	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

}
