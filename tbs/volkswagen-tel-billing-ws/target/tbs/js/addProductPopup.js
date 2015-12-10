function addDefaultProductLineForISS(productLineList) {
		
	if($.inArray('HDD', productLineList) < 0){
		productLineList.push('HDD');
	}
	if($.inArray('Memory', productLineList) < 0){
		productLineList.push('Memory');
	}
	if($.inArray('Rack&Power', productLineList) < 0){
		productLineList.push('Rack&Power');
	}
	if($.inArray('OS', productLineList) < 0){
		productLineList.push('OS');
	}

	productLineList.sort();
	return productLineList;
}
function addProductDeptPopupOnChange() {
	//alert(obj.options[obj.selectedIndex].text);

	var selectedProductDept = $.trim($("#addPopupProductDept").val());
	//alert(selectedProductDept);
	$.ajax({
		type : "GET",
		url : "./rest/product/findCascade/dept/" + selectedProductDept,
		contentType : "application/json; charset=utf-8",
		data : {},
		dataType : "json",
		success : function(data) {
			var options = '';
			/*if(selectedProductDept.toLowerCase() === 'iss'){
        		data = Array.prototype.slice.call(data);
        		data = addDefaultProductLineForISS(data);
        	}*/
			for ( var i = 0; i < data.length; i++) {
				options = options + '<option>' + data[i] + '</option>';
			}
			$('#addPopupProductLine').empty().append(options);
			$('#addPopupProductLine').trigger('change');
		},
		cache : false
	});
}
function addProductLinePopupOnChange() {
	var selectedProductDept = $.trim($("#addPopupProductDept").val());
	var selectedProductLine = $.trim($("#addPopupProductLine").val());
	//alert(selectedProductLine);
	$.ajax({
		type : "GET",
		url : "./rest/product/findCascade/productline/" + selectedProductDept
				+ '_' + selectedProductLine,
		contentType : "application/json; charset=utf-8",
		data : {},
		dataType : "json",
		success : function(data) {
			var options = '';
			for ( var i = 0; i < data.length; i++) {
				if(data[i] == "" || data[i].length < 1){
        			continue;
        		}
				options = options + '<option>' + data[i] + '</option>';
			}
			$('#addPopupProductType').empty().append(options);
		},
		cache : false
	});
}
function addProductTypeBtnClick() {
	var productType = $.trim($('#addPopupProductTypeName').val());
	//alert(productType);
	$('#addPopupProductType').prepend('<option>' + productType + '</option>');
	$("#addPopupProductType option:first").attr('selected', 'selected');
	$('#addProductTypeButtonEnable').hide();
	$('#addProductTypeButtonDisable').show();
	$('#addProductTypeButtonDisable').attr("disabled","disabled");
}

function addProductTypeInputOnchange(obj){
	if($(obj).val() != "" && $(obj).val().length > 0){
		$('#addProductTypeButtonDisable').hide();
		$('#addProductTypeButtonDisable').attr("disabled","disabled");
		$('#addProductTypeButtonEnable').show();
	}
}
		
function addProductTypeInputOnblur(obj){
	if($(obj).val() == "" || $(obj).val().length < 1){
		$('#addProductTypeButtonEnable').hide();
		$('#addProductTypeButtonDisable').show();
		$('#addProductTypeButtonDisable').attr("disabled","disabled");
	}
}
		
