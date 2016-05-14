package org.akhl.netty.patterns.encoder_decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.InetSocketAddress;


public class UserClient {
	
	public void start() {
		Bootstrap bs = new Bootstrap();
		OioEventLoopGroup group = new OioEventLoopGroup();
		
		bs.group(group).channel(OioSocketChannel.class)
		.remoteAddress(new InetSocketAddress("127.0.0.1", 5555))
		.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
//				ch.pipeline().addLast(new IntegerDecoder(), new ClientHandler());
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
	
}
