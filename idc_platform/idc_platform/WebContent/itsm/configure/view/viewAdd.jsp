<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ include file="../../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

String action = "add";
int viewOid = -1;
int type = 0;
if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
	viewOid = Integer.parseInt(request.getParameter("viewOid"));

if (request.getParameter("type")!=null && !request.getParameter("type").equals(""))
	type = Integer.parseInt(request.getParameter("type"));

try{

ViewInfo viewInfo = ViewManager.getViewByOid(viewOid);
ViewFilter viewFilter = null;
if (viewInfo!=null){
	action = "edit";
	type = viewInfo.getType();
	viewFilter = viewInfo.getViewFilter();
}

%>
<script type="text/javascript">

	//----form begin-----
	var viewForm = new Ext.form.FormPanel({
		title:'常规',
		method: 'POST',
		labelAlign:'right',
		baseParams: {"operType":"<%=action%>","viewOid":<%=viewOid%>, vfield_xml: "<attribute/>",vfilter_xml: "<attribute/>"},
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 95, // label settings here cascade unless overridden
		url:'<%=Consts.ITSM_HOME%>/configure/view/viewEdit.jsp'
	});

	var view_name = new Ext.form.TextField({
			fieldLabel: '名称',
			name: 'view_name',
			width:200,
			allowBlank:false
		});

	var viewApplyTo = new Ext.form.ComboBox({
	    fieldLabel: '归属',
	    hiddenName:'view_applyto',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        <%
					List modules = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
					for (int i = 0; i < modules.size(); i++) {
						CodeInfo cc = (CodeInfo)modules.get(i);
						out.print("['"+cc.getOid()+"','"+cc.getName()+"']");
						if (i!=modules.size()-1)
							out.print(",");
					}
					%>
	        ]
	    }),
	    <%
			if (!"root".equals(userId)) {
				out.print("readOnly:true,");
			}
			%>
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
			width:200,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'请选择......',
	    selectOnFocus:true,
	    forceSelection: true,
			allowBlank:false
		});
	var viewDisplayNo = new Ext.form.TextField({
      fieldLabel: '优先序号',
      name: 'displayNo',
			width:200,
      value:"-1",
      allowBlank:true
  });

	var view_rule = new Ext.form.SelectDialogField({
		fieldLabel: '权限',
		name: 'view_rule',
		width:200,
		allowBlank:true,
		<%
			if (!"root".equals(userId)) {
				out.print("readOnly:true,");
			}
		%>
		selectUrl:"<%=Consts.ITSM_HOME%>/configure/view/viewRuleSelect.jsp"
	});

	var viewMutiSelect = new Ext.form.Checkbox({
        fieldLabel: '允许多选',
        name: 'mutiSelect',
        allowBlank:true
    });
    
  var create_by = new Ext.form.TextField({
  	fieldLabel: '创建人',
			width:200,
  	name:'view_create_by',
  	readOnly:true
  });

//---------------------标准的数据过滤视图--------------//

	<%if (type == ViewInfo.TYPE_STANDARD){%>
		var view_wf = new Ext.form.TreeBox({
	    fieldLabel: '指定流程',
	    hiddenName:'view_wf',
	    singleMode:false,
			width:200,
	    emptyText:'请选择...',
	    allowBlank:false,
	    viewLoader: new Ext.tree.FilterTreeLoader({
	    	baseParams: {'includeAll':'true'},
	    	dataUrl:'<%=Consts.ITSM_HOME%>/configure/workflow/workflowQueryForSelect.jsp'
	    })
		});
		var view_datasource = new Ext.form.ComboBox({
	    fieldLabel: '数据源',
	    hiddenName:'view_datasource',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        	['','所有'],
	        	['current','当前'],
	        	['history','历史']
	        ]
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
			width:200,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'请选择......',
	    selectOnFocus:true,
	    forceSelection: true,
	    editable:false,
			allowBlank:true
		});

		var viewBeforeDayNo = new Ext.form.TextField({
        fieldLabel: '数据天数',
        name: 'beforeDayNo',
				width:200,
        value:"",
        allowBlank:true
    });
    var recordCountPerPage = new Ext.form.TextField({
        fieldLabel: '每页数量',
				width:200,
        name: 'recordCountPerPage',
        allowBlank:true
    });
	<%} else if (type == ViewInfo.TYPE_LINKED){%>
		var viewURL = new Ext.form.TextField({
        fieldLabel: '页面地址',
				width:200,
        name: 'view_url',
        allowBlank:true
    });
    var viewScript = new Ext.form.TextArea({
        fieldLabel: '脚本',
        name: 'view_script',
				width:200,
        height: 100,
        allowBlank:true
    });
	<%}%>

	viewForm.add(
		view_name,viewApplyTo,view_rule,viewMutiSelect,viewDisplayNo,create_by
		<%if (type == ViewInfo.TYPE_STANDARD){%>
			,view_wf,view_datasource,viewBeforeDayNo,recordCountPerPage
		<%} else if (type == ViewInfo.TYPE_LINKED){%>
			,viewURL,viewScript
		<%}%>
	);

