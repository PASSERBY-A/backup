// JavaScript Document

/* ����IE6 png ͼƬ������ ͸�� end */

/* ���� js */
/* �������� */
function action1(num) {
	var i;
	for (i = 5; i <= 9; i++) {
		if(document.getElementById("li" + i).className=="nav_liSelect")
			continue;
		document.getElementById("ul_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("ul_tatle"+num).style.display = "block";
	document.getElementById("ul_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "nav_liOn";

}

function disappear(num,menu) {
	document.getElementById("ul_tatle"+num).style.display = "none";
	document.getElementById("li" + num).className="";
	document.getElementById("li" + menu).className="nav_liSelect";
}
/* �������� end */
/* ���� end */
/**
 * ��ȡ����δ����Ϣ
 */
function getMessageCount(){
	var flag = true;
	$.ajax({
		type:'get',
		datatype:'json',
		cache:false,// ���ӻ�����ȥ����
		url:'../getData/getMessageCount.jsp',
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
			url: '../getData/getMessage.jsp',
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
