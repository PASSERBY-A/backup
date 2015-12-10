package com.hp.idc.itsm.configure.fields;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

public class GridFieldInfo extends FieldInfo{
	
	/**
	 * 表格头
	 * 格式：json格式
	 * <code>{name:'列1',dataIndex:'column1',mapping:'column1',type:'string'}</code>
	 */
	protected List<JSONObject> headColumns = new ArrayList<JSONObject>();
	
	/**
	 * 表格是否可编译，如果等于true,则此表格可增加删除行，可编辑行内数据
	 */
	protected boolean editable = false;
	
	/**
	 * 表格每一列允许填写的值，当<tt>editable=true</tt>时有效，默认是文本框<br>
	 * 格式：<tt>columnValue[{column0:[statedValueType:"list",["1","男性"],["0","女性"]]}]</tt></br>
	 * List的第一组标识 值是以什么样式显示
	 * 		<code>list:下拉列表；boolean:真假值；people:人员下拉框</code>
	 * List的后续组是可选值
	 */
	protected JSONObject columnValue = new JSONObject();
	
	protected JSONArray gridValue = new JSONArray();
	
	public Object clone() {
		return new GridFieldInfo();
	}
	
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		GridFieldInfo info2 = (GridFieldInfo) info;
		info2.setHeadColumns(this.headColumns);
		info2.setEditable(this.editable);
		info2.setColumnValue(this.columnValue);
	}

	/**
	 * this have some problem for process
	 */
	public String getHtmlCode(String value) {
		StringBuffer retBuffer = new StringBuffer();
		retBuffer.append("<table class='embed2' border=0 cellspacing=1 width='100%'>");
		if (headColumns!=null && headColumns.size()>0){
			retBuffer.append("<tr>");
			for (int i = 0; i < headColumns.size(); i++) {
				JSONObject cfgJSON = (JSONObject)headColumns.get(i);
				try {
					retBuffer.append("<td class='c1' align='center'>" + cfgJSON.getString("name") + "</td>");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			retBuffer.append("</tr>");
		}
		
		if(value!=null && !value.equals("")){
			try {
				JSONArray ja = new JSONArray(value);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject rowValue = ja.getJSONObject(i);
					retBuffer.append("<tr align='center'>");
					for (int j = 0; j < headColumns.size(); j++) {
						JSONObject cfgJSON = (JSONObject)headColumns.get(j);
						String dataIndex = cfgJSON.getString("dataIndex");
						String cellValue = "";
						if (rowValue.has(dataIndex))
							cellValue = rowValue.getString(dataIndex);
						
						
						//把值根据配置的数据，格式化成描述
						if (columnValue.has(dataIndex)){
							JSONObject colVal = columnValue.getJSONObject(dataIndex);
							try {
								JSONArray sva = colVal
										.getJSONArray("statedValue");
								for (int svaIndex = 0; svaIndex < sva.length(); svaIndex++) {
									JSONObject jo = sva.getJSONObject(svaIndex);
									if (jo.getString("id").equals(cellValue)) {
										cellValue = jo.getString("name");
										break;
									}
								}
							} catch (JSONException e) {
							}
						}
						String str = StringUtil.escapeHtml(cellValue);
						retBuffer.append("<td>" + str + "</td>");
					}
					retBuffer.append("</tr>");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		retBuffer.append("</table>");
		return retBuffer.toString();
	}
	
	public String getRestoreCode(String value){
		if (value == null)
			value = "";
		return value;
	}
	
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		if (this.editable){
			for (Iterator cvIte = columnValue.keys();cvIte.hasNext();) {
				String colIndexName = (String)cvIte.next();
				try {
					JSONObject colVal = columnValue.getJSONObject(colIndexName);
					String statedValueType = colVal.getString("statedValueType");
					if (statedValueType.equals("list")){//列表选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data = "+sva.toString()+";\n");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.ComboBox({\n");
						sb.append("		store: new Ext.data.Store({\n");
						sb.append("			reader: new Ext.data.JsonReader({root: 'dataArray'},\n");
						sb.append("		    	['id','name']),\n");
						sb.append("		    data:{dataArray:\n");
						sb.append("		    	"+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data \n");
						sb.append("		    }\n");
						sb.append("		}),\n");
						sb.append("		valueField:'id',\n");
						sb.append("		displayField:'name',\n");
						sb.append("		typeAhead: true,\n");
						sb.append("		listWidth : 120,\n");
						sb.append("		forceSelection: true,\n");
						sb.append("		mode: 'local',\n");
						sb.append("		triggerAction: 'all',\n");
						sb.append("		emptyText:'请选择...',\n");
						sb.append("		selectOnFocus:true\n");
						sb.append("	});\n");
						
						//增加renderer方法
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value,p,record,rowIndex){");
						sb.append("	var ds = "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data;\n");
						sb.append("	var dsCount = ds.length;\n");
						sb.append("	for(var i = 0; i < dsCount; i++){\n");
						sb.append("		if(ds[i].id == value)\n");
						sb.append("			return ds[i].name;\n");
						sb.append("	}");
						sb.append("	return value;");
						sb.append("};");
					} else if (statedValueType.equals("boolean")){//boolean
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.Checkbox();\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value ? '是' : '否';\n");
						sb.append("};\n");
					} else if (statedValueType.equals("people")){//人员选择框
						
					} else if (statedValueType.equals("date")) {
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.DateField({format:'Y-m-d', allowBlank:false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						if (!this.readOnly) 
							sb.append("	return value ? value.dateFormat('Y-m-d') : '';\n");
						else 
							sb.append("return value;");
						sb.append("};\n");
					} else if (statedValueType.equals("select")){//选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						String params = sva.getJSONObject(1).toString();
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.SelectDialogField({hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', emptyText:'请选择...',allowBlank:false,forceSelection:true,selectUrl:'"+url+"',params:"+params+"});\n");	
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else if (statedValueType.equals("tree")) {
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TreeBox({singleMode:true, hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', viewLoader:new Ext.tree.FilterTreeLoader({dataUrl:'"+url+"'}),emptyText:'请选择...'});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else {//其他默认text框
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TextField({allowBlank: false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (this.editable && !this.readOnly)
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.EditorGridPanel({\n");
		else
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.GridPanel({ \n");
		sb.append("	height: 200,\n");
		sb.append("	clicksToEdit:1,\n");
		sb.append("	title:'" + StringUtil.escapeJavaScript(this.name) + "',\n");	
		sb.append("	layout:'fit',\n");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("	id:'" + Consts.FLD_PREFIX + getId() + "',\n");
		sb.append("	viewConfig: {forceFit:true},\n");
		sb.append("	collapsible: false,\n animCollapse: false,\n");
		sb.append("	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),\n");
		
		//生成cm
		sb.append("	cm: new Ext.grid.ColumnModel([\n");
		
		//供store里面的reader使用
		String readerFieldCfg = "";
		if (headColumns!=null && headColumns.size()>0){
			int index = 0;
			for (int i = 0; i < headColumns.size(); i++) {
				JSONObject cfgJSON = (JSONObject)headColumns.get(i);
				try {
					if (index>0){
						sb.append(",");
						readerFieldCfg +=",\n";
					}
					String dataIndex = cfgJSON.getString("dataIndex");
					sb.append("{header: '" + cfgJSON.getString("name") + "',\n");
					sb.append("dataIndex: '"+dataIndex+"'\n,");
					sb.append("renderer: "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"_render\n");
					if ((this.editable && !this.readOnly) && columnValue.has(dataIndex)){
						sb.append("	,editor : "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"\n");
					}
					sb.append("}");
					
					
					readerFieldCfg +="{name:'" + cfgJSON.getString("dataIndex")+ "'";
					if(cfgJSON.has("mapping"))
						readerFieldCfg += ",mapping:'" + cfgJSON.getString("mapping") + "'";
					readerFieldCfg +="}";
					index++;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append(" ]),\n");
		
		//生成数据源
		sb.append("	store: new Ext.data.JsonStore({\n");
		sb.append("		fields:	[\n"+readerFieldCfg + "\n],\n");
        sb.append("     data: [\n");
		if(gridValue!=null && gridValue.length()>0){			
			for (int i = 0; i < gridValue.length(); i++) {
				try {
					JSONObject rowValue = gridValue.getJSONObject(i);
					if (i > 0)
						sb.append(",");
					sb.append(rowValue.toString() + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append("    ]\n");
		sb.append("	})\n");
		
		//toolbar
		if (this.editable && !this.readOnly){
			sb.append("	,bbar:[\n");
			sb.append("{text:'增加',handler:function(){"+Consts.FLD_PREFIX + getId()+".appendValue([{}]);}},\n");
			sb.append("'-',\n");
			sb.append("{text:'删除',handler:function(){");
			sb.append("var row = "+Consts.FLD_PREFIX + getId()+".getSelectionModel().getSelected();\n");
			sb.append("		if (row != null)\n");
			sb.append(Consts.FLD_PREFIX + getId()+".getStore().remove(row);\n}}\n");
			sb.append("]\n");
		}
		sb.append("});\n");

		
		return sb.toString();
	}
	
	public String getFormCode2(){
		StringBuffer sb = new StringBuffer();
		if (this.editable){
			for (Iterator cvIte = columnValue.keys();cvIte.hasNext();) {
				String colIndexName = (String)cvIte.next();
				try {
					JSONObject colVal = columnValue.getJSONObject(colIndexName);
					String statedValueType = colVal.getString("statedValueType");
					if (statedValueType.equals("list")){//列表选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data = "+sva.toString()+";\n");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.ComboBox({\n");
						sb.append("		store: new Ext.data.Store({\n");
						sb.append("			reader: new Ext.data.JsonReader({root: 'dataArray'},\n");
						sb.append("		    	['id','name']),\n");
						sb.append("		    data:{dataArray:\n");
						sb.append("		    	"+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data \n");
						sb.append("		    }\n");
						sb.append("		}),\n");
						sb.append("		valueField:'id',\n");
						sb.append("		displayField:'name',\n");
						sb.append("		typeAhead: true,\n");
						sb.append("		listWidth : 120,\n");
						sb.append("		forceSelection: true,\n");
						sb.append("		mode: 'local',\n");
						sb.append("		triggerAction: 'all',\n");
						sb.append("		emptyText:'请选择...',\n");
						sb.append("		selectOnFocus:true\n");
						sb.append("	});\n");
						
						//增加renderer方法
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value,p,record,rowIndex){");
						sb.append("	var ds = "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data;\n");
						sb.append("	var dsCount = ds.length;\n");
						sb.append("	for(var i = 0; i < dsCount; i++){\n");
						sb.append("		if(ds[i].id == value)\n");
						sb.append("			return ds[i].name;\n");
						sb.append("	}");
						sb.append("	return value;");
						sb.append("};");
					} else if (statedValueType.equals("boolean")){//boolean
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.Checkbox();\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value ? '是' : '否';\n");
						sb.append("};\n");
					} else if (statedValueType.equals("select")){//选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						String params = sva.getJSONObject(1).toString();
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.SelectDialogField({hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', emptyText:'请选择...',allowBlank:false,forceSelection:true,selectUrl:'"+url+"',params:"+params+"});\n");	
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else if (statedValueType.equals("tree")) {
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TreeBox({singleMode:true, hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', viewLoader:new Ext.tree.FilterTreeLoader({dataUrl:'"+url+"'}),emptyText:'请选择...'});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else if (statedValueType.equals("date")) {
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.DateField({format:'Y-m-d', allowBlank:false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						if (!this.readOnly) 
							sb.append("	return value ? value.dateFormat('Y-m-d') : '';\n");
						else
							sb.append("return value.substring(0,10);");
						sb.append("};\n");
					} else {//其他默认text框
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TextField({allowBlank: false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (this.editable && !this.readOnly)
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.EditorGridPanel({\n");
		else
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.GridPanel({ \n");
		sb.append("	height: 200,\n");
		sb.append("	stripeRows: true,\n");
		sb.append("	clicksToEdit:1,\n");
		sb.append("	layout:'fit',\n");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("	id:'" + Consts.FLD_PREFIX + getId() + "',\n");
		sb.append("	viewConfig: {forceFit:true},\n");
		sb.append("	collapsible: false,\n animCollapse: false,\n");
		sb.append("	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),\n");
		
		//生成cm
		sb.append("	cm: new Ext.grid.ColumnModel([\n");
		
		//供store里面的reader使用
		String readerFieldCfg = "";
		if (headColumns!=null && headColumns.size()>0){
			int index = 0;
			for (int i = 0; i < headColumns.size(); i++) {
				JSONObject cfgJSON = (JSONObject)headColumns.get(i);
				try {
					if (index>0){
						sb.append(",");
						readerFieldCfg +=",\n";
					}
					String dataIndex = cfgJSON.getString("dataIndex");
					sb.append("{header: '" + cfgJSON.getString("name") + "',\n");
					sb.append("dataIndex: '"+dataIndex+"'\n,");
					sb.append("renderer: "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"_render\n");
					if ((this.editable && !this.readOnly) && columnValue.has(dataIndex)){
						sb.append("	,editor : "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"\n");
					}
					sb.append("}");
					
					
					readerFieldCfg +="{name:'" + cfgJSON.getString("dataIndex")+ "'";
					if(cfgJSON.has("mapping"))
						readerFieldCfg += ",mapping:'" + cfgJSON.getString("mapping") + "'";
					readerFieldCfg +="}";
					index++;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append(" ]),\n");
		
		//reset the column size by the wide
		sb.append(" listeners: {");
		sb.append("render: function(g){");
		sb.append("var view = g.getView(); var tw = view.cm.getTotalWidth();");
		int col = -1;
		int total = 0;
		for (int i = 0; i < headColumns.size(); i++) {
			JSONObject cfgJSON = (JSONObject)headColumns.get(i);
			try {
				int width = Integer.valueOf(cfgJSON.getString("width")==null?"-1":cfgJSON.getString("width"));
				if (width > 0) {
					total = total + width;
					sb.append("view.cm.setColumnWidth("+i+","+width+");");
				} else {
					col = i;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		if (col != -1) {
			sb.append("view.cm.setColumnWidth("+col+",tw-"+total+");");
		}
		sb.append("}");
		sb.append("},");
		
		//生成数据源
		sb.append("	store: new Ext.data.JsonStore({\n");
		sb.append("		fields:	[\n"+readerFieldCfg + "\n],\n");
        sb.append("     data: [\n");
		if(gridValue!=null && gridValue.length()>0){			
			for (int i = 0; i < gridValue.length(); i++) {
				try {
					JSONObject rowValue = gridValue.getJSONObject(i);
					if (i > 0)
						sb.append(",");
					sb.append(rowValue.toString() + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append("    ]\n");
		sb.append("	})\n");
		
		//toolbar
		if (this.editable && !this.readOnly){
			sb.append("	,bbar:[\n");
			sb.append("{text:'增加',handler:function(){"+ Consts.FLD_PREFIX + getId()+".appendValue([{}]);}},\n");
			sb.append("'-',\n");
			sb.append("{text:'删除',handler:function(){");
			sb.append("var row = "+ Consts.FLD_PREFIX + getId()+".getSelectionModel().getSelected();\n");
			sb.append("		if (row != null)\n");
			sb.append(Consts.FLD_PREFIX + getId()+".getStore().remove(row);\n}}\n");
			sb.append("]\n");
		}
		sb.append("});\n");

		sb.append("var " + getId()+" = new Ext.form.FieldSet({");
		sb.append("collapsible: true,autoHeight: true,collapsed: false,");
		sb.append("title: '"+StringUtil.escapeJavaScript(getName())+"',");
		sb.append("listeners: { afterlayout : function(){ ");
		sb.append("Ext.DomHelper.append("+getId()+".getEl(), '<div class=\"x-form-item x-form-label-nowrap\" tabindex=\"-1\"><label style=\"width:85px;padding:2px 0px 0px 7px;\">"+StringUtil.escapeJavaScript(getName())+":</label><div id=\"itsm-grid-table\" class=x-form-field-wrap></div></div>');");
		sb.append(Consts.FLD_PREFIX + getId()+".render('itsm-grid-table');");
		sb.append("}}})");
		return sb.toString();
	}
	
	public String getFormCode(boolean title, int height) {
		StringBuffer sb = new StringBuffer();
		if (this.editable){
			for (Iterator cvIte = columnValue.keys();cvIte.hasNext();) {
				String colIndexName = (String)cvIte.next();
				try {
					JSONObject colVal = columnValue.getJSONObject(colIndexName);
					String statedValueType = colVal.getString("statedValueType");
					if (statedValueType.equals("list")){//列表选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data = "+sva.toString()+";\n");
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.ComboBox({\n");
						sb.append("		store: new Ext.data.Store({\n");
						sb.append("			reader: new Ext.data.JsonReader({root: 'dataArray'},\n");
						sb.append("		    	['id','name']),\n");
						sb.append("		    data:{dataArray:\n");
						sb.append("		    	"+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data \n");
						sb.append("		    }\n");
						sb.append("		}),\n");
						sb.append("		valueField:'id',\n");
						sb.append("		displayField:'name',\n");
						sb.append("		typeAhead: true,\n");
						sb.append("		listWidth : 120,\n");
						sb.append("		forceSelection: true,\n");
						sb.append("		mode: 'local',\n");
						sb.append("		triggerAction: 'all',\n");
						sb.append("		emptyText:'请选择...',\n");
						sb.append("		selectOnFocus:true\n");
						sb.append("	});\n");
						
						//增加renderer方法
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value,p,record,rowIndex){");
						sb.append("	var ds = "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_data;\n");
						sb.append("	var dsCount = ds.length;\n");
						sb.append("	for(var i = 0; i < dsCount; i++){\n");
						sb.append("		if(ds[i].id == value)\n");
						sb.append("			return ds[i].name;\n");
						sb.append("	}");
						sb.append("	return value;");
						sb.append("};");
					} else if (statedValueType.equals("boolean")){//boolean
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.Checkbox();\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value ? '是' : '否';\n");
						sb.append("};\n");
					} else if (statedValueType.equals("date")) {
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.DateField({format:'Y-m-d', allowBlank:false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						if (!this.readOnly) 
							sb.append("	return value ? value.dateFormat('Y-m-d') : '';\n");
						else 
							sb.append("return value;");
						sb.append("};\n");
					} else if (statedValueType.equals("select")){//选择框
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						String params = sva.getJSONObject(1).toString();
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.SelectDialogField({hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', emptyText:'请选择...',allowBlank:false,forceSelection:true,selectUrl:'"+url+"',params:"+params+"});\n");	
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else if (statedValueType.equals("tree")) {
						JSONArray sva = colVal.getJSONArray("statedValue");
						String url = sva.getString(0);
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TreeBox({singleMode:true, hiddenName:'"+Consts.FLD_PREFIX + getId()+"_hiddenName', viewLoader:new Ext.tree.FilterTreeLoader({dataUrl:'"+url+"'}),emptyText:'请选择...'});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
						
					} else {//其他默认text框
						sb.append("var "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+" = new Ext.form.TextField({allowBlank: false});\n");
						sb.append("function "+Consts.FLD_PREFIX + getId()+"_"+ colIndexName+"_render(value){\n");
						sb.append("	return value;\n");
						sb.append("};\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (this.editable && !this.readOnly)
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.EditorGridPanel({\n");
		else
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.grid.GridPanel({ \n");
		sb.append("	height: "+height+",\n");
		sb.append("	clicksToEdit:1,\n");
		if (title) {
			sb.append("	title:'" + StringUtil.escapeJavaScript(this.name) + "',\n");	
		}
		sb.append("	layout:'fit',\n");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("	id:'" + Consts.FLD_PREFIX + getId() + "',\n");
		sb.append("	viewConfig: {forceFit:true},\n");
		sb.append("	collapsible: false,\n animCollapse: false,\n");
		sb.append("	selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),\n");
		
		//生成cm
		sb.append("	cm: new Ext.grid.ColumnModel([\n");
		
		//供store里面的reader使用
		String readerFieldCfg = "";
		if (headColumns!=null && headColumns.size()>0){
			int index = 0;
			for (int i = 0; i < headColumns.size(); i++) {
				JSONObject cfgJSON = (JSONObject)headColumns.get(i);
				try {
					if (index>0){
						sb.append(",");
						readerFieldCfg +=",\n";
					}
					String dataIndex = cfgJSON.getString("dataIndex");
					sb.append("{header: '" + cfgJSON.getString("name") + "',\n");
					sb.append("dataIndex: '"+dataIndex+"'\n,");
					sb.append("renderer: "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"_render\n");
					if ((this.editable && !this.readOnly) && columnValue.has(dataIndex)){
						sb.append("	,editor : "+Consts.FLD_PREFIX + getId()+"_"+ dataIndex+"\n");
					}
					sb.append("}");
					
					
					readerFieldCfg +="{name:'" + cfgJSON.getString("dataIndex")+ "'";
					if(cfgJSON.has("mapping"))
						readerFieldCfg += ",mapping:'" + cfgJSON.getString("mapping") + "'";
					readerFieldCfg +="}";
					index++;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append(" ]),\n");
		
		//生成数据源
		sb.append("	store: new Ext.data.JsonStore({\n");
		sb.append("		fields:	[\n"+readerFieldCfg + "\n],\n");
        sb.append("     data: [\n");
		if(gridValue!=null && gridValue.length()>0){			
			for (int i = 0; i < gridValue.length(); i++) {
				try {
					JSONObject rowValue = gridValue.getJSONObject(i);
					if (i > 0)
						sb.append(",");
					sb.append(rowValue.toString() + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append("    ]\n");
		sb.append("	})\n");
		
		//toolbar
		if (this.editable && !this.readOnly){
			sb.append("	,tbar:[\n");
			sb.append("{text:'增加',handler:function(){"+Consts.FLD_PREFIX + getId()+".appendValue([{}]);}},\n");
			sb.append("'-',\n");
			sb.append("{text:'删除',handler:function(){");
			sb.append("var row = "+Consts.FLD_PREFIX + getId()+".getSelectionModel().getSelected();\n");
			sb.append("		if (row != null)\n");
			sb.append(Consts.FLD_PREFIX + getId()+".getStore().remove(row);\n}}\n");
			sb.append("]\n");
		}
		sb.append("});\n");

		
		return sb.toString();
	}
	
	/**
	 * parse the clob from table
	 */
	public void parse() {
		super.parse();
		if (this.xmlDoc == null)
			return;
		Element root = this.xmlDoc.getRootElement();
		if ("true".equals(root.attributeValue("editable")))
			this.editable = true;
		
		List el = root.selectNodes("./headColumns/col");
		if (el != null){
			for (int i = 0; i < el.size(); i++){
				Element col = (Element)(el.get(i));
				Element cfg = (Element)col.selectSingleNode("./cfg");
				if (cfg!=null){
					try {
						JSONObject cfgJSON = new JSONObject(cfg.getText());
						this.headColumns.add(cfgJSON);
						Element statedValue = (Element)col.selectSingleNode("./statedValue");
						JSONObject sv = new JSONObject();
						if (statedValue!=null){
							String statedValueType = statedValue.attributeValue("type");
							if (statedValueType!=null && !statedValueType.equals("")){
								sv.put("statedValueType", statedValueType);
								sv.put("statedValue", new JSONArray(statedValue.getText()));
							}
							else
								sv.put("statedValueType", "string");
							this.columnValue.put(cfgJSON.getString("dataIndex"), sv);
						} else {
							sv.put("statedValueType", "string");
							this.columnValue.put(cfgJSON.getString("dataIndex"), sv);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}		
	}
	
	public List getAttributes() {
		List returnList = super.getAttributes();
		CheckboxFieldInfo field3 = new CheckboxFieldInfo();
		TextDataSource ds3 = new TextDataSource();
		ds3.load("OK=");
		field3.setDataSource(ds3);
		field3.setName("允许编辑");
		field3.setId("cs1");
		if (this.editable)
			field3.setValue("OK");
		returnList.add(field3);
		
		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("表格属性");
		field1.setId("headCfg");
		field1.setMutiline(true);
		if (this.xmlData != null){
			Element el = (Element)this.xmlDoc.getRootElement().selectSingleNode("./headColumns");
			if (el!=null)
			field1.setValue(el.asXML());
		}
		returnList.add(field1);
		
		return returnList;
	}
	
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = super.getXmlConfig(map);
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();
		String s1 = (String) map.get("fld_cs1_OK");
		if (s1 != null && s1.length() > 0)
			root.addAttribute("editable", "true");
		else
			root.addAttribute("editable", "false");

		String s3 = (String) map.get("fld_headCfg");
		if (s3 == null)
			s3 = "<headColumns/>";
		Element el = (Element) root.selectSingleNode("./headColumns");
		if (el == null) {
			Element headCol = XmlUtil.parseString(s3).getRootElement();
			root.add(headCol);
		} else {
			el.setText(s3);
		}

		return doc.asXML();
	}
	
	public String getFormValue() {
		return Consts.FLD_PREFIX + getId() + ".setValue("
				+ this.value + ");";
	}


	public List getHeadColumns() {
		return headColumns;
	}

	public void setHeadColumns(List headColumns) {
		this.headColumns = headColumns;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public JSONObject getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(JSONObject columnValue) {
		this.columnValue = columnValue;
	}

	public JSONArray getGridValue() {
		return gridValue;
	}

	public void setGridValue(JSONArray gridValue) {
		this.gridValue = gridValue;
	}
	public void setGridValue(String gridValue) {
		JSONArray ja = null;
		try {
			ja = new JSONArray(gridValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.gridValue = ja==null?new JSONArray():ja;
	}
}
