package ssp20.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelStreamSample {

	public static void main(String[] args) {

		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		list.parallelStream().map(s -> s.toUpperCase()).forEach(s -> System.out.println(s));

		IntStream.range(1, 100).filter(i -> {
			if (i % 2 == 0)
				return true;
			else
				return false;
		}).forEach(s -> System.out.println(s));

	}

}
