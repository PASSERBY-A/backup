<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<%
int oid = -1;
if (request.getParameter("id")!=null && !request.getParameter("id").equals(""))
	oid = Integer.parseInt(request.getParameter("id"));
int module = -1;
if (request.getParameter("module")!=null && !request.getParameter("module").equals(""))
	module = Integer.parseInt(request.getParameter("module"));
String formCfgXML = request.getParameter("cfgXML");
String callBack = request.getParameter("callBack");

boolean storeToDB = true;
FormInfo form = null;
if (formCfgXML!=null && !formCfgXML.equals("")) {
	//Document doc = XmlUtil.parseString(formCfgXML);
	form = FormInfo.parse(XmlUtil.parseString(formCfgXML).getRootElement());//.getFormByOid(oid);
	storeToDB = false;
} else
	form = FormManager.getFormByOid(oid);
int width = 175;
%>

<div id="fieldAttribute_content">
	<iframe id="fieldAttribute_iframe" name="fieldAttribute_iframe" src="" width="100%" height="100%"  frameborder=0></iframe>
</div>
<script defer>
	var fieldDs = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/field/fieldQuery.jsp'
    }),
		baseParams: { "module": <%=module%>,"origin":"","includeAll":"true"},
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount',
        id: 'id'
    }, [
        {name: 'id', mapping: 'id'},
        {name: 'value', mapping: 'name'},
        {name: 'origin', mapping: 'origin'},
        {name: 'category', mapping: 'category'}
    ]),
		sortInfo:{field: 'name', direction: 'ASC'},
    remoteSort: true
  });

function formatBoolean(value){
	return value ? '是' : '否';
};

function formatField(value,p,record){
	for (var i = 0; i < fieldDs.getCount(); i++)
		if (fieldDs.getAt(i).get("id")  == value){
			record.data['origin'] = fieldDs.getAt(i).get("origin");
			if (record.data['fieldname'] == '')
				record.data['fieldname'] = fieldDs.getAt(i).get("value");
		}
	return value;
};

var fieldGrid = new Ext.grid.GridPanel({
	ds:fieldDs,
	cm:new Ext.grid.ColumnModel([{
			header: "ID",
			sortable: true,
		  dataIndex: 'id'
	  },
		{
			header: "名称",
			sortable: true,
		  dataIndex: 'value'
		}
	]),
	region:'west',
	title:'字段',
	loadMask: true,
	collapsed :true,
	collapsible :true,
	width:200,
	listeners:{
		rowdblclick:function(g,rowIndex,e){
			var record = this.store.getAt(rowIndex);
			var p = new Plant({
				fieldid: record.get("id"),
				fieldname: record.get("value"),
				readonly: false,
				notnull: true,
				injectData: true,
				filter: '',
				advance:''
			});
			ds.add(p);
		}
	}
});

var cm = new Ext.grid.ColumnModel([{
	   header: "字段ID",
	   dataIndex: 'fieldid',
	   width: 100,
	   renderer: formatField
	},{
	   header: "字段名称",
	   dataIndex: 'fieldname',
	   editor: new Ext.form.TextField(),
	   width: 90
	},{
	   header: "所属",
	   dataIndex: 'origin',
	   width: 35
	},{
	   header: "只读",
	   dataIndex: 'readonly',
	   width: 35,
	   renderer: formatBoolean,
	   editor: new Ext.grid.GridEditor(new Ext.form.Checkbox())
	},{
	   header: "必填",
	   dataIndex: 'notnull',
	   renderer: formatBoolean,
	   width: 35,
	   editor: new Ext.grid.GridEditor(new Ext.form.Checkbox())
	},{
	   header: "填充",
	   dataIndex: 'injectData',
	   renderer: formatBoolean,
	   width: 35,
	   editor: new Ext.grid.GridEditor(new Ext.form.Checkbox())
	},{
	   header: "条件",
	   dataIndex: 'filter',
	   width: 100,
	   editor: new Ext.grid.GridEditor(new Ext.form.TextField({
		   allowBlank: true
	   }))
	},{
	   header: "高级",
	   dataIndex: 'advance',
	   width: 35
	}]);

