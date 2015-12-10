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
    #exportPar{padding:2px 5px; width:120px;}
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
<script src="../js/pages/vwg-call-record/report4List.js" type="text/javascript"></script>

<script type="text/javascript">
backendRoleChecker.roleArray = ["EMERGENCY-USER", "SYSTEM-ADMIN","VCIC-CONTROLLING","VGIC-CONTROLLING","AUDI-CONTROLLING","VCRA-CONTROLLING"];

$(document).ready(function() {
	init();
	
	$("#exportPar").click(function() {
	    
		
		var year = $('#yearSel').val();
		
		var month = $('#monthSel').val();
		
		var costcenter =  $('#targetUserId').val();
		
    	var roleName = $('#roleName').val();
    	
    	//alert(roleName);
    	
    	var url = '<%=request.getContextPath()%>' + '/pdfprint'+"?type=report4&year="+year+'&month='+month+'&roleName='+roleName;
    	
    	window.location.href = url;
    	
       /*  newwindow=window.open(url,'name','height=500,width=800,scrollbars=yes,resizable=yes');
        if (window.focus) {
            newwindow.focus();
        }
    	 */
    	
    	//alert($("#userId").val());
    
    
    
    
    });
	
	
	
	$("#userLoadBtn").click(function(){
		//var targetUserId = $("#targetUserId").val();

		//clearTelephoneSel();
		//clearYearSel();
       // clearMonthSel();
       // clearSearchResult();
        
		//oadUserInfo(targetUserId);
		//getTelephonesByUser(targetUserId);
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
               
                if(roleName.indexOf("VCIC")!=-1 ||roleName.indexOf("EMERGENCY")!=-1 || roleName.indexOf("SYSTEM")!=-1 )
                {
                	//alert("all");
                	$("#roleName").val("all");
                	 break;
                }
                else if(roleName.indexOf("CONTROLLING")!=-1){
                	
                	$("#roleName").val(roleName);
                }  
                
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
    
    
    
    

    //clearTelephoneSel();
   // clearYearSel();
    //clearMonthSel();
}

function init()
{
	 var month = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
	
	var date = new Date(); 
	
	var currentYear = date.getFullYear();
	
	var beginYear = 2014;
	
	for(var i=0;i<=currentYear-beginYear;i++)	
	{
		var year = currentYear - i;
		
		$("#yearSel").append("<option value='"+year+"'>"+year+"</option>");
		
	}
	
	 for (var i = 0; i < month.length; i++) {
		 
		 $("#monthSel").append("<option value='"+(i+1)+"'>"+month[i]+"</option>");
		 
		 
	 }
	//
	
	
}


function clearSearchResult() {
    $("#theList > tbody").html("");
    
    indexPage.isQuery = false;    
    indexPage.init();
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
    		alert(obj);
    		$("#Date").html("<b>" + obj.date + "</b>");
    		$("#FixTotal").html("<b>" + obj.fixTotal + "</b>");
    		$("#CellTotal").html("<b>" + obj.cellTotal + "</b>");
    		
	        /* var list = obj.userTelList;
	        for (var i = 0; i < list.length; i++) {
	        	var theNumber = list[i];
	            $("#telNumberSel").append("<option value='"+theNumber+"'>"+theNumber+"</option>");
	        } */
    	} else {
    		alert(obj.returnMessage);
    	}
    } else {
        alert("System error. Please contact administrator.");
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
			'Cost Center',
			'Fixed Phone Cost',
			'Cellphone Cost'
		];
		indexPage.init();
		//"Cost Center",  "Fixed Phone Cost", "Cellphone Cost"
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
/* 		$("#firstName").html("<b>" + obj.firstName + "</b>");
		$("#lastName").html("<b>" + obj.lastName + "</b>");
		$("#staffCode").html("<b>" + obj.staffCode + "</b>"); */
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
        - Cost Center Overview
    </div>
    
    <div id="mainContent" style="display:none;">
	    <div class="left">
	        <p><!--Cost Center:
	            <input id="targetUserId" />
	             <input type="button" id="userLoadBtn" value="Find" /> 
	        </p>
	        <p>Date:  <span id="Date"><b> </b></span></p>
	        <p>Fix Total:   <span id="FixTotal"><b> </b></span></p>
	        <p>Cell Total:  <span id="CellTotal"><b> </b></span></p>  -->
	    </div>
	    
	    <div class="right">
	        <p>
	            <label>
	             <!--Type: 
	                <select id="telNumberSel">
	                    <option value="China Unicom">telephone</option>
	                    <option value="China Mobile">mobile</option> 
	                </select> -->
	            </label>
	            <label>Year: 
	                <select id="yearSel">
	          <!--           <option value="-1">Select one ...</option>
	                    <option selected="selected" value="2014">2014</option>
	                    <option style="color:gray" value="2013">2013</option> -->
	                </select>
	            </label>
	            <label>Month:
	                <select id="monthSel">
<!-- 	                    <option value="-1">Select one ...</option>
	                    <option style="color:black" value="3">Mar</option>
	                    <option selected="selected" style="color:gray" value="4">Apr</option> -->
	                </select>
	            </label>
	            <span>
	                <input id="searchPar" value="Search" type="button">
	                <input id="exportPar" value="Export" type="button">
	                <input id="roleName" value="roleName" type="hidden">
	            </span>
	        </p>
	    </div>
	    
	    <div id="demo_jui" class="list">
	        <table id="theList" class="display">
	            <thead>
	                <tr>
	                    <th>Cost Center</th>
	                    <th>Fixed Phone Cost</th>
	                    <th>Cellphone Cost</th>
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

