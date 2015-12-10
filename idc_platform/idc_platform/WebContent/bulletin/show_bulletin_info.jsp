<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>ҵ�����ϵͳ-����鿴</title>
<%@ include file="/common/inc/header.jsp" %>
<LINK href="css/oa.css" rel=stylesheet>
<script language="javascript">
	Ext.override(Ext.form.HtmlEditor, {  
	    onDisable: function(){  
	        if(this.rendered){    
	            var roMask = this.wrap.mask();  
	            roMask.dom.style.filter = "alpha(opacity=0);"; //IE  
	            roMask.dom.style.opacity = "0"; //Mozilla  
	            roMask.dom.style.background = "white";  
	            roMask.dom.style.overflow = "scroll";
	        }  
	        Ext.form.HtmlEditor.superclass.onDisable.call(this);  
	    },  
	    onEnable: function(){  
	        if(this.rendered){  
	            this.wrap.unmask();  
	        }  
	        Ext.form.HtmlEditor.superclass.onEnable.call(this);  
	    }  
	}); 
	Ext.onReady(function(){
		var form = new Ext.form.FormPanel({
			frame:true,
			labelAlign : 'right',
			labelWidth :80,
			items:[
				
					{
						xtype:'textfield',
						id:'bulletinTitle',
						name:'bulletinInfo.title',
						fieldLabel:'�������',
						width:500,  
						anchor:'98%',
						readOnly:true,
						value:'<s:property value="bulletinInfo.title" escape="false"/>'
					},
					{
						xtype:'htmleditor',
						id:'bulletinContent',
						name:'bulletinInfo.content',
						fieldLabel:'��������',
			            height:200,
			            anchor:'98%',
			            disabled:true,
			            value:'<s:property value="bulletinInfo.content" escape="false"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinCreator',
						fieldLabel:'������',
						anchor:'98%',
						readOnly:true,
						value:'<s:property value="bulletinInfo.creator" escape="false"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinCreateDate',
						fieldLabel:'��������',
						anchor:'98%',
						readOnly:true,
						value:'<s:date name="bulletinInfo.createdDate" format="yyyy-MM-dd"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinBeginTime',
						fieldLabel:'��Ч����',
						anchor:'98%',
						readOnly:true,
						value:'<s:date name="bulletinInfo.beginTime" format="yyyy-MM-dd"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinʧЧTime',
						fieldLabel:'ʧЧ����',
						anchor:'98%',
						readOnly:true,
						value:'<s:date name="bulletinInfo.endTime" format="yyyy-MM-dd"/>'
					}
					
			]
		});
		
		var viewport = new Ext.Viewport({ 
    		renderTo:Ext.getBody(),
    		layout:'fit',
    		items:[form]
   	 	});
	});
</script>
</head>
<body>
	<!-- 
   <table width="100%" height="100%" cellSpacing="1" cellPadding="0" align=center class="yd_table">
        <tr>
                <td colspan="2" class="yd_title" height="20">������Ϣ</td>
        </tr>
	<tr>
		<td width="20%" height="30" class="yd_ltd">&nbsp;&nbsp;����</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.title" escape="true"/></td>
	</tr>
 	<tr>
		<td width="20%" height="30" class="yd_ltd">&nbsp;&nbsp;������</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.creator.userName" escape="true"/></td>
	</tr>
	<tr>
		<td  height="30" class="yd_ltd">&nbsp;&nbsp;����ʱ��</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.createdDate" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td valign="top" class="yd_ltd" >&nbsp;&nbsp;����</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.content" escape="false"/></td>
	</tr>
	<tr>
		<td height="30" class="yd_ltd">&nbsp;&nbsp;��Чʱ��</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.beginTime" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td  height="30" class="yd_ltd">&nbsp;&nbsp;ʧЧʱ��</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.endTime" format="yyyy-MM-dd"/></td>
	</tr>   
   </table>
    -->
</body>
</html>
