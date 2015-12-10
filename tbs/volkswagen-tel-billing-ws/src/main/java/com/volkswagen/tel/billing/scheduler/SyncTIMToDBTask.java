package com.volkswagen.tel.billing.scheduler;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.ibm.itim.ws.exceptions.WSApplicationException;
import com.ibm.itim.ws.exceptions.WSLoginServiceException;
import com.ibm.itim.ws.model.WSAttribute;
import com.ibm.itim.ws.model.WSPerson;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.billcall.rest.UserInfoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.ldap.TIMService;

@Service("syncTIMToDBTask")
public class SyncTIMToDBTask {

	private static final Logger log = LoggerFactory
			.getLogger(SyncTIMToDBTask.class);
	private static final String ACTIVE = "ACTIVE";
	private static final String INACTIVE = "INACTIVE";
	//private static final Date NOW = new Date();
	
	@Autowired
	private TIMService timeService;

	@Autowired
	private UserTelephoneDaoService userTelephoneDaoService;
	
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * synchronize LDAP and DB data everyday
	 * 
	 * @throws Exception
	 */
	public void synchronize() throws Exception {

		log.info(">>>>>>synchronize LDAP and DB on " + new Date());

		timeService.init();
		syncLDAPAndDB();
	}

	/**
	 * check the telephone numbers of users in LDAP, update them in DB
	 * 
	 * @throws RemoteException
	 * @throws WSApplicationException
	 * @throws WSLoginServiceException
	 * @throws ServiceException
	 * @throws MalformedURLException
	 */
	private void syncLDAPAndDB() throws WSLoginServiceException,
			WSApplicationException, RemoteException, MalformedURLException,
			ServiceException {
		
		WSPerson[] userList = timeService.getTBSUserList();

		String uid = "";
		String adCreateTime = null;

		for (int i = 0; i < userList.length; i++) {
			WSPerson person = userList[i];
			WSAttribute[] attributes = person.getAttributes();
			
			ArrayList<String> telephoneNumbersInLdap = new ArrayList<String>();
			
			ArrayList<String> mobilephoneNumbersInLdap = new ArrayList<String>();


			for (int k = 0; k < attributes.length; k++) {

				if (attributes[k].getName().equalsIgnoreCase("telephonenumber")) {
					
					String values[] = attributes[k].getValues();
					for(int m=0;m < values.length;m++ ){
						String completeNumber = attributes[k].getValues()[m].replaceAll(" ", "");
						if(completeNumber.length()>=8){
							telephoneNumbersInLdap.add(completeNumber.substring(completeNumber.length() - 8));
						}else{
							telephoneNumbersInLdap.add(completeNumber);
						}
					}
					
				} else if (attributes[k].getName().equalsIgnoreCase("mobile")) {
					
					String values[] = attributes[k].getValues();
					for(int m=0;m< values.length;m++ ){
						String completeNumber = attributes[k].getValues()[m].replaceAll(" ", "");
						if(completeNumber.length()>=11){
							mobilephoneNumbersInLdap.add(completeNumber.substring(completeNumber.length() - 11));
						}else{
							telephoneNumbersInLdap.add(completeNumber);
						}
					}
					
				} else if (attributes[k].getName().equalsIgnoreCase("uid")) {
					uid = attributes[k].getValues()[0];	
				} else if (attributes[k].getName().equalsIgnoreCase("adcreatetime")) {
					adCreateTime = attributes[k].getValues()[0];	
				}
			}

			List<UserTelephoneEntity> userTelephoneList = null;
			try {
				userTelephoneList = userTelephoneDaoService
						.findTelephonesByUserId(uid);
			} catch (NullPointerException e) {
				userTelephoneList = null;
			}
			
			Date accountCreatedTime  = null;
						
			if(adCreateTime!=null){
				accountCreatedTime = CommonUtil.toDate(adCreateTime.substring(0,14), "yyyyMMddHHmmss");
			}
			
			if (telephoneNumbersInLdap != null) {
				for(String telephoneNumberInLdap: telephoneNumbersInLdap){
					boolean isTelNumberInDB = isNumberInDB(userTelephoneList,
							telephoneNumberInLdap, accountCreatedTime);
					// if telephonenumber is not in DB for user
					if (!isTelNumberInDB) {
						UserTelephoneEntity newUserTelephone = new UserTelephoneEntity();
						newUserTelephone.setLastUpdateTime(new Date());
						newUserTelephone.setValidStartingTime(accountCreatedTime);
						newUserTelephone.setStatus(ACTIVE);
						newUserTelephone.setUserId(uid);
						newUserTelephone.setTelephoneNumber(telephoneNumberInLdap);
						userTelephoneDaoService.saveUserTelephone(newUserTelephone);

						log.info("add a telephonenumber " + telephoneNumberInLdap
								+ " for user " + uid);
					}
				}
			}

			if (mobilephoneNumbersInLdap != null) {
				
				for(String  mobilephoneNumberInLdap :mobilephoneNumbersInLdap){
					
					boolean isMobileNumberInDB = isNumberInDB(userTelephoneList,
							mobilephoneNumberInLdap, accountCreatedTime);

					// if mobilenumber is not in DB for user
					if (!isMobileNumberInDB) {
						UserTelephoneEntity newUserTelephone = new UserTelephoneEntity();
						newUserTelephone.setLastUpdateTime(new Date());
						newUserTelephone.setValidStartingTime(accountCreatedTime);
						newUserTelephone.setStatus(ACTIVE);
						newUserTelephone.setUserId(uid);
						newUserTelephone
								.setTelephoneNumber(mobilephoneNumberInLdap);
						userTelephoneDaoService.saveUserTelephone(newUserTelephone);

						log.info("add a mobilenumber " + mobilephoneNumberInLdap
								+ " for user " + uid);
					}
				}
			}

			// Disable telephone for user
			if (userTelephoneList != null) {
				for (int j = 0; j < userTelephoneList.size(); j++) {
					UserTelephoneEntity userTelphone = userTelephoneList.get(j);
					
					String numberInDB = userTelphone.getTelephoneNumber();
					
					boolean isValid = false;
					for(String telephoneNumber : telephoneNumbersInLdap){
						if(numberInDB.equalsIgnoreCase(telephoneNumber)){
							isValid = true;
							break;
						}
					}
					
					if(!isValid){
						for(String mobileNumber : mobilephoneNumbersInLdap){
							if(numberInDB.equalsIgnoreCase(mobileNumber)){
								isValid = true;
								break;
							}
						}
					}
					
					if(!isValid){
						userTelphone.setLastUpdateTime(new Date());
						userTelphone.setValidEndTime(new Date());
						userTelphone.setStatus(INACTIVE);
						userTelephoneDaoService.saveUserTelephone(userTelphone);
					}
				}
			}
		}
		log.info("update user-telephone list finished");
	}

