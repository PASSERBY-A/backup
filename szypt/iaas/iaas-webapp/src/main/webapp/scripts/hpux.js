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
    var vmMigrationHostObjs;
    cloudStack.sections.hpux = {
        title: 'HP小型机实例',
        id: 'hpux',
        listView: {
        	disableInfiniteScrolling : true,
            section: 'hpux',
            preFilter: function(args) {
                var hiddenFields = [];
               /* if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            fields: {
	            name: {
	                label: '名称'
	            },
	            os: {
	                label: 'OS类型',
	                converter: function(osobj){
	                	return osobj.name;
    				}
	            },
	            vcpu_number:{
	            	label: 'cpu个数'
	            },
	            memory: {
	                label: '内存(GB)',
	                converter: function(memoryObj){
	                	return memoryObj.total;
    				}
	            },
	            vm_state: {
	                label: '状态',
	                indicator: {
                        'On': 'on',
                        'Off': 'off'
                    }
	            }
	        },

            dataProvider: function(args) {
            	console.info(args);
            	var data = {};
            	if(args.filterBy.search.value!=""){
            		$.extend(data,{"name":args.filterBy.search.value});
            	}
                if(args.context.hpHostManagement!=null){
                	$.extend(data,{"hostname":args.context.hpHostManagement[0].hostname});
                }else if(args.context.resource_host!=null){
                	$.extend(data,{"hostname":args.context.resource_host[0].hostname});
                }
                $.ajax({
                    url: createURL('listvm&cmsz=yes&page=1&pagesize=10000'),
                    data: data,
                    success: function(json) {
                    	
                        //var items = json.listvirtualmachinesresponse.virtualmachine;
                        /*var items=[];
                        items[0]={name:'虚拟机1',type:'HPOS',cpucount:4,memory:1024,state:'Running'};
                        items[1]={name:'虚拟机2',type:'HPUX',cpucount:8,memory:2048,state:'Stopped'};
                        items[2]={name:'虚拟机3',type:'HPUX',cpucount:16,memory:512,state:'Destroyed'};
                        items[3]={name:'虚拟机4',type:'HPOS',cpucount:2,memory:1024,state:'Error'};*/
                        args.response.success({
                            data: json.vmlist
                        });
                    }
                });
            },

            detailView: {
                name: '实例详情',
                viewAll: {
                    path: 'hpHostManagement',
                    label: '主机'
                },
                tabs: {
                    details: {
                        title: '实例详情',
                        fields:[{
                        	  name: {
      			                label: '名称'
      			            },
      			           os: {
      			                label: 'OS类型',
	      			              converter: function(osobj){
	      		                	return osobj.name;
	      	    				}
      			            },
      			          vcpu:{
      			            	label: 'cpu个数',
      			            	converter: function(vcpu){
       			                	return vcpu.numberVcpus;
       		    				}
      			            	
      			            },
      			            memory: {
      			            	label: '内存(GB)',
      			                converter: function(memoryObj){
      			                	return memoryObj.total;
      		    				}
      			            },
      			          guest_ipv4_address:{label : "ip"},
      			            
      			          pattern_path:{
      			        	  label : "模式路径"
      			        	  
      			          },
      			          storage:{
      			        	label:"物理驱动",
      			        	  converter: function(storage){
  			                	return storage.device[0].physical_device;
  		    				  }
      			          },
      			        /*storage:{
      			        	label:"物理存储",
    			        	  converter: function(storage){
			                	return storage.device[0].physical_storage;
		    				  }
    			          },*/
      			        runnable_status:{label:"是否可运行"},
      			        
      			          
      			            vm_state: {
      			                label: '状态',
      			                indicator: {
      		                        'On': 'on',
      		                        'Off': 'off'
      		                    }
      			            }
                    }],
                    dataProvider: function(args) {
                    	
                    	var data = {
    		                	cmsz: 'yes',
    		                    query:true,
    		                    hostname : args.context.hpux[0].hostName,
    		                    vmname : args.context.hpux[0].name
                        	};
                    	//console.info(data);
                        $.ajax({
                        	url: createURL("getvminfo"),
                            dataType: "json",
                            async: true,
                            data:data,
                            success: function(json) {
                               // var item = json.propertys[0];
                            	console.info(json);
                            	/*
                                var item={name:'虚拟机1',type:'HPOS',cpucount:4,memory:1024,state:'Running'};*/
                                args.response.success({
                                    data: json.vm
                                });
                            }
                        });
                    }
    			}
    		}
            }
        }
    };


})(jQuery, cloudStack);
