package org.akhil.netty.servers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Random;

public class TimeServer {

	public static void main(String[] args) {
		new TimeServer().start(5555);
	}

	public void start(int port) {
		
		try {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap bs = new ServerBootstrap();

		bs.group(group).channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new TimeServerHandler());
			}
		});
		
		ChannelFuture cf = bs.bind(port).sync();
		
		cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		

	}
	
	private class TimeServerHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ByteBuf buf = ctx.alloc().buffer(4);
			Random rand = new Random();
			int i = rand.nextInt();
			buf.writeInt(i);
			System.out.println("Sending : " + i + " : " + ByteBufUtil.hexDump(buf));
			ChannelFuture cf = ctx.writeAndFlush(buf);
			cf.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					ctx.close();
				}
			});
		}
		
	}

}


