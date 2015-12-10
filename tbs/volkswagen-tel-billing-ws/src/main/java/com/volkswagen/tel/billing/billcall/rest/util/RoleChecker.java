package com.volkswagen.tel.billing.billcall.rest.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserRoleDaoService;

@Component
public class RoleChecker {
	private static final Logger log = LoggerFactory
			.getLogger(RoleChecker.class);

	@Autowired
	UserRoleDaoService userRoleDaoService;
	
	public static final String EMERGENCY_USER = "EMERGENCY-USER";
	public static final String SYSTEM_ADMIN = "SYSTEM-ADMIN";
	public static final String HR_ADMIN = "HR-ADMIN";
	public static final String AUDITOR = "AUDITOR";
	public static final String ITASSERT = "IT-ASSERT";
	
	public static final String VCIC = "VCIC-CONTROLLING";
	public static final String VGIC = "VGIC-CONTROLLING";
	public static final String AUDI = "AUDI-CONTROLLING";
	public static final String VCRA = "VCRA-CONTROLLING";
	
	/**
	 * judge if the user has the role.
	 * @param userId
	 * @param roleName
	 * @return
	 */
	public boolean hasRole(String userId, String roleName) {
		boolean rtn = false;

		List<UserRoleEntity> urList = userRoleDaoService.getRolesByUserId(userId);
		if (urList != null) {
			for (UserRoleEntity ent : urList) {
				if (roleName.equals(ent.getRoleName())) {
					rtn = true;
					break;
				}
			}
		}

		log.info(">>> result of hasRole(userId, roleName): " + rtn);
		return rtn;
	}
}
