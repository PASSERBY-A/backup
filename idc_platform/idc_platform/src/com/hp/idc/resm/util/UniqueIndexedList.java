package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ������б����Կ��ٵĽ��в���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <T>
 *            ģ����Ĳ�����ֵ������
 * 
 */
public class UniqueIndexedList<T extends CacheableObject> extends
		IndexedList<String, T> {

	/**
	 * ���ݴ洢
	 */
	private List<T> list = null;

	/**
	 * �ȽϽӿ�
	 * 
	 */
	private class CompareHandler implements ICompareHandler<T> {
		public int compare(T a, T b) {
			return StringUtil.compareChinese(a.getPrimaryKey(),
					b.getPrimaryKey());
		}
	}

	/**
	 * ���캯��
	 */
	public UniqueIndexedList() {
		super(null, new ChineseStringCompareHandler());
		this.list = new ArrayList<T>(10);
	}

	/**
	 * ���캯��
	 * 
	 * @param initList
	 *            ��ʼ�����б�
	 */
	@SuppressWarnings("synthetic-access")
	public UniqueIndexedList(List<T> initList) {
		super(null, new ChineseStringCompareHandler());
		MaxHeap<T> m = new MaxHeap<T>();
		this.list = m.sort(initList, new CompareHandler());
	}

	@Override
	public T getAt(int index) throws IndexOutOfBoundsException {
		return this.list.get(index);
	}

	/**
	 * ������ɾ������
	 * 
	 * @param id
	 *            ����
	 * @return ɾ���Ķ���
	 */
	public T remove(String id) {
		int index = 0;
		int start = 0;
		this.wlock.lock();
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, id);
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				T ret = this.list.remove(index);
				this.wlock.unlock();
				return ret; // �ҵ���
			}
		}
		this.wlock.unlock();
		return null;
	}

	@Override
	public T remove(T value) {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // ��ͬ
				T ret = this.list.remove(pos);
				this.wlock.unlock();
				return ret;
			}
		}
		this.wlock.unlock();
		return null;
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void add(T value) throws Exception {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // ��ͬ
				this.wlock.unlock();
				throw new Exception("�����ظ�");
			}
		}
		this.list.add(pos, value);
		this.wlock.unlock();
	}

	/**
	 * ��Ӷ��󣬲�����ԭ������ͬ������ֵ
	 * 
	 * @param value
	 *            ����
	 * @return ԭ������ͬ������ֵ��û��ʱ����null
	 */
	public T put(T value) {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // ��ͬ
				T ret = this.list.set(pos, value);
				this.wlock.unlock();
				return ret;
			}
		}
		this.list.add(pos, value);
		this.wlock.unlock();
		return null;
	}

	/**
	 * ����������ȡ����
	 * 
	 * @param id
	 *            ����
	 * @return �����Ҳ���ʱ����null
	 */
	public T get(String id) {
		int index = 0;
		int start = 0;
		this.rlock.lock();
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, id);
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				this.rlock.unlock();
				return this.list.get(index); // �ҵ���
			}
		}
		this.rlock.unlock();
		return null;
	}

	/**
	 * ��ȡһ���������б���Ӧ�÷��õ�λ��
	 * 
	 * @param value
	 *            ����
	 * @return λ�ã���0��ʼ
	 */
	private int getPosition(T value) {
		int index = 0;
		int start = 0;
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, value.getPrimaryKey());
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				return index; // �ҵ���
			}
		}
		if (r < 0) // û�ҵ�ʱ�����ݱȽϽ��λ������
			return index + 1;
		return index;
	}

	@Override
	public List<T> getE(String key) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getL(String key) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getLE(String key) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getG(String key) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getGE(String key) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getGL(String k1, String k2) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getGEL(String k1, String k2) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getGLE(String k1, String k2) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public List<T> getGELE(String k1, String k2) throws Exception {
		throw new Exception("��֧�ֵķ���");
	}

	@Override
	public void clear() {
		this.wlock.lock();
		this.list.clear();
		this.wlock.unlock();
	}

	@Override
	public List<T> values() {
		this.rlock.lock();
		List<T> ret = new ArrayList<T>(this.list.size());
		ret.addAll(this.list);
		this.rlock.unlock();
		return ret;
	}
}
