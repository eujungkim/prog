package krog.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * reflection
 * process builder
 * collection util
 * base64, sha256
 * read/write file by socket
 * string, integer format
 * 10진수 <-> 2/8/16진수
 */
public class Snippets {

	// reflection
	public void reflectionSample() throws Exception {
		{
			File classFile = new File("C:\\Users\\jinus\\eclipse-workspace\\test\\bin\\aaa\\bbb\\ccc\\MyClass.class");
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { classFile.toURI().toURL() });
			Class<?> clazz = urlClassLoader.loadClass("aaa.bbb.ccc.MyClass"); // load class
			Object obj = clazz.newInstance(); // new instance
			int[] tmp = new int[] {};
			Method method1 = clazz.getMethod("returnIntArray", tmp.getClass()); // get method with int array input
			Object result1 = method1.invoke(obj, new int[] { 1, 5 }); // invoke method
			int size = Array.getLength(result1); // get length of array result
			int[] output1 = new int[size]; // convert object to array
			for (int i = 0; i < size; i++) {
				output1[i] = Array.getInt(result1, i);
			}
			System.out.println(Arrays.toString(output1));

			Method method2 = clazz.getMethod("returnString", String.class, String.class); // get method
			Object result2 = method2.invoke(obj, "data1", "data2"); // invoke method
			String output2 = (String) result2; // convert object to string
			System.out.println(output2);

			urlClassLoader.close();
		}
		{
			File jarFile = new File("C:\\Users\\jinus\\eclipse-workspace\\test\\class\\jarpath\\myclass.jar");
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() });
			Class<?> clazz = urlClassLoader.loadClass("aaa.bbb.ccc.MyClass2"); // load class
			Object obj = clazz.newInstance(); // new instance
			Method method = clazz.getMethod("returnString", String.class, String.class); // get method with string inputs
			Object result = method.invoke(obj, "data1", "data2");
			System.out.println(result);
			
			Object result1 = clazz.getMethod("returnIntArray", (new int[] {}).getClass()).invoke(obj, new int[] { 1, 5 }); // get method with int array input
			int[] output2 = (int[]) result1; // convert object to array
			System.out.println(Arrays.toString(output2));
			urlClassLoader.close();
		}
	}
	
	// process builder
	public void processBuilderSample() throws Exception {
		Process process = new ProcessBuilder("./CODECONV","MESSAGE02").start();
		InputStream is = process.getInputStream();
		byte[] buff = new byte[9];
		int size = is.read(buff);
		System.out.println(size);
		System.out.println(new String(buff));
	}
	
	// collection util
	public void collectionUtilSample() {
		// HashMap .getOrDefault
		HashMap<String, Integer> hm = new HashMap<>();
		hm.getOrDefault("key1", 0);

		// Arrays .copyOfRange & .sort
		int[] temp = Arrays.copyOfRange(new int[] {0, 1, 2, 3, 4, 5}, 2, 4);
		System.out.println(Arrays.toString(temp)); // result : [2, 3]
		Arrays.sort(temp); // array도 바로 sort 가능
	}
	
	public void Base64Sample(String TestString) throws UnsupportedEncodingException
	{
	    Encoder encoder = Base64.getEncoder();    
	    String encodedString = encoder.encodeToString(TestString.getBytes("UTF-8"));
	    System.out.println(encodedString);

	    Decoder decoder = Base64.getDecoder();
	    byte[] decodedBytes = decoder.decode(encodedString);
	    String decodedString = new String(decodedBytes, "UTF-8");
	    System.out.println(decodedString);
	}

	public void SHA256(String input) throws NoSuchAlgorithmException 
	{
	    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");    
	    byte[] result = mDigest.digest(input.getBytes());
	    StringBuffer sb = new StringBuffer();
	    
	    for (int i = 0; i < result.length; i++) {
	        sb.append(Integer.toString((result[i] & 0xFF) + 0x100, 16).substring(1));
	    }
	         
	    System.out.println(sb.toString());		
	}
	
	public void sendFileToSocket(File file) throws Exception {
		Socket socket = new Socket("127.0.0.1", 9090);
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		os.writeUTF(file.getName());
		os.writeInt((int)file.length());
		
		FileInputStream in = new FileInputStream(file);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1) {
			os.write(buf, 0, len);
		}
		in.close();
		os.close();
		socket.close();
	}
	
	public void recieveFileFromSocket(File file) throws Exception {
		ServerSocket server = new ServerSocket(9090);
		while (true) {
			Socket socket = server.accept();
			DataInputStream in = new DataInputStream(socket.getInputStream());
			String fileName = in.readUTF();
			int fileSize = in.readInt();
			int len;
			byte[] buf = new byte[1024];
			while (fileSize > 0) {
				len = in.read(buf, 0, Math.min(fileSize, buf.length));
				fileSize = fileSize - len;
			}
			
			//TODO
		}
	}
	
	public void formatString() {
		String str = "teststring";
		int a = 12345;

		String format;
		format = String.format("%15s", str); // 문자 포맷, String.format("%자릿수s", str) => [     teststring]
		format = String.format("%5s", str); // 입력 길이가 자릿수보다 길 경우 그대로 출력 => [teststring]
		format = String.format("%-15s", str); // 왼쪽 정렬 => [teststring     ]
		format = String.format("%15d", a); // 숫자 포맷, String.format("%자릿수d", num) => [            123]
		format = String.format("%015d", a); // 앞에 0 삽입 => [000000000000123]
		format = String.format("%02d", a); // 입력 길이가 자릿수보다 길 경우 그대로 출력 => [12345]
	}
	
	public void testLinkedList() {
		// 통상 ArrayList가 더 효율적이지만, LinkedList는 아래의 함수를 제공함
		// addFirst(), addLast(), removeFirst(), removeLast(), getFirst(), getLast()
	}
	
    public void testIterator() {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(12);
        numbers.add(8);
        numbers.add(2);
        numbers.add(23);
        Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            Integer i = it.next();
            if (i < 10) {
                it.remove();
            }
        }
        System.out.println(numbers); //[12, 23]
    }
    
    public void testIntegerBinaryOctalHex() {
    	int i = 127;
    	 
    	String binaryString = Integer.toBinaryString(i); //2진수
    	String octalString = Integer.toOctalString(i);   //8진수
    	String hexString = Integer.toHexString(i);       //16진수
    	 
    	System.out.println(binaryString); //1111111
    	System.out.println(octalString);  //177
    	System.out.println(hexString);    //7f
    	 
    	 
    	int binaryToDecimal = Integer.parseInt(binaryString, 2);
    	int binaryToOctal = Integer.parseInt(octalString, 8);
    	int binaryToHex = Integer.parseInt(hexString, 16);
    	 
    	System.out.println(binaryToDecimal); //127
    	System.out.println(binaryToOctal);   //127
    	System.out.println(binaryToHex);     //127
    }
    
    // java tutorial : https://www.w3schools.com/java/
    // regular expression : https://www.w3schools.com/java/java_regex.asp
	
    // Thread : isAlive()
    // https://www.tutorialspoint.com/javaexamples/java_threading.htm
    
    // https://www.javatpoint.com/multithreading-in-java
    // synchronized(this){//synchronized block  // block
    // synchronized static void printTable(int n){  // static method
}