package com.hp.idc.resm.model
{
	public class PageInfo
	{
		private var _orderInfo:Object;
		
		public function PageInfo()
		{
		}
		
		public function setFieldOrder(field:String,order:String="asc"):void{
			if(!_orderInfo){
				_orderInfo = new Object();
			}
			if(order=="asc"||order=="desc"){
				_orderInfo[field] = order;
			}
		}
		
		public function get order():Object{
			return _orderInfo;
		}
	}
}