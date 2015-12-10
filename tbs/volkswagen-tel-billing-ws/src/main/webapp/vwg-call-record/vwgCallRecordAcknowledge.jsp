<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.volkswagen.tel.billing.common.GenericConfig" %>
<%
    String[] months = {"", "January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String telNumber = request.getParameter("tel");
    int year = Integer.parseInt(request.getParameter("year"));
    int month = Integer.parseInt(request.getParameter("month"));
    String monthDisplay = months[month];
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Telephone billing system - Summary page</title>
<link type="text/css" rel="stylesheet" href="../css/reset-min.css">
<!-- <link type="text/css" rel="stylesheet" href="../css/public.css"> -->

<style type="text/css">
body {
    font-size: 12px;
    font-family: "微软雅黑";
}
.vbold {
    font-weight: bold;
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
/* .container .title p{margin:5px 0;} */
.container .title img {
    margin: 2px 0 10px 0;
}
.container .sub_title {font-weight:bold;text-align:center;margin:5px 0 10px 0;}
.container .left {
    display: inline-block;
    float: left;
    width: 280px;
    margin-left: 24px;
    /*height: 60px;*/
    /*line-height: 140%;*/
    /*position: relative;*/
    /*right:80px;*/
}
.container .right {
    display: inline-block;
    width: 740px;
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
.container .list tbody tr{
    border-left: 1px solid #aaa;
    border-right: 1px solid #aaa;
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
    font-weight:bold;
    background: #eee;
}
.container .signature {
    text-align: right;
    padding: 0 20px;
    margin: 20px 20px 0 0;
}
.container .bottom p {
    padding: 5px 10px;
    margin: 5px 15px;
}
.container .bottom label {
    display: inline-block;
    width: 500px;
}
.container .bottom .mLabel {display:inline-block; position:relative; right:0px; width:450px;}
.container .bottom span button {padding:2px 5px;width:110px;}
.backButton {
    padding: 2px 5px;
    width: 130px;
}

dt {
    position: absolute;
    z-index: 1;
    width: 120px;
    height: 30px;
    padding: 0 5px 0 5px;
    border: 1px solid #EFEFEF;
    background-position: -116px -24px;
    background-color: #F7F7F7;
    text-align: center;
    line-height: 27px;
    cursor: pointer;
}
dl {
    display: block;
    -webkit-margin-before: 1em;
    -webkit-margin-after: 1em;
    -webkit-margin-start: 0px;
    -webkit-margin-end: 0px;
}
.acBtn110{padding:2px 5px; width:110px;}
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
    var theDate = new Date();
    $("#date").append(theDate.Format("dd/MM/yyyy"));

	commons.webserversite = "/volkswagen-tel-billing-ws/rest"; //TODO local test no v-blabla only /rest

    $("#saveBtn").click(function() {
        performSave();
    });
    $("#printBtn").click(function() {
        performPrint();
    });

    $("#savePDFBtn").click(function() {
        generatePDF();
    });
    $("#sapSubmitBtn").click(function() {
        submitToSAP();
    });

    loadUserInfo();
    loadBillInfo();
    loadSummary();
    loadSum();
});
function loadSum() {
    var telephoneNumber = $("#telNumberSel").val();
    var year = $("#yearSel").val();
    var month = $("#monthSel").val();
    if (telephoneNumber=='' || year=='-1' || month=='-1') {
        alert("Please specify valid telephone number, year and month.");
        return;
    }
    var appset = '{"key":"telephoneNumber","value":"' + telephoneNumber + '"},'
            + '{"key":"year","value":"' + year + '"},'
            + '{"key":"month","value":"' + month + '"}';
    var appData = {
        isQuery: indexPage.isQuery,
        voList: appset
    };
    var pkg4conf = {
        url: commons.webserversite + '/billCallRecordService/getBillCallSumByTelAndMonth',
        isDT: true,
        data: appData,
        callFun: function(data) {
            $('#monthPkg').text(data.monthPkg);
            $('#dataBoPkg').text(data.dataBoPkg);
            $('#smsBoPkg').text(data.smsBoPkg);
            $('#roamingBoPkg').text(data.roamingBoPkg);
            $('#monthPkgParam').val(data.monthPkg);
            $('#dataBoPkgParam').val(data.dataBoPkg);
            $('#smsBoPkgParam').val(data.smsBoPkg);
            $('#roamingBoPkgParam').val(data.roamingBoPkg);
        }

    };
    doAJax.doConfig(pkg4conf);
}
function submitToSAP() {
    var msg = "This will send the bill summary of this month to SAP system.";
    msg += " And the cost will then be charged to your salary of this month.";
    msg += "\nDo you want to continue?";
    if (!confirm(msg)) {
        return;
    }

    var config = {
        url : commons.webserversite + '/billSubmitHistoryService/submitToSap',
        type : 'POST',
        data : {
            telephoneNumber : '<%=telNumber%>',
            year : '<%=year%>',
            month : '<%=month%>'
        },
        success : 'submitToSAP_Success'
    };
    doAJax.doConfig(config);
}
function submitToSAP_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        alert(obj.returnMessage);
        $("#sapSubmitBtn").attr('disabled', 'disabled');
    } else {
        alert(obj.returnMessage);
    }
}
function loadUserInfo() {
    var config = {
        url : commons.webserversite + '/userInfoService/getUserInfoBySessionUid',
        type : 'GET',
        success : 'loadUserInfo_Success'
    };
    doAJax.doConfig(config);
}
function loadUserInfo_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        $("#firstName").html("<b>"+obj.firstName+"</b>");
        $("#lastName").html("<b>"+obj.lastName+"</b>");
        $("#staffCode").html("<b>"+obj.staffCode+"</b>");
    } else {
        alert(obj.returnMessage);
    }
}
function loadBillInfo() {
    var config = {
        url : commons.webserversite + '/telephoneBillService/getTelephoneBillInfo',
        type : 'POST',
        data : {
            telephoneNumber : '<%=telNumber%>',
            year : '<%=year%>',
            month : '<%=month%>'
        },
        success : 'loadBillInfo_Success'
    };
    doAJax.doConfig(config);
}
function loadBillInfo_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        if (obj.telephoneBillEntity.status == 'ACTIVE') {
            $("#saveParagraph").show();
        } else if (obj.telephoneBillEntity.status == 'SAVED') {
            displaySavingTime();
            showSignatureOrSapButton();
        } else if (obj.telephoneBillEntity.status == 'SENT') {
            displaySavingTime();
            showSignatureOrSapButton();
            $("#sapSubmitBtn").attr('disabled', 'disabled');
        }
    } else {
        alert(obj.returnMessage);
    }
}
function showSignatureOrSapButton() {
    var sapSubmitConfigEnabled = '<%=GenericConfig.enableSapSubmit?"YES":"NO"%>';
    if (sapSubmitConfigEnabled == "YES") {
        $("#sapSubmitParagraph").show();
        $("#signatureParagraph").hide();
        $("#printParagraph").hide();
    } else {
        $("#sapSubmitParagraph").hide();
        $("#signatureParagraph").show();
        $("#printParagraph").show();
    }
}
function loadSummary() {
    var config = {
        url : commons.webserversite + '/telephoneBillStatisticService/getCommunicationTypeStatistic',
        type : 'POST',
        data : {
            telephoneNumber : '<%=telNumber%>',
            year : '<%=year%>',
            month : '<%=month%>'
        },
        success : 'loadSummary_Success'
    };
    doAJax.doConfig(config);
}
function loadSummary_Success(obj) {
    if (obj!=null && obj.returnCode=="SUCCESS") {
        var resultList = obj.resultList;
        var durationSum = 0;
        var costSum = 0.000;
        var isNull = false;
        if(resultList.length==0){
            resultList = [["0","00:00:00","0"]];
            isNull = true;
        }

        $("#summary").append("<tbody>");
        $.each(resultList,function(n,value) {

            var secondsDuration = parseInt(value[1].substr(0,2)) * 60 * 60 + parseInt(value[1].substr(3, 2)) * 60 + parseInt(value[1].substr(6,2));
            durationSum = durationSum + secondsDuration;
            costSum = costSum + value[2];
            if(isNull){
                $("#summary").append("<tr><td style='text-align:center'>0</td><td>"+value[1]+"</td><td>"+value[2]+"</td></tr>");
            }else{
                $("#summary").append("<tr><td style='text-align:right;padding-right:222px'>"+value[0]+ "</td><td>"+value[1]+"</td><td>"+value[2]+"</td></tr>");
            }
        });

        $("#summary").append("</tbody>");
        if(!isNull){
            costSum = costSum.toFixed(2);
        }else{
            costSum =0;
        }

        var sec_num = parseInt(durationSum).toString();

        var duration = sec_num.toHHMMSS();
        $("#summary").append("<tfoot><tr><td class='sum'>SUM</td><td class='sum'>"+duration+ "</td><td class='sum'>"+costSum+" RMB</td></tr></tfoot>");
    } else {
        alert(obj.returnMessage);
    }
}
String.prototype.toHHMMSS = function () {
    var sec_num = parseInt(this, 10);
    var hours   = Math.floor(sec_num / 3600);
    var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
    var seconds = sec_num - (hours * 3600) - (minutes * 60);
    if (hours   < 10) {hours   = "0"+hours;}
    if (minutes < 10) {minutes = "0"+minutes;}
    if (seconds < 10) {seconds = "0"+seconds;}
    var time    = hours+':'+minutes+':'+seconds;

    return time;
}