var dsData = [
<%
if (form != null)
{
	List lfs = form.getFields();
	for (int i = 0; i < lfs.size(); i++)
	{
		FieldInfo info = (FieldInfo)lfs.get(i);
		if (i > 0)
			out.print(",");
		out.println("['" + info.getId() + "'");
		out.println(",'" + info.getName() + "'");
		out.println(",'"+ info.getType() +"'");
		out.println("," + (info.isReadOnly() ? "true" : "false"));
		out.println("," + (info.isNotNull() ? "true" : "false"));
		out.println("," + (info.isInjectData() ? "true" : "false"));
		out.println(",\"" + StringUtil.escapeHtml(info.getFilter()) + "\"");
		out.println(",'"+info.getOrigin()+"'");
		out.println(",'"+StringUtil.escapeJavaScript(info.getRegulation())+"'");
		if (info instanceof StringFieldInfo) {
			out.println(",'"+((StringFieldInfo)info).getRowNum()+"'");
		} else
			out.println(",''");
		if (info instanceof PersonFieldInfo) {
			out.println(",'"+((PersonFieldInfo)info).getGroupPersonBy()+"'");
		} else
			out.println(",''");
		out.println("]");
	}
}

%>];
var ds = new Ext.data.SimpleStore({
	id:0,
	fields: ['fieldid','fieldname','type', 'readonly', 'notnull', 'injectData', 'filter','origin','advance','rowNum','groupBy'],
	data: []});

var Plant = Ext.data.Record.create([
	   {name: 'fieldid', type: 'string'},
	   {name: 'fieldname', type: 'string'},
	   {name: 'type', type: 'string'},
	   {name: 'readonly', type: 'bool'},
	   {name: 'notnull', type: 'bool'},
	   {name: 'injectData', type: 'bool'},
	   {name: 'filter', type: 'string'},
	   {name: 'rowNum', type: 'string'},
	   {name: 'groupBy', type: 'string'},
	   {name: 'origin', type: 'string'},
	   {name: 'advance', type: 'string'}
  ]);


var toolbar = new Ext.Toolbar(
{
items:[
	new Ext.Toolbar.Button({
		text: '删除',
		handler: function()
		{
			var cell = grid_formFieldList.getSelectionModel().getSelected();
			if (cell != null)
				ds.remove(cell);
		}
	}),
	'-',
	new Ext.Toolbar.Button({
		text: '上移',
		handler: function()
		{
			var cell = grid_formFieldList.getSelectionModel().getSelected();
			if (cell == null)
				return;
			var n = ds.indexOf(cell);
			if (n == 0)
				return;
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n - 1, r);
			grid_formFieldList.getSelectionModel().selectRow(n - 1, cell);
		}
	}),
	'-',
	new Ext.Toolbar.Button({
		text: '下移',
		handler: function()
		{
			var cell = grid_formFieldList.getSelectionModel().getSelected();
			if (cell == null)
				return;
			var n = ds.indexOf(cell);
			if (n == ds.getCount() - 1)
				return;
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n + 1, r);
			grid_formFieldList.getSelectionModel().selectRow(n + 1, cell);
		}
	}),
	'-',
	new Ext.Toolbar.Button({
		text: '高级',
		handler: function()
		{
			var r = grid_formFieldList.getSelectionModel().getSelected();
			if (r == null) {
				Ext.MessageBox.alert("提示","首先要选择一行数据");
				return;
			}
			Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/form/formAdvance.jsp",{'fieldId' : r.get("fieldid"), 'fieldRule': r.get("advance")});
		}
	})
]});

// create the editor grid
var grid_formFieldList = new Ext.grid.EditorGridPanel({
	ds:ds,
	cm: cm,
	region:'center',
	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColLock:false,
	loadMask: true,
	//anchor: '100% 100%',
	tbar: toolbar,
	listeners:{
		rowclick:function(g,rowIndex,e){
			var record = this.store.getAt(rowIndex);
			fieldAttribute.find("name","id")[0].setValue(record.get("fieldid"));
			fieldAttribute.find("name","name")[0].setValue(record.get("fieldname"));
			fieldAttribute.find("name","readOnly")[0].setValue(record.get("readonly"));
			fieldAttribute.find("name","notNull")[0].setValue(record.get("notnull"));
			fieldAttribute.find("name","injectData")[0].setValue(record.get("injectData"));
			fieldAttribute.find("name","filter")[0].setValue(record.get("filter"));
			fieldAttribute.find("name","rowNum")[0].setValue(record.get("rowNum"));
			fieldAttribute.find("hiddenName","groupBy")[0].setValue(record.get("groupBy"));
			if (record.get("type") == "com.hp.idc.itsm.configure.fields.StringFieldInfo")
				fieldAttribute.find("name","rowNum")[0].enable();
			else
				fieldAttribute.find("name","rowNum")[0].disable();
			
			if (record.get("type") == "com.hp.idc.itsm.configure.fields.PersonFieldInfo")
				fieldAttribute.find("hiddenName","groupBy")[0].enable();
			else
				fieldAttribute.find("hiddenName","groupBy")[0].disable();
			
			fieldAttribute.find("name","advance")[0].setValue(record.get("advance"));
		}
	}
});


