import java.util.List;

public class Worker extends AbstractWorker {
	
	public Worker(int queueNo) {
		super(queueNo);
	}
	
	public Worker(int queueNo, List<String> store) {
		super(queueNo, store);
	}
	
	public void removeExpiredStoreItems(long timestamp, List<String> store) {
		for (int i = store.size()-1; i >=0; i--) {
			String value = store.get(i);
			String[] temp = value.split("#");
			long ts = Long.parseLong(temp[0]);
			if (timestamp - ts > 3000) {
				store.remove(i);
			}
		}	}
}
