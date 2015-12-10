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
	cloudStack.sections.resourceStatistics = {
		title : '资源统计',
		id : 'resourceStatistics',

		// Domain tree
		treeView : {
			// Details
			detailView : {
				tabFilter : function(args) {
					var domainObj = args.context.domains[0];
					var hiddenTabs = [];
					if (domainObj.type == '5') {
						hiddenTabs.push("detailTable");
					} else {
						hiddenTabs.push("stats");
					}
					return hiddenTabs;
				},
				tabs : {
					detailTable : {
						title : 'label.statistics',
						listViewFilter : function(context, tabs) {
							var domainObj = context.domains[0];
							if (domainObj.type != '1') {
								return [ tabs.listViews[0] ];
							}
							return tabs.listViews;
						},
						listViews : [
								{
									id : 'resourcePool',
									firstClick : false,
									hideToolbar : true,
									fields : {
										'resourcePool' : {
											label : '资源池'
										},
										'total' : {
											label : '总量'
										},
										'used' : {
											label : '已使用'
										},
										'unused' : {
											label : '未使用'
										},
										'percentused' : {
											label : '使用率(%)'
										}
									},

									dataProvider : function(args) {
										var domainObj = args.context.domains[0];

										var url = "";
										var data = {
											listAll : true
										};

										if (domainObj.type == "1") {
											url = createURL('computeresource');
											data.cmsz = 'yes';
											data.resourcePoolId = domainObj.resourceId;
										} else if (domainObj.type == "2") {
											url = createURL('listCapacity');
											data.zoneid = domainObj.resourceId;
										} else if (domainObj.type == "3") {
											url = createURL('listCapacity');
											data.podid = domainObj.resourceId;
										} else if (domainObj.type == "4") {
											url = createURL('listCapacity');
											data.clusterid = domainObj.resourceId;
										}

										$.ajax({
											url : url,
											data : data,
											dataType : 'json',
											async : true,
											success : function(json) {
												var capacities = json.listcapacityresponse.capacity;
												var data = [];

												var showItems = {
													0 : {
														name : _l('label.memory')
													},
													1 : {
														name : _l('label.cpu')
													},
													2 : {
														name : _l('label.storage')
													},
													3 : {
														name : _l('label.primary.allocated')
													},
													4 : {
														name : _l('label.public.ips')
													},
													5 : {
														name : _l('label.management.ips')
													},
													6 : {
														name : _l('label.secondary.storage')
													},
													7 : {
														name : _l('label.vlan')
													},
													8 : {
														name : _l('label.direct.ips')
													},
													9 : {
														name : _l('label.local.storage')
													}
												};

												$.each(showItems, function(id, item) {
													var n = data.length;
													$(capacities).each(
															function(i) {
																var capacity = this;
																if (id == capacity.type) {
																	data[n] = {
																		resourcePool : item.name,
																		total : cloudStack.converters.convertByType(capacity.type,
																				capacity.capacitytotal),
																		used : cloudStack.converters.convertByType(capacity.type,
																				capacity.capacityused),
																		unused : cloudStack.converters.convertByType2(capacity.type,
																				capacity.capacitytotal, capacity.capacityused),
																		percentused : parseInt(capacity.percentused)
																	};
																	return false;
																}
															});

													if (data[n] == undefined || data[n] == null) {
														if (domainObj.type == "1" || domainObj.type == "2"
																|| (domainObj.type == "3" && ($.inArray(id, [ 0, 1, 2, 3, 5 ]) > -1))
																|| (domainObj.type == "4" && ($.inArray(id, [ 0, 1, 2, 3 ]) > -1))) {
															data[n] = {
																resourcePool : item.name,
																used : 0,
																unused : 0,
																total : 0,
																percentused : 0
															};
														}
													}
												});

												args.response.success({
													data : data
												});
											}
										});
									}
								}, {
									id : 'resourcePoolX86',
									firstClick : false,
									hideToolbar : true,
									fields : {
										'resourcePool' : {
											label : 'X86物理资源'
										},
										'total' : {
											label : '总量'
										},
										'used' : {
											label : '已使用'
										},
										'unused' : {
											label : '未使用'
										},
										'percentused' : {
											label : '使用率(%)'
										}
									},

									dataProvider : function(args) {
										var domainObj = args.context.domains[0];

										var url = "";
										var data = {
											listAll : true
										};

										if (domainObj.type == "1") {
											url = createURL('computeresource');
											data.type = 'workorder_type.X86PhysicalResourcesApplication';
											data.cmsz = 'yes';
											data.resourcePoolId = domainObj.resourceId;

											$.ajax({
												url : url,
												data : data,
												dataType : 'json',
												async : true,
												success : function(json) {
													var data = [ {
														resourcePool : '台数'
													}, {
														resourcePool : 'CPU资源'
													}, {
														resourcePool : '内存资源(MB)'
													} ];

													if (json) {
														$.each(json, function(i, val) {
															$.extend(data[i], {
																total : val.total,
																used : val.used,
																unused : val.unused,
																percentused : val.percentused
															});
														});
													}

													args.response.success({
														data : data
													});
												}
											});
										}

									}
								}, {
									id : 'resourcePoolHPVM',
									firstClick : false,
									hideToolbar : true,
									fields : {
										'resourcePool' : {
											label : 'HPVM资源'
										},
										'total' : {
											label : '总量'
										},
										'used' : {
											label : '已使用'
										},
										'unused' : {
											label : '未使用'
										},
										'percentused' : {
											label : '使用率(%)'
										}
									},

									dataProvider : function(args) {
										var domainObj = args.context.domains[0];

										var url = "";
										var data = {
											listAll : true
										};

										if (domainObj.type == "1") {
											url = createURL('computeresource');
											data.type = 'workorder_type.HPMinicomputerResourcesApplication';
											data.cmsz = 'yes';
											data.resourcePoolId = domainObj.resourceId;

											$.ajax({
												url : url,
												data : data,
												dataType : 'json',
												async : true,
												success : function(json) {
													var data = [ {
														resourcePool : 'VM数'
													}, {
														resourcePool : 'CPU资源'
													}, {
														resourcePool : '内存资源(MB)'
													} ];

													if (json) {
														$.each(json, function(i, val) {
															$.extend(data[i], {
																total : val.total,
																used : val.used,
																unused : val.unused,
																percentused : val.percentused
															});
														});
													}

													args.response.success({
														data : data
													});
												}
											});
										}

									}
								} ]
					},
					stats : {
						title : 'label.statistics',
						fields : {
							totalCPU : {
								label : 'label.total.cpu'
							},
							cpuused : {
								label : 'label.cpu.utilized'
							},
							cpuallocated : {
								label : 'label.cpu.allocated.for.VMs'
							},
							memorytotal : {
								label : 'label.memory.total'
							},
							memoryallocated : {
								label : 'label.memory.allocated'
							},
							memoryused : {
								label : 'label.memory.used'
							},
							networkkbsread : {
								label : 'label.network.read'
							},
							networkkbswrite : {
								label : 'label.network.write'
							}
						},
						dataProvider : function(args) {
							$.ajax({
								url : createURL("listHosts&id=" + args.context.domains[0].resourceId),
								dataType : "json",
								async : true,
								success : function(json) {
									var jsonObj = json.listhostsresponse.host[0];
									args.response
											.success({
												data : {
													totalCPU : jsonObj.cpunumber + " x " + cloudStack.converters.convertHz(jsonObj.cpuspeed),
													cpuused : jsonObj.cpuused,
													cpuallocated : (jsonObj.cpuallocated == null || jsonObj.cpuallocated == 0) ? "N/A"
															: jsonObj.cpuallocated,
													memorytotal : (jsonObj.memorytotal == null || jsonObj.memorytotal == 0) ? "N/A"
															: cloudStack.converters.convertBytes(jsonObj.memorytotal),
													memoryallocated : (jsonObj.memoryallocated == null || jsonObj.memoryallocated == 0) ? "N/A"
															: cloudStack.converters.convertBytes(jsonObj.memoryallocated),
													memoryused : (jsonObj.memoryused == null || jsonObj.memoryused == 0) ? "N/A"
															: cloudStack.converters.convertBytes(jsonObj.memoryused),
													networkkbsread : (jsonObj.networkkbsread == null) ? "N/A" : cloudStack.converters
															.convertBytes(jsonObj.networkkbsread * 1024),
													networkkbswrite : (jsonObj.networkkbswrite == null) ? "N/A" : cloudStack.converters
															.convertBytes(jsonObj.networkkbswrite * 1024)
												}
											});
								}
							});
						}
					}
				}
			},

			dataProvider : function(args) {
				var parentDomain = args.context.parentDomain;
				var data = {
					cmsz : 'yes',
					listAll : true
				};
				if (parentDomain == null) { // draw root node
					data.type = '1';
					$.ajax({
						url : createURL("listDimResourceTree"),// g_domainid
						dataType : "json",
						data : data,
						async : false,
						success : function(json) {
							var domainObjs = json.listresponse.dimresourceobj;
							if (domainObjs) {
								args.response.success({
									data : domainObjs
								});
							} else {
								$(".view").append("<br/><br/><br/>使用该功能前请先设置好一级资源池关联ZONE，并确保系统已与CloudStack进行了数据同步!");
							}
						}
					});
				} else {
					data.type = Number(parentDomain.type) + 1;
					data.preResourceId = parentDomain.resourceId;
					$.ajax({
						url : createURL("listDimResourceTree"),
						dataType : "json",
						data : data,
						async : false,
						success : function(json) {
							var domainObjs = json.listresponse.dimresourceobj;
							args.response.success({
								data : domainObjs
							});
						}
					});
				}
			}
		}
	};

})(cloudStack);
