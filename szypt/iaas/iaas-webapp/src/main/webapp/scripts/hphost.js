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
(function($, cloudStack) {
    cloudStack.sections.view = {
        title: '卡槽列表',
        id: 'view',
        listView: {
            section: 'view',
            filters : false,
            preFilter: function(args) {
                var hiddenFields = [];
               /* if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            fields:{
            	iloname : {
    				label : "iLO名称"
    			},
    			iloip : {
    				label : "iLOIP"
    			},
    			servername:{
    				label:'服务实例名称'
    			},
    			serialnum:{
    				label : "刀片机序列号"
    			},
    			status:{
    				label:"运行状态"
    			},
    			powerflag:{
    				label:'开关机状态',
    				 indicator: {
                         'Off': 'off',
                         "On":'on'
                     },
    			}
    		},

            dataProvider: function(args) {
                /*var data = {};
                $.ajax({
                    url: createURL('listVirtualMachines'),
                    data: data,
                    success: function(json) {
                        //var items = json.listvirtualmachinesresponse.virtualmachine;
                        var items=[];
                        items[0]={iloName:'IT-ESXXI01',ser_num:'CNGTx09UIO',ser_name:'HP',ser_name:'Elite8440P',iloIP:'192.168.1.32',status:'Ok',power:'on'};
                        items[1]={iloName:'IT-ESXDS03',ser_num:'CNGTxCVNP8',ser_name:'IBM',ser_name:'Folia9470m',iloIP:'17.132.4.102',status:'error',power:'off'};
                        args.response.success({
                            data: items
                        });
                    }
                });*/
            //	alert(33);
            	args.response.success({
                    data: args.context.box[0].bayinfos
                });
            },

            detailView: {
                name: '卡槽详请',
                  actions: {
                    
	    			},
                tabs: {
                    details: {
                        title: '刀箱其他信息详情',
                        fields:[{
                        	iloname : {
                				label : "iLO名称"
                			},
                			iloip : {
                				label : "iLOIP"
                			},
                			servername:{
                				label:'服务实例名称'
                			},
                			serialnum:{
                				label : "序列号"
                			},
                			status:{
                				label:"运行状态"
                			},
                			powerflag:{
                				label:'开关机状态',
                				 indicator: {
                                     'Off': 'off',
                                     "On":'on'
                                 }
                			}
                    }],
                    dataProvider: function(args) {
                    	
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
                                var item={iloName:'IT-ESXXI01',ser_num:'CNGTx09UIO',ser_name:'HP',ser_name:'Elite8440P',iloIP:'192.168.1.32',status:'Ok',power:'on'};
                                args.response.success({
                                    data: item
                                });
                            }
                        });*/
                    	setTimeout(function(){
                    		args.response.success({
                                data: args.context.view[0]
                            });
                    	},200);
                    	 
                    }
    			}
    		}
            }
        }
    };


})(jQuery, cloudStack);
