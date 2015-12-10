<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.mgr.WorkPlanMgr"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.hp.idc.portal.bean.WorkPlan"%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");
	response.setContentType("text/html;charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
%>
<%
	int userId = Integer.parseInt(request.getParameter("userId"));
	String date = request.getParameter("date");
	WorkPlanMgr mgr = (WorkPlanMgr)ContextUtil.getBean("workPlanMgr");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
	List<WorkPlan> list = mgr.getWorkPlanTopData(userId,sdf.parse(date));
	Map<String,List<WorkPlan>> map = new HashMap<String,List<WorkPlan>>();
	//��������֯����
	for(WorkPlan wp : list){
		List<WorkPlan> tmp = map.get(sdf.format(wp.getPlanTime()));
		if(tmp==null){
			tmp = new ArrayList<WorkPlan>();
		}
		tmp.add(wp);
		map.put(sdf.format(wp.getPlanTime()),tmp);
	}
	Object[] key  = map.keySet().toArray();
	Arrays.sort(key);
	Calendar cal = Calendar.getInstance();
	for(int i =0;i<key.length;i++) {   
         out.print("<ul>");
         Date tdate = sdf.parse(key[i].toString());
         cal.setTime(tdate);
         String dow = "����";
         if(cal.get(Calendar.DAY_OF_WEEK)==1)
        	 dow = "����";
         else if(cal.get(Calendar.DAY_OF_WEEK)==2)
        	 dow = "��һ";
         else if(cal.get(Calendar.DAY_OF_WEEK)==3)
        	 dow = "�ܶ�";
         else if(cal.get(Calendar.DAY_OF_WEEK)==4)
        	 dow = "����";
         else if(cal.get(Calendar.DAY_OF_WEEK)==5)
        	 dow = "����";
         else if(cal.get(Calendar.DAY_OF_WEEK)==6)
        	 dow = "����";
         
         Calendar today = Calendar.getInstance();
         today.setTime(new Date());
         if(cal.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)&&cal.get(Calendar.YEAR)==today.get(Calendar.YEAR)){
        	 dow="����";
         }
        	 
         out.print("<li class='date_downTop'>"+dow+sdf2.format(tdate)+"("+map.get(key[i]).size()+"��)</li>");
         for(int j=0;j<map.get(key[i]).size()&&j<3;j++){
        	 WorkPlan wp = map.get(key[i]).get(j);
        	 if(wp.getFinishTime()!=null)
        		 continue;
        	 cal.setTime(wp.getPlanTime());
        	 String am_pm = "����";
        	 if(cal.get(Calendar.AM_PM)==1){
        		 am_pm = "����";
        	 }
        	 out.print("<li title='"+wp.getTitle()+"' onclick='openWin(\"/portal/manager/workPlan/show.jsp?oid="+wp.getOid()+"\",400,300);'>"+am_pm+sdf1.format(wp.getPlanTime())+" "+wp.getTitle()+"</li>");
         }
         out.print("</ul>");
	}
%>
