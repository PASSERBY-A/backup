<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@page import="com.hp.idc.resm.service.ServiceManager"%>
<%@ page import="com.hp.idc.context.util.ContextUtil"%>
<%@ page import="com.hp.idc.resm.ui.UiService"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.resm.resource.AttributeBase"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.hp.idc.resm.resource.StringAttribute"%>
<%@page import="com.hp.idc.resm.resource.IntegerAttribute"%>
<%@page import="com.hp.idc.resm.resource.ReferenceAttribute"%>
<%@page import="com.hp.idc.resm.service.IModelService"%>
<%@page import="com.hp.idc.resm.model.ModelAttribute"%>
<%@include file="portal/getUser.jsp" %>
<html>
<body>

<%
String modelId = "slot";

for(int j = 9;j<12;j++){
int n = 726+j;
String rack = "050"+j+"0";
if(j > 9)
	rack = "05"+j+"0";
String rackId = String.valueOf(n);

IModelService im = ServiceManager.getModelService();
ModelAttribute na = im.getModelAttribute(modelId,"name");
ModelAttribute cr = im.getModelAttribute(modelId,"resource_admin");
ModelAttribute st = im.getModelAttribute(modelId,"status");
ModelAttribute rr = im.getModelAttribute(modelId,"ref_rack");
ModelAttribute so = im.getModelAttribute(modelId,"slot_orderno");
ModelAttribute type = im.getModelAttribute(modelId,"slot_type");
ModelAttribute dc = im.getModelAttribute(modelId,"description");

for(int i=1;i<47;i++){
	List<AttributeBase> l = new ArrayList<AttributeBase>();

	AttributeBase as1 = new StringAttribute();
	as1.setText("¿ÕÏÐ×ÊÔ´");
	as1.setAttribute(dc);
	l.add(as1);

	AttributeBase as2 = new StringAttribute();
	if(i<10)
		as2.setText(rack+"0"+i);
	else
		as2.setText(rack+i);
	as2.setAttribute(na);
	l.add(as2);

	AttributeBase as3 = new StringAttribute();
	as3.setText("root");
	as3.setAttribute(cr);
	l.add(as3);

	AttributeBase as4 = new StringAttribute();
	as4.setText("¿ÕÏÐ");
	as4.setAttribute(st);
	l.add(as4);

	AttributeBase as5 = new StringAttribute();
	as5.setText("¶ÀÁ¢");
	as5.setAttribute(type);
	l.add(as5);

	AttributeBase ai1 = new IntegerAttribute();
	ai1.setText(String.valueOf(i));
	ai1.setAttribute(so);
	l.add(ai1);

	AttributeBase ar1 = new ReferenceAttribute();
	ar1.setText(rackId);
	ar1.setAttribute(rr);
	l.add(ar1);

	//ServiceManager.getResourceUpdateService().addResource(modelId,l,userId);
}
}

%>
</body>
</html>