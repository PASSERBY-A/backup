package com.volkswagen.tel.billing.billcall.biz;

import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserRoleDaoService;

@Component
public class UserRoleBizService {
	private static final Logger log = LoggerFactory
			.getLogger(UserRoleBizService.class);

	@Autowired
	UserRoleDaoService userRoleDaoService;

	public JSONObject getAllRoleUsers() {
		JSONObject jObj = null;

		List<UserRoleEntity> userRoleList = userRoleDaoService.getAllUsers();

		if (userRoleList != null && userRoleList.size() > 0) {
			log.info(">>> Role user count: " + userRoleList.size());
			log.info(">>> Role user list: " + userRoleList);
			
			jObj = new JSONObject();
			jObj.put("userRoleList", userRoleList);

			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "No available users is found.");
		}

		return jObj;
	}
	
	/**
	 * get users of specified role.
	 * @param roleName
	 * @return
	 */
	public JSONObject getRoleUsers(String roleName) {
		JSONObject jObj = null;

		List<UserRoleEntity> userRoleList = userRoleDaoService
				.getUsersByRoleName(roleName);

		if (userRoleList != null && userRoleList.size() > 0) {
			log.info(">>> User role list: " + userRoleList);
			
			jObj = new JSONObject();
			jObj.put("userRoleList", userRoleList);

			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "No available users is found.");
		}

		return jObj;
	}
	
	/**
	 * get roles assigned to particular user.
	 * @param userId
	 * @return
	 */
	public JSONObject getUserRoles(String userId) {
		JSONObject jObj = null;

		List<UserRoleEntity> userRoleList = userRoleDaoService
				.getRolesByUserId(userId);

		if (userRoleList != null && userRoleList.size() > 0) {
			log.info(">>> User role list: " + userRoleList);
			
			jObj = new JSONObject();
			jObj.put("userRoleList", userRoleList);
			
//			for (UserRoleEntity ent : userRoleList) {
//				if ("SUPERUSER".equals(ent.getRoleName())) {
//					jObj.put("managementUrl", "vwg-admin/vwUserRole.jsp");
//					break;
//				}
//			}

			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "No available users is found.");
		}

		return jObj;
	}
	
	/**
	 * save mapping of user and role.
	 * @param userId
	 * @param roleName
	 * @return
	 */
	public JSONObject saveRoleUser(String userId, String roleName) {
		JSONObject jObj = new JSONObject();

		UserRoleEntity entity = new UserRoleEntity();
		entity.setUserId(userId);
		entity.setRoleName(roleName);
		entity.setLastUpdateTime(Calendar.getInstance().getTime());
		entity = userRoleDaoService.saveUserRole(entity);
		log.info(">>> Saved entity info: " + entity);
		
		jObj.put("userRoleEntity", entity);
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		return jObj;
	}
	
	/**
	 * delete mapping by primary key.
	 * @param entityId
	 * @return
	 */
	public JSONObject deleteRoleUser(long entityId) {
		JSONObject jObj = new JSONObject();

		userRoleDaoService.deleteUserRoleById(entityId);
		
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		return jObj;
	}
}
