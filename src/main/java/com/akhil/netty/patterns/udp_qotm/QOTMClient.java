package com.akhil.netty.patterns.udp_qotm;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class QOTMClient {

	private final static int PORT = 5555;
	private final static String IP = "255.255.255.255";

	public static void main(String[] args) throws InterruptedException {
		new QOTMClient().start();
	}

	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();

		try {
			bs.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.handler(new QOTMHandler());

			Channel ch = bs.bind(0).sync().channel();

			System.out.println("clinett started");

			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("QOTM?",
					CharsetUtil.UTF_8), new InetSocketAddress(IP, PORT)));

		} finally {

		}
	}

	private class QOTMHandler extends
			SimpleChannelInboundHandler<DatagramPacket> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx,
				DatagramPacket msg) throws Exception {
			String quote = msg.content().toString(CharsetUtil.UTF_8);
			System.out.println("Quote : " + quote);
			ctx.close();
		}

	}
}
