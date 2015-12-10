//filter calendar icon onclick
function showCalendar(obj) {
	$("#" + obj).datepicker("show");
}

// keep plan list data
var data;
var len;
function keepdata(obj) {
 	data = obj;
 	len = data.length;
}

//filter
//filter_approvalstatus
function filter_approvalstatusOnchange(data) {
	if(data == null || data.length < 1){
		return data;
	}
	var approvalstatus = $('#filter_approvalstatus select').val();
	if(approvalstatus === 'all'){
		return data;
	}
	
	var len = data.length;
	var planList = [];
	for ( var i = 0; i < len; i++) {
		if(approvalstatus != "" && approvalstatus.length > 0){
			if(approvalStatus(data[i]['level1ApproveStatus'],data[i]['level2ApproveStatus']) ===approvalstatus){
				planList.push(data[i]);
			}
		}
	}
	return planList;
}
function filter_DateOnchange(data) {
	if(data == null || data.length < 1){
		return data;
	}
	var startorend = $('#filter_startorend select').val();
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	
	try{
		if(startDate != "" && startDate.length > 0){
			startDate = $.datepicker.parseDate('yy-mm-dd', startDate);
		}else {
			startDate = $.datepicker.parseDate('yy-mm-dd', '1900-01-01');
		}
		
		if(endDate != "" && endDate.length > 0){
			endDate = $.datepicker.parseDate('yy-mm-dd', endDate);
		}else {
			endDate = $.datepicker.parseDate('yy-mm-dd', '2100-01-01');
		}
		
	}catch(err){
		alert('请输入正确的日期。');
	}
	
	var len = data.length;
	var planList = [];
	for ( var i = 0; i < len; i++) {
		var date = parseStringToJSDate(data[i][startorend+'Date']);
		if(date >= startDate && date <= endDate){
			planList.push(data[i]);
		}
	}
	return planList;
}
function filterPlanList() {
	var planList = filter_approvalstatusOnchange(data);
	planList = filter_DateOnchange(planList);
	
	//show planList
	appendToPlanTable(planList);
}

//parse string to javascript date, format must be yyyy-mm-dd
function parseStringToJSDate(str) {
	var date = new Date();
	try{
		if(str != "" && str.length > 0){
			date = $.datepicker.parseDate('yy-mm-dd', str);
		}else {
			date = $.datepicker.parseDate('yy-mm-dd', '2100-01-01');
		}
	}catch(err){
		
	}
	return date;
}