<%if (type == ViewInfo.TYPE_STANDARD){%>

	var fieldData =  [
		<%
			List fieldList = FieldManager.getFields();
			ItsmUtil.sort(fieldList,"name",false);
			for (int i=0; i < fieldList.size(); i++) {
				if (i > 0)
					out.print(",");
				FieldInfo fInfo = (FieldInfo)fieldList.get(i);
				out.print("['"+fInfo.getId()+"','"+fInfo.getName()+"','"+fInfo.getApplyTo()+"','" +fInfo.getOrigin()+"','"+fInfo.getName()+"','"+ModuleName.getModuleName(fInfo.getApplyTo())+"']");
			}
  	%>
	];

	//----field grid begin----
	var fieldDs = new Ext.data.SimpleStore({
		fields: ['id', 'value','type','origin','value_','type_name'],
		id:'id',
		data : fieldData });

	function formatBoolean(value){
		return value ? '是' : '否';
	};
	function formatRadio(value,p,record,rowIndex){
		if (value)
		for (var i = 0; i < ds.getCount(); i++) {
			if (i!=rowIndex && ds.getAt(i).get("default_sort")) {
				ds.getAt(i).set("default_sort",false);
			}
		}
		return value ? '是' : '';
	};

	function formatField(value,p, record){
		for (var i = 0; i < fieldData.length; i++)
			if (fieldData[i][0] ==  value) {
				if (record.data['field_zh']=='')
					record.data['field_zh'] = fieldData[i][4];
				record.data['origin'] = fieldData[i][3];
				break;
			}
		return value;
	};

	var cm = new Ext.grid.ColumnModel([{
			header: "字段域",
			dataIndex: 'field_en',
			width: 120,
			renderer: formatField,
			editor: new Ext.form.ComboBox({
				store: fieldDs,
				tpl: '<tpl for="."><div ext:qtip="<b>名称：</b>{value}<br/><b>I&nbsp;D：</b>{id}<br/><b>归属：</b>{type_name}<br/>" class="x-combo-list-item">{value}</div></tpl>',
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:true,
				forceSelection: true,
				allowBlank:false
			})
	},{
	   header: "显示名称",
	   dataIndex: 'field_zh',
	   width: 100,
	   editor: new Ext.form.TextField()
	},{
	   header: "分组",
	   dataIndex: 'group',
	   width: 35,
	   renderer: formatBoolean,
	   editor: new Ext.form.Checkbox()
	},{
	   header: "排序",
	   dataIndex: 'sort',
	   width: 35,
	   renderer: formatBoolean,
	   editor: new Ext.form.Checkbox()
	},{
	   header: "默认排序列",
	   dataIndex: 'default_sort',
	   width: 35,
	   renderer: formatRadio,
	   editor: new Ext.form.Checkbox()
	},{
	   header: "所属",
	   dataIndex: 'origin',
	   width: 35
	}]);


var ds = new Ext.data.SimpleStore({
	fields:
		['field_en','field_zh', 'sort','default_sort', 'group','origin'],
	data: [
		<%
		if (viewInfo!=null) {
			List columns = viewInfo.getColumns();
			if (columns!=null) {
				for (int i = 0; i < columns.size(); i++) {
					ViewColumnInfo column = (ViewColumnInfo)columns.get(i);
					if (i>0)
						out.print(",");
					out.print("[");
					out.print("'"+column.getNameEN()+"',");
					out.print("'"+column.getNameZH()+"',");
					out.print(column.isSort()+",");
					out.print(column.isDefaultSort()+",");
					out.print(column.isGroup()+",");
					out.print("'"+column.getOrigin()+"'");
					out.print("]");
				}
			}
		}
		%>
	]});

var Plant = Ext.data.Record.create([
	   {name: 'field_en', type: 'string'},
	   {name: 'field_zh', type: 'string'},
	   {name: 'group', type: 'bool'},
	   {name: 'sort', type: 'bool'},
	   {name: 'default_sort', type: 'bool'},
	   {name: 'origin', type: 'string'}
  ]);

// create the editor grid
var view_field_grid = new Ext.grid.EditorGridPanel({
	title:'显示列',
	id:'view_field_grid',
	//renderTo:'dlg_view_field',
	ds: ds,
	cm: cm,
	enableColLock:false,
	tbar:[{
    text:'新增',
    tooltip:'新增一行',
    iconCls:'add',
    handler: function()
		{
			var p = new Plant({
				field_en: '',
				field_zh: '',
				group: false,
				sort: true,
				default_sort: false,
				origin:''
			});
			view_field_grid.stopEditing();
			ds.add(p);
		}
  }, '-', {
    text:'插入',
    tooltip:'目标行前插入',
    iconCls:'option',
    handler: function()
		{
			var cell = view_field_grid.getSelectionModel().getSelectedCell();
			var p = new Plant({
				field_en: '',
				field_zh: '',
				group: false,
				sort: true,
				default_sort: false,
				origin:''
			});
			view_field_grid.stopEditing();
			if (cell != null)
				ds.insert(cell[0], p);
			else
				ds.add(p);
		}
  }, '-', {
    text:'上移',
    tooltip:'目标行上移',
    iconCls:'up',
    handler: function()
		{
			var cell = view_field_grid.getSelectionModel().getSelectedCell();
			if (cell == null || cell[0] == 0)
				return;
			var n = cell[0];
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n - 1, r);
			view_field_grid.getSelectionModel().select(n - 1, cell[1]);
		}
  }, '-', {
    text:'下移',
    tooltip:'目标行下移',
    iconCls:'down',
    handler: function()
		{
			var cell = view_field_grid.getSelectionModel().getSelectedCell();
			if (cell == null || cell[0] == ds.getCount() - 1)
				return;
			var n = cell[0];
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n + 1, r);
			view_field_grid.getSelectionModel().select(n + 1, cell[1]);
		}
  },'-',{
    text:'删除',
    tooltip:'删除选择行',
    iconCls:'remove',
    handler: function()
		{
			var cell = view_field_grid.getSelectionModel().getSelectedCell();
			if (cell != null)
				ds.remove(ds.getAt(cell[0]));
		}
  }]
});


