package ssp20.stream;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamConversionSample {
	public static void main(String[] args) {
		{
			List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
			List<String> result1 = list.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());
			System.out.println(result1);
			
			Set<String> set = list.stream().map(s -> s.toLowerCase()).collect(Collectors.toSet());
			System.out.println(set);
			
			List<String> result3 = list.stream().map(s -> s.toUpperCase())
					.collect(Collectors.toCollection(LinkedList::new));
			System.out.println(result3);
			
			Map<String, Integer> map = list.stream().collect(Collectors.toMap(s -> s, s -> s.length()));
			System.out.println(map);
			
		}
		{
			List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
			Object[] result1 = list.stream().map(s -> s.toUpperCase()).toArray();
			System.out.println(Arrays.toString(result1)); 
			String[] result2 = list.stream().map(s -> s.toUpperCase()).toArray(String[]::new);
			System.out.println(Arrays.toString(result2));

		}

	}
}
