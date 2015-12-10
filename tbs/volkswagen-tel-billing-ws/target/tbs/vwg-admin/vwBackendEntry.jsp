<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.volkswagen.tel.billing.common.GenericConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Tel billing system - call record list</title>
<link type="text/css" rel="stylesheet" href="../css/reset-min.css">
<link type="text/css" rel="stylesheet" href="../css/public.css">

<style type="text/css">
    .container{border:1px solid #999;height:auto;width:1100px;margin:0 auto;font-family:Verdana, Geneva, sans-serif;font-size:12px;padding:10px;}
    .container .title{text-align:center;font-size:20px;font-weight:bold;border-bottom:1px solid #999;color:#333;}
    .container .title img{margin:2px 0 10px 0;}
    
    .container .sub_title{font-weight:bold;text-align:center;margin:3px 0;}
    .container .left{display:inline-block;width:250px;height:60px;line-height:140%;}
    .container .right{display:inline-block;width:540px;height:60px;position:relative;clear:right;}
    .container .right label{display:inline-block;width:auto;padding:0 10px;}
    
    .container .list{width:95%;margin:20px 20px;clear:both;border-spacing:0px;border:1px solid #999;border-radius:5px 5px 0 0;}
    .container .list thead tr{border:1px solid #aaa;background:#eee;}
    .container .list thead tr td{padding:10px 5px;}
    .container .list tbody tr td{text-align:center;padding:5px 0;}
    .container .list tfoot .sum{border-top:1px #999 solid;border-bottom:1px #999 solid;padding:5px;text-align:center;}
    .container .signature{text-align:right;padding:0 20px;}
    
    .container .mainContent {margin:20px 0;}
    .container .content-line {margin:10px 5px;}
    .container .content-line .linkSlot a {display:inline-block;text-decoration:none;color:black;}
    .container .mLabel {display:inline-block; position:relative; right:0px; width:280px; vertical-align:top;text-align:right}
    .container .midElem {display:inline-block; position:relative; right:0px; width:400px; vertical-align:top;padding-left:20px;}
    .container .linkSlot {display:inline-block; position:relative; right:0px; width:100px; vertical-align:top; padding-left: 20px;}
    .container .vSeparator {border-bottom: 1px solid; margin: 0 30px;}
</style>

<script src="../js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="../js/jquery-ui.min.js" type="text/javascript"></script>

<script src="../js/library/jquery.json.js" type="text/javascript"></script>

<script src="../js/core/dataTables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../js/core/dataTables/colResizable.min.js" type="text/javascript"></script>

<script src="../js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="../js/common/ucloud_commons_ajax.js" type="text/javascript"></script>

<script src="../js/pages/vwg-call-record/vwgCallRecordList.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	loadUserRoles();
});
function loadUserRoles() {
    var config = {
        url : commons.webserversite + '/userRoleService/getUserRoles',
        type : 'GET',
        success : 'loadUserRoles_Success'
    };
    doAJax.doConfig(config);
}
function loadUserRoles_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		var userRoleList = obj.userRoleList;
        for (var i in userRoleList) {
            var roleName = userRoleList[i].roleName;
            if (roleName == "SYSTEM-ADMIN") {
            	setRoleAssignment();
            	setOverallSystemConfig();
            	setReport1();
            	setReport2();
            	setReport3();
            	setReport4();
            } else if (roleName == "HR-ADMIN") {
            	setBillStatusCheck();
            	setBillStatusOverview();
            } else if (roleName == "AUDITOR") {
            	setCallHistoryCollection();
            } else if (roleName == "EMERGENCY-USER") {
            	setRoleAssignment();
                setOverallSystemConfig();
                setBillStatusCheck();
                setBillStatusOverview();
                setCallHistoryCollection();
                setReport1();
            	setReport2();
            	setReport3();
            	setReport4();
            }else if (roleName == "IT-ASSERT") {
            	setReport1();
             	setReport2();
            }else if (roleName.indexOf("CONTROLLING")!=-1) {
            	setReport3();
             	setReport4();
            }
            
            
            
        }
    }
}
function setRoleAssignment() {
	var ctt = "<a href='./vwUserRole.jsp'>&gt;&gt; enter</a>";
    $("#roleManageLinkSpan").html(ctt);
}
function setOverallSystemConfig() {
	var ctt = "<a href='./vwOverallConfig.jsp'>&gt;&gt; enter</a>";
    $("#overallConfigLinkSpan").html(ctt);
}
function setCallHistoryCollection() {
	var ctt = "<a href='./vwCallRecordList.jsp'>&gt;&gt; enter</a>";
    $("#callHistoryCollLinkSpan").html(ctt);
}
function setBillStatusCheck() {
	var ctt = "<a href='./vwBillStatus.jsp'>&gt;&gt; enter</a>";
    $("#billStatusCheckLinkSpan").html(ctt);
}
function setBillStatusOverview() {
	var ctt = "<a href='./vwBillStatusOverview.jsp'>&gt;&gt; enter</a>";
    $("#billStatusOverviewLinkSpan").html(ctt);
}

function setReport1() {
	var ctt = "<a href='./report1.jsp'>&gt;&gt; enter</a>";
    $("#report1").html(ctt);
}
function setReport2() {
	var ctt = "<a href='./report2.jsp'>&gt;&gt; enter</a>";
    $("#report2").html(ctt);
}

function setReport3() {
	var ctt = "<a href='./report3.jsp'>&gt;&gt; enter</a>";
    $("#report3").html(ctt);
}

function setReport4() {
	var ctt = "<a href='./report4.jsp'>&gt;&gt; enter</a>";
    $("#report4").html(ctt);
}



</script>

</head>

<body>
<%@include file="../vwg-common/header-backend.jsp" %>

<div class="container">
    <div class="title">
        <img src="../images/logo_01.jpg" border="0" />
    </div>
    <div class="sub_title">
        Telephone Billing System<br />
<!--         - report3 -->
    </div>
    
    <div class='mainContent'>
        <div class="content-line">
	        <span class='mLabel'>Role assignment.</span>
	        <span class='midElem'>(nessecary role: EMERGENCY-USER or SYSTEM-ADMIN)</span>
	        <span class='linkSlot' id="roleManageLinkSpan"></span>
        </div>
        
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Overall system function configuration.</span>
            <span class='midElem'>(nessecary role: EMERGENCY-USER or SYSTEM-ADMIN)</span>
            <span class='linkSlot' id="overallConfigLinkSpan"></span>
        </div>
        
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Bill status check.</span>
            <span class='midElem'>(nessecary role: EMERGENCY-USER or HR-ADMIN)</span>
            <span class='linkSlot' id="billStatusCheckLinkSpan"></span>
        </div>
        
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Bill status overview.</span>
            <span class='midElem'>(nessecary role: EMERGENCY-USER or HR-ADMIN)</span>
            <span class='linkSlot' id="billStatusOverviewLinkSpan"></span>
        </div>
        
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Call history collection check.</span>
            <span class='midElem'>(nessecary role: EMERGENCY-USER or AUDITOR)</span>
            <span class='linkSlot' id="callHistoryCollLinkSpan"></span>
        </div>
        
        <!-- ADD BY LIQIANG 20151019-->
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>IT Asset 1</span>
            <span class='midElem'>(nessecary role: IT-Asset or SYSTEM-ADMIN)</span>
            <span class='linkSlot' id="report1"></span>
        </div>
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>IT Asset 2</span>
            <span class='midElem'>(nessecary role: IT-Asset or SYSTEM-ADMIN)</span>
            <span class='linkSlot' id="report2"></span>
        </div>
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Controlling 1</span>
            <span class='midElem'>(nessecary role: Controlling  or SYSTEM-ADMIN)</span>
            <span class='linkSlot' id="report3"></span>
        </div>
        <div class="vSeparator"></div>
        <div class="content-line">
            <span class='mLabel'>Controlling 2</span>
            <span class='midElem'>(nessecary role: Controlling  or SYSTEM-ADMIN)</span>
            <span class='linkSlot' id="report4"></span>
        </div>        
        
        
        
        
    </div>

    
</div>


</body>
</html>

