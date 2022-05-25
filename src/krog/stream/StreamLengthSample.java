package ssp20.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamLengthSample {

	public static void main(String[] args) {

		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		Stream<String> stream1 = list.stream();
		long count1 = stream1.count();
		System.out.println(count1);
		
		IntStream stream2 = IntStream.iterate(0, i -> i + 1);
		long count2 = stream2.count();
		System.out.println(count2);
	}

}
