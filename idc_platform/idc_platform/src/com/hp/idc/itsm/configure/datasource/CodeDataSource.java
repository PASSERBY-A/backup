package com.hp.idc.itsm.configure.datasource;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.CodeInfo;
import com.hp.idc.itsm.common.TreeObject;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ��ʾָ���������͵����д��������Դ
 * 
 * @author ÷԰
 * 
 */
public class CodeDataSource extends FieldDataSource {
	/**
	 * �洢��������
	 */
	protected int type;

	/**
	 * ��ȡ����Դ������
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @param style
	 *            ���ɵ�������ʽ
	 * @return ��������Դ������
	 */
	public String getData(String filter, String style) {
		return getData(filter,style,"ID");
	}
	
	public String getData(String filter, String style, String idType){
		StringBuffer sb = new StringBuffer();
		List list = getKeys(filter);
		if (style.equals("list")) {
			for (int i = 0; i < list.size(); i++) {
				CodeInfo info = CIManager.getCodeByOid(Integer
						.parseInt((String) list.get(i)));
				if (i > 0)
					sb.append(",");
				sb.append("[");
				if (idType != null && idType.equals("ID"))
					sb.append("\"" + info.getCodeId() + "\",\"" + info.getName()
						+ "\",\""+StringUtil.escapeJavaScript(info.getAlertMsg())+"\"");
				else
					sb.append("\"" + info.getOid() + "\",\"" + info.getName()
							+ "\",\""+StringUtil.escapeJavaScript(info.getAlertMsg())+"\"");
				sb.append("]");
			}
		} else /* if (style.equals("list")) */{
			for (int i = 0, count = 0; i < list.size(); i++) {
				CodeInfo info = CIManager.getCodeByOid(Integer
						.parseInt((String) list.get(i)));
				if (info.getParent() != null)
					continue;
				if (count++ > 0)
					sb.append(",");
				sb.append(info.getData(idType));
			}
		}
		return sb.toString();
	}

	/**
	 * ��������Դ��������Ϣ
	 * 
	 * @param data
	 *            ����Դ��������Ϣ
	 */
	public void load(String data) {
		this.type = Integer.parseInt(data);
	}

	/**
	 * ��ȡ����������������м�ֵ
	 * 
	 * @param filter
	 *            ���˱��ʽ
	 * @return ��������������������м�ֵ List<String>
	 */
	public List getKeys(String filter) {
		List list = CIManager.getCodesByTypeOid(this.type,false);
		List l2 = new ArrayList();
		getKeys2(list, l2);
		//ItsmUtil.filter(l2, "name", filter);
		List l3 = new ArrayList();
		for (int i = 0; i < l2.size(); i++) {
			l3.add("" + ((TreeObject) (l2.get(i))).getOid());
		}
		return l3;
	}

	/**
	 * �ݹ�������д���
	 * 
	 * @param list
	 *            �����б�
	 * @param ret
	 *            ���
	 */
	protected void getKeys2(List list, List ret) {
		for (int i = 0; i < list.size(); i++) {
			TreeObject t = (TreeObject) list.get(i);
			ret.add(t);
			getKeys2(t.getSubItems(), ret);
		}
	}

	/**
	 * ��ȡָ����ֵ����ʾ����
	 * 
	 * @param id
	 *            ��ֵ
	 * @return ����ָ����ֵ����ʾ����
	 */
	public String getDisplayText(String id) {
		return getDisplayText(id,"ID");
	}
	
	public String getDisplayText(String id,String idType) {
		CodeInfo info = null;
		if (idType.equals("ID")) {
			info = CIManager.getCodeById(id,this.type);
		} else {
			int oid = StringUtil.parseInt(id, -1);
			info = CIManager.getCodeByOid(oid);
		}
		if (info == null)
			return id;
		return info.getDisplayName();
	}
	
	
	public String getIdByText(String text){
		CodeInfo ci= CIManager.getCodeByText(text,this.type);
		if (ci != null) {
			return ci.getCodeId();
		}
		return "";
	}
	
}