function performPrint() {
    $("#printParagraph").hide();
    window.print(window.document.body.innerHTML);
    $("#printParagraph").show();
    $("#savdPDFParagraph").show();
}
function performSave() {
    var msg = "Once the reimbursement report is saved, you won't have any chance to modify it.";
    msg += " Please make sure you have checked all the records already.";
    msg += " Are you willing to save it now?";
    if (confirm(msg)) {
        saveByAjax();
    }
}
function saveByAjax() {
    var config = {
        url : commons.webserversite + '/telephoneBillService/inactivateTelephoneBillStatus',
        type : 'POST',
        data : {
            telephoneNumber : '<%=telNumber%>',
            year : <%=year%>,
            month : <%=month%>
        },
        success : 'saveByAjax_Success'
    };
    doAJax.doConfig(config);
}
function saveByAjax_Success(obj) {
    if (obj != null) {
        if (obj.returnCode=="SUCCESS") {
            displaySavingTime();
            showSignatureOrSapButton();
        } else {
            alert(obj.returnMessage);
        }
    } else {
        alert("System error. Please contact administrator.");
    }
}

function generatePDF() {

    var config = {
        url : commons.webserversite + '/telephoneBillStatisticService/exportCallListAsPDF',
        type : 'POST',
        data : {
            telephoneNumber : '<%=telNumber%>',
            year : <%=year%>,
            month : <%=month%>,
            monthPkg : $('#monthPkgParam').val(),
            dataBoPkg : $('#dataBoPkgParam').val(),
            smsBoPkg : $('#smsBoPkgParam').val(),
            roamingBoPkg : $('#roamingBoPkgParam').val()
        },
        success : 'savepAsPDF'
    };
    doAJax.doConfig(config);

}

