(function(cloudStack) {
    cloudStack.sections.resourcepools = {
    		id:"resourcepools",
    		title:'资源池列表',
    		 sectionSelect: {
    	            label: 'Select View',
    	            preFilter: function() {
    	                return ['resourcepools'];
    	            }
    	        },
    		sections:{
    			resourcepools:{
    				title:'资源池列表',
    				 type: 'select',
    				id:"resourcepools",
    				listView: {
    					id:"resourcepools",
    		    		fields:{
    		    			name : {
    		    				label : "名称"
    		    			},
    		    			desc:{
    		    				label : "描述"
    		    			}
    		    		},
    		    		filters : false,
    		    		dataProvider: function(args) {
    		    			 $.ajax({
    		                     url: createURL("listresourcepool&cmsz=yes&response=json"),
    		                     async: true,
    		                     success: function(json) {
    		                    	 var resourcepoolObjs = json.root;
    		                         args.response.success({
    		                             //actionFilter: zoneActionfilter,
    		                             data: resourcepoolObjs
    		                         });
    		                     }
    		                 });
    		    		},
    		    		detailView:{
    			    		//不显示快速查看
    		    			quickView : false,
    		    			isMaximized: true,
    		    			tabs : {
    		    				details :{
    		    					title : "详细信息",
    		    					listView : {
    		    						 id: 'physicalResources',
    		                             label: 'label.menu.physical.resources',
    		                             fields: {
    		                                 name: {
    		                                     label: 'label.zone'
    		                                 },
    		                                 networktype: {
    		                                     label: 'label.network.type'
    		                                 },
    		                                 domainid: {
    		                                     label: 'label.public',
    		                                     converter: function(args) {
    		                                         if (args == null)
    		                                             return "Yes";
    		                                         else
    		                                             return "No";
    		                                     }
    		                                 },
    		                                 allocationstate: {
    		                                     label: 'label.allocation.state',
    		                                     converter: function(str) {
    		                                         // For localization
    		                                         return str;
    		                                     },
    		                                     indicator: {
    		                                         'Enabled': 'on',
    		                                         'Disabled': 'off'
    		                                     }
    		                                 }
    		                             },

    		                             dataProvider: function(args) {
    		                                 var array1 = [];
    		                                 if (args.filterBy != null) {
    		                                     if (args.filterBy.search != null && args.filterBy.search.by != null && args.filterBy.search.value != null) {
    		                                         switch (args.filterBy.search.by) {
    		                                             case "name":
    		                                                 if (args.filterBy.search.value.length > 0)
    		                                                     array1.push("&keyword=" + args.filterBy.search.value);
    		                                                 break;
    		                                         }
    		                                     }
    		                                 }
    		                                 $.ajax({
    		                                     url: createURL("listsubresource&cmsz=yes&response=json&resourcePoolId="+args.context.resourcepools[0].resourcePoolId+ array1.join("")),
    		                                     dataType: "json",
    		                                     async: true,
    		                                     success: function(json) {
    		                                         zoneObjs = json.listzonesresponse.zone;
    		                                         args.response.success({
    		                                             actionFilter: zoneActionfilter,
    		                                             data: zoneObjs
    		                                         });
    		                                     }
    		                                 });
    		                             },

    		                             actions: {
    		                            	 listZone: {
    		                            		 isHeader: true,
    		                                     label: '设置关联ZONE',
    		                                     listView : {
    		                                    	 firstClick : false,
    		                                		 id: 'configRZ',
    		        	                             label: '配置二级资源池',
    		        	                             type : "checkbox",
    		        	                             fields: {
    		        	                                 name: {
    		        	                                     label: 'label.zone'
    		        	                                 },
    		        	                                 networktype: {
    		        	                                     label: 'label.network.type'
    		        	                                 },
    		        	                                 domainid: {
    		        	                                     label: 'label.public',
    		        	                                     converter: function(args) {
    		        	                                         if (args == null)
    		        	                                             return "Yes";
    		        	                                         else
    		        	                                             return "No";
    		        	                                     }
    		        	                                 },
    		        	                                 allocationstate: {
    		        	                                     label: 'label.allocation.state',
    		        	                                     converter: function(str) {
    		        	                                         // For localization
    		        	                                         return str;
    		        	                                     },
    		        	                                     indicator: {
    		        	                                         'Enabled': 'on',
    		        	                                         'Disabled': 'off'
    		        	                                     }
    		        	                                 }
    		        	                             },

    		        	                             dataProvider: function(args) {
    		        	                                 var array1 = [];
    		        	                                 if (args.filterBy != null) {
    		        	                                     if (args.filterBy.search != null && args.filterBy.search.by != null && args.filterBy.search.value != null) {
    		        	                                         switch (args.filterBy.search.by) {
    		        	                                             case "name":
    		        	                                                 if (args.filterBy.search.value.length > 0)
    		        	                                                     array1.push("&keyword=" + args.filterBy.search.value);
    		        	                                                 break;
    		        	                                         }
    		        	                                     }
    		        	                                 }
    		        	                                 $.ajax({
    		        	                                     url: createURL("listavailableresource&cmsz=yes&response=json&resourcePoolId="+args.context.resourcepools[0].resourcePoolId+ array1.join("")),
    		        	                                     dataType: "json",
    		        	                                     async: true,
    		        	                                     success: function(json) {
    		        	                                         zoneObjs = json.listzonesresponse.zone;
    		        	                                         args.response.success({
    		        	                                             actionFilter: zoneActionfilter,
    		        	                                             data: zoneObjs
    		        	                                         });
    		        	                                     }
    		        	                                 });
    		        	                             }
    	                                    	 },
    		                                     action:function(args){
    		                                    	 var zones = args.context.instances;
    		                                    	 var resourcePoolId = args.context.resourcepools[0].resourcePoolId;
    		                                    	 var zoneIds = "";
    		                                    	 for(var i =0;i<zones.length;++i){
    		                                    		 zoneIds +="&zoneId="+ zones[i].id;
    		                                    	 }
    		                                    	 var queryStr = "resourcePoolId="+resourcePoolId+zoneIds;
    		                                    	 $.ajax({
    	        	                                     url: createURL("configzone&cmsz=yes&response=json&"+ queryStr),
    	        	                                     dataType: "json",
    	        	                                     async: true,
    	        	                                     success: function(json) {
    	        	                                    	 
    	        	                                    	 $(".button.refresh").trigger("click");
    	        	                                    	
    	        	                                    	
    	        	                                    	 //args.closest('.list-view').listView('refresh');
    	        	                                        /* zoneObjs = json.listzonesresponse.zone;
    	        	                                         args.response.success({
    	        	                                             actionFilter: zoneActionfilter,
    	        	                                             data: zoneObjs
    	        	                                         });*/

    	        	                                    	 args.response.success({
    	        	                                    		 _custom: {
    	        	                                                   // jobId: jid,
    	        	                                                    getUpdatedItem: function(json) {
    	        	                                                    	//console.info(json);
    	        	                                                        return {}; //nothing in this account needs to be updated, in fact, this whole account has being deleted
    	        	                                                    }
    	        	                                                }
    	                                                     
    	        	                                              });
    	        	                                    	 //$(window).trigger('cloudStack.fullRefresh');
    	                                                 
    	        	                                     }
    	        	                                 });
    		                                    	 
    		                                     },
    		                                     messages: {
    	                                             notification: function(args) {
    	                                            	 return '设置关联ZONE';
    	                                             }
    	                                         },
    		                                     notification: {
    		                                         poll: function(args) {
    		                                             args.complete({
    		                                                 //actionFilter: zoneActionfilter,
    		                                                 //data: args._custom.zone
    		                                            	 //args.complete();
    		                                             });
    		                                         }
    		                                     }
    		                                 }
    		                             
    		                             },

    		                             detailView: {
    		                                 isMaximized: true,
    		                                 actions: {
    		                                     addVmwareDc: {
    		                                        // label: 'Add VMware datacenter',
    		                                     	label : '添加虚拟数据中心',
    		                                     	textLabel:'添加虚拟数据中心',
    		                                     	//textLabel: 'Add VMware datacenter',
    		                                         messages: {
    		                                             notification: function(args) {
    		                                                 //return 'Add VMware datacenter';
    		                                             	return '添加虚拟数据中心';
    		                                             }
    		                                         },
    		                                         createForm: {
    		                                            // title: 'Add VMware datacenter',
    		                                         	 title: '添加虚拟数据中心',
    		                                             fields: {
    		                                                 name: {
    		                                                     label: 'DC Name',
    		                                                     validation: {
    		                                                         required: true
    		                                                     }
    		                                                 },
    		                                                 vcenter: {
    		                                                     label: 'vcenter',
    		                                                     validation: {
    		                                                         required: true
    		                                                     }
    		                                                 },
    		                                                 username: {
    		                                                     label: 'label.username',
    		                                                     validation: {
    		                                                         required: true
    		                                                     }
    		                                                 },
    		                                                 password: {
    		                                                     label: 'label.password',
    		                                                     isPassword: true,
    		                                                     validation: {
    		                                                         required: true
    		                                                     }
    		                                                 },
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             var data = {
    		                                                 zoneid: args.context.physicalResources[0].id,
    		                                                 name: args.data.name,
    		                                                 vcenter: args.data.vcenter
    		                                             };

    		                                             if (args.data.username != null && args.data.username.length > 0) {
    		                                                 $.extend(data, {
    		                                                     username: args.data.username
    		                                                 })
    		                                             }
    		                                             if (args.data.password != null && args.data.password.length > 0) {
    		                                                 $.extend(data, {
    		                                                     password: args.data.password
    		                                                 })
    		                                             }

    		                                             $.ajax({
    		                                                 url: createURL('addVmwareDc'),
    		                                                 data: data,
    		                                                 type: "POST",
    		                                                 success: function(json) {
    		                                                     //var item = json.addvmwaredcresponse.vmwaredc;
    		                                                     args.response.success();
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     },

    		                                     removeVmwareDc: {
    		                                         //label: 'Remove VMware datacenter',
    		                                     	label : '移除VM数据中心',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                             	return '请确认要移除VM数据中心';
    		                                                // return 'Please confirm you want to remove VMware datacenter';
    		                                             },
    		                                             notification: function(args) {
    		                                                 //return 'Remove VMware datacenter';
    		                                             	return '移除VM数据中心';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             var data = {
    		                                                 zoneid: args.context.physicalResources[0].id
    		                                             };
    		                                             $.ajax({
    		                                                 url: createURL('removeVmwareDc'),
    		                                                 data: data,
    		                                                 success: function(json) {
    		                                                 	delete args.context.physicalResources[0].vmwaredcName;
    		                                                 	delete args.context.physicalResources[0].vmwaredcVcenter;
    		                                                 	delete args.context.physicalResources[0].vmwaredcId;
    		                                                 	
    		                                                 	selectedZoneObj = args.context.physicalResources[0];
    		                                                 	
    		                                                     args.response.success({                                                        
    		                                                         data: args.context.physicalResources[0]
    		                                                     });
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     },

    		                                     enable: {
    		                                         label: 'label.action.enable.zone',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return 'message.action.enable.zone';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return 'label.action.enable.zone';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL("updateZone&id=" + args.context.physicalResources[0].id + "&allocationstate=Enabled"), //embedded objects in listView is called physicalResources while embedded objects in detailView is called zones
    		                                                 dataType: "json",
    		                                                 async: true,
    		                                                 success: function(json) {
    		                                                     var item = json.updatezoneresponse.zone;
    		                                                     args.response.success({
    		                                                         actionFilter: zoneActionfilter,
    		                                                         data: item
    		                                                     });
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     },

    		                                     disable: {
    		                                         label: 'label.action.disable.zone',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return 'message.action.disable.zone';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return 'label.action.disable.zone';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL("updateZone&id=" + args.context.physicalResources[0].id + "&allocationstate=Disabled"), //embedded objects in listView is called physicalResources while embedded objects in detailView is called zones
    		                                                 dataType: "json",
    		                                                 async: true,
    		                                                 success: function(json) {
    		                                                     var item = json.updatezoneresponse.zone;
    		                                                     args.response.success({
    		                                                         actionFilter: zoneActionfilter,
    		                                                         data: item
    		                                                     });
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     },

    		                                     dedicateZone: {
    		                                        // label: 'Dedicate Zone',
    		                                     	label : '专有化Zone',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return 'Do you really want to dedicate this zone to a domain/account? ';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return 'Zone Dedicated';
    		                                             }
    		                                         },
    		                                         createForm: {
    		                                             //title: 'Dedicate Zone',
    		                                         	title : '专有化Zone',
    		                                             fields: {
    		                                                 domainId: {
    		                                                     label: 'Domain',
    		                                                     validation: {
    		                                                         required: true
    		                                                     },
    		                                                     select: function(args) {
    		                                                         $.ajax({
    		                                                             url: createURL("listDomains&listAll=true"),
    		                                                             dataType: "json",
    		                                                             async: false,
    		                                                             success: function(json) {
    		                                                                 var domainObjs = json.listdomainsresponse.domain;
    		                                                                 var items = [];

    		                                                                 $(domainObjs).each(function() {
    		                                                                     items.push({
    		                                                                         id: this.id,
    		                                                                         description: this.name
    		                                                                     });
    		                                                                 });

    		                                                                 args.response.success({
    		                                                                     data: items
    		                                                                 });
    		                                                             }
    		                                                         });
    		                                                     }
    		                                                 },
    		                                                 accountId: {
    		                                                     label: 'Account',
    		                                                     docID: 'helpAccountForDedication',
    		                                                     validation: {
    		                                                         required: false
    		                                                     }
    		                                                 }
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             //EXPLICIT DEDICATION
    		                                             var array2 = [];
    		                                             if (args.data.accountId != "")
    		                                                 array2.push("&account=" + todb(args.data.accountId));

    		                                             $.ajax({
    		                                                 url: createURL("dedicateZone&zoneId=" +
    		                                                     args.context.physicalResources[0].id +
    		                                                     "&domainId=" + args.data.domainId + array2.join("")),
    		                                                 dataType: "json",
    		                                                 success: function(json) {
    		                                                     var jid = json.dedicatezoneresponse.jobid;
    		                                                     args.response.success({
    		                                                         _custom: {
    		                                                             jobId: jid,
    		                                                             getActionFilter: function() {
    		                                                                 return zoneActionfilter;
    		                                                             }
    		                                                         }
    		                                                     });
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: pollAsyncJobResult
    		                                         }
    		                                     },
    		                                     releaseDedicatedZone: {
    		                                         label: 'Release Dedicated Zone',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return 'Do you want to release this dedicated zone ?';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return 'Zone dedication released';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL("releaseDedicatedZone&zoneid=" +
    		                                                     args.context.physicalResources[0].id),
    		                                                 dataType: "json",
    		                                                 async: true,
    		                                                 success: function(json) {
    		                                                     var jid = json.releasededicatedzoneresponse.jobid;
    		                                                     args.response.success({
    		                                                         _custom: {
    		                                                             jobId: jid,
    		                                                             getActionFilter: function() {
    		                                                                 return zoneActionfilter;
    		                                                             }
    		                                                         }
    		                                                     });
    		                                                 },
    		                                                 error: function(json) {
    		                                                     args.response.error(parseXMLHttpResponse(json));
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: pollAsyncJobResult
    		                                         }
    		                                     },

    		                                     'remove': {
    		                                         label: 'label.action.delete.zone',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return 'message.action.delete.zone';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return 'label.action.delete.zone';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL("deleteZone&id=" + args.context.physicalResources[0].id), //embedded objects in listView is called physicalResources while embedded objects in detailView is called zones
    		                                                 dataType: "json",
    		                                                 async: true,
    		                                                 success: function(json) {
    		                                                     args.response.success({
    		                                                         data: {}
    		                                                     });
    		                                                 },
    		                                                 error: function(json) {
    		                                                     args.response.error(parseXMLHttpResponse(json));
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     },
    		                                     "deleteResourcePoolRL":{
    		                                    	 label: '解除一级资源池关联',
    		                                         messages: {
    		                                             confirm: function(args) {
    		                                                 return '解除一级资源池关联';
    		                                             },
    		                                             notification: function(args) {
    		                                                 return '解除一级资源池关联';
    		                                             }
    		                                         },
    		                                         action: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL("removerelation"), 
    		                                                 data : {
    		                                                	cmsz : "yes",
    		                                                	response: "json",
    		                                                	resourcePoolId : args.context.resourcepools[0].resourcePoolId,
    		                                                	zoneId : args.data.id
    		                                                 },
    		                                                 async: true,
    		                                                 success: function(json) {
    		                                                     args.response.success({
    		                                                         data: {}
    		                                                     });
    		                                                     $(window).trigger('cloudStack.fullRefresh');
    		                                                 },
    		                                                 error: function(json) {
    		                                                     args.response.error(parseXMLHttpResponse(json));
    		                                                 }
    		                                             });
    		                                         },
    		                                         notification: {
    		                                             poll: function(args) {
    		                                                 args.complete();
    		                                             }
    		                                         }
    		                                     
    		                                     },
    		                                     edit: {
    		                                         label: 'label.edit',
    		                                         action: function(args) {
    		                                             var array1 = [];
    		                                             array1.push("&name=" + todb(args.data.name));
    		                                             array1.push("&dns1=" + todb(args.data.dns1));
    		                                             array1.push("&dns2=" + todb(args.data.dns2)); //dns2 can be empty ("") when passed to API, so a user gets to update this field from an existing value to blank.
    		                                             array1.push("&ip6dns1=" + todb(args.data.ip6dns1)); //p6dns1 can be empty ("") when passed to API, so a user gets to update this field from an existing value to blank.
    		                                             array1.push("&ip6dns2=" + todb(args.data.ip6dns2)); //ip6dns2 can be empty ("") when passed to API, so a user gets to update this field from an existing value to blank.

    		                                             if (selectedZoneObj.networktype == "Advanced" && args.data.guestcidraddress) {
    		                                                 array1.push("&guestcidraddress=" + todb(args.data.guestcidraddress));
    		                                             }

    		                                             array1.push("&internaldns1=" + todb(args.data.internaldns1));
    		                                             array1.push("&internaldns2=" + todb(args.data.internaldns2)); //internaldns2 can be empty ("") when passed to API, so a user gets to update this field from an existing value to blank.
    		                                             array1.push("&domain=" + todb(args.data.domain));
    		                                             array1.push("&localstorageenabled=" + (args.data.localstorageenabled == 'on'));
    		                                             $.ajax({
    		                                                 url: createURL("updateZone&id=" + args.context.physicalResources[0].id + array1.join("")),
    		                                                 dataType: "json",
    		                                                 async: false,
    		                                                 success: function(json) {
    		                                                     selectedZoneObj = json.updatezoneresponse.zone; //override selectedZoneObj after update zone
    		                                                     args.response.success({
    		                                                         data: selectedZoneObj
    		                                                     });
    		                                                 },
    		                                                 error: function(json) {
    		                                                     args.response.error('Could not edit zone information; please ensure all fields are valid.');
    		                                                 }
    		                                             });
    		                                         }
    		                                     }
    		                                 },
    		                                 tabs: {
    		                                     details: {
    		                                         title: 'label.details',

    		                                         preFilter: function(args) {
    		                                             var hiddenFields = [];
    		                                             if (selectedZoneObj.networktype == "Basic")
    		                                                 hiddenFields.push("guestcidraddress");
    		                                             return hiddenFields;
    		                                         },

    		                                         fields: [{
    		                                             name: {
    		                                                 label: 'label.zone',
    		                                                 isEditable: true,
    		                                                 validation: {
    		                                                     required: true
    		                                                 }
    		                                             }
    		                                         }, {
    		                                             id: {
    		                                                 label: 'label.id'
    		                                             },
    		                                             allocationstate: {
    		                                                 label: 'label.allocation.state'
    		                                             },
    		                                             dns1: {
    		                                                 label: 'label.dns.1',
    		                                                 isEditable: true,
    		                                                 validation: {
    		                                                     required: true
    		                                                 }
    		                                             },
    		                                             dns2: {
    		                                                 label: 'label.dns.2',
    		                                                 isEditable: true
    		                                             },
    		                                             ip6dns1: {
    		                                                 label: 'IPv6 DNS1',
    		                                                 isEditable: true
    		                                             },
    		                                             ip6dns2: {
    		                                                 label: 'IPv6 DNS2',
    		                                                 isEditable: true
    		                                             },
    		                                             internaldns1: {
    		                                                 label: 'label.internal.dns.1',
    		                                                 isEditable: true,
    		                                                 validation: {
    		                                                     required: true
    		                                                 }
    		                                             },
    		                                             internaldns2: {
    		                                                 label: 'label.internal.dns.2',
    		                                                 isEditable: true
    		                                             },
    		                                             domainname: {
    		                                                 label: 'label.domain'
    		                                             },
    		                                             networktype: {
    		                                                 label: 'label.network.type'
    		                                             },
    		                                             guestcidraddress: {
    		                                                 label: 'label.guest.cidr',
    		                                                 isEditable: true
    		                                             },
    		                                             domain: {
    		                                                 label: 'label.network.domain',
    		                                                 isEditable: true
    		                                             },
    		                                             localstorageenabled: {
    		                                                 label: 'label.local.storage.enabled',
    		                                                 isBoolean: true,
    		                                                 isEditable: true,
    		                                                 converter: cloudStack.converters.toBooleanText
    		                                             }
    		                                         }, {
    		                                             isdedicated: {
    		                                                 label: 'Dedicated'
    		                                             },
    		                                             domainid: {
    		                                                 label: 'Domain ID'
    		                                             }
    		                                         }, {
    		                                             vmwaredcName: {
    		                                                 label: 'VMware datacenter Name'
    		                                             },
    		                                             vmwaredcVcenter: {
    		                                                 label: 'VMware datacenter vcenter'
    		                                             },
    		                                             vmwaredcId: {
    		                                                 label: 'VMware datacenter Id'
    		                                             }
    		                                         }],
    		                                         dataProvider: function(args) {
    		                                             $.ajax({
    		                                                 url: createURL('listZones'),
    		                                                 data: {
    		                                                     id: args.context.physicalResources[0].id
    		                                                 },
    		                                                 success: function(json) {
    		                                                     selectedZoneObj = json.listzonesresponse.zone[0];
    		                                                     $.ajax({
    		                                                         url: createURL('listDedicatedZones'),
    		                                                         data: {
    		                                                         	zoneid: args.context.physicalResources[0].id
    		                                                         },
    		                                                         async: false,
    		                                                         success: function(json) {                                                       
    		                                                             if (json.listdedicatedzonesresponse.dedicatedzone != undefined) {
    		                                                                 var dedicatedzoneObj = json.listdedicatedzonesresponse.dedicatedzone[0];
    		                                                                 if (dedicatedzoneObj.domainid != null) {
    		                                                                     $.extend(selectedZoneObj, {
    		                                                                         isdedicated: 'Yes',
    		                                                                         domainid: dedicatedzoneObj.domainid,
    		                                                                         accountid: dedicatedzoneObj.accountid
    		                                                                     });
    		                                                                 }
    		                                                             } else {
    		                                                                 $.extend(selectedZoneObj, {
    		                                                                     isdedicated: 'No',
    		                                                                     domainid: null,
    		                                                                     accountid: null
    		                                                                 })
    		                                                             }
    		                                                         }
    		                                                     });

    		                                                     $.ajax({
    		                                                     	url: createURL('listClusters'),
    		                                                     	data: {
    		                                                             zoneid: args.context.physicalResources[0].id
    		                                                         },
    		                                                         async: false,
    		                                                         success: function(json) {                                                        	
    		                                                         	var clusters = json.listclustersresponse.cluster;
    		                                                         	if (clusters != null) {
    		                                                         		for (var i = 0; i < clusters.length; i++) {                                                        			
    		                                                         			if (clusters[i].hypervisortype == 'VMware') {                                                        				
    		                                                         				$.ajax({
    		                                                                             url: createURL('listVmwareDcs'), //listVmwareDcs API exists in only non-oss bild
    		                                                                             data: {
    		                                                                                 zoneid: args.context.physicalResources[0].id
    		                                                                             },
    		                                                                             async: false,
    		                                                                             success: function(json) { //e.g. json == { "listvmwaredcsresponse" { "count":1 ,"VMwareDC" [ {"id":"c3c2562d-65e9-4fc7-92e2-773c2efe8f37","zoneid":1,"name":"datacenter","vcenter":"10.10.20.20"} ] } }
    		                                                                                 var vmwaredcs = json.listvmwaredcsresponse.VMwareDC;
    		                                                                                 if (vmwaredcs != null) {
    		                                                                                     selectedZoneObj.vmwaredcName = vmwaredcs[0].name;
    		                                                                                     selectedZoneObj.vmwaredcVcenter = vmwaredcs[0].vcenter;
    		                                                                                     selectedZoneObj.vmwaredcId = vmwaredcs[0].id;
    		                                                                                 }
    		                                                                             }
    		                                                                             //, error: function(XMLHttpResponse) {} //override default error handling: cloudStack.dialog.notice({ message: parseXMLHttpResponse(XMLHttpResponse)});   
    		                                                                         });                                                        				
    		                                                         				
    		                                                         				break;
    		                                                         			}
    		                                                         		}                                                        		
    		                                                         	}                                                        	
    		                                                         }
    		                                                     });                         
    		                                                     
    		                                                     args.response.success({
    		                                                         actionFilter: zoneActionfilter,
    		                                                         data: selectedZoneObj
    		                                                     });
    		                                                 }
    		                                             });
    		                                         }
    		                                     },

    		                                     compute: {
    		                                         title: 'label.compute.and.storage',
    		                                         custom: cloudStack.uiCustom.systemChart('compute')
    		                                     },
    		                                     network: {
    		                                         title: 'label.physical.network',
    		                                         custom: cloudStack.uiCustom.systemChart('network')
    		                                     },
    		                                     resources: {
    		                                         title: 'label.resources',
    		                                         custom: cloudStack.uiCustom.systemChart('resources')
    		                                     },

    		                                     systemVMs: {
    		                                         title: 'label.system.vms',
    		                                         listView: {
    		                                             label: 'label.system.vms',
    		                                             id: 'systemVMs',
    		                                             fields: {
    		                                                 name: {
    		                                                     label: 'label.name'
    		                                                 },
    		                                                 systemvmtype: {
    		                                                     label: 'label.type',
    		                                                     converter: function(args) {
    		                                                         if (args == "consoleproxy")
    		                                                             return "Console Proxy VM";
    		                                                         else if (args == "secondarystoragevm")
    		                                                             return "Secondary Storage VM";
    		                                                         else
    		                                                             return args;
    		                                                     }
    		                                                 },
    		                                                 zonename: {
    		                                                     label: 'label.zone'
    		                                                 },
    		                                                 state: {
    		                                                     label: 'label.status',
    		                                                     converter: function(str) {
    		                                                         // For localization
    		                                                         return str;
    		                                                     },
    		                                                     indicator: {
    		                                                         'Running': 'on',
    		                                                         'Stopped': 'off',
    		                                                         'Error': 'off',
    		                                                         'Destroyed': 'off'
    		                                                     }
    		                                                 }
    		                                             },
    		                                             dataProvider: function(args) {
    		                                                 var array1 = [];
    		                                                 if (args.filterBy != null) {
    		                                                     if (args.filterBy.search != null && args.filterBy.search.by != null && args.filterBy.search.value != null) {
    		                                                         switch (args.filterBy.search.by) {
    		                                                             case "name":
    		                                                                 if (args.filterBy.search.value.length > 0)
    		                                                                     array1.push("&keyword=" + args.filterBy.search.value);
    		                                                                 break;
    		                                                         }
    		                                                     }
    		                                                 }

    		                                                 var selectedZoneObj = args.context.physicalResources[0];
    		                                                 $.ajax({
    		                                                     url: createURL("listSystemVms&zoneid=" + selectedZoneObj.id + "&page=" + args.page + "&pagesize=" + pageSize + array1.join("")),
    		                                                     dataType: "json",
    		                                                     async: true,
    		                                                     success: function(json) {
    		                                                         var items = json.listsystemvmsresponse.systemvm;
    		                                                         args.response.success({
    		                                                             actionFilter: systemvmActionfilter,
    		                                                             data: items
    		                                                         });
    		                                                     }
    		                                                 });
    		                                             },

    		                                             detailView: {
    		                                                 noCompact: true,
    		                                                 name: 'System VM details',
    		                                                 actions: {
    		                                                     start: {
    		                                                         label: 'label.action.start.systemvm',
    		                                                         messages: {
    		                                                             confirm: function(args) {
    		                                                                 return 'message.action.start.systemvm';
    		                                                             },
    		                                                             notification: function(args) {
    		                                                                 return 'label.action.start.systemvm';
    		                                                             }
    		                                                         },
    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL('startSystemVm&id=' + args.context.systemVMs[0].id),
    		                                                                 dataType: 'json',
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.startsystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             jobId: jid,
    		                                                                             getUpdatedItem: function(json) {
    		                                                                                 return json.queryasyncjobresultresponse.jobresult.systemvm;
    		                                                                             },
    		                                                                             getActionFilter: function() {
    		                                                                                 return systemvmActionfilter;
    		                                                                             }
    		                                                                         }
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }
    		                                                     },

    		                                                     stop: {
    		                                                         label: 'label.action.stop.systemvm',
    		                                                         messages: {
    		                                                             confirm: function(args) {
    		                                                                 return 'message.action.stop.systemvm';
    		                                                             },
    		                                                             notification: function(args) {
    		                                                                 return 'label.action.stop.systemvm';
    		                                                             }
    		                                                         },
    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL('stopSystemVm&id=' + args.context.systemVMs[0].id),
    		                                                                 dataType: 'json',
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.stopsystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             jobId: jid,
    		                                                                             getUpdatedItem: function(json) {
    		                                                                                 return json.queryasyncjobresultresponse.jobresult.systemvm;
    		                                                                             },
    		                                                                             getActionFilter: function() {
    		                                                                                 return systemvmActionfilter;
    		                                                                             }
    		                                                                         }
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }
    		                                                     },

    		                                                     restart: {
    		                                                         label: 'label.action.reboot.systemvm',
    		                                                         messages: {
    		                                                             confirm: function(args) {
    		                                                                 return 'message.action.reboot.systemvm';
    		                                                             },
    		                                                             notification: function(args) {
    		                                                                 return 'label.action.reboot.systemvm';
    		                                                             }
    		                                                         },
    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL('rebootSystemVm&id=' + args.context.systemVMs[0].id),
    		                                                                 dataType: 'json',
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.rebootsystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             jobId: jid,
    		                                                                             getUpdatedItem: function(json) {
    		                                                                                 return json.queryasyncjobresultresponse.jobresult.systemvm;
    		                                                                             },
    		                                                                             getActionFilter: function() {
    		                                                                                 return systemvmActionfilter;
    		                                                                             }
    		                                                                         }
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }
    		                                                     },

    		                                                     remove: {
    		                                                         label: 'label.action.destroy.systemvm',
    		                                                         messages: {
    		                                                             confirm: function(args) {
    		                                                                 return 'message.action.destroy.systemvm';
    		                                                             },
    		                                                             notification: function(args) {
    		                                                                 return 'label.action.destroy.systemvm';
    		                                                             }
    		                                                         },
    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL('destroySystemVm&id=' + args.context.systemVMs[0].id),
    		                                                                 dataType: 'json',
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.destroysystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             getUpdatedItem: function() {
    		                                                                                 return {
    		                                                                                     state: 'Destroyed'
    		                                                                                 };
    		                                                                             },
    		                                                                             jobId: jid
    		                                                                         }
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }
    		                                                     },

    		                                                     migrate: {
    		                                                         label: 'label.action.migrate.systemvm',
    		                                                         messages: {
    		                                                             notification: function(args) {
    		                                                                 return 'label.action.migrate.systemvm';
    		                                                             }
    		                                                         },
    		                                                         createForm: {
    		                                                             title: 'label.action.migrate.systemvm',
    		                                                             desc: '',
    		                                                             fields: {
    		                                                                 hostId: {
    		                                                                     label: 'label.host',
    		                                                                     validation: {
    		                                                                         required: true
    		                                                                     },
    		                                                                     select: function(args) {
    		                                                                         $.ajax({
    		                                                                             url: createURL("findHostsForMigration&VirtualMachineId=" + args.context.systemVMs[0].id),
    		                                                                             dataType: "json",
    		                                                                             async: true,
    		                                                                             success: function(json) {
    		                                                                                 var hostObjs = json.findhostsformigrationresponse.host;
    		                                                                                 var items = [];
    		                                                                                 $(hostObjs).each(function() {
    		                                                                                     if (this.requiresStorageMotion == false) {
    		                                                                                         items.push({
    		                                                                                             id: this.id,
    		                                                                                             description: (this.name + " (" + (this.suitableformigration ? "Suitable" : "Not Suitable") + ")")
    		                                                                                         });
    		                                                                                     }
    		                                                                                 });
    		                                                                                 args.response.success({
    		                                                                                     data: items
    		                                                                                 });
    		                                                                             }
    		                                                                         });
    		                                                                     },
    		                                                                     error: function(XMLHttpResponse) {
    		                                                                         var errorMsg = parseXMLHttpResponse(XMLHttpResponse);
    		                                                                         args.response.error(errorMsg);
    		                                                                     }
    		                                                                 }
    		                                                             }
    		                                                         },
    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL("migrateSystemVm&hostid=" + args.data.hostId + "&virtualmachineid=" + args.context.systemVMs[0].id),
    		                                                                 dataType: "json",
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.migratesystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             jobId: jid,
    		                                                                             getUpdatedItem: function(json) {
    		                                                                                 //return json.queryasyncjobresultresponse.jobresult.systemvminstance;    //not all properties returned in systemvminstance
    		                                                                                 $.ajax({
    		                                                                                     url: createURL("listSystemVms&id=" + json.queryasyncjobresultresponse.jobresult.systemvminstance.id),
    		                                                                                     dataType: "json",
    		                                                                                     async: false,
    		                                                                                     success: function(json) {
    		                                                                                         var items = json.listsystemvmsresponse.systemvm;
    		                                                                                         if (items != null && items.length > 0) {
    		                                                                                             return items[0];
    		                                                                                         }
    		                                                                                     }
    		                                                                                 });
    		                                                                             },
    		                                                                             getActionFilter: function() {
    		                                                                                 return systemvmActionfilter;
    		                                                                             }
    		                                                                         }
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }
    		                                                     },

    		                                                     scaleUp: {
    		                                                         label: 'label.change.service.offering',
    		                                                         createForm: {
    		                                                             title: 'label.change.service.offering',
    		                                                             desc: function(args) {
    		                                                             	var description = '';                            	
    		                                                             	var vmObj = args.jsonObj;     
    		                                                             	//if (vmObj.state == 'Running' && vmObj.hypervisor == 'VMware') { //needs to wait for API fix that will return hypervisor property
    		                                                             	if (vmObj.state == 'Running') {	
    		                                                             		description = 'Please read the dynamic scaling section in the admin guide before scaling up.';
    		                                                             	}                             
    		                                                                 return description;                  	                
    		                                                             },
    		                                                             fields: {
    		                                                                 serviceOfferingId: {
    		                                                                     label: 'label.compute.offering',
    		                                                                     select: function(args) {
    		                                                                         var apiCmd = "listServiceOfferings&issystem=true";
    		                                                                         if (args.context.systemVMs[0].systemvmtype == "secondarystoragevm")
    		                                                                             apiCmd += "&systemvmtype=secondarystoragevm";
    		                                                                         else if (args.context.systemVMs[0].systemvmtype == "consoleproxy")
    		                                                                             apiCmd += "&systemvmtype=consoleproxy";
    		                                                                         $.ajax({
    		                                                                             url: createURL(apiCmd),
    		                                                                             dataType: "json",
    		                                                                             async: true,
    		                                                                             success: function(json) {
    		                                                                                 var serviceofferings = json.listserviceofferingsresponse.serviceoffering;
    		                                                                                 var items = [];
    		                                                                                 $(serviceofferings).each(function() {
    		                                                                                     if (this.id != args.context.systemVMs[0].serviceofferingid) {
    		                                                                                         items.push({
    		                                                                                             id: this.id,
    		                                                                                             description: this.name
    		                                                                                         });
    		                                                                                     }
    		                                                                                 });
    		                                                                                 args.response.success({
    		                                                                                     data: items
    		                                                                                 });
    		                                                                             }
    		                                                                         });
    		                                                                     }
    		                                                                 }
    		                                                             }
    		                                                         },

    		                                                         action: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL("scaleSystemVm&id=" + args.context.systemVMs[0].id + "&serviceofferingid=" + args.data.serviceOfferingId),
    		                                                                 dataType: "json",
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     var jid = json.changeserviceforsystemvmresponse.jobid;
    		                                                                     args.response.success({
    		                                                                         _custom: {
    		                                                                             jobId: jid,
    		                                                                             getUpdatedItem: function(json) {
    		                                                                                 return json.queryasyncjobresultresponse.jobresult.systemvm;
    		                                                                             },
    		                                                                             getActionFilter: function() {
    		                                                                                 return systemvmActionfilter;
    		                                                                             }

    		                                                                         }
    		                                                                     });

    		                                                                 },
    		                                                                 error: function(json) {
    		                                                                     args.response.error(parseXMLHttpResponse(json));
    		                                                                 }

    		                                                             });
    		                                                         },
    		                                                         messages: {
    		                                                             confirm: function(args) {
    		                                                                 return 'Do you really want to scale up the system VM ?';
    		                                                             },
    		                                                             notification: function(args) {

    		                                                                 return 'System VM Scaled Up';
    		                                                             }
    		                                                         },
    		                                                         notification: {
    		                                                             poll: pollAsyncJobResult
    		                                                         }

    		                                                     },


    		                                                     viewConsole: {
    		                                                         label: 'label.view.console',
    		                                                         action: {
    		                                                             externalLink: {
    		                                                                 url: function(args) {
    		                                                                     return clientConsoleUrl + '?cmd=access&vm=' + args.context.systemVMs[0].id;
    		                                                                 },
    		                                                                 title: function(args) {
    		                                                                     return args.context.systemVMs[0].id.substr(0, 8); //title in window.open() can't have space nor longer than 8 characters. Otherwise, IE browser will have error.
    		                                                                 },
    		                                                                 width: 820,
    		                                                                 height: 640
    		                                                             }
    		                                                         }
    		                                                     }
    		                                                 
    		                                                 },
    		                                                 tabs: {
    		                                                     details: {
    		                                                         title: 'label.details',
    		                                                         fields: [{
    		                                                             name: {
    		                                                                 label: 'label.name'
    		                                                             }
    		                                                         }, {
    		                                                             id: {
    		                                                                 label: 'label.id'
    		                                                             },
    		                                                             state: {
    		                                                                 label: 'label.state'
    		                                                             },
    		                                                             systemvmtype: {
    		                                                                 label: 'label.type',
    		                                                                 converter: function(args) {
    		                                                                     if (args == "consoleproxy")
    		                                                                         return "Console Proxy VM";
    		                                                                     else if (args == "secondarystoragevm")
    		                                                                         return "Secondary Storage VM";
    		                                                                     else
    		                                                                         return args;
    		                                                                 }
    		                                                             },
    		                                                             zonename: {
    		                                                                 label: 'label.zone'
    		                                                             },
    		                                                             publicip: {
    		                                                                 label: 'label.public.ip'
    		                                                             },
    		                                                             privateip: {
    		                                                                 label: 'label.private.ip'
    		                                                             },
    		                                                             linklocalip: {
    		                                                                 label: 'label.linklocal.ip'
    		                                                             },
    		                                                             hostname: {
    		                                                                 label: 'label.host'
    		                                                             },
    		                                                             gateway: {
    		                                                                 label: 'label.gateway'
    		                                                             },
    		                                                             created: {
    		                                                                 label: 'label.created',
    		                                                                 converter: cloudStack.converters.toLocalDate
    		                                                             },
    		                                                             activeviewersessions: {
    		                                                                 label: 'label.active.sessions'
    		                                                             }
    		                                                         }],
    		                                                         dataProvider: function(args) {
    		                                                             $.ajax({
    		                                                                 url: createURL("listSystemVms&id=" + args.context.systemVMs[0].id),
    		                                                                 dataType: "json",
    		                                                                 async: true,
    		                                                                 success: function(json) {
    		                                                                     args.response.success({
    		                                                                         actionFilter: systemvmActionfilter,
    		                                                                         data: json.listsystemvmsresponse.systemvm[0]
    		                                                                     });
    		                                                                 }
    		                                                             });
    		                                                         }
    		                                                     }
    		                                                 }
    		                                             }
    		                                         }
    		                                     },

    		                                     // Granular settings for zone
    		                                     settings: {
    		                                         title: 'Settings',
    		                                         custom: cloudStack.uiCustom.granularSettings({
    		                                             dataProvider: function(args) {
    		                                                 $.ajax({
    		                                                     url: createURL('listConfigurations&zoneid=' + args.context.physicalResources[0].id),
    		                                                     data: {
    		                                                         page: args.page,
    		                                                         pageSize: pageSize,
    		                                                         listAll: true
    		                                                     },
    		                                                     success: function(json) {
    		                                                         args.response.success({
    		                                                             data: json.listconfigurationsresponse.configuration

    		                                                         });

    		                                                     },

    		                                                     error: function(json) {
    		                                                         args.response.error(parseXMLHttpResponse(json));

    		                                                     }
    		                                                 });

    		                                             },
    		                                             actions: {
    		                                                 edit: function(args) {
    		                                                     // call updateZoneLevelParamter
    		                                                     var data = {
    		                                                         name: args.data.jsonObj.name,
    		                                                         value: args.data.value
    		                                                     };

    		                                                     $.ajax({
    		                                                         url: createURL('updateConfiguration&zoneid=' + args.context.physicalResources[0].id),
    		                                                         data: data,
    		                                                         success: function(json) {
    		                                                             var item = json.updateconfigurationresponse.configuration;
    		                                                             args.response.success({
    		                                                                 data: item
    		                                                             });
    		                                                         },

    		                                                         error: function(json) {
    		                                                             args.response.error(parseXMLHttpResponse(json));
    		                                                         }

    		                                                     });
    		                                                 }
    		                                             }
    		                                         })
    		                                     }
    		                                 }
    		                             }
    		                         }
    		    				},
    		    				resources : {
    		    					title : '统计',
    		    					custom: cloudStack.uiCustom.systemChart('resourcesForPool')
    		    				},
    		    				hpux:{
    		    					title:'其他物理资源',
    		    					id:'hpux',
    		    					listView: {
    		    		                section: 'hpux',
    		    		                id:"hosttype",
    		    			    		fields:{
    		    			    			name : {
    		    			    				label : "名称"
    		    			    			},
    		    			    			desc:{
    		    			    				label : "类型"
    		    			    			}
    		    			    		},
    		    			    		filters : false,
    		    			    		dataProvider: function(args) {
    		    			    			/*
    		    			    			 $.ajax({
    		    			                     url: createURL("listresourcepool&cmsz=yes&response=json"),
    		    			                     async: true,
    		    			                     success: function(json) {
    		    			                    	 var resourcepoolObjs =[];
    		    			                    	 resourcepoolObjs[0]={name:'小型机',desc:'hp小型机'};
    		    			                    	 resourcepoolObjs[1]={name:'x86',desc:'x86服务器'};
    		    			                         args.response.success({
    		    			                             //actionFilter: zoneActionfilter,
    		    			                             data: resourcepoolObjs
    			    			                        // data:json.root
    		    			                         });
    		    			                     }
    		    			                 });*/
	    			                    	 args.response.success({
	    			                             data: [{type:"3",name:'小型机',desc:'hp小型机'},
	    			                                    {type:"1,2",name:'x86',desc:'x86服务器'}]
	    			                         });
    		    			    		},
    		    			    		detailView:{
    		    			    			isMaximized: true,
    		    			    			quickView : false,
    		    			    			tabs : {
    		    			    				details :{
    		    			    					title : "主机列表",
    		    			    					/* "THost [id=" + id + ", manufacturer=" + manufacturer + ", productname=" + productname + ", serialnumber="
				+ serialnumber + ", servername=" + servername + ", cpucount=" + cpucount + ", cpucores=" + cpucores
				+ ", cputype=" + cputype + ", memory=" + memory + ", status=" + status + ", saveStatus=" + saveStatus
				+ ", type=" + type + ", descript=" + descript + ", nic=" + nic + ", bayIndex=" + bayIndex + ", hostname="
				+ hostname + ", resourcepoolid=" + resourcepoolid + "]";*/
    		    			    					listView : {
    		    			    							id:"resource_host",
    		    			    				    		fields:{
    		    			    				    			servername : {
    		    			    				    				label : "名称"
    		    			    				    			},
    		    			    				    			serialnumber:{
    		    			    				    				label : "序列号"
    		    			    				    			},
    		    			    				    			manufacturer:{
    		    			    				    				label:"厂商"
    		    			    				    			},
    		    			    				    			productname:{
    		    			    				    				label:'产品名称'
    		    			    				    			},
    		    			    				    			hostname:{
    		    			    				    				label:'IP地址'
    		    			    				    			}
    		    			    				    		},
    		    			    				    		filters : false,
    		    			    				    		dataProvider: function(args) {
    		    			    				    			 $(".ui-tabs-nav li.first.last").remove();
    		    			    				    			 $.ajax({
    		    			    				                     url: createURL("listhost&cmsz=yes&response=json&type="+args.context.hosttype[0].type+"&resourcepoolid="+args.context.resourcepools[0].resourcePoolId),
    		    			    				                     async: true,
    		    			    				                     success: function(json) {
    		    			    				                    	 var hosts = json.hosts;
    		    			    				                    	 //resourcepoolObjs[0]={name:'IT-ESXXI01',ser_num:'CNGTx09UIO',vendor:'HP',pname:'Elite8440P',ip:'192.168.1.32'};
    		    			    				                    	 //resourcepoolObjs[1]={name:'IT-ESXDS03',ser_num:'CNGTxCVNP8',vendor:'IBM',pname:'Folia9470m',ip:'17.132.4.102'};
    		    			    				                         args.response.success({
    		    			    				                             //actionFilter: zoneActionfilter,
    		    			    				                             data: hosts
    		    			    				                         });
    		    			    				                     }
    		    			    				                 });
    		    			    				    		},
    		    			    				    		detailView:{
    		    			    				    			name:'主机详情',
    		    			    				    			 viewAll: [{
    		    			    			                            path: 'resourcepools.snapshots',
    		    			    			                            label: 'HBA'
    		    			    			                       }, {
    		    			    			                           path: 'hpux',
    		    			    			                           label: '实例',
    		    			    			                           preFilter: function(args) {
    		    			    			                        	   if($("#breadcrumbs ul li:eq(3)").attr("title")=='小型机'){
    		    			    			                        		   return true;
    		    			    			                        	   }
    		    			    			                               return false;
    		    			    			                           }}],
    		    			    				    			actions: {

    		    			    		                            // Remove single event
    		    			    		                           /* remove: {
    		    			    		                                label: '删除',
    		    			    		                                messages: {
    		    			    		                                    notification: function(args) {
    		    			    		                                        return '删除主机';
    		    			    		                                    },
    		    			    		                                    confirm: function() {
    		    			    		                                        return '确定要删除该主机吗?';
    		    			    		                                    }
    		    			    		                                },
    		    			    		                                action: function(args) {
    		    			    		                                	var data = {
    		    			    		                		                	cmsz: 'yes',
    		    			    		                		                    response :'json'
    		    			    		                                    	};
    		    			    		                                    $.ajax({
    		    			    		                                    	url: createURL("listresourcepool&cmsz=yes&response=json"),
    		    			    		                                        data:data,
    		    			    		                                        success: function(json) {
    		    			    		                                            args.response.success();
    		    			    		                                            $(window).trigger('cloudStack.fullRefresh');
    		    			    		                                        }

    		    			    		                                    });
    		    			    		                                }
    		    			    		                            },*/
    		    			    		                            scaleUp:{
    		    			    		                            	label:'修改资源池',
    		    			    		                                 createForm: {
    		    			    		                                     title: '更新所属资源池',
    		    			    		                                     desc: function(args) {
    		    			    		                                     	var description = '更新所属资源池';                            	
    		    			    		                                         return description;                  	                
    		    			    		                                     },
    		    			    		                                     fields: {
    		    			    		                                         resourcepoolid: {
    		    			    		                                             label: '资源池',
    		    			    		                                             select: function(args) {
    		    			    		                                                 $.ajax({
    		    			    		                                                	 url : createURL("listresourcepool"),
    		    			    		                                      				async : false,
    		    			    		                                      				data : {
    		    			    		                                      					cmsz : "yes"
    		    			    		                                      				},
    		    			    		                                                     success: function(json) {
    		    			    		                                                         var pools = json.root;
    		    			    		                                                         var items = [{id:"",description:""}];
    		    			    		                                                         $(pools).each(function() {
    		    			    		                                                             items.push({
    		    			    		                                                                 id: this.resourcePoolId,
    		    			    		                                                                 description: this.name
    		    			    		                                                             });
    		    			    		                                                         });
    		    			    		                                                         args.response.success({
    		    			    		                                                             data: items
    		    			    		                                                         });
    		    			    		                                                         $("select[name='resourcepoolid']").val(args.context.resource_host[0].resourcepoolid);
    		    			    		                                                     }
    		    			    		                                                 });
    		    			    		                                             }
    		    			    		                                         }
    		    			    		                                     }
    		    			    		                                 },
    		    			    		                                 messages: {
    		    			    		                                     confirm: function(args) {
    		    			    		                                         return '确定修改所属资源池 ?';
    		    			    		                                     },
    		    			    		                                     notification: function(args) {
    		    			    		                                         return '修改所属资源池';
    		    			    		                                     }
    		    			    		                                 },
    		    			    		                                 notification: {
    		    			    		                                     poll: null
    		    			    		                                 },
    		    			    		                            	 action: function(args) {
    		    			    		                            		 
    		    			    		                            		 var data = {
    		    			    		                 		                	cmsz: 'yes',
    		    			    		                 		                    response :'json',
    		    			    		                 		                    hostid :args.context.resource_host[0].id,
    		    			    		                 		                    resourcepoolid :args.data.resourcepoolid 

    		    			    		                            		 };
	    		    			    		                                $.ajax({
	    		    			    		                                     	url: createURL("updatehostrespool&cmsz=yes&response=json"),
	    		    			    		                                         data:data,
	    		    			    		                                         success: function(json) {
	    		    			    		                                             args.response.success();
	    		    			    		                                             $(window).trigger('cloudStack.fullRefresh');
	    		    			    		                                         }
	
	    		    			    		                                     });
    		    			    		                                
     		    			    		                                	}
    		    			    		                            }
    		    			    				    			},
    		    			    				    			tabs: {
    		    			    		                            details: {
    		    			    		                                title: '主机详情',
    		    			    		                                fields:[{
    		    			    		                                	servername : {
    		    		    			    				    				label : "名称"
    		    		    			    				    			},
    		    		    			    				    			serialnumber:{
    		    		    			    				    				label : "序列号"
    		    		    			    				    			},
    		    		    			    				    			manufacturer:{
    		    		    			    				    				label:"厂商"
    		    		    			    				    			},
    		    		    			    				    			productname:{
    		    		    			    				    				label:'产品名称'
    		    		    			    				    			},
    		    		    			    				    			hostname:{
    		    		    			    				    				label:'IP地址'
    		    		    			    				    			}
    		    			    		                            }],
    		    			    		                            dataProvider: function(args) {
    		    			    	                                	setTimeout(function(){
	    		    			    		                            	var item=args.context.resource_host[0];
		    			    	                                            args.response.success({
		    			    	                                            	actionFilter: vmActionfilter,
		    			    	                                                data: item
		    			    	                                            });
    		    			    	                                	},200);
    		    			    	                                }
    		    			    				    			}
    		    			    				    		}
    		    			    				    		}
    		    			    					}
    		    			    				}
    		    			    			}
    		    			    		}
    		    					}
    		    				}
    		    			}
    		    		}
    	    		},
    			},
    			/**
    			 * Snapshots
    			 */
    			snapshots: {
    			    type: 'select',
    			    title: 'HBA',
    			    listView: {
    			        id: 'snapshots',
    			        label: 'HBA',
    			        firstClick : false,
    			        filters : false,
    			        fields: {
    			            wwn: {
    			                label: 'WWN'
    			            },
    			            type: {
    			                label: '类型'
    			            }
    			        },


    			        dataProvider: function(args) {
    			        	if(args.context.resource_host!=undefined){
    			        		args.response.success({
    			        			data: args.context.resource_host[0].hbaList
    			        		});
    			        	}else if( args.context.hpHostManagement!=undefined){
    			        	  args.response.success({
                                  data: args.context.hpHostManagement[0].hbaList
                              });
    			        	}else if( args.context.x96!=undefined){
    			        	  args.response.success({
                                  data: args.context.x96[0].hbaList
                              });
    			        	}else{
    			        		 args.response.success({
                                     data: []
                                 });
    			        	}
    			        	/*data = {
        		                	cmsz: 'yes',
        		                    response :'json',
        		                    query:true
                            	};
                            $.ajax({
                            	url: createURL("listresourcepool&cmsz=yes&response=json"),
                                dataType: "json",
                                async: true,
                                data:data,
                                success: function(json) {
                                   // var item = json.propertys[0];
                                    var item=[];
                                    item[0]={wwn:'50:02:DC:9x:CV:12:10',type:'QLogicQMH2DCFforHPA'};
                                    args.response.success({
                                        data: item
                                    });
                                }
                            });*/
    			        }
    			    }
    			}
    			    		
    			    }
    		}
    		
    		
    		

    
    })(cloudStack);

