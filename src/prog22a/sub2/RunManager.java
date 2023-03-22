import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String line = sc.nextLine();
			String[] tmp = line.split(" ");
			String proxyName = tmp[0];
			String path = tmp[1];
			String output = readContent(proxyName + ".txt", path);
			System.out.println(output);
		}

	}

	public static String readContent(String proxyName, String path) throws Exception {
		FileReader a = new FileReader(proxyName);
		BufferedReader b = new BufferedReader(a);

		List<String> values = new ArrayList<>();
		String line;
		while ((line = b.readLine()) != null) {
			values.add(line);
		}

		String content;
		for (String value : values) {
			if (value.startsWith(path)) {
				String newProxy = value.split("#")[1];
				String newPath = value.split("#")[0];
				if (newProxy.startsWith("Proxy-")) {
					a.close();
					b.close();
					return readContent(newProxy, newPath);
				} else {
					FileReader reader = new FileReader(newProxy);
					BufferedReader buf = new BufferedReader(reader);
					String contents = buf.readLine();
					buf.close();
					reader.close();
					return contents;
				}
			}
		}
		return null;

	}

}
