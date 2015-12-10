package com.volkswagen.tel.billing.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.CostCenterEmployeeReportRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.CostCenterReportRepository;
import com.volkswagen.tel.billing.billcall.jpa.service.BillCallRecordDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillSumDaoService;
import com.volkswagen.tel.billing.common.GenericConfig;
import com.volkswagen.tel.billing.common.TBSPersonInfo;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.common.util.PreciseCompute;
import com.volkswagen.tel.billing.ldap.TIMService;

@Service("costCenterReportTask")
public class CostCenterReportTask {
	
	@Autowired
	private TIMService timService;
	
	@Autowired
	private TelephoneBillSumDaoService telephoneBillSumDaoService;

	@Autowired
	private BillCallRecordDaoService billCallRecordDaoService;
	
	@Autowired
	private CostCenterReportRepository costCenterReportRepository;
	
	@Autowired
	private CostCenterEmployeeReportRepository costCenterEmployeeReportRepository;
	
	
	
	private static final Logger log = LoggerFactory .getLogger(CostCenterReportTask.class);
	
	
	
	public double computeMobilesCostByMonth(String mobiles, int year, int month)
	{
		if(StringUtils.isBlank(mobiles))
			return 0;
		
		double cellTotal = 0;
		
		for(String m:mobiles.split(","))
		{
			
			List<Float> f = billCallRecordDaoService.findCostByCallingNumberAndYearAndMonth(m, year, month);
			
			for(Float v:f)
			{
				if(v==null)
					v = Float.parseFloat("0");
				
				cellTotal = PreciseCompute.add(cellTotal, v);
				
			}
			
			Double pkgF = telephoneBillSumDaoService.computePKGTotalByTelNumberAndMonth(m, year, month);
			if(pkgF==null)
				pkgF = Double.parseDouble("0");
			
			cellTotal = PreciseCompute.add(cellTotal, pkgF);
		}
		
		return cellTotal;
	}
	
	public double computeTelephoneCostByMonth(String telephone, int year, int month)
	{
		if(StringUtils.isBlank(telephone))
			return 0;
		
		double fixTotal = 0;

		//Float f = telephoneBillSumDaoService.findMonthPKGByTelNumberAndMonth(telephone, year, month);
		
		List<Float> f = billCallRecordDaoService.findCostByCallingNumberAndYearAndMonth(telephone, year, month);
		
		for(Float v:f)
		{
			if(v==null)
				v = Float.parseFloat("0");
			fixTotal = PreciseCompute.add(fixTotal, v);
			
		}
		
		
		
		return fixTotal;
		
	}
	
