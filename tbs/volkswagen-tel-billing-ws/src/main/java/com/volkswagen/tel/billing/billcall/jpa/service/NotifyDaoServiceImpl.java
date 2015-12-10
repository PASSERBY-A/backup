package com.volkswagen.tel.billing.billcall.jpa.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.xmlbeans.impl.jam.annotation.LineDelimitedTagParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.itim.ws.model.WSPerson;
import com.volkswagen.tel.billing.billcall.biz.util.TaskQueue;
import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.NotifyRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.TelephoneBillRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.UserTelephoneRepository;
import com.volkswagen.tel.billing.common.GenericConfig;
import com.volkswagen.tel.billing.common.NotifyDTO;
import com.volkswagen.tel.billing.common.TBSPersonInfo;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.excel.parser.ChinaMobileParserTask;
import com.volkswagen.tel.billing.excel.parser.ChinaUnicomParserTask;
import com.volkswagen.tel.billing.ldap.TIMService;

@Service("notifyDaoServiceImpl")
public class NotifyDaoServiceImpl implements NotifyDaoService {
	
	private static final Logger log = LoggerFactory.getLogger(NotifyDaoService.class);
	
	private static String notifyContent;
	
	static{
		
		try {
			notifyContent = IOUtils.toString(NotifyDaoServiceImpl.class.getResourceAsStream("/notify.html"));
		
		} catch (IOException e) {
			
			log.error("notify.html not found!");
		}
		
		
	}
	
	
	@Autowired
	private NotifyRepository repository;

//	@Autowired
//	private NotifyRepository notifyRepository; 
	
	@Autowired
	private TelephoneBillRepository telephoneBillRepository;
	
