var ITSM_HOME = "/idc_platform/itsm";
var ITSM_HOME_PARENT = "/";

var window_ciRelation = null;
var grid_taskList = null;
var window_viewTask = null;
var window_editTask = null;
var window_historyView = null;

function openURL(urls){
	//document.location.href=urls;
	var height=600,width=800;
	if(width == -1)
      width = document.body.offsetWidth;
  if(height == -1)
      height = document.body.offsetHeight;

  var xposition = (screen.width - width) / 2;
  var yposition = (screen.height - height) / 2;
	window.open(urls, '_blank','left='+xposition+',top='+yposition+',height='+height+',width='+width+',toolbar=no, menubar=no,resizable=yes, scrollbars=yes,location=no, status=no');
}

function openURL2(urls){
	//document.location.href=urls;
	var height=480,width=650;
	if(width == -1)
      width = document.body.offsetWidth;
  if(height == -1)
      height = document.body.offsetHeight;

  var xposition = (screen.width - width) / 2;
  var yposition = (screen.height - height) / 2;
	window.showModalDialog(urls,window,'dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
}

function taskHistoryView()
{
	openURL(ITSM_HOME+"/task/taskHistoryView.jsp?taskOid="+this.taskOid+"&taskDataId="+this.dataId+"&origin="+this.origin+"&wfOid="+this.wfOid+"&wfVer="+this.wfVer);
}

function taskHistoryView2(taskOid,origin,wfOid,wfVer)
{
	openURL(ITSM_HOME+"/task/taskHistoryView.jsp?taskOid="+taskOid+"&origin="+origin+"&wfOid="+wfOid+"&wfVer="+wfVer);
}

function taskEdit()
{
	openURL(ITSM_HOME+"/task/taskActionNode.jsp?origin="+this.origin+"&wfOid="+this.wfOid+"&taskOid="+this.taskOid+"&taskDataId="+this.dataId+"&toNodeId="+this.toNodeId);
}

function taskView(taskId, dataId,origin,wfOid,wfVer,dealPage)
{
	if (!dealPage)
		dealPage = ITSM_HOME+"/task/taskInfo.jsp";
	openURL(dealPage+"?origin="+origin+"&taskOid="+taskId+"&taskDataId="+dataId+"&wfOid="+wfOid+"&wfVer="+wfVer);
}

function taskAccept()
{
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.dataId = this.dataId;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	if (confirm("您确定接手处理此任务吗？"))
	{
		taskPost({"operType": 'acceptTask',origin:window_viewTask.origin,wfOid:window_viewTask.wfOid,taskOid: window_viewTask.taskOid,
			taskDataId: window_viewTask.dataId,ret:true});
	}
}

function taskPost_(p,urls){
	var formElement = document.createElement("div");
	formElement.style.visibility = 'hidden';
	formElement.style.position = 'absolute';
	document.body.appendChild(formElement);
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
		waitMsg: '正在处理，请稍候...',
		success: function(form, action)
		{
			Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text,taskPostCallBack);
		},
		failure: function(form, action)
		{
			Ext.MessageBox.alert("失败", form.errorReader.xmlData.documentElement.text);
		}
	});
}

function taskPost(p)
{
	taskPost_(p,ITSM_HOME+'/task/taskPost.jsp');
}

function taskCloseBranch()
{
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.dataId = this.dataId;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	if (confirm("您确定不再继续分配任务了吗？"))
	{
		taskPost({"operType": 'closeBranch',origin:window_viewTask.origin,wfOid:window_viewTask.wfOid,taskOid: window_viewTask.taskOid,
			taskDataId: window_viewTask.dataId,ret:true});
	}
}

function taskAddMessage()
{
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.dataId = this.dataId;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	getPrompt('提示', '请输入您的意见:', function(t)
		{
			taskPost({"operType": 'message','origin':window_viewTask.origin,wfOid:window_viewTask.wfOid, taskOid: window_viewTask.taskOid,
				taskDataId: window_viewTask.dataId, msg: t ,ret:false});
		}
	);
}

