<%@ page language="java" contentType="text/html; charset=gbk"%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
%>
<html>
<head>
<title>上传文档</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<link type="text/css" rel="stylesheet" href="./uploadify/uploadify.css" />
<script type="text/javascript" src="./uploadify/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="./uploadify/swfobject.js"></script>
<script type="text/javascript" src="./uploadify/jquery.uploadify.v2.1.4.min.js"></script>
<script type="text/javascript">
$(function() {
	$('#document_upload').uploadify({
		'uploader' : './uploadify/uploadify.swf',
		'script' : './uploadify/uploadify.jsp',
		'cancelImg' : './uploadify/cancel.png',
		'folder' : '../servlet/Uploadify',
		'multi' : true,
		'auto' : false,
		'queueID' : 'upload_status',
		'queueSizeLimit' : 3,
		'simUploadLimit' : 3,
		'removeCompleted' : false,
		'onAllComplete'  : function(event,data) {
			alert((data.filesUploaded + ' 个文件上传成功, ' + data.errors + ' 个文件上传发生错误.'));
			window.close();
			window.opener.location.reload();
    	}
	});
});
</script>
<style type="text/css">
#upload_area .uploadifyQueueItem {
	background-color: #FFFFFF;
	border: none;
	border-bottom: 1px solid #E5E5E5;
	font: 11px Verdana, Geneva, sans-serif;
	height: 50px;
	margin-top: 0;
	padding: 10px;
	width: 350px;
}

#upload_area .uploadifyError {
	background-color: #FDE5DD !important;
	border: none !important;
	border-bottom: 1px solid #FBCBBC !important;
}

#upload_area .uploadifyQueueItem .cancel {
	float: right;
}

#upload_area .uploadifyQueue .completed {
	color: #C5C5C5;
}

#upload_area .uploadifyProgress {
	background-color: #E5E5E5;
	margin-top: 10px;
	width: 100%;
}

#upload_area .uploadifyProgressBar {
	background-color: #0099FF;
	height: 3px;
	width: 1px;
}

#upload_area #upload_status {
	border: 1px solid #E5E5E5;
	height: 213px;
	margin-bottom: 10px;
	width: 370px;
}
</style>
<body>
	<div id="upload_area" class="demo">
			<input id="document_upload" type="file" name="Filedata" />
			<div id="upload_status"></div>
			<a href="#" onclick="javascript:$('#document_upload').uploadifyUpload()">上传</a>
			<a href="#" onclick="window.close()">关闭</a>
	</div>
</body>
</html>