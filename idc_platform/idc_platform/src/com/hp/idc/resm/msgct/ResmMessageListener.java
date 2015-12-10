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
 * ������Ϣ���������͡�<br/>
 * ��bean.xml�ж�������ΪresmMessageListener��bean
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmMessageListener extends IDCMessageListener implements ICacheMessagePublisher {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger.getLogger(ResmMessageListener.class);

	/**
	 * ��Ϣ���Ľ�����Ϣ�����
	 */
	public final static String DEST = "resm";

	/**
	 * ������Ϣʱʹ������ģʽ
	 */
	public final static boolean TOPIC_MODE = true;

	/**
	 * ������Ϣʱʹ������ģʽ
	 */
	public final static String SERVER_ID = "resm_1";

	/**
	 * ������Ϣʱ�����Ͷ��壺ͬ����ϸ����Դ���󣬴�ʱ��������Ϣ����Ϊ����id
	 */
	public final static String TYPE_OBJECT_SYNC = "objectSync";

	/**
	 * ������Ϣʱ�����Ͷ��壺��ӻ���
	 */
	public final static String TYPE_CACHE_ADD = "cacheAdd";

	/**
	 * ������Ϣʱ�����Ͷ��壺ɾ������
	 */
	public final static String TYPE_CACHE_REMOVE = "cacheRemove";

	/**
	 * ������Ϣʱ��Ĭ��ѡ���init()ʱ��ʼ��
	 * 
	 * @see #init()
	 */
	private MessagePublishProperty defaultMessagePublishProperty = null;

	/**
	 * ��Ϣ���Ķ��󣬶�messageCenter bean������
	 */
	private IMessageCenter messageCenter = null;

	/**
	 * ��ʼ��������������Ϣ�м���������ӡ� �벻Ҫ�ֶ����ã�ϵͳ��bean��ʼ��ʱ���Զ����á�
	 */
	public void init() {
		// ������Ϣʱ��Ĭ������
		this.defaultMessagePublishProperty = new MessagePublishProperty();
		this.defaultMessagePublishProperty.setDeliveryPersistent(true); // �־û�
		this.defaultMessagePublishProperty.setTimeToLive(24 * 3600 * 1000); // 1
																			// ��
		this.defaultMessagePublishProperty.setUseAsyncSend(true); // �첽����


	}
	
	/**
	 * ��ʼ��Ϣ��������ServiceManagerע��������ʱ���ã���֤���������Ѿ���ʼ�����
	 */
	public void startListener() {
		// ���ù�������
		String filter = "(to='ALL' and id<>'" + ResmConfig.id + "') or to='"
				+ ResmConfig.id + "'";

		// ����Ϣ�м����������
		try {
			logger.info("����������Ϣ�м��...");
			this.messageCenter
					.addListener(ResmMessageListener.DEST,
							ResmMessageListener.TOPIC_MODE, this,
							ResmConfig.id, filter);
			logger.info("��Ϣ�м�������ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��������������Ϣ�м���������ӡ� �벻Ҫ�ֶ����ã�ϵͳ��bean��ʼ��ʱ���Զ����á�
	 */
	public void close() {
		// ����
	}

	/**
	 * ��Ĭ�ϵ������д���һ���µ����Զ������ڷ�����Ϣ
	 * 
	 * @return ���Զ���
	 * @see #publishMessage(String, String)
	 */
	private Properties createPublishProperties() {
		Properties p = new Properties();
		p.setProperty("id", ResmConfig.id);
		return p;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param type
	 *            ��Ϣ����
	 * @param message
	 *            ��Ϣ����
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
	 * ������Դ����ͬ����ϸ�����Ϣ
	 * 
	 * @param id
	 *            Ҫͬ������Դ����
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
			logger.debug("���յ�ͬ����Ϣ����ԴID=" + text.getText());
			if (id > 0)
				ServiceManager.getResourceUpdateService().syncResource(id);
		} else if (ResmMessageListener.TYPE_CACHE_ADD.equals(type)) {
			ObjectMessage object = (ObjectMessage) msg;
			String cacheName = msg.getStringProperty("cache");
			CacheableObject obj = (CacheableObject) object.getObject();
			logger.debug("���յ�����������Ϣ��" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.addCache(cacheName, obj);
		} else if (ResmMessageListener.TYPE_CACHE_REMOVE.equals(type)) {
			ObjectMessage object = (ObjectMessage) msg;
			String cacheName = msg.getStringProperty("cache");
			CacheableObject obj = (CacheableObject) object.getObject();
			logger.debug("���յ�����ɾ����Ϣ��" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.removeCache(cacheName, obj);
		} else {
			System.out.println(msg.toString());
		}
	}

	/**
	 * ������Ϣ���Ķ�����bean��ʼ����ʱ���Զ�����
	 * 
	 * @param messageCenter
	 *            ��Ϣ���Ķ���
	 */
	public void setMessageCenter(IMessageCenter messageCenter) {
		this.messageCenter = messageCenter;
	}

	/**
	 * ��ȡ��Ϣ���Ķ���
	 * 
	 * @return ��Ϣ���Ķ���
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
			logger.debug("���յ�ͬ����Ϣ����ԴID=" + text);
			if (id > 0)
				ServiceManager.getResourceUpdateService().syncResource(id);
		} else if (ResmMessageListener.TYPE_CACHE_ADD.equals(type)) {
			String cacheName = (String) m.get("cache");
			Object object = m.get("object");
			CacheableObject obj = (CacheableObject) object;
			logger.debug("���յ�����������Ϣ��" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.addCache(cacheName, obj);
		} else if (ResmMessageListener.TYPE_CACHE_REMOVE.equals(type)) {
			String cacheName = (String) m.get("cache");
			Object object = m.get("object");
			CacheableObject obj = (CacheableObject) object;
			logger.debug("���յ�����ɾ����Ϣ��" + cacheName + " " + obj.getClass().getName());
			obj.onDeserialize();
			obj.dump();
			CacheManager.removeCache(cacheName, obj);
		} else {
			System.out.println(m.get("type"));
		}
		
	}

}
