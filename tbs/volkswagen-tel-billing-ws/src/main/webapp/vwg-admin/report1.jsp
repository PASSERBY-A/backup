<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Tel billing system - call record list</title>
<link type="text/css" rel="stylesheet" href="../css/reset-min.css">
<link type="text/css" rel="stylesheet" href="../css/public.css">
<link type="text/css" rel="stylesheet" href="../css/jquery-ui-1.10.3.custom.min.css">

<style type="text/css">
.container {border:1px solid #999;height:auto;width:1100px;margin:5px auto;font-family:Verdana, Geneva, sans-serif;font-size:12px;padding:10px;position:static;background:none;border-radius:0px;min-height:0px;}
.container .title{text-align:center;font-size:20px;font-weight:bold;border-bottom:1px solid #999;color:#333;}
.container .title img{margin:2px 0 10px 0;}

.container .sub_title{font-weight:bold;text-align:center;margin:5px 0 10px 0;}
.container .left{display:inline-block;width:25%;height:60px;line-height:140%;margin-left:30px;vertical-align:middle;}
.container .right{display:inline-block;width:100%;position:relative;clear:right;margin:5px 30px;vertical-align:top}
.container .right label{display:inline-block;width:auto;padding:2 10px;vertical-align:top;}
.container .right span{display:inline-block;width:auto;padding:0 10px;vertical-align:top;}

.container .list{width:100%;margin:10px 0px;clear:both;border-spacing:0px;}
.container .list thead tr{border:1px solid #999;background:#eee;}
.container .list thead tr td{padding:2px 5px;}
.container .list tbody tr td{text-align:center;padding:5px 0;}

.container .bottom p{padding:5px 10px;margin:5px 0;}
.container .bottom label{display:inline-block;width:500px;}
.container .bottom span button{padding:2px 5px;width:80px;}
.page_info{text-align:center;}
.page_info a{display:inline-block;padding:0 2px;text-decoration:none;color:blue;margin:10px 0 0 0;}

.container .ui-widget-header {background:none;border:none;font-weight:normal}
</style>

<script src="../js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="../js/jquery-ui.min.js" type="text/javascript"></script>

<script src="../js/library/jquery.json.js" type="text/javascript"></script>

<script src="../js/core/dataTables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../js/core/dataTables/colResizable.min.js" type="text/javascript"></script>

<script src="../js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="../js/common/ucloud_commons_ajax.js" type="text/javascript"></script>
<script src="../js/common/vw_backend.js" type="text/javascript"></script>

<script src="../js/pages/vwg-call-record/report1UserTelList.js" type="text/javascript"></script>

<script type="text/javascript">
backendRoleChecker.roleArray = ["EMERGENCY-USER", "HR-ADMIN","IT-ASSERT"];
var mmDialog;
var userId ="";
var firstName="";
var lastName="";
var staffCode="";


$(document).ready(function() {
	commons.webserversite = "/volkswagen-tel-billing-ws/rest";
    loadCurentUserRoles();
    mmDialog = initDialogs();
    
    $("#toPdfBtn").click(function() {
    
    	userId = $.trim($("#userId").val());
    	
    	if(""==userId)
    	{
    		alert("uid is not empty!");
    		return;
    		
    	}
    	
    	loadUserInfo(userId);
    	
    	
    	
    	var url = '<%=request.getContextPath()%>' + '/pdfprint'+"?type=report1&userId="+userId+'&firstName='+firstName+'&lastName='+lastName+'&staffCode='+staffCode;

        newwindow=window.open(url,'name','height=500,width=800,scrollbars=yes,resizable=yes');
        if (window.focus) {
            newwindow.focus();
        }
    	
    	
    	//alert($("#userId").val());
    
    
    
    
    });
    
    

});

function loadUserInfo(targetUserId) {
	var config = {
		url : commons.webserversite
				+ '/userInfoService/getUserInfoByUserId',
		type : 'POST',
		async:false,
		data : {
			targetUserId : targetUserId
		},
		success : 'loadUserInfo_Success'
	};
	doAJax.doConfig(config);
}
function loadUserInfo_Success(obj) {
	if (obj != null && obj.returnCode == "SUCCESS") {
		//$("#firstName").html("<b>" + obj.firstName + "</b>");
		//$("#lastName").html("<b>" + obj.lastName + "</b>");
		//$("#staffCode").html("<b>" + obj.staffCode + "</b>");
		firstName = obj.firstName;
		lastName = obj.lastName;
		staffCode = obj.staffCode;
	} else {
		alert(obj.returnMessage);
	}
}


function loadCurentUserRoles() {
    var config = {
        url : commons.webserversite + '/userRoleService/getUserRoles',
        type : 'GET',
        success : 'loadCurentUserRoles_Success'
    };
    doAJax.doConfig(config);
}
function loadCurentUserRoles_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var isQualified = false;
        var userRoleList = obj.userRoleList;
        for (var i in userRoleList) {
            var roleName = userRoleList[i].roleName;
	        if (backendRoleChecker.contains(roleName)) {
            	$("#mainContent").show();
            	
                isQualified = true;
                break;
            }
        }
        
        if (isQualified == false) {
            var msg = "This page is only available for ";
            msg += backendRoleChecker.roleArray.join(',') + ".";
            alert(msg);
        }
    } else {
        alert(obj.returnMessage);
    }
}
function initDialogs() {
    var dialog = $( "#dialog-bills" ).dialog({
        autoOpen: false,
        resizable: false,
        width: 480,
        height: 280,
        modal: true,
        open: function(event, ui) {
//            getNumbersToExecuteInBatch();
        },
        buttons: {
            "Close": function() {
                $(this).dialog("close");
            }
        }
    });
    return dialog;
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
        - Open Bills
    </div>
    
    <div id="mainContent" style="display:none;">
        <div class="right">
            <label>
                User ID: <input id="userId" />
            </label>
            <span>
                <input id="searchPar" value="Search" type="button"> <input id="toPdfBtn"  value="Export" type="button">
            </span>
        </div>
        
        <div id="demo_jui" class="list">
            <table id="theList" class="display">
                <thead>
                    <tr>
                        <th>user_id</th>
                        <th>telephone_number</th>
                        <th>Show Uncompleted Month</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    
    </div>
</div>

<div id="dialog-bills" title="Status Of Bills">
    <div>
        <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
        <span id="confirmMsg"></span>
    </div>
    <div style="clear:both;padding:10px 0 10px 30px;" id="dialogCoreContent"></div>
</div>

</body>
</html>

