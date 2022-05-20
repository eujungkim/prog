package krog.randomaccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomAccessFileWriter {
	public static String lock = "lock";

	public static void main(String[] args) throws IOException {
		//System.out.println(new File("output.txt").delete());
		//System.out.println(new File("output2.txt").delete());
		//System.out.println(new File("output3.txt").delete());

		String fileName = "output.txt";
    Thread thread = new Thread(new Writer(fileName));
		thread.start();
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);

		for (int i = 0; i < 10; i++) {
			fixedThreadPool.execute(new Thread(new Writer("output2.txt")));
			try {
				Thread.sleep(9);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < 10; i++) {
			fixedThreadPool.execute(new Thread(new RandomWriter("output3.txt")));
			try {
				Thread.sleep(9);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		fixedThreadPool.shutdown();
	}
}

class Writer implements Runnable {
	String fileName;

	public Writer(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		try {
			FileWriter fw = new FileWriter(fileName, true); // append
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < 10; i++) {
				pw.println(Thread.currentThread().getName() + "-" + i);
				Thread.sleep(10);
			}
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class RandomWriter implements Runnable {
	String fileName;

	public RandomWriter(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		File file = new File(fileName);
		try {
			RandomAccessFile rf = new RandomAccessFile(file, "rw");
			for (int i = 0; i < 10; i++) {
				synchronized (RandomAccessFileWriter.lock) {
					rf.seek(rf.length());
					rf.write((Thread.currentThread().getName() + "-" + i + "\n").getBytes());
				}
				Thread.sleep(10);
			}
			rf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
