package com.hp.idc.resm.ui.actions
{
	public interface IUiAction
	{
		function doAction(p :Object,newTag:Boolean=false) :void;
		
		function setTarget(target :Object, param: Object): void;
	}
}