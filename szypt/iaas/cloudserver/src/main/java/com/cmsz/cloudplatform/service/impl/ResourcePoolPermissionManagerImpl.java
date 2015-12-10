package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.ResourcePoolPermissionDao;
import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.cmsz.cloudplatform.model.request.ResourcePoolPermissionRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.model.vo.ResourcePoolPermissionVO;
import com.cmsz.cloudplatform.service.ResourcePoolPermissionManager;
import com.hp.config.model.DbConfig;
import com.hp.config.service.DbConfigManager;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value = "resourcePoolPermissionManager")
public class ResourcePoolPermissionManagerImpl extends GenericManagerImpl<ResourcePoolPermission, Long> implements ResourcePoolPermissionManager {

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private DbConfigManager dbConfigManager;

	private ResourcePoolPermissionDao resourcePoolPermissionDao;

	protected static final String OWNER_TYPE = "ownerType";

	protected static final String LIST_ACCOUNTS_RESPONSE = "listaccountsresponse";
	protected static final String ACCOUNT = "account";
	// id name accounttype domain
	protected static final String ACCOUNT_ID = "id";
	protected static final String ACCOUNT_NAME = "name";
	protected static final String ACCOUNT_TYPE = "accounttype";
	protected static final String ACCOUNT_DOMAIN = "domain";

	protected static final String LIST_DOMAINS_RESPONSE = "listdomainsresponse";
	protected static final String DOMAIN = "domain";
	// id name path
	protected static final String DOMAIN_ID = "id";
	protected static final String DOMAIN_NAME = "name";
	protected static final String DOMAIN_PATH = "path";

	protected static final String LIST_PROJECTS_RESPONSE = "listprojectsresponse";
	protected static final String PROJECT = "project";
	// id name displaytext domain account
	protected static final String PROJECT_ID = "id";
	protected static final String PROJECT_NAME = "name";
	protected static final String PROJECT_DISPLAY_TEXT = "displaytext";
	protected static final String PROJECT_DOMAIN = "domain";
	protected static final String PROJECT_ACCOUNT = "account";

	// 10 resourcepool_related_object_type.account 1 账户
	// 11 resourcepool_related_object_type.domain 2 域
	// 12 resourcepool_related_object_type.project 3 项目
	protected static final String RESOURCE_POOL_RELATED_OBJECT_TYPE = "resourcepool_related_object_type";
	protected static final String RESOURCE_POOL_RELATED_OBJECT_TYPE_ACCOUNT = "resourcepool_related_object_type.account";
	protected static final String RESOURCE_POOL_RELATED_OBJECT_TYPE_DOMAIN = "resourcepool_related_object_type.domain";
	protected static final String RESOURCE_POOL_RELATED_OBJECT_TYPE_PROJECT = "resourcepool_related_object_type.project";

	// 13 resourcepool_permission.permit 1 允许
	// 14 resourcepool_permission.reject 0 拒绝
	protected static final String RESOURCE_POOL_PERMISSION = "resourcepool_permission";
	protected static final String RESOURCE_POOL_PERMISSION_PERMIT = "resourcepool_permission.permit";
	protected static final String RESOURCE_POOL_PERMISSION_REJECT = "resourcepool_permission.reject";

	private Map<String, DbConfig> resourcePoolRelatedObjectTypeMap;
	private Map<String, Integer> resourcePoolPermissionMap;

	public ResourcePoolPermissionManagerImpl() {
		super();
	}

	@Autowired
	public void setResourcePoolPermissionDao(ResourcePoolPermissionDao resourcePoolPermissionDao) {
		this.resourcePoolPermissionDao = resourcePoolPermissionDao;
		this.dao = resourcePoolPermissionDao;
	}