var fieldAttribute = new Ext.Panel({
  layout:'form',
	split: true,
	region:'east',
	margins: '1 0 0 0',
	width:150,
  defaultType: 'textfield',
	labelWidth: 35, // label settings here cascade unless overridden
	tbar:[{text:'保存',iconCls:'left',
		handler: function(){
			var fieldId = fieldAttribute.find("name","id")[0].getValue();
			var indx = ds.find("fieldid",fieldId);
			var record = ds.getAt(indx);
			ds.remove(record);
			var p = new Plant({
				fieldid: fieldId,
				fieldname: fieldAttribute.find("name","name")[0].getValue(),
				type:record.get("type"),
				readonly: fieldAttribute.find("name","readOnly")[0].getValue(),
				notnull: fieldAttribute.find("name","notNull")[0].getValue(),
				injectData: fieldAttribute.find("name","injectData")[0].getValue(),
				filter: fieldAttribute.find("name","filter")[0].getValue(),
				origin:record.get("orgion"),
				rowNum:fieldAttribute.find("name","rowNum")[0].getValue(),
				groupBy:fieldAttribute.find("hiddenName","groupBy")[0].getValue(),
				advance:fieldAttribute.find("name","advance")[0].getValue()
			});
			ds.insert(indx,p);
			grid_formFieldList.getSelectionModel().selectRow(indx, p);
			//alert(ds.data);
		}
	}]
});


fieldAttribute.add(
	new Ext.form.TextField({
		fieldLabel: 'ID',
		name: 'id',
		width: 85,
		value:"",
		readOnly:true
	}),

	new Ext.form.TextField({
		fieldLabel: '名称',
		name: 'name',
		width: 85,
		value:''
	}),
	
	new Ext.form.Checkbox({
	  fieldLabel: '只读',
	  name: 'readOnly',
	  allowBlank:true
  }),
	
	new Ext.form.Checkbox({
	  fieldLabel: '必填',
	  name: 'notNull',
	  allowBlank:true
  }),
	
	new Ext.form.Checkbox({
	  fieldLabel: '填充',
	  name: 'injectData',
	  allowBlank:true
  }),
  
  new Ext.form.ComboBox({
	  fieldLabel: '分组方式',
	  hiddenName:'groupBy',
	  store: new Ext.data.SimpleStore({
	      fields: ['value', 'name'],
	      data : [
	      	['<%=Consts.RT_WORKGROUP_PERSON%>','工作组'],
	      	['<%=Consts.RT_ORGANIZATION_PERSON%>','组织'],
	      	['<%=Consts.RT_LOCALGROUP_PERSON%>','本地组']
	      ]
	  }),
	  displayField:'name',
	  valueField: 'value',
	  mode: 'local',
	  width:85,
	  triggerAction: 'all',
	  emptyText:'请选择......',
	  selectOnFocus:true,
	  forceSelection: true,
	  editable:false,
		allowBlank:true
	}),

	new Ext.form.TextField({
		fieldLabel: '条件',
		name: 'filter',
		width: 85
		
	}),
	
	new Ext.form.TextField({
		fieldLabel: '行数',
		name: 'rowNum'
		,width: 85
	}),
	
	new Ext.form.TextArea({
		fieldLabel: '高级',
		name: 'advance',
		width: 85
	})
);


		

var formFieldPanel = new Ext.Panel({
	title:'字段信息',
	layout:'border',
	border:false,
	items:[grid_formFieldList,fieldAttribute,fieldGrid]
});

var fld_origin = new Ext.form.ComboBox({
  fieldLabel: '工单系统',
  hiddenName:'origin',
  store: new Ext.data.SimpleStore({
      fields: ['value', 'name'],
      data : [
      	['ITSM','ITSM'],['OVSD','OVSD']
      ]
  }),
  displayField:'name',
  valueField: 'value',
  typeAhead: true,
  mode: 'local',
  triggerAction: 'all',
  emptyText:'请选择......',
  selectOnFocus:true,
  width:100,
  forceSelection: true,
  editable:false,
	allowBlank:false
});
fld_origin.on("change", function(field, newValue, oldValue){
	fieldDs.baseParams.origin = newValue;
	fieldDs.reload();
});