	@Autowired
	private TIMService timService;
	@Autowired
	private UserTelephoneRepository userTelephoneRepository;
	
	
	@Transactional
	@Override
	public NotifyEntity saveNotify(NotifyEntity entity) {

		return repository.save(entity);
	}
	@Override
	public List<NotifyEntity> findAll() {
		/*try {
			timService.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return repository.findAll();
	}
	

	public List<NotifyEntity> findBySendFlagFalse()
	{
		return repository.findBySendFlagFalse();
	}
	
	
	//
	public String getBeginMonth(String number)
	{
		
		List<TelephoneBillEntity>  list =  telephoneBillRepository.findByTelephoneNumber(number);
		
		if(list.size()==0)
		{
			return GenericConfig.onlineMonth;
			
		}
		
		Date current = new Date();
		
		Date previous = new Date();
		
		for(TelephoneBillEntity tb:list)
		{
			
			String month = tb.getYear()+"/"+tb.getMonth();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M");
			
			try {
				
				current = sdf.parse(month);
				if(current.before(previous))
				{
					previous = current;
				}
			} catch (ParseException e) {
				log.info("begin month exception.");
				return GenericConfig.onlineMonth;
			}
			
		}
		
		SimpleDateFormat resultFormat = new SimpleDateFormat("yyyyMM");
		
		return resultFormat.format(previous);
		
	}
	
	
	
	
	public synchronized List<String> getAllYearandMonth(String number) throws ParseException
	{
		
		List<Integer> years = new ArrayList<Integer>();
		
		List<String> result = new LinkedList<String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		String onlineMonthStr = StringUtils.trim(getBeginMonth(number));
		
		Calendar onlineMonthCalendar = Calendar.getInstance();
		
		onlineMonthCalendar.setTime(sdf.parse(onlineMonthStr));
		
		int onlineMonth = onlineMonthCalendar.get(Calendar.MONTH)+1;
		
		int onlineYear = onlineMonthCalendar.get(Calendar.YEAR);
		
		Calendar currentCalendar = Calendar.getInstance();
		
		currentCalendar.setTime(new Date());
		
		int currentMonth = currentCalendar.get(Calendar.MONTH)-1;
		
		int currentYear = currentCalendar.get(Calendar.YEAR);

		int yearIntervalCount = currentYear - onlineYear;
		
			for(int index=0;index<=yearIntervalCount;index++)
			{
				years.add(onlineYear+index);
			}
		
		
		for(Integer year:years)
		{
			if(year==onlineYear &&(year!=currentYear))
			{
				 
				for(int i=onlineMonth;i<=12;i++)
				{
					if(i<10)
					{
						result.add(String.valueOf(onlineYear)+"0"+i);
					}
					else{
						result.add(String.valueOf(onlineYear)+i);
					}
				}
			}
			
			
			if(year==currentYear)
			{
				
				for(int i=1;i<=currentMonth;i++)
				{
					if(i<10)
					{
						result.add(String.valueOf(currentYear)+"0"+i);
					}
					else{
						result.add(String.valueOf(currentYear)+i);
					}
				}
				
			}
			
			if(year!=onlineYear && year!=currentYear)
			{
				
				for(int i=1;i<=12;i++)
				{
					
					if(i<10)
					{
						
						result.add(String.valueOf(year)+"0"+i);
						
					}
					else{
						result.add(String.valueOf(year)+i);
					}
					
					
				}
				
			}
			
		} 
		
		List<TelephoneBillEntity> allBillMonth = this.telephoneBillRepository.findByTelephoneNumber(number);
		
		if(allBillMonth.size()==0)
		{
			result = new ArrayList<String>();
			
		}
		else{
			
			List<String> months = new ArrayList<String>();
			
			for(TelephoneBillEntity t:allBillMonth)
			{
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, t.getYear());
				calendar.set(Calendar.MONTH, t.getMonth()-1);
				months.add(sdf.format(calendar.getTime()));
			}
			
			//Collections.reverse(months);
			return  months;
		}
		
		
		return result;
		
	}
	
	
	
 	public void notification()
	{
 		int month = CommonUtil.getCurrentMonth();
 		
 		String filePath = FileUtils.getTempDirectoryPath();
 		
 		File chinaunicomFile= new File(filePath+"chinaunicom.tbs");
 		File chinamobileFile= new File(filePath+"chinamobile.tbs");
		File sumFile= new File(filePath+"sum.tbs");
		
		
 		
 		boolean chinamobile = chinaunicomFile.exists();
 		boolean chinaunicom = chinamobileFile.exists();
 		boolean sum =sumFile.exists();
 		
 		//TODO 
 		//if(true)
 		if(chinamobile  && chinaunicom && sum)
 		{
 			
 			
 	 		log.info("month : "+month +" notification begin!");
 	 		
 	 		
 	 		Map<String,TBSPersonInfo> mails = new HashMap<String, TBSPersonInfo>();
 	 		
 			WSPerson [] p = null;
 				try 
 				{
 					 timService.init();
 					 p = timService.getTBSUserList();
 					 mails = timService.getTbsPersonInfo();
 					 List<NotifyDTO> noifylist = getMail(mails);
 	 				
 	 				log.info("notify count:---"+noifylist.size());
 	 				
 	 				for(NotifyDTO notifyDTO:noifylist)
 	 				{
 	 					NotifyEntity entity = new NotifyEntity();
 	 					entity.setLastUpdateTime(new Date());
 	 					entity.setMail(notifyDTO.getMail());
 	 					entity.setMobile(notifyDTO.getMobile());
 	 					entity.setTelephone(notifyDTO.getTelephone());
 	 					entity.setContent(getContent(notifyDTO));
 	 					entity.setReceiver(notifyDTO.getReceiver());
 	 					saveNotify(entity);
 	 				}
 				} 
 				catch (Exception e) 
 				{
 					e.printStackTrace();
 				} finally{
 					
 					chinaunicomFile.delete();
 	 				chinamobileFile.delete();
 	 				sumFile.delete();
 				}
 			
 				
 				
 				
 				
 				
 				
 		}
 		
 		

	}
	
 	
 	private synchronized String getContent(NotifyDTO notifyDTO) {
 		
 		String result = new String();
 		
 		String templateFile = "notify.html";
		
		Properties prop = new Properties();
		
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		
		Velocity.init(prop);
		
        VelocityContext context = new VelocityContext();
        
        context.put("name", notifyDTO.getReceiver());
		
       // context.put("info", "billing data has been imported, please submit your private phone records as soon as possible.");
        
        context.put("tnumber", notifyDTO.getTelephone());
        //context.put("mnumber", notifyDTO.getMobile());
        
        context.put("month",CommonUtil.getLastMonth("yyyy-MM"));
        
        List telephone = new ArrayList();
        
        if(notifyDTO.getTelephoneNoBillYearAndMonth()!=null)
        {
        	
        	String s [] = notifyDTO.getTelephoneNoBillYearAndMonth().split(",");
        	
        	
        	for(String str:s)
        	{
        		if(!telephone.contains(StringUtils.trim(str)))
        		{
        			telephone.add(str);
        		}
        	}
        	//Collections.reverse(telephone);
        	
        }
        
        context.put("telphone", telephone);
        
        
        Map<String,List<String>> mobiles = new HashMap<String,List<String>>();
        
        String mobile =  notifyDTO.getMobile();
        
        if(StringUtils.isNotBlank(mobile))
        {
        	
        	 for(String m:mobile.split(","))
             {
             	
             	 List mobList = new ArrayList();
             	
             	 
             	if(StringUtils.isNotBlank(notifyDTO.getMobileNoBillYearAndMonth(m)))
             	{
             		
             		String s [] = notifyDTO.getMobileNoBillYearAndMonth(m).split(",");

                 	for(String str:s)
                 	{
                 		if(!mobList.contains(str))
                 		{
                 			mobList.add(str);
                 		}
                 	}
                 	//Collections.reverse(mobList);
                 	mobiles.put(m, mobList);
             	}
             	 
             }
        }
       
        
        
        
        
        context.put("mobiles", mobiles);
        
        Map<String,List<String>> oldTelephones = new HashMap<String,List<String>>();
        
        String oldTelephone =  notifyDTO.getOldTelephone();
        
        if(StringUtils.isNotBlank(oldTelephone))
        {
        	
        	for(String o:oldTelephone.split(","))
            {
        		 List oldTList = new LinkedList();
        		
        		if(StringUtils.isNotBlank(notifyDTO.getOldtelephoneNoBillYearAndMonth(o)))
              	{
        			 
        			String s [] = notifyDTO.getOldtelephoneNoBillYearAndMonth(o).split(",");
        			 
        			for(String str:s)
                 	{
                 		if(StringUtils.isNotBlank(str) && (!oldTList.contains(StringUtils.trim(str))))
                 		{
                 			oldTList.add(str);
                 		}
                 	}
        			log.info(oldTList.toString());
        			Collections.sort(oldTList,new Comparator<String>(){

    					@Override
    					public int compare(String o1, String o2) {
    						
    						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    						Calendar c1 = Calendar.getInstance();
    						Calendar c2 = Calendar.getInstance();
    						try {
    							c1.setTime(sdf.parse(o1));
    							c2.setTime(sdf.parse(o2));
    						} catch (ParseException e) {
    							 
    							e.printStackTrace();
    						}

    						
    						
    						return c1.before(c2)?1:-1;
    					}

    					 
    				});
        			Collections.reverse(oldTList);
        			oldTelephones.put(o, oldTList);
              	}
        		
            }
        	
        	
        }
        
        context.put("oldTelephones", oldTelephones);
        
        
        Template template =  null;

        
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        
        try
        {
           //
           BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(byteout));
            template = Velocity.getTemplate(templateFile, "UTF-8");
            
            if ( template != null)
                template.merge(context, writer);
            
            /*
             *  flush and cleanup
             */
            writer.flush();
			writer.close();
			
        }
        catch( ResourceNotFoundException rnfe )
        {
        	log.error("velocity : error : cannot find template " + templateFile );
        }
        catch( ParseErrorException pee )
        {
        	log.error("velocity : Syntax error in template " + templateFile + ":" + pee );
        } catch (IOException e) {
			log.error("velocity IOException:"+e);
		}

