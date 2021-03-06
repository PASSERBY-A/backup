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
	import spark.components.SkinnableContainer;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	
	public class ControlBar extends SkinnableContainer
	{
		public function ControlBar()
		{
			super();
			var vLayout:VerticalLayout = new VerticalLayout();
			vLayout.paddingBottom = 5;
			vLayout.paddingLeft = 5;
			vLayout.paddingRight = 5;
			vLayout.paddingTop = 5;
			this.layout = vLayout;
		}
	}
}