//执行已阅动作
function readApply(){
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.dataId = this.dataId;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	getPrompt('提示', '阅知备注:', function(t)
		{
			taskPost({"operType": 'readApply','origin':window_viewTask.origin,wfOid:window_viewTask.wfOid, taskOid: window_viewTask.taskOid,
				taskDataId: window_viewTask.dataId, msg: t ,ret:false});
		},true
	);
}

function taskRollback(obj)
{
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.dataId = this.dataId;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	
	var http = null;
	var activeX = ['MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
	try {
		http = new XMLHttpRequest();
	} catch(e) {
		for (var i = 0; i < activeX.length; ++i) {
			try {
				http = new ActiveXObject(activeX[i]);
				break;
			}
			catch(e) {
			}
		}
	}
	http.open("GET", ITSM_HOME+"/task/taskRollBackCount.jsp?taskOid="+window_viewTask.taskOid+"&wfOid="+window_viewTask.wfOid+"&wfVer="+this.wfVer+"&origin="+window_viewTask.origin, false);
	http.send();
	if(eval(http.responseText.replace(/(^\s*)|(\s*$)/g, ""))){
		alert("回退已经有3次, 工单不可在回退!");
		return;
	}
	getPrompt('提示', '请输入回退原因:', function(t)
		{
			taskPost({"operType": 'rollback',origin:window_viewTask.origin,wfOid:window_viewTask.wfOid,taskOid: window_viewTask.taskOid,
				taskDataId: window_viewTask.dataId, msg: t ,ret:true});
		}
	);
}

function sendSms()
{
	window_viewTask.sendTo = this.sendTo;
	window_viewTask.wfOid = this.wfOid;
	window_viewTask.taskTitle = this.taskTitle;
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.taskStatus = this.taskStatus;
	//getPrompt('提示', '输入短信内容:', function(t)
	//	{
	//		taskPost({"operType": 'sendSms',sendTo:window_viewTask.sendTo, msg: t });
	//	}
	//);
	Ext.loadRemoteScript(ITSM_HOME+"/task/smsSend.jsp",
		{sendTO:window_viewTask.sendTo,wfOid:window_viewTask.wfOid,taskTitle:window_viewTask.taskTitle,taskOid:window_viewTask.taskOid,taskStatus:window_viewTask.taskStatus});
}

function taskForceClose()
{
	window_viewTask.taskOid = this.taskOid;
	window_viewTask.origin = this.origin;
	window_viewTask.wfOid = this.wfOid;
	getPrompt('提示', '请输入强制关闭原因:', function(t)
		{
			taskPost({"operType": 'forceClose',origin:window_viewTask.origin,wfOid:window_viewTask.wfOid, taskOid: window_viewTask.taskOid, msg: t,ret:true});
		}
	);
}

function taskForceCloseTaskData(wfOid,taskOid,taskDataId,branchTaskDataId) {
	window_viewTask.taskOid = taskOid;
	window_viewTask.wfOid = wfOid;
	window_viewTask.taskDataId = taskDataId;
	window_viewTask.branchTaskDataId = branchTaskDataId;
	getPrompt('提示', '请输入强制关闭原因:', function(t)
		{
			taskPost({"operType": 'forceCloseBranch',wfOid:window_viewTask.wfOid, taskOid: window_viewTask.taskOid, taskDataId:window_viewTask.taskDataId,branchTaskDataId:window_viewTask.branchTaskDataId, msg: t,ret:true});
		}
	);
}

function removeTaskData(wfOid,taskOid,taskDataId,branchTaskDataId) {
	window_viewTask.taskOid = taskOid;
	window_viewTask.wfOid = wfOid;
	window_viewTask.taskDataId = taskDataId;
	window_viewTask.branchTaskDataId = branchTaskDataId;
	if (confirm("您确定要移除此分支？")) {
		taskPost({"operType": 'removeChild',wfOid:window_viewTask.wfOid, taskOid: window_viewTask.taskOid, taskDataId:window_viewTask.taskDataId,branchTaskDataId:window_viewTask.branchTaskDataId, ret:true});
	}
}

//弹出输入框口
//ae:是否允许为空
function getPrompt(t, m, f,ae)
{
	Ext.MessageBox.show({
	   title: t,
	   msg: m,
	   width:300,
	   buttons: Ext.MessageBox.OKCANCEL,
	   multiline: true,
	   fn: function (btn, text) {
		   if (btn == 'ok')
		   {
			   if (!ae && text == '') {
				   alert("请输入内容");
				   getPrompt(t, m, f);
			   }
			   else
				   f(text);
		   }
		}
	});
}

function addCIRelation(cb, sc, oid)
{
	var form = new Ext.form.FormPanel({
		baseCls: 'x-plain',
		method: 'POST',
		baseParams: {oid: oid},
		defaultType: 'textfield',
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:ITSM_HOME+'/ci/ciRelationPost.jsp'
	});

	var ds = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: ITSM_HOME+'/ci/ciRelationTypeQuery.jsp'
		}),

		baseParams: { "oid": oid },
		// create reader that reads the Topic records
		reader: new Ext.data.JsonReader({
			root: 'items',
			totalProperty: 'totalCount'
		}, [
			{name: 'type_b', mapping: 'type_b'},
			{name: 'id', mapping: 'id'},
			{name: 'name', mapping: 'name'}
		]),

		// turn on remote sorting
		remoteSort: true
	});

	var ds2 = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: ITSM_HOME+'/ci/ciQuery.jsp'
		}),

		baseParams: { "oid": -1, start:0, limit: 99999999 },
		// create reader that reads the Topic records
		reader: new Ext.data.JsonReader({
			root: 'items',
			totalProperty: 'totalCount'
		}, [
			{name: 'oid', mapping: 'oid'},
			{name: 'name', mapping: 'name'}
		]),

		// turn on remote sorting
		remoteSort: true
	});

	var fld_type = new Ext.form.ComboBox({
		store: ds,
		hiddenName:'fld_type',
		displayField:'name',
		fieldLabel: '关联类型',
		valueField:'id',
		typeAhead: false,
		mode: 'remote',
		triggerAction: 'all',
		emptyText:'请选择类型',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});

	fld_type.on('select', function (obj, record, index){
			var t = parseInt(record.get('type_b'));
			this.baseParams.oid = t;
			this._combo.clearValue();
			this._combo.enable();
			this.reload();
		}, ds2);

	var fld_item = new Ext.form.ComboBox({
		store: ds2,
		hiddenName:'fld_item',
		displayField:'name',
		fieldLabel: '关联配置项',
		valueField:'oid',
		typeAhead: false,
		mode: 'remote',
		triggerAction: 'all',
		emptyText:'请选择配置项',
		forceSelection:true,
		selectOnFocus:true,
		allowBlank: false
	});
	ds2._combo = fld_item;

	form.add(
        fld_type,
        fld_item
	);

	if (window_ciRelation)
		window_ciRelation.close();
    window_ciRelation = new Ext.Window({
        title: '配置项关联',
		width:300,
		height:135,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: form,

        buttons: [{
            text: '确定',
            handler: function() {
				var form = window_ciRelation._form.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "请输入完整的数据");
					return;
				}
				form.submit({
					waitMsg: '正在上传....',
					success: function(form, action)
					{
						if (window_ciRelation._cb)
							window_ciRelation._cb(window_ciRelation._sc, form.errorReader.xmlData.documentElement.text);
						if (window_ciRelation) {
							window_ciRelation.close();
							window_ciRelation = null;
						}
					},
					failure: function(form, action)
					{
						Ext.MessageBox.alert("上传失败", form.errorReader.xmlData.documentElement.text);
					}
				});
            }
        },{
            text: '取消',
            handler: function() {
               window_ciRelation.close();
			   window_ciRelation = null;
            }
        }]
    });
	window_ciRelation._form = form;
	window_ciRelation._cb = cb;
	window_ciRelation._sc = sc;

    window_ciRelation.show();
}

