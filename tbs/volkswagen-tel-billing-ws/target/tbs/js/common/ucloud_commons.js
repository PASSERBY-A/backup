/*
 * Common js configuration
 *
 */
var commons = {
	//project name, usually the name of war file
	webserversite : '/volkswagen-tel-billing-ws',
	
	//the position of sequence column in dataTable
	xhId : 0
};


/**
 * application management: variable definition
 * 
 * @type 
 */
var appsConstants = {
	//app type: service
	APPS_TYPE_SERVICE : "service",
	//app type: moudle
	APPS_TYPE_MODULE : "module",
	//binding type: resource
	APPS_TYPE_RESOURCE : "resource",
	
	//app type: BM
	APPS_TYPE_BM : "BM",
	//app type: BS
	APPS_TYPE_BS : "BS",
	//app type: TS
	APPS_TYPE_TS : "TS",
	
	//app state: initialized
	APPS_STATE_INITIALIZED : "initialized",
	//app state: active
	APPS_STATE_ACTIVE : "active",
	//app state: history
	APPS_STATE_HISTORY : "history",
	
	//NODE type: VM
	NODE_TYPE_VM : "VM",
	//NODE type: PM
	NODE_TYPE_PM : "PM",
	
	//NODE state: ready
	NODE_STATE_READY : "ready",

	//NODE state: history
	NODE_STATE_DEPLOYED : "deployed",
	
	//app instance state: ON
	APPS_INSTANCE_STATE_ON : "on",	

	//app instance state: off
	APPS_INSTANCE_STATE_OFF : "off",
	
	//app BINDING state: initialized
	APPS_BINDING_STATE_INITIALIZED : "initialized",	

	//app BINDING state: ready
	APPS_BINDING_STATE_READY : "ready",	
	
	//app SYSTEM state: active
	APPS_SYSTEM_STATE_ACTIVE : "active",	
	
	//app SYSTEM state: history
	APPS_SYSTEM_STATE_HISTORY : "history",
};

/*
 * Initialize dataTable plugin
 * 
 */
var datatable = {
					"bJQueryUI": true,
					"sPaginationType": "full_numbers",
					"sDom": '<"H"Tfr>t<"F"lip>',
					"bStateSave": false,
					"oLanguage": {
						"sUrl": commons.webserversite+"/js/common/zh_cn.txt"
			 		},
			 		"bInfo": true,
			 		"bDestroy": true,
			 		"iDisplayStart": 20,
			 		"aoColumns": [
			 		],
			 		"aaSorting": [[1, 'asc']],
					"bProcessing": true,
					"oTableTools": {
						"aButtons": [

						]
					},
					"fnDrawCallback": function ( oSettings ) {
						/* Set default sequence number column */
						if ( oSettings.bSorted || oSettings.bFiltered ) {
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ) {
								$('td:eq('+commons.xhId+')', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
							}
						}
					}
	};
			
var datatableOnServer = {
		"bJQueryUI": true,
		"bInfo": true,
 		"bDestroy": true,
 		"bAutoWidth" : false,
		"bStateSave": false,
		"bProcessing": true,
		"sPaginationType": "full_numbers",
		"sDom": '<"H"Tfr>t<"F"lip>',
		"oLanguage": {
			"sUrl": commons.webserversite + "/js/common/zh_cn.txt"
 		},
 		"aoColumns": [],
 		"aaSorting": [[1, 'asc']],
		"oTableTools": {
			"aButtons": []
		},
		//set tr class
		"asStripeClasses": [ 'gradeA odd', 'gradeA even' ],
		"sAjaxDataProp" : '',
		"bServerSide": true,    // the server address for fetching data.  
		"fnServerData": "",     // the function for fetching data.  
		"sAjaxSource": "",		// the url for fetching data.
		"bFilter" : false,
		"aLengthMenu" : [50, 100]
};

