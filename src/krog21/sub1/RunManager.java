package com.lgcns.test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		Queue<String> queue = new LinkedList<>();
		Scanner scanner = new Scanner(System.in);
				
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			if (input.startsWith("SEND")) {
				String tmp = input.split(" ")[1];
				queue.add(tmp);
			} else if (input.startsWith("RECEIVE") && queue.size() > 0) {
				System.out.println(queue.poll());
			}
		}

	}

}
