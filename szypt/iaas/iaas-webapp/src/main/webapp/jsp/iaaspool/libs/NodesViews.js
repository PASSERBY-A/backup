// NodesViews

var nodesViews = new Object();
//
nodesViews.init = function(selector,number){
	var eles = $(selector);
	//
	$.each(eles, function(i,n){
		var _frame = $(n).attr('frame');
		nodesViews.assign($(n), _frame, 2*number);
	});
	//
}

nodesViews.assign = function(container, frame, number){
	if (!frame) {
		frame = 'static';
	}
	var cells = container.children();
	var origin = container.offset();
	var x = 0;
	var y = 0;
	//
	var grid = {};
	grid.width = (cells.size()<4)? container.width()/cells.size() : container.width()/4;
	grid.height = (cells.size()>4)? container.height()/2 : container.height();
	//
	$.each(cells,function(i,n){
		var _cell = $(n);
		//
		if (frame == 'main') {
			if (cells.size() <8 && i >3) {
				grid.width = container.width()/(cells.size()-4);
			}
			//
			x= grid.width*(i%4 +0.5 +Math.floor(i/8)*4) - _cell.width()/2;
			y= grid.height*(Math.floor(i/4) +0.5 -Math.floor(i/8)*2) - _cell.height()/2;
		}
		//
		if (frame == 'bar') {
			x= container.width()/number*i;
		}
		//
		if (frame == 'static') {
			x= (container.width()-_cell.width())/2;
			y= (container.height()-_cell.height())/2;
		}
		//
		_cell.offset({top:origin.top +y, left:origin.left +x});
	});
	//
	if (frame == 'main') {
		cells.click(function(){
			window.location.href=clientApiUrl+"?command=firstResource&cmsz=yes&zoneId="+$(this).attr("id")+"&title="+$(this).find("a").text();
		/*},function(){
			$('.nodeViewTips').stop(true,true);*/
		});
		
		$('.nodeViewTips .close').click(function(){
			$(this).parent().hide();
			$('.nodeViewTips').removeClass('alertTips');
			$('.nodeViewControls').show();
		});
		//
		if (cells.size()>8) {
			nodesViews.controls(container, 8);
		}
	}
	if (frame == 'bar' && cells.size() >2) {
		nodesViews.controls(container, number, container.parent(), 60, 'ctrlFace2');
	}
}

nodesViews.controls = function(ele, counts, ctrlrel, ctrloffset, ctrlface){
	var controls = $('<div class="nodeViewControls"><a href="javascript:void(0)" class="prev floatl dis"></a><a href="javascript:void(0)" class="floatr next"></a></div>');
	if (!ctrlrel) ctrlrel =ele;
	if (!ctrloffset) ctrloffset =0;
	if (ctrlface) controls.addClass(ctrlface);
	//
	ctrlrel.after(controls);
	controls.width(ctrlrel.parent().width() +ctrloffset);
	controls.css({top:ctrlrel.position().top +(ctrlrel.height()-controls.height())/2, left:(ctrlrel.width()-controls.width())/2});
	var clicked=false;
	controls.children('a').click(function(e){
		if(clicked){alert('数据加载中，请勿多次点击');return;}
		clicked=true;
		$(this).blur();
		var _x = ele.position().left;
		var _page = Math.ceil(ele.children('li').size()/counts) -1;
		//
		if(_x>0){
			_x=0;
		}else if(_x<-_page *ele.width()){
			_x=-_page *ele.width();
		}
		if ($(this).hasClass('prev')){
			if (_x < 0){
				_x += ele.width();
			}
		}
		if ($(this).hasClass('next')){
			if (_x > -_page *ele.width()) {
				_x -= ele.width();
			}
		}	
		//
		ele.animate({left: _x}, 'fast', function(){
			if (_x >= 0){
				controls.children('.prev').addClass('dis');
				controls.children('.prev').hide();
			} else {
				controls.children('.prev').removeClass('dis');
				controls.children('.prev').show();
			}
			if (_x <= -_page *ele.width()) {
				controls.children('.next').addClass('dis');
				controls.children('.next').hide();
			} else {
				controls.children('.next').removeClass('dis');
				controls.children('.next').show();
			}
			clicked=false;
		});
		//
	});
}

