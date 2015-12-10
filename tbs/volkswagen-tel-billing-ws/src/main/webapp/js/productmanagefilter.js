//data structure is like: [ {"name":"HP-76","id":1,"link":"link2","department":"iss"},
                //          {"name":"HP-77","id":3,"link":"link1","department":"bcs"}]
var data;
var len;
function keepdata(obj,method) {
	if(method ==="onload"){
		data = obj;
	}else if(method === "add"){
		data.push(obj[0]);
	}else if(method === "update"){
		for ( var i = 0; i < data.length; i++) {
			if(data[i]['id'] == obj[0]['id']){
				
				data[i]['department']=obj[0]['department'];
				data[i]['descripton']=obj[0]['descripton'];
				data[i]['link']=obj[0]['link'];
				data[i]['productLine']=obj[0]['productLine'];
				data[i]['productName']=obj[0]['productName'];
				data[i]['productType']=obj[0]['productType'];
				data[i]['suggestedPrice']=obj[0]['suggestedPrice'];
				break;
			}
		}
	}
 	
 	len = data.length;
}

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
function filter_deptOnchange() {
	var selectedDept = $('#filter_dept select').val();
	
	var productLineList = new Array();
	//step 1:filter dropdown list
	for(var i =0;i<len;i++){
		if($.inArray(data[i]['productLine'], productLineList) < 0 && data[i]['productLine'].toLowerCase() !='all'){
			if(selectedDept ==='all'){
				productLineList.push(data[i]['productLine']);
			}else if(data[i]['department']===selectedDept){
				productLineList.push(data[i]['productLine']);
			}
		}
	}
	
	productLineList.sort();
	feedProductLineOption(productLineList);
	
}

function filter_productLineOnchange() {
	var selectedDept = $('#filter_dept select').val();
	var selectedProductLine = $('#filter_productLine select').val();
	
	var productTypeList = new Array();
	//step 1:filter dropdown list
	for(var i =0;i<len;i++){
		if($.inArray(data[i]['productType'], productTypeList) < 0 && data[i]['productType'].toLowerCase() !='all'){
			if(selectedDept === 'all'){
				if(selectedProductLine === 'all'){
					productTypeList.push(data[i]['productType']);
				}else if(data[i]['productLine']===selectedProductLine){
					productTypeList.push(data[i]['productType']);
				}
			}else if(data[i]['department']===selectedDept){
				if(selectedProductLine === 'all'){
					productTypeList.push(data[i]['productType']);
				}else if(data[i]['productLine']===selectedProductLine){
					productTypeList.push(data[i]['productType']);
				}
			}
		}
	}
	productTypeList.sort();
	feedProductTypeOption(productTypeList);
}

function filter_productTypeOnchange() {
	var selectedDept = $('#filter_dept select').val();
	var selectedProductLine = $('#filter_productLine select').val();
	var selectedProductType = $('#filter_productType select').val();
	
	var productNameList = new Array();
	//step 1:filter dropdown list
	for(var i =0;i<len;i++){
		if($.inArray(data[i]['productName'], productNameList) < 0 && data[i]['productName'].toLowerCase() !='all'){
			if(selectedDept === 'all'){
				if(selectedProductLine === 'all'){
					if(selectedProductType === 'all'){
						productNameList.push(data[i]['productName']);
					}else if(data[i]['productType']===selectedProductType){
						productNameList.push(data[i]['productName']);
					}
				}else if(data[i]['productLine']===selectedProductLine){
					if(selectedProductType === 'all'){
						productNameList.push(data[i]['productName']);
					}else if(data[i]['productType']===selectedProductType){
						productNameList.push(data[i]['productName']);
					}
				}
			}else if(data[i]['department']===selectedDept){
				if(selectedProductLine === 'all'){
					if(selectedProductType === 'all'){
						productNameList.push(data[i]['productName']);
					}else if(data[i]['productType']===selectedProductType){
						productNameList.push(data[i]['productName']);
					}
				}else if(data[i]['productLine']===selectedProductLine){
					if(selectedProductType === 'all'){
						productNameList.push(data[i]['productName']);
					}else if(data[i]['productType']===selectedProductType){
						productNameList.push(data[i]['productName']);
					}
				}
			}// end else if
		}// end if
	}// end for
	productNameList.sort();
	feedProductNameOption(productNameList);
	
}

