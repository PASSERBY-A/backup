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
		<title>�༭����</title>

		<!-- --------------------=== ����Weboffice��ʼ������ ===--------------------- -->

		<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyCtrlReady>
/****************************************************
*
*	��װ����Weboffice(ִ��<object>...</object>)
*	�ؼ���ִ�� "WebOffice1_NotifyCtrlReady"����
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
*		�ؼ���ʼ��WebOffice����
*
****************************************************/
function WebOffice1_NotifyCtrlReady() {
document.all.WebOffice1.OptionFlag |= 128;
<%	if(!oid.equals("0")) {	%>		// װ���Ѵ��ڵ��ĵ�
		document.all.WebOffice1.LoadOriginalFile("/getdoc.jsp?oid=<%=oid%>", "<%=doctype%>");

<%	} else if(!modDocId.equals("0")){		%>	//װ��ģ��
		document.all.WebOffice1.LoadOriginalFile("/getModDoc.jsp?modId=<%=modDocId%>", "<%=doctype%>");
	
<%  }else{
	%>	// �½��ĵ�
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
*		����office�¼�������
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
			alert("���ĵ��Ѿ���ֹ����");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="DocumentBeforePrint"){
		if(vNoPrint){
			document.all.WebOffice1.lContinue = 0;
			alert("���ĵ��Ѿ���ֹ��ӡ");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="WindowSelectionChange"){
		if(vNoCopy){
			document.all.WebOffice1.lContinue = 0;
			//alert("���ĵ��Ѿ���ֹ����");
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
								�ĵ�����
							</td>
						</tr>
						<tr>
							<td class="TableData">
								�� ��:
								<input name="DocTitle" value=<%if(!oid.equals("0")) { %>
									<%=docTitle%> <% } else { %> "Test"<% } %> size="14">
							</td>
							<td align="right">
								<input name="button" type=button onClick="javascript:window.close()" value="�ر�">
								<input name="button93" type="button"
									onClick="return SaveDoc('<%=oid%>','<%=doctype%>')" value="ȷ������"
									classs="rollout">
							</td>
						</tr>
					</table>
					<br>
					<table class="TableBlock" width="90%">
						<tr>
							<%--<td width="15%" valign="top" class="leftTableData">
								<div class="menuItem" onClick="menuOnClick('chc')">
									���ýӿڵ���
								</div>
								<div id="chc" style="display: none">
									<!-- ---------------=== �ô��ļ���ʽ��value�������Զ��� ===------------------------- -->
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
										id="CreateFile" onClick="newDoc()" value="�½��ĵ�">

									<input name="button" type="button" class="btn"
										onClick="return docOpen()" value="�򿪱����ļ�" />

									<input name="CreateFile4" type="button" class="btn"
										id="showPrint" onClick="showPrintDialog()" value="��ʾ�Ի���">

									<input name="zhiPrints" type="button" class="btn"
										id="zhiPrints" onClick="zhiPrint()" value="ֱ�Ӵ�ӡ" />

									<input name="CreateFile2" type="button" class="btn"
										id="CreateFile2" onClick="newSave()" value="����" />

									<input name="CreateFile3" type="button" class="btn"
										id="CreateFile3" onClick="SaveAsTo()" value="���Ϊ" />
								</div>
								<div class="menuItem" onClick="menuOnClick('docSafe')">
									�ĵ���ȫ����
								</div>
								<div id="docSafe" style="display: none">
									�������룺
									<input name="docPwd" type="text" value="Password"
										style="width: 74px;" maxlength="10">

									<input type="button" class="btn" value="�����ĵ�"
										onClick="return ProtectFull()">


									<input name="button3" type="button" class="btn"
										onClick="return UnProtect()" value="�������" />


									<input type="button" class="btn" value="���ô�ӡ"
										onClick="return notPrint()">


									<input name="button3" type="button" class="btn"
										onClick="return okPrint()" value="���ô�ӡ" />


									<input name="button10" type="button" class="btn"
										onClick="return notSave()" value="��ֹ����" />


									<input name="button32" type="button" class="btn"
										onClick="return okSave()" value="������" />


									<input name="button11" type="button" class="btn"
										onClick="return notCopy()" value="��ֹ����" />
									<input name="button33" type="button" class="btn"
										onClick="return okCopy()" value="������" />
									<input name="but11" type="button" class="btn"
										onClick="return notDrag()" value="��ֹ�϶�" />
									<input name="but33" type="button" class="btn"
										onClick="return okDrag()" value="�����϶�" />
									�¼����Ʒ�ʽ��
									<input type="button" class="btn" value="���ô�ӡ"
										onClick="return no_print()">
										<input name="button3" type="button" class="btn"
										onClick="return yes_print()" value="���ô�ӡ" />				
									<input name="button10" type="button" class="btn"
										onClick="return no_save()" value="��ֹ����" />


									<input name="button32" type="button" class="btn"
										onClick="return yes_save()" value="������" />


									<input name="button11" type="button" class="btn"
										onClick="return no_copy()" value="��ֹ����" />
									<input name="button33" type="button" class="btn"
										onClick="return yes_copy()" value="������" />
									
								</div>
								<div class="menuItem" onClick="menuOnClick('recension')">
									�޶�����
								</div>
								<div id="recension" style="display: none">
									<input name="UserName" type="text" value="Test"
										style="width: 74px;" maxlength="10" />

									<input name="button2" type="button" class="btn"
										onclick="return SetUserName()" value="�����û�" />

									<input name="button4" type="button" class="btn"
										onClick="return ProtectRevision()" value="�޶��ĵ�" />

									<input name="button42" type="button" class="btn"
										onClick="return ExitRevisions()" value="�˳��޶�" />

									<input name="button5" type="button" class="btn"
										onClick="return ShowRevisions()" value="��ʾ�޶�" />

									<input name="button6" type="button" class="btn"
										onClick="return UnShowRevisions()" value="�����޶�" />

									<input name="button7" type="button" class="btn"
										onClick="return AcceptAllRevisions()" value="���������޶�" />

									<input name="button72" type="button" class="btn"
										onClick="return unAcceptAllRevisions()" value="�ܾ������޶�" />

									<input name="button922" type="button" class="btn"
										onClick="return GetRevAllInfo()" value="��ȡ�޶���Ϣ" />
								</div>
								<div class="menuItem" onClick="menuOnClick('markset')">
									��ǩ����
								</div>
								<div id="markset" style="display: none">
									<input name="button8" type="button" class="btn"
										onClick="return addBookmark()" value="������ǩ" />

									<input name="button2" type=button class="btn"
										onclick="FillBookMarks()" value="���ģ��">

									<input name="CreateFile322" type="button" class="btn"
										id="CreateFile322" onClick="linkRed()" value="������ǩӦ�á�" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03menu')">
									Office03�˵�����
								</div>
								<div id="off03menu" style="display: none">
									<input name="button12" type="button" class="btn"
										onClick="return notMenu()" value="���ز˵�" />

									<input name="button12" type="button" class="btn"
										onClick="return okMenu()" value="��ʾ�˵�" />

									<input name="button12" type="button" class="btn"
										onClick="return notOfter()" value="���س���" />

									<input name="button12" type="button" class="btn"
										onClick="return okOfter()" value="��ʾ����" />

									<input name="button12" type="button" class="btn"
										onClick="return notFormat()" value="���ظ�ʽ" />

									<input name="button12" type="button" class="btn"
										onClick="return okFormat()" value="��ʾ��ʽ" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03menuItem')">
									Office03�˵���
								</div>
								<div id="off03menuItem" style="display: none">
									<input name="button122" type="button" class="btn"
										onClick="return hideFileMenu()" value="�����ļ�" />

									<input name="button123" type="button" class="btn"
										onClick="return showFileMenu()" value="��ʾ�ļ�" />

									<input name="button124" type="button" class="btn"
										onClick="return hideEditMenu()" value="���ر༭" />

									<input name="button125" type="button" class="btn"
										onClick="return showEditMenu()" value="��ʾ�༭" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off03tool')">
									Office03��������
								</div>
								<div id="off03tool" style="display: none">
									<input name="button1222" type="button" class="btn"
										onClick="return hideNewItem()" value="�����½�" />

									<input name="button1232" type="button" class="btn"
										onClick="return showNewItem()" value="��ʾ�½�" />

									<input name="button1242" type="button" class="btn"
										onClick="return hideOpenItem()" value="���ش�" />

									<input name="button1252" type="button" class="btn"
										onClick="return showOpenItem()" value="��ʾ��" />

									<input name="button1242" type="button" class="btn"
										onClick="return hideSaveItem()" value="���ر���" />

									<input name="button1252" type="button" class="btn"
										onClick="return showSaveItem()" value="��ʾ����" />
								</div>
								<div class="menuItem" onClick="menuOnClick('off07menu')">
									Office07�˵�����
								</div>
								<div id="off07menu" style="display: none">
									<input name="button12222" type="button" class="btn"
										onClick="return beginMenu_onclick()" value="���ؿ�ʼ" />

									<input name="button12322" type="button" class="btn"
										onClick="return insertMenu_onclick()" value="���ز���" />

									<input name="button12422" type="button" class="btn"
										onClick="return pageMenu_onclick()" value="����ҳ�沼��" />

									<input name="button12522" type="button" class="btn"
										onClick="return adducMenu_onclick()" value="��������" />

									<input name="button12422" type="button" class="btn"
										onClick="return emailMenu_onclick()" value="�����ʼ�" />

									<input name="button12522" type="button" class="btn"
										onClick="return checkMenu_onclick()" value="��������" />

									<input name="button125222" type="button" class="btn"
										onClick="return viewMenu_onclick()" value="������ͼ" />

									<input name="button124222" type="button" class="btn"
										onClick="return empolderMenu_onclick()" value="���ؿ�������" />

									<input name="button125222" type="button" class="btn"
										onClick="return loadMenu_onclick()" value="���ؼ�����" />
									<input name="button1242222" type="button" class="btn"
										onClick="return allHideMenu_onclick()" value="����ȫ��" />

									<input name="button12422222" type="button" class="btn"
										onClick="return nullityCopy_onclick()" value="������Ч" />

									<input name="button12422223" type="button" class="btn"
										onClick="return nullityAffix_onclick()" value="ճ����Ч" />

									<input name="button1252222" type="button" class="btn"
										onClick="return affixCopy_onclick()" value="�ָ�" />
								</div>

								<div class="menuItem" onClick="menuOnClick('webofficeTool')">
									weboffice������
								</div>
								<div id="webofficeTool" style="display: none">
									<input name="bToolBar" type="button" class="btn"
										value="������(��/��)" LANGUAGE=javascript
										onClick="return bToolBar_onclick()">

									<input name="bToolBar_New" type="button" class="btn"
										value="�½��ĵ�(��/��)" LANGUAGE=javascript
										onClick="return bToolBar_New_onclick()">

									<input name="bToolBar_Open" type="button" class="btn"
										value="���ĵ�(��/��)" LANGUAGE=javascript
										onClick="return bToolBar_Open_onclick()">

									<input name="bToolBar_Save" type="button" class="btn"
										value="�����ĵ�(��/��)" LANGUAGE=javascript
										onClick="return bToolBar_Save_onclick()">
								</div>
								<div class="menuItem" onClick="menuOnClick('other')">
									����
								</div>
								<div id="other" style="display: none">
									<input name="bToolBar_FullScreen" type="button" class="btn"
										value="ȫ  ��" LANGUAGE=javascript
										onClick="return bToolBar_FullScreen_onclick()">

									<input name="CreateFile32" type="button" class="btn"
										id="CreateFile32" onClick="TestVBA()" value="VBA����" />
								</div>
								<div class="menuItem" onClick="menuOnClick('seal')">
									����ӡ��
								</div>
								<div id="seal" style="display: none">
									<input name="seal" type="button" class="btn"
										value="��ʾ���ص���ӡ�¹�����" LANGUAGE=javascript
										onClick="return hideSeal()">

									<input  type="button" class="btn"
										onClick="return write2()" value="����" />
								</div>
								<div class="menuItem" onClick="menuOnClick('word')">
									����word�رհ�ť
								</div>
								<div id="word" style="display: none">
									<input name="EnableClose1" type="button" class="btn"
										value="����Word�ر�" 	onClick="return EnableClose(0)">
								
									<input name="EnableClose2" type="button" class="btn"
										value="����Word�ر�" 	onClick="return EnableClose(1)">
									<input name="closeWord" type="button" class="btn"
										value="�ر�Word" 	onClick="return CloseWord()">
								</div>
							</td>
							--%><td width="85%" valign="top" class="TableData">

								<!-- -----------------------------== װ��weboffice�ؼ� ==--------------------------------- -->
							<script src="js/LoadWebOffice.js"></script>

									
								<!-- --------------------------------== ����װ�ؿؼ� ==----------------------------------- -->
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
alert("�ļ����ڣ��޷��༭������ϵ����Ա����");
window.close();
</script>
</body>
</html>
<%
		}
	}
%>