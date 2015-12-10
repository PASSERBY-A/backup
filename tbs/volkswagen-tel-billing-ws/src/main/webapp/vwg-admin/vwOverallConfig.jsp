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
    /* .container .title p{margin:5px 0;} */
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
    
    .container .content-line {margin:10px 5px;}
    .container .mLabel {display:inline-block; position:relative; right:0px; width:100px; vertical-align:top;text-align:right}
    .container .midElem {display:inline-block; position:relative; right:0px; width:680px; vertical-align:top;padding-left:20px;}
    .container .buttonSlot {display:inline-block; position:relative; right:0px; padding-left: 20px; width:50px; vertical-align:top}
    .container .separator {border-bottom: 1px solid; margin: 10px 5px;}
</style>

<script src="../js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="../js/jquery-ui.min.js" type="text/javascript"></script>

<script src="../js/library/jquery.json.js" type="text/javascript"></script>

<script src="../js/core/dataTables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../js/core/dataTables/colResizable.min.js" type="text/javascript"></script>

<script src="../js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="../js/common/ucloud_commons_ajax.js" type="text/javascript"></script>
<script src="../js/common/vw_backend.js" type="text/javascript"></script>

<script src="../js/pages/vwg-call-record/vwgCallRecordList.js" type="text/javascript"></script>

<script type="text/javascript">
backendRoleChecker.roleArray = ["EMERGENCY-USER", "SYSTEM-ADMIN"];
$(document).ready(function() {
    $("#submitBtn").click(function() {
    	performSubmit();
    });
    $("#sapSubmitBtn").click(function() {
        performSapSubmit();
    });
    
    loadCurentUserRoles();
});
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
            	startBizLogic();
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
function startBizLogic() {
	$("#mainContent").show();
    loadUIConfiguration();
    loadSapSubmitConfig(); 
}
function loadSapSubmitConfig() {
	var sapSubmitConfigEnabled = '<%=GenericConfig.enableSapSubmit?"YES":"NO"%>';
	if (sapSubmitConfigEnabled == "YES") {
		$("input[name='sapSubmitFlag'][value='YES']").attr('checked', 'checked');
	} else {
		$("input[name='sapSubmitFlag'][value='NO']").attr('checked', 'checked');
	}
	 
	var dir = '<%=(GenericConfig.sapSubmitDirectory==null)?"":GenericConfig.sapSubmitDirectory%>';
	$("#sapSubmitDirectory").val(dir);
}
function performSapSubmit() {
	var sapSubmitFlag = $('input:radio[name="sapSubmitFlag"]:checked').val();
	var sapSubmitDirectory = $("#sapSubmitDirectory").val();
    var config = {
        url : commons.webserversite + '/uiConfigurationService/updateSapSubmitConfig',
        type : 'POST',
        data : {
        	sapSubmitFlag : sapSubmitFlag,
        	sapSubmitDirectory : sapSubmitDirectory
        },
        success : 'performSapSubmit_Success'
    };
    doAJax.doConfig(config);
}
function performSapSubmit_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		alert(obj.returnMessage);
    }
}
function performSubmit() {
	var config = {
        url : commons.webserversite + '/uiConfigurationService/updateUIConfiguration',
        type : 'POST',
        data : {
        	configContent : $("#configContent").val()
        },
        success : 'updateUIConfiguration_Success'
    };
    doAJax.doConfig(config);
}
function updateUIConfiguration_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
        alert("Updated successfully!");
    } else {
        alert(obj.returnMessage);
    }
}
function loadUIConfiguration() {
    var config = {
        url : commons.webserversite + '/uiConfigurationService/getUIConfiguration',
        type : 'GET',
        success : 'loadUIConfiguration_Success'
    };
    doAJax.doConfig(config);
}
function loadUIConfiguration_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        $("#configContent").val(obj.configContent);
    }
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
        - overall system configuration
    </div>
    
    <div id="mainContent" style="display:none;">
    
	    <div class="content-line">
	        <span class="mLabel">
	             Text of column title &amp; button.
	        </span>
	        <span class="midElem">
	            <textarea id="configContent" cols="80" rows="15"></textarea>
	        </span>
	        <span class="buttonSlot">
	            <input id="submitBtn" type="button" value="Save" class="commonButton80" />
	        </span>
	    </div>
	
	    <div class="separator"></div>
	    <div class="content-line">
	        <span class="mLabel">Config for SAP submit.</span>
	        <span class="midElem">
	            <div style="margin-bottom: 5px">
	                Feature switch: 
					<input type="radio" name="sapSubmitFlag" value="YES" />Enable
					&nbsp;&nbsp;
					<input type="radio" name="sapSubmitFlag" value="NO" />Disable
				</div>
				<div>
	                Directory for sending file to SAP:
	                <input id="sapSubmitDirectory" size='40' style='height:23px;' />
				</div>
	        </span>
	        <span class="buttonSlot">
	            <input id="sapSubmitBtn" type="button" value="Save" class="commonButton80" />
	        </span>
	    </div>
    
    </div>
</div>


</body>
</html>

