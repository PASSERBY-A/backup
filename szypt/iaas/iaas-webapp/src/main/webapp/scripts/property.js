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
	var X86HOST = 2;
	var X86RACKHOST = 1;
	
	var PROPERTY_X86HOST = 1;
	var PROPERTY_X86RACKHOST = 0;
	var PROPERTY_X86BOX = 7;

	cloudStack.sections.property = {
		title : '资产管理',
		id : 'property',
		sectionSelect : {
			label : 'label.select-view'
		},
		sections : {
			propertys : {
				type : 'select',
				title : '系统资产',
				listView : {
					id : 'propertys',
					fields : {
						code : {
							label : '设备编号'
						},
						type : {
							label : '设备类型',
							converter : function(val) {
								switch (val) {
								/*
								 * case "0":return '机架式X86服务器'; case "1":return
								 * '刀片式X86服务器';
								 */
								case "2":
									return '小型机';
								case "3":
									return '交换机';
								case "4":
									return '路由器';
								case "5":
									return 'SAN Switch';
								case "6":
									return '存储设备';
								default:
									return '';
								}
							}
						},
						vendor : {
							label : '厂商'
						},
						model : {
							label : '型号'
						},
						serial_num : {
							label : '设备序列号'
						},
						owner : {
							label : '负责人'
						},
						status : {
							label : '状态',
							converter : function(val) {
								switch (val) {
								case "0":
									return '有效';
								case "1":
									return '无效';
								default:
									return '';
								}
							}
						}
					},
					actions : {
						// Remove multiple events

						// Archive multiple events
						archive : {
							label : '添加资产',
							isHeader : true,
							addRow : false,
							messages : {
								notification : function(args) {
									return '添加资产';
								}
							},
							createForm : {
								title : '添加资产',
								desc : '',
								flag : 0,
								fields : {
									type : {
										label : '设备类型',
										validation : {
											required : true
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : '2',
													description : '小型机'
												}/*, {
													id : '3',
													description : '交换机'
												}, {
													id : '4',
													description : '路由器'
												}, {
													id : '5',
													description : 'SAN Switch'
												}, {
													id : '6',
													description : '存储设备'
												}*/ ]
											});
										}
									},

									vendor : {
										label : '厂商',
										validation : {
											required : true,
											maxlength : 15
										}
									},

									model : {
										label : '型号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									code : {
										label : '设备编号',
										validation : {
											required : true,
											number : true,
											maxlength : 15
										},
										docID : 'helpPropertyAdd'
									},

									start_date : {
										label : '上架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},

									serial_num : {
										label : '设备序列号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									cost : {
										label : '核算成本',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isDatepicker : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									},
									/*
									 * os_name_chn: { label: '应用系统中文' },
									 * 
									 * os_name_eng: { label: '应用系统英文' },
									 * belong_to:{ label:'所属中心' }, room:{
									 * label:'机房' }, machine_cabinet:{
									 * label:'机柜' }, using: { label: '是否在用',
									 * select: function(args) {
									 * args.response.success({ data: [{ id: '0',
									 * description: '否' }, { id: '1',
									 * description: '是' } ] }); } },
									 * 
									 * power: { label: '用电信息' },
									 * 
									 * check_point: { label: '是否巡检点', select:
									 * function(args) { args.response.success({
									 * data: [{ id: '0', description: '否' }, {
									 * id: '1', description: '是' } ] }); } },
									 */
									cpu_account : {
										label : 'cpu数量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									memory_size : {
										label : '内存大小',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									}

								}
							},
							action : function(args) {
								var data = {
									cmsz : 'yes',
									response : 'json'
								};
								if (args.data.type != "")
									$.extend(data, {
										type : args.data.type
									});

								if (args.data.vendor != "")
									$.extend(data, {
										vendor : args.data.vendor
									});

								if (args.data.model != "")
									$.extend(data, {
										model : args.data.model
									});

								if (args.data.code != "")
									$.extend(data, {
										code : args.data.code
									});
								if (args.data.start_date != "")
									$.extend(data, {
										start_date : args.data.start_date
									});

								if (args.data.end_date != "")
									$.extend(data, {
										end_date : args.data.end_date
									});

								if (args.data.serial_num != "")
									$.extend(data, {
										serial_num : args.data.serial_num
									});

								if (args.data.cost != "")
									$.extend(data, {
										cost : args.data.cost
									});
								if (args.data.u_bit != "" && args.data.u_bit > 0) {
									$.extend(data, {
										u_bit : args.data.u_bit
									});
								}

								if (args.data.u_high != "" && args.data.u_high > 0) {
									$.extend(data, {
										u_high : args.data.u_high
									});
								}

								if (args.data.position != "")
									$.extend(data, {
										position : args.data.position
									});

								if (args.data.position_desc != "")
									$.extend(data, {
										position_desc : args.data.position_desc
									});

								if (args.data.owner != "")
									$.extend(data, {
										owner : args.data.owner
									});
								if (args.data.contractInfo != "")
									$.extend(data, {
										contractInfo : args.data.contractInfo
									});

								if (args.data.service_period != "")
									$.extend(data, {
										service_period : args.data.service_period
									});
								if (args.data.expire_date != "")
									$.extend(data, {
										expire_date : args.data.expire_date
									});

								if (args.data.status != "")
									$.extend(data, {
										status : args.data.status
									});
								/*
								 * if (args.data.os_name_chn != "")
								 * $.extend(data, { os_name_chn:
								 * args.data.os_name_chn });
								 * 
								 * if (args.data.os_name_eng != "")
								 * $.extend(data, { os_name_eng:
								 * args.data.os_name_eng });
								 * 
								 * if (args.data.belong_to != "") $.extend(data, {
								 * belong_to: args.data.belong_to });
								 * 
								 * if (args.data.room != "") $.extend(data, {
								 * room: args.data.room });
								 * 
								 * if (args.data.machine_cabinet != "")
								 * $.extend(data, { machine_cabinet:
								 * args.data.machine_cabinet });
								 * 
								 * 
								 * if (args.data.using != "") $.extend(data, {
								 * using: args.data.using });
								 * 
								 * if (args.data.power != "") $.extend(data, {
								 * power: args.data.power });
								 * 
								 * if (args.data.check_point != "")
								 * $.extend(data, { check_point:
								 * args.data.check_point });
								 */

								if (args.data.cpu_account != "" && args.data.cpu_account > 0) {
									$.extend(data, {
										cpu_account : args.data.cpu_account
									});
								}

								if (args.data.memory_size != "" && args.data.memory_size > 0) {
									$.extend(data, {
										memory_size : args.data.memory_size
									});
								}

								if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
									$.extend(data, {
										hdd_size : args.data.hdd_size
									});
								}

								/*
								 * if(Date.parse(date1)>Date.parse(date2)){
								 * args.response.error("下架时间必须在上架时间"); }
								 */

								$.ajax({
									url : createURL("addProperty"),
									data : data,
									dataType : 'json',
									async : false,
									success : function(data) {
										if (data && data.root && data.root == "success") {
											cloudStack.dialog.hide();
											args.response.success();
											// return;
										} else if (data && data.root) {
											// args.response.error("新增失败:请根据设备类型输入对应的设备编码");
											cloudStack.dialog.notice({
												message : data.root
											});
											cloudStack.dialog.hide();
										}
									}
								});

							}
						}

					},

					advSearchFields : {
						type : {
							label : '设备类型',
							select : function(args) {
								args.response.success({
									data : [ {
										id : '',
										description : '所有类型'
									},

									{
										id : "2",
										description : '小型机'
									}/*, {
										id : '3',
										description : '交换机'
									}, {
										id : '4',
										description : '路由器'
									}, {
										id : '5',
										description : 'SAN Switch'
									}, {
										id : '6',
										description : '存储设备'
									}*/ ]
								});
							}
						},

						vendor : {
							label : '厂商',
							validation : {
								maxlength : 15
							}
						},

						model : {
							label : '型号',
							validation : {
								maxlength : 15
							}
						},
						code : {
							label : '设备编号',
							validation : {
								maxlength : 15
							}
						},
						start_date : {
							label : '上架时间',
							isDatepicker : true
							
						},
						end_date : {
							label : '下架时间',
							isDatepicker : true
						},
						serial_num : {
							label : '设备序列号',
							validation : {
								maxlength : 15
							}
						},

						owner : {
							label : '负责人',
							validation : {
								maxlength : 15
							}
						},
						searchFlag : {
							label : 'aaaaa',
							isHidden: true
						},

						status : {
							label : '状态',
							select : function(args) {
								args.response.success({
									data : [ {
										id : '',
										description : '全部'
									}, {
										id : '0',
										description : '有效'
									}, {
										id : '1',
										description : '无效'
									} ]
								});
							}
						}
					},

					dataProvider : function(args) {
						var data = {
							cmsz : 'yes',
							section : 'propertys',
							response : 'json'
						};
						listViewDataProvider(args, data);

						$.ajax({
							url : createURL('listProperty'),
							data : data,
							success : function(json) {
								var items = json.propertys;
								args.response.success({
									data : items
								});
							}
						});
					},

					detailView : {
						name : '资产详情',
						actions : {

							// Remove single event
							remove : {
								label : '删除',
								messages : {
									notification : function(args) {
										return '删除资产';
									},
									confirm : function() {
										return '确定要删除该资产吗?';
									}
								},
								action : function(args) {
									var data = {
										cmsz : 'yes',
										response : 'json'
									};
									$.ajax({
										url : createURL("deleteProperty&id=" + args.context.propertys[0].id),
										data : data,
										success : function(json) {
											args.response.success();
											$(window).trigger('cloudStack.fullRefresh');
										}

									});
								}
							},

							// Archive single event
							edit : {
								label : '编辑',
								messages : {
									notification : function(args) {
										return '资产修改';
									},
									confirm : function() {
										return '请确认是否重新调整该资产信息';
									}
								},
								action : function(args, event) {
									var data = {
										cmsz : 'yes',
										response : 'json'
									};
									
									$.extend(data, {
										type : args.context.propertys[0].type
									});

									if (args.data.vendor != "")
										$.extend(data, {
											vendor : args.data.vendor
										});

									if (args.data.model != "")
										$.extend(data, {
											model : args.data.model
										});
									if (args.data.code != "")
										$.extend(data, {
											code : args.data.code
										});
									if (args.data.start_date != "")
										$.extend(data, {
											start_date : args.data.start_date
										});

									if (args.data.end_date != "")
										$.extend(data, {
											end_date : args.data.end_date
										});

									if (args.data.serial_num != "")
										$.extend(data, {
											serial_num : args.data.serial_num
										});

									if (args.data.cost != "")
										$.extend(data, {
											cost : args.data.cost
										});
									if (args.data.u_bit != "" && args.data.u_bit > 0) {
										$.extend(data, {
											u_bit : args.data.u_bit
										});
									}
									if (args.data.u_high != ""  &&args.data.u_high>0){ $.extend(data, {
									  u_high: args.data.u_high }); 
									}/* else {
									  cloudStack.dialog.notice({message:"U高:请输入正整数"});
									  return; }*/
									 
									if (args.data.position != "")
										$.extend(data, {
											position : args.data.position
										});

									if (args.data.position_desc != "")
										$.extend(data, {
											position_desc : args.data.position_desc
										});

									if (args.data.owner != "")
										$.extend(data, {
											owner : args.data.owner
										});
									if (args.data.contractInfo != "")
										$.extend(data, {
											contractInfo : args.data.contractInfo
										});

									if (args.data.service_period != "")
										$.extend(data, {
											service_period : args.data.service_period
										});
									if (args.data.expire_date != "")
										$.extend(data, {
											expire_date : args.data.expire_date
										});

									if (args.data.status != "")
										$.extend(data, {
											status : args.data.status
										});
									/*
									 * if (args.data.os_name_chn != "")
									 * $.extend(data, { os_name_chn:
									 * args.data.os_name_chn });
									 * 
									 * if (args.data.os_name_eng != "")
									 * $.extend(data, { os_name_eng:
									 * args.data.os_name_eng });
									 * 
									 * if (args.data.belong_to != "")
									 * $.extend(data, { belong_to:
									 * args.data.belong_to });
									 * 
									 * if (args.data.room != "") $.extend(data, {
									 * room: args.data.room });
									 * 
									 * if (args.data.machine_cabinet != "")
									 * $.extend(data, { machine_cabinet:
									 * args.data.machine_cabinet });
									 * 
									 * 
									 * if (args.data.using != "") $.extend(data, {
									 * using: args.data.using });
									 * 
									 * if (args.data.power != "") $.extend(data, {
									 * power: args.data.power });
									 * 
									 * if (args.data.check_point != "")
									 * $.extend(data, { check_point:
									 * args.data.check_point });
									 */

									if (args.data.cpu_account != "" && args.data.cpu_account > 0) {
										$.extend(data, {
											cpu_account : args.data.cpu_account
										});
									}

									if (args.data.memory_size != "" && args.data.memory_size > 0) {
										$.extend(data, {
											memory_size : args.data.memory_size
										});
									}

									if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
										$.extend(data, {
											hdd_size : args.data.hdd_size
										});
									}

									$.ajax({
										url : createURL("addProperty&id=" + args.context.propertys[0].id),
										data : data,
										success : function(json) {
											if (json && json.root && json.root == "success") {
												args.response.success();
											} else if (json && json.root) {
												args.response.error(json.root);
												// cloudStack.dialog.notice({
												// message : json.root
												// });
												// $(window).trigger('cloudStack.fullRefresh');
											} else {
												args.response.error("发生未知错误，请与管理员联系！");
											}
										}
									});
								}
							}
						},
						tabs : {
							details : {
								title : '资产详情',
								fields : [ {
									type : {
										label : '设备类型',
										converter : function(val) {
											switch (val) {
											case "2":
												return '小型机';
											/*case "3":
												return '交换机';
											case "4":
												return '路由器';
											case "5":
												return 'SAN Switch';
											case "6":
												return '存储设备';*/
											default:
												return '';
											}
										}
									},
									vendor : {
										isEditable : true,
										label : '厂商'
									},
									model : {
										isEditable : true,
										label : '型号'
									},
									code : {
										isEditable : true,
										label : '设备编号'
									},
									serial_num : {
										isEditable : true,
										label : '设备序列号'
									},
									start_date : {
										label : '上架时间',
										isEditable : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isEditable : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										isDatepicker : true,
										validation : {
											required : true
										}
									},

									cost : {
										label : '核算成本',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},

									position_desc : {
										label : '位置描述',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},

									owner : {
										label : '负责人',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isEditable : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										isDatepicker : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										isEditable : true,
										converter : function(val) {
											switch (val) {
											case "0":
												return '有效';
											case "1":
												return '无效';
											default:
												return '';
											}
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										},
										isEditable : true
									},

									/*
									 * os_name_chn: { label: '应用系统中文',
									 * isEditable: true },
									 * 
									 * os_name_eng: { label: '应用系统英文',
									 * isEditable: true }, belong_to:{
									 * label:'所属中心', isEditable: true }, room:{
									 * label:'机房', isEditable: true },
									 * machine_cabinet:{ label:'机柜', isEditable:
									 * true },
									 * 
									 * 
									 * using: { label: '是否在用', select:
									 * function(args) { args.response.success({
									 * data: [{ id: '0', description: '否' }, {
									 * id: '1', description: '是' } ] }); },
									 * isEditable: true },
									 * 
									 * power: { label: '用电信息', isEditable: true },
									 * 
									 * check_point: { label: '是否巡检点', select:
									 * function(args) { args.response.success({
									 * data: [{ id: '0', description: '否' }, {
									 * id: '1', description: '是' } ] }); },
									 * isEditable: true },
									 */
									cpu_account : {
										label : 'cpu数量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},

									memory_size : {
										label : '内存大小',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},

									hdd_size : {
										label : '磁盘容量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									}

								} ],

								dataProvider : function(args) {
									data = {
										cmsz : 'yes',
										response : 'json',
										query : true
									};
									$.ajax({
										url : createURL("listProperty&id=" + args.id),
										dataType : "json",
										async : true,
										data : data,
										success : function(json) {
											var item = json.propertys[0];
											args.response.success({
												data : item
											});
										}
									});
								}
							}
						}
					}
				}
			},
			x86 : {
				type : 'select',
				title : '机架式X86服务器',
				listView : {
					id : 'x86',
					label : 'x86',
					filters : {
						all : {
							label : '所有'
						},
						deleteflag : {
							label : '待删除'
						},
						unarch : {
							label : '待归档'
						},
						ok : {
							label : '已归档'
						}
					},
					fields : {
						servername : {
							label : "服务器名称"
						},
						serialnumber : {
							label : "序列号"
						},
						manufacturer : {
							label : "厂商"
						},
						productname : {
							label : '产品名称'
						// ,isEditable: true
						},
						hostname : {
							label : 'IP地址'
						},
						/*
						 * cpucount:{ label:'CPU数量' }, memory:{ label:'内存' },
						 * netcard:{ label:'网卡' },
						 */
						status : {
							label : '状态',
							indicator : {
								'1' : 'off',
								'3' : 'off',
								"2" : 'on'
							},
							converter : function(val) {
								switch (val) {
								case 1:
									return '待归档';
								case 3:
									return '待删除';
								case 2:
									return '已归档';
								}
							}
						}

					},

					actions : {
						// Remove multiple events

						// Archive multiple events
						archive : {
							label : '添加资产',
							isHeader : true,
							addRow : false,
							messages : {
								notification : function(args) {
									return '添加资产';
								}
							},
							createForm : {
								title : '添加资产',
								desc : '',
								flag : 0,
								fields : {
									type : {
										label : '设备类型',
										validation : {
											required : true
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '机架式X86服务器'
												} ]
											});
										}
									},

									vendor : {
										label : '厂商',
										validation : {
											required : true,
											maxlength : 15
										}
									},

									productname : {
										label : '产名名称',
										validation : {
											required : true,
											maxlength : 20
										}
									},
									servername : {
										label : '服务器名称',
										validation : {
											required : true,
											maxlength : 20
										}
									},
									model : {
										label : '型号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									code : {
										label : '设备编号',
										validation : {
											required : true,
											number : true,
											maxlength : 15
										},
										docID : 'helpX86PropertyAdd'
									},

									start_date : {
										label : '上架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},

									serial_num : {
										label : '设备序列号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									cost : {
										label : '核算成本',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isDatepicker : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									},
									cpu_account : {
										label : 'cpu数量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									cpucores : {
										label : 'cpu核数',
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},

									memory_size : {
										label : '内存大小',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},
									nic : {
										label : '网卡个数',
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},
									hostname : {
										label : 'IP地址',
										validation : {
											required : false,
											maxlength : 15
										}
									},
									resourcepoolid : {
										label : '一级资源池',
										select : function(args) {
											var resourcePoolObj = {};

											$.ajax({
												url : createURL("listDimResourceTree"),
												dataType : "json",
												data : {
													cmsz : 'yes',
													type : "1"
												},
												async : false,
												success : function(json) {
													var domainObjs = json.listresponse.dimresourceobj;

													var items = [ {
														id : null,
														description : null
													} ];

													$(domainObjs).each(function() {
														items.push({
															id : this.resourceId,
															description : this.name
														});
													});
													args.response.success({
														data : items
													});
												}
											});
										}
									}
								}
							},
							action : function(args) {
								var data = {
									cmsz : 'yes',
									response : 'json'
								};
								if (args.data.type != "")
									$.extend(data, {
										type : args.data.type
									});

								if (args.data.vendor != "")
									$.extend(data, {
										vendor : args.data.vendor
									});

								if (args.data.model != "")
									$.extend(data, {
										model : args.data.model
									});

								if (args.data.code != "")
									$.extend(data, {
										code : args.data.code
									});
								if (args.data.start_date != "")
									$.extend(data, {
										start_date : args.data.start_date
									});

								if (args.data.end_date != "")
									$.extend(data, {
										end_date : args.data.end_date
									});

								if (args.data.serial_num != "")
									$.extend(data, {
										serial_num : args.data.serial_num
									});

								if (args.data.cost != "")
									$.extend(data, {
										cost : args.data.cost
									});
								if (args.data.u_bit != "" && args.data.u_bit > 0) {
									$.extend(data, {
										u_bit : args.data.u_bit
									});
								}

								if (args.data.u_high != "" && args.data.u_high > 0) {
									$.extend(data, {
										u_high : args.data.u_high
									});
								}

								if (args.data.position != "")
									$.extend(data, {
										position : args.data.position
									});

								if (args.data.position_desc != "")
									$.extend(data, {
										position_desc : args.data.position_desc
									});

								if (args.data.owner != "")
									$.extend(data, {
										owner : args.data.owner
									});
								if (args.data.contractInfo != "")
									$.extend(data, {
										contractInfo : args.data.contractInfo
									});

								if (args.data.service_period != "")
									$.extend(data, {
										service_period : args.data.service_period
									});
								if (args.data.expire_date != "")
									$.extend(data, {
										expire_date : args.data.expire_date
									});

								if (args.data.status != "")
									$.extend(data, {
										status : args.data.status
									});
								$.extend(data, {
									productname : args.data.productname,
									cputype : args.data.cputype,
									cpucores : args.data.cpucores,
									nic : args.data.nic,
									hostname : args.data.hostname,
									resourcepoolid : args.data.resourcepoolid,
									servername : args.data.servername
								});

								if (args.data.cpu_account != "" && args.data.cpu_account > 0) {
									$.extend(data, {
										cpu_account : args.data.cpu_account
									});
								}

								if (args.data.memory_size != "" && args.data.memory_size > 0) {
									$.extend(data, {
										memory_size : args.data.memory_size
									});
								}

								if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
									$.extend(data, {
										hdd_size : args.data.hdd_size
									});
								}

								/*
								 * if(Date.parse(date1)>Date.parse(date2)){
								 * args.response.error("下架时间必须在上架时间"); }
								 */

								$.ajax({
									url : createURL("addProperty"),
									data : data,
									dataType : 'json',
									async : false,
									success : function(data) {
										if (data && data.root && data.root == "success") {
											cloudStack.dialog.hide();
											args.response.success();
											// return;
										} else if (data && data.root) {
											// args.response.error("新增失败:请根据设备类型输入对应的设备编码");
											cloudStack.dialog.notice({
												message : data.root
											});
											cloudStack.dialog.hide();
										}
									}
								});

							}
						}

					},

					dataProvider : function(args) {

						var data = {
							"cmsz" : "yes",
							"type" : X86RACKHOST
						};
						listViewDataProvider(args, data);

						if (args.filterBy != null) { // filter dropdown

							if (args.filterBy.kind != null) {
								var status = "";
								var savestatus = "";
								var kind = args.filterBy.kind;
								if (kind == "deleteflag") {// 待删除
									savestatus = "1";// 己处理
									status = "3"; // 己废弃
								} else if (kind == "unarch") {
									savestatus = "0";// 未处理
									status = "1"; // 初始化
								} else if (kind == "ok") {
									savestatus = "1";// 己处理
									status = "2";// 己归档
								} else if (kind == "all" || kind == "") {

								}
								$.extend(data, {
									"status" : status,
									"saveStatus" : savestatus
								});
							}
							if (args.filterBy.search != null) {
								$.extend(data, {
									"hostName" : args.filterBy.search.value
								});
							}
						}
						$.ajax({
							url : createURL('listhost'),
							data : data,
							async : true,
							success : function(json) {
								/*
								 * console.info(json); var items=[];
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
						viewAll : {
							path : 'resourcepools.snapshots',
							label : 'HBA'
						},
						actions : {

							// Remove single Alert
							remove : {
								label : '删除',
								messages : {
									notification : function(args) {
										return '机架式X86服务器删除成功!';
									},
									confirm : function() {
										return '确认要删除该机架式X86服务器?';
									}
								},
								action : function(args) {

									$.ajax({
										url : createURL("removehost&id=" + args.context.x86[0].id + "&cmsz=yes"),
										success : function(json) {
											args.response.success();
											$(window).trigger('cloudStack.fullRefresh');
										}
									});
								}
							},

							edit : {
								label : '编辑',
								messages : {
									notification : function(args) {
										return '资产修改';
									},
									confirm : function() {
										return '请确认是否重新调整该资产信息';
									}
								},
								action : function(args, event) {
									
									var data = {
										cmsz : 'yes',
										response : 'json'
									};
									$.extend(data, {
										type : PROPERTY_X86RACKHOST,
										hostid: args.context.x86[0].id,
										id:args.data.id,
										vendor : args.data.manufacturer,
										model : args.data.model,
										code : args.data.code,
										serial_num : args.data.serialnumber,
										
										nic: args.data.nic,
										productname : args.data.productname,
										cputype : args.data.cputype,
										servername : args.data.servername,
										resourcepoolid:args.data.resourcepoolid
										
									});
					

									if (args.data.start_date != "")
										$.extend(data, {
											start_date : args.data.start_date
										});
									if (args.data.end_date != "")
										$.extend(data, {
											end_date : args.data.end_date
										});
									if (args.data.cost != ""){
										$.extend(data, {
											cost : args.data.cost
										});
									}
									if (args.data.u_bit != "" && args.data.u_bit > 0) {
										$.extend(data, {
											u_bit : args.data.u_bit
										});
									}
									if (args.data.u_high != "" && args.data.u_high > 0) {
										$.extend(data, {
											u_high : args.data.u_high
										});
									}
									if (args.data.position != "")
										$.extend(data, {
											position : args.data.position
										});

									if (args.data.position_desc != "")
										$.extend(data, {
											position_desc : args.data.position_desc
										});

									if (args.data.owner != "")
										$.extend(data, {
											owner : args.data.owner
										});
									if (args.data.contractInfo != "")
										$.extend(data, {
											contractInfo : args.data.contractInfo
										});

									if (args.data.service_period != "")
										$.extend(data, {
											service_period : args.data.service_period
										});
									if (args.data.expire_date != "")
										$.extend(data, {
											expire_date : args.data.expire_date
										});

									if (args.data.status != "")
										$.extend(data, {
											status : args.data.status
										});

									if (args.data.cpucount != "" && args.data.cpucount > 0) {
										$.extend(data, {
											cpu_account : args.data.cpucount
										});
									}
									
									if (args.data.cpucores != "" && args.data.cpucores > 0) {
										$.extend(data, {
											cpucores : args.data.cpucores
										});
									}

									if (args.data.memory != "" && args.data.memory > 0) {
										$.extend(data, {
											memory_size : args.data.memory
										});
									}

									if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
										$.extend(data, {
											hdd_size : args.data.hdd_size
										});
									}
									
									if (args.data.hostname != "")
										$.extend(data, {
											hostname : args.data.hostname
										});

									$.ajax({
										url : createURL("addProperty"),
										data : data,
										success : function(json) {
											if (json && json.root && json.root == "success") {
												args.response.success();
											} else if (json && json.root) {
												setTimeout(function(){
													args.response.error(json.root);
												},270);
											} else {
												args.response.error("发生未知错误，请与管理员联系！");
											}
										}
									});
								}
							}

						},

						tabs : {
							details : {
								title : 'label.details',
								preFilter : function(args) {
									return [ "id"];
								},
								fields : [ {
									id : {
										isEditable : true
									},
									type : {
										label : '设备类型',
										converter : function(args) {
											var data = [ {
												id : '0',
												description : '机架式X86服务器'
											} ];
											$.each(data, function() {
												if (this.id == args) {
													args = this.description;
													return false;
												}
											});
											return args;
										}
									},

									//vendor : {
									manufacturer:{
										label : '厂商',
										isEditable : true
									},
									
									productname : {
										label : '产名名称',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 20
										}
									},
									servername : {
										label : '服务器名称',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 20
										}
									},
									model : {
										label : '型号',
										isEditable : true
									},
									code : {
										label : '设备编号(以10开头)',
										isEditable : true,
										validation : {
											required : true
										}
									},

									start_date : {
										label : '上架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},
									//serial_num : {
									serialnumber:{
										label : '设备序列号',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									cost : {
										label : '核算成本',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										isEditable : true,
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									},
									cpucount : {
										label : 'cpu数量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									cpucores : {
										label : 'cpu核数',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},

									//memory_size : {
									memory:{
										label : '内存大小',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},
									nic : {
										label : '网卡个数',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},
									hostname : {
										label : 'IP地址',
										isEditable : true,
										validation : {
										//	required : true,
											maxlength : 15
										}
									},
									resourcepoolid : {
										label : '一级资源池',
										isEditable : true,

										select : function(args) {
											var resourcePoolObj = {};

											$.ajax({
												url : createURL("listDimResourceTree"),
												dataType : "json",
												data : {
													cmsz : 'yes',
													type : "1"
												},
												async : false,
												success : function(json) {
													var domainObjs = json.listresponse.dimresourceobj;

													var items = [ {
														id : null,
														description : null
													} ];

													$(domainObjs).each(function() {
														items.push({
															id : this.resourceId,
															description : this.name
														});
													});
													args.response.success({
														data : items
													});
												}
											});
										}
									}
								} ],
								dataProvider : function(args) {
									console.info(3);
									console.info(args);
									var hostId = 0;
									if(args.context.x86[0].hostId!=undefined && args.context.x86[0].hostId!=0){
										hostId = args.context.x86[0].hostId;
									}else{
										hostId = args.context.x86[0].id;
									}
									
									$.ajax({
										url : createURL('listhost'),
										data : {
											cmsz : "yes",
											id: hostId
										},
										async : true,
										success : function(json) {
											args.response.success({
												data : json && json.hosts && json.hosts.length > 0 ? json.hosts[0] : {}
											});
										}
									});
								}
							}
						}
					}
				}
			},
			x96 : {
				type : 'select',
				title : '刀片式X86服务器',
				listView : {
					id : 'x96',
					label : 'x86-刀片机',
					filters : {
						all : {
							label : '所有'
						},
						deleteflag : {
							label : '待删除'
						},
						unarch : {
							label : '待归档'
						},
						ok : {
							label : '已归档'
						}
					},
					fields : {
						servername : {
							label : "服务器名称"
						},
						serialnumber : {
							label : "序列号"
						},
						manufacturer : {
							label : "厂商"
						},
						productname : {
							label : '产品名称'
						// ,isEditable: true
						},
						/*hostname : {
							label : 'IP地址'
						},*/
						/*
						 * cpucount:{ label:'CPU数量' }, memory:{ label:'内存' },
						 * netcard:{ label:'网卡' },
						 */
						status : {
							label : '状态',
							indicator : {
								'1' : 'off',
								'3' : 'off',
								"2" : 'on'
							},
							converter : function(val) {
								switch (val) {
								case 1:
									return '待归档';
								case 3:
									return '待删除';
								case 2:
									return '已归档';
								}
							}
						}

					},

					actions : {
						// Remove multiple events

						// Archive multiple events
						archive : {
							label : '添加资产',
							isHeader : true,
							addRow : false,
							messages : {
								notification : function(args) {
									return '添加资产';
								}
							},
							createForm : {
								title : '添加资产',
								desc : '',
								flag : 0,
								fields : {
									type : {
										label : '设备类型',
										validation : {
											required : true
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : '1',
													description : '刀片式X86服务器'
												} ]
											});
										}
									},

									vendor : {
										label : '厂商',
										validation : {
											required : true,
											maxlength : 15
										}
									},

									productname : {
										label : '产名名称',
										validation : {
											required : true,
											maxlength : 20
										}
									},
									servername : {
										label : '服务器名称',
										validation : {
											required : true,
											maxlength : 20
										}
									},
									model : {
										label : '型号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									code : {
										label : '设备编号',
										validation : {
											required : true,
											number : true,
											maxlength : 15
										},
										docID : 'helpX96PropertyAdd'
									},

									start_date : {
										label : '上架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},

									serial_num : {
										label : '设备序列号',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									cost : {
										label : '核算成本',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '机箱序列号',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isDatepicker : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									},
									cpu_account : {
										label : 'cpu数量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									cpucores : {
										label : 'cpu核数',
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},

									memory_size : {
										label : '内存大小',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},
									nic : {
										label : '网卡个数',
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},
								/*	hostname : {
										label : 'IP地址',
										validation : {
											required : true,
											maxlength : 15
										}
									},*/
									resourcepoolid : {
										label : '一级资源池',
										select : function(args) {
											var resourcePoolObj = {};

											$.ajax({
												url : createURL("listDimResourceTree"),
												dataType : "json",
												data : {
													cmsz : 'yes',
													type : "1"
												},
												async : false,
												success : function(json) {
													var domainObjs = json.listresponse.dimresourceobj;

													var items = [ {
														id : null,
														description : null
													} ];

													$(domainObjs).each(function() {
														items.push({
															id : this.resourceId,
															description : this.name
														});
													});
													args.response.success({
														data : items
													});
												}
											});
										}
									}

								}
							},
							action : function(args) {
								var data = {
									cmsz : 'yes',
									response : 'json'
								};
								if (args.data.type != "")
									$.extend(data, {
										type : args.data.type
									});

								if (args.data.vendor != "")
									$.extend(data, {
										vendor : args.data.vendor
									});

								if (args.data.model != "")
									$.extend(data, {
										model : args.data.model
									});

								if (args.data.code != "")
									$.extend(data, {
										code : args.data.code
									});
								if (args.data.start_date != "")
									$.extend(data, {
										start_date : args.data.start_date
									});

								if (args.data.end_date != "")
									$.extend(data, {
										end_date : args.data.end_date
									});

								if (args.data.serial_num != "")
									$.extend(data, {
										serial_num : args.data.serial_num
									});

								if (args.data.cost != "")
									$.extend(data, {
										cost : args.data.cost
									});
								if (args.data.u_bit != "" && args.data.u_bit > 0) {
									$.extend(data, {
										u_bit : args.data.u_bit
									});
								}

								if (args.data.u_high != "" && args.data.u_high > 0) {
									$.extend(data, {
										u_high : args.data.u_high
									});
								}

								if (args.data.position != "")
									$.extend(data, {
										position : args.data.position
									});

								if (args.data.position_desc != "")
									$.extend(data, {
										position_desc : args.data.position_desc
									});

								if (args.data.owner != "")
									$.extend(data, {
										owner : args.data.owner
									});
								if (args.data.contractInfo != "")
									$.extend(data, {
										contractInfo : args.data.contractInfo
									});

								if (args.data.service_period != "")
									$.extend(data, {
										service_period : args.data.service_period
									});
								if (args.data.expire_date != "")
									$.extend(data, {
										expire_date : args.data.expire_date
									});

								if (args.data.status != "")
									$.extend(data, {
										status : args.data.status
									});
								$.extend(data, {
									productname : args.data.productname,
									cputype : args.data.cputype,
									cpucores : args.data.cpucores,
									nic : args.data.nic,
									hostname : args.data.hostname,
									resourcepoolid : args.data.resourcepoolid,
									servername : args.data.servername
								});
								if (args.data.cpu_account != "" && args.data.cpu_account > 0) {
									$.extend(data, {
										cpu_account : args.data.cpu_account
									});
								}

								if (args.data.memory_size != "" && args.data.memory_size > 0) {
									$.extend(data, {
										memory_size : args.data.memory_size
									});
								}

								if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
									$.extend(data, {
										hdd_size : args.data.hdd_size
									});
								}

								/*
								 * if(Date.parse(date1)>Date.parse(date2)){
								 * args.response.error("下架时间必须在上架时间"); }
								 */

								$.ajax({
									url : createURL("addProperty"),
									data : data,
									dataType : 'json',
									async : false,
									success : function(data) {
										if (data && data.root && data.root == "success") {
											cloudStack.dialog.hide();
											args.response.success();
											// return;
										} else if (data && data.root) {
											// args.response.error("新增失败:请根据设备类型输入对应的设备编码");
											cloudStack.dialog.notice({
												message : data.root
											});
											cloudStack.dialog.hide();
										}
									}
								});

							}
						}

					},

					dataProvider : function(args) {

						var data = {
							"cmsz" : "yes",
							section : 'x96',
							"type" : X86HOST
						};
						listViewDataProvider(args, data);

						if (args.filterBy != null) { // filter dropdown

							if (args.filterBy.kind != null) {
								var status = "";
								var savestatus = "";
								var kind = args.filterBy.kind;
								if (kind == "deleteflag") {// 待删除
									savestatus = "1";// 己处理
									status = "3"; // 己废弃
								} else if (kind == "unarch") {
									savestatus = "0";// 未处理
									status = "1"; // 初始化
								} else if (kind == "ok") {
									savestatus = "1";// 己处理
									status = "2";// 己归档
								} else if (kind == "all" || kind == "") {

								}
								$.extend(data, {
									"status" : status,
									"saveStatus" : savestatus
								});
							}
							if (args.filterBy.search != null) {
								$.extend(data, {
									"hostName" : args.filterBy.search.value
								});
							}
						}
						$.ajax({
							url : createURL('listhost'),
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
						viewAll : {
							path : 'resourcepools.snapshots',
							label : 'HBA'
						},
						actions : {

							// Remove single Alert
							remove : {

								label : '删除',

								messages : {
									notification : function(args) {
										return 'X86刀片机删除成功';
									},
									confirm : function(args) {
									//	console.info()
										return '确认要删除己保存的x86刀片机吗？';
									}
								},
								action : function(args) {

									$.ajax({
										url : createURL("removehost&id=" + args.context.x96[0].id + "&cmsz=yes"),
										success : function(json) {
											args.response.success();
											$(window).trigger('cloudStack.fullRefresh');
										}
									});

								}
							},

							edit : {
								label : '编辑',
								messages : {
									notification : function(args) {
										return '资产修改';
									},
									confirm : function() {
										return '请确认是否重新调整该资产信息';
									}
								},
								action : function(args, event) {
									var data = {
										cmsz : 'yes',
										response : 'json'
									};
									$.extend(data, {
										type : PROPERTY_X86HOST,
										hostid: args.context.x96[0].id,
										id:args.data.id,
										vendor : args.data.manufacturer,
										model : args.data.model,
										code : args.data.code,
										serial_num : args.data.serialnumber,
										
										nic: args.data.nic,
										productname : args.data.productname,
										cputype : args.data.cputype,
										servername : args.data.servername,
										resourcepoolid:args.data.resourcepoolid
										
									});
							/*		console.info(args);
									console.info("args.data");
									console.info(args.data);
									console.info("args.context.x96[0]");
									console.info(args.context.x96[0]);*/

									if (args.data.start_date != "")
										$.extend(data, {
											start_date : args.data.start_date
										});
									if (args.data.end_date != "")
										$.extend(data, {
											end_date : args.data.end_date
										});
									if (args.data.cost != ""){
										$.extend(data, {
											cost : args.data.cost
										});
									}
									if (args.data.u_bit != "" && args.data.u_bit > 0) {
										$.extend(data, {
											u_bit : args.data.u_bit
										});
									}
									if (args.data.u_high != "" && args.data.u_high > 0) {
										$.extend(data, {
											u_high : args.data.u_high
										});
									}
									if (args.data.position != "")
										$.extend(data, {
											position : args.data.position
										});

									if (args.data.position_desc != "")
										$.extend(data, {
											position_desc : args.data.position_desc
										});

									if (args.data.owner != "")
										$.extend(data, {
											owner : args.data.owner
										});
									if (args.data.contractInfo != "")
										$.extend(data, {
											contractInfo : args.data.contractInfo
										});

									if (args.data.service_period != "")
										$.extend(data, {
											service_period : args.data.service_period
										});
									if (args.data.expire_date != "")
										$.extend(data, {
											expire_date : args.data.expire_date
										});

									if (args.data.status != "")
										$.extend(data, {
											status : args.data.status
										});

									if (args.data.cpucount != "" && args.data.cpucount > 0) {
										$.extend(data, {
											cpu_account : args.data.cpucount
										});
									}
									
									if (args.data.cpucores != "" && args.data.cpucores > 0) {
										$.extend(data, {
											cpucores : args.data.cpucores
										});
									}

									if (args.data.memory != "" && args.data.memory > 0) {
										$.extend(data, {
											memory_size : args.data.memory
										});
									}

									if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
										$.extend(data, {
											hdd_size : args.data.hdd_size
										});
									}
									
									$.ajax({
										url : createURL("addProperty"),
										data : data,
										success : function(json) {
											if (json && json.root && json.root == "success") {
												args.response.success();
											} else if (json && json.root) {
												
												/*cloudStack.dialog.notice({
													message : json.root
												});*/
												setTimeout(function(){
													args.response.error(json.root);
												},270);
												//$(window).trigger('cloudStack.fullRefresh');
											} else {
												cloudStack.dialog.notice({
													message : "发生未知错误，请与管理员联系！"
												});
												$(window).trigger('cloudStack.fullRefresh');
											}
										}
									});
								}
							}

						},

						tabs : {
							details : {
								title : 'label.details',
								preFilter : function(args) {
									return [ "id","hostId" ];
								},
								fields : [ {
									id : {
										isEditable : true
									},
									hostId:{
										isEditable : true
									},
									hostType : {
										label : '设备类型',
										converter:function(args){
											if(args==X86HOST){
												return '刀片式X86服务器';
											}else{
												return args;
											}
										}/*,
										validation : {
											required : true
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : X86HOST,
													description : '刀片式X86服务器'
												} ]
											});
										}

										,isEditable : false*/
									},

									//vendor : {
									manufacturer:{
										label : '厂商',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},

									productname : {
										label : '产名名称',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 20
										}
									},
									servername : {
										label : '服务器名称',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 20
										}
									},
									model : {
										label : '型号',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									code : {
										label : '设备编号(以11开头)',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											maxlength : 15
										}
									},

									start_date : {
										label : '上架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},

									//serial_num : {
									serialnumber:{
										label : '设备序列号',
										validation : {
											required : true,
											maxlength : 15
										},
										isEditable : true
									},
									cost : {
										label : '核算成本',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									u_bit : {
										label : 'U位',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										isEditable : true,
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									},
									//cpu_account : {
									cpucount:{
										label : 'cpu数量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									cpucores : {
										label : 'cpu核数',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},
									
									memory : {
										label : '内存大小',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},
									nic : {
										label : '网卡个数',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										}
									},
									/*hostname : {
										label : 'IP地址',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},*/
									resourcepoolid : {
										label : '一级资源池',
										isEditable : true,

										select : function(args) {
											var resourcePoolObj = {};

											$.ajax({
												url : createURL("listDimResourceTree"),
												dataType : "json",
												data : {
													cmsz : 'yes',
													type : "1"
												},
												async : false,
												success : function(json) {
													var domainObjs = json.listresponse.dimresourceobj;

													var items = [ {
														id : null,
														description : null
													} ];

													$(domainObjs).each(function() {
														items.push({
															id : this.resourceId,
															description : this.name
														});
													});
													args.response.success({
														data : items
													});
												}
											});
										}
									}
								} ],
								dataProvider : function(args) {
									var hostId = 0;
									if(args.context.x96[0].hostId!=undefined && args.context.x96[0].hostId!=0){
										hostId = args.context.x96[0].hostId;
									}else{
										hostId = args.context.x96[0].id;
									}
									$.ajax({
										url : createURL('listhost'),
										data : {
											cmsz : "yes",
											id: hostId
										},
										async : true,
										success : function(json) {
											//console.info(json.hosts);
											args.response.success({
												data : json && json.hosts && json.hosts.length > 0 ? json.hosts[0] : {}
											});
										}
									});
								}
							}
						}
					}
				}
			},
			box : {
				type : 'select',
				title : '刀箱',
				listView : {
					id : 'box',
					label : 'box',
					filters : {
						all : {
							label : '所有'
						},
						deleteflag : {
							label : '待删除'
						},
						unarch : {
							label : '待归档'
						},
						ok : {
							label : '已归档'
						}
					},
					fields : {
						name : {
							label : "刀箱名称"
						},
						serialnumber : {
							label : "序列号"
						},
						uuid : {
							label : "UUID"
						},
						/*
						 * baycount:{ label:"卡槽数量" },
						 */
						type : {
							label : '类型'
						},
						status : {
							label : '状态',
							indicator : {
								'1' : 'off',
								'3' : 'off',
								"2" : 'on'
							},
							converter : function(val) {
								switch (val) {
								case 1:
									return '待归档';
								case 3:
									return '待删除';
								case 2:
									return '已归档';
								}
							}
						}
					},

					actions : {
						// Archive multiple Alerts
						archaive : {
							label : '新增资产',
							isHeader : true,
							addRow : false,
							messages : {
								notification : function(args) {
									return '新增刀箱成功';
								}
							},
							createForm : {
								title : '添加资产',
								desc : '',
								fields : {

									type : {
										label : '设备类型',
										validation : {
											required : true
										},
										select : function(args) {
											args.response.success({
												data : [ {
													id : '7',
													description : '刀箱'
												} ]
											});
										}
									},
									vendor : {
										label : '厂商',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									name : {
										label : "刀箱名称",
										validation : {
											required : true,
											maxlength : 50
										}
									},
									model : {
										label : '类型',
										validation : {
											required : true,
											maxlength : 100
										}
									},
									code : {
										label : '设备编号',
										validation : {
											required : true,
											number : true,
											maxlength : 15
										}
									},
									start_date : {
										label : '上架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isDatepicker : true,
										validation : {
											required : true
										}
									},
									serial_num : {
										label : '设备序列号',
										validation : {
											required : true,
											maxlength : 50
										}
									},
									uuid : {
										label : "UUID",
										validation : {
											required : true,
											maxlength : 50
										}
									},
									cost : {
										label : '核算成本',
										validation : {
											required : true,
											maxlength : 15
										}
									},
									position : {
										label : '位置编号',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isDatepicker : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									}
								}
							},
							action : function(args) {
								var data = {
									cmsz : 'yes',
									response : 'json'
								};
								if (args.data.type != "")
									$.extend(data, {
										type : args.data.type
									});

								if (args.data.vendor != "")
									$.extend(data, {
										vendor : args.data.vendor
									});

								if (args.data.model != "")
									$.extend(data, {
										model : args.data.model
									});

								if (args.data.code != "")
									$.extend(data, {
										code : args.data.code
									});
								if (args.data.start_date != "")
									$.extend(data, {
										start_date : args.data.start_date
									});

								if (args.data.end_date != "")
									$.extend(data, {
										end_date : args.data.end_date
									});

								if (args.data.serial_num != "")
									$.extend(data, {
										serial_num : args.data.serial_num
									});

								if (args.data.cost != "")
									$.extend(data, {
										cost : args.data.cost
									});
								if (args.data.position != "")
									$.extend(data, {
										position : args.data.position
									});

								if (args.data.position_desc != "")
									$.extend(data, {
										position_desc : args.data.position_desc
									});

								if (args.data.owner != "")
									$.extend(data, {
										owner : args.data.owner
									});
								if (args.data.contractInfo != "")
									$.extend(data, {
										contractInfo : args.data.contractInfo
									});

								if (args.data.service_period != "")
									$.extend(data, {
										service_period : args.data.service_period
									});
								if (args.data.expire_date != "")
									$.extend(data, {
										expire_date : args.data.expire_date
									});

								if (args.data.status != "")
									$.extend(data, {
										status : args.data.status
									});

								$.extend(data, {
									productname : args.data.name,
									uuid : args.data.uuid
								});

								$.ajax({
									url : createURL("addProperty"),
									data : data,
									dataType : 'json',
									async : false,
									success : function(data) {
										if (data && data.root && data.root == "success") {
											cloudStack.dialog.hide();
											args.response.success();
											// return;
										} else if (data && data.root) {
											// args.response.error("新增失败:请根据设备类型输入对应的设备编码");
											cloudStack.dialog.notice({
												message : data.root
											});
											cloudStack.dialog.hide();
										}
									}
								});

							}
						}
					},

					dataProvider : function(args) {
						var data = {
							"cmsz" : "yes",
							section : 'box'
						};
						listViewDataProvider(args, data);

						if (args.filterBy != null) { // filter dropdown

							if (args.filterBy.kind != null) {
								var status = "";
								var savestatus = "";
								var kind = args.filterBy.kind;
								if (kind == "deleteflag") {// 待删除
									savestatus = "1";// 己处理
									status = "3"; // 己废弃
								} else if (kind == "unarch") {
									savestatus = "0";// 未处理
									status = "1"; // 初始化
								} else if (kind == "ok") {
									savestatus = "1";// 己处理
									status = "2";// 己归档
								} else if (kind == "all" || kind == "") {

								}
								$.extend(data, {
									"status" : status,
									"saveStatus" : savestatus
								});
							}
							if (args.filterBy.search != null) {
								$.extend(data, {
									"name" : args.filterBy.search.value
								});
							}
						}

						$.ajax({
							url : createURL('listrack'),
							data : data,
							async : true,
							success : function(json) {
								/*
								 * var items=[];
								 * items[0]={name:'IT-ESXXI01',ser_num:'CNGTx09UIO',uuid:'09CNG151SWDX',type:'BladeSystem-c7000-Enclosure-G2',state:'0'};
								 * items[1]={name:'IT-ESDXI35',ser_num:'CCDGTUI89s',uuid:'32GHJ15WQCVS',type:'BladeSystem-c4320-Enclosure-D4',state:'1'};
								 * items[2]={name:'IT-CRFXXI1',ser_num:'CSDQmU09er',uuid:'JhsqLOIBVDFS',type:'BladeSystem-d1213-Enclosure-F8',state:'2'};
								 * args.response.success({ data: items });
								 */
								args.response.success({
									data : json.racks
								});

							}
						});
					},
					detailView : {
						name : '资产详情',
						viewAll : {
							path : 'view',
							label : '卡槽详情'
						},
						actions : {

							// Remove single Alert
							remove : {
								label : '删除',
								messages : {
									notification : function(args) {
										return '刀箱删除成功';
									},
									confirm : function() {
										return '确认要删除该刀箱记录?';
									}
								},
								action : function(args) {
									$.ajax({
										url : createURL("removerack&id=" + args.context.box[0].id + "&cmsz=yes"),
										success : function(json) {
											args.response.success();
											$(window).trigger('cloudStack.fullRefresh');
										}
									});

								}
							},

							edit : {
								label : '编辑',
								messages : {
									notification : function(args) {
										return '资产修改';
									},
									confirm : function() {
										return '请确认是否重新调整该资产信息';
									}
								},
								action : function(args, event) {
									var data = {
										cmsz : 'yes',
										response : 'json'
									};
								
									$.extend(data, {
										type : PROPERTY_X86BOX
									});

									if (args.data.vendor != "")
										$.extend(data, {
											vendor : args.data.vendor
										});

									if (args.data.model != "")
										$.extend(data, {
											model : args.data.model
										});

									if (args.data.code != "")
										$.extend(data, {
											code : args.data.code
										});
									if (args.data.start_date != "")
										$.extend(data, {
											start_date : args.data.start_date
										});

									if (args.data.end_date != "")
										$.extend(data, {
											end_date : args.data.end_date
										});

									if (args.data.serial_num != "")
										$.extend(data, {
											serial_num : args.data.serial_num
										});

									if (args.data.cost != "")
										$.extend(data, {
											cost : args.data.cost
										});
									if (args.data.u_bit != "" && args.data.u_bit > 0) {
										$.extend(data, {
											u_bit : args.data.u_bit
										});
									}
									if (args.data.u_high != "" && args.data.u_high > 0) {
										$.extend(data, {
											u_high : args.data.u_high
										});
									}
									if (args.data.position != "")
										$.extend(data, {
											position : args.data.position
										});

									if (args.data.position_desc != "")
										$.extend(data, {
											position_desc : args.data.position_desc
										});

									if (args.data.owner != "")
										$.extend(data, {
											owner : args.data.owner
										});
									if (args.data.contractInfo != "")
										$.extend(data, {
											contractInfo : args.data.contractInfo
										});

									if (args.data.service_period != "")
										$.extend(data, {
											service_period : args.data.service_period
										});
									if (args.data.expire_date != "")
										$.extend(data, {
											expire_date : args.data.expire_date
										});

									if (args.data.status != "")
										$.extend(data, {
											status : args.data.status
										});

									if (args.data.cpu_account != "" && args.data.cpu_account > 0) {
										$.extend(data, {
											cpu_account : args.data.cpu_account
										});
									}

									if (args.data.memory_size != "" && args.data.memory_size > 0) {
										$.extend(data, {
											memory_size : args.data.memory_size
										});
									}

									if (args.data.hdd_size != "" && args.data.hdd_size > 0) {
										$.extend(data, {
											hdd_size : args.data.hdd_size
										});
									}

									$.extend(data, {
										productname : args.data.name,
										uuid : args.data.uuid,
										id : args.data.id,
										code : args.data.code,
										rackid:args.context.box[0].id
									});
									$.ajax({
										url : createURL("addProperty"),
										data : data,
										success : function(json) {
											if (json && json.root && json.root == "success") {
												args.response.success();
											} else if (json && json.root) {
												setTimeout(function(){
													args.response.error(json.root);
												},270);
											} else {
												args.response.error("发生未知错误，请与管理员联系！");
											}
										}
									});
								}
							}

						},

						tabs : {
							details : {
								title : 'label.details',
								preFilter : function(args) {
									return [ "id" ,"rackId"];
								},
								fields : [ {
									id : {
										isEditable : true
									},
									rackId:{
										isEditable : true
									},
									type : {
										label : '设备类型',
										converter : function(val) {
											switch (val) {
											case "7":
												return '刀箱';
											default:
												return '';
											}
											/*var data = [ {
												id : '7',
												description : '刀箱'
											} ];
											$.each(data, function() {
												if (this.id == args) {
													args = this.description;
													return false;
												}
											});
											return args;*/
										}
									},
									vendor : {
										label : '厂商',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									name : {
										label : "刀箱名称",
										isEditable : true,
										validation : {
											required : true,
											maxlength : 50
										}
									},
									model : {
										label : '类型',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 100
										}
									},
									code : {
										label : '设备编号',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											maxlength : 15
										}
									},
									start_date : {
										label : '上架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},
									end_date : {
										label : '下架时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true
										}
									},
									serial_num : {
										label : '设备序列号',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 50
										}
									},
									uuid : {
										label : "UUID",
										isEditable : true,
										validation : {
											required : true,
											maxlength : 50
										}
									},
									cost : {
										label : '核算成本',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										}
									},
									/*u_bit : {
										label : 'U位',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},

									u_high : {
										label : 'U高',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1,
											maxlength : 15
										}
									},*/
									position : {
										label : '位置编号',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									position_desc : {
										label : '位置描述',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									owner : {
										label : '负责人',
										isEditable : true,
										validation : {
											required : true,
											maxlength : 15
										},
									},
									contractInfo : {
										label : '负责人联系方式',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									service_period : {
										label : '保修期限',
										isEditable : true,
										validation : {
											maxlength : 15,
											required : false
										}
									},
									expire_date : {
										label : '到保时间',
										isEditable : true,
										isDatepicker : true,
										converter : function(date) {
											return cloudStack.converters.toCloudDate(date);
										},
										validation : {
											required : true,
											maxlength : 15
										}
									},
									status : {
										label : '状态',
										isEditable : true,
										select : function(args) {
											args.response.success({
												data : [ {
													id : '0',
													description : '有效'
												}, {
													id : '1',
													description : '无效'
												} ]
											});
										}
									}
									/*,cpu_account : {
										label : 'cpu数量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},
									memory_size : {
										label : '内存大小',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									},

									hdd_size : {
										label : '磁盘容量',
										isEditable : true,
										validation : {
											required : true,
											number : true,
											min : 1
										},
									}*/

								} ],
								dataProvider : function(args) {
									var rackId = 0;
									if(args.context.box[0].rackId!=undefined && args.context.box[0].rackId!=0){
										rackId = args.context.box[0].rackId;
									}else{
										rackId = args.context.box[0].id;
									}
									
									$.ajax({
										url : createURL('listrack'),
										data : {
											cmsz : "yes",
											id : rackId
										},
										async : true,
										success : function(json) {
											args.response.success({
												data : json && json.racks && json.racks.length > 0 ? json.racks[0] : {}
											});
										}
									});
								}
							}
						}
					}
				}
			}

		}
	};

	var hostActionFilter = cloudStack.actionFilter.hostActionFilter = function(args) {
		var jsonObj = args.context.item;
		var allowedActions = [];
		if (jsonObj.saveStatus == 0) {// 未保存，去除删除控钮
			allowedActions.push("edit");
		} else if (jsonObj.saveStatus == 1) {
			if (jsonObj.status == 2) {// 己归档
				allowedActions.push("remove");
				allowedActions.push("edit");
			} else {
				allowedActions.push("edit");
			}

		}

		return allowedActions;
	};
})(cloudStack);
