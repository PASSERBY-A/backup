package com.hp.idc.util
{
	public class StringToolkit
	{
		
		public static function formatNumberWithChar(n:Number,i:int,char:String):String{
			var s:String = n.toString();
			if(s.length<i){
				var size:int = i-s.length;
				for(var l:int=0;l<size;l++){
					s = char + s;
				}
			}
			return s;
		}
	}
}