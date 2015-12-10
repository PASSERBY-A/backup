package com.cmsz.cloudplatform.service;

import java.util.List;

import com.cmsz.cloudplatform.model.ApproveRule;
import com.hp.core.service.GenericManager;

public interface ApproveRuleManager extends GenericManager<ApproveRule,Long>{
	List<ApproveRule> listApproveRule(ApproveRule approveRule);

}
