package ssp20.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamGroupingSample {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		Map<Integer, List<String>> map = list.stream().collect(Collectors.groupingBy(s -> s.length()));
		System.out.println(map); // {4=[Java], 5=[Scala], 6=[Groovy], 10=[JavaScript]}
	}

}
