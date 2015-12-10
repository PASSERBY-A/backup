package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.security.Role;
import com.hp.idc.resm.service.CachedRoleService;
import com.hp.idc.resm.util.ICompareKeyGetter;
import com.hp.idc.resm.util.IntegerCompareHandler;
import com.hp.idc.resm.util.SortedArrayList;

/**
 * �洢��ɫ����Ŀ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RoleCacheStore extends UniqueIndexedCacheStore<Role> {

	/**
	 * ����ģ�͵�parentId
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class RolePriorityGetter implements
			ICompareKeyGetter<Integer, Role> {

		public Integer getCompareKey(Role obj) {
			return obj.getPriority();
		}

	}
	
	/**
	 * ���ȼ�����
	 */
	public SortedArrayList<Integer, Role> priorityIndex = null;

	@Override
	public void initIndex() {
		RolePriorityGetter getter = new RolePriorityGetter();
		IntegerCompareHandler compare = new IntegerCompareHandler();
		this.priorityIndex = new SortedArrayList<Integer, Role>(getter, compare,
				this.data.values());
	}

	@Override
	public Role put(Role obj) {
		Role m = this.data.put(obj);
		if (m != null)
			this.priorityIndex.remove(m);
		this.priorityIndex.add(obj);
		if (m != null && m.getPriority() != obj.getPriority()) { // �и��£���Ҫ���½�ɫȨ�޻���
			if (CachedRoleService.rolePermissionCache != null)
				CachedRoleService.rolePermissionCache.getCacheStore().initIndex();
		}
		return m;
	}

	@Override
	public Role remove(String key) {
		Role m = this.data.remove(key);
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
	public List<Role> values() {
		return this.priorityIndex.values();
	}
}
