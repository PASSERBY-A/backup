package com.volkswagen.tel.billing.ldap;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.itim.ws.client.constants.WSObjectCategoryConstants;
import com.ibm.itim.ws.exceptions.WSApplicationException;
import com.ibm.itim.ws.exceptions.WSInvalidLoginException;
import com.ibm.itim.ws.exceptions.WSLoginServiceException;
import com.ibm.itim.ws.exceptions.WSUnsupportedVersionException;
import com.ibm.itim.ws.model.WSAttribute;
import com.ibm.itim.ws.model.WSOrganizationalContainer;
import com.ibm.itim.ws.model.WSPerson;
import com.ibm.itim.ws.model.WSRole;
import com.ibm.itim.ws.model.WSSession;
import com.ibm.itim.ws.services.WSPersonService;
import com.ibm.itim.ws.services.WSRoleService;
import com.ibm.itim.ws.services.facade.ITIMWebServiceFactory;
import com.volkswagen.tel.billing.common.TBSPersonInfo;

/**
 * this class is used to execute some TIM LDAP related operations
 * 
 * @author ELZHNTA
 * 
 */
public class TIMService {

	private static final Logger log = LoggerFactory.getLogger(TIMService.class);

	private String timLDAPHost;

	private String timLookupPassword;

	private String timLookupUser;

	private String timLookupBaseDN;

	public String getTimLDAPHost() {
		return timLDAPHost;
	}

	public void setTimLDAPHost(String timLDAPHost) {
		this.timLDAPHost = timLDAPHost;
	}

	public String getTimLookupPassword() {
		return timLookupPassword;
	}

	public void setTimLookupPassword(String timLookupPassword) {
		this.timLookupPassword = timLookupPassword;
	}

	public String getTimLookupUser() {
		return timLookupUser;
	}

	public void setTimLookupUser(String timLookupUser) {
		this.timLookupUser = timLookupUser;
	}

	public String getTimLookupBaseDN() {
		return timLookupBaseDN;
	}

	public void setTimLookupBaseDN(String timLookupBaseDN) {
		this.timLookupBaseDN = timLookupBaseDN;
	}

	public static final String PERSON_FILTER = "(&(objectclass=organizationalPerson)(uid={0}))";

	private static ITIMWebServiceFactory webServiceFactory = null;

	private static WSSession session = null;

	private static WSPersonService personService = null;
	private static String containerProfile = WSObjectCategoryConstants.ORG;

	/**
	 * to initialize the TIMService
	 * 
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws WSInvalidLoginException
	 * @throws WSLoginServiceException
	 * @throws RemoteException
	 */
	public void init() throws MalformedURLException, ServiceException,
			WSInvalidLoginException, WSLoginServiceException, RemoteException {

		webServiceFactory = new ITIMWebServiceFactory(timLDAPHost);

		session = webServiceFactory.getWSSessionService().login(timLookupUser,
				timLookupPassword);

		personService = webServiceFactory.getWSPersonService();
	}

