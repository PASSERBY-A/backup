package com.volkswagen.tel.billing.billcall.biz.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

	private static Map<String,String> map = new HashMap<String,String>();

	private static TaskQueue instance = new TaskQueue();

	private Lock lock = new ReentrantLock();

	private TaskQueue() {
	}

	public static synchronized TaskQueue getInstance() {

		if (instance == null) {

			instance = new TaskQueue();
		}

		return instance;
	}

	public void put(String key,String value) {

		lock.lock();

		try {

			map.put(key, value);
			System.out.println(map.size() + "------put");
		} finally {
			lock.unlock();
		}

	}

	public void remove(String key) {

		lock.lock();

		try {
			map.remove(key);
			System.out.println(map.size() + "------remove");
		} finally {
			lock.unlock();
		}

	}

	public boolean containsKey(String key) {

		lock.lock();

		try {
			return map.containsKey(key);
			
		} finally {
			lock.unlock();
		}

	}
	
	
	
	public void clear() {

		lock.lock();

		try {
			map.clear();
			System.out.println(map.size() + "------clear");
		} finally {
			lock.unlock();
		}

	}
	
	

	public static void main(String[] args) throws InterruptedException {

		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 10; i++) {

					new TaskQueue().put("8","-----");

					try {
						Thread.sleep(900);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {

					new TaskQueue().clear();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}).start();

	}

}
