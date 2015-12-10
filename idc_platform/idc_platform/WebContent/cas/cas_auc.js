var orgDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'organizationQuery.jsp'
    }),
		baseParams:{type:'all'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
				{name: 'id', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'parentId', type: 'string'},
				{name: 'status', type: 'string'}
    ]),
    sortInfo:{field: 'id', direction: 'DESC'}
});

var orgCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
		{header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    {header: "��ID", width: 75, sortable: true, dataIndex: 'parentId'},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);



var userDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'userQuery.jsp'
    }),
	baseParams:{type:'all',includeAll:'true'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount',
        id:'id',
        fields:[
						{name: 'id', type: 'string'},
						{name: 'name', type: 'string'},
						{name: 'role', type: 'string'},
						{name: 'mobile', type: 'string'},
						{name: 'email', type: 'string'},
						{name: 'p_status', type: 'string'},
						{name: 'status', type: 'string'},
						{name: 'department', type: 'string'}
		    ]
    }),
    sortInfo:{field: 'id', direction: 'ASC'}
});

var userCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
    {header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    //{header: "ְ��", width: 75, sortable: true, dataIndex: 'role'},
    {header: "�ֻ���", width: 75, sortable: true, dataIndex: 'mobile'},
    {header: "E-MAIL", width: 75, sortable: true, dataIndex: 'email'},
    {header: "��Ա״̬", width: 85, sortable: true, dataIndex: 'p_status',renderer:formatPersonStatus},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);

var userMutiDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'userQuery.jsp'
    }),
		baseParams:{type:'all'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
				{name: 'id', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'p_status', type: 'string'},
				{name: 'status', type: 'string'}
    ]),
    sortInfo:{field: 'id', direction: 'DESC'}
});
var sm = new Ext.grid.CheckboxSelectionModel();
var userMutiCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
		sm,
		{header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    {header: "��Ա״̬", width: 85, sortable: true, dataIndex: 'p_status',renderer:formatPersonStatus},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);



var wgDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'workgroupQuery.jsp'
    }),
		baseParams:{type:'all'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
    		{name: 'parentId', type: 'string'},
				{name: 'id', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'status', type: 'string'}
    ]),
    sortInfo:{field: 'id', direction: 'DESC'}
});

var wgCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
		{header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    {header: "��ID", width: 75, sortable: true, dataIndex: 'parentId'},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);


var statusStore = [
	['0','����'],
	['1','����']
];

var PSStore = [
	['0','�ڰ�'],
	['1','����'],
	['2','����']
];

function formatStatus(value){
	for (var i = 0; i < statusStore.length; i++){
		if (statusStore[i][0]==value){
			return statusStore[i][1];
		}
	}
	return value;
}

function formatPersonStatus(value){
	for (var i = 0; i < PSStore.length; i++){
		if (PSStore[i][0]==value){
			return PSStore[i][1];
		}
	}
	return value;
}


function postForm(p,urls,callback){
	if(callback == null)
		callback = postCallback;
	var formElement = document.getElementById("local_post_form_div");
	if (!formElement) {
		formElement = document.createElement("div");
		formElement.style.visibility = 'hidden';
		formElement.style.position = 'absolute';
		formElement.id = "local_post_form_div";
		document.body.appendChild(formElement);
	}
	var simple = new Ext.form.FormPanel({
		defaultType: 'textfield',
		method: 'POST',
		baseParams: p,
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		items: [{
				xtype: 'hidden',
                name: '_temp_hidden_field'
            }],
		url:urls
	});
	simple.render(formElement);
	simple.form.submit({
		waitMsg: '<%=Consts.MSG_WAIT%>',
		success: function(form, action)
		{
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text,callback);
		},
		failure: function(form, action)
		{
			Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
		}
	});
}