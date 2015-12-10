<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="com.hp.idc.context.util.ContextUtil"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>

<%@ page import="org.quartz.impl.StdScheduler"%>
<%@ page import="org.quartz.*"%>

<%@ page import="java.util.*"%>

<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
<table class='embed2' border=0 cellspacing=1 width=100%>
	<tr>
		<td align="center" style="background:#EEEEEE">Bean名称</td>
		<td align="center" style="background:#EEEEEE">启动时间</td>
		<td align="center" style="background:#EEEEEE">实例类</td>
	</tr>
<%
	ApplicationContext ac = (ApplicationContext)ContextUtil.current;
	String[] names = ac.getBeanDefinitionNames();
	for (int i = 0; i < names.length; i++){
		out.println("<tr>");
		out.println("<td>"+names[i]+"</td>");
		out.println("<td>"+DateTimeUtil.formatDate(ac.getStartupDate(), "yyyy-MM-dd HH:mm:ss")+"</td>");
		out.println("<td>"+ac.getBean(names[i]).getClass().getName()+"</td>");
		out.println("</tr>");
	}
%>
</table>
<br>
事务调度列表
<table class='embed2' border=0 cellspacing=1 width=100%>
	<tr>
		<td align="center" style="background:#EEEEEE">事务名</td>
		<td align="center" style="background:#EEEEEE">表达式</td>
		<td align="center" style="background:#EEEEEE">启动时间</td>
		<td align="center" style="background:#EEEEEE">上次运行时间</td>
		<td align="center" style="background:#EEEEEE">下次运行时间</td>
	</tr>
<%
	Scheduler ss = (Scheduler)ac.getBean("serviceScheduler");
	String[] triggerGroup = ss.getTriggerGroupNames();
	for (int i = 0; i < triggerGroup.length; i++){
		String[] triggerName = ss.getTriggerNames(triggerGroup[i]);
		for (int j = 0; j < triggerName.length; j++){
			Trigger trigger = ss.getTrigger(triggerName[j],triggerGroup[i]);
			out.print("<tr>");
			out.print("<td>"+trigger.getJobName()+"</td>");
			if (trigger instanceof CronTrigger)
				out.print("<td>"+((CronTrigger)trigger).getCronExpression()+"</td>");
			else if (trigger instanceof SimpleTrigger)
				out.print("<td>循环次数："+((SimpleTrigger)trigger).getRepeatCount()+"间隔："+((SimpleTrigger)trigger).getRepeatInterval()/1000+"秒</td>");
			out.print("<td>"+DateTimeUtil.formatDate(trigger.getStartTime(), "yyyy-MM-dd HH:mm:ss")+"</td>");
			out.print("<td>"+DateTimeUtil.formatDate(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss")+"</td>");
			out.print("<td>"+DateTimeUtil.formatDate(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss")+"</td>");
			out.print("</tr>");
		}
	}
%>
