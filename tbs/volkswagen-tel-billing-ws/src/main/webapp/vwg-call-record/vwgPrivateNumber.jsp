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

<style type="text/css">
body {
	font-size: 12px;
	font-family: "微软雅黑";
}
.container {
	border: 1px solid #999;
	height: auto;
	width: 1100px;
	margin: 5px auto;
	font-family: Verdana, Geneva, sans-serif;
	font-size: 12px;
	padding: 10px;
}
.container .title {
	text-align: center;
	font-size: 20px;
	font-weight: bold;
	border-bottom: 1px solid #999;
	color: #333;
}
.container .title img {
	margin: 2px 0 10px 0;
}
.container .sub_title {
	font-weight: bold;
	text-align: center;
	margin: 5px 0 10px 0;
}
.container .contentDiv {
	text-align: left;
	margin: 20px 20px;
}
.container .contentDiv .vColumn {
	display: inline-block; width =40%;
	text-align: left;
	margin: 0px;
}
.container .contentDiv .vSeparator {
	display: inline-block;
	width: 100px;
}
.container .left {
	display: inline-block;
	width: 250px;
	height: 60px;
	line-height: 140%;
}
.container .right {
	display: inline-block;
	width: 540px;
	height: 60px;
	position: relative;
	clear: right;
}
.container .right label {
	display: inline-block;
	width: auto;
	padding: 0 10px;
}
.container .list {
	width: 95%;
	margin: 10px 20px;
	clear: both;
	border-spacing: 0px;
	border: 1px solid #999;
	border-radius: 5px 5px 0 0;
}
.container .list thead tr {
	border: 1px solid #aaa;
	background: #eee;
}
.container .list thead tr td {
	padding: 10px 5px;
}
.container .list tbody tr {
	border: 1px solid #aaa;
}
.container .list tbody tr td {
	text-align: center;
	padding: 5px 0;
}
.container .list tfoot .sum {
	border-top: 1px #999 solid;
	border-bottom: 1px #999 solid;
	padding: 5px;
	text-align: center;
}
.container .bottom p {
	padding: 5px 10px;
	margin: 5px 0;
}
.container .bottom label {
	display: inline-block;
	width: 500px;
}
.container .bottom span button {
	padding: 2px 5px;
	width: 80px;
}
#sampleLink {color:#7A7A7A;text-decoration:underline;}
.container .note-area {text-align: left; margin: 0px 30px 0px 20px;}
.container .note-area p {margin: 0px 0px 5px;}
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
    $("#pnAddBtn").click(function() {
        addRowForPN();
    });
    
    $("#sampleLink").click(function() {
    	$("#sampleDiv").toggle('blind', null, 500 );
    });
    
    loadPrivateNumbers();
});
function addRowContent(targetContainerElementId, uniquePageElemId, pnValue, savedEntityId) {
    var content = "<tr id='tr-"+uniquePageElemId+"'>";
    
    content += "<td>";
    if (savedEntityId == 0) {
        content += "<input id='uniquePageElemId-"+uniquePageElemId+"' value='"+pnValue+"' size='30' style='height:23px;' />";
    } else {
        content += "<input id='uniquePageElemId-"+uniquePageElemId+"' value='"+pnValue+"' size='30' style='height:23px;' disabled />";
    }
    content += "<input id='savedEntityId-"+uniquePageElemId+"' type='hidden' value='"+savedEntityId+"' />";
    content += "</td>";
    content += "<td style='text-align:right;'>";
    if (savedEntityId == 0) {
        content += " ";
        content += "<input type='button' value='Save' id='sv-"+uniquePageElemId+"' onclick=\"javascript:savePrivateNumber('"+uniquePageElemId+"')\" class='commonButton60' />";
    }
    content += "&nbsp;&nbsp;&nbsp;";
    content += "<input type='button' value='Delete' id='rm-"+uniquePageElemId+"' onclick=\"javascript:removePrivateNumber('"+uniquePageElemId+"')\" class='commonButton60' style='margin-right:300px;' />";
    content += "</td>";
    
    content += "</tr>";
    
    $("#"+targetContainerElementId).append(content);
}
function addRowForPN() {
	var rowCount = $("#pnTable tbody tr").size();
	if (rowCount>=30) {
		alert("You already stored 30 private numbers. Please delete some of them and then try to add new one.");
		return;
	}
    var randomVal = Math.floor(Math.random()*999999+1);
    addRowContent('pnTable', randomVal, '', 0);
}
function savePrivateNumber(uniquePageElemId) {
    var privateNumber = $("#uniquePageElemId-" + uniquePageElemId).val();
    var trimmedPN = privateNumber.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
    if (trimmedPN==null || trimmedPN==undefined || trimmedPN=='' || trimmedPN.length<7 || trimmedPN.length>15) {
    	alert("Minimum length is 7 and maximum is 15. Please input again.");
    	return;
    }
    
    var config = {
        url : commons.webserversite + '/userPrivateNumberService/savePrivateNumber',
        type : 'POST',
        data : {
        	privateNumber : trimmedPN,
            uniquePageElemId : uniquePageElemId
        },
        success : 'savePrivateNumber_Success'
    };
    doAJax.doConfig(config);
}
function savePrivateNumber_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var uniquePageElemId = obj.uniquePageElemId;
        var entityId = obj.userPrivateEntityEntity.id;
        
        $("#savedEntityId-" + uniquePageElemId).val(entityId);
        $("#uniquePageElemId-" + uniquePageElemId).attr("disabled", "disabled");
        $("#sv-" + uniquePageElemId).hide();
        alert("Saved successfully.");
    } else {
        alert(obj.returnMessage);
    }
}
function removePrivateNumber(uniquePageElemId) {
    var savedEntityId = $("#savedEntityId-" + uniquePageElemId).val();
    if (savedEntityId > 0) {
        // - remove record on server.
        var config = {
            url : commons.webserversite + '/userPrivateNumberService/deletePrivateNumber',
            type : 'POST',
            data : {
                entityId : savedEntityId,
                uniquePageElemId : uniquePageElemId
            },
            success : 'removePrivateNumber_Success'
        };
        doAJax.doConfig(config);
    } else {
        $("#tr-"+uniquePageElemId).remove();
    }
}
function removePrivateNumber_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        alert("Removed successfully.");
        $("#tr-"+obj.uniquePageElemId).remove();
    } else {
        alert(obj.returnMessage);
    }
}
function loadPrivateNumbers() {
    var config = {
        url : commons.webserversite + '/userPrivateNumberService/getPrivateNumbers',
        type : 'GET',
        success : 'loadPrivateNumbers_Success'
    };
    doAJax.doConfig(config);
}
function loadPrivateNumbers_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var upnList = obj.userPrivateNumberList;
        for (var i in upnList) {
            addRowContent('pnTable', upnList[i].id, upnList[i].privateNumber, upnList[i].id);
        }
    } else {
        alert(obj.returnMessage);
    }
}
</script>

