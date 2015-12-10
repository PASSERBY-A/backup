package com.cmsz.cloudplatform.model.request;

import com.cmsz.cloudplatform.model.Parameter;

public class ListResourceStructureRequest extends BaseListRequest {
	@Parameter(name = "domainId", type = FieldType.STRING, description = "")
	private String domainId;
	@Parameter(name = "account", type = FieldType.STRING, description = "")
	private String account;

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
