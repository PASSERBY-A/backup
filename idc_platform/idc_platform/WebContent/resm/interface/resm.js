Ext.form.PersonField = Ext.extend(Ext.form.SelectDialogField, {
	editable:false,
	mutiModel:false,
	hideTriggers:[true,true],
  initComponent : function(){
      Ext.form.PersonField.superclass.initComponent.call(this);

      this.triggerConfig = {
          tag:'span', cls:'x-form-twin-triggers', cn:[
          {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
          {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class,title: "Ñ¡Ôñ"}
      ]};
  },
  onTriggerClick : function() {
  	this.onTrigger3Click();
  },
  onTrigger2Click : function(){
  	  var height=480,width=640;
  	  var ua = navigator.userAgent;
		  if(ua.lastIndexOf("MSIE 6.0") != -1){
			  if(ua.lastIndexOf("Windows NT 5.1") != -1){
			    height += 49;
			  }
			  else if(ua.lastIndexOf("Windows NT 5.0") != -1){
			    height += 49;
			  }
			}
  	  var xposition = (screen.width - width) / 2;
		  var yposition = (screen.height - height) / 2;
		  var urls = (this.selectUrl?this.selectUrl:"/idc_platform/resm")+"/interface/personList.jsp?id=" + this.getValue();
		  if (this.mutiModel)
		  	urls += "&muti=1";

  		var rv = window.showModalDialog(urls,'','dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
			if (rv)
				this.setValue(rv);
  },
  onTrigger3Click : function(){

  	if (this.value && this.value.length>0) {
  		var height=500,width=700;
  	  var xposition = (screen.width - width) / 2;
		  var yposition = (screen.height - height) / 2;
		  var urls = (this.selectUrl?this.selectUrl:"/idc_platform/resm")+"/interface/resView.jsp?id=" + this.getValue();
			window.showModalDialog(urls,'','dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
  	}
  }
});

Ext.reg('personfield', Ext.form.PersonField);

Ext.form.ResourceField = Ext.extend(Ext.form.SelectDialogField, {
	editable:false,
	mutiModel:false,
	hideTriggers:[true,true],
  initComponent : function(){
      Ext.form.PersonField.superclass.initComponent.call(this);

      this.triggerConfig = {
          tag:'span', cls:'x-form-twin-triggers', cn:[
          {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
          {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class,title: "Ñ¡Ôñ"}
      ]};
  },
  onTriggerClick : function() {
  	this.onTrigger3Click();
  },
  onTrigger2Click : function(){
  	  var height=480,width=640;
  	  var ua = navigator.userAgent;
		  if(ua.lastIndexOf("MSIE 6.0") != -1){
			  if(ua.lastIndexOf("Windows NT 5.1") != -1){
			    height += 49;
			  }
			  else if(ua.lastIndexOf("Windows NT 5.0") != -1){
			    height += 49;
			  }
			}
  	  var xposition = (screen.width - width) / 2;
		  var yposition = (screen.height - height) / 2;
		  var urls = (this.selectUrl?this.selectUrl:"/idc_platform/resm")+"/interface/resourceList.jsp?model=" + this.modelId + "&id=" + this.getValue();
		  if (this.mutiModel)
		  	urls += "&muti=1";

  		var rv = window.showModalDialog(urls,'','dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
			if (rv)
				this.setValue(rv);
  },
  onTrigger3Click : function(){

  	if (this.value && this.value.length>0) {
  		var height=500,width=700;
  	  var xposition = (screen.width - width) / 2;
		  var yposition = (screen.height - height) / 2;
		  var urls = (this.selectUrl?this.selectUrl:"/idc_platform/resm")+"/interface/resView.jsp?id=" + this.getValue();
			window.showModalDialog(urls,'','dialogWidth='+width+'px;dialogHeight='+height+'px;dialogLeft='+xposition+'px;dialogTop='+yposition+'px;center=yes;status=no;help=no;resizable=no;scroll=no');
  	}
  }
});

Ext.reg('resourcefield', Ext.form.ResourceField);
