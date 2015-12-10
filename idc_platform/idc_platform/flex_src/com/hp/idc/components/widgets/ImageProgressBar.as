package com.hp.idc.components.widgets
{
	import mx.controls.ProgressBar;
	import mx.controls.ProgressBarMode;
	
	public class ImageProgressBar extends ProgressBar
	{
		public function ImageProgressBar()
		{
			super();
			this.mode = ProgressBarMode.MANUAL;//手动设置进度条状态
			this.minimum = 0;
			this.maximum = 100;
			this.labelPlacement="center";
		}
		
		//设置完成
		public stopProgressBar(){
			this.setProgress(100,100);
		}
	}
}