package ssp20.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamBasicSample {

	public static void main(String[] args) {
		{
			List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
			Stream<String> s1 = list.stream();

			Set<String> set = new HashSet<>();
			Stream<String> s2 = set.stream();

			Map<String, String> map = new HashMap<>();
			Stream<Map.Entry<String, String>> s3 = map.entrySet().stream();
		}

		{
			String[] array = { "Java", "Scala", "JavaScript", "Groovy" };
			Stream<String> s1 = Stream.of(array);

			Stream<String> s2 = Stream.of("Java", "Scala", "JavaScript", "Groovy");
		}
		{
			String[] array1 = { "Java", "Scala", "JavaScript", "Groovy" };
			Stream<String> s1 = Arrays.stream(array1);
			int[] array2 = { 1, 2, 3 };
			IntStream s2 = Arrays.stream(array2);
		}
		{
			List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
			list.stream()
			.filter(s -> s.startsWith("J"))
			.map(s -> s.toUpperCase())
			.sorted((a, b) -> a.length() - b.length())
			.forEach(System.out::println); // JAVA, JAVASCRIPT
			System.out.println(list); // [Java, Scala, JavaScript, Groovy]
		}
		{
			Stream<String> stream = Stream.of("Java", "Scala", "JavaScript", "Groovy");
			stream.forEach(s -> {System.out.println(s);});
		}
	}

}
