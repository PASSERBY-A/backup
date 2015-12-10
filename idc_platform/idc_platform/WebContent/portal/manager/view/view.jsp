<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.velocity.app.*"%>
<%@page import="org.apache.velocity.*"%>
<%@page import="org.dom4j.*"%>
<%@page import="org.dom4j.Document"%>
<%@page import="com.hp.idc.portal.mgr.ViewMgr"%>
<%@page import="com.hp.idc.portal.mgr.LayoutMgr"%>
<%@page import="com.hp.idc.portal.bean.View"%>
<%@page import="com.hp.idc.portal.bean.Layout"%>
<%@page import="java.io.StringWriter"%>
<%@include file="../../getUser.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>中文</title>
</head>
<body>
<%
	try{
		String oid = request.getParameter("oid");
		ViewMgr viewMgr = (ViewMgr)ContextUtil.getBean("viewMgr");
		LayoutMgr layoutMgr = (LayoutMgr)ContextUtil.getBean("layoutMgr");
		View view = viewMgr.getBeanById(oid);
		Layout layout = layoutMgr.getBeanById(view.getLayoutId());
		String templatePath = layout.getPath();
		File tmpFile = new File(ContextUtil.getContextPath()+ "/vm/"+templatePath);
		if(null!=templatePath&&!"".equals(templatePath)&&tmpFile.exists()&&tmpFile.exists()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			VelocityEngine ve = new VelocityEngine();
			/* 布局模版固定文件路径 */
			ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, ContextUtil.getContextPath() + "/vm/");
			ve.init();
			/* 布局模版文件名 */
			Template t = ve.getTemplate(templatePath, "GBK");
			/* create a context and add data */
			VelocityContext context = new VelocityContext();
			
			Document d = DocumentHelper.parseText(view.getNodes());
			Element e = d.getRootElement();
			List list = e.selectNodes("node");
			Map<String,List> map = new HashMap<String,List>();
			for(int i=0;i<list.size();i++){
				Element element = (Element)list.get(i);
				String areaId = element.attributeValue("areaId");
				String nodeId = element.attributeValue("nodeId");
				List arr = map.get(areaId);
				if(arr==null){
					arr = new ArrayList();
				}
				arr.add(nodeId);
				map.put(areaId,arr);
			}
			context.put("sdf", sdf);
			context.put("contextUtil", new ContextUtil());
			context.put("nodeDataMap", map);
			context.put("userId", userId);
			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			/* show the World */
			out.println(writer.toString());
		}else{
			out.println("请联系管理员，检查该视图所对应布局模版文件！！");
		}
	}catch(Exception e){
		out.println("请联系管理员，检查该视图的相关配置！！<br>错误信息："+e.getMessage());
	}
%>
</body>
</html>