function savepAsPDF(obj){

    if (obj!=null && obj.returnCode=="SUCCESS") {
        var filename = obj.pdfFile;

        var url = '<%=request.getContextPath()%>' + '/tmp/'+ filename;

        newwindow=window.open(url,'name','height=500,width=800,scrollbars=yes,resizable=yes');
        if (window.focus) {
            newwindow.focus();
        }

    }
}

function displaySavingTime() {
    $("#savdPDFParagraph").hide();
    $("#saveParagraph").hide();

    $("#printParagraph").show();
    $("#savdPDFParagraph").show();
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};


</script>

</head>

<body>
<%@include file="../vwg-common/header.jsp" %>

<div class="container">
    <div class="title">
        <!-- <p>VOLKSWAGEN</p>
    <p style="font-size:8px;">GROUP CHINA</p> -->
        <img src="../images/logo_01.jpg" border="0" />
    </div>
    <div class="sub_title">Telephone Billing System</div>

    <div class="left">
        <p>
            First Name: <span id="firstName"></span>
        </p>
        <p>
            Last Name: <span id="lastName"></span>
        </p>
        <p>
            Staff code: <span id="staffCode"></span>
        </p>
    </div>

    <div class="right">

        <p id="basicInfo" style="">
            Telephone Number: <b><%=telNumber%></b>
            <label>Year: <b><%=year%></b></label>
            <label>Month: <b><%=monthDisplay%></b></label>
            <label>Date:<b id="date"></b></label>
            <input type="hidden" id="telNumberSel" value="<%=telNumber%>" />
            <input type="hidden" id="yearSel" value="<%=year%>" />
            <input type="hidden" id="monthSel" value="<%=month%>" />
            <input type="hidden" id="monthPkgParam" name="monthPkg" value="" />
            <input type="hidden" id="dataBoPkgParam" name="dataBoPkg" value="" />
            <input type="hidden" id="smsBoPkgParam" name="smsBoPkg" value="" />
            <input type="hidden" id="roamingBoPkgParam" name="roamingBoPkg" value="" />
        </p>
    </div>

    <div style="margin-left: 24px;">
        Monthly package: <span id="monthPkg" class="vbold">0</span>
        <br/>
        Cost beyond monthly package:
        <span style="padding-left: 9px">Data-Traffic:</span> <span id="dataBoPkg" class="vbold">0</span>
        <span style="padding-left: 9px">SMS/MMS:</span> <span id="smsBoPkg" class="vbold">0</span>
        <span style="padding-left: 9px">Calling/Roaming:</span> <span id="roamingBoPkg" class="vbold">0</span>
    </div>

    <table id="summary" class="list">
        <thead>
        <tr align="center">
            <td style="width:50%;">Private Called Number(s)</td>
            <td>Duration</td>
            <td>Cost in RMB</td>
        </tr>
        </thead>
    </table>

    <div style="padding: 10px 0;margin-left: 20px;">I hereby certify that I have
        marked and tested my private conversations regarding their accuracy.
        The costs for my private conversations can be deducted from my
        salary.</div>
    <div class="signature">
        <p id="signatureParagraph" style="display:none;">Signature:________________</p>
        <p id="sapSubmitParagraph" style="display:none;">
            Click submit to send the summary.
            <button id="sapSubmitBtn" class="acBtn110">Submit</button>
        </p>
    </div>
    <div style="border-bottom: 1px solid #999; height: 20px;"></div>
    <div class="bottom" id="buttonDiv">
        <p id="saveParagraph" style="display:none;">
            <span class="mLabel">Please ensure that all indications are correct before saving.</span>
				<span>
				    <button id="saveBtn" class="acBtn110">Save</button>
				</span>
        </p>
        <p id="printParagraph" style="display:none;">
        <span class="mLabel">Please sign the printouts and hand them over to HR.</span>
        <span>
        <button id="printBtn" class="acBtn110">Print</button>
        </span>
        </p>
        <p id="savdPDFParagraph" style="display:none;">
            <span class="mLabel">You can also save your call record list as PDF file.</span>
				<span>
				    <button id="savePDFBtn" class="acBtn110">Export to PDF</button>
				</span>
        </p>
    </div>
</div>

</body>
</html>