//----过滤器表格----

var filterRelation = new Ext.data.SimpleStore({
	fields: ['id', 'value'],
	data : [
		<%
			List operationList = OperationCode.getOperations();
			for (int i = 0; i< operationList.size(); i++) {
				String name = (String)operationList.get(i);
				if (i>0)
					out.print(",");
				out.print("['"+name+"','"+name+"']");
			}
		%>
	]
});

	function formatField1(value,p, record){
		for (var i = 0; i < fieldData.length; i++)
			if (fieldData[i][0] == value) {
				record.data['origin'] = fieldData[i][3];
				record.data['fieldname'] = fieldData[i][4];
				break;
			}
		return value;
	};

	var filter_cm = new Ext.grid.ColumnModel([{
			header: "字段域",
			dataIndex: 'field',
			renderer: formatField1,
			width: 100,
			editor:new Ext.form.ComboBox({
		    store:fieldDs,
		    displayField:'value',
		    valueField: 'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择......',
		    selectOnFocus:true,
		    width:100,
		    editable:false,
				allowBlank:false
			})
	},{
	   header: "名称",
	   dataIndex: 'fieldname',
	   width: 100
	},{
	   header: "所属",
	   dataIndex: 'origin',
	   width: 60
	},{
	   header: "运算符",
	   dataIndex: 'relation',
	   width: 60,
	   editor:new Ext.form.ComboBox({
				store: filterRelation,
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:false,
				forceSelection: true,
				width:100,
				allowBlank:false
			})
	},{
	   header: "值",
	   dataIndex: 'value',
	   width: 120,
	   editor:new Ext.form.TextField()
	}]);

