/*function isRegistered(str){
	if(str==null || str == "null"){
		return false;
	}
	else{ 
		return true;	
	}
}*/
function checkPlanStatus(planDetailID, planRejectedID){
	if((planDetailID==null || planDetailID == "null")&&!(planRejectedID==null || planRejectedID == "null")){
		return "PlanDetail.jsp?mode=reopen&";
	}else if(!(planDetailID==null || planDetailID == "null")&&(planRejectedID==null || planRejectedID == "null")){
		return "PlanDetail.jsp?mode=view&";
	}
	else{ 
		return "PlanDetail.jsp?mode=add&";	
	}
}
function addContentHeader(){
	var username=userName;
	var imgname = "";
	var agentname="";
	if(comLevel==5){imgname="";agentname="分销商";}
	else if(comLevel==6){imgname="";agentname="增值分销商";}
	var htmlUsername = '<tr align="right">'
					  +'<td width="623px"></td><td style="font-size: 12px;font-weight: bold;">'+username+'</td>';
	var htmlAgent ='<td width="100px"><table cellspacing="0px"><tr><td height=22px width="17px"><div style="background-image: url(images/'+imgname+'); background-repeat: no-repeat; padding-left: 0px; padding-top: 1px;">&nbsp;&nbsp;&nbsp;&nbsp;'+(agentname=="标准代理商"?'&nbsp;':'')+'</div></td><td width="65px" height=22px style="font-size: 12px;font-weight: bold; ">'+agentname+'</td></tr></table></td>'; 
	
	$('#contentHeader').html(htmlUsername+htmlAgent+"</tr>");
}
function getRequestParamter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function getEditable(inCurrentQuarter){
	var editable = "";
	 if(inCurrentQuarter==null || inCurrentQuarter=="null" || inCurrentQuarter==false || inCurrentQuarter=="false"){
		 editable = "editable=false&";
	 }else{
		 editable = "editable=true&";
	 }
	 return editable;
}

