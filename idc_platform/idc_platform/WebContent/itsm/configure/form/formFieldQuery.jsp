<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="org.dom4j.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

{	"totalCount" : 0,
	"items" : [
<%
String formList = request.getParameter("formList");
String[] forms = formList.split(",");
int index = 0;
Map temp = new HashMap();
for(int i = 0; i < forms.length; i++){
	if (temp.get(forms[i])!=null)
		continue;
	try{
		FormInfo formInfo = FormManager.getFormByOid(Integer.parseInt(forms[i]));
		temp.put(forms[i],"");
		//	if(index >0 )
		//out.println(",");
		//out.println("{id:"+forms[i]+",text:'"+formInfo.getName()+"',children:[");
		List fields = formInfo.getFields();
		for (int j = 0; j < fields.size(); j++){
			FieldInfo f = (FieldInfo)fields.get(j);
			if (index>0)
				out.println(",");
			out.println("{id:'"+f.getId()+"',");
			out.println("name:'"+StringUtil.escapeJavaScript(f.getName())+"',");
			out.println("formName: '"+StringUtil.escapeJavaScript(formInfo.getName())+"'}");
			
			index++;
		}
		//out.println("]}");
		
	}catch(Exception e){
	}
}

%>
]}