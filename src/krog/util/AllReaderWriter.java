package krog.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllReaderWriter {
	
	public static List<String> readFile(String inputFile) throws Exception {
		List<String> list = new ArrayList<String>();
		FileReader reader = new FileReader(inputFile);
		BufferedReader buf = new BufferedReader(reader);
		String line;
		while ((line = buf.readLine()) != null) {
			list.add(line);
		}
		buf.close();
		reader.close();
		return list;
	}
	
	public static void writeFile(List<String> list, String outputFile) throws Exception {
		FileWriter writer = new FileWriter(outputFile);
		PrintWriter pw = new PrintWriter(writer);
		for (String line : list) {
			pw.println(line);
		}
		pw.close();
		writer.close();
	}

	public static void copyBinaryFile(String inputFile, String outputFile) {
		int size = 1024;
		int len;
		try {
			InputStream in = new FileInputStream(inputFile);
			OutputStream out = new FileOutputStream(outputFile);
			
			byte[] buf = new byte[size];
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyTextFile(String inputFile, String outputFile) throws IOException {
		FileReader reader = new FileReader(inputFile);
		FileWriter writer = new FileWriter(outputFile);
		BufferedReader buf = new BufferedReader(reader);
		PrintWriter pw = new PrintWriter(writer);
		
		String line;
		while ((line = buf.readLine()) != null) {
			pw.println(line);
		}
		pw.close();
		buf.close();
		writer.close();
		reader.close();
	}
	
	public static void socketByteReader() throws Exception {
		Socket s = new Socket("127.0.0.1", 9090);
		
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		
		byte[] buffer = new byte[4096];
		int len;
		while (true) {
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		}
	}
	
	public static void socketByteWriter() throws Exception {
		ServerSocket server = new ServerSocket(9090);
		try {
			Socket socket = server.accept();
			try {
				OutputStream out = socket.getOutputStream();
				for (int i = 0; i  < 10; i++) {
					out.write((new Date() + "").getBytes());
				}
			} finally {
				socket.close();
			}
		} finally {
			server.close();
		}
	}
	
	public static void socketLineReader() throws Exception {
		Socket s = new Socket("127.0.0.1", 9090);
		BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line;
		while (true) {
			while ((line = reader.readLine()) != null) {
				System.out.println("CLIENT " + line);
				if ("QUIT".equals(line)) {
					s.close();
					return;
				}
			}
		}
	}
	
	public static void socketLineWriter() throws Exception {
		ServerSocket server = new ServerSocket(9090);
		try {
			Socket socket = server.accept();
			try {
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				for (int i = 0; i < 10; i++) {
					writer.println(new Date() + " test");
				}
			} finally {
				socket.close();
			}
		} finally {
			server.close(); 
		}
	}
}