function filter_productNameOnchange() {
	var selectedDept = $('#filter_dept select').val();
	var selectedProductLine = $('#filter_productLine select').val();
	var selectedProductType = $('#filter_productType select').val();
	var selectedProductName = $('#filter_productName select').val();
	var htmlProductTable ="";
	for(var i =0;i<len;i++){
		if(selectedDept === 'all'){
			if(selectedProductLine === 'all'){
				if(selectedProductType === 'all'){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}else if(data[i]['productType']===selectedProductType){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}
			}else if(data[i]['productLine']===selectedProductLine){
				if(selectedProductType === 'all'){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}else if(data[i]['productType']===selectedProductType){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}
			}
		}else if(data[i]['department'] === selectedDept){
			if(selectedProductLine === 'all'){
				if(selectedProductType === 'all'){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}else if(data[i]['productType']===selectedProductType){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}
			}else if(data[i]['productLine']===selectedProductLine){
				if(selectedProductType === 'all'){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}else if(data[i]['productType']===selectedProductType){
					if(selectedProductName === 'all'){
						htmlProductTable += appendToHtml(data[i]);
					}else if(data[i]['productName']===selectedProductName)
						htmlProductTable += appendToHtml(data[i]);
				}
			}
		}
	}
	$("#productTable tbody").not('#selectDeptTable tbody').not('#iconTable tbody').empty().append(htmlProductTable);
}

function appendToHtml(obj) {
	var price = formatCurrency(obj["suggestedPrice"]);
	if(price == null || price == 'null' || price == '' || price == "_blank_") {
		price = "N/A"; 
	}
	var htmlProductTable = '<tr style="height:34px;" onclick="onClickRow(this, 1);" onmouseover="isTableOut=false" onmouseout="isTableOut=true">' +
	'<td align="center" class="product_dept_td">'+(obj["department"]!="_blank_"?obj["department"]:"&nbsp;")+'</td>'+
	'<td class="product_line_td">'+(obj["productLine"]!="_blank_"?obj["productLine"]:"&nbsp;")+'</td>'+
	'<td class="product_type_td">'+(obj["productType"]!="_blank_"?obj["productType"]:"&nbsp;")+'</td>'+
	'<td class="product_name_td"><a target="_blank" href="'+obj["link"]+'">'+(obj["productName"]!="_blank_"?obj["productName"]:"&nbsp;")+'</a></td>'+
	'<td class="product_price_td">'+price+'</td>'+
	'<td align="right" width="90px" class="product_edit_td"><div><input type="button" id="'+obj["id"]+'" name="editProduct" value="编辑" class="editButton" onclick="editProductPopup(this);"></div><input type="hidden" id="desc" value="'+obj["description"]+'"></td>'+
	'</tr>';
	return htmlProductTable;
}

function feedProductLineOption(productLineList) {

	var len = productLineList.length;
	var options = '';
	for(var i=0;i<len;i++){
		options =options + '<option value="'+productLineList[i]+'">'+productLineList[i]+'</option>';
	}
	options = '<option value="all">产品线</option>' +options;
	$('#filter_productLine select').empty();
	$('#filter_productLine select').empty().append(options);
	$('#filter_productLine select').trigger("change");
}

function feedProductTypeOption(productTypeList) {
	var len = productTypeList.length;
	var options = '';
	for(var i=0;i<len;i++){
		options =options + '<option value="'+productTypeList[i]+'">'+productTypeList[i]+'</option>';
	}
	options = '<option value="all">产品型号</option>' +options;
	$('#filter_productType select').empty();
	$('#filter_productType select').empty().append(options);
	$('#filter_productType select').trigger("change");
}

function feedProductNameOption(productNameList) {
	var len = productNameList.length;
	var options = '';
	for(var i=0;i<len;i++){
		options =options + '<option value="'+productNameList[i]+'">'+productNameList[i]+'</option>';
	}
	options = '<option value="all">产品编号</option>' +options;
	$('#filter_productName select').empty();
	$('#filter_productName select').empty().append(options);
	$('#filter_productName select').trigger("change");
}
