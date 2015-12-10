package com.volkswagen.tel.billing.billcall.biz;

import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Component
public class UserInfoBizService {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoBizService.class);
	
	@Resource(name="extServiceAddressConfigMap")
	private Map<String, String> extServiceAddressConfig;

	/**
	 * get user info by user id.
	 * @param userId
	 * @return
	 */
	public JSONObject getUserInfoByUserId(String userId) {
		log.info("========== getUserInfoByUserId start.");
		JSONObject jObj = null;
		
		log.info(">>> userId=" + userId);
		// - call LDAP service to get user info.
        MultivaluedMapImpl params = new MultivaluedMapImpl();
        params.add("uid", userId);
        
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(extServiceAddressConfig.get("ad-tim-service"));
		log.info(">>> Service address: " + service.toString());
		ClientResponse response = service.path("rest")
				.path("userInfoService")
				.path("getUserInfoByUserId")
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, params);

        int status = response.getStatus();
        if (status == 200) {
            EntityTag e = response.getEntityTag();
            String entity = response.getEntity(String.class);
            log.info(">>>> entity=" + entity);
            
            JSONObject resultObj = JSONObject.fromObject(entity);
            
            jObj = new JSONObject();
            jObj.put("userId", userId);
			if (resultObj.has("returnCode")
					&& resultObj.getString("returnCode").equals("SUCCESS")) {
	            jObj.put("firstName", resultObj.getString("givenname"));
	            jObj.put("lastName", resultObj.getString("surname"));
	            if(resultObj.getString("whenCreated")!=null){
		            jObj.put("whenCreated", resultObj.getString("whenCreated"));
	            }
	            jObj.put("staffCode", resultObj.has("staffcode")?resultObj.getString("staffcode"):"");
	            jObj.put("returnCode", "SUCCESS");
	            jObj.put("returnMessage", "SUCCESS");
			} else {
				jObj.put("returnCode", resultObj.getString("returnCode"));
	            jObj.put("returnMessage", resultObj.getString("returnMessage"));
			}
        } else {
            log.error(">>> ad-tim-service call failed.");
            jObj = new JSONObject();
            jObj.put("returnCode", "FAILURE");
            jObj.put("returnMessage", "Service call failed.");
        }
        log.info("========== getUserInfoByUserId end.");

		return jObj;
	}
}
