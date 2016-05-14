package org.akhil.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServerNIO1 {

	public static void main(String[] args) throws Exception {
		new EchoServerNIO1().server(8888);
	}

	public void server(int port) throws Exception {
		ServerSocketChannel channel = ServerSocketChannel.open();
		ServerSocket ss = channel.socket();
		InetSocketAddress addr = new InetSocketAddress(8888);
		ss.bind(addr);
		channel.configureBlocking(false);
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server setup.");
		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			System.out.println("slected keys : " + keys);
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key
								.channel();
						SocketChannel client = server.accept();
						System.out
								.println("Accepted Connection from " + client);
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ
								| SelectionKey.OP_WRITE,
								ByteBuffer.allocate(4096));

					}
					if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer output = (ByteBuffer) key.attachment();
						client.read(output);
						System.out.println("REad : " + output);
					}
					if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer output = (ByteBuffer) key.attachment();
						output.flip();
						client.write(output);
						output.compact();
						System.out.println("Written : " + output);
					}
				} catch (IOException e) {
					key.cancel();
					key.channel().close();
				}
			}
		}
	}

}
