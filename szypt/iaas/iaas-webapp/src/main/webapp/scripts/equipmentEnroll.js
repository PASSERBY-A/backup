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
	// 类型
	// 主机
	// 描述
	// 开启
	var fields = {
		targetType : "类型",
		ip : "主机",
		descript : "描述",
		available : "开启"
	};

	// 0:X86机架式服务器 1:X86刀片服务器 7:刀箱 待讨论 8:HP小型机
	var targetType = [ {
		type : 0,
		desc : "X86机架式服务器"
	}, {
		type : 1,
		desc : "X86刀片服务器"
	}, {
		type : 7,
		desc : "刀箱"
	}, {
		type : 8,
		desc : "HP小型机"
	} ];

	// 是否启动扫描 开启1 关闭0
	var available = [ {
		type : 1,
		desc : "开启"
	}, {
		type : 0,
		desc : "关闭"
	} ];

	cloudStack.sections.equipmentEnroll = {
		// id : 'equipmentEnroll',
		title : '自动发现目标',
		listView : {
			id : 'equipmentEnroll',
			fields : {
				ip : {
					label : fields.ip
				},
				targetType : {
					label : fields.targetType,
					converter : function(args) {
						$.each(targetType, function(i, val) {
							if (val.type == args) {
								args = val.desc;
								return false;
							}
						});
						return args;
					}
				},
				// descript : {
				// label : fields.descript
				// },
				available : {
					converter : function(args) {
						$.each(available, function(i, val) {
							if (val.type == args) {
								args = val.desc;
								return false;
							}
						});
						return args;
					},
					label : fields.available,
					indicator : {
						'1' : 'on',
						'0' : 'off'
					}
				}
			},

			actions : {
				add : {
					label : '注册',
					preFilter : function(args) {
						if (isAdmin())
							return true;
						else
							return false;
					},
					messages : {
						notification : function(args) {
							return '自动发现目标注册';
						}
					},

					createForm : {
						title : '自动发现目标注册',
						// desc : '自动发现目标注册',
						fields : {
							targetType : {
								label : fields.targetType,
								validation : {
									required : true
								},
								select : function(args) {
									var items = [];
									$.each(targetType, function(i, val) {
										items.push({
											id : this.type,
											description : this.desc
										});
									});
									args.response.success({
										data : items
									});
								}
							},
							ip : {
								label : fields.ip,
								validation : {
									required : true,
									maxlength : 100
								}
							},
							descript : {
								isTextarea : true,
								label : fields.descript,
								validation : {
									required : false,
									maxlength : 200
								}
							},
							available : {
								label : fields.available,
								validation : {
									required : true
								},
								select : function(args) {
									var items = [];
									$.each(available, function(i, val) {
										items.push({
											id : this.type,
											description : this.desc
										});
									});
									args.response.success({
										data : items
									});
								}
							}
						}
					},

					action : function(args) {
						var data = {
							cmsz : 'yes',
							targetType : args.data.targetType,
							ip : args.data.ip,
							descript : args.data.descript,
							available : args.data.available
						};

						$.ajax({
							url : createURL('createEquipmentEnroll'),
							type : "POST",
							data : data,
							success : function(json) {
								var item = json.listresponse.tequipmentenrollobj[0];
								if (item.id) {
									args.response.success({
										data : item
									});
								} else {
									args.response.error("同一种类型的同一个主机名称不运行重复注册");
								}
							},
							error : function(XMLHttpResponse) {
								args.response.error(parseXMLHttpResponse(XMLHttpResponse));
							}
						});
					},

					notification : {
						poll : function(args) {
							args.complete({
							// actionFilter : accountActionfilter
							});
						}
					}
				}
			},

			dataProvider : function(args) {
				var data = {};
				listViewDataProvider(args, data);

				$.extend(data, {
					cmsz : 'yes',
					page : args.page,
					pageSize : pageSize
				});

				$.ajax({
					url : createURL('listEquipmentEnroll'),
					data : data,
					success : function(json) {
						var items = json.listresponse.tequipmentenrollobj;
						args.response.success({
							// actionFilter : equipmentEnrollActionfilter,
							data : items
						});
					},
					error : function(json) {
						var errorMsg = parseXMLHttpResponse(json);
						args.response.error(errorMsg);
					}
				});
			},

			detailView : {
				// name : 'Account details',
				// isMaximized : true,
				// viewAll : {
				// path : 'accounts.users',
				// label : 'label.users'
				// },

				actions : {
					edit : {
						label : '更新',
						compactLabel : '更新自动发现目标',
						action : function(args) {
							var data = {
								cmsz : 'yes',
								id : args.context.equipmentEnroll[0].id,
								targetType : args.data.targetType,
								ip : args.data.ip,
								descript : args.data.descript,
								available : args.data.available
							};

							$.ajax({
								url : createURL('updateEquipmentEnroll'),
								data : data,
								success : function(json) {
									var result = json.listresponse.tequipmentenrollobj[0];
									if (result.id) {
										args.response.success({
											data : result
										});
									} else {
										args.response.error("同一种类型的同一个主机名称不运行重复注册");
									}
								},
								error : function(json) {
									var errorMsg = parseXMLHttpResponse(json);
									args.response.error(errorMsg);
								}
							});

						}
					},

					remove : {
						label : '删除',
						messages : {
							confirm : function(args) {
								return '是否删除自动发现目标？';
							},
							notification : function(args) {
								return '删除自动发现目标';
							}
						},
						action : function(args) {
							var data = {
								cmsz : 'yes',
								id : args.context.equipmentEnroll[0].id
							};
							$.ajax({
								url : createURL('removeEquipmentEnroll'),
								data : data,
								async : true,
								success : function(json) {
									var jid = json.listresponse.tequipmentenrollobj[0].id;
									args.response.success({
										_custom : {
											jobId : jid,
											getUpdatedItem : function(json) {
												return {}; // nothing
											}
										// ,
										// getActionFilter : function() {
										// return equipmentEnrollActionfilter;
										// }
										}
									});
								}
							});
						}
					// ,
					// notification : {
					// poll : pollAsyncJobResult
					// }
					}

				},

				// tabFilter : function(args) {
				// var hiddenTabs = [];
				// if (!isAdmin()) {
				// hiddenTabs.push('settings');
				// }
				// return hiddenTabs;
				// },

				tabs : {
					details : {
						title : 'label.details',

						fields : [ {
							ip : {
								label : fields.ip,
								isEditable : true,
								validation : {
									required : true,
									maxlength : 100
								}
							}
						}, {
							targetType : {
								label : fields.targetType,
								isEditable : true,
								converter : function(args) {
									$.each(targetType, function(i, val) {
										if (val.type == args) {
											args = val.desc;
											return false;
										}
									});
									return args;
								},
								validation : {
									required : true
								},
								select : function(args) {
									var items = [];
									$.each(targetType, function(i, val) {
										items.push({
											id : this.type,
											description : this.desc
										});
									});
									args.response.success({
										data : items
									});
								}
							},
							descript : {
								label : fields.descript,
								isEditable : true,
								isTextarea : true,
								validation : {
									required : false,
									maxlength : 200
								}
							},
							available : {
								label : fields.available,
								isEditable : true,
								converter : function(args) {
									$.each(available, function(i, val) {
										if (val.type == args) {
											args = val.desc;
											return false;
										}
									});
									return args;
								},
								validation : {
									required : true
								},
								select : function(args) {
									var items = [];
									$.each(available, function(i, val) {
										items.push({
											id : this.type,
											description : this.desc
										});
									});
									args.response.success({
										data : items
									});
								}
							}

						} ],

						dataProvider : function(args) {
							$.ajax({
								url : createURL('listEquipmentEnroll'),
								data : {
									cmsz : 'yes',
									id : args.context.equipmentEnroll[0].id
								},
								success : function(json) {
									var item = json.listresponse.tequipmentenrollobj[0];

									args.response.success({
										// actionFilter :
										// equipmentEnrollActionfilter,
										data : item
									});
								}
							});
						}
					}
				}
			}
		}
	};

	// var equipmentEnrollActionfilter = function(args) {
	// var allowedActions = [];
	//
	// // if (jsonObj.state == 'Destroyed')
	// // return [];
	// //
	// // if (isAdmin() && jsonObj.isdefault == false)
	// // allowedActions.push("remove");
	// //
	// // if (isAdmin()) {
	// // allowedActions.push("edit"); // updating networkdomain is allowed
	// // // on any account, including
	// // // system-generated default admin
	// // // account
	// // if (!(jsonObj.domain == "ROOT" && jsonObj.name == "admin" &&
	// // jsonObj.accounttype == 1)) { // if
	// // // not
	// // // system-generated
	// // // default
	// // // admin
	// // // account
	// // if (jsonObj.state == "enabled") {
	// // allowedActions.push("disable");
	// // allowedActions.push("lock");
	// // } else if (jsonObj.state == "disabled" || jsonObj.state == "locked")
	// // {
	// // allowedActions.push("enable");
	// // }
	// // allowedActions.push("remove");
	// // }
	// // allowedActions.push("updateResourceCount");
	// // } else if (isDomainAdmin()) {
	// // allowedActions.push("updateResourceCount");
	// // }
	// allowedActions.push("remove");
	// return allowedActions;
	// }

})(cloudStack);