	/**
	 * create a new person in TIM server
	 * 
	 * @param uid
	 * @param givenName
	 * @param surname
	 * @param mail
	 * @param telephoneNumber
	 * @param mobile
	 * @throws WSInvalidLoginException
	 * @throws WSLoginServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public void addPerson(String uid, String givenName, String surname,
			String mail, String telephoneNumber, String mobile)
			throws WSInvalidLoginException, WSLoginServiceException,
			RemoteException, MalformedURLException, ServiceException {

		Collection<WSAttribute> attrList = new ArrayList<WSAttribute>();
		WSAttribute wsAttr = new WSAttribute("uid", new String[] { uid });
		attrList.add(wsAttr);
		// Populate the mandatory cn and sn attributes
		wsAttr = new WSAttribute("cn",
				new String[] { givenName + " " + surname });
		attrList.add(wsAttr);
		wsAttr = new WSAttribute("sn", new String[] { surname });
		attrList.add(wsAttr);

		wsAttr = new WSAttribute("givenname", new String[] { givenName });
		attrList.add(wsAttr);

		wsAttr = new WSAttribute("mail", new String[] { mail });
		attrList.add(wsAttr);

		attrList.add(wsAttr);

		if (telephoneNumber != null) {
			wsAttr = new WSAttribute("telephonenumber",
					new String[] { telephoneNumber });
			attrList.add(wsAttr);
		}

		if (mobile != null) {
			wsAttr = new WSAttribute("mobile", new String[] { mobile });
			attrList.add(wsAttr);
		}

		// Add any more attrs to the Collection attrList, and set
		// attributes on person object.
		WSAttribute[] wsAttrs = (WSAttribute[]) attrList
				.toArray(new WSAttribute[attrList.size()]);
		WSPerson wsPerson = new WSPerson();
		wsPerson.setProfileName("VGCPerson");
		wsPerson.setAttributes(wsAttrs);
		// Submit a person create request
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		personService.createPerson(session, wsContainer, wsPerson, calendar);
		log.info("Submitted person create uid = " + uid);

	}

	/**
	 * add a role for person
	 * 
	 * @param personDN
	 * @throws WSInvalidLoginException
	 * @throws WSLoginServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public void addRole(String personDN) throws WSInvalidLoginException,
			WSLoginServiceException, RemoteException, MalformedURLException,
			ServiceException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		personService.addRole(session, personDN,
				"erglobalid=1331114540348238546," + timLookupBaseDN, calendar);
	}

	/**
	 * search person by uid
	 * 
	 * @param uid
	 * @return
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson searchPersonByUid(String uid)
			throws WSLoginServiceException, WSApplicationException,
			RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile" };

		String filter = "(uid=" + uid + ")";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		if (persons != null) {
			return persons[0];
		}
		return null;
	}

	/**
	 * search person by person dn
	 * 
	 * @param erglobalid
	 * @return
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson searchPersonByDN(String erglobalid)
			throws WSLoginServiceException, WSApplicationException,
			RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile" };

		String filter = "(erglobalid=" + erglobalid + ")";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		if (persons != null) {
			return persons[0];
		}
		return null;
	}

	/**
	 * update attribute value for user
	 * 
	 * @param attributeName
	 * @param value
	 * @param uid
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public void updateAttribute(String attributeName, String value, String uid)
			throws WSLoginServiceException, WSApplicationException,
			RemoteException {

		WSPerson person = searchPersonByUid(uid);

		WSAttribute[] attributes = person.getAttributes();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		boolean isAttributeNotExist = true;
		boolean isChanged = false;

		for (int i = 0; i < attributes.length; i++) {
			String attrName = attributes[i].getName();
			if (attrName.equalsIgnoreCase(attributeName)) {
				isAttributeNotExist = false;
				if (!value.equals(attributes[i].getValues()[0])) {
					String[] values = { value };
					attributes[i].setValues(values);
					isChanged = true;
				}
				break;
			}
		}
		if (isAttributeNotExist && !isChanged) {
			WSAttribute wsAttr = new WSAttribute(attributeName,
					new String[] { value });
			Collection<WSAttribute> attrList = new ArrayList<WSAttribute>();
			for (int i = 0; i < attributes.length; i++) {
				attrList.add(attributes[i]);
			}
			attrList.add(wsAttr);
			attributes = (WSAttribute[]) attrList
					.toArray(new WSAttribute[attrList.size()]);
			personService.modifyPerson(session, person.getItimDN(), attributes,
					calendar);
		} else if (!isAttributeNotExist && isChanged) {

			personService.modifyPerson(session, person.getItimDN(), attributes,
					calendar);
		}
	}

	/**
	 * delete person from server,
	 * 
	 * @param personDN
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public void deletePerson(String personDN) throws WSLoginServiceException,
			WSApplicationException, RemoteException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		// for security reason, only suspend users
		personService.suspendPerson(session, personDN);

	}

	/**
	 * retrieve the person list
	 * 
	 * @return
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson[] getPersonList() throws WSLoginServiceException,
			WSApplicationException, RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile","staffcode" };

		String filter = "(objectclass=VGCPerson)";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		return persons;
	}

	/**
	 * retrieve user list according to role name
	 * 
	 * @param roleName
	 * @param persons
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws WSUnsupportedVersionException
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public ArrayList<WSPerson> getUserListByRole(String roleName,
			WSPerson[] persons) throws MalformedURLException, ServiceException,
			WSUnsupportedVersionException, WSLoginServiceException,
			WSApplicationException, RemoteException {

		ArrayList<WSPerson> userList = new ArrayList<WSPerson>();
		for (int i = 0; i < persons.length; i++) {
			WSRole[] roles = personService.getPersonRoles(session,
					persons[i].getItimDN());
			if (roles != null) {
				for (WSRole role : roles) {
					if (role.getName().equalsIgnoreCase(roleName)) {
						userList.add(persons[i]);
						break;
					}
				}
			}
		}
		return userList;
	}

	/**
	 * retrieve the user info according to uid
	 * 
	 * @param uid
	 * @return
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public Map<String, Object> getUserInfo(String uid)
			throws WSLoginServiceException, WSApplicationException,
			RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile","costcenter","employeenumber" };

		String filter = "(uid=" + uid + ")";
		Map<String, Object> userInfo = new HashMap<String, Object>();

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		if (persons != null) {
			WSAttribute[] attributes = persons[0].getAttributes();

			for (int i = 0; i < attributes.length; i++) {

				if (attributes[i].getName().equalsIgnoreCase("sn")) {
					userInfo.put("surname", attributes[i].getValues()[0]);
				} else if (attributes[i].getName()
						.equalsIgnoreCase("givenname")) {
					userInfo.put("givenname", attributes[i].getValues()[0]);
				} else if (attributes[i].getName().equalsIgnoreCase("mobile")) {
					userInfo.put("mobile", attributes[i].getValues()[0]);
				} else if (attributes[i].getName().equalsIgnoreCase("mail")) {
					userInfo.put("mail", attributes[i].getValues()[0]);
				} else if (attributes[i].getName().equalsIgnoreCase("telephonenumber")) {
					userInfo.put("telephonenumber",attributes[i].getValues()[0]);
				} else if (attributes[i].getName().equalsIgnoreCase("uid")) {
					userInfo.put("uid", attributes[i].getValues()[0]);
				}
				else if (attributes[i].getName().equalsIgnoreCase("costcenter")) {
					userInfo.put("costcenter", attributes[i].getValues()[0]);
				}//staffCode
				else if (attributes[i].getName().equalsIgnoreCase("employeenumber")) {
					userInfo.put("employeenumber", attributes[i].getValues()[0]);
				}
			
			
			}
		}
		return userInfo;
	}

	/**
	 * get application portal user list
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws WSUnsupportedVersionException
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson[] getTBSUserList() throws MalformedURLException,
			ServiceException, WSUnsupportedVersionException,
			WSLoginServiceException, WSApplicationException, RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile", "adcreatetime","employeenumber","costcenter"};

		String tbsRoleDN = getRole("tbs");

		String filter = "(&(objectclass=VGCPerson)(erroles=" + tbsRoleDN + "))";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);

		return persons;
	}

	/**
	 * get persons dont't belong to ap domain
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws WSUnsupportedVersionException
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson[] getNoneAPUserList() throws MalformedURLException,
			ServiceException, WSUnsupportedVersionException,
			WSLoginServiceException, WSApplicationException, RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN("erglobalid=00000000000000000000,ou=vgc,o=vgc");
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile" };

		String filter = "(&(objectclass=VGCPerson)(erroles=erglobalid=1331114540348238546,ou=roles,erglobalid=00000000000000000000,ou=vgc,o=vgc))";
		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);

		return persons;
	}

	/**
	 * check if the user is special user
	 * 
	 * @param person
	 * @return
	 * @throws WSInvalidLoginException
	 * @throws WSLoginServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public boolean isSpecialUser(WSPerson person)
			throws WSInvalidLoginException, WSLoginServiceException,
			RemoteException, MalformedURLException, ServiceException {

		WSAttribute[] attributes = person.getAttributes();

		String[] roleList = null;
		for (WSAttribute attribute : attributes) {
			if (attribute.getName().equalsIgnoreCase("erroles")) {
				roleList = attribute.getValues();
				break;
			}
		}

		boolean isSpecialUser = false;
		for (String role : roleList) {
			if (role.equalsIgnoreCase(getRole("appadmin"))) {
				isSpecialUser = true;
				break;
			} else if (role.equalsIgnoreCase(getRole("appspecuser"))) {
				isSpecialUser = true;
				break;
			} else if (role.equalsIgnoreCase(getRole("noneaprole"))) {
				isSpecialUser = true;
				break;
			}
		}

		return isSpecialUser;
	}

	/**
	 * get role dn by role name
	 * 
	 * @param roleName
	 * @return
	 * @throws WSInvalidLoginException
	 * @throws WSLoginServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String getRole(String roleName) throws WSInvalidLoginException,
			WSLoginServiceException, RemoteException, MalformedURLException,
			ServiceException {

		WSRoleService roleService = webServiceFactory.getWSRoleService();

		WSRole[] roles = roleService.searchRoles(session, "(errolename="+ roleName + ")");
		return roles[0].getItimDN();
	}

	/**
	 * retrieve the person list
	 * 
	 * @return
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws RemoteException
	 */
	public WSPerson[] getTbsPersonList() throws WSLoginServiceException,
			WSApplicationException, RemoteException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile" };

