package org.akhil.netty.servers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class EchoServer {

	public static void main(String[] args) throws Exception {
		new EchoServer().start(5555);
	}

	public void start(int port) throws Exception{
		NioEventLoopGroup group = null;
		try {
		 group = new NioEventLoopGroup();
		ServerBootstrap bs = new ServerBootstrap();

		bs.group(group).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new EchoServerHandler());
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture cf;
			cf = bs.bind(port).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}

	private class EchoServerHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			ByteBuf buffer = (ByteBuf) msg;
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.getBytes(0, bytes);
			System.out.println("Server Received : " + new String(bytes) + " : " + ByteBufUtil.hexDump(buffer));
			ctx.writeAndFlush(msg);
		}

	}

}
