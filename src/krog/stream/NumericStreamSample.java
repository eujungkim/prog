package ssp20.stream;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NumericStreamSample {

	public static void main(String[] args) {
		{
			IntStream intStream1 = IntStream.of(1, 2, 3);
			
			int [ ] array = {1, 2, 3};
			IntStream intStream2 = IntStream.of(array);
			
			IntStream intStream3 = IntStream.range(1,  10); // 1 2 3 4 5 6 7 8 9 
			
			IntStream intStream4 = IntStream.rangeClosed(1,  10); // 1 2 3 4 5 6 7 8 9 10 
		}
		{
			Stream<String> stream = Stream.of("Java", "Scala", "JavaScript", "Groovy");
			IntStream intStream = stream.mapToInt(s -> s.length());
			intStream.forEach(s -> System.out.println(s));
		}
	}

}
