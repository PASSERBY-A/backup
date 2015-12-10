<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.security.Cache"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%@include file="getUser.jsp" %>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.hp.idc.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>IDCҵ����Ӫ����ƽ̨</title>
<link href="style/css/index.css" rel="stylesheet" type="text/css" />
<link href="css/css.css" type="text/css" rel="stylesheet" />
<script src="style/js/base.js" type="text/javascript" language="javascript"></script>
<script src="style/js/jquery-1.5.1.min.js" type="text/javascript" language="javascript"></script>
<script src="js/js.js" type="text/javascript"></script>
<!--[if lt IE 7]>
<script type="text/javascript" src="/portal/style/iepngfix/iepngfix_tilebg.js"></script>
<![endif]-->
<script type="text/javascript" src="style/js/jquery.scrollTo-1.4.2.js"></script>
<script type="text/javascript" language="javascript">
$(document).ready(function(){
	$(".index_line1Span6").click(function(){
		$(".cover_divBig").show("slow");
		$(".cover_divBig").css("background-color","transparent");
		$(".btn1").fadeIn();
		$(".cover_divSmall").fadeIn();
	});			   
	$("#index_line1Span7").click(function(){
		$(".cover_divBig").show("slow");
		$(".cover_divBig").css("background-color","transparent");
		$(".btn1").fadeIn();
		$(".cover_divSmall").fadeIn();
	});			   
	
	$("#close_message_win").click(function(){
		$(".cover_divBig").hide("slow");
		$(this).fadeOut();
		$(".cover_divSmall").fadeOut();
	});
	initDate();
	getWorkPlan();
	getMessage();
	getKBM();
	getMessageCount();
});

function searchUser(){
	if($("#keyWord")[0].value==""){
		alert("�ؼ��ֲ���Ϊ��");
		return;
	}
	jQuery.ajax({
	   url: 'getData/seachUser.jsp', // ����Ϊ ��Ա��ѯ����ҳ��
	   data: {"keyWord":$("#keyWord")[0].value}, // �ؼ���
	   type: 'POST', // ������������Ϊ ��POST����Ĭ��Ϊ ��GET��
	   dataType:'json',
	   complete: function(data) {
	       $("#seachUser").html(data.responseText);
	   }
	});
}

