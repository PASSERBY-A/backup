package com.cmsz.cloudplatform.web.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.cmsz.cloudplatform.model.request.ResourcePoolPermissionRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.ResourcePoolPermissionVO;
import com.cmsz.cloudplatform.service.LogManager;
import com.cmsz.cloudplatform.service.ResourcePoolPermissionManager;
import com.cmsz.cloudplatform.utils.LogConstants;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.config.model.DbConfig;

public class ResourcePoolPermissionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	protected static final String LIST_ACCOUNTS = "listAccounts";
	protected static final String LIST_DOMAINS = "listDomains";
	protected static final String LIST_PROJECTS = "listProjects";
	protected static final String OWNER_TYPE = "ownerType";

	@Autowired
	private LogManager logManager;

	@Autowired
	private ResourcePoolPermissionManager resourcePoolPermissionManager;

	public String listResourcePoolRelatedObjectType() {
		ListResponse<DbConfig> response = resourcePoolPermissionManager.getResourcePoolRelatedObjectType();
		writeResponse(response);
		return NONE;
	}

	public String listResourcePoolPermission() {
		Object[] ownerType = requestParams.get(OWNER_TYPE);
		String url = null;
		if (ownerType != null && ownerType.length > 0) {
			switch (Integer.parseInt((String) ownerType[0])) {
			case 1:
				url = LIST_ACCOUNTS;
				break;
			case 2:
				url = LIST_DOMAINS;
				break;
			case 3:
				url = LIST_PROJECTS;
				break;
			default:
				break;
			}
		}
		requestParams.put(ControlAction.COMMAND, new Object[] { url });

		ResourcePoolPermissionRequest request = (ResourcePoolPermissionRequest) this.wrapRequest(new ResourcePoolPermissionRequest());
		ListResponse<ResourcePoolPermissionVO> response = resourcePoolPermissionManager.getResourcePoolPermissions(request, requestParams);

		writeResponse(response);
		return NONE;
	}

	public String saveResourcePoolPermission() {
		ResourcePoolPermissionRequest request = (ResourcePoolPermissionRequest) this.wrapRequest(new ResourcePoolPermissionRequest());
		String operation = request.getId() == null ? LogConstants.CREATE : LogConstants.MODIFY;

		String content = "";
		try {
			EntityResponse<ResourcePoolPermission> result = resourcePoolPermissionManager.saveResourcePoolPermission(request);

			ListResponse<DbConfig> ownerTypeList = resourcePoolPermissionManager.getResourcePoolRelatedObjectType();
			for (DbConfig dc : ownerTypeList.getResponses()) {
				if (dc.getValue().equals(request.getOwnerType().toString())) {
					content = dc.getDescription() + "[" + request.getOwnerName() + "]资源池分配。";
					break;
				}
			}

			logManager.log(this.request.getRemoteHost(), LogConstants.RESOURCE_POOL_PERMISSION, operation, request.getLoginId(), content,
					LogConstants.SUCCESS, "生产池：" + result.getEntity().getProductionPool() + "，测试池：" + result.getEntity().getTestingPool());
			writeResponse(result);
		} catch (Exception e) {
			log.error("资源池分配发生异常：", e);
			logManager.log(this.request.getRemoteHost(), LogConstants.RESOURCE_POOL_PERMISSION, operation, request.getLoginId(), content,
					LogConstants.FAIL, e.toString());
		}

		return NONE;
	}

	public String listProjectResourcePoolPermission() {
		ResourcePoolPermission response = resourcePoolPermissionManager.getPool(Integer.parseInt(request.getParameter("key")),
				Integer.parseInt(request.getParameter("key")) == 1 ? this.activeUser.getAccountid() : request.getParameter("value"));
		if (response != null) {
			writeResponse(response);
		}
		return NONE;
	}
}
