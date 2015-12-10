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
    .container{border:1px solid #999;height:auto;width:1100px;margin:0 auto;font-family:Verdana, Geneva, sans-serif;font-size:12px;padding:10px;position:static;background:none;border-radius:0px;min-height:0px;}
    .container .title{text-align:center;font-size:20px;font-weight:bold;border-bottom:1px solid #999;color:#333;}
    .container .title img{margin:2px 0 10px 0;}
    
    .container .sub_title{font-weight:bold;text-align:center;margin:3px 0;}
    .container .left{display:inline-block;width:25%;height:60px;line-height:140%;margin-left:30px;}
    .container .right{display:inline-block;width:70%;height:60px;position:relative;clear:right;}
    .container .right p{position:absolute; top:25px;}
    .container .right label{display:inline-block;width:auto;padding:2 10px;vertical-align:top;}
    .container .right span{display:inline-block;width:auto;padding:0 10px;vertical-align:top;}
    
    .container .list{width:100%;margin:20px 0px;clear:both;border-spacing:0px;}
    .container .list thead tr{border:1px solid #999;background:#eee;}
    .container .list thead tr td{padding:2px 5px;}
    .container .list tbody tr td{text-align:center;padding:5px 0;}
    .container .signature{text-align:right;padding:0 20px;}
    
    .container .bottom p{padding:5px 10px;margin:5px 0;}
    .container .bottom label{display:inline-block;width:500px;}
    .container .bottom span button{padding:2px 5px;width:80px;}
    .page_info{text-align:center;}
    .page_info a{display:inline-block;padding:0 2px;text-decoration:none;color:blue;margin:10px 0 0 0;}
    #searchPar{padding:2px 5px; width:120px;}
    #save_print_btn{padding:2px 5px; width:120px;margin-top:10px;}
    
    a.backToTop{width:60px;height:60px;background:url(../images/top.gif) no-repeat -51px 0;text-indent:-999em} 
    a.backToTop:hover{background-position:-113px 0}
    .container .ui-widget-header {background:none;border:none;font-weight:normal} 
</style>

<script src="../js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="../js/jquery-ui.min.js" type="text/javascript"></script>

<script src="../js/library/jquery.json.js" type="text/javascript"></script>
<script src="../js/gotoTop.js" type="text/javascript"></script>

<script src="../js/core/dataTables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../js/core/dataTables/colResizable.min.js" type="text/javascript"></script>

<script src="../js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="../js/common/ucloud_commons_ajax.js" type="text/javascript"></script>
<script src="../js/common/vw_backend.js" type="text/javascript"></script>

<script src="../js/pages/vwg-call-record/vwCallRecordList_admin.js" type="text/javascript"></script>

<script type="text/javascript">
backendRoleChecker.roleArray = ["EMERGENCY-USER", "AUDITOR"];
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
    $("#save_print_btn").click(function() {
        goSavePrint();
    });
    
    // - for back-to-top
    $(".backToTop").goToTop();
    $(window).bind('scroll resize',function(){
        $(".backToTop").goToTop({
            pageWidth:960,
            duration:0
        });
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

    clearTelephoneSel();
    clearYearSel();
    clearMonthSel();
}
function clearSearchResult() {
    $("#theList > tbody").html("");
    
    indexPage.isQuery = false;    
    indexPage.init();
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
function goSavePrint() {
	var tel = $("#telNumberSel").val();
	var year = $("#yearSel").val();
	var month = $("#monthSel").val();
	
	if (tel!='' && year>0 && month>=0) {
        var theUrl = "vwgCallRecordAcknowledge.jsp?tel=" + tel + "&year=" + year + "&month=" + month;
        window.location = theUrl;
	} else {
		alert('Please specify valid telephone number, year and month.');
	}
}
function loadUIConfiguration() {
	var config = {
	    url : commons.webserversite + '/uiConfigurationService/getUIConfigurationJson',
	    type : 'GET',
	    success : 'loadUIConfiguration_Success'
	};
	doAJax.doConfig(config);
}
function loadUIConfiguration_Success(obj) {
	if (obj!=null && obj.returnCode=="SUCCESS") {
		$("#searchPar").val(obj.configContent.searchButtonText);
		$("#save_print_btn").val(obj.configContent.saveAndPrintButtonText);
		
		indexPage.titleTextArray = [
			obj.configContent.titleColumn1,
			obj.configContent.titleColumn2,
			obj.configContent.titleColumn3,
			obj.configContent.titleColumn4,
			obj.configContent.titleColumn5,
			obj.configContent.titleColumn6,
			obj.configContent.titleColumn7,
			obj.configContent.titleColumn8,
		];
		indexPage.init();
	}
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
        - call history collection check
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
	    
	    <div id="demo_jui" class="list">
	        <table id="theList" class="display">
	            <thead>
	                <tr>
	                    <th>date</th>
	                    <th>starting_time</th>
	                    <th>duration</th>
	                    <th>Called Number</th>
	                    <th>Type</th>
	                    <th>Location</th>
	                    <th>Cost</th>
	                    <th>private</th>
	                </tr>
	            </thead>
	            <tbody>
	            </tbody>
	        </table>
	    </div>
    
    </div>
    
</div>

</body>
</html>

