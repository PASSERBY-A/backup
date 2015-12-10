/**
 * 
 */
package com.hp.idc.resm.security;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.RoleUtil;

/**
 * ģ�Ͷ�����ص�Ȩ��
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class ModelPermission extends BasicPermission {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = -6950665885486693767L;

	/**
	 * ������ģ��id
	 */
	private List<String> modelIds = new ArrayList<String>();
	
	@Override
	public int valid(int userId, Object obj) throws Exception {
		if (RoleUtil.isSystemAdmin(userId))
			return PERM_ALLOW;
		if (!(obj instanceof String))
			throw new Exception("����Ĳ�������Ϊģ��id���������");
		String mid = (String)obj;
		for (String id1 : this.modelIds) {
			if (id1.equals(mid))
				return PERM_ALLOW;
		}
		return PERM_PASS; // �����б��У�����
	}
	
	@Override
	public<T> List<T> filter(int userId, List<T> list) throws Exception {
		if (RoleUtil.isSystemAdmin(userId))
			return super.filter(userId, list);
		List<T> ret = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (!(obj instanceof Model))
				throw new Exception("����Ĳ�������Ϊģ�Ͷ����б��������");
			int r = valid(userId, ((Model)obj).getId());
			if (r == PERM_DENY) {
				list.remove(i);
				i--;
			} else if (r == PERM_ALLOW) {
				ret.add(list.remove(i));
				i--;
			}
		}
		return ret;
	}
	
	@Override
	public void setParameter(String param) {
		this.modelIds.clear();
		String[] ids = param.split(",");
		for (String id1 : ids) {
			this.modelIds.add(id1);
		}
	}
	
	@Override
	public String getParamDesc() {
		StringBuilder sb = new StringBuilder();
		for (String id1 : this.modelIds) {
			Model m = ServiceManager.getModelService().getModelById(id1);
			if (m == null)
				continue;
			if (sb.length() > 0)
				sb.append(",");
			sb.append(m.getName());
		}
		return "���ģ�ͣ�" + sb.toString();
	}
}
