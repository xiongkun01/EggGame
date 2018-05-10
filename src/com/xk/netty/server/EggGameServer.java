package com.xk.netty.server;

import com.xk.netty.codec.MarshallingCodeFactory;
import com.xk.netty.handler.RegisterHandler;
import com.xk.netty.struct.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class EggGameServer {

    //专门用来处理耗时业务的线程池，不会阻塞IO线程
    private static EventExecutorGroup group = new DefaultEventExecutorGroup(10);

    public static void main(String[] args) throws InterruptedException {
        new EggGameServer().bind();
    }

    public void bind() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //解码器
                            pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            //编码器
                            pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                            //注册处理器
                            pipeline.addLast(group, "RegisterHandler", new RegisterHandler());

                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
