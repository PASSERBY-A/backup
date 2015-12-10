<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.regex.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.*"%>

<%@ include file="getPurview.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");

try{

List persons = new ArrayList();
String type = request.getParameter("type");
if (type == null || type.equals(""))
	type = "all";

boolean includeAll = true;
if ( request.getParameter("includeAll") != null && !request.getParameter("includeAll").equals(""))
	includeAll = Boolean.parseBoolean(request.getParameter("includeAll"));

if (type.equals("all")){
	persons = PersonManager.getAllPersons(includeAll);
} else if (type.equals("org")){
	String orgId = request.getParameter("orgId");
	orgId = orgId == null?"":orgId;
	persons = PersonManager.getPersonsOfOrganization(orgId,includeAll);
} else if (type.equals("wog")){
	String wogId = request.getParameter("wogId");
	wogId = wogId == null?"":wogId;
	persons = PersonManager.getPersonsOfWorkgroup(wogId,includeAll);
	
} else if (type.equals("addToWorkgroup")){
	String wogId = request.getParameter("objectId");
	persons = PersonManager.getPersonsForAddToWorkgroup(wogId,includeAll);
} else if (type.equals("addToOrganization")){
	persons = PersonManager.getPersonsForAddToOrganization(includeAll);
}

String filter = request.getParameter("filter");
filter = (filter==null?"":filter);
String filterAttribute = request.getParameter("filterAttribute");
filterAttribute = (filterAttribute==null?"id":filterAttribute);

String pathFilter = request.getParameter("pathFilter");
pathFilter = (pathFilter==null?"":pathFilter);

if(!pathFilter.equals("")) {
	Pattern p = Pattern.compile(pathFilter);
	for (int i = 0; i < persons.size(); i++) {
		PersonInfo pInfo = (PersonInfo)persons.get(i);
		Matcher m = p.matcher(pInfo.getId());
		if (!m.find()) {
			persons.remove(i);
			i--;
		}
	}
}

if (!filter.equals("")){
	for (int i = 0; i < persons.size(); i++) {
		PersonInfo pInfo = (PersonInfo)persons.get(i);
			if ((filterAttribute.equals("all")||filterAttribute.equals("id"))&& pInfo.getId().indexOf(filter)!=-1)
				continue;
			if ((filterAttribute.equals("all")||filterAttribute.equals("name"))&&pInfo.getName().indexOf(filter)!=-1)
				continue;
			if ((filterAttribute.equals("all")||filterAttribute.equals("mobile"))&&pInfo.getMobile().indexOf(filter)!=-1)
				continue;
			if ((filterAttribute.equals("all")||filterAttribute.equals("email"))&&pInfo.getEmail().indexOf(filter)!=-1)
				continue;
			
			persons.remove(i);
			i--;
	}
}

int limit = 0;
int start = 0;
String limit_s = request.getParameter("limit");
if (limit_s!=null && !limit_s.equals(""))
	limit = Integer.parseInt(limit_s);
else
	limit = 25;
String start_s = request.getParameter("start");
if (start_s != null)
	start = Integer.parseInt(start_s);

Collections.sort(persons, new Comparator() {
	public int compare(Object a, Object b) {
		String id1 = ((PersonInfo)a).getId();
		String id2 = ((PersonInfo)b).getId();
		return id1.compareTo(id2);
	}
});
%>
{
"totalCount" : <%=persons.size()%>,
"records" : [
<%

int count = 0;
for (int i = start; i < persons.size() && i < (start+limit); i++){
	PersonInfo pInfo = (PersonInfo)persons.get(i);
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'id':'"+pInfo.getId()+"',");
	out.print("'name':'"+pInfo.getName()+"',");
	out.print("'mobile':'"+pInfo.getMobile()+"',");
	out.print("'email':'"+pInfo.getEmail()+"',");
	out.print("'status':'"+pInfo.getStatus()+"',");
	OrganizationInfo oi = OrganizationManager.getOrganizationOfPerson(pInfo.getId(), false);
	if (oi!=null) {
		out.print("'department':'"+oi.getName()+"',");
		if (type.equals("all")) {
			RoleInfo ri = RoleManager.getPersonRole(oi.getId(),pInfo.getId());
			if (ri!=null)
				out.print("'role':'"+ri.getName()+"',");
			else
				out.print("'role':'',");
		}
	} else {
		out.print("'department':'',");
		if (type.equals("all")) {
			out.print("'role':'',");
		}
	}
	
	if(type.equals("org") || type.equals("wog")) {
		String moId = request.getParameter("orgId");
		if (moId == null)
			moId  = request.getParameter("wogId");
		moId = moId == null?"":moId;
		RoleInfo ri = RoleManager.getPersonRole(moId,pInfo.getId());
		if (ri!=null)
			out.print("'role':'"+ri.getName()+"',");
		else
			out.print("'role':'',");
	}
	
	out.print("'p_status':'"+pInfo.getPersonStatus()+"'");
	out.print("}");
	count++;
}
%>

]}

<%
} catch(Exception e){
	e.printStackTrace();
}
%>