	public Map<String,Double> computeCostCenterTelephoneAndMobileTotalByMonth(String costCenter, int year, int month)
	{
		Map<String,Double> result = new HashMap<String,Double>();
		
		List<TBSPersonInfo> user =Collections.EMPTY_LIST;

		try {
			timService.init();
			user = timService.findPersonInfoListByAttribute("costcenter", costCenter);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		double cellTotal = 0;
		double fixTotal = 0;
		for(TBSPersonInfo u:user)
		{
			double c = computeMobilesCostByMonth(u.getMobile(),year,month);
			cellTotal = PreciseCompute.round(PreciseCompute.add(c, cellTotal),2);
			double f = computeTelephoneCostByMonth(u.getTelephonenumber(),year,month);
			fixTotal = PreciseCompute.round(PreciseCompute.add(f,fixTotal),2);
			
		}
		
		
		result.put("cellTotal", PreciseCompute.round(cellTotal, 2));
		result.put("fixTotal", PreciseCompute.round(fixTotal, 2));
		
		
		return result;
		
	}
	
	
	
	public void exec()
	{
		List<String> costCenterList =Collections.EMPTY_LIST;
		
		Set<CostCenterReportEntity> costCenterReportEntitys = new HashSet<CostCenterReportEntity>();
		Set<CostCenterEmployeeReportEntity> costCenterEmployeeReportEntitys = new HashSet<CostCenterEmployeeReportEntity>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		Calendar lastMonth = Calendar.getInstance();
		
		lastMonth.setTime(new Date());
		
		lastMonth.add(Calendar.MONTH, -1);
		
		String  beginMonth = GenericConfig.reportBeginMonth==null?sdf.format(lastMonth.getTime()):GenericConfig.reportBeginMonth;
		
		try {
			Date bMonth =  sdf.parse(beginMonth) ;
			
			for(;;)
			{
				int year = lastMonth.get(Calendar.YEAR);
				int month = lastMonth.get(Calendar.MONTH)+1;
				
			
				String beginMonthStr = sdf.format(bMonth);
				String lastMonthStr = sdf.format(lastMonth.getTime());
				
				System.out.println(year+" /"+month);
					
				try {
					timService.init();
					
					
					costCenterList = timService.findAllCostCenter();
					
					for(String costCenter:costCenterList)
					{
						
						CostCenterReportEntity costCenterReportEntity = new CostCenterReportEntity();
						Map<String, Double>  totals = computeCostCenterTelephoneAndMobileTotalByMonth(costCenter,year,month);
						double cellTotal=totals.get("cellTotal");
						double fixTotal = totals.get("fixTotal");
						
						costCenterReportEntity.setDate(lastMonth.getTime());
						costCenterReportEntity.setCostCenter(costCenter.trim());
						costCenterReportEntity.setFixedPhoneCost(fixTotal);
						costCenterReportEntity.setCellphoneCost(cellTotal);
						costCenterReportEntity.setLastUpdateTime(new Date());
						costCenterReportEntitys.add(costCenterReportEntity);
						costCenterReportRepository.save(costCenterReportEntitys);
						List<TBSPersonInfo> users = timService.findPersonInfoListByAttribute("costcenter", costCenter);
						
						for(TBSPersonInfo u:users)
						{
							CostCenterEmployeeReportEntity costCenterEmployeeReportEntity = new CostCenterEmployeeReportEntity();
							costCenterEmployeeReportEntity.setUserId(u.getUsername());
							costCenterEmployeeReportEntity.setName(u.getName());
							costCenterEmployeeReportEntity.setStaffCode(u.getEnumber());
							costCenterEmployeeReportEntity.setTelephoneNumber(u.getTelephonenumber());
							costCenterEmployeeReportEntity.setMobilePhone(u.getMobile());
							double mobileTotal = computeMobilesCostByMonth(u.getMobile(),year,month);
							double telephoneTotal = computeTelephoneCostByMonth(u.getTelephonenumber(),year,month);
							costCenterEmployeeReportEntity.setCellphoneCost(mobileTotal);
							costCenterEmployeeReportEntity.setFixedPhoneCost(telephoneTotal);
							costCenterEmployeeReportEntity.setDate(lastMonth.getTime());
							costCenterEmployeeReportEntity.setLastUpdateTime(new Date());
							costCenterEmployeeReportEntity.setCostCenter(costCenter);
							costCenterEmployeeReportEntitys.add(costCenterEmployeeReportEntity);
							
						}
						
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
				/**/
				clearData(year,month);
				costCenterReportRepository.save(costCenterReportEntitys);
				costCenterEmployeeReportRepository.save(costCenterEmployeeReportEntitys);
				
				log.info("costCenterReport count:="+costCenterReportEntitys.size()+" date:"+sdf.format(lastMonth.getTime()));
				log.info("costCenterEmployeeReport  count:="+costCenterEmployeeReportEntitys.size()+" date:"+sdf.format(lastMonth.getTime()));
				
				costCenterReportEntitys.clear();
				costCenterEmployeeReportEntitys.clear();
				
				
				if(beginMonthStr.equals(lastMonthStr))
				{
					break;
				}
				
				lastMonth.add(Calendar.MONTH, -1);
				
				
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		

		
		
		
		
		
		
	}
	
	public void clearData(int year,int month)
	{
		 Date begin = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		 Date end = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		 
		 List<CostCenterReportEntity> costCenterReportEntityList = costCenterReportRepository.findByMonth(begin, end);
		 List<CostCenterEmployeeReportEntity> costCenterEmployeeReportEntityList = costCenterEmployeeReportRepository.findByMonth(begin, end);
			
		 costCenterReportRepository.delete(costCenterReportEntityList);
		 costCenterEmployeeReportRepository.delete(costCenterEmployeeReportEntityList);
		
		 log.info("clear date "+year+"-"+month+" success.");
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		CostCenterReportTask t = context.getBean(CostCenterReportTask.class);

		//double d = t.computeTelephoneCostByMonth("65315744", 2015, 8);
		
		
		//System.out.println(d);
		
		//t.clearData(2015, 8);
		//t.clearData(2015, 9);
		//t.clearData(2015, 10);
		
		
		t.exec();
		
				//CostCenterReportEntity c = new CostCenterReportEntity();
				
				//c.setCostCenter("8001000200");
		
	}
	
	
	
	
}
