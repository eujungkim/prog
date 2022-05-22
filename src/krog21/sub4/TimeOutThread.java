package com.lgcns.test;

import java.util.Iterator;
import java.util.List;

public class TimeOutThread implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for (String name : MessageService.queueMap.keySet()) {
				List<Message> queue = MessageService.queueMap.get(name);
				MessageInfo info = MessageService.infoMap.get(name);
				if (info.getProcessTimeout() > 0) {
					Iterator<Message> iter = queue.iterator();
					while (iter.hasNext()) {
						Message m = iter.next();
						if (m.getFailCount() > info.getMaxFailCount()) {
							MessageService.deadMap.get(name).add(m);
							iter.remove();
							continue;
						}
						
						if (m.getReceivedTime() != -1 && 
								System.currentTimeMillis() - m.getReceivedTime() > info.getProcessTimeout() * 1000) {
							m.setLock(false);
							m.setReceivedTime(-1);
							m.setFailCount(m.getFailCount() + 1);
							System.out.println(System.currentTimeMillis() + "UNLOCK>>>" + m);
						}
					}
				}
			}
		}
	}

}
