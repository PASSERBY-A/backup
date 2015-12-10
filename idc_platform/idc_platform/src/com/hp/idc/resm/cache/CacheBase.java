package com.hp.idc.resm.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.DbUtil;

/**
 * ����ʵ�ֵĻ��࣬���಻��ʵ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ����Ĳ�����������CacheableObject�̳�
 */
public abstract class CacheBase<T extends CacheableObject> {

	/**
	 * ������־����
	 */
	private static Logger logger = Logger.getLogger(CacheBase.class);

	/**
	 * �ṩ����Ŀ�
	 */
	protected CacheStore<T> cacheStore = null;

	/**
	 * ��д��
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * ����
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * д��
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * ����ʼ���������ʱ���ã���Ҫʱ����������������д�˺���
	 */
	protected void onInitEnd() {
		// ������������д�˺���
	}

	/**
	 * �����»������ʱ���ã���Ҫʱ����������������д�˺���
	 * 
	 * @param oldObject
	 *            �ɶ���
	 * @param object
	 *            �¶���
	 */
	protected void onUpdate(T oldObject, T object) {
		// ������������д�˺���
	}

	/**
	 * ����Ӷ���������ʱ���ã���Ҫʱ����������������д�˺���
	 * 
	 * @param object
	 *            ��ӵĶ���
	 */
	protected void onAdd(T object) {
		// ������������д�˺���
	}

	/**
	 * ��������ӻ�����ɾ��ʱ���ã���Ҫʱ����������������д�˺���
	 * 
	 * @param object
	 *            ��ӵĶ���
	 */
	protected void onRemove(T object) {
		// ������������д�˺���
	}

	/**
	 * �����µĶ�������������ʵ��
	 * 
	 * @param rs
	 *            ���ݿ����ݼ�
	 * 
	 * @return �µĶ���
	 * @throws Exception
	 *             ��������������ʱ����
	 */
	protected abstract T createNewObject(ResultSet rs) throws Exception;

	/**
	 * ��ȡ���������
	 * 
	 * @return ���������
	 */
	public abstract String getCacheName();

	/**
	 * �������ݴ洢
	 * 
	 * @return ���ݴ洢
	 */
	protected CacheStore<T> createStore() {
		return new UniqueIndexedCacheStore<T>();
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public CacheStore<T> getCacheStore() {
		return this.cacheStore;
	}

	/**
	 * ��ȡ��ʼ����sql���
	 * 
	 * @return ��ʼ����sql���
	 * @throws Exception
	 *             �����쳣ʱ����
	 */
	protected String getInitSql() throws Exception {
		T obj = createNewObject(null);
		return "select * from " + obj.getDatabaseTable();
	}

	/**
	 * ��ȡ���ݿ����ӳأ���Ҫ�ı�����ʱ����������������
	 * 
	 * @return ���ݿ����ӳ�
	 */
	protected DataSource getDataSource() {
		return DbUtil.getDataSource();
	}

	/**
	 * ��ʼ������
	 * 
	 * @throws Exception
	 *             ����ʱ�������쳣
	 */
	public void initCache() throws Exception {
		String sql = getInitSql();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CacheStore<T> data0 = createStore();
		List<T> arr = new ArrayList<T>();
		int count = 0;
		try {
			int ppp = 500;
			conn = getDataSource().getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.setFetchSize(ppp);
			long l0 = System.currentTimeMillis();
			long l1 = 0;
			while (rs.next()) {
				try {
					T obj = createNewObject(rs);
					if (obj != null)
						arr.add(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				count++;
				if (count % ppp == 0) {
					l1 = System.currentTimeMillis() - l0;
					if (l1 == 0)
						l1++;
					l0 = System.currentTimeMillis();
					CacheBase.logger.info("��ʼ�� " + getCacheName() + " ���棺�Ѷ�ȡ"
							+ count + "����¼��" + (ppp * 1000 / l1) + "��/��");
				}
				// data0.put(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.free(conn, stmt, rs);
		}
		data0.initData(arr);
		data0.initIndex();
		this.wlock.lock();
		this.cacheStore = data0;
		onInitEnd();
		CacheBase.logger.info("��ʼ�� " + getCacheName() + " ����ɹ���" + data0.size()
				+ "����¼��");
		CacheManager.registerCache(this);
		this.wlock.unlock();
	}

	/**
	 * ��ջ���
	 */
	public void clear() {
		this.wlock.lock();
		this.cacheStore.clear();
		this.wlock.unlock();
	}

	/**
	 * ���/����һ�������¼
	 * 
	 * @param obj
	 *            �������
	 */
	public void add(T obj) {
		add(obj, true);
	}

	/**
	 * ���/����һ�������¼
	 * 
	 * @param obj
	 *            �������
	 * @param sendMsg
	 *            �Ƿ���ͬ��������Ϣ
	 */
	public void add(T obj, boolean sendMsg) {
		this.wlock.lock();
		T oldObject = this.cacheStore.get(obj.getPrimaryKey());
		if (oldObject != null)
			onUpdate(oldObject, obj);
		else
			onAdd(obj);
		this.cacheStore.put(obj);
		obj.setCached(true);
		if (sendMsg) {
			ICacheMessagePublisher p = getMessagePublisher();
			if (p != null)
				p.publishCacheAddMessage(this.getCacheName(), obj);
		}
		this.wlock.unlock();
	}

	/**
	 * ��ȡ��Ϣ���������������������ؿ��Խ�����ͬ����Ϣ������ͬ�Ľ�����
	 * 
	 * @return ��Ϣ��������
	 */
	public ICacheMessagePublisher getMessagePublisher() {
		return ServiceManager.getMessageListener();
	}

	/**
	 * ɾ�������еĶ���
	 * 
	 * @param obj
	 *            �������
	 */
	public void remove(T obj) {
		remove(obj, true);
	}

	/**
	 * ɾ�������еĶ���
	 * 
	 * @param obj
	 *            �������
	 * @param sendMsg
	 *            �Ƿ���ͬ��������Ϣ
	 */
	public void remove(T obj, boolean sendMsg) {
		this.wlock.lock();
		this.cacheStore.remove(obj.getPrimaryKey());

		if (sendMsg) {
			ICacheMessagePublisher p = getMessagePublisher();
			if (p != null)
				p.publishCacheRemoveMessage(this.getCacheName(), obj);
		}
		this.wlock.unlock();
	}

	/**
	 * ��ȡһ���������<br/>
	 * ����ȡ���Ķ�����Ҫ�޸�ʱ��ʹ�ö����clone��������һ�������ٽ����޸ġ�
	 * 
	 * @param key
	 *            ����
	 * @return �������
	 */
	public T get(String key) {
		this.rlock.lock();
		T obj = this.cacheStore.get(key);
		this.rlock.unlock();
		return obj;
	}

	/**
	 * ��ȡ���ж���
	 * 
	 * @return ���ж�����б�
	 */
	public List<T> getAll() {
		this.rlock.lock();
		List<T> objs = this.cacheStore.values();
		this.rlock.unlock();
		return objs;
	}

	/**
	 * �ڿ���̨���������Ϣ
	 */
	public void dump() {
		this.cacheStore.dump();
	}
}
