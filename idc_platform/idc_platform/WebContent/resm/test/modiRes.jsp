<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.cache.*"%>
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
long l0 = System.currentTimeMillis();
		long l1 = 0;
		int ppp = 100;
	CachedResourceService crs = (CachedResourceService)ServiceManager.getResourceService();
		for (int i = 1; i <= 10000; i++) {
			String sss = "" + i;
			while (sss.length() < 6)
				sss = "0" + sss;
			List<ResourceObject> objs = crs.getCache().findInGlobal("searchcode", "HW-EPS-ZZZ-" + sss);
			if (objs.size() != 1) {
				System.out.println("errr: HW-EPS-ZZZ-" + sss);
				continue;
			}
			ResourceObject obj = objs.get(0);
			AttributeBase ab = obj.createAttribute("description");
			ab.setText("description" + i);
			List<AttributeBase> list = new ArrayList<AttributeBase>();
			list.add(ab);
			ServiceManager.getResourceUpdateService().updateResource(obj.getId(), list, 1);
			if (i % ppp == 0) {
					l1 = System.currentTimeMillis() - l0;
					l0 = System.currentTimeMillis();
					System.out.println("更新" + i	+ "条记录，" + (ppp * 1000 / l1) + "条/秒");
			}
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
