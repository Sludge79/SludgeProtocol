package com.sludge.server;

import com.sludge.channelConfig.ChannelInitializerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {


    public static void main(String[] args) {
        STARTUP(9999);
    }


    private static void STARTUP(int port) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            Channel channel = serverBootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(ChannelInitializerConfig.buildWithRequest())
                    .bind(port)
                    .sync()
                    .channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.info("error", e);
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }


}
