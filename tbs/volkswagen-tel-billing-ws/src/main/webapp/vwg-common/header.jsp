<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
function tbsLogout2() {
	var url = document.URL;
	var splitArray = url.split('/');
	var toAddress = splitArray[0] + "//" + splitArray[2] + "/pkmslogout";
	
	window.location = toAddress;
}
</script>

<style>
#navDiv .item-right-border {text-align: right; border-right: 2px solid #CCCCCC; padding: 0px 5px;}
#navDiv .item-no-border {text-align:right; border:none; padding: 0px 5px;}
#navDiv a {
    display:inline-block;
    text-decoration:none;
    color:black;
}
</style>

<div style="margin:10px auto; text-align:right; width:1120px;">
    <div id="navDiv">
        <span class="item-right-border">
            <a href="../vwg-call-record/vwgCallRecordList.jsp">Home</a>
        </span>
        <span class="item-right-border">
            <a href="../vwg-call-record/vwgPrivateNumber.jsp">Private number setting</a>
        </span>
        <span class="item-no-border">
            <a href="javascript:tbsLogout2();">Logout</a>
        </span>
    </div>
</div>