package ssp20.stream;

import java.util.stream.Stream;

public class StreamIterateSample {

	public static void main(String[] args) {
		Stream<Integer> stream = Stream.iterate(10, i -> i *2);
		stream.limit(5).forEach(s -> System.out.println(s));

	}

}
