package com.lgcns.test;

import java.util.UUID;

public class Message {
	private String id;
	private String message;
	private boolean lock;
	
	public Message(String message) {
		this.id = UUID.randomUUID().toString();
		this.message = message;
		this.lock = false;
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
		return "Message [id=" + id + ", message=" + message + ", lock=" + lock + "]";
	}
	
}
