package com.hp.idc.resm.msgct;

import java.util.Map;
import java.util.Properties;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.hp.idc.msgct.IDCMessageListener;
import com.hp.idc.msgct.IMessageCenter;
import com.hp.idc.msgct.MessagePublishProperty;
import com.hp.idc.resm.cache.CacheManager;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.cache.ICacheMessagePublisher;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ResmConfig;
import com.hp.idc.resm.util.StringUtil;

/**
 * 处理消息监听、发送。<br/>
 * 在bean.xml中定义了名为resmMessageListener的bean
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmMessageListener extends IDCMessageListener implements ICacheMessagePublisher {
	/**
	 * log4j日志
	 */
	private static Logger logger = Logger.getLogger(ResmMessageListener.class);

	/**
	 * 消息中心接收消息的类别
	 */
	public final static String DEST = "resm";

	/**
	 * 发送消息时使用主题模式
	 */
	public final static boolean TOPIC_MODE = true;

	/**
	 * 发送消息时使用主题模式
	 */
	public final static String SERVER_ID = "resm_1";

	/**
	 * 发送消息时的类型定义：同步明细表资源对象，此时，设置消息内容为对象id
	 */
	public final static String TYPE_OBJECT_SYNC = "objectSync";

	/**
	 * 发送消息时的类型定义：添加缓存
	 */
	public final static String TYPE_CACHE_ADD = "cacheAdd";

	/**
	 * 发送消息时的类型定义：删除缓存
	 */
	public final static String TYPE_CACHE_REMOVE = "cacheRemove";

	/**
	 * 发送消息时的默认选项，在init()时初始化
	 * 
	 * @see #init()
	 */
	private MessagePublishProperty defaultMessagePublishProperty = null;

	/**
	 * 消息中心对象，对messageCenter bean的引用
	 */
	private IMessageCenter messageCenter = null;

	/**
	 * 初始化参数，并与消息中间件进行连接。 请不要手动调用，系统在bean初始化时会自动调用。
	 */
	public void init() {
		// 发送消息时的默认属性
		this.defaultMessagePublishProperty = new MessagePublishProperty();
		this.defaultMessagePublishProperty.setDeliveryPersistent(true); // 持久化
		this.defaultMessagePublishProperty.setTimeToLive(24 * 3600 * 1000); // 1
																			// 天
		this.defaultMessagePublishProperty.setUseAsyncSend(true); // 异步发送


	}
	
	/**
	 * 开始消息监听，在ServiceManager注入服务对象时调用，保证其他服务已经初始化完成
	 */
	public void startListener() {
		// 设置过滤条件
		String filter = "(to='ALL' and id<>'" + ResmConfig.id + "') or to='"
				+ ResmConfig.id + "'";

		// 与消息中间件进行连接
		try {
			logger.info("正在连接消息中间件...");
			this.messageCenter
					.addListener(ResmMessageListener.DEST,
							ResmMessageListener.TOPIC_MODE, this,
							ResmConfig.id, filter);
			logger.info("消息中间件监听成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化参数，并与消息中间件进行连接。 请不要手动调用，系统在bean初始化时会自动调用。
	 */
	public void close() {
		// 留空
	}

	/**
	 * 从默认的属性中创建一个新的属性对象，用于发送消息
	 * 
	 * @return 属性对象
	 * @see #publishMessage(String, String)
	 */
	private Properties createPublishProperties() {
		Properties p = new Properties();
		p.setProperty("id", ResmConfig.id);
		return p;
	}

	/**
	 * 发布消息
	 * 
	 * @param type
	 *            消息类型
	 * @param message
	 *            消息内容
	 */
	public void publishMessage(String type, String message) {
		try {
			Properties p = createPublishProperties();
			p.setProperty("to", "ALL");
			p.setProperty("type", type);
			this.messageCenter.publishTextMessage(ResmMessageListener.DEST,
					ResmMessageListener.TOPIC_MODE, message,
					this.defaultMessagePublishProperty, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publishCacheAddMessage(String cacheName, CacheableObject object) {
		try {
			Properties p = createPublishProperties();
			p.setProperty("to", "ALL");
			p.setProperty("type", TYPE_CACHE_ADD);
			p.setProperty("cache", cacheName);
			this.messageCenter.publishObjectMessage(ResmMessageListener.DEST,
					ResmMessageListener.TOPIC_MODE, object,
					this.defaultMessagePublishProperty, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publishCacheRemoveMessage(String cacheName,
			CacheableObject object) {
		try {
			Properties p = createPublishProperties();
			p.setProperty("to", "ALL");
			p.setProperty("type", TYPE_CACHE_REMOVE);
			p.setProperty("cache", cacheName);
			this.messageCenter.publishObjectMessage(ResmMessageListener.DEST,
					ResmMessageListener.TOPIC_MODE, object,
					this.defaultMessagePublishProperty, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发布资源对象同步明细表的消息
	 * 
	 * @param id
	 *            要同步的资源对象
	 */
	public void publishObjectSyncMessage(int id) {
		try {
			Properties p = createPublishProperties();
			p.setProperty("to", ResmMessageListener.SERVER_ID);
			p.setProperty("type", ResmMessageListener.TYPE_OBJECT_SYNC);
			this.messageCenter.publishTextMessage(ResmMessageListener.DEST,
					ResmMessageListener.TOPIC_MODE, "" + id,
					this.defaultMessagePublishProperty, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleExcetpiton(Exception arg0) {
		arg0.printStackTrace();
	}

	@Override
	public void onMessageDo(Message msg) throws Exception {
		String type = msg.getStringProperty("type");
		if (ResmMessageListener.TYPE_OBJECT_SYNC.equals(type)) {
			TextMessage text = (TextMessage) msg;
			int id = StringUtil.parseInt(text.getText(), -1);
			logger.debug("接收到同步消息：资源ID=" + text.getText());
			if (id > 0)
				ServiceManager.getResourceUpdateService().syncResource(id);
		} else if (ResmMessageListener.TYPE_CACHE_ADD.equals(type)) {
			ObjectMessage object = (ObjectMessage) msg;
			String cacheName = msg.getStringProperty("cache");
			CacheableObject obj = (CacheableObject) object.getObject();
			logger.debug("接收到缓存增加消息：" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.addCache(cacheName, obj);
		} else if (ResmMessageListener.TYPE_CACHE_REMOVE.equals(type)) {
			ObjectMessage object = (ObjectMessage) msg;
			String cacheName = msg.getStringProperty("cache");
			CacheableObject obj = (CacheableObject) object.getObject();
			logger.debug("接收到缓存删除消息：" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.removeCache(cacheName, obj);
		} else {
			System.out.println(msg.toString());
		}
	}

	/**
	 * 设置消息中心对象，在bean初始化的时候自动设置
	 * 
	 * @param messageCenter
	 *            消息中心对象
	 */
	public void setMessageCenter(IMessageCenter messageCenter) {
		this.messageCenter = messageCenter;
	}

	/**
	 * 获取消息中心对象
	 * 
	 * @return 消息中心对象
	 */
	public IMessageCenter getMessageCenter() {
		return this.messageCenter;
	}

	@Override
	public void onMessageDo(Map<String, Object> m) throws Exception {
		String type = (String) m.get("type");
		if (ResmMessageListener.TYPE_OBJECT_SYNC.equals(type)) {
			Object text = m.get("id");
			int id = StringUtil.parseInt(text.toString(), -1);
			logger.debug("接收到同步消息：资源ID=" + text);
			if (id > 0)
				ServiceManager.getResourceUpdateService().syncResource(id);
		} else if (ResmMessageListener.TYPE_CACHE_ADD.equals(type)) {
			String cacheName = (String) m.get("cache");
			Object object = m.get("object");
			CacheableObject obj = (CacheableObject) object;
			logger.debug("接收到缓存增加消息：" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.addCache(cacheName, obj);
		} else if (ResmMessageListener.TYPE_CACHE_REMOVE.equals(type)) {
			String cacheName = (String) m.get("cache");
			Object object = m.get("object");
			CacheableObject obj = (CacheableObject) object;
			logger.debug("接收到缓存删除消息：" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.removeCache(cacheName, obj);
		} else {
			System.out.println(m.get("type"));
		}
		
	}

}
