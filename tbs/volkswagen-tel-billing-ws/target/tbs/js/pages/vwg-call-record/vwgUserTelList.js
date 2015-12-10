
var indexPage = {
	systemData : null,
	oTable : null,
	resultData : null,
	queryListJson : null,
	isUpdate : false,
	systemId : null,
	isQuery : false,
	titleTextArray : [
		"User Id", "Telephone Number", "Status", "Valid Period", "Show Bill Status"
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
			url : commons.webserversite + '/userTelephoneService/getListByUserId',
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
//				that.oTable.fnDraw(0);
		}
		// define the columns in data-table
		datatableOnServer.aoColumns =[
			{ "sTitle": indexPage.titleTextArray[0], "sClass" : "center" },
			{ "sTitle": indexPage.titleTextArray[1], "sClass" : "center" },
			{ "sTitle": indexPage.titleTextArray[2], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[3], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[4], "sClass": "center",
			  "fnRender": function (obj) {
			     var content = "<input type='button' value='Show' class='commonButton60'";
			     content += " onclick=\"indexPage.showStatusHistory('"+obj.aData[0]+"', '"+obj.aData[1]+"');\" />";
			     return content;
			  }
			}
		];

		// default sorting
		datatableOnServer.bSort = false;
		datatableOnServer.aaSorting = [[1, 'asc']];
		// returned data list
		datatableOnServer.sAjaxDataProp = 'resultList';
		datatableOnServer.fnServerData = that.retrieveData; // processing method for getting data  
		datatableOnServer.sAjaxSource = commons.webserversite + '/userTelephoneService/getListByUserId';
		datatableOnServer.bStateSave = false;
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
			var userId = $("#userId").val();
//			userId = $.trim(userId); 

			appset = appset + '{"key":"userId","value":"' + userId + '"},';
			var apps = appset.substring(0, appset.length-1);
			// var apps = {};
		
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
			this.setResultListToLocal(resp);			
		} else {
			alert(resp.returnMessage);
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
//			alert("apps=" + apps);
			tr_json = "{'app_id':'"+that.isNull(apps.app_id)+"','dataValue':"+$.toJSONString(apps)+"}";
			if ( i < resultList.length-1 ) {
				tr_json = tr_json + ",";
			}
			that.queryListJson = that.queryListJson + tr_json;
		}
		that.queryListJson = that.queryListJson + "]";
	},
	// event for deleting record
	systemsDel : function() {
		var that = this;
		var sData = $("#dataTableList :input").serialize();
//        	alert( "delete data: \n\n"+sData );
    	
    	if (sData == null || sData == "") {
    		alert( "Please select the record to delete.");
    	} else {
//        		var config = {
//					url : commons.webserversite+ '/rest/apps/systems/delete/'+sData,
//					type : 'DELETE',
//					success : 'indexPage.systemsDelSuccess'
//				};
//				doAJax.doConfig(config);
    	}
	},
	showStatusHistory : function(userId, telNumber) {
        var config = {
            url : commons.webserversite + '/telephoneBillService/getValidTelBillListForAdmin',
            type : 'POST',
            data : {
                targetUserId : userId,
                telephoneNumber : telNumber
            },
            success : 'indexPage.showStatusHistory_Success'
        };
        doAJax.doConfig(config);
	},
	showStatusHistory_Success : function(obj) {
	   $("#dialogCoreContent").html("");
	   
	   if (obj!=null && obj.returnCode=="SUCCESS") {
	       var targetUserId = obj.targetUserId;
	       var telephoneNumber = obj.telephoneNumber;
           var billList = obj.billList;
           $.each(billList, function(index, content){
               var bYear = content.year;
               var bMonth = content.month;
               var bStatus = content.status;
//               alert(bMonth + "/"+bYear+": "+bStatus);

               var cnt = "<div style='text-align: left; border-bottom: 1px solid #000000; margin-bottom: 5px;'>";
               cnt += "<span style='width: 130px; display: inline-block; text-align: right; margin-right: 100px;'>" + bMonth + "/" + bYear + "</span>";
               cnt += "<span>" + bStatus + "</span>";
               cnt += "</div>";
               $("#dialogCoreContent").append(cnt);
           });

           var message = "<p>The following list shows the status of the bills during the owning period of the user.</p>";
           message += "<p><span style='width: 160px; display: inline-block;'>User ID: <u>"+targetUserId+"</u></span>";
           message += "<span style='width: 260px; display: inline-block;'>Telephone number: <u>"+telephoneNumber+"</u></span></p>";
           $("#confirmMsg").html(message);

           mmDialog.dialog("open");
	   } else {
	       alert(obj.returnMessage);
	   }
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
			var userId = $("#userId").val();
			
			indexPage.isQuery = true;				
			indexPage.createDataTable();
		});
	},
	isNull : function(str) {
		if (str == null) {
			str  = "";
		}
		return str;
	}
};

$(document).ready(function(){
	commons.webserversite = "/volkswagen-tel-billing-ws/rest";
	var thats = this;
	indexPage.isQuery = false;
	
	indexPage.init();
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
