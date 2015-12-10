package com.volkswagen.tel.billing.billcall.jpa.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volkswagen.tel.billing.billcall.biz.ReportBizService;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.TelephoneBillRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.UserTelephoneRepository;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Service("telephoneBillDaoService")
public class TelephoneBillDaoServiceImpl implements TelephoneBillDaoService {
	
	private static final Logger log = LoggerFactory.getLogger(TelephoneBillDaoServiceImpl.class);
	
	@Autowired
	private TelephoneBillRepository repository;

	@Autowired
	private UserTelephoneRepository userTelephoneRepository;
	
	
	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public TelephoneBillEntity saveTelephoneBill(TelephoneBillEntity entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteTelephoneBillById(Long id) {
		repository.delete(id);
	}

	@Override
	public TelephoneBillEntity findTelephoneBillByTelNumberAndMonth(
			String telephoneNumber, int year, int month) throws Exception {
		List<TelephoneBillEntity> list = repository
				.findTelephoneBillByTelNumberAndMonth(telephoneNumber, year, month);
		
		if (list == null || list.size() <= 0) {
			return null;
		}

		if (list.size() > 1) {
			throw new Exception("More than one bill is found for the month "
					+ year + "/" + month);
		} else {
			return list.get(0);
		}

	}

	@Override
	public List<TelephoneBillEntity> findAvailableMonthsByTelAndYear(
			String telNumber, int year, int monthLimitStart, int monthLimitEnd) {
		log.error(String.valueOf("monthLimitStart"));
		log.error(String.valueOf(monthLimitStart));
		log.error(String.valueOf("monthLimitEnd"));
		log.error(String.valueOf(monthLimitEnd));
		return repository.findAvailableMonthsByTelAndYear(telNumber, year,
				monthLimitStart, monthLimitEnd);
	}

	@Override
	public List<TelephoneBillEntity> findAvailableYearsByTel(
			String telephoneNumber) {
		return repository.findAvailableYearsByTel(telephoneNumber);
	}

	@Transactional
	@Override
	public int updateTelephoneBillStatus(String telephoneNumber, int year,
			int month, String status) {

		return repository.updateTelephoneBillStatus(telephoneNumber, year, month, status);
	}

	@Transactional
	@Override
	public int updateTelephoneBillStatusByBillId(long billId, String status) {
		return repository.updateTelephoneBillStatusByBillId(billId, status);
	}

	@Override
	public List<TelephoneBillEntity> findBillsByTelTimePeriod(
			String telephoneNumber, Calendar startCalendar, Calendar endCalendar) {
		String startDate = CommonUtil.formatDate(startCalendar.getTime(), "yyyyMMdd");
		String endDate = CommonUtil.formatDate(endCalendar.getTime(), "yyyyMMdd");
		
		log.info(">>> startDate="+startDate);
		log.info(">>> endDate="+endDate);
		
		return repository.findBillsByTelTimePeriod(telephoneNumber, startDate, endDate);
	}

	@Override
	public Map<String,List<TelephoneBillEntity>> findNoSendBillByUser(String userId){
		
		List<UserTelephoneEntity> userEntitys = userTelephoneRepository.findTelephonesByUserId(userId);
		
		Map<String,List<TelephoneBillEntity>> result = new LinkedHashMap<String,List<TelephoneBillEntity>>();
		
		for(UserTelephoneEntity phone:userEntitys)
		{
			List<TelephoneBillEntity> list = repository.findByTelephoneNumberAndStatusNot(phone.getTelephoneNumber(), "SENT");
			
			 Collections.sort(list, new Comparator<TelephoneBillEntity>(){

					@Override
					public int compare(TelephoneBillEntity o1, TelephoneBillEntity o2) {
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
						Calendar c1 = Calendar.getInstance();
						Calendar c2 = Calendar.getInstance();
						
						c1.set(Calendar.YEAR, o1.getYear());
						c1.set(Calendar.MONTH, o1.getMonth());
						c2.set(Calendar.YEAR, o2.getYear());
						c2.set(Calendar.MONTH, o2.getMonth());
						
						
						return c1.before(c2)?1:-1;
					}}); 
			
			result.put(phone.getTelephoneNumber(), list);
			
		}
		
		
		
		
		return result;
	}	
	
	
	
	public List<TelephoneBillEntity> findAllOpenBills(String vendorName,int year,int month,int page, int size, Direction direction,String properties)
	{
		// "lastUpdateTime"
		PageRequest pageReq = new PageRequest(page-1, size, direction, properties);
		
		List<TelephoneBillEntity> result = repository.findAllOpenBillsByType(year, month,vendorName, pageReq);
		
		return result;
	}
	
	
	public Long countOpenBillsByType(String vendorName,int year,int month)
	{
		
		return repository.countOpenBillsByType(year, month, vendorName);
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		TelephoneBillDaoServiceImpl n= context.getBean(TelephoneBillDaoServiceImpl.class);
		
		
		List<TelephoneBillEntity> l = n.findAllOpenBills("",2015, 8, 1, 50, Sort.Direction.ASC,"lastUpdateTime");
		
		System.out.println(l);
		
	}
	
}
