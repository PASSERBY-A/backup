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

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.BillCallRecordDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillSumDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.TBSPersonInfo;
import com.volkswagen.tel.billing.common.util.PreciseCompute;
import com.volkswagen.tel.billing.ldap.TIMService;

@Component
public class ReportBizService {
	
	private static final Logger log = LoggerFactory .getLogger(ReportBizService.class);
 
	@Autowired
	private TelephoneBillDaoService telephoneBillDaoService;
	
	@Autowired
	private UserTelephoneDaoService userTelephoneDaoService;

	@Autowired
	private TelephoneBillSumDaoService telephoneBillSumDaoService;
	
	@Autowired
	private BillCallRecordDaoService billCallRecordDaoService;
	
	 
	
	@Autowired
	private TIMService timeService;
	
	
	public JSONObject findAllOpenBills(final String type,int year,int month,int page, int size)
	{
		try {
			timeService.init();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		JSONObject jObj = new JSONObject();
		 
		JSONArray resultList = new JSONArray();
		
		List<TelephoneBillEntity> list = telephoneBillDaoService.findAllOpenBills(type, year, month, page, size, Direction.ASC, "lastUpdateTime");
		
		resultList =convertToJSONArrayStructure(list,false);
		
		boolean isEditable = true;
		
		long iTotalRecords = telephoneBillDaoService.countOpenBillsByType(type, year, month);
				
		long iTotalDisplayRecords = iTotalRecords;
	
		
		jObj.put("resultList", resultList);
		jObj.put("isEditable", isEditable);
		jObj.put("iTotalRecords",iTotalRecords);
		jObj.put("iTotalDisplayRecords", iTotalDisplayRecords);
		
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");
		
		return jObj;
		
	}

	
	public JSONObject findAllCostCenter(int year,int month,int page, int size)
	{
		
		JSONObject jObj = new JSONObject();
		
		List<String> costCenterList =Collections.EMPTY_LIST;
		
		List<String> pageList = new LinkedList<String>();
		
		pageList.clear();
		
		page = page<=0?1:page;
		
		try {
			
			int beginRow =0;
			int endRow=0;
			timeService.init();
			
			costCenterList = timeService.findAllCostCenter();
			
			
			if(size<costCenterList.size())
			{
				beginRow = (page-1)*size;
				endRow = beginRow+size;
				
				if(endRow>costCenterList.size())
				{
					endRow = costCenterList.size()-1;
				}
				
			} else{
				
				endRow = costCenterList.size()-1;
				
			}
			
			for(int i=beginRow;i<=endRow ;i++)
			{
				pageList.add(costCenterList.get(i));
			}
			
			
			
			boolean isEditable = true;
			
			long iTotalRecords = costCenterList.size();
			
			long iTotalDisplayRecords = iTotalRecords;
			
			JSONArray resultList = new JSONArray();
			
			resultList = allCostCenterToJsonArray(pageList, year, month,isEditable);
			
			jObj.put("resultList", resultList);
			jObj.put("isEditable", isEditable);
			jObj.put("iTotalRecords",iTotalRecords);
			jObj.put("iTotalDisplayRecords",iTotalDisplayRecords);
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
			jObj.put("date", year+"/"+month);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		
		
		
		
		
		
		return jObj;
		
	}
	
	private JSONArray allCostCenterToJsonArray(List<String> pageList, int year,int month, boolean isEditable) {
			
		
		JSONArray data = new JSONArray();
		
		for(String costcenter:pageList)
		{
			
			JSONArray row = new JSONArray();
		 
			Map<String, Double>  totals = computeCostCenterTelephoneAndMobileTotalByMonth(costcenter,year,month);
			
			double cellTotal=totals.get("cellTotal");
			double fixTotal = totals.get("fixTotal");
			row.add(costcenter);
			row.add(String.valueOf(PreciseCompute.round(fixTotal, 2)));
			row.add(String.valueOf(PreciseCompute.round(cellTotal, 2)));
			
			data.add(row);
		}
		
		
		
		return data;
	
	
	
	
	
	}


	public JSONObject findCostCenter(final String costCenter,int year,int month,int page, int size)
	{
		JSONObject jObj = new JSONObject();
		
		List<TBSPersonInfo> user =Collections.EMPTY_LIST;
		
		List<TBSPersonInfo> pageUser = new LinkedList<TBSPersonInfo>();
		
		pageUser.clear();
		
		page = page<=0?1:page;
		
		try {
			int beginRow =0;
			int endRow=0;
			timeService.init();
			
			user = timeService.findPersonInfoListByAttribute("costcenter", costCenter);
			
			if(size<user.size())
			{
				beginRow = (page-1)*size;
				endRow = beginRow+size;
				if(endRow>user.size())
				{
					endRow = user.size()-1;
				}
				
			} else{
				
				endRow = user.size()-1;
				
			}
			
			for(int i=beginRow;i<=endRow ;i++)
			{
				pageUser.add(user.get(i));
			}
				//pageUser.add(user.get(i));
				
			
			
			boolean isEditable = true;
			
			long iTotalRecords = user.size();
			
			long iTotalDisplayRecords = iTotalRecords;
			
			JSONArray resultList = new JSONArray();
			
			resultList = costCenterToJsonArray(pageUser, year, month,isEditable);
			

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
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return jObj;
		
	}
	
/*	public double computeMobileCostByMonth(String mobiles, int year, int month)
	{
		if(StringUtils.isBlank(mobiles))
			return 0;
		
		double cellTotal = 0;
		
		for(String m:mobiles.split(","))
		{
			
			Float f = telephoneBillSumDaoService.findMonthPKGByTelNumberAndMonth(m, year, month);
			
			if(f==null)
				f = Float.parseFloat("0");
			
			
			cellTotal= PreciseCompute.add(cellTotal, f);
		}
		
		
		return cellTotal;
	}
	
	public double computeTelephoneCostByMonth(String telephone, int year, int month)
	{
		if(StringUtils.isBlank(telephone))
			return 0;
		
		double fixTotal = 0;

		List<Float> f = billCallRecordDaoService.findCostByCallingNumberAndYearAndMonth(telephone, year, month);
		
		for(Float v:f)
		{
			if(v==null)
				v = Float.parseFloat("0");
			
			fixTotal = PreciseCompute.add(fixTotal, v);
			
		}
		
		return fixTotal;
		
	}
	*/
	
	
	public double computeMobileCostByMonth(String mobiles, int year, int month)
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
			timeService.init();
			user = timeService.findPersonInfoListByAttribute("costcenter", costCenter);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		double cellTotal = 0;
		double fixTotal = 0;
		for(TBSPersonInfo u:user)
		{
			double c = computeMobileCostByMonth(u.getMobile(),year,month);
			cellTotal = PreciseCompute.round(PreciseCompute.add(c, cellTotal),2);
			double f = computeTelephoneCostByMonth(u.getTelephonenumber(),year,month);
			fixTotal = PreciseCompute.round(PreciseCompute.add(f,fixTotal),2);
			
		}
		
		
		result.put("cellTotal", PreciseCompute.round(cellTotal, 2));
		result.put("fixTotal", PreciseCompute.round(fixTotal, 2));
		
		
		return result;
		
	}
	
	
	
	
	public JSONArray costCenterToJsonArray(List<TBSPersonInfo> user,int year,int month,boolean isEditable) {
		
			JSONArray data = new JSONArray();
			
			for (TBSPersonInfo ent : user) {
				
				JSONArray row = new JSONArray();
			 
				String mobiles = ent.getMobile();
				String telephone = ent.getTelephonenumber();
				
				
				double cellTotal=0;
				
				double fixTotal = 0;
				
				//查手机
				if(StringUtils.isNotBlank(mobiles))
				{
					cellTotal = computeMobileCostByMonth(mobiles,year,month);
					
				}
				if(StringUtils.isNotBlank(telephone))
				{
					
					fixTotal = computeTelephoneCostByMonth(telephone,year,month);
					
				}
				
				
				row.add(ent.getEnumber()==null?" ":ent.getEnumber());
				row.add(ent.getName());
				row.add(String.valueOf(PreciseCompute.round(fixTotal, 2)));
				row.add(String.valueOf(PreciseCompute.round(cellTotal, 2)));
				
				data.add(row);
			}
		
		return data;
		
		
	}

	//public JSONObject getBillCallRecordsByTelAndMonth(String userId, String telephoneNumber, int year, int month, int pageNumber, int displayLength);
	
	
	
	
	
	/**
	 * Convert call record list to json array for front-end display.
	 * @param list - call record list fetched from db.
	 * @param isEditable - flag for identifying if the call record can be editable on front-end.
	 * @return
	 */
	private JSONArray convertToJSONArrayStructure(List<TelephoneBillEntity> list, boolean isEditable) {
		JSONArray data = null;

		if (list != null) {
			data = new JSONArray();
			for (TelephoneBillEntity ent : list) {
				JSONArray row = new JSONArray();
				UserTelephoneEntity entity = userTelephoneDaoService.findByTelephoneNumber(ent.getTelephoneNumber());
				TBSPersonInfo person = null;
				if(entity!=null)
				{
					try {
						person = timeService.findPersonInfoByAttribute("uid", entity.getUserId());
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}
					
				}
				
				row.add(person!=null?person.getEnumber():"Intern/External");
				row.add(person!=null?person.getName():" ");
				row.add(ent.getTelephoneNumber());
				row.add(person!=null?person.getEmail():" ");
				row.add(ent.getVendorName()); 				 
				row.add(isEditable);					 

				data.add(row);
			}
		}

		return data;
	}	
	
	
	
	
}
