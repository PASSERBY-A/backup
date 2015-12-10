// Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) { 
	
	var g_worktype = undefined;
	//添加实例
	var deployVirtualMachine=5;
	var projectLimitapplication=2;
	//新加卷
	var newVolume=7;
	
	var status_provisonfail = 6;//处理失败
	var status_provisionsucceed=5;//处理成功
	var status_provisioning=4;//审批通过正在处理
	var status_rejected=3;//审批未通过
	var status_approved=2;//审批通过待处理
	var status_waitingforapproval=1;//待审批
	
	var g_iscustomized = undefined;
	
	
	//工单类型条件
	var filterWorkOrderType = {all:{label: "所有工单"}};
	$.ajax({ 
		url : createURL("listConfig"),
		dataType : "json", 
		async : false, 
		data : { 
			cmsz : "yes", //configKey :		 "workorder_status%" 
			configKey : "workorder_type%" 
			}, 
		success :function(json) { 
			workOrderType = json.configList;
			$.each(workOrderType,function(index, val){
				var obj = {};
				$.extend(obj,{label : val.description});
				filterWorkOrderType[val.key]=obj;
			});
		}
	});

    cloudStack.sections.workOrders = {
        title: '服务',
        id: 'workOrders',
        listView: {
            section: 'workOrders',
            filters:filterWorkOrderType, 
            preFilter: function(args) {
                var hiddenFields = [];
                /*if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            
            afterFilter: function(args) {
            	$("div.button.action.add").hide();
            },
            
            fields: {
        		workOrderType : {
        			label : '工单类型',
        			converter : function(arg) {
        				return showWorkOrderType(arg);
        			}
        		},
        		applierName : {
        			label : '申请人'
        		},
        		createdOn : {
        			label : '申请时间',
        			converter : function(date) {
        				return cloudStack.converters.toCloudDate(date, "1");
        			}
        		},
        		status : {
        			label : '状态',
        			converter : function(status) {
        				return showWorkOrderStatus(status);
        			}
        		}	
            },
            advSearchFields: {
                 status: {
                	 label : "工单状态",
                	 select: function(args) {
                		 var obj = [{id:"",description: '所有类型'}];
                		 if(workOrderStatus){
                			 $.each(workOrderStatus,function(index, val){
	                				obj.push({
	                					 id:val.value,
	                					 description: val.description
	                				 });
                				});
                			 args.response.success({
	                             data: obj
                			 });
                		 }
                	 }
                 },
                 startDate:{
                	 label : "申请时间从",
                	 isDatepicker: true
                 },
                 endDate : {
                	 label : "至",
                	 isDatepicker: true
                 }
            },
            
            actions: {
                add: {
                    label: '服务申请',

                    preFilter: function(args) {
                    	return true;
                    },

                    messages: {
                        notification: function(args) {
                            return '服务申请';
                        }
                    },

                    createForm: {
                        title: '服务申请',
                        fields: {
                        	operatingSystem: {
                                label: '操作系统',
                                validation: {
                                    required: true,
                                    maxlength: 50
                                }
                            },
                            resourcePoolId: {
                                label: '一级资源池',
                                validation: {
                                    required: true
                                },
                                select: function(args) {
                                    var resourcePoolObj = {};
                                    
                                    $.ajax({
            							url : createURL("getExtval"),
            							async : false,
            							data : {
            								cmsz : "yes",
            								workordertype : function() {
                                        		return getWorkOrderVal($("#filterBy").val());
                                        	},
            								attributename : 'resourcePoolId'
            							},
            							success : function(json) {
            								var items = [];
            								resourcePoolObj = json.keyValues;
            								$(resourcePoolObj).each(function() {
            									if (this.id != 'all') {
            										items.push({
            											id: this.id,
            											description: this.description
            										});
            									}
            								});
            								args.response.success({
            									data: items
            								});
            							}
            						});
                                }
                            },
                            cpu: {
                                label: 'CPU核数',
                                validation: {
                                    required: true,
                                    number: true,
                                    min: 1
                                }
                            },
                            memory: {
                                label: '内存(MB)',
                                validation: {
                                    required: true,
                                    maxlength: 20,
                                    number: true,
                                    min: 1
                                }
                            },
                            unit: {
                                label: '台数',
                                validation: {
                                    required: true,
                                    number: true,
                                    min: 1,
                                    max: 100
                                }
                            },
                            description: {
                            	isTextarea: true,
                                label: '说明',
                                docID: 'helpApplicationDescription',
                                validation: {
                                	required: false,
                                    maxlength: 100
                                }
                            },
                            applyReason: {
                            	isTextarea: true,
                                label: '申请理由',
                                validation: {
                                    required: true,
                                    maxlength: 100
                                }
                            }
                        }
                    },

                    action: function(args) {
                        $.ajax({
                            url: createURL('saveOrder', {
                                ignoreProject: true
                            }),
                            data: {
                            	workOrderType: function() {
                            		return getWorkOrderVal($("#filterBy").val());
                            	},
                            	cmsz: 'yes',
                            	status: 1,
                            	accountId: args.context.users[0].account,
                                domainId: args.context.users[0].domainid,
                                operatingSystem: args.data['operatingSystem'],
                                resourcePoolId: args.data['resourcePoolId'],
                                cpu: args.data['cpu'],
                                memory: args.data['memory'],
                                unit: args.data['unit'],
                                description: args.data['description'],
                                applyReason: args.data['applyReason']
                            },
                            dataType: 'json',
                            async: true,
                            type:'post',
                            success: function(data) {
                            	cloudStack.dialog.window({message:"提交成功"}, function() {
                              		$("#basic_search").click();
                            	});
                            },
                            error: function(XMLHttpResponse) {
                                var errorMsg = parseXMLHttpResponse(XMLHttpResponse);
                                args.response.error(errorMsg);
                            }
                        });
                    },

                    notification: {
                        poll: function(args) {
                            args.complete();
                        }
                    }
                }
            },
            
            dataProvider: function(args) {
				var data = {
        					cmsz : 'yes',
        					page : args.page,
        					pageSize  : pageSize,
        					listAll : true
        				};
                listViewDataProvider(args, data);
                data.pagesize = 22;
                
                var getWorkOrderVal=function(key){

            		if (!workOrderType) {
            			$.ajax({ 
            				url : createURL("listConfig"),
            				dataType : "json", 
            				async : false, 
            				data : { 
            					cmsz : "yes", //configKey :		 "workorder_status%" 
            					configKey : "workorder_type%" 
            					}, 
            				success :function(json) { 
            					workOrderType = json.configList;
            				}
            			});
            		}
            		var dbconfig = $.grep(workOrderType, function(value, index) { 
            			return value.key == key; 
            		});
            		if (dbconfig.length > 0) { 
            			return	 ""+dbconfig[0].value; 
            		} else {
            			return null; 
            		}
            	
            	};
                
                if (args.filterBy != null) { //filter dropdown
                	
                	if (args.filterBy.kind != null) {
                    	var value = getWorkOrderVal(args.filterBy.kind);
                    	
                    	var add = $("div.button.action.add");
                    	if (add) {
                    		if(args.filterBy.kind == "workorder_type.X86PhysicalResourcesApplication" 
                    			|| args.filterBy.kind == "workorder_type.HPMinicomputerResourcesApplication") {
                        		add.show();
                        	} else {
                        		add.hide();
                        	}
                    	}
                    	
                    	if(value!=null){
                    		 $.extend(data, {
                    			 WORKORDER_TYPE: value
                             });
                    	}
                    }
                }
                $.ajax({
        			url : createURL('listWorkOrder'),
        			data : data,
        			success : function(json) {
        				var items = json.workOrders;
        				args.response.success({
        					data : items
        				});
        			}
        		});
            },

            detailView: {
            	quickView :false,
                //name: 'Instance details',
                isMaximized: true,
                /*viewAll: [{
                    path: 'storage.volumes',
                    label: 'label.volumes'
                }, {
                    path: 'vmsnapshots',
                    label: 'label.snapshots'
                }, {
                    path: 'affinityGroups',
                    label: 'label.affinity.groups'
                }],*/
                tabFilter: function(args) {
                	var hiddenTabs = [];
                	g_worktype = args.context.workOrders[0].workOrderType;
                	
                  if(args.context.workOrders[0].status=="1"){//待审批
                	  hiddenTabs.push("viewApprove");
                	  //return ["details","approve"];
                  }else{
                	  hiddenTabs.push("approve");
                	 // return ["details","viewApprove"];
                  }
                  
                  
                  if(isDomainAdmin() || isUser()){
                	  hiddenTabs.push("approve");
              	  }
                  return hiddenTabs;
                },
				/*actions: {
					changeItem: {
                    	label : '修改',
                        action: {}
					}
				},*/
                tabs: {
                    // Details tab
                    details: {
                        title: '工单详情',
                        /*preFilter: function(args) {
                        	return [];
                        },*/
                        preFilter: function(args) {
                            var hiddenFields = [];
                            if(args.context.workOrders[0].workOrderType==newVolume){
                            	var iscustomized =getItemAttributeValue(args.context.workOrders[0],"iscustomized");
                            	if(iscustomized==0){//非自定义磁盘大小的方案
                            		hiddenFields.push("diskSize");
                            	}
                            }else if(args.context.workOrders[0].workOrderType==deployVirtualMachine){
                            	var dvmdiskSize =getItemAttributeValue(args.context.workOrders[0],"diskSize");
                            	if(dvmdiskSize==undefined || dvmdiskSize=="" || dvmdiskSize==0){//非自定义磁盘大小的方案
                            		hiddenFields.push("diskSize");
                            	}
                            }
                            return hiddenFields;
                        },
                       /* actions:{
                        	edit: {
                                label: '编辑',
                                action:null
                        	}
                        },*/
                        fields: [],


                        dataProvider: function(args) {
                        	
                        	var renderObj = {};
                           if(args.context.workOrders[0].status!=1){
                        	   var data = {
               						id : args.context.workOrders[0].id,
               						cmsz : "yes",
               						step : 2
               					};
                        	   $.ajax({
                                   url: createURL("listWorkOrder"),
                                   data: data,
                                   dataType: "json",
                                   async: false,
                                   success: function(json) {
                                	   renderObj = json.workOrders[0];
                       				}
                                   });

                           }else{
                        	   renderObj = args.context.workOrders[0];
                           }
                          
        					// 初始化字段
        					var fields = viewFields(args.context.workOrders[0]);
        					var obj = {};
        					$.extend(obj, renderObj);
        					
        					for(var i = 0;i<args.context.workOrders[0].workItems.length;++i){
        						var workItem = args.context.workOrders[0].workItems[i];

        						//控制 以Default View视图添加实例时 ProjectId字段不显示
        						if(workItem.attributeName=="projectid" && workItem.attributeValue=="" && args.context.workOrders[0].workOrderType==deployVirtualMachine){
        							//fields[workItem.attributeName] = undefined;
        							delete fields[workItem.attributeName];
        							continue;
        						}
        						/*//控制新加卷时，非自定义磁盘大小时不显示磁盘大小
        						if(workItem.attributeName=="diskSize" && fields["iscustomized"]==0  && args.context.workOrders[0].workOrderType==newVolume){
        							//delete fields[workItem.attributeName];
        							fields[workItem.attributeName].isHidden=true;
        							continue;
        						}
        						*/
        						if(fields[workItem.attributeName] && fields[workItem.attributeName].externalValue==1 && (fields[workItem.attributeName].valType=="text" || (fields[workItem.attributeName].valType=="enum" && !fields[workItem.attributeName].editable))){
        							$.ajax({
        								url : createURL("getExtval"),
        								async : false,
        								data : {
        									cmsz : "yes",
        									workordertype : args.context.workOrders[0].workOrderType,
        									attributename : workItem.attributeName,
        									id : workItem.attributeValue
        								},
        								success : function(json) {
        									
        									if(json && json.keyValues.length>0){
        										for(var i=0;i<json.keyValues.length;++i){
        											if(json.keyValues[i].id==workItem.attributeValue){
        												obj[workItem.attributeName]=json.keyValues[i].description;
        												break;
        											}
        										}
        									}else{
        										obj[workItem.attributeName]=workItem.attributeValue;
        									}
        								}
        							});
        						}else{
        							obj[workItem.attributeName]=workItem.attributeValue;
        						}
        					}
        					
        					args.response.success({
        						data : obj,
        						fields : fields
        					});
                        }
                    },
                  

                    /**
                     * 审批 tab
                     */
                    approve: {
                        title: '审批',
                        multiple: true,
						noHeader : true,
                        preFilter: function(args) {
                                 var hiddenFields = [];
                                 g_iscustomized = undefined;
                                 if(args.context.workOrders[0].workOrderType==newVolume){
                                	 g_iscustomized = getItemAttributeValue(args.context.workOrders[0],"iscustomized");
                                 	if(g_iscustomized==0){//非自定义磁盘大小的方案
                                 		hiddenFields.push("diskSize");
                                 	}
                                 }else if(args.context.workOrders[0].workOrderType==deployVirtualMachine){
                                 	var dvmdiskSize =getItemAttributeValue(args.context.workOrders[0],"diskSize");
                                	if(dvmdiskSize==undefined || dvmdiskSize=="" || dvmdiskSize==0){//非自定义磁盘大小的方案
                                		hiddenFields.push("diskSize");
                                	}
                                }
                                 return hiddenFields;
                        },
                        actions: {
                            add:{
                            	
                            	label : '审批',
                                messages: {
                                   /* confirm: function(args) {
                                        return 'Please confirm that you would like to add a new VM NIC for this network.';
                                    },*/
                                    notification: function(args) {
                                        //return 'Add network to VM';
                                    	return '审批工单';
                                    }
                                },
                            	action:function(args){
                            		 if (!$("#validateForm").valid()) {
                                		 //$(".loading-overlay").hide();
                            			 //$(".loading-overlay").remove();
                                         return;
                                     }
                            		 
                            		 $("#details-tab-approve").append($('.loading-overlay'));
                            		 
                                	 var data=cloudStack.serializeForm($("[formdata=true]"));
                                	 var id = args.context.workOrders[0].id;
                                	 var workOrder = args.context.workOrders[0];
                                	 workOrder.approveDesc =data["approveDesc"];
                                	 workOrder.approveResult= data['approveResult'];
                                	 if(g_iscustomized!=undefined){
                                		 $.extend(data,{iscustomized:g_iscustomized});
                                	 }
                            		 $.extend(data,{workOrderId:id,cmsz:"yes"});
                            		 
                            		 $.ajax({
                                         url: createURL("doapprove"),
                                         data: data,
                                         dataType: "json",
                                         async: false,
                                         success: function(json) {
                                        	 cloudStack.dialog.window({message:"审批成功"},function(){
                                        		/*if(args.context.workOrders[0].status!="1"){//待审批
                                        			 = 
                                        		}*/
                                         		args.response.success();
                                         		$(".loading-overlay").remove();
                                         		$("#breadcrumbs ul li:eq(0)").click();
//                                         		$(window).trigger('cloudStack.fullRefresh');
                                         		$("#basic_search").click();
                                         	});
                                         },
                                         error: function(json) {
                                        	 $(".loading-overlay").remove();
                                          	cloudStack.dialog.error(parseXMLHttpResponse(json));
                                         }
                                     });
                                	
                            	},
                            	notification: {
                            		 poll: function(args) {
                                         args.complete({
                                         });
                                     }
                                    
                                }
                            }
                        },
                        fields: [],

                        dataProvider: function(args) {
        					
        					var renderObj = args.context.workOrders[0];

        					// 初始化字段
        					var fields = initFields(args.context.workOrders[0]);
        					
        					var obj = {};
        					$.extend(obj, renderObj);
        					for(var i = 0;i<args.context.workOrders[0].workItems.length;++i){
        						var workItem = args.context.workOrders[0].workItems[i];
        						
        						//控制 以Default View视图添加实例时 ProjectId字段不显示
        						if(workItem.attributeName=="projectid" && workItem.attributeValue=="" && args.context.workOrders[0].workOrderType==deployVirtualMachine){
        							//fields[workItem.attributeName] = undefined;
        							delete fields[workItem.attributeName];
        							continue;
        						}
        						
        						/*//控制新加卷时，非自定义磁盘大小时不显示磁盘大小
        						if(workItem.attributeName=="diskSize" && fields["iscustomized"]==0  && args.context.workOrders[0].workOrderType==newVolume){
        							//delete fields[workItem.attributeName];
        							fields[workItem.attributeName].isHidden=true;
        							continue;
        						}*/
        						
        						if(fields[workItem.attributeName] && fields[workItem.attributeName].externalValue==1 && (fields[workItem.attributeName].valType=="text" || (fields[workItem.attributeName].valType=="enum" && !fields[workItem.attributeName].editable))){
        							$.ajax({
        								url : createURL("getExtval"),
        								async : false,
        								data : {
        									cmsz : "yes",
        									workordertype : args.context.workOrders[0].workOrderType,
        									attributename : workItem.attributeName,
        									id : workItem.attributeValue,
        								},
        								success : function(json) {
        									
        									if(json  && json.keyValues.length>0){
        										for(var i=0;i<json.keyValues.length;++i){
        											if(json.keyValues[i].id==workItem.attributeValue){
        												obj[workItem.attributeName]=json.keyValues[i].description;
        												obj[workItem.attributeName+"_id"]= workItem.attributeValue;
        												break;
        											}
        										}
        									}else{
        										obj[workItem.attributeName]=workItem.attributeValue;
        										obj[workItem.attributeName+"_id"]= workItem.attributeValue;
        									}
        									//obj[workItem.attributeName]=json.keyValues[0].description;
        								}
        							});
        						}else{
        							obj[workItem.attributeName]=workItem.attributeValue;
        						}
        					}
        					
        					args.response.success({
        						data : obj,
        						fields : fields,
        						name : "审批"
        					});
        					
        					
        					if(g_worktype==newVolume){//添加卷
        						$("select[name=diskOfferingId]",$("#details-tab-approve")).bind("change",function(){
        							var iscustomized= $(this).find("option[value="+$(this).val()+"]").attr("iscustomized");
        							g_iscustomized = iscustomized;
        							if(iscustomized==1){
        								$("input[name=diskSize]",$("#details-tab-approve")).parents("tr").show();
        							}else{
        								$("input[name=diskSize]",$("#details-tab-approve")).parents("tr").hide();
        							}
        						});
        						$("select[name=diskOfferingId]",$("#details-tab-approve")).change();
        					}else if(g_worktype==deployVirtualMachine){//添加实例
        						//显隐磁盘大小输入框
        						$("select[name=diskofferingid]",$("#details-tab-approve")).bind("change",function(){
        							$("input[name=diskSize]",$("#details-tab-approve")).removeClass("required number");
        							var iscustomized= $(this).find("option[value="+$(this).val()+"]").attr("iscustomized");
        							//g_iscustomized = iscustomized;
        							if(iscustomized==1){
        								$("input[name=diskSize]",$("#details-tab-approve")).parents("tr").show();
        								$("input[name=diskSize]",$("#details-tab-approve")).addClass("required number");
        							}else{
        								$("input[name=diskSize]",$("#details-tab-approve")).parents("tr").hide();
        								$("input[name=diskSize]",$("#details-tab-approve")).removeClass("required number");
        							}
        						});
        						$("select[name=diskofferingid]",$("#details-tab-approve")).change();
        						
        						//更改zone时，模版会级联变化  网络会级联变化
        						$("select[name=zoneid]",$("#details-tab-approve")).bind("change",function(){
        							//模版
        							 $("select[name=templateid]",$("#details-tab-approve")).empty();
        							$.ajax({
        								url : createURL("getExtval"),
        								async : false,
        								data : {
        									cmsz : "yes",
        									workordertype : g_worktype,
        									attributename : "templateid",
        									zoneid : this.value,
        								},
        								success : function(json) {
        									if(json  && json.keyValues.length>0){
        										
        										for(var i=0;i<json.keyValues.length;++i){
        											var $option = $("<option value='"+json.keyValues[i].id+"'>"+json.keyValues[i].description+"</option>");
        											$("select[name=templateid]",$("#details-tab-approve")).append($option);
        										}
        										
        										
        									}
        								}
        							});
        							//网络
        							$("select[name=networkids]",$("#details-tab-approve")).empty();
        							$.ajax({
        								url : createURL("getExtval"),
        								async : false,
        								data : {
        									cmsz : "yes",
        									workordertype : g_worktype,
        									attributename : "networkids",
        									domainid:args.context.workOrders[0].domainId,
        									account:args.context.workOrders[0].account,
        									zoneid : this.value
        								},
        								success : function(json) {
        									console.info(json);
        									if(json  && json.keyValues.length>0){
        										for(var i=0;i<json.keyValues.length;++i){
        											var $option = $("<option value='"+json.keyValues[i].id+"'>"+json.keyValues[i].description+"</option>");
        											$("select[name=networkids]",$("#details-tab-approve")).append($option);
        										}
        										
        										
        									}
        								}
        							});
        							
        						});
        						$("select[name=zoneid]",$("#details-tab-approve")).change();
        						
        					}
        					
                        }
                    },
                    viewApprove:{

                        title: '审批信息',
                        preFilter: function(args) {
                        	 var hiddenFields = [];
                             if(args.context.workOrders[0].workOrderType==newVolume){
                             	var iscustomized =getItemAttributeValue(args.context.workOrders[0],"iscustomized");
                             	if(iscustomized==0){//非自定义磁盘大小的方案
                             		hiddenFields.push("diskSize");
                             	}
                             }else if(args.context.workOrders[0].workOrderType==deployVirtualMachine){
                            	var dvmdiskSize =getItemAttributeValue(args.context.workOrders[0],"diskSize");
//                            	alert(dvmdiskSize);
                            	if(dvmdiskSize==undefined || dvmdiskSize=="" || dvmdiskSize==0){//非自定义磁盘大小的方案
                            		hiddenFields.push("diskSize");
                            	}
                            }
                             return hiddenFields;
                        },                        
                       /* actions:{
                        	edit: {
                                label: '编辑',
                                action:null
                        	}
                        },*/
                        
                        fields: [],

                      /*  tags: cloudStack.api.tags({
                            resourceType: 'UserVm',
                            contextId: 'instances'
                        }),*/

                        dataProvider: function(args) {
                        	
                        	

                        	var renderObj = {};
                           if(args.context.workOrders[0].status!=1){
                        	   var data = {
               						id : args.context.workOrders[0].id,
               						cmsz : "yes",
               						step : 2
               					};
                        	   $.ajax({
                                   url: createURL("listWorkOrder"),
                                   data: data,
                                   dataType: "json",
                                   async: false,
                                   success: function(json) {
                                	   renderObj = json.workOrders[0];
                       				}
                                   });

                           }else{
                        	   renderObj = args.context.workOrders[0];
                           }

        					// 初始化字段
        					var fields = viewFields(args.context.workOrders[0]);
        					$.extend(fields,{
    							approveResult:{
    								label : "审批结果",
    								select :  function(args){
    									args.response.success({data: [{
    										id: '1',
    										description: '审批通过'
    									}, {
    										id: '0',
    										description: '审批不通过'
    									}]});
    								}
    							},
    							approveDesc : {
    								label : "审批描述"
    							}
    					});
        					var obj = {};
        					$.extend(obj, renderObj);
        					for(var i = 0;i<args.context.workOrders[0].workItems.length;++i){
        						var workItem = args.context.workOrders[0].workItems[i];
        						
        						//控制 以Default View视图添加实例时 ProjectId字段不显示
        						if(workItem.attributeName=="projectid" && workItem.attributeValue=="" && args.context.workOrders[0].workOrderType==deployVirtualMachine){
        							//fields[workItem.attributeName] = undefined;
        							delete fields[workItem.attributeName];
        							continue;
        						}
        						
        						if(fields[workItem.attributeName] && fields[workItem.attributeName].externalValue==1){// && fields[workItem.attributeName].valType=="text"){
        							$.ajax({
        								url : createURL("getExtval"),
        								async : false,
        								data : {
        									cmsz : "yes",
        									workordertype : args.context.workOrders[0].workOrderType,
        									attributename : workItem.attributeName,
        									id : workItem.attributeValue
        								},
        								success : function(json) {
        									if(json && json.keyValues[0] && json.keyValues.length>0){
        										obj[workItem.attributeName]=json.keyValues[0].description;
        									}else{
        										obj[workItem.attributeName]=workItem.attributeValue;
        									}
        								}
        							});
        						}else{
        							obj[workItem.attributeName]=workItem.attributeValue;
        						}
        					}    						
        					args.response.success({
        						data : obj,
        						fields : fields
        					});
        					
                        }
                    
                    }
                }
            }
        }
    };
	
	
	var workOrderStatus = undefined;
	var showWorkOrderStatus = function(args) {
		
		 if(!workOrderStatus){ 
			 $.ajax({ 
					 url : createURL("listConfig"), 
					 dataType :"json", 
					 async : false, 
					 data : { cmsz : "yes", 
						 configKey : "workorder_status%" }, 
					success : function(json) { 
						workOrderStatus = json.configList;
					} 
			 });
		 } 
		 var dbconfig =	 $.grep(workOrderStatus,function(value,index){ return value.value == args; }); 
		 if(dbconfig.length>0){ 
			 return dbconfig[0].description;
		 }else{ 
			 return args;
		 }
		/*1	 "待审批";
		  2  "审批通过待处理";
		  3	 "审批未通过";
		  4  "审批通过正在处理";
		  5   "处理成功";
		  6   "处理失败";
		}*/

	};

	var workOrderType = undefined;
	var showWorkOrderType = function(args) {

		if (!workOrderType) {
			$.ajax({ 
				url : createURL("listConfig"),
				dataType : "json", 
				async : false, 
				data : { 
					cmsz : "yes", //configKey :		 "workorder_status%" 
					configKey : "workorder_type%" 
					}, 
				success :function(json) { 
					workOrderType = json.configList;
				}
			});
		}
		var dbconfig = $.grep(workOrderType, function(value, index) { 
							return value.value == args; 
						});
		if (dbconfig.length > 0) { 
				return	 dbconfig[0].description; 
		} else {
			return args; 
		}
	};
	
	

	var commonField = {
		id : {
			label : '工单编号'
		},
		createdOn : {
			label : '创建时间',
			converter : function(date) {
				return cloudStack.converters.toCloudDate(date, "1");
			}
		},
		workOrderType : {
			label : '工单类型',
			converter : function(arg) {
				return showWorkOrderType(arg);
			}
		},
		createdBy : {
			label : '申请人'
		}
	};
	//初始化查看字段
	var viewFields = function(workOrder){
		var workOrderType = workOrder.workOrderType;
		var fields = {};
		$.ajax({
			url : createURL("getAttribute"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes",
				workordertype : workOrderType
			},
			success : function(json) {
				$.extend(fields, commonField);
				for ( var i = 0; i < json.fields.length; ++i) {
					var obj = {};
					//不可见不显示
					if(json.fields[i].visible==0){
						if(json.fields[i].required==0){//不是必要的
							continue;
						}else{//必要的
							obj.isHidden=true;
						}
					}
					obj.label = json.fields[i].displayName;
					obj.externalValue = json.fields[i].externalValue;
					obj.valType = json.fields[i].type;
					obj.alignTo = json.fields[i].alignto;
					//obj.isEditable = true;
					// externalValue=1时表示要从外部获取
					
					//获取枚举值
					if (json.fields[i].externalValue == 1) {
						var renderObj = {};
						$.ajax({
							url : createURL("getExtval"),
							async : false,
							data : {
								cmsz : "yes",
								workordertype : workOrderType,
								attributename : json.fields[i].attributeName
							},
							success : function(json) {
								renderObj = json.keyValues;
							}
						});
						obj.select = function(args) {
							args.response.success({
								data : renderObj
							});
						};
					}
				//	if(json.fields[i])
					fields[json.fields[i].attributeName] = obj;
				}
			}
			
		});
		
		//添加开通失败信息字段
		if(status_provisonfail==workOrder.status){
			$.extend(fields,{
				errorCode:{
					label : "错误码"
				},
				errorText:{
					label : "错误信息"
				}
			});
		}
		return fields;
	
	};
	var initFields = function(workOrder) {
		var workOrderType = workOrder.workOrderType;
		var fields = {};
		var alignToVal = {};
		$.ajax({
			url : createURL("getAttribute"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes",
				workordertype : workOrderType
			},
			success : function(json) {
				$.extend(fields, commonField);
				for(var i= 0;i<json.fields.length;++i){
					
					if(json.fields[i].alignto!=""){
						alignToVal[json.fields[i].alignto] ="true";//临时值
					}
				}
				for(var i=0;i<workOrder.workItems.length;++i){
					if(alignToVal[workOrder.workItems[i].attributeName]=="true"){
						alignToVal[workOrder.workItems[i].attributeName] = workOrder.workItems[i].attributeValue;
					}
				}
				for ( var i = 0; i < json.fields.length; ++i) {
					
					var obj = {};
					
					//不可见不显示
					if(json.fields[i].visible==0){
						if(json.fields[i].required==0){//不是必要的
							continue;
						}else{//必要的
							obj.isHidden=true;
						}
						obj.isHidden=true;
					}
					
					obj.label = json.fields[i].displayName;
					
					//可编辑
					obj.editable = json.fields[i].editable==1;
					obj.valType = json.fields[i].type;
					obj.externalValue = json.fields[i].externalValue;
					obj.alignTo = json.fields[i].alignto;
					obj.required = json.fields[i].required;
					var querystr = "";
					if(obj.alignTo!=""){
						var refs = obj.alignTo.split(",");
						for(var j=0;j<refs.length;j++){
						 querystr  = querystr +"&"+ refs[j]+"="+alignToVal[refs[j]];
						}
						//alert(querystr);
						//console.info(querystr);
					}
					// externalValue=1时表示要从外部获取
					//获取枚举值
					if (json.fields[i].externalValue == 1) {
						var dataParam = {
								cmsz : "yes",
								workordertype : workOrderType,
								attributename : json.fields[i].attributeName,
							};
						//添加实例5特殊处理
						if(workOrderType==deployVirtualMachine){
							if(json.fields[i].attributeName=="networkids"){//网络
								$.extend(dataParam,{
										"domainid":workOrder.domainId,
										"account":workOrder.account
									});
							}
						}
						
						//console.info(querystr +":"+    json.fields[i].attributeName  );
						var renderObj = {};
						$.ajax({
							url : createURL("getExtval"+querystr),
							async : false,
							data :dataParam,
							success : function(json) {
								renderObj = json.keyValues;
							}
						});
						if(json.fields[i].type=="enum"){
							obj.select = function(args) {
								args.response.success({
									data : renderObj
								});
							};
							obj.data = renderObj;
						}
						if(workOrderType==deployVirtualMachine){
							if(json.fields[i].attributeName=="networkids"){//网络
								//TODO
							}
						}
					}
					fields[json.fields[i].attributeName] = obj;
				}
			}
			
		});
		$.extend(fields,{
			approveResult:{
				label : "审批结果",
				editable : true,
				isEditable : true,
				data : [{
                    id: '1',
                    description: '审批通过'
                }, {
                    id: '0',
                    description: '审批不通过'
                }],
				select :  function(args){
					args.response.success({data: [{
                        id: '1',
                        description: '审批通过'
                    }, {
                        id: '0',
                        description: '审批不通过'
                    }]});
				}
			},
			approveDesc : {
				label : "审批描述",
				inputType:"textarea",
				editable : true,
				required : 1
			}});
		
		return fields;
	};
	
	var getItemAttributeValue = function(workOrder,attributeName){
		if(workOrder==undefined){
			return undefined;
		}else{
        	var items = workOrder.workItems;
        	for(var i=0;i<items.length;++i){
        		if(items[i].attributeName==attributeName){
        			return items[i].attributeValue;
        		}
        	}
		}
			
	};

	var getWorkOrderVal = function(key){

		if (!workOrderType) {
			$.ajax({ 
				url : createURL("listConfig"),
				dataType : "json", 
				async : false, 
				data : { 
					cmsz : "yes", //configKey :		 "workorder_status%" 
					configKey : "workorder_type%" 
					}, 
				success :function(json) { 
					workOrderType = json.configList;
				}
			});
		}
		var dbconfig = $.grep(workOrderType, function(value, index) { 
			return value.key == key; 
		});
		if (dbconfig.length > 0) { 
			return	 ""+dbconfig[0].value; 
		} else {
			return null; 
		}
	
	};
})(jQuery, cloudStack);


