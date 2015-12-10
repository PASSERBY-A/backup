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
 * 模型对象相关的权限
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class ModelPermission extends BasicPermission {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6950665885486693767L;

	/**
	 * 关联的模型id
	 */
	private List<String> modelIds = new ArrayList<String>();
	
	@Override
	public int valid(int userId, Object obj) throws Exception {
		if (RoleUtil.isSystemAdmin(userId))
			return PERM_ALLOW;
		if (!(obj instanceof String))
			throw new Exception("传入的参数必须为模型id，请检查代码");
		String mid = (String)obj;
		for (String id1 : this.modelIds) {
			if (id1.equals(mid))
				return PERM_ALLOW;
		}
		return PERM_PASS; // 不在列表中，忽略
	}
	
	@Override
	public<T> List<T> filter(int userId, List<T> list) throws Exception {
		if (RoleUtil.isSystemAdmin(userId))
			return super.filter(userId, list);
		List<T> ret = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (!(obj instanceof Model))
				throw new Exception("传入的参数必须为模型对象列表，请检查代码");
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
		return "相关模型：" + sb.toString();
	}
}