		String filter = "(objectclass=VGCPerson)";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		return persons;
	}

	
	
	/**
	 * email为key，value为电话号码的数据 map
	 * 
	 * @return
	 * @throws WSUnsupportedVersionException
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public Map<String, TBSPersonInfo> getTbsPersonInfo()
			throws WSUnsupportedVersionException, WSLoginServiceException,
			WSApplicationException, MalformedURLException, RemoteException,
			ServiceException {

		Map<String,TBSPersonInfo> result = new HashMap<String,TBSPersonInfo>();
		
		//Set<TBSPersonInfo> result = new HashSet<TBSPersonInfo>();
		
		WSPerson[] p = getTBSUserList();

		outter :for (WSPerson person : p) 
		{
			

			TBSPersonInfo personInfo = new TBSPersonInfo();
			
			personInfo.setName(person.getName());
			
			WSAttribute[] attr = person.getAttributes();

			for (WSAttribute a : attr) {

				if (a.getName().equals("uid")) {
					
					try
					{
						
						String v = a.getValues()[0];
						
						if(v==null)
						{
							break outter;
						}
						
						personInfo.setUsername(v);
						
					} catch (Exception e) 
					{
						log.error(person.getName()+"uid-----break."+e.getMessage());
						break outter;
					}
				}
				
				
				if (a.getName().equals("mail")) {
					
					try
					{
						personInfo.setEmail(a.getValues()[0]);
						
					} catch (Exception e) 
					{
						System.out.println(person.getName()+"mail-----break.");
						break outter;
					}
				}
				
				if (a.getName().equals("telephonenumber")) {
					
					try
					{	
							
							String completeNumber = a.getValues()[0].replaceAll(" ", "");
							if(completeNumber.length()>=11){
								completeNumber = completeNumber.substring(completeNumber.length() - 8);
							} 
							
							personInfo.setTelephonenumber(completeNumber);
						
					} catch (Exception e) 
					{
						log.error(person.getName()+"uid-----telephonenumber-----break."+e.getMessage());
						break outter;
					}
					
				}
				
				if (a.getName().equals("mobile")) {
					
					for(int i=0;i<a.getValues().length;i++)
					{
						String completeNumber = a.getValues()[i].replaceAll(" ", "");
						if(completeNumber.length()>=11){
							completeNumber = completeNumber.substring(completeNumber.length() - 11);
						} 
						
						personInfo.setMobile(completeNumber);
					}
					
					
				}
			}
			
			
				if(personInfo.getEmail()!=null)
				{
					
					if(!result.containsKey(personInfo.getEmail()))
					{
						result.put(personInfo.getEmail(), personInfo);
						
					}
					
				}
				
			 
			
		}

		
		return result;

	}
	
	public synchronized List<TBSPersonInfo> wsPersonTOPersonInfo(WSPerson[] p)
	{

		List<TBSPersonInfo> personInfos = new LinkedList<TBSPersonInfo>();
		
		
		outter :for (WSPerson person : p) 
		{
			

			TBSPersonInfo personInfo = new TBSPersonInfo();
			
			personInfo.setName(person.getName());
			
			WSAttribute[] attr = person.getAttributes();

			for (WSAttribute a : attr) {

				if (a.getName().equals("uid")) {
					
					try
					{
						
						String v = a.getValues()[0];
						
						if(v==null)
						{
							break outter;
						}
						
						personInfo.setUsername(v);
						
					} catch (Exception e) 
					{
						log.error(person.getName()+"uid-----break."+e.getMessage());
						break outter;
					}
				}
				
				
				if (a.getName().equals("mail")) {
					
					try
					{
						personInfo.setEmail(a.getValues()[0]);
						
					} catch (Exception e) 
					{
						System.out.println(person.getName()+"mail-----break.");
						break outter;
					}
				}
				
				if (a.getName().equals("telephonenumber")) {
					
					try
					{	
							
							String completeNumber = a.getValues()[0].replaceAll(" ", "");
							if(completeNumber.length()>=11){
								completeNumber = completeNumber.substring(completeNumber.length() - 8);
							} 
							
							personInfo.setTelephonenumber(completeNumber);
						
					} catch (Exception e) 
					{
						log.error(person.getName()+"uid-----telephonenumber-----break."+e.getMessage());
						break outter;
					}
					
				}
				
				if (a.getName().equals("mobile")) 
				{
					
					for(int i=0;i<a.getValues().length;i++)
					{
						String completeNumber = a.getValues()[i].replaceAll(" ", "");
						if(completeNumber.length()>=11){
							completeNumber = completeNumber.substring(completeNumber.length() - 11);
						} 
						
						personInfo.setMobile(completeNumber);
					}
					
					
				}
				
				if (a.getName().equals("employeenumber")) 
				{
					
						
					personInfo.setEnumber(a.getValues()[0]);
					
				}
				
				if (a.getName().equals("costcenter")) 
				{
					
						
					personInfo.setCostcenter(a.getValues()[0]);
					
				}
				
				
				
				
			}
			
			personInfos.add(personInfo)	;
			
			
		}
		return personInfos;
		
		
		
	}
	
	
	
	
	/**
	 * email为key，value为电话号码的数据 map
	 * 
	 * @return
	 * @throws WSUnsupportedVersionException
	 * @throws WSLoginServiceException
	 * @throws WSApplicationException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public List<TBSPersonInfo> findPersonInfoListByAttribute(String key,String value) throws WSUnsupportedVersionException, WSLoginServiceException, WSApplicationException, MalformedURLException, RemoteException, ServiceException {

		WSOrganizationalContainer wsContainer = new WSOrganizationalContainer();
		wsContainer.setItimDN(timLookupBaseDN);
		wsContainer.setProfileName(containerProfile);

		String attr[] = { "cn", "givenname", "sn", "uid", "telephonenumber",
				"mobile","costcenter","employeenumber" };

		String filter = "("+key+"=" + value + ")";

		WSPerson[] persons = personService.searchPersons(session, wsContainer,
				filter, attr);
		
		List<TBSPersonInfo> result = wsPersonTOPersonInfo(persons);
		
		return result;

	}
	
	public TBSPersonInfo  findPersonInfoByAttribute(String key,String value) throws WSUnsupportedVersionException, WSLoginServiceException, WSApplicationException, MalformedURLException, RemoteException, ServiceException
	{
		
		List<TBSPersonInfo> set = findPersonInfoListByAttribute(key,value);
		
		if(set.size()!=0)
		return set.iterator().next();
		
		return null;
		
	}
	
	public List<String> findAllCostCenter() throws WSUnsupportedVersionException, WSLoginServiceException, WSApplicationException, MalformedURLException, RemoteException, ServiceException
	{
		
		List<String> result = new LinkedList<String>();
		
		List<TBSPersonInfo> l = findPersonInfoListByAttribute("costcenter","*");
		
		for(TBSPersonInfo tbsInfo:l)
		{
			if(!result.contains(tbsInfo.getCostcenter()))
			{
				result.add(tbsInfo.getCostcenter());
			}
			
		}
		
		return result;
		
	}
	
	
	
	

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		TIMService t = context.getBean(TIMService.class);

		t.init();
		//costcenter=8120010
		
		
		 
	/**/	//List<TBSPersonInfo> l = t.wsPersonTOPersonInfo(t.getTBSUserList());
		
		
		//System.out.println(l);
		
 		List<TBSPersonInfo> l = t
 				.findPersonInfoListByAttribute("uid", "ELIYUA0") ;
		
		 System.out.println(l.get(0));
		 System.out.println(l.get(0).getTelephonenumber());
		
		
		/*List<String> set = t.findAllCostCenter();
		
		for(String s:set)
		{
			
			System.out.println(s);
			
			
			
		}*/
		
		
		
	}

}