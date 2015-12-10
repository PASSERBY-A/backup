/**
 * @file jQuery.Json.js
 * @description ����֧��Json���������ͻ�ת����չ����
 * @author knowmore
 * @date 2011-03-01
 * @license share
 * @version 1.0.20110301
 **/

/**
 * ��json�ַ�ת��Ϊ����ķ�����
 * 
 * @public
 * @param json�ַ�
 * @return ����object,array,string�ȶ���
 */
jQuery.extend({
	/**
	 * *
	 * 
	 * @see ��json�ַ�ת��Ϊ���� *
	 * @param json�ַ� *
	 * @return ����object,array,string�ȶ���
	 */
	evalJSON : function(strJson) {
		return eval("(" + strJson + ")");
	}
});

/**
 * ��javascript�������ת��Ϊjson�ַ�ķ�����
 * 
 * @public
 * @param {object}
 *            ��ת��Ϊjson�ַ�Ķ���, һ��ΪJson
 *            ��֧��object,array,string,function,number,boolean,regexp *��
 * @return ����json�ַ�
 */
jQuery.extend({
	toJSONString : function(object) {
		// updated by songsr,2012--6-27
		if (object === null)
			return 'null';
		// end
		var type = typeof object;
		if ('object' == type) {
			if (Array == object.constructor)
				type = 'array';
			else if (RegExp == object.constructor)
				type = 'regexp';
			else
				type = 'object';
		}
		switch (type) {
		case 'undefined':
		case 'unknown':
			return;
			break;
		case 'function':
		case 'boolean':
		case 'regexp':
			return object.toString();
			break;
		case 'number':
			return isFinite(object) ? object.toString() : 'null';
			break;
		case 'string':
			return '"'
					+ object.replace(/(\\|\")/g, "\\$1").replace(
							/\n|\r|\t/g,
							function() {
								var a = arguments[0];
								return (a == '\n') ? '\\n'
										: (a == '\r') ? '\\r'
												: (a == '\t') ? '\\t' : ""
							}) + '"';
			break;
		case 'object':
			if (object === null)
				return 'null';
			var results = [];
			for ( var property in object) {
				var value = jQuery.toJSONString(object[property]);
				if (value !== undefined)
					results.push(jQuery.toJSONString(property) + ':' + value);
			}
			return '{' + results.join(',') + '}';
			break;
		case 'array':
			var results = [];
			for ( var i = 0; i < object.length; i++) {
				var value = jQuery.toJSONString(object[i]);
				if (value !== undefined)
					results.push(value);
			}
			return '[' + results.join(',') + ']';
			break;
		}
	}
});
