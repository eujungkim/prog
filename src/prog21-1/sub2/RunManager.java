import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		Map<String, List<String>> queueMap = new HashMap<>();
		Map<String, Integer> infoMap = new HashMap<>();
		
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String input = sc.nextLine();
			if (input.startsWith("CREATE")) {
				String[] tmp = input.split(" ");
				String queueName = tmp[1];
				int size = Integer.parseInt(tmp[2]);
				
				if (queueMap.containsKey(queueName)) {
					System.out.println("Queue Exist");
				} else {
					queueMap.put(queueName, new ArrayList<>());
					infoMap.put(queueName, size);
				}
				
			} else if (input.startsWith("SEND")) {
				String[] tmp = input.split(" ");
				String queueName = tmp[1];
				String message = tmp[2];
				
				List<String> queue = queueMap.get(queueName);
				if (queue.size() == infoMap.get(queueName)) {
					System.out.println("Queue Full");
				} else {
					queue.add(message);
				}
			} else if (input.startsWith("RECEIVE")) {
				String[] tmp = input.split(" ");
				String queueName = tmp[1];
				List<String> queue = queueMap.get(queueName);
				if (queue.size() > 0) {
					String message = queue.get(0);
					queue.remove(0);
					System.out.println(message);					
				}
			}
			
		}
	}

}