Ext.form.CIRelationField = function(config){
    Ext.form.CIRelationField.superclass.constructor.call(this, config);
};

Ext.extend(Ext.form.CIRelationField, Ext.form.TextField,  {
    defaultAutoCreate : {tag: "input", type: "hidden", size: "16", autocomplete: "off"},
    hideTrigger:false,
	relations: [],
    autoSize: Ext.emptyFn,
    monitorTab : true,
    deferHeight : true,
	ciCategory: -1,

    onResize : function(w, h){
        Ext.form.CIRelationField.superclass.onResize.apply(this, arguments);
        if(typeof w == 'number'){
            this.el.setWidth(this.adjustWidth('input', w - this.trigger.getWidth()));
        }
    },

    adjustSize : Ext.BoxComponent.prototype.adjustSize,

    getResizeEl : function(){
        return this.wrap;
    },

    getPositionEl : function(){
        return this.wrap;
    },

    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
    },

    onRender : function(ct, position){
        Ext.form.CIRelationField.superclass.onRender.call(this, ct, position);
        this.wrap = this.el.wrap({cls: "x-form-field-wrap"});
        this.trigger = this.wrap.createChild(this.triggerConfig ||
                {tag: "img", src: ITSM_HOME+"/images/add.gif", style: "cursor:hand;vertical-align:text-bottom" });

		this.relationList = this.wrap.createChild({tag: "div"});

        if(this.hideTrigger){
            this.trigger.setDisplayed(false);
        }
        this.initTrigger();
        if(!this.width){
            this.wrap.setWidth(this.el.getWidth()+this.trigger.getWidth());
        }
		this.renderRelations();
    },

    // private
    initTrigger : function(){
        this.trigger.on("click", this.onTriggerClick, this, {preventDefault:true});
    },

    // private
    onDestroy : function(){
        if(this.trigger){
            this.trigger.removeAllListeners();
            this.trigger.remove();
        }
        if(this.wrap){
            this.wrap.remove();
        }
        Ext.form.CIRelationField.superclass.onDestroy.call(this);
    },

    // private
    onDisable : function(){
        Ext.form.CIRelationField.superclass.onDisable.call(this);
        if(this.wrap){
            this.wrap.addClass('x-item-disabled');
        }
    },

    // private
    onEnable : function(){
        Ext.form.CIRelationField.superclass.onEnable.call(this);
        if(this.wrap){
            this.wrap.removeClass('x-item-disabled');
        }
    },

    // private
    onShow : function(){
        if(this.wrap){
            this.wrap.dom.style.display = '';
            this.wrap.dom.style.visibility = 'visible';
        }
    },

    // private
    onHide : function(){
        this.wrap.dom.style.display = 'none';
    },

	removeRelation : function(i) {
		var n = this.relations.length;
		for (var j = i; j < n - 1; j++)
			this.relations[j] = this.relations[j + 1];
		this.relations.length = n - 1;
		this.renderRelations();
		this.calcValue();
	},

	calcValue : function() {
		var v = "";
		for (var i = 0; i < this.relations.length; i++) {
			if (i > 0)
				v += ";";
			v += this.relations[i][0];
		}
        Ext.form.CIRelationField.superclass.setValue.call(this, v);
	},

	setValue : function(v) {
		v = "" + v;
		var s = v.split(";");
		this.relations = [];
		for (var i = 0; i < s.length; i++) {
			if (s[i] == "")
				continue;
			var s1 = s[i].split(",");
			this.relations[this.relations.length] = [s1[0], s1[1]];
		}
		this.renderRelations();
		this.calcValue();
	},

	renderRelations : function() {
		if (!this.relationList)
			return;
		this.relationList.dom.innerHTML = "";
		for (var i = 0; i < this.relations.length; i++)
		{
			if (i > 0)
				this.relationList.createChild({tag: "br"});
			var p = this.relationList.createChild({tag: "img", src: ITSM_HOME+"/images/delete.gif",
				style: "cursor:hand;vertical-align:text-bottom"});
			p._id = i;
			p._obj = this;
			p.on("click", function() { this._obj.removeRelation(this._id); }, p);
			this.relationList.createChild({tag: "span"}).dom.innerText = " " + this.relations[i][1];
		}
	},

	onRelationAdd : function(obj, t) {
		var s1 = t.split(",");
		obj.relations[obj.relations.length] = [s1[0], s1[1]];
		obj.renderRelations();
		obj.calcValue();
	},

    onTriggerClick : function()
	{
		addCIRelation(this.onRelationAdd, this, this.ciCategory);
	}
});


	function calcPoints(x1, y1, w1, h1, p1, x2, y2, w2, h2, p2, delta)
	{
		if (p1 == 1) {
			x1 += w1 / 2;
		} else if (p1 == 2) {
			y1 += h1 / 2;
		} else if (p1 == 3) {
			x1 += w1;
			y1 += h1 / 2;
		} else {
			x1 += w1 / 2;
			y1 += h1;
		}
		if (p2 == 1) {
			x2 += w2 / 2;
		} else if (p2 == 2) {
			y2 += h2 / 2;
		} else if (p2 == 3) {
			x2 += w2;
			y2 += h2 / 2;
		} else {
			x2 += w2 / 2;
			y2 += h2;
		}
		// 调整位置
		if (x2 > x1)
			x2 -= delta;
		else
			x2 += delta;
		if (y2 > y1)
			y2 -= delta;
		else
			y2 += delta;

		var x0 = (x1 + x2) / 2;
		var y0 = (y1 + y2) / 2;
		var type = 1;
		if ((p1 == 4 && p2 == 1) || (p1 == 1 && p2 == 4))
			type = 0;
		else if ((p1 == 2 || p1 == 3) && (p2 == 2 || p2 == 3))
			type = 2;
		else if (p1 == 2 || p1 == 3)
			type = 3;
		else
			type = 1;
		var str;
		var dd = 10;
		if (p1 == 1 && p2 == 1)
			str = "" + x1 + "," + y1 + " " + (x1) + "," + (y1 - dd) + " " + x2 + "," + (y2 - dd) + " " + x2 + "," + y2;
		else if (p1 == 2 && p2 == 2)
			str = "" + x1 + "," + y1 + " " + (x1 - dd) + "," + (y1) + " " + (x2 - dd) + "," + (y2) + " " + x2 + "," + y2;
		else if (p1 == 3 && p2 == 3)
			str = "" + x1 + "," + y1 + " " + (x1 + dd) + "," + (y1) + " " + (x2 + dd) + "," + (y2) + " " + x2 + "," + y2;
		else if (p1 == 4 && p2 == 4)
			str = "" + x1 + "," + y1 + " " + (x1) + "," + (y1 + dd) + " " + x2 + "," + (y2 + dd) + " " + x2 + "," + y2;
		else if (type == 0)
			str = "" + x1 + "," + y1 + " " + x1 + "," + y0 + " " + x2 + "," + y0 + " " + x2 + "," + y2;
		else if (type == 1)
			str = "" + x1 + "," + y1 + " " + x1 + "," + y2 + " " + x2 + "," + y2;
		else if (type == 2)
			str = "" + x1 + "," + y1 + " " + x0 + "," + y1 + " " + x0 + "," + y2 + " " + x2 + "," + y2;
		else if (type == 3)
			str = "" + x1 + "," + y1 + " " + x2 + "," + y1 + " " + x2 + "," + y2;

		return str;
	}

	Date.prototype.Format = function(fmt) 
	{ 
	  var o = { 
	    "M+" : this.getMonth()+1,                 //月份 
	    "d+" : this.getDate(),                    //日 
	    "h+" : this.getHours(),                   //小时 
	    "m+" : this.getMinutes(),                 //分 
	    "s+" : this.getSeconds(),                 //秒 
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
	    "S"  : this.getMilliseconds()             //毫秒 
	  }; 
	  if(/(y+)/.test(fmt)) 
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	  for(var k in o) 
		  if(new RegExp("("+ k +")").test(fmt)) 
			  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	  return fmt; 
	} 
