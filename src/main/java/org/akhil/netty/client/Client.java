package org.akhil.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class Client {

	public static void main(String[] args) {
		new Client().start();
	}

	public void start() {
		EventLoopGroup group = null;
		try {
			group = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();
		bs.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.remoteAddress(new InetSocketAddress("127.0.0.1", 5555))
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ClientHandler());				
					}
				});
		
		ChannelFuture cf = bs.connect().sync();
		cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}

	}
	
	private class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			final String SEND = "Akhil is rocking";
			ByteBuf buffer = Unpooled.copiedBuffer(SEND, CharsetUtil.UTF_8);
			System.out.println("Client Sent : " + SEND + " : " + ByteBufUtil.hexDump(buffer));
			ctx.writeAndFlush(buffer);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
				throws Exception {
			byte[] bytes = new byte[msg.readableBytes()];
			msg.getBytes(0, bytes);
			System.out.println("Client Received : " + new String(bytes) + " : " + ByteBufUtil.hexDump(msg));
		}
		
	}
}
