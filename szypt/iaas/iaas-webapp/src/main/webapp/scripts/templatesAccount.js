(function(cloudStack) {

    var domainObjs;
    var rootDomainId;

    cloudStack.sections.templatesAccounts = {
        title: 'label.templatesaccounts',
        id: 'templatesaccounts',
        sectionSelect: {
            label: 'Select View',
            preFilter: function() {
                return ['accounts'];
            }
        },
        sections: {
            templateAccounts: {
                type: 'select',
                id: 'templatesaccounts',
                title: 'label.accounts',
                listView: {
                    id: 'templatesaccounts',
                    fields: {
					
                        username: {
                            label: 'label.name'
                        },
						password:{
							label :'密码'
						},
                        accounttype: {
                            label: '类型'
                        }
                    },

                    actions: {
                        add: {
                            label: 'label.add.account',
                            preFilter: function(args) {
                                if (isAdmin())
                                    return true;
                                else
                                    return false;
                            },
                            messages: {
                                notification: function(args) {
                                    return 'label.add.account';
                                }
                            },

                            createForm: {
                                title: 'label.add.account',
                                desc: 'label.add.account',
                                fields: {
                                    username: {
                                        label: 'label.username',
                                        validation: {
                                            required: true
                                        },
                                        docID: 'helpTemplateAccountUsername'
                                    },
                                    password: {
                                        label: 'label.password',
                                        validation: {
                                            required: true
                                        },
                                        isPassword: true,
                                        id: 'password',
                                        docID: 'helpTemplateAccountUsername'
                                    },
                                    'password-confirm': {
                                        label: 'label.confirm.password',
                                        validation: {
                                            required: true,
                                            equalTo: '#password'
                                        },
                                        isPassword: true,
                                        docID: 'helpAccountConfirmPassword'
                                    },
                                    
                                    accounttype: {
                                        label: 'label.type',
                                        docID: 'helpAccountType',
                                        validation: {
                                            required: true
                                        },
                                        select: function(args) {
                                            var items = [];
                                            items.push({
                                                id: 0,
                                                description: "OS"
                                            }); //OS
                                            items.push({
                                                id: 1,
                                                description: "SFTP"
                                            }); //FTP
                                            args.response.success({
                                                data: items
                                            });
                                        }
                                    }
                                }
                            },

                            action: function(args) {
                                var data = {
                                    username: args.data.username,
                                };

                                var password = args.data.password;
                                if (md5Hashed) {
                                    password = $.md5(password);
                                }
                                $.extend(data, {
                                    password: password
                                });

                                var accountType = args.data.accounttype;
                                $.extend(data, {
                                    accounttype: accountType
                                });

                                $.ajax({
                                    url: createURL('createTemplateAccount'),
                                    type: "POST",
                                    data: data,
                                    success: function(json) {
                                        var item = json.createaccountresponse.account;
                                        args.response.success({
                                            data: item
                                        });
                                    },
                                    error: function(XMLHttpResponse) {
                                        args.response.error(parseXMLHttpResponse(XMLHttpResponse));
                                    }
                                });
                            },

                            notification: {
                                poll: function(args) {
                                    args.complete({
                                      //  actionFilter: accountActionfilter
                                    });
                                }
                            }
                        }
                    },

                    dataProvider: function(args) {
                        var data = {};
                        listViewDataProvider(args, data);

                        /*if ("domains" in args.context) {
                            $.extend(data, {
                                domainid: args.context.domains[0].id
                            });
                        }*/
						 $.extend(data, {
                                templateId: "templateId"//TODO
                        });

                        $.ajax({
                            url: createURL('listTemplateAccounts'),
                            data: data,
                            async: true,
                            success: function(json) {
                                var items = json.listaccountsresponse.account;
                                args.response.success({
                                    actionFilter: accountActionfilter,
                                    data: items
                                });
                            }
                        });
                    },

                    detailView: {
                        name: 'Account details',
                        isMaximized: false,
                        viewAll: {
                            path: 'templates.templatesaccounts',
                            label: 'label.users'
                        },

                        actions: {
                            edit: {
                                label: 'message.edit.account',
                                compactLabel: 'label.edit',
                                action: function(args) {
                                  
                                  
                                }
                            },
							
                            remove: {
                                label: 'label.action.delete.account',
                                messages: {
                                    confirm: function(args) {
                                        return 'message.delete.account';
                                    },
                                    notification: function(args) {
                                        return 'label.action.delete.account';
                                    }
                                },
                                action: function(args) {
                                    var data = {
                                        id: args.context.accounts[0].id
                                    };
                                    $.ajax({
                                        url: createURL('deleteAccount'),
                                        data: data,
                                        async: true,
                                        success: function(json) {
                                            var jid = json.deleteaccountresponse.jobid;
                                            args.response.success({
                                                _custom: {
                                                    jobId: jid,
                                                    getUpdatedItem: function(json) {
                                                        return {}; //nothing in this account needs to be updated, in fact, this whole account has being deleted
                                                    },
                                                    getActionFilter: function() {
                                                        return accountActionfilter;
                                                    }
                                                }
                                            });
                                        }
                                    });
                                },
                                notification: {
                                    poll: pollAsyncJobResult
                                }
                            }

                        },
                        
                        tabFilter: function(args) {
                        	var hiddenTabs = [];
                        	if(!isAdmin()) {
                        		hiddenTabs.push('settings');
                        	}                        	
                        	return hiddenTabs;
                        },

                        tabs: {
                            details: {
                                title: 'label.details',

                                fields: [{
                                    username: {
                                        label: 'label.name',
                                        isEditable: true,
                                        validation: {
                                            required: true
                                        }
                                    }
                                }, {
                                    id: {
                                        label: 'ID'
                                    },
                                    
                                    account: {
                                        label: 'label.account.name'
                                    },
                                    accounttype: {
                                        label: 'label.role',
                                        converter: function(args) {
                                            return cloudStack.converters.toRole(args);
                                        }
                                    },
                                   
                                    firstname: {
                                        label: 'label.first.name',
                                        isEditable: true,
                                        validation: {
                                            required: true
                                        }
                                    },
                                    lastname: {
                                        label: 'label.last.name',
                                        isEditable: true,
                                        validation: {
                                            required: true
                                        }
                                    }
									}],

                                dataProvider: function(args) {
                                    var data = {
                                        id: args.context.accounts[0].id
                                    };
									 $.ajax({
                                            url: createURL('listUsers'),
                                            data: {
                                                id: args.context.users[0].id
                                            },
                                            success: function(json) {
                                                args.response.success({
                                                    actionFilter: userActionfilter,
                                                    data: json.listusersresponse.user[0]
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

    var accountActionfilter = function(args) {
        var jsonObj = args.context.item;
        var allowedActions = [];

        if (jsonObj.state == 'Destroyed') return [];

        if (isAdmin() && jsonObj.isdefault == false)
            allowedActions.push("remove");

        if (isAdmin()) {
            allowedActions.push("edit"); //updating networkdomain is allowed on any account, including system-generated default admin account
            if (!(jsonObj.domain == "ROOT" && jsonObj.name == "admin" && jsonObj.accounttype == 1)) { //if not system-generated default admin account
                if (jsonObj.state == "enabled") {
                    allowedActions.push("disable");
                    allowedActions.push("lock");
                } else if (jsonObj.state == "disabled" || jsonObj.state == "locked") {
                    allowedActions.push("enable");
                }
                allowedActions.push("remove");
            }
            allowedActions.push("updateResourceCount");
        } else if (isDomainAdmin()) {
            allowedActions.push("updateResourceCount");
        }
        return allowedActions;
    }

})(cloudStack);
