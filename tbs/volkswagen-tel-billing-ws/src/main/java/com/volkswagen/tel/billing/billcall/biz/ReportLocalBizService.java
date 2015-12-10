package com.volkswagen.tel.billing.billcall.biz;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.BillCallRecordDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.CostCenterEmployeeReportDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.CostCenterReportDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillSumDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.TBSPersonInfo;
import com.volkswagen.tel.billing.common.util.PreciseCompute;
import com.volkswagen.tel.billing.ldap.TIMService;

@Component
public class ReportLocalBizService {
	
	private static final Logger log = LoggerFactory .getLogger(ReportLocalBizService.class);
 
	@Autowired
	private CostCenterEmployeeReportDaoService costCenterEmployeeReportDaoService;
	
	@Autowired
	private CostCenterReportDaoService costCenterReportDaoService;
	
	
/*	@Autowired
	private UserTelephoneDaoService userTelephoneDaoService;

	@Autowired
	private TelephoneBillSumDaoService telephoneBillSumDaoService;
	
	@Autowired
	private BillCallRecordDaoService billCallRecordDaoService;*/
	
	
	@Autowired
	private TIMService timeService;
	

	public JSONObject findCostCenter(String costCenter,int year,int month,int page, int size)
	{
		List<CostCenterEmployeeReportEntity> costCenterList = costCenterEmployeeReportDaoService.findByCostCenterPage(costCenter, year, month, page, size, Direction.ASC, "");
		
		JSONObject jObj = new JSONObject();
		
		boolean isEditable = true;
		
		long iTotalRecords = costCenterEmployeeReportDaoService.countCostCenter(costCenter, year, month);
		
		long iTotalDisplayRecords = iTotalRecords;
		
		JSONArray resultList = new JSONArray();
		
		resultList = allemployeeCostCenterToJsonArray(costCenterList, year, month,isEditable);
		
		jObj.put("resultList", resultList);
		jObj.put("isEditable", isEditable);
		jObj.put("iTotalRecords",iTotalRecords);
		jObj.put("iTotalDisplayRecords",iTotalDisplayRecords);
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");
		jObj.put("date", year+"/"+month); 
			
		
		Map<String, Double> total = computeCostCenterTelephoneAndMobileTotalByMonth(costCenter,year,month);
		
		jObj.put("fixTotal", String.valueOf(total.get("fixTotal")));
		jObj.put("cellTotal", String.valueOf(total.get("cellTotal")));	
		 
		return jObj;
		
	}

	public JSONObject findAllCostCenter(String type,int year,int month,int page, int size)
	{
		JSONObject jObj = new JSONObject();
		
		List<CostCenterReportEntity> costCenterPage = Collections.EMPTY_LIST;
		
		long iTotalRecords =0;
		try {
			if(StringUtils.isBlank(type))
			{
				costCenterPage = costCenterReportDaoService.findAllPage(year, month, page, size, Direction.DESC, "lastUpdateTime");
				iTotalRecords = costCenterReportDaoService.countAllCostCenter(year, month);
			}
			else{
				
				costCenterPage = costCenterReportDaoService.findByCostCenterPage(type, year, month, page, size, Direction.DESC, "lastUpdateTime");
				iTotalRecords = costCenterReportDaoService.countCostCenter(type, year, month);
			}
			
			long iTotalDisplayRecords = iTotalRecords;
			
			JSONArray resultList = new JSONArray();
			
			resultList = allCostCenterToJsonArray(costCenterPage, year, month,true);
			

			jObj.put("resultList", resultList);
			jObj.put("isEditable", true);
			jObj.put("iTotalRecords",iTotalRecords);
			jObj.put("iTotalDisplayRecords",iTotalDisplayRecords);
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
			jObj.put("date", year+"/"+month);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return jObj;
		
	}
	
	
 
	
	
	

	private Map<String, Double> computeCostCenterTelephoneAndMobileTotalByMonth(
			String costCenter, int year, int month) {
		
		Map<String,Double> result = new HashMap<String,Double>();
		
		List<CostCenterEmployeeReportEntity>  list = costCenterEmployeeReportDaoService.findByCostCenter(costCenter, year, month);
		
		double cellTotal = 0;
		double fixTotal = 0;
		
		for(CostCenterEmployeeReportEntity u:list)
		{
			cellTotal = PreciseCompute.round(PreciseCompute.add(u.getCellphoneCost(), cellTotal),2);
			fixTotal = PreciseCompute.round(PreciseCompute.add(u.getFixedPhoneCost(),fixTotal),2);
		}
		
		
		result.put("cellTotal", PreciseCompute.round(cellTotal, 2));
		result.put("fixTotal", PreciseCompute.round(fixTotal, 2));
		
		return result;
	}

	private JSONArray allCostCenterToJsonArray(
			List<CostCenterReportEntity> costCenterList, int year,
			int month, boolean isEditable) {
		
		JSONArray data = new JSONArray();
		
		for(CostCenterReportEntity entity:costCenterList)
		{
			
			JSONArray row = new JSONArray();
			
			row.add(entity.getCostCenter());
			row.add(String.valueOf(PreciseCompute.round(entity.getFixedPhoneCost(), 2)));
			row.add(String.valueOf(PreciseCompute.round(entity.getCellphoneCost(), 2)));
			data.add(row);
			
		}
		return data;
	}
	

	public JSONArray allemployeeCostCenterToJsonArray(
			List<CostCenterEmployeeReportEntity> costCenterList, int year,
			int month, boolean isEditable) {
		
		JSONArray data = new JSONArray();
		
		for(CostCenterEmployeeReportEntity entity:costCenterList)
		{
			
			JSONArray row = new JSONArray();
			row.add(entity.getStaffCode()==null?"Intern/External":entity.getStaffCode());
			row.add(entity.getName());
			row.add(String.valueOf(PreciseCompute.round(entity.getFixedPhoneCost(), 2)));
			row.add(String.valueOf(PreciseCompute.round(entity.getCellphoneCost(), 2)));
			data.add(row);
			
		}
		return data;
	}
	
	
	
}
