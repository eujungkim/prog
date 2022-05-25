package ssp20.stream;

import java.util.Arrays;
import java.util.List;

public class StreamForEachSample {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		list.stream().forEach(s -> System.out.println(s));
		list.stream().forEach(System.out::println);
		
		list.stream().filter(s -> s.startsWith("J"))
		.forEach(s -> System.out.println(s));
	}

}
