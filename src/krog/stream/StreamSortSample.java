package ssp20.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StreamSortSample {

	public static void main(String[] args) {

		List<String> list = Arrays.asList("Java", "Scala", "JavaScript", "Groovy");
		list.stream().sorted().forEach(s -> System.out.println(s));
		list.stream().sorted((a, b) -> a.length() - b.length()).forEach(s -> System.out.println(s));
		list.stream().sorted(new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return arg1.length() - arg0.length();
			}}).forEach(s -> System.out.println(s));;
	}

}
