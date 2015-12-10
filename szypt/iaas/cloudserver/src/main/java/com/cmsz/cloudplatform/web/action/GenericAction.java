package com.cmsz.cloudplatform.web.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.cmsz.cloudplatform.model.response.Response;

public class GenericAction extends BaseAction {
	
	private static final long serialVersionUID = 6088453089914883547L;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager ;
	
	public String execute() {
		return generic();
	}
	
	private String generic() {
        log.debug("----------------------------");
        log.debug("---------------------------- generic");
        log.debug("----------------------------");
        Object[] commandObj = this.requestParams.get("command");
        
        if (commandObj != null) {
    		Response response = null;
		    if ("GET".equalsIgnoreCase(this.request.getMethod())) {
		    	response = this.genericCloudServerManager.get(requestParams);
		    } else {
		    	response = this.genericCloudServerManager.post(requestParams);
		    }
		    
		    this.writeResponse(response);

        } else {
        	//TODO error
        	
        }
        
        return NONE;
    }
	
    
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}
