package com.cmsz.cloudplatform.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cmsz.cloudplatform.BaseTestCase;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;

public class GenericCloudServerManagerTestCase extends BaseTestCase {
	public static void mains(String[] args) throws SQLException {
		String str = "http://localhost:8080/client/api?response=json&"
				+ "VirtualMachineId=3463b470-b747-4f85-9cbc-ae8b40ee852b"
				+ "&command=listServiceOfferings"
				+ "&_=1389930876338&"
				+ "apikey=USJK3oID5IQJ_sKwrns3_9zStgNEtoiEfb8renTSySijuehcYgzX5GAXtqisz6WtrayIOR8UeiWdVBJGQlt4bQ&signature=OHP%2fERR9AmmSqrAsm9%2b15uHXIGE%3d";
		GenericCloudServerManagerImpl bean = (GenericCloudServerManagerImpl)getApplicationContext().getBean("genericCloudServerManager");
		Map<String, Object[]> params = new HashMap<String, Object[]>();
		params.put("response", new Object[]{"json"});
		params.put("VirtualMachineId", new Object[]{"3463b470-b747-4f85-9cbc-ae8b40ee852b"});
		params.put("command", new Object[]{"listServiceOfferings"});
		params.put("_", new Object[]{"1389930876338"});
//		params.put("apikey", new Object[]{"2krDORHjIDE_Ed3DIKsYmA2Snn5WS1BUzylyzyuTgGQHX3_r98VBbThPKr-GGTaIIE4uzEe_JSyawJ4S2QXVJA"});
//		params.put("secretkey", new Object[]{"MyUzEpKVVxoWziZhMsYXYpSpwcA1xRRDP5OmyTjFT2Ct5v8r72zkxguPnnf0-NvDeC3dH0CXUKWHKPsk4jWwXg"});
		params.put("apikey", new Object[]{"USJK3oID5IQJ_sKwrns3_9zStgNEtoiEfb8renTSySijuehcYgzX5GAXtqisz6WtrayIOR8UeiWdVBJGQlt4bQ"});
		params.put("secretkey", new Object[]{"22gRXV5iwejA0BFCyKHz43Dieu0zfRG-fEnCsMmrP6ldBaPi7WqZiav8nxxeBAftICIVhuaqrAr9ySJqFQ50vw"});
		Object obj = bean.get(params);
		System.out.println(obj);
		//se(), === virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&response=json&command=listserviceofferings&apikey=usjk3oid5iqj_skwrns3_9zstgnetoiefb8rentsysijuehcygzx5gaxtqisz6wtrayior8ueiwdvbjgqlt4bq&_=1389930876338
		//          virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&response=json&command=listserviceofferings&apikey=usjk3oid5iqj_skwrns3_9zstgnetoiefb8rentsysijuehcygzx5gaxtqisz6wtrayior8ueiwdvbjgqlt4bq&_=1389946167546>
		String s = "virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json";
		String x = "_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json&virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b";
		        // "virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json


	}
	public static void main(String[] args) throws SQLException {
		String str = "http://localhost:8080/client/api?response=json&"
				+ "command=listIsos&response=json&isofilter=featured&isReady=true&zoneid=bcdf98b8-33ba-48a9-b785-6be289c5ff10"
				+ "apikey=USJK3oID5IQJ_sKwrns3_9zStgNEtoiEfb8renTSySijuehcYgzX5GAXtqisz6WtrayIOR8UeiWdVBJGQlt4bQ&signature=OHP%2fERR9AmmSqrAsm9%2b15uHXIGE%3d";
		GenericCloudServerManagerImpl bean = (GenericCloudServerManagerImpl)getApplicationContext().getBean("genericCloudServerManager");
		Map<String, Object[]> params = new HashMap<String, Object[]>();
		params.put("response", new Object[]{"json"});
		params.put("isofilter", new Object[]{"featured"});
		params.put("command", new Object[]{"listIsos"});
		params.put("isReady", new Object[]{"true"});
		params.put("_", new Object[]{"1389930876338"});
//		params.put("apikey", new Object[]{"2krDORHjIDE_Ed3DIKsYmA2Snn5WS1BUzylyzyuTgGQHX3_r98VBbThPKr-GGTaIIE4uzEe_JSyawJ4S2QXVJA"});
//		params.put("secretkey", new Object[]{"MyUzEpKVVxoWziZhMsYXYpSpwcA1xRRDP5OmyTjFT2Ct5v8r72zkxguPnnf0-NvDeC3dH0CXUKWHKPsk4jWwXg"});
		params.put("apikey", new Object[]{"USJK3oID5IQJ_sKwrns3_9zStgNEtoiEfb8renTSySijuehcYgzX5GAXtqisz6WtrayIOR8UeiWdVBJGQlt4bQ"});
		params.put("secretkey", new Object[]{"22gRXV5iwejA0BFCyKHz43Dieu0zfRG-fEnCsMmrP6ldBaPi7WqZiav8nxxeBAftICIVhuaqrAr9ySJqFQ50vw"});
		Object obj = bean.get(params);
		System.out.println(obj);
		//se(), === virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&response=json&command=listserviceofferings&apikey=usjk3oid5iqj_skwrns3_9zstgnetoiefb8rentsysijuehcygzx5gaxtqisz6wtrayior8ueiwdvbjgqlt4bq&_=1389930876338
		//          virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&response=json&command=listserviceofferings&apikey=usjk3oid5iqj_skwrns3_9zstgnetoiefb8rentsysijuehcygzx5gaxtqisz6wtrayior8ueiwdvbjgqlt4bq&_=1389946167546>
		String s = "virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json";
		String x = "_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json&virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b";
		        // "virtualmachineid=3463b470-b747-4f85-9cbc-ae8b40ee852b&_=1389930876338&apikey=2krdorhjide_ed3diksyma2snn5ws1buzylyzyutggqhx3_r98vbbthpkr-ggtaiie4uzee_jsyawj4s2qxvja&command=listserviceofferings&response=json


	}
}
