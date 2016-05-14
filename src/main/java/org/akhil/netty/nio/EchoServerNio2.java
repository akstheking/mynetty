package org.akhil.netty.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class EchoServerNio2 {
	
	public static void main(String[] args) throws IOException {
		new EchoServerNio2().start(5555);
	}

	public void start(int port) throws IOException {

		final AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel
				.open();
		InetSocketAddress address = new InetSocketAddress(port);
		serverSocket.bind(address);
		final CountDownLatch latch = new CountDownLatch(1);
		System.out.println("server started");
		serverSocket.accept(null,
				new CompletionHandler<AsynchronousSocketChannel, Object>() {

					@Override
					public void completed(AsynchronousSocketChannel channel,
							Object attachment) {
						System.out.println("accepted");
						serverSocket.accept(null, this);
						ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
						channel.read(byteBuffer, byteBuffer,
								new EchoCompletionHandler(channel));
					}

					@Override
					public void failed(Throwable exc, Object attachment) {
						exc.printStackTrace();
						try {
							serverSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							latch.countDown();
						}
					}

				});

		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class EchoCompletionHandler implements
			CompletionHandler<Integer, ByteBuffer> {
		
		AsynchronousSocketChannel channel;
		
		public EchoCompletionHandler(AsynchronousSocketChannel channel) {
			this.channel = channel;
		}

		@Override
		public void completed(Integer result, ByteBuffer buffer) {
			System.out.println("readComplete");
			try {
				String out = new String(buffer.array(), "UTF-8");
				System.out.println(out);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			buffer.flip();
			channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					if(buffer.hasRemaining()) {
						channel.write(buffer, null, this);
					} else {
						buffer.compact();
						channel.read(buffer, null, EchoCompletionHandler.this);
					}
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					exc.printStackTrace();					
				}
			});
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exc.printStackTrace();
		}

	}

}
