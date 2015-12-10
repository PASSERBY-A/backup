package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;


public interface CostCenterEmployeeReportDaoService {
	
public List<CostCenterEmployeeReportEntity> findByCostCenterPage(String costCenter, int year,int month,int page, int size, Direction direction,String properties);
	
public List<CostCenterEmployeeReportEntity> findByCostCenter(String costCenter, int year,int month);

public Long countCostCenter(String costCenter, int year,int month);
	
}