function sendSms(){
	var smsUsers = $("#smsUsers")[0].value;
	var smsContent = $("#smsContent")[0].value;
	if(smsUsers==""){
		alert("���Ž����˲���Ϊ��");
		return;
	}
	if(smsContent==""){
		alert("�������ݲ���Ϊ��");
		return;
	}
	jQuery.ajax({
		url: 'getData/sendSms.jsp', // ����Ϊ ��Ա��ѯ����ҳ��
		data: {"smsUsers":$("#smsUsers")[0].value,"smsContent":$("#smsContent")[0].value}, // �ؼ���
		type: 'POST', // ������������Ϊ ��POST����Ĭ��Ϊ ��GET��
		dataType:'json',
		success: function(data) {
		    alert(data.data.msg);
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {
			alert("����"+XMLHttpRequest.responseText);
		}
	});
}

function openUrl(mid){
	location.href="web/index.jsp?mid="+mid;
}

function openUrl2(url){
	location.href="web/index.jsp?url="+url;
}

function openWin(url,width,height){
	 //��ô��ڵĴ�ֱλ��
    var iTop = (window.screen.availHeight-30-height)/2;        
    //��ô��ڵ�ˮƽλ��
    var iLeft = (window.screen.availWidth-10-width)/2;
	window.open (url, 'newwindow', 'height='+height+', width='+width+',top='+iTop+',left='+iLeft+', toolbar=no, menubar=no, scrollbars=no,resizable=yes,location=no, status=no') 
}

function openModalDialog(urls){
	var height=500,width=700;
	var xposition = (screen.width - width) / 2;
	var yposition = (screen.height - height) / 2;
	var rv = window.showModalDialog(urls,'','dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
	var arr = rv.split(",");
	var ids="";
	var values="";
	for(var i=0;i<arr.length;i++){
		if(i>0){
			ids += ",";
			values += ",";
		}
		ids += arr[i].split("=")[0]
		values += arr[i].split("=")[1];
	}
	if(rv)
		$("#smsUsersValues")[0].value=values;
		$("#smsUsers")[0].value=ids;
}

function getWorkPlan(){
	var y = $("#select_year")[0].value;
	var m = $("#select_month")[0].value;
	var d = $("#select_day")[0].value;
	var date = y+"-"+m+"-"+d;
	jQuery.ajax({
		url: 'getData/getWorkPlan.jsp', // ����Ϊ ��Ա��ѯ����ҳ��
		data: {"userId":'<%=userId%>',"date":date}, // �ؼ���
		type: 'POST', // ������������Ϊ ��POST����Ĭ��Ϊ ��GET��
		dataType:'json',
		complete: function(data) {
		    $("#workPlanData").html(data.responseText);
		}
	});
}

</script>
<!--[if lt IE 7]>
<style type="text/css"> 
	img, div, a, input { behavior: url(/portal/style/iepngfix/iepngfix.htc) }
</style>
<![endif]-->
</head>
	
<body style="margin-bottom: 0px">
	<!--����������  layout-->
    <div class="cover_divBig" style="display:none;">
    </div>
    <div class="cover_divSmall"  style="display:none;">
    	<!-- Widget XHTML Starts Here -->
		<div id="posts-container">
			<!-- Posts go inside this DIV -->
			<div id="posts"></div>
			<!-- Load More "Link" -->
			<div id="load-more" class="btn1">���ظ���δ����Ϣ</div>
			<div class="btn1" id="close_message_win">�ر�</div>
		</div>
		<!-- Widget XHTML Ends Here -->
   	</div>
    <!--����������  end-->
	 
    <!--head  layout-->
    
    
    <div class="header">
	<div class="title">
		<div class="rigthTop"><span>��ӭ��<%=userName %></span><span style="padding: 0 15px; background: none;">|</span>
		<a class="index_line1Span6" title="ϵͳ��Ϣ"></a>
		<a href="<%=request.getContextPath() %>/logout.action">ע��</a></div>
	</div>
	</div>
	<!--index_nav  layout-->
    <div class="menu"> 
	<em id="hrefa0"><span class="icomenu01">ҵ����Ӫ</span>
	<div id="divMenu01" class="display" style="display: none;">
		<div class="displaytop"></div>
		<div class="displaymid"> 
		  <%
             	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
          		List<Menu> list = mgr.getBeanByFilter(userId,"1");
          		for(int i=0; i<list.size(); i++){
          			Menu menu = list.get(i);
          %>
			<a href="#" onclick="openUrl('<%=menu.getOid() %>',5)" title="<%=menu.getAlt() %>" ><%=StringUtil.escapeHtml(menu.getTitle()) %></a> 
		 <%
			}		
		 %>
		</div>
		<div class="displaybtm"></div>
	</div>
	</em> <em id="hrefa1"><span class="icomenu02">�ͻ�����</span>
	<div id="divMenu02" class="display" style="display: none;">
		<div class="displaytop"></div>
		<div class="displaymid">
		<%
             	list = mgr.getBeanByFilter(userId,"2");
            	for(int i=0; i<list.size(); i++){
            		Menu menu = list.get(i);
          %>
			<a href="#" onclick="openUrl('<%=menu.getOid() %>',6)" title="<%=menu.getAlt() %>" ><%=StringUtil.escapeHtml(menu.getTitle()) %></a> 
		 <%
			}		
		 %> </div>
		<div class="displaybtm"></div>
	</div>
	</em> <em id="hrefa2"><span class="icomenu03">��Դ����</span>
	<div id="divMenu03" class="display" style="display: none;">
		<div class="displaytop"></div>
		<div class="displaymid">
		<%
             	list = mgr.getBeanByFilter(userId,"3");
            	for(int i=0; i<list.size(); i++){
            		Menu menu = list.get(i);
          %>
			<a href="#" onclick="openUrl('<%=menu.getOid() %>',7)" title="<%=menu.getAlt() %>" ><%=StringUtil.escapeHtml(menu.getTitle()) %></a> 
		 <%
			}		
		 %> </div>
		<div class="displaybtm"></div>
	</div>
	</em> <em id="hrefa3"><span class="icomenu04">ͳ�Ʊ���</span>
	<div id="divMenu04" class="display" style="display: none;">
		<div class="displaytop"></div>
		<div class="displaymid"> 
		<%
             	list = mgr.getBeanByFilter(userId,"4");
            	for(int i=0; i<list.size(); i++){
            		Menu menu = list.get(i);
          %>
			<a href="#" onclick="openUrl('<%=menu.getOid() %>',8)" title="<%=menu.getAlt() %>" ><%=StringUtil.escapeHtml(menu.getTitle()) %></a> 
		 <%
			}		
		 %> </div>
		<div class="displaybtm"></div>
	</div>
	</em> <em id="hrefa4"><span class="icomenu05">ϵͳά��</span>
	<div id="divMenu05" class="display" style="display: none;">
		<div class="displaytop"></div>
		<div class="displaymid"> 
		<%
             	list = mgr.getBeanByFilter(userId,"5");
            	for(int i=0; i<list.size(); i++){
            		Menu menu = list.get(i);
          %>
			<a href="#" onclick="openUrl('<%=menu.getOid() %>',9)" title="<%=menu.getAlt() %>" ><%=StringUtil.escapeHtml(menu.getTitle()) %></a> 
		 <%
			}		
		 %> </div>
		<div class="displaybtm"></div>
	</div>
	</em>
	</div>
    <!--index_nav  end-->
    <!--head  end-->
	<div class="wrap">
        <!--line3  layout-->
        <div class="index_line3">
        	<div class="index_line3Lef">
            	<!-- <img src="style/img/temp_flash.jpg" style="display:block;"/> -->
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,22,0" width="652" height="285">
					<param name="bgcolor" value="#ffffff" />
					<param name="play" value="true" />
					<param name="loop" value="true" />
					<param name="wmode" value="transparent" />
					<param name="quality" value="high">
					<param name="scale" value="showall" />
					<param name="menu" value="true" />
					<param name="devicefont" value="false" />
					<param name="salign" value="" />
					<param name="allowScriptAccess" value="sameDomain" />
					<param name="movie" value="index.swf" >
					<embed wmode="transparent" src="index.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="652" height="285">
					</embed>
				</object>
			</div>
            <div class="index_line3Rit">
            	<div class="coner_top1">
                	<span class="coner_topLef1"></span>
                    <span class="coner_topMid1"></span>
                    <span class="coner_topRit1"></span>
                    <br class="clearfloat"/>
                </div>
                
                <div class="index_conerCon1">
                	<h2>����</h2>
                	<%
                		TopData topData = (TopData)ContextUtil.getBean("topData");
                		List<Map<String, String>> noticeList = topData.getNoticeByUserId(String.valueOf(userId));
                	%>
                    <div class="index_con1">
                    	<%if(noticeList.size()>0){ %>
                    	<span title="<%=noticeList.get(0).get("title") %>"><%=noticeList.get(0).get("title") %></span>
                        <p title="<%=noticeList.get(0).get("content") %>"><%=noticeList.get(0).get("content") %></p>
                        <div><%=noticeList.get(0).get("creator") %></div>
                        <%}else{%>
                        	<p>���޹���</p>
                        <%} %>
                    </div>
                    <ul>
                    	<%
                    		for(int i=1;i<5;i++){
					if(noticeList.size() == 0)
						break;
                    	%>
                    	<li><span><%=noticeList.get(i).get("pubtime") %></span><a href="#" title="<%=noticeList.get(i).get("title") %>"><%=noticeList.get(i).get("title") %></a></li>
                    	<%
                    		}
                    	%>
                    </ul>
                    
                    <br class="clearfloat"/>
                </div>
                
                 <div class="more_wrap">
                    	 <a class="more" href="#">����</a>
                </div>
                
                <div class="coner_bottom1">
                	<span class="coner_btmLef1"></span>
                    <span class="coner_btmMid1"></span>
                    <span class="coner_btmRit1"></span>
                    <br class="clearfloat"/>
                </div>
            
            </div>
            <br class="clearfloat"/>
        </div>
        <!--line3  end-->
        <!--line4  layout-->
        <div class="index_line4">
        	<div class="index_line4Lef">
            	<div class="line4_lef">
                	<h2>���칤��</h2>
                    <div class="coner_top2">
                        <span class="coner_topLef2"></span>
                        <span class="coner_topMid2"></span>
                        <span class="coner_topRit2"></span>
                        <br class="clearfloat"/>
               		</div>
                    <div class="index_conerCon2">
                     	<table cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th>״̬</th>
                                <th>����</th>
                                <th>ʱ��</th>
                            </tr>
                        	<tr bgcolor="#e7ebe6">
                            	<td width="50" style="text-align: center;"><img src="style/img/icon5.jpg" /></td>
                                <td width="215"><a href="#" title="����400��������;�����칫���������ɹ�">����400��������;�����칫���������ɹ�</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                            <tr>
                            	<td width="50" style="text-align: center;"><img src="style/img/icon5.jpg" /></td>
                                <td width="215"><a href="#">11�·ݵ�λ���պ�ʱ�����ճɹ�</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                            <tr bgcolor="#e7ebe6">
                            	<td width="50" style="text-align: center;"><img src="style/img/icon6.jpg" /></td>
                                <td width="215"><a href="#">�û���Ƿ�ѵ��Ǹ߶�ͣ��״̬</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                            <tr>
                            	<td width="50" style="text-align: center;"><img src="style/img/icon6.jpg" /></td>
                                <td width="215"><a href="#">����NGBOSSϵͳ����ʹ�ò�ȫ</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                            <tr bgcolor="#e7ebe6">
                            	<td width="50" style="text-align: center;"><img src="style/img/icon5.jpg" /></td>
                                <td width="215"><a href="#">�����ɷѻ�ˢ���ɷ�ʧ��</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                            <tr>
                            	<td width="50" style="text-align: center;"><img src="style/img/icon6.jpg" /></td>
                                <td width="215"><a href="#">����111111111111</a></td>
                                <td width="23">2010/12/26</td>
                            </tr>
                        </table>
                        
                    	<br class="clearfloat"/>
                    </div>
                    
                    <div class="more_wrap">
                    	 <a class="more" href="#">����</a>
                    </div>
                    
                    <div class="coner_bottom2">
                        <span class="coner_btmLef2"></span>
                        <span class="coner_btmMid2"></span>
                        <span class="coner_btmRit2"></span>
                    	<br class="clearfloat"/>
                	</div>
                </div>
                <div class="line4_rit">
                	<h2>���˼ƻ�����</h2>
                    <div class="coner_top2">
                        <span class="coner_topLef2"></span>
                        <span class="coner_topMid2"></span>
                        <span class="coner_topRit2"></span>
                        <br class="clearfloat"/>
               		</div>
                    <div class="index_conerCon2">
                     	<!--����Ӧ�������ǵ�������ɵģ�����ʱ��һ��ͼƬ-->
                        <div class="date">
                        	<select id="select_year" onchange='change(this, $("#select_month")[0], $("#select_day")[0])'></select>��
				            <select id="select_month" onchange='change(this,$("#select_day")[0])'></select>�� 
				            <select id="select_day"></select>��
				            <input type="button" value="��ѯ" style="cursor: pointer;" onclick="getWorkPlan()"/>
                        </div>
                        <div class="date_downCon" id="workPlanData">
                            <br class="clearfloat"/>
                            
                    		<br class="clearfloat"/>
                        </div>
                    </div>
                    
                    <div class="more_wrap">
                    	 <a class="more" href="#">����</a>
                    </div>
                    
                    <div class="coner_bottom2">
                        <span class="coner_btmLef2"></span>
                        <span class="coner_btmMid2"></span>
                        <span class="coner_btmRit2"></span>
                    	<br class="clearfloat"/>
                	</div>
                </div>
                <br class="clearfloat"/>
            </div>
            <div class="index_line4Rit">
            	<h2 style="margin-top:3px; background:url(style/img/icon4.jpg) no-repeat left center;">�ҵ�����</h2>
            	<div style="height: 80px">
                <table cellpadding="0" cellspacing="0" width="100%">
                	<%
	                	FavoritesMgr favoritesMgr = (FavoritesMgr)ContextUtil.getBean("favoritesMgr");
                		String menuIdsStr = favoritesMgr.getBeanByUserId(111).getMenuIds();
                		try{
                			menuIdsStr = menuIdsStr.substring(1,menuIdsStr.length()-1);
	                		String[]  menuIds = menuIdsStr.split(",");
		                	for(int i=0;i<12&&i<menuIds.length;i++){
		                		if(menuIds[i].equals(""))
		                			continue;
		                		if(Cache.MenuMap.get(menuIds[i])==null)
		                			continue;
		                		if((i+1)%3==1){
		                			out.print("<tr>");
		                		}
                	%>
                    	<td width="99"><a href="#" onclick="openUrl('<%=menuIds[i] %>');" title="<%=Cache.MenuMap.get(menuIds[i]).getAlt() %>"><%=Cache.MenuMap.get(menuIds[i]).getTitle() %></a></td>
                        <%
                        	if((i+1)%3==0){
                        		out.print("</tr>");
                        	}
                        %>
                        <%
		                	}
                		}catch(Exception e){
                			
                		}
                        %>
                </table>
                </div>
                <br class="clearfloat"/>
            	<h2>��ظ澯</h2>
                <div class="coner_top3">
                    <span class="coner_topLef3"></span>
                    <span class="coner_topMid3"></span>
                    <span class="coner_topRit3"></span>
                    <br class="clearfloat"/>
           		</div>
                
                <div class="index_conerCon3">
                	<div class="con3_block1">
                    	<img src="style/img/icon7.jpg"/>
                        <div class="img_blockRit">
                       		<p><span>�ҹ���ĸ澯</span><a onclick="openUrl2('/sysmgr/alarmMain/alarmMonitorMain.jsp')">49��</a></p>
                            <div><span>���ؾ���</span><a href="#">9��</a><span>��Ҫ����</span><a href="#">12��</a></div>
                        </div>
                        <br class="clearfloat"/>
                    </div>
                    <div class="con3_block2">
                    	<img src="style/img/icon8.jpg"/>
                        <div class="img_blockRit">
                       		<p><span>�ҹ�ע�ĸ澯</span><a onclick="openUrl2('/sysmgr/alarmMain/alarmMonitorMain.jsp')">49��</a></p>
                            <div><span>���ؾ���</span><a href="#">9��</a><span>��Ҫ����</span><a href="#">12��</a></div>
                        </div>
                        <br class="clearfloat"/>
                    </div>
                </div>
                    
                <div class="coner_bottom3">
                    <span class="coner_btmLef3"></span>
                    <span class="coner_btmMid3"></span>
                    <span class="coner_btmRit3"></span>
                	<br class="clearfloat"/>
            	</div>
            	<br class="clearfloat"/>
            </div>
        </div>
        <!--line4  end-->
        <!--line5  layout-->
        <div class="index_line5">
        	<div class="index_line5Lef">
            	<div class="line5_lef">
                    <div class="coner_top2">
                        <span class="coner_topLef2 top_lef5"></span>
                        <span class="coner_topMid2"></span>
                        <span class="coner_topRit2 top_rit5"></span>
                        <br class="clearfloat">
                    </div>
                    
                    <div class="h2_wrap">
                    	<h2 class="h2_absolute">�����ĵ�</h2>
                    </div>
                    
                    <div class="index_conerCon2 index_conerCon22">

                        <table cellpadding="0" cellspacing="0">
                        	<%	
                        		DocumentMgr documentMgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
                        		List<Document> d = documentMgr.getBeanByUserId(userId);
                        		for(int i=0;i<6;i++){
                        			Document bean = d.get(i);
                        	%>
                        	<tr bgcolor="<%=i%2==0?"#e7ebe6":"#FFFFFF" %>">
                            	<td width="50" style="text-align: center;"><%=bean.getFiletype() %></td>
                                <td width="215"><a href="/portal/manager/document/download.jsp?oid=<%=bean.getOid() %>" title="<%=bean.getName() %>" target="_blank"><%=bean.getName()%></a></td>
                                <td width="23"><%=sdf.format(bean.getCreateTime()) %></td>
                            </tr>
                            <%
                        		}
                            %>
                        </table>
                        
                    	<br class="clearfloat"/>
                    </div>
                    
                    <div class="more_wrap">
                    	 <a class="more" href="#">����</a>
                    </div>
                    
                    <div class="coner_bottom2">
                        <span class="coner_btmLef2 btm_lef5"></span>
                        <span class="coner_btmMid2"></span>
                        <span class="coner_btmRit2 btm_rit5"></span>
                        <br class="clearfloat">
                    </div>
            	</div>
                <div class="line5_rit">
                	 <div class="coner_top2 top6">
                        <span class="coner_topLef2 top_lef6"></span>
                        <span class="coner_topMid2 top_mid6">
                        	<ul class="title6">
                            	<li id="li1_title">֪ʶ��</li>
                                <li onmouseover="action2(1)" class="li_on1" id="li1">�ȵ�֪ʶ</li>
                                <li onmouseover="action2(2)" id="li2">֪ʶ�ʴ�</li>
                                <br class="clearfloat">
                            </ul>
                        </span>
                        <span class="coner_topRit2 top_rit6"></span>
                        <br class="clearfloat">
                     </div>
                    
                     <div class="index_conerCon2 con6" id="index_conerCon2">
                    	<br class="clearfloat"/>
                     </div>
                     
                     <div class="more_wrap">
                    	 <a class="more" href="#">����</a>
                     </div>
                     
                     <div class="coner_bottom2">
                        <span class="coner_btmLef2 btm_lef5"></span>
                        <span class="coner_btmMid2"></span>
                        <span class="coner_btmRit2 btm_rit5"></span>
                        <br class="clearfloat">
                    </div>
                    
            	</div>
                <br class="clearfloat"/>
            </div>
            <div class="index_line5Rit">
                <form>
                    <span class="input_wd">���Ͷ���</span>
                    <span class="input_message"><input type="text" id="smsUsersValues"/><input type="hidden" id="smsUsers"/></span>
                    <span><input class="button_search" type="button" onclick="openModalDialog('/resm/interface/personList.jsp?id=&muti=1')"/></span>
                    <br class="clearfloat"/>
                    <span><textarea cols="45" rows="5" id="smsContent"></textarea> </span>
                    <span><input class="button_search2" type="button" onclick="sendSms()"/></span>
                	<br class="clearfloat"/>
                </form>
                <form class="form2">
                	<span class="input_wd">��Ա��ѯ</span>
                    <span class="input_message message2"><input type="text" id="keyWord"/></span>
                    <span><input class="button_search" type="button" onclick="searchUser()"/></span>
                    <br class="clearfloat"/>
                </form>
                <div class="line5_formBtm" id="seachUser"/>
            </div>
        	<br class="clearfloat"/>
        </div>
        <!--line5  end-->
        <!--footer  layout-->
       	<div class="footer">
        	<div class="footer_left">
            	<div>
                	<p>��ǰ�û���2010</p>
                    <p>���յ�¼����13888</p>
            	</div>
            </div>
            <div class="footer_right">
				<div>
                	<p>�����ˣ�����Ա13888888888</p>
                    <p>���ߣ�0551-88888888</p>
                </div>
            </div>
            <br class="clearfloat"/>
        </div>
        <!--footer  layout-->
    </div>
</body>
</html>
