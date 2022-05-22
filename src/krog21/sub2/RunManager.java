package com.lgcns.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class RunManager {
	
	public static Map<String, Queue<String>> queueMap = new HashMap<>();
	public static Map<String, Integer> sizeMap = new HashMap<>();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
				
		while (scanner.hasNextLine()) {
			String[] inputs = scanner.nextLine().split(" ");
			String name = inputs[1];
						
			if ("CREATE".equals(inputs[0])) {
				if (queueMap.containsKey(name)) {
					System.out.println("Queue Exist");
				} else {
					int size = Integer.parseInt(inputs[2]);
					queueMap.put(name, new LinkedList<String>());
					sizeMap.put(name, size);
				}
			} else if ("SEND".equals(inputs[0])) {
				if (queueMap.get(name).size() == sizeMap.get(name)) {
					System.out.println("Queue Full");
				} else {
					queueMap.get(name).add(inputs[2]);
				}
				
			} else if ("RECEIVE".equals(inputs[0])) {
				if (queueMap.get(name).size() > 0) {
					System.out.println(queueMap.get(name).poll());					
				}
			}
		}

	}

}
