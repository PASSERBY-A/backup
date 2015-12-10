function formatCurrency(nStr) {
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}

function unformatCurrency(nStr) {
	var str = nStr;
	//str = str.replaceAll(',','');
	str = str.replace(new RegExp(",","g"),"");
	return str;
}

//function panel

function  gotoProductManage(){
	$(location).attr('href','ProductManage.jsp');
}

function  gotoPlanManage(){
	$(location).attr('href','PlanManage.jsp');
}

function gotoPlanApprovalList(level){
	$(location).attr('href','PlanApprovalList.jsp?level='+level);
}
function setHighLightFunction(functionname) {
	//alert("functionname:"+functionname);
	if(functionname==="productManage"){
		$('#productManageBtn').attr('class','function_enable');
		$('#planManageBtn').attr('class','function_disable');
		$('#planApproveBtn').attr('class','function_disable');
	}else if(functionname==="planManage"){
		$('#productManageBtn').attr('class','function_disable');
		$('#planManageBtn').attr('class','function_enable');
		$('#planApproveBtn').attr('class','function_disable');
	}else if(functionname==="planApprove"){
		$('#productManageBtn').attr('class','function_disable');
		$('#planManageBtn').attr('class','function_disable');
		$('#planApproveBtn').attr('class','function_enable');
	}
	
}