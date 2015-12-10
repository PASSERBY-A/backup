// Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) {
	cloudStack.sections.hpHostManagement = {
		title : '主机管理-HP小型机',
		id : 'hpHostManagement',
		listView : {
			id : 'hpHostManagement',
			label : '主机管理-HP小型机',
			/*
			 * filters: { all: { label: '所有' }, bad: { label: '待处理' }, ok: {
			 * label: '已归档' } },
			 */
			fields : {
				servername : {
					label : "名称"
				},
				serialnumber : {
					label : "序列号"
				},
				manufacturer : {
					label : "厂商"
				},
				productname : {
					label : '产品名称'
				},
				hostname : {
					label : 'IP地址/主机名'
				},
				cpucount : {
					label : 'CPU数量'
				},
				memory : {
					label : '内存(MB)'
				}
			/*
			 * state: { label: '状态', indicator: { '0': 'off', '1': 'off',
			 * "2":'on' }, converter: function(val){switch(val){ case "0":return
			 * '待处理'; case "1":return '待处理'; case "2":return '已归档'; }} }
			 */

			},

			dataProvider : function(args) {
				hpux = args.context.hpux != undefined ? args.context.hpux[0] : undefined;

				var data = {};
				if (hpux != undefined) {
					$.extend(data, {
						"hostName" : hpux.hostName
					});
				}
				listViewDataProvider(args, data);
				$.ajax({
					url : createURL("listhost&cmsz=yes&response=json&type=3"),
					data : data,
					async : true,
					success : function(json) {
						/*
						 * var items=[];
						 * items[0]={name:'IT-ESXXI01',ser_num:'CNGTx09UIO',vendor:'HP',pname:'Elite8440P',ip:'192.168.1.32',state:'0'};
						 * items[1]={name:'IT-ESXDS03',ser_num:'CNGTxCVNP8',vendor:'IBM',pname:'Folia940m',ip:'17.132.4.102',state:'1'};
						 * items[2]={name:'IT-ESXDS06',ser_num:'CXSDxCVNP8',vendor:'GOOGLE',pname:'Alio09m',ip:'172.15.34.9',state:'2'};
						 */
						args.response.success({
							data : json.hosts
						});
					}
				});
			},
			detailView : {
				name : '资产详情',
				viewAll : [ {
					path : 'resourcepools.snapshots',
					label : 'HBA'
				}, {
					path : 'hpux',
					label : '实例'
				}, {
					path : 'devinfolist',
					label : '设备信息'
				} ],
				actions : {

					// Remove single Alert
					/*
					 * remove: { label: '删除', messages: { notification:
					 * function(args) { return 'Alert Deleted'; }, confirm:
					 * function() { return 'Are you sure you want to delete this
					 * alert ?'; } }, action: function(args) {
					 * 
					 * $.ajax({ url: createURL("deleteAlerts&ids=" +
					 * args.context.alerts[0].id), success: function(json) {
					 * args.response.success();
					 * $(window).trigger('cloudStack.fullRefresh'); } });
					 *  } },
					 */

					scaleUp : {
						label : '修改资源池',
						createForm : {
							title : '更新所属资源池',
							desc : function(args) {
								var description = '更新所属资源池';
								return description;
							},
							fields : {
								resourcepoolid : {
									id : "resourcepoolid_hphost",
									label : '资源池',
									select : function(args) {
										var hostresourcepoolid = args.context.hpHostManagement[0].resourcepoolid;
										$.ajax({
											url : createURL("listresourcepool"),
											async : false,
											data : {
												cmsz : "yes"
											},
											success : function(json) {
												var pools = json.root;
												var items = [];
												$(pools).each(function() {
													items.push({
														id : this.resourcePoolId,
														description : this.name
													});
												});
												args.response.success({
													data : items
												});
												$("div.ui-dialog").find("select[name='resourcepoolid']").val(hostresourcepoolid);
											}
										});
									}
								}
							}
						},
						messages : {
							confirm : function(args) {
								return '确定修改所属资源池 ?';
							},
							notification : function(args) {
								return '修改所属资源池';
							}
						},
						notification : {
						// poll: pollAsyncJobResult
						},
						action : function(args) {

							var data = {
								cmsz : 'yes',
								response : 'json',
								hostid : args.context.hpHostManagement[0].id,
								resourcepoolid : args.data.resourcepoolid

							};
							$.ajax({
								url : createURL("updatehostrespool&cmsz=yes&response=json"),
								data : data,
								success : function(json) {
									args.response.success();
									$(window).trigger('cloudStack.fullRefresh');
								}

							});
						}
					}

				},

				tabs : {
					details : {
						title : 'label.details',
						fields : [ {
							state : {
								label : '操作提示',
								converter : function(val) {
									switch (val) {
									case "0":
										return '需要手工保存该新记录(新增)';
									case "1":
										return '需要手工保存该记录(编辑)';
									case "2":
										return '已归档';
									}
								}
							},
							name : {
								label : "服务器名称",
								isEditable : true
							},
							ser_num : {
								label : "序列号",
								isEditable : true
							},
							vendor : {
								label : "厂商",
								isEditable : true
							},
							pname : {
								label : '产品名称',
								isEditable : true
							},
							ip : {
								label : 'IP地址',
								isEditable : true
							},
							cpucount : {
								label : 'CPU数量',
								isEditable : true
							},
							memory : {
								label : '内存',
								isEditable : true
							},
							netcard : {
								label : '网卡',
								isEditable : true
							},
							resourcepool : {
								label : '资源池',
								select : function(args) {
									$.ajax({
										url : createURL("listresourcepool"),
										async : false,
										data : {
											cmsz : "yes"
										},
										success : function(json) {
											var pools = json.root;
											var items = [];
											$(pools).each(function() {
												items.push({
													id : this.resourcePoolId,
													description : this.name
												});
											});
											args.response.success({
												data : items
											});
										}
									});
								},
								isEditable : true
							}
						} ],
						dataProvider : function(args) {
							/*$.ajax({
								url : createURL('listVirtualMachines'),
								dataType : "json",
								async : true,
								success : function(json) {
									var item = {
										name : 'IT-ESXXI01',
										ser_num : 'CNGTx09UIO',
										vendor : 'HP',
										pname : 'Elite8440P',
										ip : '192.168.1.32',
										cpucount : '3',
										memory : '1023',
										netcard : '192.168.1.1',
										state : '0',
										action : '0'
									};
									args.response.success({
										data : item
									});
								}
							});*/
							/*setTimeout(function(){
								args.response.success({
									data : args.context.
								});
							},200);*/
						}
					}
				}
			}
		}

	}
})(jQuery, cloudStack);
