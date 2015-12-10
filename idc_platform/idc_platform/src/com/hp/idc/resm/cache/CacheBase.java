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
 * 缓存实现的基类，此类不能实例化
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类的参数，必须由CacheableObject继承
 */
public abstract class CacheBase<T extends CacheableObject> {

	/**
	 * 定义日志变量
	 */
	private static Logger logger = Logger.getLogger(CacheBase.class);

	/**
	 * 提供缓存的库
	 */
	protected CacheStore<T> cacheStore = null;

	/**
	 * 读写锁
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 读锁
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * 写锁
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * 当初始化缓存完成时调用，需要时可以在派生类中重写此函数
	 */
	protected void onInitEnd() {
		// 在派生类中重写此函数
	}

	/**
	 * 当更新缓存对象时调用，需要时可以在派生类中重写此函数
	 * 
	 * @param oldObject
	 *            旧对象
	 * @param object
	 *            新对象
	 */
	protected void onUpdate(T oldObject, T object) {
		// 在派生类中重写此函数
	}

	/**
	 * 当添加对象至缓存时调用，需要时可以在派生类中重写此函数
	 * 
	 * @param object
	 *            添加的对象
	 */
	protected void onAdd(T object) {
		// 在派生类中重写此函数
	}

	/**
	 * 当将对象从缓存内删除时调用，需要时可以在派生类中重写此函数
	 * 
	 * @param object
	 *            添加的对象
	 */
	protected void onRemove(T object) {
		// 在派生类中重写此函数
	}

	/**
	 * 生成新的对象，在派生类中实现
	 * 
	 * @param rs
	 *            数据库数据集
	 * 
	 * @return 新的对象
	 * @throws Exception
	 *             创建对象发生错误时发生
	 */
	protected abstract T createNewObject(ResultSet rs) throws Exception;

	/**
	 * 获取缓存的名称
	 * 
	 * @return 缓存的名称
	 */
	public abstract String getCacheName();

	/**
	 * 创建数据存储
	 * 
	 * @return 数据存储
	 */
	protected CacheStore<T> createStore() {
		return new UniqueIndexedCacheStore<T>();
	}

	/**
	 * 获取缓存库对象
	 * 
	 * @return 缓存库对象
	 */
	public CacheStore<T> getCacheStore() {
		return this.cacheStore;
	}

	/**
	 * 获取初始化的sql语句
	 * 
	 * @return 初始化的sql语句
	 * @throws Exception
	 *             操作异常时发生
	 */
	protected String getInitSql() throws Exception {
		T obj = createNewObject(null);
		return "select * from " + obj.getDatabaseTable();
	}

	/**
	 * 获取数据库连接池，需要改变链接时，在派生类中重载
	 * 
	 * @return 数据库连接池
	 */
	protected DataSource getDataSource() {
		return DbUtil.getDataSource();
	}

	/**
	 * 初始化缓存
	 * 
	 * @throws Exception
	 *             操作时发生的异常
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
					CacheBase.logger.info("初始化 " + getCacheName() + " 缓存：已读取"
							+ count + "条记录，" + (ppp * 1000 / l1) + "条/秒");
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
		CacheBase.logger.info("初始化 " + getCacheName() + " 缓存成功：" + data0.size()
				+ "条记录。");
		CacheManager.registerCache(this);
		this.wlock.unlock();
	}

	/**
	 * 清空缓存
	 */
	public void clear() {
		this.wlock.lock();
		this.cacheStore.clear();
		this.wlock.unlock();
	}

	/**
	 * 添加/更新一条缓存记录
	 * 
	 * @param obj
	 *            缓存对象
	 */
	public void add(T obj) {
		add(obj, true);
	}

	/**
	 * 添加/更新一条缓存记录
	 * 
	 * @param obj
	 *            缓存对象
	 * @param sendMsg
	 *            是否发送同步缓存消息
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
	 * 获取消息发布对象，在派生类中重载可以将缓存同步消息发到不同的接收者
	 * 
	 * @return 消息发布对象
	 */
	public ICacheMessagePublisher getMessagePublisher() {
		return ServiceManager.getMessageListener();
	}

	/**
	 * 删除缓存中的对象
	 * 
	 * @param obj
	 *            缓存对象
	 */
	public void remove(T obj) {
		remove(obj, true);
	}

	/**
	 * 删除缓存中的对象
	 * 
	 * @param obj
	 *            缓存对象
	 * @param sendMsg
	 *            是否发送同步缓存消息
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
	 * 获取一个缓存对象。<br/>
	 * 当获取到的对象需要修改时，使用对象的clone方法复制一个对象再进行修改。
	 * 
	 * @param key
	 *            主键
	 * @return 缓存对象
	 */
	public T get(String key) {
		this.rlock.lock();
		T obj = this.cacheStore.get(key);
		this.rlock.unlock();
		return obj;
	}

	/**
	 * 获取所有对象
	 * 
	 * @return 所有对象的列表
	 */
	public List<T> getAll() {
		this.rlock.lock();
		List<T> objs = this.cacheStore.values();
		this.rlock.unlock();
		return objs;
	}

	/**
	 * 在控制台输出调试信息
	 */
	public void dump() {
		this.cacheStore.dump();
	}
}
