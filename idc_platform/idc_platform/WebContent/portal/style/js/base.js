// JavaScript Document
/*导航  js*/
/*导航滑动*/
function action1(num) {
	var i;
	for (i = 5; i <= 9; i++) {
		document.getElementById("ul_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("ul_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "nav_liOn";
}

/*导航滑动 end*/

/*知识库、知识问答、热点知识---滑动*/
function action2(num) {
	var i;
	for (i = 1; i <= 2; i++) {
		document.getElementById("con_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("con_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "li_on1";
}
/*知识库、知识问答、热点知识---滑动 end*/
/*导航  end*/

/*弹出层  js*/

/*弹出层  end*/
/*首页日期3级联动JS*/
function initDate() {
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	//每个月的初始天数
	MonDays = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
	//当前的年份
	var y = new Date().getFullYear();
	//当前的月份
	var m = new Date().getMonth() + 1; //javascript月份为0-11
	//但前的天份
	var d = new Date().getDate();
	//以今年为准，向后2年，填充年份下拉框
	for ( var i = y; i < (y + 2); i++) {
		year.options.add(new Option(i, i));
	}
	//选中今年
	year.value = y;

	//填充月份下拉框
	for ( var i = 1; i <= 12; i++) {
		month.options.add(new Option(i, i));
	}
	//选中当月
	month.value = m;

	//获得当月的初始化天数
	var n = MonDays[m - 1];
	//如果为2月，天数加1
	if (m == 2 && isLeapYear(year.options[year.selectedIndex].value))
		n++;
	//填充日期下拉框
	createDay(n, day);
	//选中当日
	day.value = new Date().getDate();
}

function change(year, month, day) {//年月变化，改变日
	var y = year.options[year.selectedIndex].value;
	var m = month.options[month.selectedIndex].value;
	//if (m == "" ){  clearOptions(day); return;}
	var n = MonDays[m - 1];
	if (m == 2 && isLeapYear(y)) {
		n++;
	}
	createDay(n, day)
}　
function createDay(n,day){ //填充日期下拉框
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	//清空下拉框
	clearOptions(day);
	//几天，就写入几项
	for(var i=1; i<=n; i++){
		day.options.add(new Option(i,i));
	}
}

function clearOptions(ctl){//删除下拉框中的所有选项
	for(var i=ctl.options.length-1; i>=0; i--){
	    ctl.remove(i);
	}
}
    　　
function isLeapYear(year) {//判断是否闰年
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	return( year%4==0 || (year%100 ==0 && year%400 == 0));
}
/*首页日期3级联动JS end*/

/**
 * 消息中心异步获取数据
 */
function getMessage(){
	var postHandler = function(postsJSON) {
		if(postsJSON.length==0){
			alert('没有未读消息！！！');
			loadMore.text('已没有未读消息');
		}
		$.each(postsJSON,function(i,post) {
			//post url
			var postURL = '/portal/';
			var id = 'post-' + post.ID;
			//create the HTML
			$('<div></div>')
			.addClass('post')
			.attr('id',id)
			//generate the HTML
			.html('<a href="' + post.url + '" class="post-title">' + post.title + '</a><p class="post-content">' + post.content + '<br /><a href="' + post.url + '" class="post-more">Read more...</a></p>')
			.click(function() {
				window.location = postURL;
			})
			//inject into the container
			.appendTo($('#posts'))
			.hide()
			.slideDown(250,function() {
				if(i == 0) {
					$.scrollTo($('div#' + id));
				}
			});
		});	
	};
	//place the initial posts in the page
	//first, take care of the "load more"
	//when someone clicks on the "load more" DIV
	var start = 0;
	var limit = 3;
	var loadMore = $('#load-more');
	//load event / ajax
	loadMore.click(function(){
		//add the activate class and change the message
		loadMore.addClass('activate').text('读取中...');
		//begin the ajax attempt
		$.ajax({
			url: 'getData/getMessage.jsp',
			data: {
				'start': start,
				'limit': limit
			},
			type: 'get',
			dataType: 'json',
			cache: false,
			success: function(responseJSON) {
				//reset the message
				loadMore.text('加载更多未读消息');
				//increment the current status
				start += limit;
				//add in the new posts
				postHandler(responseJSON);
			},
			//failure class
			error: function() {
				//reset the message
				loadMore.text('加载失败，请您重新加载.');
			},
			//complete event
			complete: function() {
				//remove the spinner
				loadMore.removeClass('activate');
			}
		});
	});
}
/**
 * 获取知识库数据
 */
function getKBM(){
	$.post("/kbm/common/topData.jsp", function(data){
		var values = eval('(' + data + ')');
		var info_update = values.info_update;
		var qa_update = values.qa_update;
		var info ="<ul class='content6' id='con_block1'>";
		for(var i =0;i<info_update.length&&i<5;i++){
			info += "<li><a onclick='openWin(\""+info_update[i].url+"\",600,400)'>"+info_update[i].title+"</a></li>";
		}
		info += "</ul>";
		info +="<ul class='content6' id='con_block2' style='display:none;'>";
		for(var i =0;i<qa_update.length&&i<5;i++){
			info += "<li><a onclick='openWin(\""+qa_update[i].url+"\",600,400)'>"+qa_update[i].title+"</a></li>";
		}
		info += "</ul>";
		$("#index_conerCon2").html(info);
	});
}

/**
 * 获取个人未读消息
 */
function getMessageCount(){
	var flag = true;
	$.ajax({
		type:'get',
		datatype:'json',
		cache:false,// 不从缓存中去数据
		url:'getData/getMessageCount.jsp',
		success:function(data){
			$('#messageCount').text(data.msg); 
		},
		complete:function(){
			if(flag)
				setTimeout("getMessageCount()",30000); // 定时器
		},
		error:function(){
			flag = false;
		} 
	})
}
