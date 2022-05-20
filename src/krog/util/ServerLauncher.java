package krog.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerLauncher {
	public static void main(String[] args) {

		Server server = new Server();
		Thread thread = new Thread(server);
		thread.start();

		Scanner scanner = new Scanner(System.in);

		String line;
		while ((line = scanner.nextLine()) != null) {
			if (line.equals("QUIT")) {
				server.close();
			} else {
				System.out.println(line);
			}
		}
	}
}

class Server implements Runnable {
	public static final int BUF_SIZE = 4096;
	private ServerSocket serverSocket;
	private boolean isStop;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void run() {
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(27015);
			byte[] buffer = new byte[BUF_SIZE];
			int length;

			while (!isStop) {
				Socket socket = null;
				DataInputStream is = null;

				try {
					socket = serverSocket.accept();
					is = new DataInputStream(socket.getInputStream());
					while (true) {
						String fileName = is.readUTF();
						int fileSize = is.readInt();

						while (fileSize > 0) {
							length = is.read(buffer, 0, Math.min(fileSize, buffer.length));
							fileSize -= length;
							writeFile(fileName, buffer, 0, length);
						}
					}
				} catch (EOFException e) {
					// do nothing
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (socket != null) {
						socket.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void close() {
		isStop = true;
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(String fileName, byte[] buffer, int offset, int length) throws IOException {
		FileOutputStream fw = null;
		try {
			fw = new FileOutputStream("SERVER//" + fileName, true);
			fw.write(buffer, offset, length);
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}

}

class Client {
	private String insId;

	public void sendToServer() throws IOException {
		Socket socket = null;
		DataOutputStream os = null;
		try {
			socket = new Socket("127.0.0.1", 27015);
			os = new DataOutputStream(socket.getOutputStream());

			byte[] buffer = new byte[4096];
			int length;

			//File directory = new File("..//" + insId);
			File directory = new File("insId");
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile()) {
					os.writeUTF(file.getName());
					os.writeInt((int) file.length());

					FileInputStream is = null;
					try {
						is = new FileInputStream(file.getPath());
						while ((length = is.read(buffer)) != -1) {
							os.write(buffer, 0, length);
						}
					} finally {
						if (is != null) {
							is.close();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
	}
}

/**
public class Test {
	public static void main(String[] args) throws Exception {
		new Client().sendToServer();
	}
}
 */