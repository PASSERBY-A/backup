package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;

public interface CostCenterEmployeeReportRepository extends
		CrudRepository<CostCenterEmployeeReportEntity, Long> {

	@Query("from CostCenterEmployeeReportEntity a where lower(a.costCenter)=lower(?1) and  a.date>=?2 and a.date<?3 ")
	public List<CostCenterEmployeeReportEntity> findByCostCenterPage(String costCenter,Date begin,Date end,Pageable pageable);
	
	@Query("from CostCenterEmployeeReportEntity a where lower(a.costCenter)=lower(?1) and  a.date>=?2 and a.date<?3 ")
	public List<CostCenterEmployeeReportEntity> findByCostCenter(String costCenter,Date begin,Date end);
	
	@Query("select count(a.costCenter) from CostCenterEmployeeReportEntity a where lower(a.costCenter)=lower(?1) and  a.date>=?2 and a.date<?3 ")
	public Long countCostCenter(String costCenter,Date begin,Date end);
	
	@Query("from CostCenterEmployeeReportEntity a where a.date>=?1 and a.date<?2 ")
	public List<CostCenterEmployeeReportEntity> findByMonth(Date begin,Date end);
	
}
