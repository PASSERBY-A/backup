package com.cmsz.cloudplatform;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.junit.Test;

import antlr.collections.List;

import com.cmsz.cloudplatform.model.request.BaseRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
//import com.cmsz.cloudplatform.model.response.UserResponse;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ResponseSerializerTestCase extends BaseTestCase {

	@Test
	public void testBuildResponseFromJson() {
		String jsonString = "{ \"listusersresponse\" : { \"count\":1 ,\"user\" : [  {\"id\":\"c7fd3ce9-6dd7-44d5-a8e6-53aa1384e6d3\",\"username\":\"user\",\"firstname\":\"user\",\"lastname\":\"user\",\"email\":\"user@msn.com\",\"created\":\"2013-12-21T17:30:24+0800\",\"state\":\"enabled\",\"account\":\"user\",\"accounttype\":0,\"domainid\":\"485f3b4d-65b4-11e3-8275-e4115b493a9b\",\"domain\":\"ROOT\",\"apikey\":\"xo05ZKg3sqK3aHlsZEd0foNHwiYy7M1Vkfff4B6vpMy4Aw4N3gUODQvIRlWJN79wrtwECo10dF2Ena_D5DvUHg\",\"secretkey\":\"_gSK5ZGKRaJ4v23DSttkLUZURZ07gUfNSQxLfddsFtR0_CiYiEn1HmfD27jqPqPQP431_w8fM-v5_DKEFKSQaw\",\"accountid\":\"22893918-221c-4b7f-9fca-d95bea418f5a\",\"iscallerchilddomain\":false,\"isdefault\":false} ] } }"; 
		String userlistString = "{ \"count\":1 ,\"responses\" : [  {\"id\":\"c7fd3ce9-6dd7-44d5-a8e6-53aa1384e6d3\",\"username\":\"user\",\"firstname\":\"user\",\"lastname\":\"user\",\"email\":\"user@msn.com\",\"created\":\"2013-12-21T17:30:24+0800\",\"state\":\"enabled\",\"account\":\"user\",\"accounttype\":0,\"domainid\":\"485f3b4d-65b4-11e3-8275-e4115b493a9b\",\"domain\":\"ROOT\",\"apikey\":\"xo05ZKg3sqK3aHlsZEd0foNHwiYy7M1Vkfff4B6vpMy4Aw4N3gUODQvIRlWJN79wrtwECo10dF2Ena_D5DvUHg\",\"secretkey\":\"_gSK5ZGKRaJ4v23DSttkLUZURZ07gUfNSQxLfddsFtR0_CiYiEn1HmfD27jqPqPQP431_w8fM-v5_DKEFKSQaw\",\"accountid\":\"22893918-221c-4b7f-9fca-d95bea418f5a\",\"iscallerchilddomain\":false,\"isdefault\":false} ] } ";

		jsonString = jsonString.replace("\"user\"", "\"responses\"");
		//		UserResponse ur = new UserResponse();
//		String userString = "{\"id\":\"c7fd3ce9-6dd7-44d5-a8e6-53aa1384e6d3\",\"username\":\"user\",\"firstname\":\"user\",\"lastname\":\"user\",\"email\":\"user@msn.com\",\"created\":\"2013-12-21T17:30:24+0800\",\"state\":\"enabled\",\"account\":\"user\",\"accounttype\":0,\"domainid\":\"485f3b4d-65b4-11e3-8275-e4115b493a9b\",\"domain\":\"ROOT\",\"apikey\":\"xo05ZKg3sqK3aHlsZEd0foNHwiYy7M1Vkfff4B6vpMy4Aw4N3gUODQvIRlWJN79wrtwECo10dF2Ena_D5DvUHg\",\"secretkey\":\"_gSK5ZGKRaJ4v23DSttkLUZURZ07gUfNSQxLfddsFtR0_CiYiEn1HmfD27jqPqPQP431_w8fM-v5_DKEFKSQaw\",\"accountid\":\"22893918-221c-4b7f-9fca-d95bea418f5a\",\"iscallerchilddomain\":false,\"isdefault\":false}";
//		ResponseObject ro =
//		List
//		Gson gson = new GsonBuilder().setDateFormat(BaseRequest.DATE_OUTPUT_FORMAT).setFieldNamingStrategy(new PersonNamingStrategy())
//				.create();
		
//		UserResponse ur = gson.fromJson(userString, UserResponse.class);
//		ListResponse<UserResponse> urlist = gson.fromJson(userlistString, ListResponse.class);
//		ListResponse<UserResponse> urlist = gson.fromJson(userlistString, new TypeToken<ListResponse<UserResponse>>(){}.getType());
//		String objectname = "user";
		
//		System.out.println(urlist.getResponses());
//		ResponseSerializer.buildResponseFromJson();
		
//		JSONArray ja = JSONArray.fromObject(userlistString);

//		JSON js = JSONSerializer.toJSON(userlistString);
		
		JSONObject jo = JSONObject.fromObject(jsonString);
		jo = JSONObject.fromObject(jo.get("listusersresponse"));
		
		Map<String, Class> classMap = new HashMap<String, Class>();
//		classMap.put("responses", UserResponse.class);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setClassMap(classMap);
		jsonConfig.setRootClass(ListResponse.class);
		
//		ListResponse<UserResponse> urlist = (ListResponse<UserResponse>) JSONObject.toBean(jo, jsonConfig);
//		urlist = (ListResponse<UserResponse>) JSONObject.toBean(jo, ListResponse.class, classMap);
		
//		System.out.println(urlist.getCount());
//		System.out.println(urlist.getResponses().size());
//		for (Object o : urlist.getResponses()) {
		//	net.sf.ezmorph.bean.MorphDynaBean mdb = (net.sf.ezmorph.bean.MorphDynaBean)o;
//			System.out.println(o);
			
			
		}

