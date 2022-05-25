package ssp20.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamDistinctSample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = Arrays.asList("Java", "Scala", "Java", "Scala", "JavaScript", "Groovy");
		Stream<String> stream1 = list.stream();
		
		long count = stream1.distinct().count(); // 4
		System.out.println(count);
		System.out.println(list.stream().count()); // 6

	}

}
