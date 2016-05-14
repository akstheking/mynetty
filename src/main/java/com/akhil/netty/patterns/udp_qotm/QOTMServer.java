package com.akhil.netty.patterns.udp_qotm;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class QOTMServer {

	private final static int PORT = 5555;
	private final static String[] QUOTES = {
			"Enough is enough! I have had it with these motherf_____g snakes on this motherf_____g plane!",
			"Ever since I can remember I always wanted to be a gangster. To me that was better than being president of the United States. To be a gangster was to own the world.",
			"Everybody runs, Fletch.",
			"Fasten your seat belts, it's going to be a bumpy night!",
			"Frankly, my dear, I don't give a damn!",
			"Gentlemen, you can\'t fight in here! This is the War Room!" };

	private final static Random rand = new Random();

	public static void main(String[] args) throws InterruptedException {
		new QOTMServer().start();
	}

	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();

		try {
			bs.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.handler(new QOTMServerHandler());

			System.out.println("Server started");

			bs.bind(PORT).sync().channel().closeFuture().await();
		} finally {
			group.shutdownGracefully();
		}

	}

	private class QOTMServerHandler extends
			SimpleChannelInboundHandler<DatagramPacket> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx,
				DatagramPacket msg) throws Exception {
			System.out.println(msg.content().toString(CharsetUtil.UTF_8));
			if (msg.content().toString(CharsetUtil.UTF_8).equals("QOTM?")) {
				System.out.println(msg);
				String quote = QUOTES[rand.nextInt(QUOTES.length)];
				System.out.println("sending quote : " + quote + " to : " + msg.sender());
				ctx.writeAndFlush(new DatagramPacket(
						Unpooled.copiedBuffer(quote,CharsetUtil.UTF_8)
								, msg.sender()));
			}
		}
	}

}
