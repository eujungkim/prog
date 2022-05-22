package com.lgcns.test;

import java.util.Iterator;

public class WaitThread implements Runnable {
	
	private String name;
	private MessageService service;
	public WaitThread(String name, MessageService service) {
		this.name = name;
		this.service = service;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Iterator<WaitMessage> iter = MessageService.waitMap.get(name).iterator();
			while (iter.hasNext()) {
				WaitMessage wm = iter.next();
				if (wm.getMessage() == null) {
					Message m = service.receive(name, System.currentTimeMillis());
					if (m != null) {
						wm.setMessage(m);	
					}
				}
			}
		}
		
	}

}
