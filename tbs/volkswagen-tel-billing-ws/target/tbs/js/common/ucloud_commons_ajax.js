/*
 * The encapsulation for consolidated processing of ajax call.
 * 
 * Parameters：
 * configuration object, including
 * 		url      : the url for the ajax call
 *  	type     : type of ajax request, default 'POST'
 *  	dataType : dataType of ajax request, default 'json'
 *  	data     : data of ajax, including biz parameters, defaul null.
 *  	contentType ：ajax的contentType，默认为application/json
 *  	success  : callback method for successful ajax invocation.
 *  
 */
var doAJax = {
	doConfig : function(config) { 
		var that = this;
		if (config.data == undefined) {
			config.data = null;
		}
		if (config.type == undefined || config.type == null) {
			config.type = 'POST';
		}
		if (config.async == undefined || config.async == null) {
			config.async = 'true';
		}
		$.ajax({
				url : config.url,
				type : config.type,
				dataType : 'json',
				async : config.async,
				timeout : 120000,
				data : config.data,
				//contentType : 'application/json',
				cache : false,
				beforeSend: function(x) {
		            if (x && x.overrideMimeType) {
		              x.overrideMimeType("application/j-son;charset=UTF-8");
		            }
		            // for cross-domain
		            x.setRequestHeader("Accept", "application/json");
		        },
		        afterSend : function(x) {
		        	alert("after");
		        },
				"success" : function(data) {
//						data.doneCode = 0;
					// use this parameter for call-back, when using DataTable.
					if (config.isDT) {
						config.callFun(data);
					}
//						that.successFun(data);
					eval(config.success + '(data)');
				},
				error : function(data){
					alert("Invocation failure! \nError code: "+data.status+"\nError description: "+data.statusText);
//						alert(data.status+":"+data.statusText);

				}
			});
	},
	successFun : function(data) {
//			alert("Invocation success!");
	}
};


