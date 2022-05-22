package com.lgcns.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MessageService {
	public static Map<String, Queue<Message>> queueMap = new HashMap<>();
	public static Map<String, Integer> sizeMap = new HashMap<>();
	
	public boolean create(String name, int size) {
		if (queueMap.containsKey(name)) {
			return false;
		} else {
			queueMap.put(name, new LinkedList<Message>());
			sizeMap.put(name, size);
			return true;
		}
	}
	
	public boolean send(String name, String message) {
		if (queueMap.get(name).size() == sizeMap.get(name)) {
			return false;
		} else {
			queueMap.get(name).add(new Message(message));
			return true;
		}
	}
	
	public Message receive(String name) {
		if (queueMap.containsKey(name) && queueMap.get(name).size() > 0) {
			Iterator<Message> iter = queueMap.get(name).iterator();
			while (iter.hasNext()) {
				Message m = iter.next();
				if (!m.isLock()) {
					m.setLock(true);
					return m;
				}
			}
		}
		return null;
	}
	
	public void ack(String name, String id) {
		if (queueMap.containsKey(name) && queueMap.get(name).size() > 0) {
			Iterator<Message> iter = queueMap.get(name).iterator();
			while (iter.hasNext()) {
				Message m = iter.next();
				if (id.equals(m.getId())) {
					iter.remove();
					return;
				}
			}
		}
	}
	
	public void fail(String name, String id) {
		if (queueMap.containsKey(name) && queueMap.get(name).size() > 0) {
			Iterator<Message> iter = queueMap.get(name).iterator();
			while (iter.hasNext()) {
				Message m = iter.next();
				if (id.equals(m.getId())) {
					m.setLock(false);
					return;
				}
			}
		}
	}

}
