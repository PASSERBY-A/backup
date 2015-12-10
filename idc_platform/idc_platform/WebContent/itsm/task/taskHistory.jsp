<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.ITSM_HOME%>/style.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=Consts.ITSM_HOME%>/js/itsm.js"></script>

	<STYLE>
		v\:* {BEHAVIOR: url(#default#VML)}
@media print {
   .notprint {
       display:none;
       }
}

@media screen {
   .notprint {
       display:inline;
       cursor:hand;
       }
}

	</style>
</head>
<body>
	<%
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String begin = sdf1.format(new java.util.Date());
		String end = sdf1.format(new java.util.Date());
	%>

	<script>


Ext.onReady(function() {
	<%
		PersonFieldInfo excuteUser = new PersonFieldInfo();
		excuteUser.setId("personId");
		excuteUser.setName("������");
		excuteUser.parse();
		out.println(excuteUser.getFormCode(150));
	%>

	var states = new Ext.form.ComboBox({
	    fieldLabel: '״̬',
	    hiddenName:'status',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        	['-1','����'],
						['0','��'],
						['1','�ر�'],
						['2','ǿ�ƹر�']
	        ]
	    }),
	    displayField:'name',
	    valueField: 'value',
	    mode: 'local',
	    triggerAction: 'all',
	    selectOnFocus:true,
	    width:100,
	    editable:false,
	    forceSelection :true,
			allowBlank:false
		});
	var combination = new Ext.form.ComboBox({
	    fieldLabel: '��Ϸ�ʽ',
	    hiddenName:'combination',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        	[' and ','��'],
						[' or ','��']
	        ]
	    }),
	    displayField:'name',
	    valueField: 'value',
	    mode: 'local',
	    triggerAction: 'all',
	    selectOnFocus:true,
	    forceSelection :true,
	    width:100,
	    editable:false,
			allowBlank:false
		});

	var hisSelForm = new Ext.FormPanel({
        labelAlign: 'top',
        frame:true,
        bodyBorder :false,
        border :false,
        width: 'auto',

        bodyStyle:'padding:5px 5px 0',
        items: [{
            layout:'column',
            border:false,
            items:[{
                columnWidth:.15,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'datefield',
                    fieldLabel: '��ʼʱ��',
                    name: 'begin',
                    format: 'Y-m-d',
                    height:'25',
                    width:'100'
                },{
                    xtype:'datefield',
                    fieldLabel: '����ʱ��',
                    name: 'end',
                    format: 'Y-m-d',
                    width :'100'
                }]
            },{
                columnWidth:.25,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'textfield',
                    fieldLabel: '�������',
                    name: 'serial',
                    width :'150'
                },{
                    xtype:'textfield',
                    fieldLabel: '�������������',
                    name: 'serialThird',
                    width :'150'
                }]
            },{
                columnWidth:.25,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'textfield',
                    fieldLabel: '����',
                    name: 'title',
                    width :'150'
                },fld_personId]
            },{
                columnWidth:.15,
                layout: 'form',
                border:false,
                items: [states,combination]
            }]
        }],
        buttons: [{
            text: '��ѯ',
            handler: function() {
            	ds.baseParams.begin = hisSelForm.form.findField("begin").getEl().dom.value;
            	ds.baseParams.end = hisSelForm.form.findField("end").getEl().dom.value;
            	ds.baseParams.title = hisSelForm.form.findField("title").getValue();
            	ds.baseParams.personId = hisSelForm.form.findField("fld_personId").getValue();
            	ds.baseParams.status = hisSelForm.form.findField("status").getValue();
            	ds.baseParams.serial = hisSelForm.form.findField("serial").getValue();
            	ds.baseParams.serialThird = hisSelForm.form.findField("serialThird").getValue();
            	ds.baseParams.combination = hisSelForm.form.findField("combination").getValue();

            	ds.reload();
            }
        }]
    });

    //hisSelForm.render('form');

    // ��������Դ
    var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '<%=Consts.ITSM_HOME%>/task/taskHistoryQuery.jsp'
        }),

				baseParams:{
					'begin':'<%=begin%>',
					'end':'<%=end%>',
					'personId':'',
					'status':'-1',
					'serial':'',
					'serialThird':'',
					'combination':' and ',
					'title':''
				},
        // create reader that reads the Topic records
        reader: new Ext.data.JsonReader({
            root: 'items',
            totalProperty: 'totalCount',
            id: 'oid'
        },[
        	"oid","tdOid","state","title","serial","crateTime","createBy","lastUpdate","excuteUser"
        ]),
        remoteSort: true
    });

    // ���ɱ��ͷ��Ϣ
    var cm = new Ext.grid.ColumnModel([
  			new Ext.grid.RowNumberer(),
  			{
  				header: "ID",
  				dataIndex: 'oid',
  				width: 80,
  				renderer: function(value, p, record){
  					if (record.data.tdOid!=-1)
					 		return String.format('{0}.{1}', value, record.data['tdOid']);
					 	return value;
					},
          css: 'white-space:normal;'},
      	{header: "״̬",dataIndex: 'state'},
      	{header: "����",dataIndex: 'title'},
      	{header: "�������",dataIndex: 'serial'},
      	{header: "����ʱ��",dataIndex: 'crateTime'},
      	{header: "������",dataIndex: 'createBy'},
      	{header: "������ʱ��",dataIndex: 'lastUpdate'},
      	{header: "��ǰ������",dataIndex: 'excuteUser'}
		]);

	// ���ɱ��
	var grid_taskHisList = new Ext.grid.GridPanel({
      ds: ds,
      cm: cm,
      border:false,
			viewConfig: {
				forceFit:true
			},
      selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
      enableColLock:false,
      loadMask: true,

		 	bbar: new Ext.PagingToolbar({
				store: ds,
				pageSize: <%=Consts.ITEMS_PER_PAGE%>,
				displayInfo: true,
				displayMsg: "<%=Consts.MSG_PAGE_DISPLAY%>",
				emptyMsg: "<%=Consts.MSG_PAGE_EMPTY%>"
				})
    });



	// ������˫�����������¼�
	grid_taskHisList.on("rowdblclick", function(grid, rowIndex, e)
	{
		var p = grid.getStore().getAt(rowIndex);
		taskHistoryView2({taskOid:p.get("oid")});
	});

    // ��ȡ��ʼ������
    ds.load({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});


	var viewport = new Ext.Viewport({
			layout:'border',
      items:[
      	{ // raw
            region:'north',
            //contentEl: 'form',
	          split:true,
	          height:150,
	          minSize: 60,
	          maxSize: 150,
	          collapsible: true,
	          collapseMode :'mini',
	          margins:'1 1 1 1',
	          items:hisSelForm
        },{
            region:'center',
            split:true,
            autoScroll:true,
            minSize: 175,
            maxSize: 400,
            layout:'fit',
            margins:'1 1 1 1',
            items:grid_taskHisList
     	}]
   });
});

</script>

</body>
</html>