package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		List<String> queue = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String input = sc.nextLine();
			if (input.startsWith("SEND")) {
				String message = input.split(" ")[1];
				queue.add(message);
			} else if (input.startsWith("RECEIVE")) {
				if (queue.size() > 0) {
					String message = queue.get(0);
					queue.remove(0);
					System.out.println(message);					
				}
			}
			
		}
	}

}
