package org.akhl.netty.patterns.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;

public class SumClient {
	
	public static void main(String[] args) {
		new SumClient().start();
	}
	
	public void start() {
		Bootstrap bs = new Bootstrap();
		OioEventLoopGroup group = new OioEventLoopGroup();
		
		bs.group(group).channel(OioSocketChannel.class)
		.remoteAddress(new InetSocketAddress("127.0.0.1", 5555))
		.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new IntegerDecoder(), new ClientHandler());
			}
		});
		
		ChannelFuture cf;
		try {
			cf = bs.connect().sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ClientHandler extends SimpleChannelInboundHandler<Integer> {
		
		int sum = 0;

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Chnnel active");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Integer msg)
				throws Exception {
			int i = msg;
			System.out.println("read0 : " + i);
			sum += i;
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx)
				throws Exception {
			System.out.println("Read complete : " + sum);
		}
		
		

	}
	
private class ClientHandler2 extends ChannelInboundHandlerAdapter {
		
		int sum = 0;

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Chnnel active");
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			int i = (Integer) msg; 
			System.out.println("read0 : " + i);
			sum += i;
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx)
				throws Exception {
			System.out.println("Read complete : " + sum);
		}
		
		

	}
	
	private class IntegerDecoder extends ByteToMessageDecoder {

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in,
				List<Object> out) throws Exception {
			if(in.readableBytes() < 4) {
				return;
			}
			int i = in.readInt();
			System.out.println("Read : " + i);
			out.add(i);
		}

		
	}
	

}
