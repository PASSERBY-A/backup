<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>业务管理系统-公告查看</title>
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
						fieldLabel:'公告标题',
						width:500,  
						anchor:'98%',
						readOnly:true,
						value:'<s:property value="bulletinInfo.title" escape="false"/>'
					},
					{
						xtype:'htmleditor',
						id:'bulletinContent',
						name:'bulletinInfo.content',
						fieldLabel:'公告内容',
			            height:200,
			            anchor:'98%',
			            disabled:true,
			            value:'<s:property value="bulletinInfo.content" escape="false"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinCreator',
						fieldLabel:'创建人',
						anchor:'98%',
						readOnly:true,
						value:'<s:property value="bulletinInfo.creator" escape="false"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinCreateDate',
						fieldLabel:'创建日期',
						anchor:'98%',
						readOnly:true,
						value:'<s:date name="bulletinInfo.createdDate" format="yyyy-MM-dd"/>'
					},
					{
						xtype:'textfield',
						id:'bulletinBeginTime',
						fieldLabel:'生效日期',
						anchor:'98%',
						readOnly:true,
						value:'<s:date name="bulletinInfo.beginTime" format="yyyy-MM-dd"/>'
					},
					{
						xtype:'textfield',
						id:'bulletin失效Time',
						fieldLabel:'失效日期',
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
                <td colspan="2" class="yd_title" height="20">公告信息</td>
        </tr>
	<tr>
		<td width="20%" height="30" class="yd_ltd">&nbsp;&nbsp;标题</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.title" escape="true"/></td>
	</tr>
 	<tr>
		<td width="20%" height="30" class="yd_ltd">&nbsp;&nbsp;发布者</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.creator.userName" escape="true"/></td>
	</tr>
	<tr>
		<td  height="30" class="yd_ltd">&nbsp;&nbsp;创建时间</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.createdDate" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td valign="top" class="yd_ltd" >&nbsp;&nbsp;内容</td>
		<td class="yd_rtd">&nbsp;<s:property value="bulletinInfo.content" escape="false"/></td>
	</tr>
	<tr>
		<td height="30" class="yd_ltd">&nbsp;&nbsp;生效时间</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.beginTime" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td  height="30" class="yd_ltd">&nbsp;&nbsp;失效时间</td>
		<td class="yd_rtd">&nbsp;<s:date name="bulletinInfo.endTime" format="yyyy-MM-dd"/></td>
	</tr>   
   </table>
    -->
</body>
</html>
