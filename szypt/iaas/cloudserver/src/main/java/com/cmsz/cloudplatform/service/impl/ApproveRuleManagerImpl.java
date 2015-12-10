package com.cmsz.cloudplatform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.ApproveRuleDao;
import com.cmsz.cloudplatform.model.ApproveRule;
import com.cmsz.cloudplatform.service.ApproveRuleManager;
import com.hp.core.service.impl.GenericManagerImpl;
@Service(value="approveRuleManager")
public class ApproveRuleManagerImpl extends GenericManagerImpl<ApproveRule,Long> implements ApproveRuleManager{

	private ApproveRuleDao approveRuleDao;
	public ApproveRuleManagerImpl(){
		super();
	}
	
	public ApproveRuleManagerImpl(ApproveRuleDao approveRuleDao){
		super(approveRuleDao);
		this.approveRuleDao = approveRuleDao;
	}
	
	@Autowired
	public void setApproveRuleDao(ApproveRuleDao approveRuleDao) {
		this.dao = approveRuleDao;
		this.approveRuleDao = approveRuleDao;
	}
	
	@Override
	public List<ApproveRule> listApproveRule(ApproveRule approveRule) {
		return this.approveRuleDao.listApproveRule(approveRule);
		
	}

}
