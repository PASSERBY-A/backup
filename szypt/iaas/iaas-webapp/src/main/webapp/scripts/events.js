// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
(function(cloudStack) {
    cloudStack.sections.events = {
        title: 'label.menu.events',
        id: 'events',
        sectionSelect: {
            preFilter: function(args) {
                if (isAdmin())
                    return ["events", "alerts","messages"];
                else
                    return ["events"];
            },
            label: 'label.select-view'
        },
        sections: {
            events: {
                type: 'select',
                title: 'label.menu.events',
                listView: {
                    id: 'events',
                    label: 'label.menu.events',
                    fields: {
                        description: {
                            label: 'label.description'
                        },
                        level: {
                            label: 'label.level'
                        },
                        type: {
                            label: 'Type'
                        },
                        domain: {
                            label: 'label.domain'
                        },
                        account: {
                            label: 'label.account'
                        },
                        created: {
                            label: 'label.date',
                            converter: cloudStack.converters.toLocalDate
                        }
                    },

                    actions: {
                        // Remove multiple events
                        remove: {
                            label: 'label.delete.events',
                            isHeader: true,
                            addRow: false,
                            messages: {
                                notification: function(args) {
                                    return 'label.delete.events';
                                }
                            },
                            createForm: {
                                title: 'label.delete.events',
                                desc: '',
                                fields: {
                                    type: {
                                        label: 'label.by.event.type',
                                        docID: 'helpEventsDeleteType'
                                    },
                                    startdate: {
                                        label: 'label.by.date.start',
                                        docID: 'helpEventsDeleteDate',
                                        isDatepicker: true
                                    },
                                    enddate: {
                                        label: 'label.by.date.end',
                                        docID: 'helpEventsDeleteDate',
                                        isDatepicker: true
                                    }
                                }
                            },
                            action: function(args) {

                                var data = {};

                                if (args.data.type != "")
                                    $.extend(data, {
                                        type: args.data.type
                                    });

                                if (args.data.startdate != "")
                                    $.extend(data, {
                                        startdate: args.data.startdate
                                    });

                                if (args.data.enddate != "")
                                    $.extend(data, {
                                        enddate: args.data.enddate
                                    });

                                $.ajax({
                                    url: createURL("deleteEvents"),
                                    data: data,
                                    success: function(data) {
                                        args.response.success();
                                    }
                                });
                            }
                        },

                        // Archive multiple events
                        archive: {
                            label: 'label.archive.events',
                            isHeader: true,
                            addRow: false,
                            messages: {
                                notification: function(args) {
                                    return 'label.archive.events';
                                }
                            },
                            createForm: {
                                title: 'label.archive.events',
                                desc: '',
                                fields: {
                                    type: {
                                        label: 'label.by.event.type',
                                        docID: 'helpEventsArchiveType'
                                    },
                                    startdate: {
                                        label: 'label.by.date.start',
                                        docID: 'helpEventsArchiveDate',
                                        isDatepicker: true
                                    },
                                    enddate: {
                                        label: 'label.by.date.end',
                                        docID: 'helpEventsArchiveDate',
                                        isDatepicker: true
                                    }
                                }
                            },
                            action: function(args) {
                                var data = {};

                                if (args.data.type != "")
                                    $.extend(data, {
                                        type: args.data.type
                                    });

                                if (args.data.startdate != "")
                                    $.extend(data, {
                                        startdate: args.data.startdate
                                    });

                                if (args.data.enddate != "")
                                    $.extend(data, {
                                        enddate: args.data.enddate
                                    });

                                $.ajax({
                                    url: createURL("archiveEvents"),
                                    data: data,
                                    dataType: 'json',
                                    async: false,

                                    success: function(data) {
                                        args.response.success();
                                    }
                                });

                            }
                        }

                    },



                    advSearchFields: {
                        level: {
                            label: 'label.level',
                            select: function(args) {
                                args.response.success({
                                    data: [{
                                        id: '',
                                        description: ''
                                    }, {
                                        id: 'INFO',
                                        description: 'INFO'
                                    }, {
                                        id: 'WARN',
                                        description: 'WARN'
                                    }, {
                                        id: 'ERROR',
                                        description: 'ERROR'
                                    }]
                                });
                            }
                        },

                        domainid: {
                            label: 'Domain',
                            select: function(args) {
                                if (isAdmin() || isDomainAdmin()) {
                                    $.ajax({
                                        url: createURL('listDomains'),
                                        data: {
                                            listAll: true,
                                            details: 'min'
                                        },
                                        success: function(json) {
                                            var array1 = [{
                                                id: '',
                                                description: ''
                                            }];
                                            var domains = json.listdomainsresponse.domain;
                                            if (domains != null && domains.length > 0) {
                                                for (var i = 0; i < domains.length; i++) {
                                                    array1.push({
                                                        id: domains[i].id,
                                                        description: domains[i].path
                                                    });
                                                }
                                            }
                                            args.response.success({
                                                data: array1
                                            });
                                        }
                                    });
                                } else {
                                    args.response.success({
                                        data: null
                                    });
                                }
                            },
                            isHidden: function(args) {
                                if (isAdmin() || isDomainAdmin())
                                    return false;
                                else
                                    return true;
                            }
                        },

                        account: {
                            label: 'Account',
                            isHidden: function(args) {
                                if (isAdmin() || isDomainAdmin())
                                    return false;
                                else
                                    return true;
                            }
                        }
                    },

                    dataProvider: function(args) {
                        var data = {};
                        listViewDataProvider(args, data);

                        $.ajax({
                            url: createURL('listEvents'),
                            data: data,
                            success: function(json) {
                                var items = json.listeventsresponse.event;
                                args.response.success({
                                    data: items
                                });
                            }
                        });
                    },
                    detailView: {
                        name: 'label.details',
                        actions: {

                            // Remove single event
                            remove: {
                                label: 'Delete',
                                messages: {
                                    notification: function(args) {
                                        return 'Event Deleted';
                                    },
                                    confirm: function() {
                                        return 'Are you sure you want to remove this event?';
                                    }
                                },
                                action: function(args) {

                                    $.ajax({
                                        url: createURL("deleteEvents&ids=" + args.context.events[0].id),
                                        success: function(json) {
                                            args.response.success();
                                            $(window).trigger('cloudStack.fullRefresh');
                                        }

                                    });
                                }
                            },

                            // Archive single event
                            archive: {
                                label: 'Archive',
                                messages: {
                                    notification: function(args) {
                                        return 'Event Archived';
                                    },
                                    confirm: function() {
                                        return 'Please confirm that you want to archive this event.';
                                    }
                                },
                                action: function(args) {

                                    $.ajax({
                                        url: createURL("archiveEvents&ids=" + args.context.events[0].id),
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
                                title: 'label.details',
                                fields: [{
                                    description: {
                                        label: 'label.description'
                                    },
                                    state: {
                                        label: 'label.state'
                                    },
                                    level: {
                                        label: 'label.level'
                                    },
                                    type: {
                                        label: 'label.type'
                                    },
                                    domain: {
                                        label: 'label.domain'
                                    },
                                    account: {
                                        label: 'label.account'
                                    },
                                    username: {
                                        label: 'label.initiated.by'
                                    },
                                    created: {
                                        label: 'label.date',
                                        converter: cloudStack.converters.toLocalDate
                                    },
                                    id: {
                                        label: 'label.id'
                                    }
                                }],
                                dataProvider: function(args) {
                                    $.ajax({
                                        url: createURL("listEvents&id=" + args.context.events[0].id),
                                        dataType: "json",
                                        async: true,
                                        success: function(json) {
                                            var item = json.listeventsresponse.event[0];
                                            args.response.success({
                                                data: item
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            },
            alerts: {
                type: 'select',
                title: 'label.menu.alerts',
                listView: {
                    id: 'alerts',
                    label: 'label.menu.alerts',
                    fields: {
                        description: {
                            label: 'label.description'
                        },
                        type: {
                            label: 'Type'
                        },
                        sent: {
                            label: 'label.date',
                            converter: cloudStack.converters.toLocalDate
                        }
                    },

                    actions: {
                        // Remove multiple Alerts
                        remove: {
                            label: 'label.delete.alerts',
                            isHeader: true,
                            addRow: false,
                            messages: {
                                notification: function(args) {
                                    return 'label.delete.alerts';
                                }
                            },
                            createForm: {
                                title: 'label.delete.alerts',
                                desc: '',
                                fields: {
                                    type: {
                                        label: 'label.by.alert.type',
                                        docID: 'helpAlertsDeleteType'
                                    },
                                    startdate: {
                                        label: 'label.by.date.start',
                                        docID: 'helpAlertsDeleteDate',
                                        isDatepicker: true
                                    },
                                    enddate: {
                                        label: 'label.by.date.end',
                                        docID: 'helpAlertsDeleteDate',
                                        isDatepicker: true
                                    }
                                }
                            },
                            action: function(args) {

                                var data = {};

                                if (args.data.type != "")
                                    $.extend(data, {
                                        type: args.data.type
                                    });

                                if (args.data.startdate != "")
                                    $.extend(data, {
                                        startdate: args.data.startdate
                                    });

                                if (args.data.enddate != "")
                                    $.extend(data, {
                                        enddate: args.data.enddate
                                    });

                                $.ajax({
                                    url: createURL("deleteAlerts"),
                                    data: data,
                                    dataType: 'json',
                                    async: false,

                                    success: function(data) {
                                        args.response.success();
                                    }
                                });
                            }
                        },

                        // Archive multiple Alerts
                        archive: {
                            label: 'label.archive.alerts',
                            isHeader: true,
                            addRow: false,
                            messages: {
                                notification: function(args) {
                                    return 'label.archive.alerts';
                                }
                            },
                            createForm: {
                                title: 'label.archive.alerts',
                                desc: '',
                                fields: {
                                    type: {
                                        label: 'label.by.alert.type',
                                        docID: 'helpAlertsArchiveType'
                                    },
                                    startdate: {
                                        label: 'label.by.date.start',
                                        docID: 'helpAlertsArchiveDate',
                                        isDatepicker: true
                                    },
                                    enddate: {
                                        label: 'label.by.date.end',
                                        docID: 'helpAlertsArchiveDate',
                                        isDatepicker: true
                                    }

                                }
                            },
                            action: function(args) {
                                var data = {};

                                if (args.data.type != "")
                                    $.extend(data, {
                                        type: args.data.type
                                    });

                                if (args.data.startdate != "")
                                    $.extend(data, {
                                        startdate: args.data.startdate
                                    });

                                if (args.data.enddate != "")
                                    $.extend(data, {
                                        enddate: args.data.enddate
                                    });

                                $.ajax({
                                    url: createURL("archiveAlerts"),
                                    data: data,
                                    dataType: 'json',
                                    async: false,

                                    success: function(data) {
                                        args.response.success();
                                    }
                                });
                            }
                        }
                    },

                    dataProvider: function(args) {
                        var data = {};
                        listViewDataProvider(args, data);

                        $.ajax({
                            url: createURL('listAlerts'),
                            data: data,
                            async: true,
                            success: function(json) {
                                var items = json.listalertsresponse.alert;
                                args.response.success({
                                    data: items
                                });
                            }
                        });
                    },
                    detailView: {
                        name: 'Alert details',
                        actions: {

                            // Remove single Alert
                            remove: {
                                label: 'Delete',
                                messages: {
                                    notification: function(args) {
                                        return 'Alert Deleted';
                                    },
                                    confirm: function() {
                                        return 'Are you sure you want to delete this alert ?';
                                    }
                                },
                                action: function(args) {

                                    $.ajax({
                                        url: createURL("deleteAlerts&ids=" + args.context.alerts[0].id),
                                        success: function(json) {
                                            args.response.success();
                                            $(window).trigger('cloudStack.fullRefresh');
                                        }
                                    });

                                }
                            },

                            archive: {
                                label: 'Archive',
                                messages: {
                                    notification: function(args) {
                                        return 'Alert Archived';
                                    },
                                    confirm: function() {
                                        return 'Please confirm that you want to archive this alert.';
                                    }
                                },
                                action: function(args) {

                                    $.ajax({
                                        url: createURL("archiveAlerts&ids=" + args.context.alerts[0].id),
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
                                title: 'label.details',
                                fields: [{
                                    id: {
                                        label: 'ID'
                                    },
                                    description: {
                                        label: 'label.description'
                                    },
                                    sent: {
                                        label: 'label.date',
                                        converter: cloudStack.converters.toLocalDate
                                    }
                                }],
                                dataProvider: function(args) {
                                    $.ajax({
                                        url: createURL("listAlerts&id=" + args.context.alerts[0].id),
                                        dataType: "json",
                                        async: true,
                                        success: function(json) {
                                            var item = json.listalertsresponse.alert[0];
                                            args.response.success({
                                                data: item
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            },
            messages:{
	        	type: 'select',
                title: '消息',
                listView: {
                    id: 'messages',
                    label: '消息',
                    fields: {
                    	serialnumber: {
                            label: '设备序列号'
                        },
                        eventtype: {
                            label: '事件类型',
                             converter: function(val){
                            	 switch(val){
	               				case "1":return '增加物理机';
	               				case "2":return '移除物理机';
	               				case "3":return '变化位置';
	               				case "4":return '物理机状态变化';
               				}}
                        },
                        eventstatus: {
         	                label: '事件状态',
         	                indicator: {
                                 1: 'off',
                                 2: 'on',
                             },
            			converter: function(val){switch(val){
	        				case 1:return '待处理';
	        				case 2:return '已处理';
	        				}}
         	            },
         	           descript: {
                            label: '事件描述'
                        },
                        createdOn: {
                            label: '事件发生日期',
                            converter: function(val){
                            	return cloudStack.converters.toCloudDate(val,"1");
                            }
                        }
                    },

                   /* actions: {
                        // Remove multiple events
                        remove: {
                            label: 'label.delete.events',
                            isHeader: true,
                            addRow: false,
                            messages: {
                                notification: function(args) {
                                    return 'label.delete.events';
                                }
                            },
                            createForm: {
                                title: 'label.delete.events',
                                desc: '',
                                fields: {
                                    type: {
                                        label: 'label.by.event.type',
                                        docID: 'helpEventsDeleteType'
                                    },
                                    startdate: {
                                        label: 'label.by.date.start',
                                        docID: 'helpEventsDeleteDate',
                                        isDatepicker: true
                                    },
                                    enddate: {
                                        label: 'label.by.date.end',
                                        docID: 'helpEventsDeleteDate',
                                        isDatepicker: true
                                    }
                                }
                            },
                            action: function(args) {

                                var data = {};

                                if (args.data.type != "")
                                    $.extend(data, {
                                        type: args.data.type
                                    });

                                if (args.data.startdate != "")
                                    $.extend(data, {
                                        startdate: args.data.startdate
                                    });

                                if (args.data.enddate != "")
                                    $.extend(data, {
                                        enddate: args.data.enddate
                                    });

                                $.ajax({
                                    url: createURL("deleteEvents"),
                                    data: data,
                                    success: function(data) {
                                        args.response.success();
                                    }
                                });
                            }
                        },

                    },*/

                    dataProvider: function(args) {
                        var data = {};
                        listViewDataProvider(args, data);
                        $.extend(data,{"cmsz":"yes"})
                        $.ajax({
                            url: createURL('listmessage'),
                            data: data,
                            success: function(json) {
                            	
                                //var items = json.listeventsresponse.event;
                                /* var items=[];
                                 items[0]={ser_num:'CNGTx09UIO',type:'1',status:'1',desc:'新加了一台刀片机',created:'2014-05-12'};
                                 items[1]={ser_num:'CNGTxCVNP8',type:'2',status:'2',desc:'x86服务器DD已经移除',created:'2014-05-12'};
                                 items[2]={ser_num:'CXSDxCVNP8',type:'3',status:'1',desc:'刀片机XX槽位发生改变',created:'2014-05-12'};*/
                                args.response.success({
                                    data: json.messages
                                });
                            }
                        });
                    },
                    detailView: {
                        name: 'label.details',
                       viewAll:1==0?{
                            path: 'property.x86',
                            label: '物理资源'
                       }:{
                           path: 'property.x96',
                           label: '物理资源'
                      },
                    /*  viewAll:{
                    	  preFilter: function(args) {
                    		  var message=args.context.messages[0];
                       	   		if(message.type=="1"){
                       	   			return {
                                        path: 'property.x86',
                                        label: '物理资源'
                                   };
                       	   		}else{
                       	   			return {
                                        path: 'property.x96',
                                        label: '物理资源'
                                   };
                       	   		}
                          }
                      },*/
                        actions: {

                            // Remove single event
                            remove: {
                                label: '删除',
                                messages: {
                                    notification: function(args) {
                                        return '删除消息';
                                    },
                                    confirm: function() {
                                        return '确定要删掉该消息吗？';
                                    }
                                },
                                action: function(args) {
                                	
                                    $.ajax({
                                        url: createURL("deletemessage&cmsz=yes&id=" + args.context.messages[0].id),
                                        success: function(json) {
                                            args.response.success();
                                            $(window).trigger('cloudStack.fullRefresh');
                                        }

                                    });
                                }
                            },

                        },
                        tabs: {
                            details: {
                                title: 'label.details',
                                fields: [{
                                	serialnumber: {
			                            label: '设备序列号'
			                        },
			                        eventtype: {
			                            label: '事件类型',
			                             converter: function(val){switch(val){
				               				case "1":return '增加物理机';
				               				case "2":return '移除物理机';
				               				case "3":return '变化位置';
				               				case "4":return '物理机状态变化';
			               				}}
			                        },
			                        
			         	           eventstatus: {
			         	                label: '事件状态',
				            			converter: function(val){switch(val){
					        				case 1:return '待处理';
					        				case 2:return '已处理';
					        				}}
			         	            },
			         	           descript: {
			                            label: '事件描述'
			                        },
			                        createdOn: {
			                            label: '事件发生日期',
			                            converter: function(val){
			                            	return cloudStack.converters.toCloudDate(val,"1");
			                            }
			                        }
                                }],
                                dataProvider: function(args) {
                                	  args.response.success({
                                          data: args.context.messages[0]
                                      });
                                    /*$.ajax({
                                        url: createURL("listEvents"),
                                        dataType: "json",
                                        async: true,
                                        success: function(json) {
                                            //var item = json.listeventsresponse.event[0];
                                        	
                                            var item={ser_num:'CNGTx09UIO',type:'1',status:'1',desc:'新加了一台刀片机',created:'2014-05-12'};
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
	        }
        }
    };
})(cloudStack);