//action filters (begin)
var zoneActionfilter = function(args) {
    var jsonObj = args.context.item;
    var allowedActions = ['enableSwift'];

    if (jsonObj.vmwaredcId == null)
        allowedActions.push('addVmwareDc');
    else
        allowedActions.push('removeVmwareDc');

    if (jsonObj.domainid != null)
        allowedActions.push("releaseDedicatedZone");
    else
        allowedActions.push("dedicateZone");

    allowedActions.push("edit");
    allowedActions.push("deleteResourcePoolRL");

    if (jsonObj.allocationstate == "Disabled")
        allowedActions.push("enable");
    else if (jsonObj.allocationstate == "Enabled")
        allowedActions.push("disable");

    allowedActions.push("remove");
    return allowedActions;
}

var systemvmActionfilter = function(args) {
    var jsonObj = args.context.item;
    var allowedActions = [];

    if (jsonObj.state == 'Running') {
        allowedActions.push("stop");
        allowedActions.push("restart");
        allowedActions.push("remove");
        
        //when systemVm is running, scaleUp is not supported for KVM and XenServer.            
        //however, listSystemVms API doesn't return hypervisor property....
        /*
        if (jsonObj.hypervisor != 'KVM' && jsonObj.hypervisor != 'XenServer') {
        	allowedActions.push("scaleUp");
        }  
        */
        allowedActions.push("scaleUp");
        
        allowedActions.push("viewConsole");
        if (isAdmin())
            allowedActions.push("migrate");
    } else if (jsonObj.state == 'Stopped') {
        allowedActions.push("start");
        allowedActions.push("scaleUp");  //when vm is stopped, scaleUp is supported for all hypervisors           
        allowedActions.push("remove");
    } else if (jsonObj.state == 'Error') {
        allowedActions.push("remove");
    }
    return allowedActions;
}
var vmActionfilter = function(args) {
   
    var allowedActions = [];
      allowedActions.push("remove");
      allowedActions.push("scaleUp");
    return allowedActions;
}