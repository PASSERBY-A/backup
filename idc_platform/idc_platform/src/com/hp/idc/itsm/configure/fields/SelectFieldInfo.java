package com.hp.idc.itsm.configure.fields;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.datasource.RemoteDataSource;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 下拉选择框
 *  @version 新增了url远程数据读取的配置。
 * @author 李会争
 * 
 */
public class SelectFieldInfo extends FieldInfoWithDataSource {
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.fields.FieldInfoWithDataSource#clone()
	 */
	public Object clone() {
		return new SelectFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
			" = new Ext.form.ComboBox({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("hiddenName:'" + Consts.FLD_PREFIX + getId() + "',");
		
		if (this.dataSourceClass.equals(RemoteDataSource.class.getName())){
			sb.append("store: new Ext.data.Store({");
			sb.append("    proxy: new Ext.data.HttpProxy({");
			sb.append("        url: '" + this.dataSourceData + "'");
			sb.append("    }),");
			sb.append("    reader: new Ext.data.JsonReader({");
			sb.append("        root: 'records',");
			sb.append("        totalProperty: 'totalCount'");
			sb.append("    }, [");
			sb.append("        {name: 'id', mapping: 'id'},");
			sb.append("        {name: 'value', mapping: 'value'}");
			sb.append("    ]),");
			sb.append("		sortInfo:{field: 'value', direction: 'ASC'},");
			sb.append("    remoteSort: true");
			sb.append("  }),");	
			sb.append("mode: 'remote',");
		} else {
			sb.append("store: new Ext.data.SimpleStore({");
			sb.append("fields: ['id', 'value','alertMsg'],");
			sb.append("data : [");
			sb.append(this.dataSource.getData(this.filter, "list" ,"ID"));
			sb.append("]}),");
			sb.append("mode: 'local',");
		}
		sb.append("valueField: 'id',");
		sb.append("displayField:'value',");
		//sb.append("typeAhead: true,");
		sb.append("triggerAction: 'all',");
		sb.append("emptyText:'请选择...',");
		sb.append("selectOnFocus:true,");
		if (this.editable) {
			sb.append("editable:true,");
		} else {
			sb.append("editable:false,");
			sb.append("forceSelection: true,");
		}
		if (this.notNull)
			sb.append("allowBlank:false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width + ",");
		sb.append("listeners:{");
		sb.append("	select:function(c,r,i) {");
		sb.append("		var alertMsg = r.get(\"alertMsg\");");
		sb.append("		if (alertMsg && alertMsg!=''){alert(alertMsg);}");
		sb.append("	}");
		sb.append("}");
		sb.append(",msgTarget:'title'});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.fields.FieldInfoWithDataSource#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value){
		String ret = "";
		if (this.dataSource!=null)
			ret = this.dataSource.getDisplayText(value);
		return ret;
	}

}
