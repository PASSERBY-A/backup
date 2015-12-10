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
	cloudStack.uiCustom.resourceStructure = function(args) {
		var listView = args.listView;

		return function(args) {
			var context = args.context;

			var vmList = function(args) {
				// Create a listing of instances, based on limited information
				// from main instances list view
				var $listView;
				var instances = $.extend(true, {}, args.listView, {
					context : context,
					uiCustom : true
				});

				$listView = $('<div>').listView(instances);

				// Change action label
				$listView.find('th.actions').html(_l('label.select'));

				return $listView;
			};

			var $dataList = vmList({
				listView : listView
			}).dialog({
				dialogClass : 'multi-edit-add-list panel',
				width : 825,
				title : '查看资源关系',
				buttons : [ {
					text : _l('label.close'),
					'class' : 'cancel ui-button',
					click : function() {
						$dataList.fadeOut(function() {
							$dataList.remove();
						});
						$('div.overlay').fadeOut(function() {
							$('div.overlay').remove();
							$(':ui-dialog').dialog('destroy');
						});
					}
				} ]
			}).parent('.ui-dialog').addClass('create-form').overlay();

		};
	};
}(cloudStack, jQuery));
