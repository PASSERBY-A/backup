<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.hp.idc.resm.cache.ResourceObjectCache"%>
<%@page import="com.hp.idc.resm.service.CachedResourceService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.hp.idc.resm.resource.ResourceObject"%>
<%@page import="com.hp.idc.resm.service.IResourceService"%>
<%@page import="com.hp.idc.resm.model.Model"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.resm.service.ServiceManager"%>
<%@page import="com.hp.idc.resm.service.IModelService"%>
<%

String modelId = request.getParameter("modelId");
if(modelId == null){
	modelId = "device";
}
String status = request.getParameter("status");

String[] _status = status.split(",");

IModelService ms = ServiceManager.getModelService();
List<Model> l = ms.getChildModelsById(modelId,0);

CachedResourceService is = (CachedResourceService)ServiceManager.getResourceService();
StringBuffer sb = new StringBuffer("<chart showToolTip='1' baseFontSize='12' useRoundEdges='1'>");

for(Model m : l){
	int count = 0;
	if(status != null && _status.length < 4){
		ResourceObjectCache rc = is.getCache();
		for(String s : _status){
			if(s.equals("1")){
				count = count + rc.findInModel(m.getId(),"status","空闲").size();
			}
			if(s.equals("2")){
				count = count + rc.findInModel(m.getId(),"status","预占").size();
			}
			if(s.equals("3")){
				count = count + rc.findInModel(m.getId(),"status","实占").size();
			}
			if(s.equals("4")){
				count = count + rc.findInModel(m.getId(),"status","使用中").size();
			}
		}
	} else {
		count = is.getResourcesByModelId(m.getId(),true,1).size();
	}
	sb.append("\n<set value='"+count+"' label='"+m.getName()+"' />");	
}

sb.append("\n</chart>");
out.print(sb.toString());
%>