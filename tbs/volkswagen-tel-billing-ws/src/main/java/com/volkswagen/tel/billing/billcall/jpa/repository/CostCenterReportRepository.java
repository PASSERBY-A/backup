package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;

public interface CostCenterReportRepository extends
		CrudRepository<CostCenterReportEntity, Long> 
{
	
	@Query("from CostCenterReportEntity a where lower(a.costCenterType)=lower(?1)    and  a.date>=?2 and a.date<?3 ")
	public List<CostCenterReportEntity> findByCostCenterPage(String costCenterType,Date begin,Date end,Pageable pageable);
	
	@Query("from CostCenterReportEntity a where lower(a.costCenterType)=lower(?1)  and  a.date>=?2 and a.date<?3 ")
	public List<CostCenterReportEntity> findByCostCenter(String costCenterType,Date begin,Date end);
	
	@Query("select count(a.costCenter) from CostCenterReportEntity a where lower(a.costCenterType)=lower(?1)  and  a.date>=?2 and a.date<?3 ")
	public Long countCostCenter(String costCenterType,Date begin,Date end);
	
	@Query("from CostCenterReportEntity a where a.date>=?1 and a.date<?2 ")
	public List<CostCenterReportEntity> findAllPage(Date begin,Date end,Pageable pageable);
	
	@Query("select count(a.costCenter) from CostCenterReportEntity a where a.date>=?1 and a.date<?2 ")
	public Long countAllCostCenter(Date begin,Date end);
	
	
	@Query("from CostCenterReportEntity a where a.date>=?1 and a.date<?2 ")
	public List<CostCenterReportEntity> findByMonth(Date begin,Date end);
	
}
