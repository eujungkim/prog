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
			String result = map.get(temp[0]).run(temp[1]);

			if (result != null) {
				System.out.println(result);
			}
		}

	}
}
