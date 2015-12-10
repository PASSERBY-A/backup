// Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) {
	
	$("#header .link").live('click',function(){
		 /*$.ajax({
             url: createURL("firstPage&cmsz=yes&response=json"),
             async: true,
             success: function(json) {
            	 window.location.href="/jsp/iaaspool/firstResource.jsp";
             }
         });*/
		 window.location.href=clientApiUrl+"?command=firstPage&cmsz=yes";
	});

})(jQuery, cloudStack);