var filter_ds = new Ext.data.SimpleStore({
	fields: ['field', 'relation', 'value','origin','fieldname'],
	data: [
		<%
			if (viewFilter!=null) {
				List filters = viewFilter.getFilters();
				if (filters!=null) {
					for (int i = 0; i < filters.size(); i++) {
						FilterInfo filter = (FilterInfo)filters.get(i);
						if (i>0)
							out.print(",");

						out.print("[");
						out.print("'"+filter.getFieldId()+"',");
						out.print("'"+filter.getRelation()+"',");
						out.print("'"+StringUtil.escapeJavaScript(filter.getValue())+"',");
						out.print("'"+filter.getOrigin()+"',");
						out.print("''");
						out.print("]");
					}
				}
			}
		%>
	]
});
var filterGridRecord = Ext.data.Record.create([
	   {name: 'field',type:'string'},
	   {name: 'relation',type:'string'},
	   {name: 'value',type:'string'},
	   {name: 'origin',type:'string'}
  ]);

var combination_expression = new Ext.form.TextField({
	name: 'combination_expression',
	value:'<%if(viewFilter != null)out.print(StringUtil.escapeJavaScript(viewFilter.getCombination_expression()));%>',
	width:250
});

var filter_select_mode = new Ext.ux.RadioGroup({
	name:'filter_select_mode',
	horizontal:true,
	radios:[{
			boxLabel :'全与',
			<%if (viewFilter != null && viewFilter.getCombination_mode() == ViewFilter.COMBINATION_MODE_AND){%>
			checked:true,
			<%}%>
			value:'<%=ViewFilter.COMBINATION_MODE_AND%>'
		},{
			boxLabel :'全或',
			<%if (viewFilter != null && viewFilter.getCombination_mode() == ViewFilter.COMBINATION_MODE_OR){%>
			checked:true,
			<%}%>
			value:'<%=ViewFilter.COMBINATION_MODE_OR%>'
		},{
			boxLabel :'其他',
			<%if (viewFilter != null && viewFilter.getCombination_mode() == ViewFilter.COMBINATION_MODE_OTHER){%>
			checked:true,
			<%}%>
			value:'<%=ViewFilter.COMBINATION_MODE_OTHER%>'
		}]
	});
var view_filter_grid = new Ext.grid.EditorGridPanel({
	title:'过滤器',
	id:'filter_grid',
	ds: filter_ds,
	cm: filter_cm,
	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColLock:false,
	tbar:[{
      text: '增加',
      tooltip:'增加一行',
      iconCls:'add',
      handler: function()
			{
				var p = new filterGridRecord({
					field: '',
					relation: '',
					value: '',
					origin: ''
				});
				view_filter_grid.stopEditing();
				filter_ds.add(p);
				var row = filter_ds.getTotalCount();
				view_filter_grid.getSelectionModel().selectLastRow();
			}
  }, '-', {
      text: '删除',
      tooltip:'删除选择行',
      iconCls:'remove',
      handler: function()
			{
				var row = view_filter_grid.getSelectionModel().getSelected();
				if (row != null)
					filter_ds.remove(row);
			}
  }, '-', {
      text: '帮助',
      tooltip:"\$\{date\}=今天<br>\$\{datetime\}=当前时间<br>\$\{year\}=今年<br>\$\{month\}=本月<br>\$\{user\}=当前登录者",
      iconCls:'help',
      disabled:true
  }],
  bbar:[{
      text: '组合方式:'
  },'-',filter_select_mode,combination_expression
	]
});


	//------------------表格动作--------------------

	var action_ds = new Ext.data.SimpleStore({
	fields: ['name', 'code'],
	data: [
		<%
			if (viewInfo!=null) {
				Map actions = viewInfo.getAction();
				if (actions!=null) {
					Set aset = actions.keySet();
					boolean hasR = false;
					for (Iterator ite = aset.iterator();ite.hasNext();) {
						if (hasR)
							out.print(",");
						String key = (String)ite.next();
						String value = (String)actions.get(key);
						out.print("[");
						out.print("'"+key+"',");
						out.print("'"+StringUtil.escapeJavaScript(value)+"'");
						out.print("]");
						hasR = true;
					}
				}
			}
		%>
	]});

	var actionData = [
		['rowdblclick','记录双击'],
		['rowclick','记录单击']
	];
	var actionList = new Ext.data.SimpleStore({
		fields: ['id','value'],
		data : actionData
	});
	function formatAction(value,p,record){
		for (var i = 0; i < actionData.length; i++)
			if (actionData[i][0] == value){
				return actionData[i][1];
			}
		return value;
	};

	var actionGrid_cm = new Ext.grid.ColumnModel([{
			header: "动作名",
			dataIndex: 'name',
			renderer: formatAction,
			width: 80,
			editor: new Ext.grid.GridEditor(new Ext.form.ComboBox({
				store: actionList,
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				maxHeight: 50,
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:true,
				forceSelection: true,
				allowBlank:false
	   	}))
	},{
	   header: "动作代码",
	   dataIndex: 'code',
	   width: 400,
	   editor: new Ext.grid.GridEditor(new Ext.form.TextArea({
	   	 grow:true,
	   	 height:200,
	   	 growMax:200
	   }))
	}]);

	/*var expander = new Ext.grid.RowExpander({
        tpl : new Ext.Template(
            "<table><tr><td>动作代码:</td></tr><tr><td><font color='gray'> {code}</font></td></tr></table>"
        )
    });
  var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,header:''});
  */
  var actionGridRecord = Ext.data.Record.create([
	   {name: 'name',type:'string'},
	   {name: 'code',type:'string'}
  ]);
	var actionGrid = new Ext.grid.EditorGridPanel({
		title:'表格动作',
		ds: action_ds,
		cm: actionGrid_cm,
		selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
		//columns:[expander,sm,actionGrid_cm],
		enableColLock:false,
		tbar:[{
	      text: '新增',
	      tooltip:'新增一条',
	      iconCls:'add',
	      handler: function()
				{
					var p = new actionGridRecord({
						name: '',
						code: ''
					});
					actionGrid.stopEditing();
					action_ds.add(p);
					var row = action_ds.getTotalCount();
					actionGrid.getSelectionModel().selectLastRow();
				}
	  },
	  '-',
	  {
	      text: '删除',
	      tooltip:'删除选择行',
	      iconCls:'remove',
	      handler: function()
				{
					var row = actionGrid.getSelectionModel().getSelected();
					if (row != null)
						action_ds.remove(row);
				}
	  }]
	});

