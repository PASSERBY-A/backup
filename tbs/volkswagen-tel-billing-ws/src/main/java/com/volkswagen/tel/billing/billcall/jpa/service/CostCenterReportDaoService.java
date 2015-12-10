package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;


public interface CostCenterReportDaoService {
	
public List<CostCenterReportEntity> findByCostCenterPage(String type, int year,int month,int page, int size, Direction direction,String properties);
	
public List<CostCenterReportEntity> findByCostCenter(String type, int year,int month);

public Long countCostCenter(String type, int year,int month);

public List<CostCenterReportEntity> findAllPage(int year,int month,int page, int size, Direction direction,String properties);

public Long countAllCostCenter(int year,int month);

}
