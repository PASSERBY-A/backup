package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.ApproveRule;
import com.hp.core.dao.GenericDao;

public interface ApproveRuleDao extends GenericDao<ApproveRule,Long>{
	List<ApproveRule> listApproveRule(ApproveRule approveRule);
}
