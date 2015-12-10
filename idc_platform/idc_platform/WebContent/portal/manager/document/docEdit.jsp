<%@ page contentType="text/html;charset=GB2312"%>
<%@ page import="java.sql.*"%>
<%@page import="com.hp.idc.portal.bean.Document"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="java.io.File"%>
<html>
	<head>
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="js/main.js"></script>
		
<%
	String oid = "0";
	String docTitle = "";
	String doctype = "doc";
	String modDocId = "";
	String flag="";
	String sql = "";
	ResultSet rs = null;
	if (request.getParameter("modId") != null
			&& !request.getParameter("modId").equals("")) {
		modDocId = request.getParameter("modId");
	}
	if (request.getParameter("oid") != null
			&& !request.getParameter("oid").equals("")) {
		oid = request.getParameter("oid");
	}
	doctype = request.getParameter("doctype");
	
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	
	if (!oid.equals("0")) {
		Document bean = mgr.getBeanById(oid);
		doctype = bean.getFiletype();
		docTitle = bean.getName();
		File file = new File(bean.getFilepath()+bean.getFilename());
		System.out.println(file.exists());
		if(file.exists()){
%>
		<title>编辑正文</title>

		<!-- --------------------=== 调用Weboffice初始化方法 ===--------------------- -->

		<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyCtrlReady>
/****************************************************
*
*	在装载完Weboffice(执行<object>...</object>)
*	控件后执行 "WebOffice1_NotifyCtrlReady"方法
*
****************************************************/
	WebOffice1_NotifyCtrlReady()
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyWordEvent(eventname)>
<!--
 WebOffice1_NotifyWordEvent(eventname)
//-->
</SCRIPT>
		<SCRIPT LANGUAGE=javascript>
/****************************************************
*
*		控件初始化WebOffice方法
*
****************************************************/
function WebOffice1_NotifyCtrlReady() {
document.all.WebOffice1.OptionFlag |= 128;
<%	if(!oid.equals("0")) {	%>		// 装载已存在的文档
		document.all.WebOffice1.LoadOriginalFile("/getdoc.jsp?oid=<%=oid%>", "<%=doctype%>");

<%	} else if(!modDocId.equals("0")){		%>	//装载模板
		document.all.WebOffice1.LoadOriginalFile("/getModDoc.jsp?modId=<%=modDocId%>", "<%=doctype%>");
	
<%  }else{
	%>	// 新建文档
		document.all.WebOffice1.LoadOriginalFile("", "<%=doctype%>");
<%	}				%>
}
var flag=false;
function menuOnClick(id){
	var id=document.getElementById(id);
	var dis=id.style.display;
	if(dis!="none"){
		id.style.display="none";
		
	}else{
		id.style.display="block";
	}
}
/****************************************************
*
*		接收office事件处理方法
*
****************************************************/
var vNoCopy = 0;
var vNoPrint = 0;
var vNoSave = 0;
var vClose=0;
function no_copy(){
	vNoCopy = 1;
}
function yes_copy(){
	vNoCopy = 0;
}


function no_print(){
	vNoPrint = 1;
}
function yes_print(){
	vNoPrint = 0;
}


function no_save(){
	vNoSave = 1;
}
function yes_save(){
	vNoSave = 0;
}
function EnableClose(flag){
 vClose=flag;
}
function CloseWord(){
	
  document.all.WebOffice1.CloseDoc(0); 
}

function WebOffice1_NotifyWordEvent(eventname) {
	if(eventname=="DocumentBeforeSave"){
		if(vNoSave){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止保存");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="DocumentBeforePrint"){
		if(vNoPrint){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止打印");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="WindowSelectionChange"){
		if(vNoCopy){
			document.all.WebOffice1.lContinue = 0;
			//alert("此文档已经禁止复制");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else   if(eventname =="DocumentBeforeClose"){
	    if(vClose==0){
	    	document.all.WebOffice1.lContinue=0;
	    } else{
	    	alert("word");
		    document.all.WebOffice1.lContinue = 1;
		  }
 }
	//alert(eventname); 
}
</SCRIPT>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
	</head>
	<body style="background: #ccc;" onUnload="return window_onunload()">
		<center>
			<div align="center"
				style="width: 1024; height: 750px; background: #fff; margin: 50px 0 0 0; padding: 30px 0 0 0">

				<form name="myform">
					<table class="TableBlock" width="90%">
						<tr class="TableHeader">
							<td colspan="2">
								文档操作
							</td>
						</tr>
						<tr>
							<td class="TableData">
								标 题:
								<input name="DocTitle" value=<%if(!oid.equals("0")) { %>
									<%=docTitle%> <% } else { %> "Test"<% } %> size="14">
							</td>
							<td align="right">
								<input name="button" type=button onClick="javascript:window.close()" value="关闭">
								<input name="button93" type="button"
									onClick="return SaveDoc('<%=oid%>','<%=doctype%>')" value="确定保存"
									classs="rollout">
							</td>
						</tr>
					</table>
					<br>
					<table class="TableBlock" width="90%">
						<tr>
							<%--<td width="15%" valign="top" class="leftTableData">
								<div class="menuItem" onClick="menuOnClick('chc')">
									常用接口调用
								</div>
								<div id="chc" style="display: none">
									<!-- ---------------=== 该处文件格式的value不可以自定义 ===------------------------- -->
									<select name="doctype" size="1" id="doctype">
										<option value="doc" selected>
											Word
										</option>
										<option value="xls">
											Excel
										</option>
										<option value="wps" >
											wps
										</option>
									</select>

									<input name="CreateFile" type="button" class="btn"
										id="CreateFile" onClick="newDoc()" value="新建文档">

									<input name="button" type="button" class="btn"
										onClick="return docOpen()" value="打开本地文件" />

									<input name="CreateFile4" type="button" class="btn"
										id="showPrint" onClick="showPrintDialog()" value="显示对话框">

									<input name="zhiPrints" type="button" class="btn"
										id="zhiPrints" onClick="zhiPrint()" value="直接打印" />

									<input name="CreateFile2" type="button" class="btn"
										id="CreateFile2" onClick="newSave()" value="保存" />

									<input name="CreateFile3" type="button" class="btn"
										id="CreateFile3" onClick="SaveAsTo()" value="另存为" />
								</div>
								<div class="menuItem" onClick="menuOnClick('docSafe')">
									文档安全设置
								</div>
								<div id="docSafe" style="display: none">
									保护密码：
									<input name="docPwd" type="text" value="Password"
										style="width: 74px;" maxlength="10">

									<input type="button" class="btn" value="保护文档"
										onClick="return ProtectFull()">


									<input name="button3" type="button" class="btn"
										onClick="return UnProtect()" value="解除保护" />


									<input type="button" class="btn" value="禁用打印"
										onClick="return notPrint()">


									<input name="button3" type="button" class="btn"
										onClick="return okPrint()" value="启用打印" />


									<input name="button10" type="button" class="btn"
										onClick="return notSave()" value="禁止保存" />


									<input name="button32" type="button" class="btn"
										onClick="return okSave()" value="允许保存" />


									<input name="button11" type="button" class="btn"
										onClick="return notCopy()" value="禁止复制" />
									<input name="button33" type="button" class="btn"
										onClick="return okCopy()" value="允许复制" />
									<input name="but11" type="button" class="btn"
										onClick="return notDrag()" value="禁止拖动" />
									<input name="but33" type="button" class="btn"
										onClick="return okDrag()" value="允许拖动" />
									事件控制方式：
									<input type="button" class="btn" value="禁用打印"
										onClick="return no_print()">
										<input name="button3" type="button" class="btn"
										onClick="return yes_print()" value="启用打印" />				
									<input name="button10" type="button" class="btn"
										onClick="return no_save()" value="禁止保存" />


									<input name="button32" type="button" class="btn"
										onClick="return yes_save()" value="允许保存" />


									<input name="button11" type="button" class="btn"
										onClick="return no_copy()" value="禁止复制" />
									<input name="button33" type="button" class="btn"
										onClick="return yes_copy()" value="允许复制" />
									
								</div>
								<div class="menuItem" onClick="menuOnClick('recension')">
									修订操作
								</div>
								<div id="recension" style="display: none">
									<input name="UserName" type="text" value="Test"
										style="width: 74px;" maxlength="10" />

									<input name="button2" type="button" class="btn"
										onclick="return SetUserName()" value="设置用户" />

									<input name="button4" type="button" class="btn"
										onClick="return ProtectRevision()" value="修订文档" />

									<input name="button42" type="button" class="btn"
										onClick="return ExitRevisions()" value="退出修订" />

									<input name="button5" type="button" class="btn"
										onClick="return ShowRevisions()" value="显示修订" />

									<input name="button6" type="button" class="btn"
										onClick="return UnShowRevisions()" value="隐藏修订" />

									<input name="button7" type="button" class="btn"
										onClick="return AcceptAllRevisions()" value="接受所有修订" />

									<input name="button72" type="button" class="btn"
										onClick="return unAcceptAllRevisions()" value="拒绝所有修订" />

									<input name="button922" type="button" class="btn"
										onClick="return GetRevAllInfo()" value="获取修订信息" />
								</div>
								<div class="menuItem" onClick="menuOnClick('markset')">
									书签操作
								</div>
								<div id="markset" style="display: none">
									<input name="button8" type="button" class="btn"
										onClick="return addBookmark()" value="设置书签" />

									<input name="button2" type=button class="btn"
										onclick="FillBookMarks()" value="填充模版">

									<input name="CreateFile322" type="button" class="btn"
										id="CreateFile322" onClick="linkRed()" value="更多书签应用…" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03menu')">
									Office03菜单设置
								</div>
								<div id="off03menu" style="display: none">
									<input name="button12" type="button" class="btn"
										onClick="return notMenu()" value="隐藏菜单" />

									<input name="button12" type="button" class="btn"
										onClick="return okMenu()" value="显示菜单" />

									<input name="button12" type="button" class="btn"
										onClick="return notOfter()" value="隐藏常用" />

									<input name="button12" type="button" class="btn"
										onClick="return okOfter()" value="显示常用" />

									<input name="button12" type="button" class="btn"
										onClick="return notFormat()" value="隐藏格式" />

									<input name="button12" type="button" class="btn"
										onClick="return okFormat()" value="显示格式" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03menuItem')">
									Office03菜单项
								</div>
								<div id="off03menuItem" style="display: none">
									<input name="button122" type="button" class="btn"
										onClick="return hideFileMenu()" value="隐藏文件" />

									<input name="button123" type="button" class="btn"
										onClick="return showFileMenu()" value="显示文件" />

									<input name="button124" type="button" class="btn"
										onClick="return hideEditMenu()" value="隐藏编辑" />

									<input name="button125" type="button" class="btn"
										onClick="return showEditMenu()" value="显示编辑" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03tool')">
									Office03工具栏项
								</div>
								<div id="off03tool" style="display: none">
									<input name="button1222" type="button" class="btn"
										onClick="return hideNewItem()" value="隐藏新建" />

									<input name="button1232" type="button" class="btn"
										onClick="return showNewItem()" value="显示新建" />

									<input name="button1242" type="button" class="btn"
										onClick="return hideOpenItem()" value="隐藏打开" />

									<input name="button1252" type="button" class="btn"
										onClick="return showOpenItem()" value="显示打开" />

									<input name="button1242" type="button" class="btn"
										onClick="return hideSaveItem()" value="隐藏保存" />

									<input name="button1252" type="button" class="btn"
										onClick="return showSaveItem()" value="显示保存" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off07menu')">
									Office07菜单设置
								</div>
								<div id="off07menu" style="display: none">
									<input name="button12222" type="button" class="btn"
										onClick="return beginMenu_onclick()" value="隐藏开始" />

									<input name="button12322" type="button" class="btn"
										onClick="return insertMenu_onclick()" value="隐藏插入" />

									<input name="button12422" type="button" class="btn"
										onClick="return pageMenu_onclick()" value="隐藏页面布局" />

									<input name="button12522" type="button" class="btn"
										onClick="return adducMenu_onclick()" value="隐藏引用" />

									<input name="button12422" type="button" class="btn"
										onClick="return emailMenu_onclick()" value="隐藏邮件" />

									<input name="button12522" type="button" class="btn"
										onClick="return checkMenu_onclick()" value="隐藏审阅" />

									<input name="button125222" type="button" class="btn"
										onClick="return viewMenu_onclick()" value="隐藏视图" />

									<input name="button124222" type="button" class="btn"
										onClick="return empolderMenu_onclick()" value="隐藏开发工具" />

									<input name="button125222" type="button" class="btn"
										onClick="return loadMenu_onclick()" value="隐藏加载项" />
									<input name="button1242222" type="button" class="btn"
										onClick="return allHideMenu_onclick()" value="隐藏全部" />

									<input name="button12422222" type="button" class="btn"
										onClick="return nullityCopy_onclick()" value="复制无效" />

									<input name="button12422223" type="button" class="btn"
										onClick="return nullityAffix_onclick()" value="粘贴无效" />

									<input name="button1252222" type="button" class="btn"
										onClick="return affixCopy_onclick()" value="恢复" />
								</div>

								<div class="menuItem" onClick="menuOnClick('webofficeTool')">
									weboffice工具栏
								</div>
								<div id="webofficeTool" style="display: none">
									<input name="bToolBar" type="button" class="btn"
										value="工具栏(隐/显)" LANGUAGE=javascript
										onClick="return bToolBar_onclick()">

									<input name="bToolBar_New" type="button" class="btn"
										value="新建文档(隐/显)" LANGUAGE=javascript
										onClick="return bToolBar_New_onclick()">

									<input name="bToolBar_Open" type="button" class="btn"
										value="打开文档(隐/显)" LANGUAGE=javascript
										onClick="return bToolBar_Open_onclick()">

									<input name="bToolBar_Save" type="button" class="btn"
										value="保存文档(隐/显)" LANGUAGE=javascript
										onClick="return bToolBar_Save_onclick()">
								</div>
								<div class="menuItem" onClick="menuOnClick('other')">
									其它
								</div>
								<div id="other" style="display: none">
									<input name="bToolBar_FullScreen" type="button" class="btn"
										value="全  屏" LANGUAGE=javascript
										onClick="return bToolBar_FullScreen_onclick()">

									<input name="CreateFile32" type="button" class="btn"
										id="CreateFile32" onClick="TestVBA()" value="VBA调用" />
								</div>
								<div class="menuItem" onClick="menuOnClick('seal')">
									电子印章
								</div>
								<div id="seal" style="display: none">
									<input name="seal" type="button" class="btn"
										value="显示隐藏电子印章工具栏" LANGUAGE=javascript
										onClick="return hideSeal()">

									<input  type="button" class="btn"
										onClick="return write2()" value="盖章" />
								</div>
								<div class="menuItem" onClick="menuOnClick('word')">
									屏蔽word关闭按钮
								</div>
								<div id="word" style="display: none">
									<input name="EnableClose1" type="button" class="btn"
										value="禁用Word关闭" 	onClick="return EnableClose(0)">
								
									<input name="EnableClose2" type="button" class="btn"
										value="启用Word关闭" 	onClick="return EnableClose(1)">
									<input name="closeWord" type="button" class="btn"
										value="关闭Word" 	onClick="return CloseWord()">
								</div>
							</td>
							--%><td width="85%" valign="top" class="TableData">

								<!-- -----------------------------== 装载weboffice控件 ==--------------------------------- -->
							<script src="js/LoadWebOffice.js"></script>

									
								<!-- --------------------------------== 结束装载控件 ==----------------------------------- -->
							</td>
						</tr>
					</table>
				</form>
			</div>
		</center>
	</body>
</html>
<%
		}else{
			out.println("</SCRIPT>");
			out.println("</head>");
%>
<body>
<script type="text/javascript">
alert("文件不在，无法编辑，请联系管理员！！");
window.close();
</script>
</body>
</html>
<%
		}
	}
%>