function addProductPopup(width, height, title) {
	var divObject;
	//append the div elements into body
	var divStr = "<div id='divPopup' style='width:390px;'></div>";
	if ($("#divPopup").length > 0) {
		$("#divPopup").remove();
	}
	$("body").append(divStr);
	divObject = $("#divPopup");

	var htmlStr = '<div  class="main_div"><div style="border-bottom: rgb(185, 185, 187) 1px solid;width:370px;margin-top:-8px;margin-bottom:10px;"></div>'
			+ '<table width="390px" cellPadding="10px" style="margin-left:-10px;">'
			+ '<tr><td align="right"><label style="color:red;">*</label>部门:</td>'
			+ '<td><select id="addPopupProductDept" style="width:240px;" onchange="addProductDeptPopupOnChange();"><option>iss</option><option>bcs</option><option>hpsd</option><option>ts</option></select></td></tr>'
			+ '<tr><td align="right"><label style="color:red;">*</label>产品线:</td>'
			+ '<td><select id="addPopupProductLine" style="width:240px;" onchange="addProductLinePopupOnChange();"></select></td></tr>'
			+ '<tr><td align="right">产品型号:</td>'
			+ '<td><select id="addPopupProductType" style="width:240px;"></select></td></tr>'
			+ '<tr><td></td><td><input id="addPopupProductTypeName" type="text" style="width:139px;" onkeyup="addProductTypeInputOnchange(this);" onblur="addProductTypeInputOnblur(this);"><input id="addProductTypeButtonEnable" type="button" value="添加" class="enablesubmit" onclick="addProductTypeBtnClick();" style="display:none;"><input id="addProductTypeButtonDisable" type="button" value="添加" class="cancelButton" onclick="addProductTypeBtnClick();" disabled="disabled"></td></tr>'
			+ '<tr><td align="right">产品编号:</td><td><input id="addPopupProductName" type="text" style="width:222px;"></td></tr>'
			+ '<tr><td align="right">产品说明:</td><td><input id="addPopupProductDesc" type="text" style="width:222px;"></td></tr>'
			+ '<tr><td align="right">产品链接:</td><td><input id="addPopupProductLink" type="text" style="width:222px;"></td></tr>'
			+ '<tr><td align="right">推荐价格:</td><td><input id="addPopupProductPrice" type="text" style="width:222px;"></td></tr>'
			+ '</table>'
			+ '<div style="margin-top: 20px;margin-left:6px;"><input id="add" type="button" value="添加" class="enablesubmit" onclick="addProductOnPopup();">'
			+ '&nbsp;&nbsp;&nbsp;<input id="add" type="button" value="取消" class="cancelButton" onclick="closeDiag();"></div>'
			+ '</div>';
	divObject.html(htmlStr);
	//set title
	divObject
			.attr(
					"title",
					title
							+ '<img src="images/CloseButton.png" style="margin-left:345px;margin-top: -30px;z-index:100;cursor: pointer;" onclick="closeDiag();"/>');

	divObject.dialog({
		show : 'fade',
		bgiframe : true,
		autoOpen : false,
		width : 400,
		height : 570,
		draggable : false,
		resizable : false,
		modal : true
	});
	divObject.dialog("open");
	$('#addPopupProductDept').trigger('change');
	//$('#addPopupProductDept').onchange();
}
function addProductOnPopup() {
	
	var dept = $.trim($("#addPopupProductDept").val());
	var line = $.trim($("#addPopupProductLine").val());
	var type = $.trim($("#addPopupProductType").val());
	
	var name = $.trim($("#addPopupProductName").val());
	var desc = $.trim($("#addPopupProductDesc").val());
	var link = $.trim($("#addPopupProductLink").val());
	var price = $.trim($("#addPopupProductPrice").val());

	if (line.length < 1 || type.length < 1) {
		return;
	}
	var obj = {
		id : 0,
		department : dept,
		productLine : line,
		productType : type,
		productName : name,
		description : desc,
		link : link,
		suggestedPrice : price
	};
	var id = 0;
	$.ajax({
		type : "POST",
		url : "./rest/product/save",
		contentType : "application/json; charset=utf-8",
		data : {
			save_param : JSON.stringify(obj)
		},
		dataType : "json",
		success : function(data) {
			//id = data['id'];
			$("#divPopup").dialog("close");
			if(dept.toLowerCase() === $.trim($("#inputProductDept").val()).toLowerCase()){
				$("#inputProductDept").trigger('change');
			}
		},
		cache : false
	});

	/*price = formatCurrency(price);
	if (price == null || price == 'null' || price == '') {
		price = "N/A";
	}*/

	//var htmlStr = '<tr style="height:33px;" onclick="onClickRow(this, 1);" onmouseover="isTableOut=false" onmouseout="isTableOut=true"><td class="product_name_td"><a target="_blank" href="'+$.trim($("#addPopupProductLink").val())+'">'+$.trim($("#addPopupProductName").val())+'</a></td><td align="center" width="100px">'+$.trim($("#addPopupProductDept").val())+'</td><td align="right" width="80px" class="product_edit_td"><div><input type="button" name="editProduct" id="'+id+'" value="编辑" class="editButton" onclick="editProductPopup(this);"></div></td></tr>';
/*	var htmlStr = '<tr style="height:34px;" onclick="onClickRow(this, 1);" onmouseover="isTableOut=false" onmouseout="isTableOut=true">'
			+ '<td align="center" class="product_dept_td">'
			+ dept
			+ '</td>'
			+ '<td class="product_line_td">'
			+ line
			+ '</td>'
			+ '<td class="product_type_td">'
			+ type
			+ '</td>'
			+ '<td class="product_name_td"><a target="_blank" href="'
			+ link
			+ '">'
			+ name
			+ '</a></td>'
			+ '<td class="product_price_td">'
			+ price
			+ '</td>'
			+ '<td align="right" width="90px" class="product_edit_td"><div><input type="button" id="'
			+ id
			+ '" name="editProduct" value="编辑" class="editButton" onclick="editProductPopup(this);"></div></td>'
			+ '</tr>';
	$("#productTable tbody").not('#selectDeptTable tbody').not(
			'#iconTable tbody').append(htmlStr);*/
	//$("#divPopup").dialog("close");
}
function closeDiag() {
	$("#divPopup").dialog("close");
}