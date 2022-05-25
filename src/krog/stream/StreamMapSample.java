package ssp20.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamMapSample {

	public static void main(String[] args) {
		{
			List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
			list.stream().map(s -> s.length()).forEach(s -> {
				System.out.println(s);
			});
		}
		{
			List<String> list = Arrays.asList("Java,Groovy", "C#,VB.NET");

			list.stream().flatMap(s -> Stream.of(s.split(",")))
					.forEach(System.out::println); // "Java","Groovy","C#","VB.NET"

		}
	}

}