function getVADParters(){
	
//	$.blockUI({
//		message: "数据加载中...",
//		fadeIn: 700,
//		fadeOut: 1000,
//		centerY: false,
//		showOverlay: true,
//		css: {
//			width: "300px",
//			top: '200px',
//			"margin-left": 'auto',
//			"margin-right": 'auto',
//			border: 'none',
//			padding: '5px',
//			height: "16px",
//			"line-height": "16px",
//			opacity: 0.6,
//			cursor: 'default',
//			color: 'block',
//			backgroundColor: '#FFFFFF',
//			"background-image": "url(css/images/loading.gif)",
//			"background-position": "80px 5px",
//			"background-repeat": "no-repeat",
//			'-webkit-border-radius': '10px',
//			'-moz-border-radius': '10px',
//			'border-radius': '10px'
//}
//});
	
	$.blockUI({
		message: "数据加载中...",
		fadeIn: 700,
		fadeOut: 1000,
		centerY: false,
		showOverlay: true,
		css: {
			width: "480px",
			top: '300px',
			"margin-left": 'auto',
			"margin-right": 'auto',
			border: '1px solid black',
			padding: '5px',
			height: "100px",
			"line-height": "100px",
			opacity: 0.6,
			cursor: 'default',
			'font-weight':'border',
			color: '#f26522',
			backgroundColor: '#FFFFFF',
			"background-image": "url(css/images/loading.gif)",
			"background-position": "160px 50px",
			"background-repeat": "no-repeat"//,
			//'-webkit-border-radius': '10px',
			//'-moz-border-radius': '10px',
			//'border-radius': '10px'
		}
	});
	var strRang = QuarterGenerator._dropdownlist.getSelectedItem(),
		rang = strRang.value.split(','); 
	
	
    $.ajax({
         type : "GET",
         url : "./rest/partner/quarter/retrieveVAD/" + comId,
         contentType : "application/json; charset=utf-8",
         data : {
 			quarterStart: rang[0],
 			quarterEnd:   rang[1]
 		 },
         dataType : "json",
         success : function(data){
        	 $.unblockUI();
        	 var inCurrentQuarter = false;
        	 var editable = "";
        	 
        	 var specialPlanList = data['specialPrizePlanList'];
             var length = specialPlanList.length;
        	 	var htmlSpecialTable ="";
        	 	var htmlSpecialTableText = "";
        	 	for(var i =0;i<length;i++) {
        	 		inCurrentQuarter = specialPlanList[i]['inCurrentQuarter'];
         	 		editable = getEditable(inCurrentQuarter);
         	 		
        	 		operation = checkPlanStatus(specialPlanList[i]['planDetailId'],specialPlanList[i]['planRejectedId']);
        	 		if(operation == "PlanDetail.jsp?mode=add&"){
	     	 			htmlSpecialTable += '<tr><td style="padding-left:10px;"><div id="'+specialPlanList[i]['planId']+'" class="'+(specialPlanList[i]['isAccessable']?" blueFont":"")+'">'
	        	 		+(specialPlanList[i]['isAccessable']?'<a href="'+operation+editable+'userId='+userId+'&isVAD=1'+'&planId='+specialPlanList[i]['planId']+'&planDetailId='+specialPlanList[i]['planDetailId']+'">':"")
	        	 		+specialPlanList[i]['planName']+(specialPlanList[i]['isAccessable']?'</a>':"");
	         	 		if(!specialPlanList[i]['isAccessable']){
	        	 			htmlSpecialTable +="<img src='images/cannotparticipate.jpg' width='33px' height='21px' class='notparticipateimg'/>";
	         	 		} else{
	         	 			htmlSpecialTable +="<img src='images/canparticipate.jpg' width='28px' height='22px' class='participateimg'/>";
	         	 		}
	     	 		}else if(operation == "PlanDetail.jsp?mode=view&"){
	     	 			htmlSpecialTable += '<tr><td style="padding-left:10px;"><div id="'
	     	 				+specialPlanList[i]['planId']+'" class="'
	     	 				+(specialPlanList[i]['isAccessable']?"RegisteredProgramIcon":"")
	     	 				+(specialPlanList[i]['isAccessable']?" blueFont":"")+'">'
	        	 		+(specialPlanList[i]['isAccessable']?'<a href="'+operation+editable+'userId='+userId+'&isVAD=1'+'&planId='+specialPlanList[i]['planId']+'&planDetailId='+specialPlanList[i]['planDetailId']+'">':"")
	        	 		+specialPlanList[i]['planName']+(specialPlanList[i]['isAccessable']?'</a>':"");
	         	 		if(!specialPlanList[i]['isAccessable']){
        	 				htmlSpecialTable +="<img src='images/cannotparticipate.jpg' width='33px' height='21px' class='notparticipateimg'/>";
         	 			} 
	     	 		}else if(operation == "PlanDetail.jsp?mode=reopen&"){
	     	 			htmlSpecialTable += '<tr><td style="padding-left:10px;"><div id="'+specialPlanList[i]['planId']+'" class="'+(specialPlanList[i]['isAccessable']?" blueFont":"")+'">'
	        	 		+(specialPlanList[i]['isAccessable']?'<a href="'+operation+editable+'userId='+userId+'&isVAD=1'+'&planId='+specialPlanList[i]['planId']+'&planRejectedId='+specialPlanList[i]['planRejectedId']+'&planRejectedType='+specialPlanList[i]['planRejectedType']+'">':"")
	        	 		+specialPlanList[i]['planName']+(specialPlanList[i]['isAccessable']?'</a>':"");
	         	 		if(!specialPlanList[i]['isAccessable']){
	        	 			htmlSpecialTable +="<img src='images/cannotparticipate.jpg' width='33px' height='21px' class='notparticipateimg'/>";
	         	 		} else{
	         	 			if(specialPlanList[i]['planRejectedType']!=1){
	         	 				htmlSpecialTable +="<img src='images/wait.jpg' height='22px' class='participateimg'/>";
	         	 			}else
	         	 			htmlSpecialTable +="<img src='images/reject_icon.png'  height='22px' class='participateimg'/>";
	         	 		}
	     	 		}
	     	 		htmlSpecialTable +="</div></td></tr>";
        	 		// htmlSpecialTable += '<tr><td style="padding-left:10px;"><div id="'+specialPlanList[i]['planId']+'" class="'+((specialPlanList[i]['isAccessable'] && isRegistered(specialPlanList[i]['planDetailId']))?"RegisteredProgramIcon":"")+(specialPlanList[i]['isAccessable']?" blueFont":"")+'">'
        	 		// +(specialPlanList[i]['isAccessable']?'<a href="'+operation+'userId='+userId+'&planId='+specialPlanList[i]['planId']+'&planDetailId='+specialPlanList[i]['planDetailId']+'">':"")
        	 		// +specialPlanList[i]['planName']+(specialPlanList[i]['isAccessable']?'</a>':"");
        	 		// if(!specialPlanList[i]['isAccessable']){
        	 			// htmlSpecialTable +="<img src='images/cannotparticipate.jpg' width='33px' height='21px' class='notparticipateimg'/>";
         	 		// } else if (specialPlanList[i]['isAccessable'] && !isRegistered(specialPlanList[i]['planDetailId'])){
         	 			// htmlSpecialTable +="<img src='images/canparticipate.jpg' width='28px' height='22px' class='participateimg'/>";
         	 		// }
        	 		// htmlSpecialTable +="</div></td></tr>";
        	 		//alert(htmlSpecialTable);
        	 		
        		 }
        	 	if(htmlSpecialTable.length == 0){
        	 		htmlSpecialTable = "<tr><td align='center'><img src='images/noplan.jpg' width='134px' height='34px'/></td></tr>";
        	 	}
        	 	$("#specialPlan").empty().html(htmlSpecialTable);
        	/*var specialPlanList = data['specialPrizePlanList'];
        	var length = specialPlanList.length;
     	 	var htmlPlatinumTable ="";
     	 	var operation = "";
        	 	var htmlSpecialTable ="";
        	 	var htmlSpecialTableText = "";
        	 	for(var i =0;i<length;i++) {
        	 		operation = isRegistered(specialPlanList[i]['planDetailId'])?"PlanDetail.jsp?mode=view&":"PlanDetail.jsp?mode=add&";
        	 		htmlSpecialTable += '<tr><td style="padding-left:10px;"><div id="'+specialPlanList[i]['planId']+'" class="'+((specialPlanList[i]['isAccessable'] && isRegistered(specialPlanList[i]['planDetailId']))?"RegisteredProgramIcon":"")+(specialPlanList[i]['isAccessable']?" blueFont":"")+'">'
        	 		+(specialPlanList[i]['isAccessable']?'<a href="'+operation+'userId='+userId+'&planId='+specialPlanList[i]['planId']+'&planDetailId='+specialPlanList[i]['planDetailId']+'">':"")
        	 		+specialPlanList[i]['planName']+(specialPlanList[i]['isAccessable']?'</a>':"");
        	 		if(!specialPlanList[i]['isAccessable']){
        	 			htmlSpecialTable +="<img src='images/cannotparticipate.jpg' width='33px' height='21px' class='notparticipateimg'/>";
         	 		} else if (specialPlanList[i]['isAccessable'] && !isRegistered(specialPlanList[i]['planDetailId'])){
         	 			htmlSpecialTable +="<img src='images/canparticipate.jpg' width='28px' height='22px' class='participateimg'/>";
         	 		}
        	 		htmlSpecialTable +="</div></td></tr>";
        	 		//alert(htmlSpecialTable);
        	 		
        		 }
        	 	if(htmlSpecialTable.length > 0){
        	 		$("#specialPlan").html(htmlSpecialTable);
        	 	}*/
         },
         cache : false
     });
}
$(document).ready(function(){
	
	//var userPara = getRequestParamter("userId");
	//if(userPara == "") {
		//userPara = comId;
	//}
	


	addContentHeader();
	
	window.QuarterGenerator.renderHTML({
	    target: "quarterWraper",
	    showNextQuarter: false,
        click: function () {
            window.getVADParters();
        }
    });

	//$(".quarterhistory ul li a.selected").click();
	
	
});