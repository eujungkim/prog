package ssp20.stream;

import java.util.stream.Stream;

public class StreamConcatSample {

	public static void main(String[] args) {
		Stream<String> s1 = Stream.of("Java", "Groovy");
	    Stream<String> s2 = Stream.of("Scala", "Clojure");
	    
	    Stream<String> s3 = Stream.concat(s1, s2);
	    s3.forEach(s -> System.out.println(s.length())); // 4, 5, 6, 7
	}

}