	@SuppressWarnings("unchecked")
	public Map<String, DbConfig> getResourcePoolRelatedObjectTypeMap() {
		if (this.resourcePoolRelatedObjectTypeMap == null) {
			resourcePoolRelatedObjectTypeMap = new HashMap<String, DbConfig>();
			DbConfig exampleEntity = new DbConfig();
			exampleEntity.setKey(RESOURCE_POOL_RELATED_OBJECT_TYPE + "%");
			List<DbConfig> dbConfigList = (List<DbConfig>) dbConfigManager.findByExample(exampleEntity);
			if (CollectionUtils.isNotEmpty(dbConfigList)) {
				for (DbConfig dbConfig : dbConfigList) {
					resourcePoolRelatedObjectTypeMap.put(dbConfig.getKey(), dbConfig);
				}
			}
		}

		return resourcePoolRelatedObjectTypeMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getResourcePoolPermissionMap() {
		if (this.resourcePoolPermissionMap == null) {
			resourcePoolPermissionMap = new HashMap<String, Integer>();
			DbConfig exampleEntity = new DbConfig();
			exampleEntity.setKey(RESOURCE_POOL_PERMISSION + "%");
			List<DbConfig> dbConfigList = (List<DbConfig>) dbConfigManager.findByExample(exampleEntity);
			if (CollectionUtils.isNotEmpty(dbConfigList)) {
				for (DbConfig dbConfig : dbConfigList) {
					resourcePoolPermissionMap.put(dbConfig.getKey(), Integer.parseInt(dbConfig.getValue().trim()));
				}
			}
		}

		return resourcePoolPermissionMap;
	}

	private Integer ownerType(String type) {
		if (getResourcePoolRelatedObjectTypeMap() != null && getResourcePoolRelatedObjectTypeMap().get(type) != null
				&& getResourcePoolRelatedObjectTypeMap().get(type).getValue() != null) {
			return Integer.parseInt(getResourcePoolRelatedObjectTypeMap().get(type).getValue());
		} else {
			return new Integer(-100);
		}
	}

	private Integer permit() {
		if (getResourcePoolPermissionMap() != null) {
			return getResourcePoolPermissionMap().get(RESOURCE_POOL_PERMISSION_PERMIT);
		} else {
			return new Integer(-100);
		}
	}

	private Integer reject() {
		if (getResourcePoolPermissionMap() != null) {
			return getResourcePoolPermissionMap().get(RESOURCE_POOL_PERMISSION_REJECT);
		} else {
			return new Integer(-100);
		}
	}

	@Override
	public ListResponse<DbConfig> getResourcePoolRelatedObjectType() {
		ListResponse<DbConfig> result = new ListResponse<DbConfig>();
		List<DbConfig> responses = new ArrayList<DbConfig>();

		Iterator<String> itr = this.getResourcePoolRelatedObjectTypeMap().keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			responses.add(this.getResourcePoolRelatedObjectTypeMap().get(key));
		}
		result.setResponses(responses);

		return result;
	}

	@Override
	public EntityResponse<ResourcePoolPermission> saveResourcePoolPermission(ResourcePoolPermissionRequest request) {
		EntityResponse<ResourcePoolPermission> result = null;
		ResourcePoolPermission entity = null;

		if (request != null) {
			if (request.getId() != null) {
				entity = resourcePoolPermissionDao.get(request.getId());
			} else {
				entity = new ResourcePoolPermission();
				entity.setOwnerType(request.getOwnerType());
				entity.setOwnerId(request.getOwnerId());
				entity.setOwnerName(request.getOwnerName());
				entity.setCreatedBy(request.getLoginId());
				entity.setCreatedOn(new Date());
			}

			if (entity != null) {
				entity.setProductionPool(request.getProductionPool() != null && request.getProductionPool() ? permit() : reject());
				entity.setTestingPool(request.getTestingPool() != null && request.getTestingPool() ? permit() : reject());
				entity.setModifiedBy(request.getLoginId());
				if (request.getId() != null) {
					entity.setModifiedOn(new Date());
				} else {
					entity.setModifiedOn(entity.getCreatedOn());
				}

				entity = resourcePoolPermissionDao.save(entity);
			}
		}
		if (null != entity) {
			result = new EntityResponse<ResourcePoolPermission>();
			result.setEntity(entity);
		}
		return result;
	}

	@Override
	public ListResponse<ResourcePoolPermissionVO> getResourcePoolPermissions(ResourcePoolPermissionRequest request,
			Map<String, Object[]> requestParams) {
		ListResponse<ResourcePoolPermissionVO> result = new ListResponse<ResourcePoolPermissionVO>();

		Object[] ownerTypeArr = requestParams.get(OWNER_TYPE);
		if (ownerTypeArr != null && ownerTypeArr.length > 0) {
			Integer ownerType = Integer.parseInt((String) ownerTypeArr[0]);
			List<String> ownerIds = new ArrayList<String>();

			List<ResourcePoolPermissionVO> responses = getResults(requestParams, ownerType, ownerIds);

			List<ResourcePoolPermission> list = resourcePoolPermissionDao.getResourcePoolPermissions(ownerType, ownerIds);

			if (CollectionUtils.isNotEmpty(responses) && CollectionUtils.isNotEmpty(list)) {
				for (ResourcePoolPermissionVO vo : responses) {
					for (ResourcePoolPermission entity : list) {
						if (vo.getOwnerType() == entity.getOwnerType() && vo.getOwnerId().equals(entity.getOwnerId())) {
							vo.setProductionPool(entity.getProductionPool().equals(permit()) ? true : false);
							vo.setTestingPool(entity.getTestingPool().equals(permit()) ? true : false);
							vo.setId(entity.getId());
							break;
						}
					}
				}
			}

			result.setResponses(responses);
		}

		return result;
	}

