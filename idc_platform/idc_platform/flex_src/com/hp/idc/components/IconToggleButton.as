/****************************************************************************
 **
 ** This file is part of yFiles FLEX 1.4.2.
 **
 ** yWorks proprietary/confidential. Use is subject to license terms.
 **
 ** Unauthorized redistribution of this file or of a byte-code version
 ** of this file is strictly forbidden.
 **
 ** Copyright (c) 2006-2010 by yWorks GmbH, Vor dem Kreuzberg 28, 
 ** 72070 Tuebingen, Germany. All rights reserved.
 **
 ***************************************************************************/
package com.hp.idc.components
{
	import spark.components.ToggleButton;
	import spark.primitives.BitmapImage;
	
	public class IconToggleButton extends ToggleButton
	{
		private var _iconClass:Class;
		
		public function get icon():Class {
			return _iconClass;
		}
		
		public function set icon(val:Class):void {
			_iconClass = val;
			if (iconPart != null)
				iconPart.source = _iconClass;
		}
		
		[SkinPart(required="false")]
		public var iconPart:BitmapImage;
		
		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			
			if (icon !== null && instance == iconPart)
				iconPart.source = icon;
		}
	}
}