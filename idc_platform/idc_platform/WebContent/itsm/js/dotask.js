var notNullFields = new Array();
	var notNullFieldsDesc = new Array();
	function selectPerson(id) {
		var p = document.all['div_' + id];
		if (p != null)
		{
			p.style.display = 'block';
			p.src = p.src2;
		}
	}
	
	function addPerson(id, perid, pername,fm) {
		var p = document.forms[fm].all[id];
		if (p!=null) {
			var value = p.value;
			if (value!="") {
				var v = value.split(",");
				var found = false;
				for( var i = 0; i < v.length; i++) {
					if (v[i]==perid) {
						found = true;
						break;
					}
				}
				if (!found) {
					p.value += ","+perid;
					document.forms[fm].all["pers_"+id].value += ","+pername;
				}
			} else {
				p.value += perid;
				document.forms[fm].all["pers_"+id].value += pername;
			}
		}
	}
	
	function clearPerson(id,fm) {
		var p = document.forms[fm].all[id];
		if (p!=null) {
			p.value = "";
			document.forms[fm].all["pers_"+id].value = "";
		}
	}
	
	function addnotNull(fieldId,desc) {
		var index = notNullFields.length;
		notNullFields[index] = fieldId;
		notNullFieldsDesc[index] = desc;
	}
	
	function addUploadFile(spanId) {
		var s = document.all[spanId];
		var sc = s.getElementsByTagName("input");
		var inp = document.createElement("INPUT");
		inp.name=sc[0].name;
		inp.type="file";
		
		var br = document.createElement("br");
		s.appendChild(br);
		s.appendChild(inp);
	}
	
	function dealSubmit(formObj) {
		if (notNullFields.length!=0) {
			for (var i = 0; i < notNullFields.length; i++) {
				if (formObj[notNullFields[i]].value=="") {
					alert("[ "+notNullFieldsDesc[i]+" ]²»ÄÜÎª¿Õ!");
					formObj[notNullFields[i]].focus();
					return;
				}
			}
		}
		formObj.submit();
	}