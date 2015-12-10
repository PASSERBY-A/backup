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

.container{border:1px solid #999;height:auto;width:1100px;margin:0 auto;font-family:Verdana, Geneva, sans-serif;font-size:12px;padding:10px;}
.container .title{text-align:center;font-size:20px;font-weight:bold;border-bottom:1px solid #999;color:#333;}
.container .title img{margin:2px 0 10px 0;}
.container .sub_title{font-weight:bold;text-align:center;margin:3px 0;}
.container .left{display:inline-block;width:25%;height:60px;line-height:140%;margin-left:30px;}
.container .right{display:inline-block;width:70%;height:60px;position:relative;clear:right;}
.container .right p{position:absolute; top:25px;}
.container .right label{display:inline-block;width:auto;padding:2 10px;vertical-align:top;}
.container .right span{display:inline-block;width:auto;padding:0 10px;vertical-align:top;}
.container #resultDiv{text-align:left;margin:50px 30px 30px 30px;}

.container .list{width:95%;margin:20px 20px;clear:both;border-spacing:0px;border:1px solid #999;border-radius:5px 5px 0 0;}
.container .list thead tr{border:1px solid #aaa;background:#eee;}
.container .list thead tr td{padding:10px 5px;}
.container .list tbody tr td{text-align:center;padding:5px 0;}
.container .list tfoot .sum{border-top:1px #999 solid;border-bottom:1px #999 solid;padding:5px;text-align:center;}

.container .bottom p{padding:5px 10px;margin:5px 0;}
.container .bottom label{display:inline-block;width:500px;}
.container .bottom span button{padding:2px 5px;width:80px;}
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
backendRoleChecker.roleArray = ["EMERGENCY-USER", "HR-ADMIN"];

$(document).ready(function() {
	$("#userLoadBtn").click(function(){
	     var targetUserId = $("#targetUserId").val();
	
	     clearTelephoneSel();
	     clearYearSel();
	     clearMonthSel();
	     clearSearchResult();
	     
	     loadUserInfo(targetUserId);
	     getTelephonesByUser(targetUserId);
	});
	$("#telNumberSel").change(function() {
	    clearSearchResult();
	    clearYearSel();
	    clearMonthSel();
	    
	    var telNumber = $("#telNumberSel").val();
	    getAvailableYears(telNumber);
	});
	$("#yearSel").change(function() {
	    clearSearchResult();
	    clearMonthSel();
	    
	    var uid = $("#targetUserId").val();
	    var telNumber = $("#telNumberSel").val();
	    var year = $("#yearSel").val();
	    getAvailableMonths(uid, telNumber, year);
	});
	$("#monthSel").change(function() {
	    clearSearchResult();
	});
    $("#searchPar").click(function() {
        queryForBill();
    });
    
    clearTelephoneSel();
    clearYearSel();
    clearMonthSel();
    
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
function queryForBill() {
	var targetUserId = $("#targetUserId").val();
	var tel = $("#telNumberSel").val();
    var year = $("#yearSel").val();
    var month = $("#monthSel").val();
    
    if (tel=='' || year=='-1' || month=='-1') {
        alert("Please specify valid telephone number, year and month.");
        return;
    }
	
    var config = {
        url : commons.webserversite + '/telephoneBillService/getTelephoneBillInfoForAdmin',
        type : 'POST',
        data : {
        	targetUserId : targetUserId,
            telephoneNumber : tel,
            year  : year,
            month : month
        },
        success : 'getTelephoneBillInfoForAdmin_Success'
    };
    doAJax.doConfig(config);
}
function getTelephoneBillInfoForAdmin_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		var content = "Bill ID: " + obj.telephoneBillEntity.billId;
		content += " | ";
		
		var lastUpdateTime = obj.telephoneBillEntity.lastUpdateTime;
		var theDate = new Date(lastUpdateTime.year, lastUpdateTime.month,
				lastUpdateTime.date);
	    var lutStr = theDate.Format("dd/MM/yyyy");
		content += "Last updated time: " + lutStr;
		content += " | ";
		
		var status = obj.telephoneBillEntity.status;
		if (status == "ACTIVE") {
			content += "status: UNSAVED";	
		} else if (status == "SAVED") {
			content += "status: <span id='statusSpan'>SAVED</span>";
            content += " | ";
            content += "<input id='unsaveBtn' type='button' value='Unsave it!' onclick=\"javascript:activateTelephoneBillStatus('"+obj.telephoneBillEntity.billId+"');\" />";			
		} else if (status == "SENT") {
			content += "status: <span id='statusSpan'>Sent to SAP</span>";
			content += " | ";
			content += "<input id='unsaveBtn' type='button' value='Unsave it!' onclick=\"javascript:activateTelephoneBillStatus('"+obj.telephoneBillEntity.billId+"');\" />";
		}
		
		$("#resultDiv").html(content);
	} else {
        alert(obj.returnMessage);
    }
}
function getAvailableYears(tel) {
    var config = {
        url : commons.webserversite + '/telephoneBillService/getAvailableYearsByTel',
        type : 'POST',
        data : {
            telephoneNumber : tel
        },
        success : 'getAvailableYears_Success'
    };
    doAJax.doConfig(config);
}
function getAvailableYears_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var list = obj.yearList;
        for (var i = 0; i < list.length; i++) {
            var yyyy = list[i].year;
            var status = list[i].status;
            
            var displayColor = 'black';
            if (status=='SAVED' || status=='SENT') {
                displayColor = 'gray';
            }
            $("#yearSel").append("<option style='color:"+displayColor+"' value='"+yyyy+"'>"+yyyy+"</option>");
        }
    } else {
        alert(obj.returnMessage);
    }
}
function getAvailableMonths(userId, tel, year) {
    var config = {
        url : commons.webserversite + '/telephoneBillService/getAvailableMonthsByTelAndYearAndUid',
        type : 'POST',
        data : {
        	userId : userId,
            telephoneNumber : tel,
            year : year
        },
        success : 'getAvailableMonths_Success'
    };
    doAJax.doConfig(config);
}
function getAvailableMonths_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var list = obj.monthList;
        var month = ['', 'Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        for (var i = 0; i < list.length; i++) {
            var mm = list[i].month;
            var status = list[i].status;
            var mmName = month[mm];
                
            var displayColor = 'black';
            if (status=='SAVED' || status=='SENT') {
                displayColor = 'gray';
            }
            $("#monthSel").append("<option style='color:"+displayColor+"' value='"+mm+"'>"+mmName+"</option>");
        }
    } else {
        alert(obj.returnMessage);
    }
}
function activateTelephoneBillStatus(billId) {
	var msg = "You are unsaving bill(bill id: "+billId+"). Continue?";
    if (!confirm(msg)) {
    	return;
    }
    var config = {
        url : commons.webserversite + '/telephoneBillService/activateTelephoneBillStatus',
        type : 'POST',
        data : {
        	billId : billId
        },
        success : 'activateTelephoneBillStatus_Success'
    };
    doAJax.doConfig(config);
}
function activateTelephoneBillStatus_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		$("#statusSpan").html("unsaved");
		$("#unsaveBtn").remove();
	} else {
        alert(obj.returnMessage);
    }
}
function clearTelephoneSel() {
    $("#telNumberSel").empty();
    $("#telNumberSel").append("<option value=''>Select one ...</option>");
}
function clearYearSel() {
    $("#yearSel").empty();
    $("#yearSel").append("<option value='-1'>Select one ...</option>");
}
function clearMonthSel() {
    $("#monthSel").empty();
    $("#monthSel").append("<option value='-1'>Select one ...</option>");
}
function clearSearchResult() {
	$("#resultDiv").html("");
}
function loadUserInfo(targetUserId) {
    var config = {
        url : commons.webserversite
                + '/userInfoService/getUserInfoByUserId',
        type : 'POST',
        data : {
            targetUserId : targetUserId
        },
        success : 'loadUserInfo_Success'
    };
    doAJax.doConfig(config);
}
function loadUserInfo_Success(obj) {
    if (obj != null && obj.returnCode == "SUCCESS") {
        $("#firstName").html("<b>" + obj.firstName + "</b>");
        $("#lastName").html("<b>" + obj.lastName + "</b>");
        $("#staffCode").html("<b>" + obj.staffCode + "</b>");
    } else {
        alert(obj.returnMessage);
    }
}
function getTelephonesByUser(targetUserId) {
    var config = {
        url : commons.webserversite + '/userTelephoneService/getTelephonesByUserId',
        type : 'POST',
        data : {
            targetUserId : targetUserId
        },
        success : 'getTelephonesByUser_Success'
    };
    doAJax.doConfig(config);
}
function getTelephonesByUser_Success(obj) {
    if (obj != null) {
        if (obj.returnCode=="SUCCESS") {
            var list = obj.userTelList;
            for (var i = 0; i < list.length; i++) {
                var theNumber = list[i];
                $("#telNumberSel").append("<option value='"+theNumber+"'>"+theNumber+"</option>");
            }
        } else {
            alert(obj.returnMessage);
        }
    } else {
        alert("System error. Please contact administrator.");
    }
}
Date.prototype.Format = function (fmt) { 
    var o = {
        "M+": this.getMonth() + 1, 
        "d+": this.getDate(),
        "h+": this.getHours(), 
        "m+": this.getMinutes(), 
        "s+": this.getSeconds(), 
        "q+": Math.floor((this.getMonth() + 3) / 3), 
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + 1900 + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
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
        - bill status check
    </div>
    
    <div id="mainContent" style="display:none;">
	    <div class="left">
	        <p>User ID:
	            <input id="targetUserId" />
	            <input type="button" id="userLoadBtn" value="Find" />
	        </p>
	        <p>First Name:  <span id="firstName"><b> </b></span></p>
	        <p>Last Name:   <span id="lastName"><b> </b></span></p>
	        <p>Staff code:  <span id="staffCode"><b> </b></span></p>
	    </div>
	
	    <div class="right">
	        <p>
	            <label>Telephone Number: 
	                <select id="telNumberSel">
	                    <option value="">Select one ...</option>
	                    <option value="111111">111111</option>
	                </select>
	            </label>
	            <label>Year: 
	                <select id="yearSel">
	                    <option value="-1">Select one ...</option>
	                    <option selected="selected" value="2014">2014</option>
	                    <option style="color:gray" value="2013">2013</option>
	                </select>
	            </label>
	            <label>Month:
	                <select id="monthSel">
	                    <option value="-1">Select one ...</option>
	                    <option style="color:black" value="3">Mar</option>
	                    <option selected="selected" style="color:gray" value="4">Apr</option>
	                </select>
	            </label>
	            <span>
	                <input id="searchPar" value="Search" type="button">
	            </span>
	        </p>
	    </div>
	    
	    <div id="resultDiv"></div>
    </div>
</div>

</body>
</html>