//		ja.toList(ja, UserResponse)
//		System.out.println(o);
//	}
	
	public static String replaceJsonString(int level, String jsonStr, String oldStr, String newStr) {
		String result = "";
		
		for (int i=0; i < level; i++) {
			if (i==level-1) {
				//TODO
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		ResponseSerializerTestCase tc = new ResponseSerializerTestCase();
		tc.testBuildResponseFromJson();

	}

}

class PersonNamingStrategy implements FieldNamingStrategy {   
    public String translateName(final Field f) {   
        if (f.getName().equals("responses")) {
        	
        	System.out.println("xxxxxxx");
            return "user";   
        }   

        return f.getName();   
    }  
    
    
}  

class TestJsonBeanProcessor implements JsonBeanProcessor {
	private String target;
	private String replace;
	
	public TestJsonBeanProcessor(String target, String replace) {
		this.target = target;
		this.replace = replace;
		System.out.println("======1======");
	} 
	 public JSONObject processBean( Object bean, JsonConfig jsonConfig ) {
	      JSONObject jsonObject = null;
//	      if( bean instanceof UserResponse ){
//	         bean = new Date( ((java.sql.Date) bean).getTime() );
//	      }
	      if( bean instanceof Date ){
	         Calendar c = Calendar.getInstance();
	         c.setTime( (Date) bean );
	         jsonObject = new JSONObject().element( "year", c.get( Calendar.YEAR ) )
	               .element( "month", c.get( Calendar.MONTH ) )
	               .element( "day", c.get( Calendar.DAY_OF_MONTH ) )
	               .element( "hours", c.get( Calendar.HOUR_OF_DAY ) )
	               .element( "minutes", c.get( Calendar.MINUTE ) )
	               .element( "seconds", c.get( Calendar.SECOND ) )
	               .element( "milliseconds", c.get( Calendar.MILLISECOND ) );
	      }else{
	         jsonObject = new JSONObject( true );
	      }
	      return jsonObject;
	   }
}