	/**
	 * check if the telephone number is already stored in database for user
	 * 
	 * @param userTelephoneList
	 * @param number
	 * @return
	 */
	private boolean isNumberInDB(List<UserTelephoneEntity> userTelephoneList,
			String number, Date accountCreatedTime) {

		boolean isTelNumberInDB = false;
		if (userTelephoneList != null) {
			for (int i = 0; i < userTelephoneList.size(); i++) {
				UserTelephoneEntity userTelephone = userTelephoneList.get(i);
				String telephonenumber = userTelephone.getTelephoneNumber();
				if (telephonenumber.equalsIgnoreCase(number)) {
					isTelNumberInDB = true;
					
					Date validStartTime = userTelephone.getValidStartingTime();
					
					if( accountCreatedTime!=null){						
						if(validStartTime!=null){
							if(validStartTime.getTime() != accountCreatedTime.getTime()){
								
								if(validStartTime.before(accountCreatedTime))
								{
									userTelephone.setValidStartingTime(accountCreatedTime);
									userTelephone.setLastUpdateTime(new Date());
									userTelephoneDaoService.saveUserTelephone(userTelephone);
									log.info("update valid start time to: " + accountCreatedTime +" for nubmer: " + number );
								}
							
							}
						}else{
							userTelephone.setValidStartingTime(accountCreatedTime);
							userTelephone.setLastUpdateTime(new Date());
							userTelephoneDaoService.saveUserTelephone(userTelephone);
							log.info("update valid start time to: " + accountCreatedTime +" for nubmer: " + number );
						}
					}
					return isTelNumberInDB;
				}
			}
		}
		return isTelNumberInDB;
	}
	
	public static void main(String[] args) throws Exception {
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		
		SyncTIMToDBTask s = context.getBean(SyncTIMToDBTask.class);
		
		s.synchronize();
		
		
		
		
	}
	
	
}