</head>

<body>
<%@include file="../vwg-common/header.jsp" %>

<div class="container">
    <div class="title">
        <img src="../images/logo_01.jpg" border="0" />
    </div>
    <div class="sub_title">
        Telephone Billing System<br />
        - private number setting
    </div>
    
    <div class="note-area">
        <p>
			Note: This feature aims to help employee manage frequently called telephone number as 'private number'.
			The stored private numbers are always matched starting from the end.
			Therefore you only need to register a number once ex. 13912345678 no matter if the invoice shows +8613912345678 or 13912345678.
			Therefore you can normally omit country codes especially for Chinese numbers.
	    </p>
	    <p>
            The minimum length you need to store is 7 and the maximum length is 15.
	    </p>
	    <p><a id="sampleLink" href="#">Show sample</a></p>
	    <div id="sampleDiv" style="display:none;">
		    <p>
		        For example, if the private number is configured as: 2345678,
		        then the following numbers will be matched:
		    </p>
		    <p style="display: inline-block; width: 140px; text-align: right;">
		        2345678<br>
	            862345678<br>
	            00862345678<br>
	            +86102345678<br>
	            ...
	        </p>
        </div>
    </div>

    <div style="margin:10px 0 0 20px;">
        <input id="pnAddBtn" class="commonButton140"
               type="button" value="Add private number" />
    </div>
    
    <table id="pnTable" class="list">
        <thead>
            <tr align="center">
                <td width="30%">Private number</td>
                <td>Save or Delete</td>
            </tr>
        </thead>
    </table>
</div>


</body>
</html>