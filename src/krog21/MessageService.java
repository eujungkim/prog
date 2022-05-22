package com.lgcns.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessageService {
	public static Map<String, List<Message>> queueMap = new HashMap<>();
	public static Map<String, List<Message>> deadMap = new HashMap<>();
	public static Map<String, MessageInfo> infoMap = new HashMap<>();
	public static Map<String, List<WaitMessage>> waitMap = new HashMap<>();
	
	public boolean create(String name, MessageInfo info) {
		if (queueMap.containsKey(name)) {
			return false;
		} else {
			queueMap.put(name, Collections.synchronizedList(new LinkedList<Message>()));
			deadMap.put(name, Collections.synchronizedList(new LinkedList<Message>()));
			infoMap.put(name, info);
			waitMap.put(name, Collections.synchronizedList(new LinkedList<WaitMessage>()));
			return true;
		}
	}
	
	public boolean send(String name, String message) {
		System.out.println("SEND : " + name + " " + message);
		if (queueMap.get(name).size() == infoMap.get(name).getQueueSize()) {
			return false;
		} else {
			Message m = new Message(message);
			queueMap.get(name).add(m);			
			return true;
		}
	}
	
	synchronized public Message receive(String name, long recieved) {
		if (queueMap.containsKey(name) && queueMap.get(name).size() > 0) {
			Iterator<Message> iter = queueMap.get(name).iterator();
			while (iter.hasNext()) {
				Message m = iter.next();
				if (!m.isLock()) {
					m.setLock(true);
					m.setReceivedTime(recieved);
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
			Message m = null;
			while (iter.hasNext()) {
				m = iter.next();
				if (id.equals(m.getId())) {
					m.setLock(false);
					m.setReceivedTime(-1);
					m.setFailCount(m.getFailCount() + 1);
					break;
				}
			}			
		}
	}
	
	public synchronized Message dlq(String name) {
		if (deadMap.containsKey(name) && deadMap.get(name).size() > 0) {
			Message result = deadMap.get(name).get(0);
			deadMap.get(name).remove(0);
			return result;
		}
		return null;
	}

}
