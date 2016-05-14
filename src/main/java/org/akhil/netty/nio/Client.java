package org.akhil.netty.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String... args) throws InterruptedException {
		try {
			Socket socket = new Socket("127.0.0.1", 5555);
			//InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8888);
			//socket.connect(addr);
			Thread.sleep(5000);
			OutputStream os = socket.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			bos.write(new String("Heelo Server").getBytes());
			bos.flush();
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			byte[] bytes = new byte[256];
			int read = bis.read(bytes);
			System.out.println(new String(bytes));
			
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
