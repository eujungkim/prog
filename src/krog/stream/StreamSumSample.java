package ssp20.stream;

import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamSumSample {

	public static void main(String[] args) {
		{
			IntStream stream = IntStream.of(1, 2, 3, 4, 5);
			int total = stream.sum();
			System.out.println(total); // 15
		}
		{
			Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
			Optional<Integer> result = stream.reduce((a, b) -> a * b);
			System.out.println(result.orElse(-1)); // 120
		}
		{
			Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
			Integer result = stream.reduce(1, (a, b) -> a * b);
			System.out.println(result); // 120

		}
		{
			IntStream stream = IntStream.of(1, 2, 3, 4, 5);
			IntSummaryStatistics statics = stream.summaryStatistics();
			System.out.println(statics.getAverage()); // 3.0
			System.out.println(statics.getMax()); // 5
			System.out.println(statics.getMin()); // 1
			System.out.println(statics.getSum()); // 15
			statics.accept(6);
			System.out.println(statics.getSum()); // 21
			System.out.println(statics.getMax()); // 6
			System.out.println(statics.getAverage()); // 3.5
    
		}
	}

}
