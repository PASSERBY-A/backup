package com.hp.idc.util
{
	
	import mx.controls.Alert;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.rpc.remoting.RemoteObject;

	public class UserTool
	{
		public static var userId:int = 1;
		public static var userName:String;
		
		public static function init():void {			
			var service:HTTPService = new HTTPService();
			service.url = "loginUser.jsp";
			service.resultFormat = "text";
			service.addEventListener(ResultEvent.RESULT, loginUser);
			service.addEventListener(FaultEvent.FAULT, failedHandler);
			service.send();
		}
		
		private static function loginUser(event:ResultEvent):void {
			var ret:String = event.result.toString();
			
			UserTool.userId = int(ret.split(";")[0]);
			UserTool.userName = ret.split(";")[1];
		}
		
		private static function failedHandler(event:FaultEvent):void{
			Alert.show(event.fault.toString());
		}
	}
}