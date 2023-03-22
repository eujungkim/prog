import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			
			FileReader reader = new FileReader(line + ".txt");
			BufferedReader buf = new BufferedReader(reader);
			String filename = buf.readLine();
			
			FileReader a = new FileReader(filename);
			BufferedReader b = new BufferedReader(a);
			String content = b.readLine();
			System.out.println(content);
			
			b.close();
			a.close();
			buf.close();
			reader.close();
		}
	}

}