	/**
	 * 
	 * 
	 * @param requestParams
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ResourcePoolPermissionVO> getResults(Map<String, Object[]> requestParams, Integer ownerType, List<String> ownerIds) {
		List<ResourcePoolPermissionVO> result = new ArrayList<ResourcePoolPermissionVO>();
		Response response = genericCloudServerManager.get(requestParams);

		if (response != null && ownerType != null) {
			JSONObject json = JSONObject.fromObject(response.getResponseString());

			if (json != null) {
				String jsonObjKey = null;
				if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_ACCOUNT))) {
					jsonObjKey = LIST_ACCOUNTS_RESPONSE;
				} else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_DOMAIN))) {
					jsonObjKey = LIST_DOMAINS_RESPONSE;
				}else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_PROJECT))) {
					jsonObjKey = LIST_PROJECTS_RESPONSE;
				}
				JSONObject jsonObj = (JSONObject) json.get(jsonObjKey);

				if (jsonObj != null) {
					String jsonObjListKey = null;
					if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_ACCOUNT))) {
						jsonObjListKey = ACCOUNT;
					} else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_DOMAIN))) {
						jsonObjListKey = DOMAIN;
					}else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_PROJECT))) {
						jsonObjListKey = PROJECT;
					}
					List<JSONObject> jsonObjList = (List<JSONObject>) jsonObj.get(jsonObjListKey);

					if (CollectionUtils.isNotEmpty(jsonObjList)) {
						for (JSONObject jsobj : jsonObjList) {
							ResourcePoolPermissionVO vo = new ResourcePoolPermissionVO();

							if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_ACCOUNT))) {

								// name accounttype domain
								vo.setOwnerType(ownerType);
								vo.setOwnerId(jsobj.getString(ACCOUNT_ID) != null ? jsobj.getString(ACCOUNT_ID) : null);
								vo.setOwnerName(jsobj.getString(ACCOUNT_NAME) != null ? jsobj.getString(ACCOUNT_NAME) : null);

								vo.setAccountName(jsobj.getString(ACCOUNT_NAME) != null ? jsobj.getString(ACCOUNT_NAME) : null);
								vo.setAccountType(jsobj.getString(ACCOUNT_TYPE) != null ? jsobj.getString(ACCOUNT_TYPE) : null);
								vo.setAccountDomain(jsobj.getString(ACCOUNT_DOMAIN) != null ? jsobj.getString(ACCOUNT_DOMAIN) : null);
							} else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_DOMAIN))) {

								// id name path
								vo.setOwnerType(ownerType);
								vo.setOwnerId(jsobj.getString(DOMAIN_ID) != null ? jsobj.getString(DOMAIN_ID) : null);
								vo.setOwnerName(jsobj.getString(DOMAIN_NAME) != null ? jsobj.getString(DOMAIN_NAME) : null);

								vo.setDomainId(jsobj.getString(DOMAIN_ID) != null ? jsobj.getString(DOMAIN_ID) : null);
								vo.setDomainName(jsobj.getString(DOMAIN_NAME) != null ? jsobj.getString(DOMAIN_NAME) : null);
								vo.setDomainPath(jsobj.getString(DOMAIN_PATH) != null ? jsobj.getString(DOMAIN_PATH) : null);
							} else if (ownerType.equals(ownerType(RESOURCE_POOL_RELATED_OBJECT_TYPE_PROJECT))) {

								// name displaytext domain account
								vo.setOwnerType(ownerType);
								vo.setOwnerId(jsobj.getString(PROJECT_ID) != null ? jsobj.getString(PROJECT_ID) : null);
								vo.setOwnerName(jsobj.getString(PROJECT_NAME) != null ? jsobj.getString(PROJECT_NAME) : null);

								vo.setProjectName(jsobj.getString(PROJECT_NAME) != null ? jsobj.getString(PROJECT_NAME) : null);
								vo.setProjectDisplayText(jsobj.getString(PROJECT_DISPLAY_TEXT) != null ? jsobj.getString(PROJECT_DISPLAY_TEXT) : null);
								vo.setProjectDomain(jsobj.getString(PROJECT_DOMAIN) != null ? jsobj.getString(PROJECT_DOMAIN) : null);
								vo.setProjectAccount(jsobj.getString(PROJECT_ACCOUNT) != null ? jsobj.getString(PROJECT_ACCOUNT) : null);
							}

							ownerIds.add(vo.getOwnerId());

							result.add(vo);
						}
					}
				}
			}
		}

		return result;
	}

	public ResourcePoolPermission getPool(Integer type,String value){
		return resourcePoolPermissionDao.getPool(type,value);
	}
}
