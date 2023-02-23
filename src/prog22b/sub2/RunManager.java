import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		Map<String, Worker> map = new HashMap<>();

		map.put("0", new Worker(0));
		map.put("1", new Worker(1));
		
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String in = scanner.nextLine();
			String[] temp = in.split(" ");
			long timestamp = Long.parseLong(temp[0]);
			String queueNo = temp[1];
			String value = temp[2];
			
			String result = map.get(queueNo).run(timestamp, value);

			if (result != null) {
				System.out.println(result);
			}
		}
	}

}
