
var indexPage = {
	systemData : null,
	oTable : null,
	resultData : null,
	queryListJson : null,
	isUpdate : false,
	systemId : null,
	isQuery : false,
	titleTextArray : [
		"Private", "Date", "Starting Time", "Duration",
		"Called Number", "Type", "Location", "Amount"
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
			url : commons.webserversite + '/billCallRecordService/getBillCallRecordsByTelAndMonth',
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
			{
				"sTitle" : indexPage.titleTextArray[0],
				"sClass" : "center",
				"fnRender" : function(obj) {
					var dateOfCall = obj.aData[0];
					if (dateOfCall == null || dateOfCall == undefined) {
						return "";
					} else {
						var theDate = new Date(dateOfCall.year, dateOfCall.month,
								               dateOfCall.date);
						return theDate.Format("dd/MM/yyyy");
					}
				}
			},
			{
				"sTitle" : indexPage.titleTextArray[1],
				"sClass" : "center",
				"fnRender" : function(obj) {
					var startingTime = obj.aData[1];
					if (startingTime== null || startingTime==undefined) {
						return "";
					} else {
						var theDate = new Date(startingTime.year,
								startingTime.month, startingTime.date,
								startingTime.hours, startingTime.minutes,
								startingTime.seconds);
						return theDate.Format("hh:mm:ss");
					}
				}
			},
			{ "sTitle": indexPage.titleTextArray[2], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[3], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[4], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[5], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[6], "sClass": "center" },
			{ "sTitle": indexPage.titleTextArray[7], "sClass": "center",
				"fnRender": function (obj) {
					var calledNumber = obj.aData[3];
					var isPrivate = obj.aData[7];
					var isEditable = obj.aData[8];
					var callRecordId = obj.aData[9];
					
					var content = "";
					content += "<input type='checkbox' id='prv-"+callRecordId+"' value='"+callRecordId+"'";
					content += (isPrivate=="1")?" checked":"";
					if (isEditable == true) {
						content += " onclick='indexPage.processPrivate(this);'";
					} else {
						content += " disabled='disabled'";
					}
					content += " />";
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
		datatableOnServer.sAjaxSource = commons.webserversite + '/billCallRecordService/getBillCallRecordsByTelAndMonth'; 
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
			var telephoneNumber = $("#telNumberSel").val();
			var year = $("#yearSel").val();
			var month = $("#monthSel").val();

			appset = appset + '{"key":"telephoneNumber","value":"' + telephoneNumber + '"},'
							+ '{"key":"year","value":"' + year + '"},'
							+ '{"key":"month","value":"' + month + '"},';
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
		var isEditable = resp.isEditable;
		that.queryListJson = "[";
		var tr_json = "";
		
		if (isEditable == true) {
    		if($("#monthSel option:selected").css("color") != "rgb(128, 128, 128)" && resultList.length!=0){
    			$('#privatNumberPar').show();	
    		}
		}
		
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
                }

            };
            doAJax.doConfig(pkg4conf);
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
//		alert(rowId+targetColor);
		$("#"+rowId).closest("tr").css('color', targetColor);
//		trElement.style.color = targetColor;
	},
	checkOtherCalledNumber : function(elem) {
		var theCalledNum = $("#"+elem.id).closest("tr").children("td:eq(4)").html();
//		alert("theCalledNum=" + theCalledNum);
		
		var inputArray = $("#"+elem.id).closest("tbody").find("input");
		for (var idx=0; idx<inputArray.length; idx++) {
			var theId = inputArray[idx].id;
			var tmpCalledNumVal = $("#"+theId).closest("tr").children("td:eq(4)").html();
			if (theId!=elem.id && tmpCalledNumVal == theCalledNum) {
//				alert("need to update: " + theId);
				$("#"+theId).attr("checked", elem.checked);
			}
		}
	},
	processPrivate : function(elem) {
		var recordId = elem.value;
		var isPrivate = 0;
		
		if (elem.checked == true) {
			isPrivate = 1;
		}
//		alert(recordId + ", " + isPrivate);
		// - check if there's other called number on the same page.
//		indexPage.checkOtherCalledNumber(elem);
		
//		var targetColor = (elem.checked==true)?"red":"#6C6C6C";
//		indexPage.adjustTrFontColor(elem.id, targetColor);
		
		var config = {
			url : commons.webserversite + '/billCallRecordService/updatePrivatePurposeByRecordId',
			type : 'POST',
			data : {
				recordId : recordId,
				isPrivate : isPrivate
			},
			success : 'indexPage.processPrivate_Success'
		};
		doAJax.doConfig(config);
	},
	processPrivate_Success : function(obj) {
//		var aId = obj.activityId;
//		$("#status-"+aId).html("Completed.");
//		$("#operationDiv-"+aId).remove();
	}
};

$(document).ready(function(){
	commons.webserversite = "/volkswagen-tel-billing-ws/rest"; //TODO local test no v-blabla only /rest
	var thats = this;
	indexPage.isQuery = false;
	
//	indexPage.init();
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