//------------------表格按钮--------------------

var button_ds = new Ext.data.SimpleStore({
	fields: ['name', 'code'],
	data: [
		<%
			if (viewInfo!=null) {
				List buttons = viewInfo.getButton();
				if (buttons!=null) {
					boolean hasR = false;
					for (int i = 0; i < buttons.size(); i++) {
						if (hasR)
							out.print(",");
						String[] key = (String[])buttons.get(i);
						out.print("[");
						out.print("'"+key[0]+"',");
						out.print("'"+StringUtil.escapeJavaScript(key[1])+"'");
						out.print("]");
						hasR = true;
					}
				}
			}
		%>
	]});

	var buttonGrid_cm = new Ext.grid.ColumnModel([{
			header: "按钮名称",
			dataIndex: 'name',
			width: 80,
			editor: new Ext.grid.GridEditor(new Ext.form.TextField({
				allowBlank: true
	   	}))
	},{
	   header: "动作函数",
	   dataIndex: 'code',
	   width: 400,
	   editor: new Ext.grid.GridEditor(new Ext.form.TextArea({
	   	 grow:true,
		   height:200,
		   growMax:200
	   }))
	}]);

	var smb = new Ext.grid.CheckboxSelectionModel({singleSelect:true,header:''});
	var buttonGrid = new Ext.grid.EditorGridPanel({
		title:'动作按钮',
		ds: button_ds,
		cm: buttonGrid_cm,
		selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
		enableColLock:false,
		tbar:[{
	      text: '新增',
	      tooltip:'新增一条',
	      iconCls:'add',
	      handler: function()
				{
					var p = new actionGridRecord({
						name: '',
						code: ''
					});
					buttonGrid.stopEditing();
					button_ds.add(p);
					var row = button_ds.getTotalCount();
					buttonGrid.getSelectionModel().selectLastRow();
				}
	  },'-',{
	      text: '删除',
	      tooltip:'删除选择行',
	      iconCls:'remove',
	      handler: function()
				{
					var row = buttonGrid.getSelectionModel().getSelected();
					if (row != null)
						button_ds.remove(row);
				}
	  }]
	});

	//----------------检索列--------------------

	var search_ds = new Ext.data.SimpleStore({
	fields: ['field_en', 'field_zh','origin'],
	data: [
		<%
			if (viewInfo!=null) {
				List searchs = viewInfo.getSearchCol();
				boolean hasR = false;
				for (int i = 0; i < searchs.size();i++) {
					ViewSearchInfo cols = (ViewSearchInfo)searchs.get(i);
					if (hasR)
						out.print(",");
					out.print("[");
					out.print("'"+cols.getNameEN()+"',");
					out.print("'"+cols.getNameZH()+"',");
					out.print("'"+cols.getOrigin()+"'");
					out.print("]");
					hasR = true;
				}
			}
		%>
	]});

	var searchcm = new Ext.grid.ColumnModel([{
			header: "字段域",
			dataIndex: 'field_en',
			width: 120,
			renderer: formatField,
			editor: new Ext.form.ComboBox({
				store: fieldDs,
				valueField: 'id',
				displayField:'value',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'请选择...',
				selectOnFocus:true,
				editable:false,
				forceSelection: true,
				allowBlank:false
			})
	},{
	   header: "显示名称",
	   dataIndex: 'field_zh',
	   width: 100,
	   editor: new Ext.form.TextField()
	},{
	   header: "所属",
	   dataIndex: 'origin',
	   width: 100
	}]);
	var searchPlant = Ext.data.Record.create([
	   {name: 'field_en', type: 'string'},
	   {name: 'field_zh', type: 'string'}
  ]);
	var searchGrid = new Ext.grid.EditorGridPanel({
		title:'检索列',
		ds: search_ds,
		enableColLock:false,
		autoHeight:true,
		cm:searchcm,
		tbar:[{
    text:'新增',
    tooltip:'新增一行',
    iconCls:'add',
    handler: function()
		{
			var p = new searchPlant({
				field_en: '',
				field_zh: ''
			});
			searchGrid.stopEditing();
			search_ds.add(p);
		}
  },'-',{
    text:'删除',
    tooltip:'删除选择行',
    iconCls:'remove',
    handler: function()
		{
			var cell = searchGrid.getSelectionModel().getSelectedCell();
			if (cell != null)
				search_ds.remove(search_ds.getAt(cell[0]));
		}
  }]
	});
