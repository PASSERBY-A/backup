package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.Code;
import com.hp.idc.resm.model.Code.CodeIdGetter;
import com.hp.idc.resm.util.ICompareHandler;
import com.hp.idc.resm.util.MaxHeap;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;

/**
 * �洢���뻺��Ŀ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CodeCacheStore extends UniqueIndexedCacheStore<Code> {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger
			.getLogger(CodeCacheStore.class);
	
	/**
	 * ��������
	 */
	public SortedArrayList<String, Code> codeIndex = null;

	@Override
	public void initIndex() {
		CodeIdGetter getter = new CodeIdGetter();
		StringCompareHandler compare = new StringCompareHandler();
		MaxHeap<Code> m = new MaxHeap<Code>();

		List<Code> list = m.sort(this.data.values(), new CodeCompareHandler());
		List<Code> list2 = new ArrayList<Code>();
		for (Code c : list) {
			if (c.getParentOid() == -1) {
				list2.add(c);
			} else {
				Code p = this.data.get("" + c.getParentOid());
				if (p == null) {
					logger.error("�Ҳ�������, oid=" + c.getParentOid() + ", �������á�");
				} else {
					p.addChild(c);
				}
			}
		}
		this.codeIndex = new SortedArrayList<String, Code>(getter, compare,
				list2);
	}

	/**
	 * ��������Ƚ�
	 * @author ÷԰
	 *
	 */
	class CodeCompareHandler implements ICompareHandler<Code> {
		public int compare(Code a, Code b) {
			return a.getOrder() - b.getOrder();
		}
	}
	/**
	 * ��ȡid=key���б�
	 * 
	 * @param key
	 *            ����
	 * @return �����������б�
	 */
	public List<Code> getCodeList(String key) {
		MaxHeap<Code> m = new MaxHeap<Code>();
		return m.sort(this.codeIndex.getE(key), new CodeCompareHandler());
	}

	@Override
	public Code put(Code obj) {
		Code m = this.data.put(obj);
		if (m != null)
			this.codeIndex.remove(m);
		this.codeIndex.add(obj);
		return m;
	}

	@Override
	public Code remove(String key) {
		Code m = this.data.remove(key);
		if (m != null)
			this.codeIndex.remove(m);
		return m;
	}

	@Override
	public void clear() {
		super.clear();
		this.codeIndex.clear();
	}

}
