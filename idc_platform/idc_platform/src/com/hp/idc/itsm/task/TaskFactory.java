package com.hp.idc.itsm.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.ThreadPool;
import com.hp.idc.itsm.task.listener.TaskListener;
import com.hp.idc.itsm.workflow.ColumnFieldInfo;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

public class TaskFactory {
	
	private Log logger = LogFactory.getLog(getClass());
	public static final String ITSM = "ITSM";

	/**
	 * 监听器
	 */
	private static Map<String, List<TaskListener>> listenersMap = new HashMap<String, List<TaskListener>>();
	/**
	 * 监听器,这个数组里的监听器是主要的，如果处理出错会抛异常，下面的将全部无法执行
	 */
	private static Map<String, List<TaskListener>> listenersMapMaster = new HashMap<String, List<TaskListener>>();
	
	
	//private static Map<String,>
	//线程管理类
	private ThreadPool threadPool;

	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	/**
	 * 增加监听
	 */
	public void addListener(TaskListener _listener) {
		addListener(TaskListener.CATEGORY_DEFAULT, _listener);
	}

	/**
	 * 增加监听
	 * @param category
	 * @param _listener
	 */
	public void addListener(String category, TaskListener _listener) {
		if (_listener == null) {
			logger.warn("添加监听失败,传入对象为空");
			return;
		}
		if (category == null || category.equals(""))
			category = TaskListener.CATEGORY_DEFAULT;

		if(_listener.isThrowException()){
			synchronized (listenersMapMaster) {
				List<TaskListener> ll = listenersMapMaster.get(category);
				if (ll == null) {
					ll = new ArrayList<TaskListener>();
					listenersMapMaster.put(category, ll);
				}
				if (!exists(_listener,ll))
					ll.add(_listener);
				else {
					logger.warn("添加监听失败,ID已存在("+_listener.getId()+")");
				}
			}
		} else {
			synchronized (listenersMap) {
				List<TaskListener> ll = listenersMap.get(category);
				if (ll == null) {
					ll = new ArrayList<TaskListener>();
					listenersMap.put(category, ll);
				}
				if (!exists(_listener,ll))
					ll.add(_listener);
				else {
					logger.warn("添加监听失败,ID已存在("+_listener.getId()+")");
				}
			}
		}
	}

	/**
	 * 删除监听
	 */
	public void removeListener(TaskListener _listener) {
		if (_listener == null) {
			logger.warn("删除监听失败,传入对象为空");
			return;
		}
		removeListener(_listener.getId());
	}

	/**
	 * 删除监听
	 */
	public void removeListener(String listenerId) {
		if (listenerId == null) {
			logger.warn("删除监听失败,ID为空");
			return;
		}
		synchronized (listenersMapMaster) {
			Set<String> keys = listenersMapMaster.keySet();
			for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
				String key = ite.next();
				List<TaskListener> ll = listenersMapMaster.get(key);
				for (int i = 0; i < ll.size(); i++) {
					TaskListener listener = ll.get(i);
					if (listener != null && listener.getId().equals(listenerId)) {
						ll.remove(i);
						break;
					}
				}
			}
		}
		
