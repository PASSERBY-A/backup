<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Tel billing system - Login</title>
<link type="text/css" rel="stylesheet" href="../css/reset-min.css">
<!-- <link type="text/css" rel="stylesheet" href="../css/public.css"> -->

<script src="./js/jquery-1.7.2-min.js" type="text/javascript"></script>
<script src="./js/jquery-ui.min.js" type="text/javascript"></script>

<script src="./js/library/jquery.json.js" type="text/javascript"></script>

<script src="./js/common/ucloud_commons.js" type="text/javascript"></script>
<script src="./js/common/ucloud_commons_ajax.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	commons.webserversite = "/volkswagen-tel-billing-ws/rest";
	
    var isIE = !!window.ActiveXObject;
    if (isIE) {
      getUid_And_LoadUserInfo();
    } else {
        waitAppletLoad(function(){getUid_And_LoadUserInfo();});
    }
    
    // - for mockup
    $("#sesSubmitBtn").click(function(){
        var config = {
            url : commons.webserversite + '/sessionMockupService/mockup',
            type : 'POST',
            data : {
            	sessionUid : $("#sessionUid").val()
            },
            success : 'sessionMock_Success'
        };
        doAJax.doConfig(config);
    });
});
function sessionMock_Success(obj) {
    alert("returnCode: " + obj.returnCode
            + ", returnMessage: " + obj.returnMessage);
}
function getUid_And_LoadUserInfo() {
	var uid = getUidByApplet();
	$("#usrId").val(uid);
	
	loadUserInfo(uid);
}
function getUidByApplet() {
	var uid = document.myApplet.getUid();
	return uid;
}
function loadUserInfo(uid) {
    $.ajax({
        type : "GET",
//        async : false,
        url : 'http://10.120.4.221:8060/ad-tim-service-ws/rest/userInfoService/getUserInfoByUserIdViaJsonp?uid='+uid+'&callback=?',
        dataType : "jsonp",
    }).done(function (data) {
        if (data.returnCode == 'SUCCESS') {
            $("#greetingNameSpan").html("Hello Mr " + data.surname + " !");
        } else {
        	alert(data.returnMessage);
        }
    }).fail(function (XHR, status, error) {
        alert(error);
    });
}
function getUidByActiveX() { // for IE only
    if (window.ActiveXObject) {
        var wshNetwork = new ActiveXObject("WScript.Network");
        var username=wshNetwork.UserName;
        var domain=wshNetwork.UserDomain;
        return username;
    } else {
    	alert("window.ActiveXObject is not supported.");
    }
    return "";
}
function waitAppletLoad(callback){
    if (document.myApplet.isActive != '' && document.myApplet.isActive!=undefined ){
        callback();
    } else {
        setTimeout(function(){waitAppletLoad(callback);}, 800);
    }
}
</script>

</head>

<body>
    <div align="center">
        Login page.
    </div>
    
    <div align="center">
        <span id="greetingNameSpan"></span>
    </div>
    <p><br /><br /></p>
    <div align="center">
        User ID: <input id="usrId" /> <br />
        Password: <input id="psw" /> <br />
        <input type="submit" value="Submit" />
    </div>
    
    <div>
        <applet name="myApplet" width="1" height="1"
                code="TestApplet"
                archive="test.jar"
        >
        </applet>
    </div>
    
    <div align="center">
        <p><br /><br /></p><p><br /><br /></p>
        <span>Mock session uid here.</span><br />
        <input id="sessionUid" />
        <input id="sesSubmitBtn" type="button" value="Submit" />
    </div>
    
</body>
</html>

