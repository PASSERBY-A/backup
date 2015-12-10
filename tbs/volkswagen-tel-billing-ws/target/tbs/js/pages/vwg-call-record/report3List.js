
var indexPage = {
	systemData : null,
	oTable : null,
	resultData : null,
	queryListJson : null,
	isUpdate : false,
	systemId : null,
	isQuery : false,
	titleTextArray : [
		"Staff Code", "Employee Name", "Fixed Phone Cost", "Cellphone Cost"
    ],
	ii : 0,
	init : function() {
		var that = this;
		that.createDataTable();
		// add event
		that.initEvent();
	},
	initDataSuccess : function(data) {
 		this.createDataTable();
	},
	initData : function(e) {
		var that = this;
		var config = {
			url : commons.webserversite + '/reportService/costCenterMonthlyReport',
			type : 'POST',
			success : 'indexPage.initDataSuccess'
		};
		doAJax.doConfig(config);
	},
	// create dataTable
	createDataTable : function() {
		var that = this;
		that.ii = 0;
		if (that.oTable == null) {
		} else {
			that.oTable.fnClearTable(0);
		}
		// define the columns in data-table
		datatableOnServer.aoColumns =[
			{ "sTitle": indexPage.titleTextArray[0], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[1], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[2], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[3], "sClass": "center" }
		];

		// default sorting
		datatableOnServer.bSort = false;
		datatableOnServer.aaSorting = [[1, 'asc']];
		// returned data list
		datatableOnServer.sAjaxDataProp = 'resultList';
		datatableOnServer.fnServerData = that.retrieveData; // processing method for getting data  
		datatableOnServer.sAjaxSource = commons.webserversite + '/reportService/costCenterMonthlyReport'; 
		// define id position
		commons.xhId = 0;
		// generate data-table object
		that.oTable = $('#theList').dataTable(datatableOnServer);
	},
	retrieveData : function( sSource, aoData, fnCallback ) {
		var that = this;
		var appset = "";
		for ( var i = 0; i < aoData.length; i++) {
			appset = appset + '{"key":"'+aoData[i].name + '","value":"' + aoData[i].value + '"},';
		}
		
		// set query parameters
		if (indexPage.isQuery) {
			var targetUserId = $("#targetUserId").val();
			var year = $("#yearSel").val();
			var month = $("#monthSel").val();

			appset = appset
						+ '{"key":"targetUserId","value":"' + targetUserId + '"},'
						+ '{"key":"year","value":"' + year + '"},'
						+ '{"key":"month","value":"' + month + '"},';
			var apps = appset.substring(0, appset.length-1);
			//		var apps = {};
		
			var appData = {
				isQuery: indexPage.isQuery,
				voList: apps
			};
			var config = {
				url : sSource,
				type : 'POST',
				data : appData,
				isDT : true,           // for datatable only
				callFun : fnCallback,  // for datatable only
				success : 'indexPage.respSuccess'
			};
			doAJax.doConfig(config);
		}
	},
	respSuccess : function(resp) {
		if (resp!=null && resp.returnCode=="SUCCESS") {
    		$("#Date").html("<b>" + resp.date + "</b>");
    		$("#FixTotal").html("<b>" + resp.fixTotal + "</b>");
    		$("#CellTotal").html("<b>" + resp.cellTotal + "</b>");
			this.setResultListToLocal(resp);
			 $("#searchPar").removeAttr("disabled");//
		} else {
			alert(resp.returnMessage);
			 $("#searchPar").removeAttr("disabled");//
		}
	},
	// set backend data to local array for editing.
	setResultListToLocal : function(resp) {
		var that = this;
		var resultList = resp.resultList;
		that.queryListJson = "[";
		var tr_json = "";

		for (var i = 0; i < resultList.length; i++) {
			var apps = resultList[i];
			tr_json = "{'app_id':'"+that.isNull(apps.app_id)+"','dataValue':"+$.toJSONString(apps)+"}";
			if ( i < resultList.length-1 ) {
				tr_json = tr_json + ",";
			}
			that.queryListJson = that.queryListJson + tr_json;
		}
		that.queryListJson = that.queryListJson + "]";
	},
	
	// adding event;
	initEvent : function(e){
		var that = this;
		 /* double-clicking on tr of datatable */
		$('#dataTableList tbody tr').live('dblclick', function () {
			var apps = that.oTable.fnGetData( this );

		    parent.appId = apps.app_id;
		    parent.appsData = apps;
			window.location.href="appDetail.jsp";
		} );
		// search event
		$("#searchPar").click(function(e) {
			var targetUserId = $("#targetUserId").val();
			var telephoneNumber = $("#telNumberSel").val();
			var year = $("#yearSel").val();
			var month = $("#monthSel").val();
			if (targetUserId=='' || telephoneNumber=='' || year=='-1' || month=='-1') {
				alert("Please specify valid CostCeneter.");
				return;
			}
			$("#searchPar").attr({"disabled":"disabled"});
			indexPage.isQuery = true;				
			indexPage.createDataTable();
		});
	},
	isNull : function(str) {
		if (str == null) {
			str  = "";
		}
		return str;
	},
	adjustTrFontColor : function(rowId, targetColor) {
		$("#"+rowId).closest("tr").css('color', targetColor);
	} 
};

$(document).ready(function(){
	commons.webserversite = "/volkswagen-tel-billing-ws/rest";
	var thats = this;
	indexPage.isQuery = false;
});

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
