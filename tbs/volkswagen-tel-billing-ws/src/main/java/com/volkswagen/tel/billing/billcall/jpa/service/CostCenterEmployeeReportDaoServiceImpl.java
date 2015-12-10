package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillSubmitHistoryEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.BillSubmitHistoryRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.CostCenterEmployeeReportRepository;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.scheduler.CostCenterReportTask;

@Service("costCenterEmployeeReportDaoService")
public class CostCenterEmployeeReportDaoServiceImpl implements CostCenterEmployeeReportDaoService {

	@Autowired
	private CostCenterEmployeeReportRepository repository;
	
	@Override
	public List<CostCenterEmployeeReportEntity> findByCostCenterPage( String costCenter, int year, int month, int page, int size,Direction direction, String properties) 
	{
        Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		
		PageRequest pageReq = new PageRequest(page-1, size, direction, "lastUpdateTime");
		return repository.findByCostCenterPage(costCenter,begin,end, pageReq);
	}
	
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		CostCenterEmployeeReportDaoService t = context.getBean(CostCenterEmployeeReportDaoService.class);

		List<CostCenterEmployeeReportEntity> l = t.findByCostCenterPage("C140", 2015, 8, 1, 1000, Direction.ASC, "lastUpdateTime");	
		 l = t.findByCostCenter("C140", 2015, 8);
		//System.out.println(t.countCostCenter("C140",2015, 8));
		
		
		System.out.println(l);
		
		
				//CostCenterReportEntity c = new CostCenterReportEntity();
				
				//c.setCostCenter("8001000200");
		
	}

	@Override
	public Long countCostCenter(String costCenter, int year, int month) {
		 Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
			Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		return repository.countCostCenter(costCenter, begin, end);
	}

	@Override
	public List<CostCenterEmployeeReportEntity> findByCostCenter(
			String costCenter, int year, int month) {
		System.out.println(year);
		System.out.println(month);
		
		
		Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		
		List<CostCenterEmployeeReportEntity> result  = repository.findByCostCenter(costCenter, begin, end);
		
				System.out.println(result);
				
		return result;
	}
 
 
}