<%}%>
	//------------page layout begin----------------

	var viewEditTabs = new Ext.TabPanel({
      border:false,
      activeTab:0,
      items:[viewForm
      <%if (type == ViewInfo.TYPE_STANDARD){%>
	      ,view_field_grid,view_filter_grid,searchGrid,actionGrid,buttonGrid
	    <%}%>
      ]
  });

  viewEditTabs.on("tabchange",function(tabPanel,tab){
  	if(tab.getId()=='view_field_grid'){
  		var selValue = viewForm.form.findField("view_applyto").getValue();
			fieldDs.filterBy(function(rec,id){
				var comValue = rec.data['type'];
				if (comValue==selValue || comValue=='<%=ModuleName.ALL%>')
					return true;
				return false;
			});
  	}
  });

	//------------page layout end----------------


	var formWindow = null;
	if (formWindow) {
		formWindow.close();
	}

	formWindow = new Ext.Window({
      title: '编辑视图',
      width: 510,
      height:400,
      minWidth: 300,
      minHeight: 200,
      layout: 'fit',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: viewEditTabs,

      buttons: [{
          text: '确定',
          handler: function() {
          	if (!Ext.isIE)
						{
							alert("请使用 IE 进行操作！");
							return;
						}
						var viewForm = formWindow.viewForm.form;
          	if (!viewForm.isValid()) {
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
						doc.documentElement.setAttribute("type",<%=type%>);
						doc.documentElement.setAttribute("displayNo",viewForm.findField("displayNo").getEl().dom.value);
						doc.documentElement.setAttribute("mutiSelect",viewForm.findField("mutiSelect").getValue() ? "true" : "false");
						doc.documentElement.setAttribute("create_by",viewForm.findField("view_create_by").getEl().dom.value);
						var ruleNode = doc.createElement("rule");
						doc.documentElement.appendChild(ruleNode);
						var ruleValue = doc.createTextNode(viewForm.findField("view_rule").getEl().dom.value);
						ruleNode.appendChild(ruleValue);
<%if (type == ViewInfo.TYPE_LINKED){%>
						var viewURL = doc.createElement("url");
						doc.documentElement.appendChild(viewURL);
						var urlValue = doc.createTextNode(viewForm.findField("view_url").getEl().dom.value);
						viewURL.appendChild(urlValue);

						var viewScript = doc.createElement("script");
						doc.documentElement.appendChild(viewScript);
						var scriptValue = doc.createTextNode(viewForm.findField("view_script").getEl().dom.value);
						viewScript.appendChild(scriptValue);
<%} else if (type == ViewInfo.TYPE_STANDARD){%>
						doc.documentElement.setAttribute("dataSource",viewForm.findField("view_datasource").getValue());
						doc.documentElement.setAttribute("workflow",viewForm.findField("view_wf").getValue());
						doc.documentElement.setAttribute("beforeDayNo",viewForm.findField("beforeDayNo").getEl().dom.value);
						doc.documentElement.setAttribute("recordCountPerPage",viewForm.findField("recordCountPerPage").getEl().dom.value);

						//--显示列
						var ds = formWindow.fieldDS;
						var count = ds.getCount();
						var field_col = doc.createElement("columns");
						doc.documentElement.appendChild(field_col);
						for (var i = 0; i < count; i++)
						{
							if (ds.getAt(i).get("field_en")==null || ds.getAt(i).get("field_en")=="")
								continue;
							var node = doc.createElement("column");
							field_col.appendChild(node);
							node.setAttribute("nameEN", ds.getAt(i).get("field_en"));
							node.setAttribute("nameZH", ds.getAt(i).get("field_zh"));
							node.setAttribute("sort", ds.getAt(i).get("sort") ? "true" : "false" );
							node.setAttribute("group", ds.getAt(i).get("group") ? "true" : "false" );
							node.setAttribute("defaultSort", ds.getAt(i).get("default_sort") ? "true" : "false" );
						}

						//--过滤器
						ds = formWindow.filterDS;
						count = ds.getCount();
						var filter_col = doc.createElement("filters");

						//过滤器的组合表达式
						var com_exp = doc.createElement("combination_expression");
						var com_exp_value = doc.createTextNode(combination_expression.getValue());
						com_exp.setAttribute("combination_mode", filter_select_mode.getValue());
						com_exp.appendChild(com_exp_value);
						filter_col.appendChild(com_exp);

						doc.documentElement.appendChild(filter_col);
						for (var i = 0; i < count; i++)
						{
							if (ds.getAt(i).get("field")==null || ds.getAt(i).get("field")=="")
								continue;
							var node = doc.createElement("filter");
							filter_col.appendChild(node);
							node.setAttribute("field", ds.getAt(i).get("field"));
							node.setAttribute("relation", ds.getAt(i).get("relation"));
							var filterValue = doc.createTextNode(ds.getAt(i).get("value"));
							node.appendChild(filterValue);
							//node.setText(ds.getAt(i).get("value_macro") ? ds.getAt(i).get("value_macro") : ds.getAt(i).get("value_text") );
						}

						//--动作
						ds = formWindow.actionDS;
						count = ds.getCount();
						var action_col = doc.createElement("actions");
						doc.documentElement.appendChild(action_col);
						for (var i = 0; i < count; i++)
						{
							if (ds.getAt(i).get("name")==null || ds.getAt(i).get("name")=="")
								continue;
							var node = doc.createElement("action");
							action_col.appendChild(node);
							node.setAttribute("name", ds.getAt(i).get("name"));
							var codeValue = doc.createTextNode(ds.getAt(i).get("code"));
							node.appendChild(codeValue);
						}

						//--检索
						ds = formWindow.searchDS;
						count = ds.getCount();
						var search_col = doc.createElement("searchs");
						doc.documentElement.appendChild(search_col);
						for (var i = 0; i < count; i++)
						{
							if (ds.getAt(i).get("field_en")==null || ds.getAt(i).get("field_en")=="")
								continue;
							var node = doc.createElement("column");
							search_col.appendChild(node);
							node.setAttribute("nameEN", ds.getAt(i).get("field_en"));
							node.setAttribute("nameZH", ds.getAt(i).get("field_zh"));
						}

						//--按钮
						ds = formWindow.buttonDS;
						count = ds.getCount();
						var button_col = doc.createElement("buttons");
						doc.documentElement.appendChild(button_col);
						for (var i = 0; i < count; i++)
						{
							var node = doc.createElement("button");
							button_col.appendChild(node);
							node.setAttribute("name", ds.getAt(i).get("name"));
							var codeValue = doc.createTextNode(ds.getAt(i).get("code"));
							node.appendChild(codeValue);
						}
<%}%>
						viewForm.baseParams.vfield_xml = doc.xml;
						doc = null;

						viewForm.submit({
						waitMsg: '<%=Consts.MSG_WAIT%>',
						success: function(form, action)
						{

							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});

							Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
							formWindow.close();
							document.location.reload();
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
          }
      },{
          text: '取消',
          handler: function() {
            formWindow.close();
          }
      }]
  });

  formWindow.viewForm = viewForm;
<%if (type == ViewInfo.TYPE_STANDARD){%>
  formWindow.fieldDS = ds;
  formWindow.filterDS = filter_ds;
  formWindow.actionDS = action_ds;
  formWindow.searchDS = search_ds;
  formWindow.buttonDS = button_ds;
<%}%>
  formWindow.on("close",function(){
		//document.location.reload();
	});

  formWindow.show();


 <%
		if (viewInfo!=null) {
			String view_wf_value = "";

			String workflowOid = viewInfo.getWorkflowOid();
			if (workflowOid!=null && !workflowOid.equals("")){
				String[] wfOids= workflowOid.split(",");
				int count = 0;
				for(int i = 0; i < wfOids.length; i++) {
					if (wfOids[i]==null || wfOids[i].equals(""))
						continue;
					String workflowName = "所有";
					WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(Integer.parseInt(wfOids[i]));
					if (wfInfo!=null) {
						workflowName = wfInfo.getName();
						if (count>0)
							view_wf_value += ",";
						view_wf_value += wfOids[i] +"=" + workflowName;
						count++;
					} else if (wfOids[i].equals("-1")){
						if (count>0)
							view_wf_value += ",";
						view_wf_value += wfOids[i] +"=" + workflowName;
						count++;
					}
				}

			} else
				view_wf_value = "-1=所有";
	%>
	var field_fld =  viewForm.form.findField("view_name");
	field_fld.setValue("<%=viewInfo.getName()%>");
	field_fld =  viewForm.form.findField("view_applyto");
	field_fld.setValue("<%=viewInfo.getApplyTo()%>");

	field_fld =  viewForm.form.findField("mutiSelect");
	field_fld.setValue("<%=viewInfo.getAttribute("mutiSelect")%>");
	
	field_fld =  viewForm.form.findField("view_create_by");
	field_fld.setValue("<%=(viewInfo.getAttribute("create_by").equals("")?userId:viewInfo.getAttribute("create_by"))%>");

	field_fld =  viewForm.form.findField("view_rule");
	field_fld.setValue("<%=viewInfo.getRule()%>");

	field_fld =  viewForm.form.findField("displayNo");
	field_fld.setValue("<%=viewInfo.getAttribute("displayNo")%>");

<%if (type == ViewInfo.TYPE_STANDARD){%>
	field_fld =  viewForm.form.findField("view_datasource");
	field_fld.setValue("<%=viewInfo.getDataSource()%>");

	field_fld =  viewForm.form.findField("view_wf");
	field_fld.setValue("<%=view_wf_value%>");

	field_fld =  viewForm.form.findField("beforeDayNo");
	field_fld.setValue("<%=viewInfo.getAttribute("beforeDayNo")%>");


	field_fld =  viewForm.form.findField("recordCountPerPage");
	field_fld.setValue("<%=viewInfo.getAttribute("recordCountPerPage")%>");
<%} else if (type == ViewInfo.TYPE_LINKED){%>
	field_fld =  viewForm.form.findField("view_url");
	field_fld.setValue("<%=StringUtil.escapeJavaScript(viewInfo.getUrl())%>");
	field_fld =  viewForm.form.findField("view_script");
	field_fld.setValue("<%=StringUtil.escapeJavaScript(viewInfo.getScript())%>");
	<%
	}
} else {
	if (!"root".equals(userId)) {

%>
	var field_fld =  viewForm.form.findField("view_rule");
	field_fld.setValue("ap:<%=userId%>;reject");
	field_fld =  viewForm.form.findField("view_applyto");
	field_fld.setValue("12");
<%
}
%>
	var field_fld =  viewForm.form.findField("view_create_by");
	field_fld.setValue("<%=userId%>");
<%
}
%>
	</script>

<%
 }catch(Exception e) {
 	e.printStackTrace();
 }
%>