<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Tel billing system - call record list</title>
<link type="text/css" rel="stylesheet" href="../css/reset-min.css">

<style type="text/css">
body { font-size: 12px;font-family: "微软雅黑";}

.container{border:1px solid #999;height:auto;width:1400px;margin:0 auto;font-family:Verdana, Geneva, sans-serif;font-size:12px;padding:10px;}
.container .title{text-align:center;font-size:20px;font-weight:bold;border-bottom:1px solid #999;color:#333;}
.container .title img{margin:2px 0 10px 0;}
.container .sub_title{font-weight:bold;text-align:center;margin:3px 0;}
.container .contentDiv{text-align:left;margin:20px 20px;}
.container .contentDiv .vColumn{display:inline-block;width=40%;text-align:left;margin:0px;}
.container .contentDiv .vSeparator{display:inline-block;width:10px;}
.container .left{display:inline-block;width:250px;height:60px;line-height:140%;}
.container .right{display:inline-block;width:540px;height:60px;position:relative;clear:right;}
.container .right label{display:inline-block;width:auto;padding:0 10px;}

.container .list{width:95%;margin:20px 20px;clear:both;border-spacing:0px;border:1px solid #999;border-radius:5px 5px 0 0;}
.container .list thead tr{border:1px solid #aaa;background:#eee;}
.container .list thead tr td{padding:10px 5px;}
.container .list tbody tr td{text-align:center;padding:5px 0;}
.container .list tfoot .sum{border-top:1px #999 solid;border-bottom:1px #999 solid;padding:5px;text-align:center;}

.container .bottom p{padding:5px 10px;margin:5px 0;}
.container .bottom label{display:inline-block;width:500px;}
.container .bottom span button{padding:2px 5px;width:80px;}

.container #rIntrDiv{margin: 10px 0px 0px 20px;}
.container #rMapDiv{margin: 10px 0px 0px 20px;}
.container #rMapDiv .aRow{padding-left:30px; margin-bottom:-1px;}
.container #rMapDiv .rTitleCol{display: inline-block; width: 160px; border: 1px solid; margin-right:-5px; background-color:#eeeeee; text-align:center; padding: 3px 0;}
.container #rMapDiv .rTitleRow{display: inline-block; width: 160px; border: 1px solid; margin-right:-5px; background-color:#eff0b3; text-align: right; padding: 3px 10px 3px 0;}
.container #rMapDiv .rDataSlot{display: inline-block; width: 160px; border: 1px solid; margin-right:-5px; text-align:center; padding: 3px 0;}
</style>

<script src="../js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="../js/jquery-ui.min.js" type="text/javascript"></script>

<script src="../js/library/jquery.json.js" type="text/javascript"></script>

<script src="../js/core/dataTables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../js/core/dataTables/colResizable.min.js" type="text/javascript"></script>

<script src="../js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="../js/common/ucloud_commons_ajax.js" type="text/javascript"></script>
<script src="../js/common/vw_backend.js" type="text/javascript"></script>

<!-- <script src="../js/pages/vwg-call-record/vwgCallRecordList.js" type="text/javascript"></script> -->

<script type="text/javascript">
backendRoleChecker.roleArray = ["EMERGENCY-USER", "SYSTEM-ADMIN"];

$(document).ready(function() {
	commons.webserversite = "/volkswagen-tel-billing-ws/rest";
	
	$("#emergencyUserAddBtn").click(function() {
        addRowForEmergencyUser();
    });
    $("#systemAdminAddBtn").click(function() {
        addRowForSystemAdmin();
    });
    $("#hrAdminAddBtn").click(function() {
        addRowForHrAdmin();
    });
    $("#auditorAddBtn").click(function() {
        addRowForAuditor();
    });
    $("#itassertAddBtn").click(function() {
    	addRowForItAssert();
    });
    
    $("#controllingAddBtn").click(function() {
    	addRowForControlling();
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
        		$("#mainContent").show();
        		loadAllRoleUsers();
        		
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
function addRowContent(targetContainerElementId, uniquePageElemId, userId, roleName, savedEntityId) {
    var content = "<li id='li-"+uniquePageElemId+"'>";
    if (savedEntityId == 0) {
        content += "<input id='uniquePageElemId-"+uniquePageElemId+"' value='"+userId+"' />";
    } else {
    	content += "<input id='uniquePageElemId-"+uniquePageElemId+"' value='"+userId+"' disabled />";
    }
    content += "<input id='savedEntityId-"+uniquePageElemId+"' type='hidden' value='"+savedEntityId+"' />";
    if (savedEntityId == 0) {
	    content += " ";
	    content += "<input type='button' value='Save' id='sv-"+uniquePageElemId+"' onclick=\"javascript:saveUserRole('"+uniquePageElemId+"', '"+roleName+"')\">";
    }
    content += " ";
    content += "<input type='button' value='Remove' id='rm-"+uniquePageElemId+"' onclick=\"javascript:removeUserRole('"+uniquePageElemId+"')\">";
    content += "</li>";
    
    $("#"+targetContainerElementId).append(content);
}
function addRowForEmergencyUser() {
	var randomVal = Math.floor(Math.random()*999999+1);
    addRowContent('emergencyUserTable', randomVal, '', 'EMERGENCY-USER', 0);
}
function addRowForSystemAdmin() {
	var randomVal = Math.floor(Math.random()*999999+1);
	addRowContent('systemAdminTable', randomVal, '', 'SYSTEM-ADMIN', 0);
}
function addRowForHrAdmin() {
    var randomVal = Math.floor(Math.random()*999999+1);
    addRowContent('hrAdminTable', randomVal, '', 'HR-ADMIN', 0);
}
function addRowForAuditor() {
    var randomVal = Math.floor(Math.random()*999999+1);
    addRowContent('auditorTable', randomVal, '', 'AUDITOR', 0);
}

function addRowForItAssert() {
    var randomVal = Math.floor(Math.random()*999999+1);
    addRowContent('itassertTable', randomVal, '', 'IT-ASSERT', 0);
}

function addRowForControlling() {
    var randomVal = Math.floor(Math.random()*999999+1);
    var selectValue = $("#controllingRoleNames").val();
    addRowContent('controllingTable', randomVal, '', selectValue, 0);
}



function saveUserRole(uniquePageElemId, roleName) {
	var userId = $("#uniquePageElemId-" + uniquePageElemId).val();
    var config = {
        url : commons.webserversite + '/userRoleService/saveRoleUser',
        type : 'POST',
        data : {
        	userId : userId,
        	roleName : roleName,
        	uniquePageElemId : uniquePageElemId
        },
        success : 'saveUserRole_Success'
    };
    doAJax.doConfig(config);
}
function saveUserRole_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		var uniquePageElemId = obj.uniquePageElemId;
		var entityId = obj.userRoleEntity.id;
		
		$("#savedEntityId-" + uniquePageElemId).val(entityId);
		$("#uniquePageElemId-" + uniquePageElemId).attr("disabled", "disabled");
		$("#sv-" + uniquePageElemId).hide();
        alert("Saved successfully.");
    } else {
        alert(obj.returnMessage);
    }
}
function removeUserRole(uniquePageElemId) {
	var savedEntityId = $("#savedEntityId-" + uniquePageElemId).val();
	if (savedEntityId > 0) {
		// - remove record on server.
	    var config = {
            url : commons.webserversite + '/userRoleService/deleteRoleUser',
            type : 'POST',
            data : {
            	entityId : savedEntityId,
            	uniquePageElemId : uniquePageElemId
            },
            success : 'removeUserRole_Success'
        };
        doAJax.doConfig(config);
	} else {
		$("#li-"+uniquePageElemId).remove();
	}
}
function removeUserRole_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		alert("Removed successfully.");
		$("#li-"+obj.uniquePageElemId).remove();
	} else {
        alert(obj.returnMessage);
    }
}
function loadAllRoleUsers() {
    var config = {
        url : commons.webserversite + '/userRoleService/getAllRoleUsers',
        type : 'GET',
        success : 'loadAllRoleUsers_Success'
    };
    doAJax.doConfig(config);	
}
function loadAllRoleUsers_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var userRoleList = obj.userRoleList;
        for (var i in userRoleList) {
        	var roleName = userRoleList[i].roleName;
        	var targetTableName = "Table";
        	if ('EMERGENCY-USER' == roleName) {
        		targetTableName = "emergencyUser" + targetTableName;
        	} else if ('SYSTEM-ADMIN' == roleName) {
        		targetTableName = "systemAdmin" + targetTableName;
        	} else if ('HR-ADMIN' == roleName) {
        		targetTableName = "hrAdmin" + targetTableName;
            } else if ('AUDITOR' == roleName) {
            	targetTableName = "auditor" + targetTableName;
            }else if ('IT-ASSERT' == roleName) {
            	targetTableName = "itassert" + targetTableName;
            }
            else if ('VCIC-CONTROLLING' == roleName) {
            	targetTableName = "controlling" + targetTableName;
            	userRoleList[i].userId=userRoleList[i].userId+'[VCIC]';
            	
            }
            else if ('VGIC-CONTROLLING' == roleName) {
            	targetTableName = "controlling" + targetTableName;
            	userRoleList[i].userId=userRoleList[i].userId+'[VGIC]';
            }
            else if ('AUDI-CONTROLLING' == roleName) {
            	targetTableName = "controlling" + targetTableName;
            	userRoleList[i].userId=userRoleList[i].userId+'[AUDI]';
            }
            else if ('VCRA-CONTROLLING' == roleName) {
            	targetTableName = "controlling" + targetTableName;
            	userRoleList[i].userId=userRoleList[i].userId+'[VCRA]';
            }
        	addRowContent(targetTableName, userRoleList[i].id, userRoleList[i].userId, roleName, userRoleList[i].id);
        	/* if(roleName.indexOf("CONTROLLING")!=-1)
        	{
        		var r = roleName.substring(0,roleName.indexOf("-"));
        		//alert();
        		var content='<select><option>'+r+'</option></select>';
        		 $("#controllingTable").append(content);
        		//alert(roleName);
        	} */
        	
        }
    } else {
        alert(obj.returnMessage);
    }
}
</script>

</head>

<body>
<%@include file="../vwg-common/header-userrole.jsp" %>

<div class="container">
    <div class="title">
        <img src="../images/logo_01.jpg" border="0" />
    </div>
    <div class="sub_title">
        Telephone Billing System<br />
        - role assignment
    </div>
    
    <div id="introductionDiv">
        <div id="rIntrDiv">
            <p style="margin-bottom:5px;">Available back-end functionalities:</p>
            <p style="padding-left:30px;">
	            A. Role assignment.<br />
				B. Overall function configuration. (Column title text, SAP file switch, SAP file repository)<br />
				C. Call collection check.<br />
				D. Bill status check. (Save/Unsave)<br />
				E. Bill status overview. (Checklist)<br />
				F. IT assert report. <br />
				G. controlling report. <br />
			</p>
        </div>
        <div id="rMapDiv">
            <p style="margin-bottom:5px;">Roles &amp; Mappings:</p>
            <div class="aRow">
                <div class="rTitleRow">&nbsp;</div>
                <div class="rTitleCol">A: role assignment</div>
                <div class="rTitleCol">B: overall function config</div>
                <div class="rTitleCol">C: call collection check</div>
                <div class="rTitleCol">D: bill status check</div>
                <div class="rTitleCol">E: bill status overview</div>
                <div class="rTitleCol">F: IT assert report</div>
                <div class="rTitleCol">G: controlling report</div>
            </div>
            <div class="aRow">
                <div class="rTitleRow">Emergency user</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
            </div>
            <div class="aRow">
                <div class="rTitleRow">System admin</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
            </div>
            <div class="aRow">
                <div class="rTitleRow">HR admin</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
            </div>
            <div class="aRow">
                <div class="rTitleRow">Auditor</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
            </div>
            <div class="aRow">
                <div class="rTitleRow">IT-Assert</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">O</div>
                <div class="rDataSlot">&nbsp;</div>
            </div>
             <div class="aRow">
                <div class="rTitleRow">Controlling</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">&nbsp;</div>
                <div class="rDataSlot">O</div>
            </div>
        </div>
    </div>
    
    <div id="mainContent" class="contentDiv" style="display:none;">
	    <div class="vColumn">
	        <div style="background-color:#eff0b3">Role: Emergency user</div>
	        <div>
                Users: <input type="button" value="Add user" id="emergencyUserAddBtn" />
	        </div>
	        <div>
	            <ol id="emergencyUserTable"></ol>
	        </div>
	    </div>
	    
	    <div class="vSeparator"></div>
	    <div class="vColumn" style="vertical-align:top;">
	        <div style="background-color:#eff0b3">Role: System admin</div>
	        <div>
                Users: <input type="button" value="Add user" id="systemAdminAddBtn" /> 
            </div>
	        <div>
	            <table id="systemAdminTable"></table>
	        </div>
	    </div>
	    
        <div class="vSeparator"></div>
        <div class="vColumn" style="vertical-align:top;">
            <div style="background-color:#eff0b3">Role: HR admin</div>
            <div>
                Users: <input type="button" value="Add user" id="hrAdminAddBtn" /> 
            </div>
            <div>
                <table id="hrAdminTable"></table>
            </div>
        </div>
        
        <div class="vSeparator"></div>
        <div class="vColumn" style="vertical-align:top;">
            <div style="background-color:#eff0b3">Role: Auditor</div>
            <div>
                Users: <input type="button" value="Add user" id="auditorAddBtn" /> 
            </div>
            <div>
                <table id="auditorTable"></table>
            </div>
        </div>
    
    	<div class="vSeparator"></div>
        <div class="vColumn" style="vertical-align:top;">
            <div style="background-color:#eff0b3">Role: IT-Assert</div>
            <div>
                Users: <input type="button" value="Add user" id="itassertAddBtn" /> 
            </div>
            <div>
                <table id="itassertTable"></table>
            </div>
        </div>
    
    	<div class="vSeparator"></div>
        <div class="vColumn" style="vertical-align:top;">
            <div style="background-color:#eff0b3">Role: Controlling</div>
            <div>
                Users: <input type="button" value="Add user" id="controllingAddBtn" /> 
                 <select id="controllingRoleNames">
	                  <option value="VCIC-CONTROLLING">VCIC</option>
	                  <option value="VGIC-CONTROLLING">VGIC</option>
	                  <option value="AUDI-CONTROLLING">AUDI</option>
	                  <option value="VCRA-CONTROLLING">VCRA</option>
                 </select>
                
                
            </div>
            <div>
                <table id="controllingTable"></table>
            </div>
        </div>
    
    
    </div>
    
</div>


</body>
</html>

