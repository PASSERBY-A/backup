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
(function(cloudStack, $) {

	var accountOwnerType = null;
	var domainOwnerType = null;
	var projectOwnerType = null;
	var resourcePoolPermission = {
		accounts : {
			type : 'select',
			title : 'label.accounts',
			listView : {
				id : 'accounts',
				ownerType : {},
				firstClick : false,
				filters : false,
				fields : {
					accountName : {
						label : 'label.name'
					},
					accountType : {
						label : 'label.role',
						converter : function(args) {
							return cloudStack.converters.toRole(args);
						}
					},
					accountDomain : {
						label : 'label.domain'
					},
					productionPool : {
						label : '生产池',
						checkbox : true
					},
					testingPool : {
						label : '测试池',
						checkbox : true
					},
					save : {
						label : 'label.save',
						button : true,
						action : function(dataItem, tr) {
							saveResourcePoolPermission(dataItem, tr);
						}
					}
				},

				dataProvider : function(args) {
					listresponse(args, accountOwnerType);
				}
			}
		},
		domains : {
			type : 'select',
			title : 'label.menu.domains',
			listView : {
				firstClick : false,
				id : 'domains',
				ownerType : {},
				filters : false,
				fields : {
					domainName : {
						label : 'label.name'
					},
					domainId : {
						label : 'ID'
					},
					domainPath : {
						label : 'label.full.path'
					},
					productionPool : {
						label : '生产池',
						checkbox : true
					},
					testingPool : {
						label : '测试池',
						checkbox : true
					},
					save : {
						label : 'label.save',
						button : true,
						action : function(dataItem, tr) {
							saveResourcePoolPermission(dataItem, tr);
						}
					}
				},

				dataProvider : function(args) {
					listresponse(args, domainOwnerType);
				}
			}
		},
		projects : {
			type : 'select',
			title : 'label.projects',
			listView : {
				id : 'projects',
				firstClick : false,
				ownerType : {},
				filters : false,
				fields : {
					projectName : {
						label : 'label.name'
					},
					projectDisplayText : {
						label : 'label.display.name'
					},
					projectDomain : {
						label : 'label.domain'
					},
					projectAccount : {
						label : 'label.owner.account'
					},
					productionPool : {
						label : '生产池',
						checkbox : true
					},
					testingPool : {
						label : '测试池',
						checkbox : true
					},
					save : {
						label : 'label.save',
						button : true,
						action : function(dataItem, tr) {
							saveResourcePoolPermission(dataItem, tr);
						}
					}
				},

				dataProvider : function(args) {
					listresponse(args, projectOwnerType);
				}
			}
		}
	};

	cloudStack.sections.resourcePoolPermission = {
		title : '资源池分配',
		id : 'resourcePoolPermission',
		sectionSelect : {
			label : 'label.select-view'
		},
		sections : {}
	};

	var listresponse = function(args, ownerType) {
		var data = {
			cmsz : 'yes',
			page : args.page,
			pageSize : pageSize,
			listAll : true,
			ownerType : ownerType
		};

		$.ajax({
			url : createURL('listResourcePoolPermission'),
			data : data,
			success : function(json) {
				var item = json.listresponse.resourcepoolpermissionvoobj;
				args.response.success({
					data : item
				});
			}
		});
	};

	var saveResourcePoolPermission = function(dataItem, tr) {
		$.ajax({
			url : createURL('saveResourcePoolPermission'),
			data : {
				cmsz : 'yes',
				id : dataItem.id,
				ownerType : dataItem.ownerType,
				ownerId : dataItem.ownerId,
				ownerName : dataItem.ownerName,
				productionPool : tr.find("input[name='productionPool']").prop("checked"),
				testingPool : tr.find("input[name='testingPool']").prop("checked")
			},
			success : function(result) {
				var message = "";
				if (result) {
					message = "资源池分配成功！";
				} else {
					message = "资源池分配失败！";
				}
				cloudStack.dialog.window({
					message : message
				}, function() {
					cloudStack.dialog.hide();
				});
			}
		});
	};

	$.ajax({
		url : createURL("listResourcePoolRelatedObjectType"),
		dataType : "json",
		async : true,
		data : {
			cmsz : "yes"
		},
		success : function(json) {
			var item = json.listresponse.dbconfigobj;
			$.each(item, function(i) {
				if (item[i].key == "resourcepool_related_object_type.account") {
					cloudStack.sections.resourcePoolPermission.sections["account"] = resourcePoolPermission.accounts;
					accountOwnerType = item[i].value;
				} else if (item[i].key == "resourcepool_related_object_type.domain") {
					cloudStack.sections.resourcePoolPermission.sections["domain"] = resourcePoolPermission.domains;
					domainOwnerType = item[i].value;
				} else if (item[i].key == "resourcepool_related_object_type.project") {
					cloudStack.sections.resourcePoolPermission.sections["project"] = resourcePoolPermission.projects;
					projectOwnerType = item[i].value;
				}
			});
			resourcePoolPermission = null;
		}
	});

})(cloudStack, jQuery);
