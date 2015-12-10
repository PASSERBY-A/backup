package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.CostCenterReportRepository;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Service("costCenterReportDaoService")
public class CostCenterReportDaoServiceImpl implements CostCenterReportDaoService {

	@Autowired
	private CostCenterReportRepository repository;
	
	@Override
	public List<CostCenterReportEntity> findByCostCenterPage(String type,int year, int month, int page, int size,Direction direction, String properties) 
	{
        Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		PageRequest pageReq = new PageRequest(page-1, size, direction, "lastUpdateTime");
		return repository.findByCostCenterPage(type,begin,end, pageReq);
	}
	
	
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		CostCenterReportDaoService t = context.getBean(CostCenterReportDaoService.class);

		List<CostCenterReportEntity> l = t.findByCostCenterPage("VCRA",2015, 9, 1, 1000, Direction.ASC, "lastUpdateTime");	
		
		System.out.println(t.countCostCenter("VCRA",2015, 9));
		
		//List<CostCenterReportEntity> l = t.findAllPage(2015, 9, 1, 1000, Direction.ASC, "lastUpdateTime");
		//System.out.println(t.countAllCostCenter(2015, 9));
		System.out.println(l);

		
		
		
				//CostCenterReportEntity c = new CostCenterReportEntity();
				
				//c.setCostCenter("8001000200");
		
	}

	@Override
	public Long countCostCenter(String type, int year, int month) {
		 Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
			Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		return repository.countCostCenter(type, begin, end);
	}

	@Override
	public List<CostCenterReportEntity> findByCostCenter(String type, int year, int month) {
		
		Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		
		return repository.findByCostCenter(type, begin, end);
	}


	@Override
	public List<CostCenterReportEntity> findAllPage(int year, int month, int page, int size, Direction direction, String properties) {
		
		Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		
		PageRequest pageReq = new PageRequest(page-1, size, direction, "lastUpdateTime");
		
		return repository.findAllPage(begin, end, pageReq);
	}


	@Override
	public Long countAllCostCenter(int year, int month) {
		
		Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		
		Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		
		return repository.countAllCostCenter(begin, end);
	}

 
 
}
