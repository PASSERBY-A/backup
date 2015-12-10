package com.hp.idc.ui
{
	import com.hp.idc.components.PetAlert;
	
	import mx.controls.Alert;
	import mx.managers.CursorManager;
	import mx.rpc.events.FaultEvent;

	public class FaultHandler
	{
		public function FaultHandler()
		{
		}
		
		private static var ignorStrings :Array = [
			"java.lang.Exception : "
			];
		
		public static function defaultFaultHandler(event:FaultEvent):void
		{
			//Alert.show(event.toString(), "错误");
			var s:String = event.fault.faultString;
			for each (var ss :String in ignorStrings)
			{
				if (s.indexOf(ss) == 0)
				{
					s = s.substr(ss.length);
					break;
				}
			}
			CursorManager.getInstance().removeBusyCursor();
			PetAlert.show(s, "错误", 4, null, null, PetAlert.ICON_ERROR);
		}
	}
}