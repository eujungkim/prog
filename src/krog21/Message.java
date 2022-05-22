package com.lgcns.test;

import java.util.UUID;

public class Message {
	private String id;
	private String message;
	private boolean lock;
	private int failCount;
	private long receivedTime;
	
	public Message(String message) {
		this.id = UUID.randomUUID().toString();
		this.message = message;
		this.lock = false;
		this.failCount = 0;
		this.receivedTime = -1;
	}

	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", lock=" + lock + ", failCount=" + failCount
				+ ", receivedTime=" + receivedTime + "]";
	}

	
	
}

class MessageInfo {
	private int queueSize;
	private int processTimeout;
	private int waitTime;
	private int maxFailCount;
	
	public MessageInfo(int queueSize, int processTimeout, int waitTime, int maxFailCount) {
		super();
		this.queueSize = queueSize;
		this.processTimeout = processTimeout;
		this.waitTime = waitTime;
		this.maxFailCount = maxFailCount;
	}
	public int getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
	public int getProcessTimeout() {
		return processTimeout;
	}
	public void setProcessTimeout(int processTimeout) {
		this.processTimeout = processTimeout;
	}
	public int getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	public int getMaxFailCount() {
		return maxFailCount;
	}
	public void setMaxFailCount(int maxFailCount) {
		this.maxFailCount = maxFailCount;
	}
}

class WaitMessage {
	private String id;
	private Message message;
	private long start;
	public WaitMessage(String id, long start) {
		super();
		this.id = id;
		this.start = start;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	@Override
	public String toString() {
		return "WaitMessage [id=" + id + ", message=" + message + ", start=" + start + "]";
	}	
}
