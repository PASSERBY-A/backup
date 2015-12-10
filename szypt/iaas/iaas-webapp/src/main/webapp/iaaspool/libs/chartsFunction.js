// JavaScript Document

function initPieCart(id,data){
	if (!id) return false;
	//
	var _pie = new AmCharts.AmPieChart();
	_pie.dataProvider = data;
	_pie.titleField = "title";
	_pie.valueField = "value";
	_pie.colors = ["#B0DE09", "#fc965f", "#c3cfe5"];
	_pie.startAlpha = 0;
	_pie.startAngle = 100;
	_pie.startEffect = '>';
	_pie.startRadius = 10;
	_pie.gradientRatio = [-0.2,0.2];
	_pie.radius = 70;
	_pie.outlineAlpha = 0.8;
	_pie.outlineThickness = 2;
	_pie.depth3D = 15;
	_pie.angle = 30;
	_pie.labelRadius = 5;
	_pie.labelText = '[[percents]]%';
	_pie.write(id);
	//
	var _all = 0;
	var _obj = $('#'+id).next('.countData');
	for(var _i in data) {
		_all += data[_i].value;
		_obj.append('<p>'+data[_i].title+'：'+data[_i].value+'</p>');
	}
	if(data.length>1) {
		_obj.prepend('<p>总数：'+_all+'</p>');
	}
}
function initColumn(id,data,page){
	if (!id) return false;
	//
	$('#'+id).children('li').each(function(i){
		var _data = data.data[i];
		if (_data) {
			$(this).removeClass('empty');
			$(this).children('label').text(_data.networkname).next().children('.value').text(Math.round(100*_data.usedSum/_data.ipTotal)+'%');
			TweenLite.to($(this).children('.chart'), 0.6, {css:{height:$(this).find('.value').text()}, delay:0.1*i});
		} else {
			$(this).addClass('empty');
			$(this).children('label').text(null).next().children('.value').text('0%');
		}
	});
	$('#'+page).html('<label>'+data.page+'</label>'+'/'+data.total);
}

