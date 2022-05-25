package ssp20.stream;

import java.util.Arrays;
import java.util.List;

public class StreamMatchSample {
	public static void main(String[] args) {
		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		
		boolean result1 = list.stream().allMatch(s -> s.startsWith("J"));
		System.out.println(result1);
		
		boolean result2 = list.stream().anyMatch(s -> s.startsWith("J"));
		System.out.println(result2);

		boolean result3 = list.stream().noneMatch(s -> s.startsWith("J"));
		System.out.println(result3);
	}
}