		return byteout.toString();
	}
 	
 	public synchronized List<NotifyDTO>  getMail(Map<String,TBSPersonInfo> mails)
 	{
 		List<NotifyDTO> result = new ArrayList<NotifyDTO>();
 		

		for(String key:mails.keySet())
		{
 
			NotifyDTO notifyDTO = new NotifyDTO();
			
			notifyDTO.setMail(key);
			
			TBSPersonInfo personInfo = mails.get(key);
			
			notifyDTO.setReceiver(personInfo.getName());
			
			List<TelephoneBillEntity> list =new ArrayList<TelephoneBillEntity>();
			
			if(StringUtils.isNotBlank(personInfo.getMobile()))
			{
				notifyDTO.setMobile(personInfo.getMobile());
				for(String m:personInfo.getMobile().split(","))
				{
					
					   //有提交过的手机号  
					  list =  telephoneBillRepository.findByTelephoneNumberAndStatus(m, "SENT");
					  
					  //if(list.size()!=0)
						//mobile false
					  fillNoBillYearAndMonth(personInfo.getUsername(),m,notifyDTO,list,false);
					  
				}
			}
			 
			if(StringUtils.isNotBlank(personInfo.getTelephonenumber()))
			{
				notifyDTO.setTelephone(personInfo.getTelephonenumber());
				
				//有提交过的座机号码
				list =  telephoneBillRepository.findByTelephoneNumberAndStatus(personInfo.getTelephonenumber(), "SENT");
				
				 //if(list.size()!=0)
					//telephone true
				fillNoBillYearAndMonth(personInfo.getUsername(),personInfo.getTelephonenumber(),notifyDTO,list,true);
					
			
			}else {
				 
				try {
				  // List<String> oldtelphonBillMonths = performFixPhone(personInfo.getUsername(),notifyDTO);
					
				   //log.info(oldtelphonBillMonths.toString());
					
					performOldFixPhone(personInfo.getUsername(),notifyDTO);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				 
				 
				
				
				
				
			}
			
		
			
			
				result.add(notifyDTO);
			 
			
		}
 		
 		return result;
 		
 	}
 	
 	
	private synchronized void getNoBillYearAndMonth(String number,NotifyDTO notifyDTO,TelephoneBillEntity tb,boolean telephoneOrMobile) {
		
		StringBuffer sb = new StringBuffer();
		
		List<String> yearAndMonth = null;
	
		try {
			yearAndMonth = getAllYearandMonth(number);
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, tb.getYear());
			c.set(Calendar.MONTH, tb.getMonth()-1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String activedMonthAndYear = sdf.format(c.getTime());
			yearAndMonth.remove(activedMonthAndYear);
			
			for(String s:yearAndMonth)
			{
				
				
				sb.append(s+",");
				
			}
			
			if(telephoneOrMobile)
			{
				//personInfo.setNoBillYearAndMonth(sb.substring(0,sb.length()-1));
				notifyDTO.setTelephoneNoBillYearAndMonth(sb.substring(0,sb.length()-1));
				
			}
			else{
				notifyDTO.setMobileNoBillYearAndMonth(number,sb.substring(0,sb.length()-1));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
 	

	
	private synchronized void fillNoBillYearAndMonth(String userName,String number,NotifyDTO notifyDTO,List<TelephoneBillEntity> tbList,boolean telephoneOrMobile) {
		
		StringBuffer sb = new StringBuffer();
		
		List<String> yearAndMonth = null;
	
		try {
		
		yearAndMonth = getAllYearandMonth(number);
		
		Calendar l = Calendar.getInstance();
		l.setTime(new Date());
		l.add(Calendar.MONTH, -1);
		String lastMonth = new SimpleDateFormat("yyyyMM").format(l.getTime());
		
		if(yearAndMonth.contains(lastMonth))
			yearAndMonth.remove(lastMonth);
		
		Collections.sort(yearAndMonth,new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				try {
					c1.setTime(sdf.parse(o1));
					c2.setTime(sdf.parse(o2));
				} catch (ParseException e) {
					 
					e.printStackTrace();
				}

				
				
				return c1.before(c2)?1:-1;
			}

			 
		});
		
		
		
		for(TelephoneBillEntity tb:tbList)
		{
			
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, tb.getYear());
				c.set(Calendar.MONTH, tb.getMonth()-1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				String activedMonthAndYear = sdf.format(c.getTime());
				yearAndMonth.remove(activedMonthAndYear);
				 
		} 
		
		//TODO 这里需要 判断手机号 是否 inactive
  
		List<String> oldtelphonBillMonths = new ArrayList<String>();
		
		if(telephoneOrMobile)
		{
			//如果是telephone  查询是否有换座机的情况 
			
			//有开始时间   没结束时间，代表新产生 。   
			//List<String> yearAndMonth
			oldtelphonBillMonths = performFixPhone(userName,notifyDTO);
			
			
			
		}
		
		//根据 他们老大的要求改的。
		for(String s:yearAndMonth)
		{
			
			if(oldtelphonBillMonths.contains(StringUtils.trim(s)))
			{
				continue;
			}
			
			sb.append(s.substring(0, 4)+"-"+s.substring(4)+",");
			
		}
		
		
		if(telephoneOrMobile)
		{
			//处理遗留下的固话号码  未完成的账单 。 
			
			//TODO
			 //performOldFixPhone(userName,notifyDTO);
			
			//personInfo.setNoBillYearAndMonth(sb.substring(0,sb.length()-1));
			if(StringUtils.isBlank(notifyDTO.getOldtelephoneNoBillYearAndMonth(number)))
			{
				if(sb.length()!=0)
					notifyDTO.setTelephoneNoBillYearAndMonth(sb.substring(0,sb.length()-1));
			}
			
			
			
		}
		else
		{
			if(sb.length()!=0)
			notifyDTO.setMobileNoBillYearAndMonth(number,sb.substring(0,sb.length()-1));
			
		}
		
			
		}
		
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void performOldFixPhone(String userName,NotifyDTO notifyDTO) throws ParseException  
	{
		
		if(userName.equals("ELIYUA0"))
		{
			
			
			System.out.println("f");
			
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		List<UserTelephoneEntity> userTelephoneList = userTelephoneRepository.findByUserIdAndStatus(userName, "INACTIVE");
		
		for(UserTelephoneEntity userTelephoneEntity:userTelephoneList)
		{
			//if(userTelephoneEntity.getValidEndTime()!=null)
			//{
				
				StringBuffer months = new StringBuffer("");
				
				Calendar startMonth =Calendar.getInstance();
				startMonth.setTime(userTelephoneEntity.getValidStartingTime());
				startMonth.set(Calendar.DATE, 1);
				startMonth.set(Calendar.HOUR, 0);
				startMonth.set(Calendar.MINUTE, 0);
				startMonth.set(Calendar.SECOND, 0);
				
				
				Calendar endMonth = Calendar.getInstance();
				endMonth.setTime(userTelephoneEntity.getValidEndTime()==null?new Date():userTelephoneEntity.getValidEndTime());
				endMonth.set(Calendar.DATE, 1);
				endMonth.set(Calendar.HOUR, 0);
				endMonth.set(Calendar.MINUTE, 0);
				endMonth.set(Calendar.SECOND, 0);
				
				List<TelephoneBillEntity> tbeList = this.telephoneBillRepository.findByTelephoneNumberAndStatusNot(userTelephoneEntity.getTelephoneNumber(), "SENT");
				
				Calendar lastMonth  =Calendar.getInstance();
				
				for(TelephoneBillEntity tbe:tbeList)
				{
					 Calendar tempC  =Calendar.getInstance();
					 
					 tempC.set(Calendar.YEAR, tbe.getYear());
					 tempC.set(Calendar.MONTH, tbe.getMonth()-1);
					 tempC.set(Calendar.DATE, 1);
					 tempC.set(Calendar.HOUR, 0);
					 tempC.set(Calendar.MINUTE, 0);
					 tempC.set(Calendar.SECOND, 0);
					 
					 if((tempC.getTimeInMillis()> startMonth.getTimeInMillis()) && (tempC.getTimeInMillis()< endMonth.getTimeInMillis()) )
					 {
						 months.append(sdf.format(tempC.getTime())+",");
						 //lastMonth = tempC;
						// lastMonth.add(Calendar.MONTH, 1);
					 }
					
				}
				
				String oldtelephoneNoBillYearAndMonth="";
				
				/* if(userTelephoneEntity.getValidEndTime()!=null)
				 {
					 months.append(sdf.format(lastMonth.getTime()));
					 oldtelephoneNoBillYearAndMonth = months.toString();
				 }
				 else{
					 
				 oldtelephoneNoBillYearAndMonth = months.substring(0,months.length()-1); 
				 }
				 */
				
				
				
				 if(userTelephoneEntity.getValidEndTime()!=null)
				 {
					 List<TelephoneBillEntity> l = telephoneBillRepository.findByTelephoneNumber(userTelephoneEntity.getTelephoneNumber());
					 if(l!=null && l.size()>0)
					 {
						 
						String status = l.get(0).getStatus();
						 
						if(!status.equalsIgnoreCase("sent"))
						{
							 lastMonth.setTime(userTelephoneEntity.getValidEndTime());
							 months.append(","+ sdf.format(lastMonth.getTime()));
							 if(userName.equals("ELIYUA0"))
								{
								 			log.info(l.get(0).getTelephoneNumber()+""+"rrrrrrrrrr");
								 			log.info(l.get(0).getStatus()+""+"rrrrrrrrrr");
								 			log.info(lastMonth.get(Calendar.YEAR)+""+"rrrrrrrrrr");
								 			log.info(lastMonth.get(Calendar.MONTH)+""+"rrrrrrrrrr");
								}
							
							 
						}
					 }
				 } 
				
				
				 oldtelephoneNoBillYearAndMonth = months.toString();
				
				 
				 notifyDTO.setOldTelephone(userTelephoneEntity.getTelephoneNumber());
				 notifyDTO.setOldtelephoneNoBillYearAndMonth(userTelephoneEntity.getTelephoneNumber(),oldtelephoneNoBillYearAndMonth);
				 
			//}
			
			
			
		}
	}
	
	//有开始时间 没结束时间， 代表新换座机 。   
	public synchronized List<String> performFixPhone(String userName,NotifyDTO notifyDTO) throws ParseException
	{
		
		if(userName.contains("ELIFENG"))
		{
			System.out.println();
			
		}
		List<String> yearAndMonth = new LinkedList<String>();
		
		List<UserTelephoneEntity>  l = this.userTelephoneRepository.findByUserIdAndStatus(userName,"INACTIVE");
		
		
		
		if(l==null ||l.size()==0)
		{
			return yearAndMonth;
		}
		
		UserTelephoneEntity e =  l.get(0);
		
		String telephonenumber = e.getTelephoneNumber();
		
		//没有 更换固话的情况  
		if(e==null)
		{
			return yearAndMonth;
		}
		
		//此人此时的固话和  号码不相同 也不处理 
	/*	if(!telephonenumber.equals(e.getTelephoneNumber()))
		{
			return yearAndMonth;
		}*/
		
		
		
		//可能是新入职的员工   用老座机的情况   之前的 月份  就得被remove 掉
		//需要查询未完成帐单的月份 不能低于  validStartingTime  
		if(e.getValidEndTime()==null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			
			
			
			Calendar startMonth = Calendar.getInstance();
			
			startMonth.setTime(e.getValidStartingTime()==null?sdf.parse("201407"):e.getValidStartingTime());
			
			startMonth.set(Calendar.DATE, 1);
			startMonth.set(Calendar.HOUR, 0);
			startMonth.set(Calendar.MINUTE, 0);
			startMonth.set(Calendar.SECOND, 0);
			
			 List<TelephoneBillEntity> tbeList = this.telephoneBillRepository.findByTelephoneNumberAndStatusNot(telephonenumber, "SENT");
			
			 for(TelephoneBillEntity entity:tbeList)
			 {
				 Calendar tempC =  CommonUtil.getCalendar(entity.getYear(), entity.getMonth(), 1, 0, 0, 0);
				 
				 if(startMonth.getTimeInMillis()< tempC.getTimeInMillis())
				 {
					 
							 yearAndMonth.add(sdf.format(tempC.getTime()));
				 }
			 }
			 
			 //TODO
			 
			 
		}
		//查询 validStartingTime  validEndTime 之间的未完成的账单月份  
		else{
			
			// 这个地方应该不会有这种情况 。。。。。。
			
			
		}
		
	/*	if(yearAndMonth.size()!=0)
		{
			String s = yearAndMonth.get(yearAndMonth.size()-1);
			
			String y = s.substring(0, 4);
			
			String m = s.substring(4);
			
			for()
			
			
		}*/
		
		notifyDTO.setOldTelephone(telephonenumber);
		
		StringBuffer sb = new StringBuffer();
		for(String s:yearAndMonth)
		{
			
			TelephoneBillEntity entity =  telephoneBillRepository.findByTelephoneNumberAndYearAndMonth(telephonenumber,Integer.parseInt(s.substring(0, 4)),Integer.parseInt(s.substring(4)));
			if(!entity.getStatus().equalsIgnoreCase("sent"))
			{
				sb.append(s.substring(0, 4)+"-"+s.substring(4)+",");
			}
			
			
		}
		
		notifyDTO.setOldtelephoneNoBillYearAndMonth(telephonenumber, sb.toString());
		
		log.info(notifyDTO.toString());
		
		return yearAndMonth;
		
	}
	
	
	
	
	@Override
	@Transactional
	public void setSendFlagAndSendsendTime(long id) {
		
		  repository.setSendFlagAndSendsendTime(id,new Date());
	}
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		NotifyDaoServiceImpl n= context.getBean(NotifyDaoServiceImpl.class);
		
		NotifyDTO notifyDTO = new NotifyDTO();
		//n.performOldFixPhone("ELIQIAG",notifyDTO);
		//System.out.println(notifyDTO);
		n.notification();
		
		
		
		 
		//System.out.println(n.getAllYearandMonth("65318888"));
		
		//System.out.println(n.getAllYearandMonth(""));
		
		
		//System.out.println("201301".substring(0,4));
		
		//System.out.println("201301".substring(4));
	}
 
 
	
	
	 
	
}