		synchronized (listenersMap) {
			Set<String> keys = listenersMap.keySet();
			for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
				String key = ite.next();
				List<TaskListener> ll = listenersMap.get(key);
				for (int i = 0; i < ll.size(); i++) {
					TaskListener listener = ll.get(i);
					if (listener != null && listener.getId().equals(listenerId)) {
						ll.remove(i);
						break;
					}
				}
			}
		}
	}

	/**
	 * 获取所有监听
	 */
	public List<TaskListener> listeners() {
		List<TaskListener> ret = new ArrayList<TaskListener>();
		synchronized (listenersMapMaster) {
			if (listenersMapMaster != null && listenersMapMaster.size() > 0) {
				Set<String> keys = listenersMapMaster.keySet();
				for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
					String key = ite.next();
					ret.addAll(listenersMapMaster.get(key));
				}
			}
		}
		synchronized (listenersMap) {
			if (listenersMap != null && listenersMap.size() > 0) {
				Set<String> keys = listenersMap.keySet();
				for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
					String key = ite.next();
					ret.addAll(listenersMap.get(key));
				}
			}
		}
		return ret;
	}

	private boolean exists(TaskListener _listener, List<TaskListener> listeners) {
		boolean exists = false;
		for (int i = 0; i < listeners.size(); i++) {
			TaskListener listener = listeners.get(i);

			if (listener.getId().equals(_listener.getId())) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public void publishEvent(Event event, boolean isolated) throws Exception {
		publishEvent(TaskListener.CATEGORY_DEFAULT,event,isolated);
	}

	public void publishEvent(Event event) throws Exception {
		publishEvent(TaskListener.CATEGORY_DEFAULT,event);
	}

	public void publishEvent(String category, Event event) throws Exception {
		publishEvent(category,event,true);
	}

	/**
	 * 发布工单事件
	 * @param category 类别
	 * @param event 工单事件
	 * @param isolated 是否独立处理（是否启用多线程模式）
	 * @throws Exception 
	 */
	public void publishEvent(String category, final Event event, boolean isolated) throws Exception {
		if (category == null || category.equals(""))
			category = TaskListener.CATEGORY_DEFAULT;
		//处理主要的监听
		List<TaskListener> listenersMaster = new ArrayList<TaskListener>();
		synchronized (listenersMapMaster) {
			if (listenersMapMaster.get(category)!=null)
				listenersMaster = listenersMapMaster.get(category);
		}
		for (int i = 0; i < listenersMaster.size(); i++) {
			final TaskListener listener = (TaskListener) listenersMaster.get(i);
			if (listener.accept(event)) {
				try {
					listener.handleEvent(event);
				} catch (Exception e) {
					logger.warn("事件处理失败(事件:" + event.getClass()
							+ "处理类:" + listener.getClass(), e);
					throw e;
				}
			}
		}
		
		//处理附加的监听
		List<TaskListener> listeners = new ArrayList<TaskListener>();
		synchronized (listenersMap) {
			if (listenersMap.get(category)!=null)
				listeners = listenersMap.get(category);
		}
		
		for (int i = 0; i < listeners.size(); i++) {
			final TaskListener listener = (TaskListener) listeners.get(i);
			if (isolated) {
				threadPool.addThread(new Runnable() {
					public void run() {
						try {
							if (listener.accept(event)) {
								listener.handleEvent(event);
								
							}
						} catch (Exception e) {
							logger.warn("事件处理失败(事件:" + event.getClass()
									+ "处理类:" + listener.getClass(), e);
						}
					}
				});
			} else {
				if (listener.accept(event)) {
					try {
						listener.handleEvent(event);
					} catch (Exception e) {
						logger.warn("事件处理失败(事件:" + event.getClass()
								+ "处理类:" + listener.getClass(), e);
					}
				}
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateCache(TaskInfo obj) {
		if (obj != null) {
			if (obj.getParentOid() != -1 || obj.getParent() != null)
				return;
			if (obj.getStatus() == TaskInfo.STATUS_OPEN) {
				
				List<TaskData> childs = obj.getTaskData();
				for (Iterator<TaskData> iterator = childs.iterator(); iterator.hasNext();) {
					TaskData taskData = iterator.next();
					String key = obj.getWfOid()+"_"+taskData.getNodeId()+"_"+taskData.getAssignTo()+"_"+obj.getOid();
					if (taskData.isWait()) {
						Cache.WaitData.put(key, taskData);
					} else {
						if (Cache.WaitData.containsKey(key)) {
							Cache.WaitData.remove(key);
						}
					}
				}

				Cache.Tasks.put(ITSM + "_" + obj.getWfOid() + "_"
						+ obj.getOid(), obj);
				Map<String, TaskInfo> tm = Cache.Workflow_Tasks.get(obj
						.getWfOid()
						+ "");
				if (tm == null) {
					tm = new HashMap<String, TaskInfo>();
					Cache.Workflow_Tasks.put(obj.getWfOid() + "", tm);
				}
				tm.put(obj.getOid() + "", obj);
				
			} else {
				// 增加到历史缓存
				WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(obj
						.getWfOid());
				if (wfInfo.isLoadHisToCache()) {
					TaskData td = obj.getTaskData(obj.getLatestTaskDataId());
					if (td == null)
						return;
					List<ColumnFieldInfo> columns = wfInfo.getColumnFieldsList();
					for (int i = 0; i < columns.size(); i++) {
						ColumnFieldInfo cinfo = columns.get(i);
						obj.addValue(cinfo.getFieldName().toUpperCase(), td
								.getAttribute(cinfo.getFieldName()));
					}
					if (obj.getStatus() == TaskInfo.STATUS_FORCE_CLOSE)
						obj.setStatus(TaskInfo.STATUS_FORCE_CLOSE);
					else
						obj.setStatus(TaskInfo.STATUS_CLOSE);

					Cache.TasksHis.put(ITSM + "_" + obj.getWfOid() + "_"
							+ obj.getOid(), obj);
					Map<String, TaskInfo> tm = Cache.Workflow_TasksHis.get(obj
							.getWfOid()
							+ "");
					if (tm == null) {
						tm = new HashMap<String, TaskInfo>();
						Cache.Workflow_TasksHis.put(obj.getWfOid() + "", tm);
					}
					tm.put(obj.getOid() + "", obj);

				}
				// 从打开缓存里面清除
				Cache.Tasks.remove(ITSM + "_" + obj.getWfOid() + "_"
						+ obj.getOid());
				Map<String, TaskInfo> tmo = Cache.Workflow_Tasks.get(obj
						.getWfOid()
						+ "");
				if (tmo != null) {
					tmo.remove(obj.getOid() + "");
				}
			}
		}
	}


}
