package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.security.Role;
import com.hp.idc.resm.security.RolePermission;
import com.hp.idc.resm.service.CachedRoleService;
import com.hp.idc.resm.util.ICompareKeyGetter;
import com.hp.idc.resm.util.IntegerCompareHandler;
import com.hp.idc.resm.util.SortedArrayList;

/**
 * 存储角色权限缓存的库
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RolePermissionCacheStore extends
		UniqueIndexedCacheStore<RolePermission> {

	/**
	 * 生成模型的parentId
	 * 
	 * @author 梅园
	 * 
	 */
	public static class RolePermissionPriorityGetter implements
			ICompareKeyGetter<Integer, RolePermission> {

		public Integer getCompareKey(RolePermission obj) {
			int n = 0;
			if (CachedRoleService.roleCache != null) {
				Role r = CachedRoleService.roleCache.get(obj.getRoleId());
				if (r != null)
					n = r.getPriority() << 16;
			}
			return n | obj.getPriority();
		}
	}

	/**
	 * 优先级索引
	 */
	public SortedArrayList<Integer, RolePermission> priorityIndex = null;

	@Override
	public void initIndex() {
		RolePermissionPriorityGetter getter = new RolePermissionPriorityGetter();
		IntegerCompareHandler compare = new IntegerCompareHandler();
		this.priorityIndex = new SortedArrayList<Integer, RolePermission>(
				getter, compare, this.data.values());
	}

	@Override
	public RolePermission put(RolePermission obj) {
		RolePermission m = this.data.put(obj);
		if (m != null)
			this.priorityIndex.remove(m);
		this.priorityIndex.add(obj);
		return m;
	}

	@Override
	public RolePermission remove(String key) {
		RolePermission m = this.data.remove(key);
		if (m != null)
			this.priorityIndex.remove(m);
		return m;
	}

	@Override
	public void clear() {
		super.clear();
		this.priorityIndex.clear();
	}

	@Override
	public List<RolePermission> values() {
		return this.priorityIndex.values();
	}
}
