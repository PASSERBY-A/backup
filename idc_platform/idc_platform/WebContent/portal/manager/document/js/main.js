
/****************************************************
*
*		关闭页面时调用此函数，关闭文件 
*
****************************************************/
function window_onunload() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.Close();
	}catch(e){
	//	alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					新建文档
*
****************************************************/
function newDoc() {
	try{
		var webObj=document.getElementById("WebOffice1");
		var doctype=document.getElementById("doctype").value;
		webObj.LoadOriginalFile("", doctype);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}

/****************************************************
*
*				显示打印对话框
*
/***************************************************/
function showPrintDialog(){
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.PrintDoc(1);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					直接打印
*
****************************************************/
function zhiPrint(){
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.PrintDoc(0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*			关闭页面时调用此函数，关闭文件 
*
****************************************************/
function window_onunload() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.Close();
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*				 解除文档保护 
*
****************************************************/
function UnProtect() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ProtectDoc(0,1, document.all.docPwd.value);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			
*				设置文档保护 
*
****************************************************/
function ProtectFull() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ProtectDoc(1,1, document.all.docPwd.value);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					禁止打印
*
****************************************************/
function notPrint() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x01); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					恢复允许打印
*
/****************************************************/
function okPrint() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x01 + 0x8000);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}

}
/****************************************************
*
*					禁止保存
*
****************************************************/
function notSave() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x02); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}

}
/****************************************************
*
*					恢复允许保存
*
/****************************************************/
function okSave() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x02 + 0x8000);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}

}
/****************************************************
*
*					禁止复制
*
/****************************************************/
function notCopy() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x04); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					恢复允许复制
*
/****************************************************/
function okCopy() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x04 + 0x8000); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					禁止拖动
*
/****************************************************/
function notDrag() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x08); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					恢复拖动
*
/****************************************************/
function okDrag() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetSecurity(0x08 + 0x8000); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}

}
/****************************************************
*
*					修订文档
*
/****************************************************/
function ProtectRevision() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetTrackRevisions(1) 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏修订
*
/****************************************************/
function UnShowRevisions() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ShowRevisions(0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示当前修订
*
/****************************************************/
function ShowRevisions() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ShowRevisions(1);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					退出修订状态
*
/****************************************************/
function ExitRevisions() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetTrackRevisions(0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					接受当前所有修订
*
/****************************************************/
function AcceptAllRevisions() {
	try{
		var webObj=document.getElementById("WebOffice1");
 		document.all.WebOffice1.SetTrackRevisions(4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					拒绝当前所有修订
*
/****************************************************/
function unAcceptAllRevisions() {
	try{
		var webObj=document.getElementById("WebOffice1");
		var vCount = webObj.GetRevCount();
		var strUserName;
		for(var i=1;i<=vCount;i++){
			strUserName=webObj.GetRevInfo(i,0);
			document.all.WebOffice1.AcceptRevision(strUserName ,1)	
		}
		}catch(e){
			alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
		}
}
/****************************************************
*
*					获取修订相关信息
*
/****************************************************/
function GetRevAllInfo() {
var vCount;
vCount = document.all.WebOffice1.GetRevCount(); 
var vOpt = 0;
var vDate;
for(var i=1; i<= vCount; i++){
	vOpt = document.all.WebOffice1.GetRevInfo(i,2);
	if("1" == vOpt){
		vOpt = "插入";
	}else if("2" == vOpt){
		vOpt = "删除";
	}else{
		vOpt = "未知操作";
	}
	vDate = new String(document.all.WebOffice1.GetRevInfo(i,1));
	vDate = parseFloat(vDate); 
	dateObj = new Date(vDate);
  alert(dateObj.getYear()   + "年" + dateObj.getMonth() + 1 + "月" + dateObj.getDate() +"日" +  dateObj.getHours() +"时" +  dateObj.getMinutes() +"分" +  dateObj.getSeconds() +"秒" );
	alert("用户:"+document.all.WebOffice1.GetRevInfo(i,0) + "\r\n操作:" + vOpt + "\r\n内容:" + document.all.WebOffice1.GetRevInfo(i,3));
}
}
/****************************************************
*
*					设置当前操作用户
*
/****************************************************/
function SetUserName() {
	try{
		var webObj=document.getElementById("WebOffice1");
		if(document.all.UserName.value ==""){
			alert("用户名不可为空")
			document.all.UserName.focus();
			return false;
		}
 		webObj.SetCurrUserName(document.all.UserName.value);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					设置书签
*
/****************************************************/
function addBookmark() {

		var webObj=document.getElementById("WebOffice1");
		webObj.BookMarkOpt("/template/ListBookMarks.jsp",1);

}
/****************************************************
*
*					填充模板
*
/****************************************************/
function FillBookMarks(){
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.BookMarkOpt("/template/FillBookMarks.jsp",2);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏office2003文件菜单
*
/****************************************************/
function hideFileMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",1,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示office2003文件菜单
*
/****************************************************/
function showFileMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",1,4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏office2003编辑菜单
*
/****************************************************/
function hideEditMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",2,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示office2003编辑菜单
*
/****************************************************/
function showEditMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",2,4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏office2003新建按钮
*
/****************************************************/
function hideNewItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示office2003新建按钮
*
/****************************************************/
function showNewItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏office2003打开按钮
*
/****************************************************/
function hideOpenItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",2,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示office2003打开按钮
*
/****************************************************/
function showOpenItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",2,4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏office2003保存按钮
*
/****************************************************/
function hideSaveItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示office2003保存按钮
*
/****************************************************/
function showSaveItem() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,4);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					返回首页
*
/****************************************************/
function return_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.Close();
		window.opener.location.reload();
		window.close();
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					打开本地文件
*
/****************************************************/
function docOpen() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.LoadOriginalFile("open", "doc");
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					保存文档
*
/****************************************************/
function newSave() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.Save();
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					另存为文档
*
/****************************************************/
function SaveAsTo() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ShowDialog(84);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏菜单
*
/****************************************************/
function notMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",1,8);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示菜单
*
/****************************************************/
function okMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",1,11);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏常用工具栏
*
/****************************************************/
function notOfter() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,8);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示常用工具栏
*
/****************************************************/
function okOfter() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Standard",1,11);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					隐藏格式工具栏
*
/****************************************************/
function notFormat() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Formatting",1,8);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					显示格式工具栏
*
/****************************************************/
function okFormat() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Formatting",1,11);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}    
/****************************************************
*
*					套红及数据交互
*
/****************************************************/
function linkRed() {
		window.open("mark.html","newwindow",'height=768, width=1024, top=0, left=0, toolbar=yes,resizable=yes, menubar=yes,location=yes, status=yes');
} 
/****************************************************
*
*					上传文档
*
/****************************************************/
function SaveDoc(oid,docType) {
	try{
		var webObj=document.getElementById("WebOffice1");
		var returnValue;
		 if(myform.DocTitle.value ==""){
			alert("标题不可为空")
			myform.DocTitle.focus();
			return false;
		}
		webObj.HttpInit();			//初始化Http引擎
		// 添加相应的Post元素 
		webObj.HttpAddPostString("oid", oid);
		webObj.HttpAddPostString("title", encodeURIComponent(myform.DocTitle.value));
		webObj.HttpAddPostCurrFile("DocContent","");		// 上传文件
		returnValue = webObj.HttpPost("./savedoc.jsp");	// 判断上传是否成功
		if("succeed" == returnValue){
			alert("文件上传成功");	
		}else if("failed" == returnValue)
			alert("文件上传失败");
		return_onclick(); 
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					全屏
*
/****************************************************/
function bToolBar_FullScreen_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.FullScreen = true;
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*	设置weboffice自带工具栏“新建文档”显示或隐藏
*
/****************************************************/
function bToolBar_New_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		var vCurItem = document.all.WebOffice1.HideMenuItem(0);
		//根据vCurItem判断当前按钮是否显示
		if(vCurItem & 0x01){
			webObj.HideMenuItem(0x01); //Show it
		}else{
			webObj.HideMenuItem(0x01 + 0x8000); //Hide it
		}
		
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*	设置weboffice自带工具栏“打开文档”显示或隐藏
*
/****************************************************/
function bToolBar_Open_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		var vCurItem = webObj.HideMenuItem(0);
		//根据vCurItem判断当前按钮是否显示
		if(vCurItem & 0x02){
			webObj.HideMenuItem(0x02); //Show it
		}else{
			webObj.HideMenuItem(0x02 + 0x8000); //Hide it
		}
		
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*	设置weboffice自带工具栏“保存文档”显示或隐藏
*
/****************************************************/
function bToolBar_Save_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		var vCurItem = webObj.HideMenuItem(0);
		//根据vCurItem判断当前按钮是否显示
		if(vCurItem & 0x04){
			webObj.HideMenuItem(0x04); //Show it
		}else{
			webObj.HideMenuItem(0x04 + 0x8000); //Hide it
		}
		
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*		设置weboffice自带工具栏显示或隐藏
*
/****************************************************/
function bToolBar_onclick() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ShowToolBar =  !webObj.ShowToolBar;
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*			得到当前文档用户列表
*
/****************************************************/
function ReUserList_onclick()
{
	var webObj=document.getElementById("WebOffice1");
	var vCount = webObj.GetRevCount();
//1.Remove All
 	var selLen= document.all.UserList.length;
	for (i=0;i<selLen;i++){
		document.all.UserList.remove(0);
	}
//2.ReLoad All	 
	var vCount;
	vCount = webObj.GetRevCount();
		var  el1   =   document.createElement("OPTION");   
		el1.text  ="--请选择用户--";   
		document.all.UserList.options.add(el1);	 
	
	for(var i=1;i<=vCount;i++){
		var strUserName=webObj.GetRevInfo(i,0);
		var  el   =   document.createElement("OPTION");   
		el.text   =   strUserName;   
		el.value   =   strUserName;   
		document.all.UserList.options.add(el);	   
	}
}

/*************************************************
功能：在演示如何调用VBA接口
      WebOffice提供GetDocumentObject()的接口导出对象
      Word 导出的是：MSWord::_Document
      Excel导出的是: MSExcel::_Workbook
      WPS  导出的是: WPS::_Document
列子：
1.通过VBA获取当前用户的用户名
  document.all.WebOffice1.GetDocumentObject().Application.UserName;
2.获取文档的标题
	document.all.WebOffice1.GetDocumentObject().FullName;
**************************************************/

function TestVBA(){
	try{
		var webObj=document.getElementById("WebOffice1");
		var vObj = webObj.GetDocumentObject();
		if(!vObj){
			alert("获取对象失败，请核实您已经打开文档");
			return false;
		}
		var vUserName;
		var vFullName;
		var vDocType = webObj.DocType;
		if(11==vDocType){ //对于WOrd文件
				vUserName = vObj.Application.UserName;
				vFullName = vObj.Name;
		}else if(12==vDocType){  //对于Excel文件
				vUserName = vObj.Application.UserName;
				vFullName = vObj.Name;
		}else{
			alert("不支持的文件格式");
			return false;
		}
		alert("VBA测试结果\r\n用户名:"+vUserName+"\r\n文档名:"+vFullName+"\r\n可以参照代码调用任意的VBA功能");
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					接受修订
*
/****************************************************/
function AcceptRevision_onclick() {
	try{	
	var webObj=document.getElementById("WebOffice1");
		var strUserName=document.all.UserList.value;
		document.all.WebOffice1.AcceptRevision(strUserName ,0)	
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					拒绝修订
*
/****************************************************/
function unAcceptRevision_onclick() {
	try{	
		var webObj=document.getElementById("WebOffice1");
		var strUserName=document.all.UserList.value;
		webObj.AcceptRevision(strUserName ,1)	
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}

}
/****************************************************
*
*		显示或隐藏印章工具栏
*	 通过录制VBA查看工具栏的名称。
*	然后可以采用下面方式来显示或隐藏
*
/****************************************************/
function ShowToolBar_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		//通过Document->application->CommandBars 获取到菜单对象
		var vObj = webObj.GetDocumentObject().Application.CommandBars("电子印章");
		vObj.Visible = !vObj.Visible
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*					盖章
*
/****************************************************/
function AddSeal_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
   		 //通过Document->application->CommandBars 获取到菜单对象
  		var vObj = webObj.GetDocumentObject().Application.CommandBars("电子印章");
		if(vObj) vObj.Controls("盖章").Execute();
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*			Office2007菜单隐藏和恢复
*			----开始菜单隐藏
*
/****************************************************/
function beginMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x100000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*
*			Office2007菜单隐藏和恢复
*			---插入菜单隐藏
*
/****************************************************/
function insertMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x200000);
		webObj. HideMenuAction(5,0);//激活设置

	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			
*			Office2007菜单隐藏和恢复
*			---页面菜单隐藏
*
/****************************************************/
function pageMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x400000);
		webObj. HideMenuAction(5,0);//激活设置
	
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			--引用菜单隐藏
*
/****************************************************/
function adducMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x800000);
		webObj. HideMenuAction(5,0);//激活设置

	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---邮件菜单隐藏
*
/****************************************************/
function	emailMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x1000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---审阅菜单隐藏
*
/****************************************************/
function	checkMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x2000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---视图菜单隐藏
*
/****************************************************/
function	viewMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x4000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---开发工具菜单隐藏
*
/****************************************************/
function	empolderMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x8000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---加载项菜单隐藏
*
/****************************************************/
function	loadMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x10000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---全部菜单隐藏
*
/****************************************************/
function	allHideMenu_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.HideMenuAction(1,0x100000+0x200000+0x400000+0x800000+0x1000000+0x2000000+0x4000000+0x8000000+0x10000000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---复制无效
*
/****************************************************/
function nullityCopy_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj. HideMenuAction(1,0x2000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*			---粘贴无效
*
/****************************************************/
function nullityAffix_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj. HideMenuAction(1,0x1000);
		webObj. HideMenuAction(5,0);//激活设置
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			Office2007菜单隐藏和恢复
*	---恢复至设置之前状态（菜单显示，复制，粘贴可用）
*
/****************************************************/
function affixCopy_onclick()
{
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj. HideMenuAction(6,0);
	}catch(e){
		alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}
/****************************************************
*			
*	---电子印章
*
/****************************************************/


function hideSeal(){
	var obj;
	try{
        obj = new Object(document.all.WebOffice1.GetDocumentObject());
         if(obj !=null){
         obj.Application.CommandBars("电子印章").Visible = !obj.CommandBars("电子印章").Visible;
        
					}
	
	
	    delete obj;
    }catch(e){
    	alert("隐藏显示印章工具栏出错");
    	}
}

function write2(){
	var obj1;
	try{
        obj1 = new Object(document.all.WebOffice1.GetDocumentObject());
         if(obj1 !=null){
         obj1.Application.CommandBars("电子印章").Controls("盖章").Execute();
        
					}
	
	
	delete obj1;
	}catch(e){
    	alert("盖章出错");
    	}
}