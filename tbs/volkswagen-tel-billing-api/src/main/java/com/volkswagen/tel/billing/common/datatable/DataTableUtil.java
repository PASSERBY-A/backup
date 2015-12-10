package com.volkswagen.tel.billing.common.datatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class DataTableUtil {

	public static DataTableParam collectParameters(String isQuery,
			String vosetString) throws JSONException {
		DataTableParam param = new DataTableParam();
		param.setIsQuery(isQuery);

		StringBuilder sBuilder = new StringBuilder();
		JSONArray jsonArray = JSONArray.fromObject(sBuilder.append("[")
				.append(vosetString).append("]").toString());

		List<VOSet> voList = new ArrayList<VOSet>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonobject2 = jsonArray.getJSONObject(i);
			VOSet vs = new VOSet();
			vs.setKey(jsonobject2.getString("key"));
			vs.setValue(jsonobject2.getString("value"));
			voList.add(vs);
		}

		param.setVoList(voList);
		return param;
	}

	public static Map<String, String> convertVoSetToMap(List<VOSet> params) {
		Map<String, String> map = new HashMap<String, String>();
		for (VOSet param : params) {
			map.put(param.getKey(), param.getValue());
		}
		return map;
	}

}
