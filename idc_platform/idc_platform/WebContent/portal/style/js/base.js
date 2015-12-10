// JavaScript Document
/*����  js*/
/*��������*/
function action1(num) {
	var i;
	for (i = 5; i <= 9; i++) {
		document.getElementById("ul_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("ul_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "nav_liOn";
}

/*�������� end*/

/*֪ʶ�⡢֪ʶ�ʴ��ȵ�֪ʶ---����*/
function action2(num) {
	var i;
	for (i = 1; i <= 2; i++) {
		document.getElementById("con_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("con_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "li_on1";
}
/*֪ʶ�⡢֪ʶ�ʴ��ȵ�֪ʶ---���� end*/
/*����  end*/

/*������  js*/

/*������  end*/
/*��ҳ����3������JS*/
function initDate() {
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	//ÿ���µĳ�ʼ����
	MonDays = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
	//��ǰ�����
	var y = new Date().getFullYear();
	//��ǰ���·�
	var m = new Date().getMonth() + 1; //javascript�·�Ϊ0-11
	//��ǰ�����
	var d = new Date().getDate();
	//�Խ���Ϊ׼�����2�꣬������������
	for ( var i = y; i < (y + 2); i++) {
		year.options.add(new Option(i, i));
	}
	//ѡ�н���
	year.value = y;

	//����·�������
	for ( var i = 1; i <= 12; i++) {
		month.options.add(new Option(i, i));
	}
	//ѡ�е���
	month.value = m;

	//��õ��µĳ�ʼ������
	var n = MonDays[m - 1];
	//���Ϊ2�£�������1
	if (m == 2 && isLeapYear(year.options[year.selectedIndex].value))
		n++;
	//�������������
	createDay(n, day);
	//ѡ�е���
	day.value = new Date().getDate();
}

function change(year, month, day) {//���±仯���ı���
	var y = year.options[year.selectedIndex].value;
	var m = month.options[month.selectedIndex].value;
	//if (m == "" ){  clearOptions(day); return;}
	var n = MonDays[m - 1];
	if (m == 2 && isLeapYear(y)) {
		n++;
	}
	createDay(n, day)
}��
function createDay(n,day){ //�������������
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	//���������
	clearOptions(day);
	//���죬��д�뼸��
	for(var i=1; i<=n; i++){
		day.options.add(new Option(i,i));
	}
}

function clearOptions(ctl){//ɾ���������е�����ѡ��
	for(var i=ctl.options.length-1; i>=0; i--){
	    ctl.remove(i);
	}
}
    ����
function isLeapYear(year) {//�ж��Ƿ�����
	var year = $("#select_year")[0];
	var month = $("#select_month")[0];
	var day = $("#select_day")[0];
	return( year%4==0 || (year%100 ==0 && year%400 == 0));
}
/*��ҳ����3������JS end*/

/**
 * ��Ϣ�����첽��ȡ����
 */
function getMessage(){
	var postHandler = function(postsJSON) {
		if(postsJSON.length==0){
			alert('û��δ����Ϣ������');
			loadMore.text('��û��δ����Ϣ');
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
		loadMore.addClass('activate').text('��ȡ��...');
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
				loadMore.text('���ظ���δ����Ϣ');
				//increment the current status
				start += limit;
				//add in the new posts
				postHandler(responseJSON);
			},
			//failure class
			error: function() {
				//reset the message
				loadMore.text('����ʧ�ܣ��������¼���.');
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
 * ��ȡ֪ʶ������
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
 * ��ȡ����δ����Ϣ
 */
function getMessageCount(){
	var flag = true;
	$.ajax({
		type:'get',
		datatype:'json',
		cache:false,// ���ӻ�����ȥ����
		url:'getData/getMessageCount.jsp',
		success:function(data){
			$('#messageCount').text(data.msg); 
		},
		complete:function(){
			if(flag)
				setTimeout("getMessageCount()",30000); // ��ʱ��
		},
		error:function(){
			flag = false;
		} 
	})
}
