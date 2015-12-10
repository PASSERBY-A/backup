<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.expression.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
String message = "";

String modelId = "host";
Model model = ServiceManager.getModelService().getModelById(modelId);

try {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 79667; i <= 80000; i++) {

		map.put("audit_status", "已审核");
		map.put("description", "description");
		map.put("important", "1");
		map.put("resource_admin", "1");
		map.put("status", "已安装");
		map.put("affect_customer", "test_affect_customer");
		map.put("affect_system", "test_affect_system");
		map.put("affect_region", "test_affect_region");
		map.put("asset_tag", "test_asset_tag");
		map.put("belong_to_system", "BOSS");
		map.put("department", "111");
		map.put("manufacturer", "111");
		map.put("serial_number", "111");
		map.put("service_contact", "111");
		map.put("service_end_date", "2010-01-11");
		map.put("service_start_date", "2010-01-11");
		map.put("service_level", "111");
		map.put("service_provider", "111");
		map.put("cpu_frequency", "3");
		map.put("cpu_number", "3");
		map.put("default_gateway", "111");
		map.put("fiber_card", "111");
		map.put("filesystem_description", "111");
		map.put("internal_hard_disk", "111");
		map.put("ip_address", "192.168.1.1");
		map.put("location", "111");
		map.put("logic_volume", "111");
		map.put("mac_address", "111");
		map.put("memory_size", "111");
		map.put("nic_description", "111");
		map.put("operating_system", "111");
		map.put("other_card", "111");
		map.put("partition_flag", "1");
		map.put("restart_time", "111");
		map.put("special_device", "111");
		map.put("swap_size", "111");
		map.put("tpcc", "111");
		map.put("type", "111");
		map.put("name", "test_" + i);
		
		// cpu_frequency
		int newId = ServiceManager.getResourceUpdateService().addResource(modelId, map, 1);
		message = "操作成功，<a href=resource.jsp?id=" + newId + ">新对象 id=" + newId + "</a>";
		}
	
} catch (Exception e) {
	message = e.getMessage();
	e.printStackTrace();
}

%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
</head>
<body>

<font color="red"><%=message%></font>

</body>

</html>