var form = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: false,
    defaultType: 'textfield',
	baseParams: {"fld_applyto": <%=module%>, fld_xml: "<attribute/>"},
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/configure/form/formPost.jsp',
	layout:'fit',
	items: {
		baseCls: 'x-plain',
		xtype:'tabpanel',
		activeTab: 0,
		deferredRender: false,
		//border:false,

		bodyBorder:false,
		height: 340,
		layoutOnTabChange: true,
		items:[{
				title:'基本信息',
				layout:'form',
				baseCls: 'x-plain',
				defaultType: 'textfield',
				items: [
					new Ext.form.TextField({
						fieldLabel: 'ID',
						name: 'fld_oid',
						width:<%=width%>,
						allowBlank:false
					}),

					new Ext.form.TextField({
						fieldLabel: '名称',
						name: 'fld_name',
						width:<%=width%>,
						allowBlank:false
					}),fld_origin
				]
			},
			formFieldPanel,
			{
				title:'脚本',
				layout:'anchor',
				baseCls: 'x-plain',
				defaultType: 'textfield',
				items: [
					{
						anchor:'100% 100%',
						xtype: 'textarea',
						hideLabel: true,
						name: 'fld_script'
					}
				]
			}
		]}

});
var window_formView = null;

	if (window_formView)
		window_formView.close();

  window_formView = new Ext.Window({
		title: '表单信息',
		width: 500,
		height:420,
		minWidth: 400,
		minHeight: 420,
		layout:'fit',
		bodyBorder:false,
		border:false,
		iconCls :'edit',
		plain:true,
		bodyStyle:'padding:5px;',
		buttonAlign:'center',
		items: form,

		buttons: [{
      text: '确定',
      handler: function() {
				grid_formFieldList.stopEditing();
				var form = window_formView._form.form;
				if (!Ext.isIE)
				{
					alert("请使用 IE 进行操作！");
					return;
				}

				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}

				var doc = new ActiveXObject("Microsoft.XMLDOM");
				if (doc == null)
				{
					alert("new ActiveXObject('Microsoft.XMLDOM') 失败");
					return;
				}
				doc.loadXML("<attribute/>");
				doc.documentElement.setAttribute("id", form.baseParams.formId);
				doc.documentElement.setAttribute("name", form.findField("fld_name").getValue());
				var ds = form._ds;
				var count = ds.getCount();
				for (var i = 0; i < count; i++)
				{
					for (var j = 0; j < i; j++)
					{
						if (ds.getAt(j).get("fieldid") == ds.getAt(i).get("fieldid"))
						{
							Ext.MessageBox.alert("提示", "请不要设置重复的字段！" + ds.getAt(j).get("fieldid"));
							doc = null;
							return;
						}
					}
					var node = doc.createElement("field");
					doc.documentElement.appendChild(node);
					node.setAttribute("id", ds.getAt(i).get("fieldid"));
					node.setAttribute("name", ds.getAt(i).get("fieldname"));
					node.setAttribute("readonly", ds.getAt(i).get("readonly") ? "true" : "false" );
					node.setAttribute("notnull", ds.getAt(i).get("notnull") ? "true" : "false" );
					node.setAttribute("injectData", ds.getAt(i).get("injectData") ? "true" : "false" );
					node.setAttribute("filter", ds.getAt(i).get("filter"));
					if (ds.getAt(i).get("type") == "com.hp.idc.itsm.configure.fields.StringFieldInfo")
						node.setAttribute("rowNum", ds.getAt(i).get("rowNum"));
					if (ds.getAt(i).get("type") == "com.hp.idc.itsm.configure.fields.PersonFieldInfo")
						node.setAttribute("groupBy", ds.getAt(i).get("groupBy"));
					node.setAttribute("origin", ds.getAt(i).get("origin"));
					var codeValue = doc.createCDATASection(ds.getAt(i).get("advance"));
					node.appendChild(codeValue);
				}
				var node1 = doc.createElement("script");
				var scriptCData = doc.createCDATASection("");
				node1.appendChild(scriptCData);
				doc.documentElement.appendChild(node1);
				scriptCData.text = form.findField('fld_script').getValue();

				form.baseParams.fld_xml = doc.xml;
				doc = null;
				
				form.items.each(function(f){
				   f.old_status_disabled = f.disabled;
				   f.enable();
				});
				if (<%=storeToDB%>){
					form.submit({
						waitMsg: '<%=Consts.MSG_WAIT%>',
						success: function(form, action)
						{
							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
							Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
							formListDS.reload();
							window_formView.hide();
						},
						failure: function(form, action)
						{
							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
							Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
						}
					});
				} else {
					workflowPanel.formCfg = form.baseParams.fld_xml;
					//window_formView.close();
				}
      }
    },{
      text: '取消',
      handler: function() {
         window_formView.hide();
      }
    }]
   });

  window_formView.on("close",function(){
		//document.location.reload();
	});
	window_formView._form = form;
	//window_formView._win = this._win;
    window_formView.show();
form.form._ds = ds;

var fld = form.form.findField("fld_oid");
fld.setValue("<%=oid%>");
fld.disable();

<% if (form != null) { %>
	form.form.baseParams.formId = form.getId();
	fld = form.form.findField("fld_name");
	fld.setValue("<%=StringUtil.escapeJavaScript(form.getName())%>");
	fld = form.form.findField("origin");
	fld.setValue("<%=form.getOrigin()%>");
	fieldDs.baseParams.origin="<%=form.getOrigin()%>";
	fld = form.form.findField("fld_script");
	fld.setValue("<%=StringUtil.escapeJavaScript(form.getScript())%>");
<%}%>
fieldDs.load();
ds.loadData(dsData);
</script>
