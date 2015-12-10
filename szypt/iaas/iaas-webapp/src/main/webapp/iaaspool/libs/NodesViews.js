// NodesViews

var nodesViews = new Object();
nodesViews.nodeDatas = null;
nodesViews.rollLevel = 1;
nodesViews.rollAlign = null;
nodesViews.rollFront = 2;
nodesViews.cellMaxX = 480;
nodesViews.cellMaxY = 220;
nodesViews.returnTxt = '返回上一层结点';
//nodesViews.loadPath = 'loads/';
nodesViews.pathObj = null;
nodesViews.pageObj = null;
nodesViews.navDatas = new Array();
nodesViews.msgStr = '<div class="msgText"></div>';
nodesViews.levelStr = '<div class="nodeLevel"></div>';
nodesViews.controlStr = '<div id="nodeViewControls"><a class="prev floatl" href="####" rel="left"></a><a class="next floatr" href="####" rel="right"></a></div>';
nodesViews.rollPositions = [
	{scale:0.5, left:0, zIndex:300, opacity:0.25},
	{scale:0.7, left:130, zIndex:400, opacity:0.55},
	{scale:1, left:325, zIndex:500, opacity:1},
	{scale:0.7, left:520, zIndex:400, opacity:0.55},
	{scale:0.5, left:650, zIndex:300, opacity:0.25},
	{scale:0.3, left:325, zIndex:200, opacity:0}
];
nodesViews.levelPositions = [
	{scale:1, transformOrigin:"100% 0", opacity:0},
	{scale:1, transformOrigin:"100% 0", opacity:1},
	{scale:0.65, transformOrigin:"100% 0", opacity:0.45},
	{scale:0.4, transformOrigin:"100% 0", opacity:0.15},
	{scale:0.2, transformOrigin:"100% 0", opacity:0}
];
//
nodesViews.init = function(selector){
	this.root = $(selector);
	//
	if(($.browser.msie && $.browser.version<9) && !$.browser.mozilla) {
		this.badBrowser();
		return false;
	}
	//
	var _control = $(this.controlStr);
	_control.children('a').click(function() {
		nodesViews.unbindBTN();
		nodesViews.rollRoot(this.rel);
		nodesViews.rootMotion();
		return false;
	});
	this.root.append(_control);
	this.controls = _control;
	this.checkControl();
	//
	return this;
}
nodesViews.loadData = function(url){
	var _msg = $(this.msgStr);
	this.root.prepend(_msg);
	//
	//var _url = this.loadPath + url;
	$.ajax(url, {
		dataType:"json",
		cache:false,
		beforeSend:function(xhr){
			_msg.text('正在加载结点数据...');
		},
		success:function(data,txt,xhr){
			_msg.remove();
			nodesViews.nodeDatas = data;
			nodesViews.updateNav();
			nodesViews.loadNodes();
		},
		error:function(xhr,txt,er){
			_msg.text('加载结点数据失败...');
		}
	});
	
}
//
nodesViews.setupReturn = function(selector){
	this.returnObj = $(selector);
	//
	return this;
}
nodesViews.setupPath = function(selector){
	this.pathObj = $(selector);
	//this.updateNav();
	//
	return this;
}
nodesViews.setupPage = function(selector){
	this.pageObj = $(selector);
	//this.loadPage();
	//
	return this;
}
//
nodesViews.loadPage = function(){
	//var _url = this.nodeDatas.page;
	var _id=0,_level=0,_title;
	var _nodes = this.nodeDatas.items;
	var _data = this.navDatas;
	for (var i in _data) {
		var _n = _nodes[_data[i]];
		_nodes = _n.items;
		//if (_n.page) { _url = _n.page;}
		//else { _url = null;}
		_id = _n.id;
		_level = _n.level;
		_title = _n.title;
	}
	//if (!_url) { _url = '404.html';}
	//_url = this.loadPath + _url;
	var _p = this.pageObj;
	if (_p) {
		$("body").css("cursor","wait");
		//$.ajax(_url, {
		$.ajax("respool/resPoolUi", {
			dataType:"html",
			cache:false,
			data:{oid:_id ,level:_level ,title:_title},
			success:function(data,txt){
				_p.html(data);
			},
			error:function(xhr,txt,er){
				_p.html('<div class="msgText">试图加载的页面不存在或该结点视图配置错误...</div>');
			},
			complete:function(xhr,txt){
				$("body").css("cursor","auto");
			}
		});
	}
}
nodesViews.rollRoot = function(d){
	var _rolls = this.levels.eq(this.rollLevel).children('dl');
	//
	if (d == 'left') {
		this.rollAlign--;
		if (this.rollAlign<0) {
			this.rollAlign = _rolls.size() -1;
		}
	} else {
		this.rollAlign++;
		if (this.rollAlign >= _rolls.size()) {
			this.rollAlign = 0;
		}
	}
}
nodesViews.rootMotion = function(s,e){
	if (!s) { s = 0.6;}
	if (!e) { e = Power2.easeOut;}
	//
	var _rolls = this.levels.eq(this.rollLevel).children('dl');
	var _posCount = this.rollPositions.length-1;
	var _offset = (_rolls.size()<_posCount)? 1:0;
	_rolls.each(function(i){
		var _node = $(this);
		var _pos = nodesViews.rollFront - nodesViews.rollAlign +i;
		if (_pos < 0+_offset) {
			_pos += _rolls.size();
		}
		if (_pos >= _posCount-_offset) {
			_pos -= _rolls.size();
		}
		if (_pos < 0+_offset || _pos > _posCount-_offset) {
			_pos = _posCount;
		}
		TweenLite.to(_node, s, {css:nodesViews.rollPositions[_pos], ease:e, onStart:function(){
				if (this.vars.css.opacity == 1) {
					nodesViews.navDatas = [i];
					nodesViews.updateNav();
					nodesViews.loadPage();
				}
			}, onComplete:function(){
				if (this.vars.css.opacity == 1) {
					nodesViews.showItems(_node);
				}
			}
		});
	});
	//
	var _items = _rolls.children('dd');
	if (_items.size()) {
		TweenLite.killTweensOf(_items);
		TweenLite.to(_items, 0.3, {css:{scale:0.2,left:0,top:0,autoAlpha:0}});
	}
}
nodesViews.updateNav = function(){
	var _nav = this.nodeDatas.title;
	var _nodes = this.nodeDatas.items;
	var _data = this.navDatas;
	for (var _i in _data) {
		var _n = _nodes[_data[_i]];
		_nav += '>' + _n.title;
		_nodes = _n.items;
	}
	this.pathObj.html(_nav);
}
nodesViews.enterLevel = function(){
	var _n = this.levelPositions.length - 1;
	var _lvls = this.levels.not('.rootNodes');
	_lvls.each(function(i){
		var _pos = _lvls.size() - i;
		if (_pos < 0) { _pos = 0;}
		if (_pos >_n) { _pos = _n;}
		TweenLite.to($(this), 0.5, {css:nodesViews.levelPositions[_pos]});
	});
	this.updateNav();
	this.checkControl();
}
nodesViews.quitLevel = function(){
	var _n = this.levelPositions.length - 1;
	var _lvls = this.levels.not('.rootNodes');
	_lvls.each(function(i){
		var _l = $(this);
		var _pos = _lvls.size() - i - 1;
		if (_pos < 0) { _pos = 0;}
		TweenLite.to(_l, 0.5, {css:nodesViews.levelPositions[_pos], onComplete:function(lvl){
				if (this.vars.css.opacity == 0) {
					_l.remove();
				}
				if (i+1 == _lvls.size()) {
					nodesViews.levels = nodesViews.root.find('.nodeLevel');
					nodesViews.bindBTN();
				}
			}
		});
	});
	if (_lvls.size() == 1) {
		TweenLite.to(_lvls.prev(), 0.3, {css:{opacity:1}});
	}
	//
	this.updateNav();
	this.checkControl();
	this.loadPage();
}
nodesViews.checkControl = function(){
	if (this.navDatas.length == this.rollLevel) {
		TweenLite.to(this.controls, 0.3, {css:{autoAlpha:1}});
	} else {
		TweenLite.to(this.controls, 0.2, {css:{autoAlpha:0}});
	}
}
nodesViews.loadNodes = function(){
	var _lvl = $(this.levelStr);
	var _nav = this.navDatas;
	var _data = this.nodeDatas;
	var _nodes = [_data];
	//
	if (this.levels) {
		this.levels.last().after(_lvl);
	} else {
		this.root.prepend(_lvl.addClass('rootNodes'));
	}
	if (_lvl.index() == this.rollLevel) {
		_lvl.addClass('rollLevel');
	}
	this.levels = this.root.find('.nodeLevel');
	//
	for (var _i in _nav) {
		_nodes = _data.items;
		if (_nodes) {
			_data = _nodes[_nav[_i]];
		}
	}
	if (!_lvl.hasClass('rollLevel')) {
		_nodes = [_data];
	}
	//
	if (!_nodes) {
		if (!_lvl.hasClass('rollLevel')) {
			_lvl.addClass('endNodes');
		}
	} else if (!_nodes[0].items && !_lvl.hasClass('rootNodes')) {
		_lvl.addClass('lastNodes');
	}
	//
	for (var _i in _nodes) {
		var _node = _nodes[_i];
		var _item = $('<dl></dl>');
		//
		var _val = '';
		if (_node.val) {
			_val = '<div class="percent"><span class="val">'+_node.val+'</span>%</div>';
		}
		//
		_item.append('<dt><table class="cell"><tr><td>'+_val+_node.title+'</td></tr></table></dt>');
		if (_node.items) {
			for (var _j in _node.items) {
				_item.append('<dd><table class="cell"><tr><td>'+_node.items[_j].title+'</td></tr></table></dd>');
			}
		}
		_lvl.append(_item);
	}
	//
	if (_lvl.hasClass('rollLevel')) {
		for (var _i in _nav) {
			this.rollAlign = _nav[_i];
		}
		//
		TweenLite.from(_lvl.find('dt'), 0.3, {css:{scale:0.2,opacity:0}});
		TweenLite.to(_lvl.prev(), 0.5, {css:{opacity:0}});
		this.rootMotion(1.2, Elastic.easeOut);
	} else {
		TweenLite.from(_lvl.find('dt'), 0.3, {css:{scale:0.2}, onComplete:function(){
				nodesViews.showItems(_lvl.children('dl'));
			}
		});
		this.loadPage();
	}
}
nodesViews.showItems = function(obj){
	var _items = obj.children('dd');
	var _cells = _items.size()+1;
	var _mx = this.cellMaxX;
	var _my = this.cellMaxY;
	_items.each(function(i){
		var _item = $(this);
		var _x = _mx*2/_cells*(i+1) - _mx;
		var _y = _my*Math.sin((i+1) / _cells*Math.PI);
		var _s = _mx*2/_cells/250;
		if (_s>0.6) { _s = 0.6;}
		TweenLite.fromTo(_item, 1.2, {css:{scale:0.2,left:0,top:0,autoAlpha:0}}, {css:{scale:_s,left:_x,top:_y,autoAlpha:1}, delay:i*0.2, ease:Elastic.easeOut, onStart:function(){
				_item.show();
			}, onComplete:function(){
				if(i+1 == _items.size()) {
					nodesViews.bindBTN();
				}
			}
		});
		_item.attr('scale',_s);
	});
	if (!_items.size()) { this.bindBTN();}
}
nodesViews.unbindBTN = function(){
	if (this.returnObj) {
		this.returnObj.unbind();
	}
	this.levels.find('dd').unbind().removeClass('btn');
}
nodesViews.bindBTN = function(){
	if (this.returnObj) {
		this.returnObj.click(function(){
			if (nodesViews.navDatas.length > 0) {
				nodesViews.unbindBTN();
				nodesViews.navDatas.pop();
				nodesViews.quitLevel();
			}
			//
			return false;
		}).attr('title',this.returnTxt);
	}
	//
	this.levels.last().find('dd').bind('click', function(){
		nodesViews.unbindBTN();
		nodesViews.navDatas.push($(this).index()-1);
		nodesViews.loadNodes();
		nodesViews.enterLevel();
	}).hover(function(){
		var _s = +$(this).attr('scale');
		TweenLite.to($(this), 0.2, {css:{scale:_s+0.03}});
	},function(){
		TweenLite.to($(this), 0.6, {css:{scale:$(this).attr('scale')}, ease:Elastic.easeOut});
	}).addClass('btn').mouseout();
}
nodesViews.badBrowser = function(){
	var _msg = $(this.msgStr);
	_msg.html('<h3>浏览器兼容性问题</h3><p>您当前使用的浏览器不能满足本模块页面显示要求，通常这是因为浏览器版本过低。</p><p>系统目前只支持火狐及 <strong>IE9.0</strong> 以上的版本浏览，请切换至 <em>IE9模式</em> 浏览或升级您的 IE...</p><p><strong>IE9 升级地址：</strong></p><p><a target="_blank" href="/file/IE9-Windows7-x86-chs.exe">IE9 32位 中文版</a></p><p><a target="_blank" href="IE9-Windows7-x64-chs.exe">IE9 64位 中文版</a></p><p><strong>火狐 升级地址：</strong></p><p><a target="_blank" href="FirefoxSetup17.0.1_cn.zip">FireFox17 中文版</a></p>');
	this.root.append(_msg);
}

