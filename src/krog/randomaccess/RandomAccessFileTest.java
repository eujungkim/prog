package krog.randomaccess;

import java.io.IOException;
import java.io.RandomAccessFile;
 
/**
생성자는 두가지.
new RandomAccessFile(파일패스, 모드);
new RandomAccessFile(파일, 모드);
RandomAccessFile.length() : 파일 길이 반환
RandomAccessFile.seek(포인터 위치값) : 포인터 위치값을 설정
RandomAccessFile.read(바이트배열) : 현 포인터 위치에서부터 바이트 배열 길이만큼 읽어 들임
RandomAccessFile.getFilePointer() :  현 포인터 위치를 반환
RandomAccessFile.close() : 파일 닫기
 */
public class RandomAccessFileTest {
 
	public static void main(String[] args) throws IOException {
 
		// 읽어들일 사이즈
		int seekSize = 5;
 
		// 읽기 전용으로 파일을 읽음.
		RandomAccessFile rdma = new RandomAccessFile("/test.txt","r");
		String line = "";
 
		// 전체 문자열을 출력
		while ((line = rdma.readLine()) != null) {
			System.out.println(line);			
		}
 
		// 문자열 총 길이
		System.out.println("total length : " + rdma.length()+"\n");
 
		byte[] data = null;
 
		// 루프 사이즈 = 총길이/seekSize + (총길이%seekSize의 나머지가 0이면 0을 반환 0이아니면 1을 반환)
		long size = rdma.length()/seekSize+(rdma.length()%seekSize == 0 ? 0:1);
		for (int i = 0; i < size; i++) {
			data = new byte[seekSize];
 
			// seekSize 만큼 증가
			rdma.seek(i*seekSize);
			rdma.read(data);
 
			// 바이트 데이터를 문자열로 변환(trim()을 사용해 공백을 제거) 
			System.out.printf("pointer : %02d  str : %s \n" , rdma.getFilePointer(), new String(data).trim());
		}	
 
		// 파일 닫기
		rdma.close();
	}
}
