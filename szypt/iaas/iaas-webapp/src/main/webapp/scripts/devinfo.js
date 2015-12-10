// Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) {
    cloudStack.sections.devinfolist = {
        title: '设备列表',
        id: 'devinfolist',
        listView: {
            section: 'devinfolist',
            filters : false,
            firstClick : false,
            disableInfiniteScrolling : true,
            preFilter: function(args) {
                var hiddenFields = [];
               /* if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            fields:{
            	vmname : {
    				label : "VM名称"
    			},
    			vmdevtype : {
    				label : "VM设备类型"
    			},
    			target : {
    				label : "目标"
    			},
    			storetype:{
    				label:'存储类型'
    			},
    			hostdevname:{
    				label : "HOST设备名称"
    			}
    		},

            dataProvider: function(args) {
                var data = {"cmsz":"yes","hostname":args.context.hpHostManagement[0].hostname,"listAll":"true"};
                $.ajax({
                    url: createURL('listdevinfo'),
                    data: data,
                    success: function(json) {
                    	console.info(json);
                        //var items = json.listvirtualmachinesresponse.virtualmachine;
                       /* var items=[];
                        items[0]={iloName:'IT-ESXXI01',ser_num:'CNGTx09UIO',ser_name:'HP',ser_name:'Elite8440P',iloIP:'192.168.1.32',status:'Ok',power:'on'};
                        items[1]={iloName:'IT-ESXDS03',ser_num:'CNGTxCVNP8',ser_name:'IBM',ser_name:'Folia9470m',iloIP:'17.132.4.102',status:'error',power:'off'};*/
                        args.response.success({
                            data: json.devinfos
                        });
                    }
                });

            }
        }
    };


})(jQuery, cloudStack);
