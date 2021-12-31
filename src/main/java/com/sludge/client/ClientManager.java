package com.sludge.client;

import com.sludge.channelConfig.ChannelInitializerConfig;
import com.sludge.core.IDGenerator;
import com.sludge.core.handler.RpcResultKeeper;
import com.sludge.core.protocol.RpcRequestMessage;
import com.sludge.sample.SludgeService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;


@Slf4j
public class ClientManager {


    public static void main(String[] args) {
        SludgeService service = ClientManager.getService(SludgeService.class);
        String caonima = service.sayHello("caonima");
        System.out.println("----result---" + caonima);
    }

    public static <T> T getService(Class<T> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = new Class[]{clazz};
        Object o = Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {
            Channel channel = Client.getChannel("127.0.0.1", 9999);
            int i = IDGenerator.get();
            channel.writeAndFlush(RpcRequestMessage.builder()
                    .sequenceId(i)
                    .interfaceName(clazz.getName())
                    .method(method.getName())
                    .parameterTypes(method.getParameterTypes())
                    .parameterValues(args)
                    .returnType(method.getReturnType())
                    .build());
            DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
            RpcResultKeeper.RESULT_KEEPER.put(i, promise);
            //await for returning
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            } else {
                throw new RuntimeException(promise.cause());
            }
        });
        return clazz.cast(o);
    }

    static class Client {

        private static Channel channel = null;

        private static final Object LOCK = new Object();


        public static Channel getChannel(String address, int port) {
            if (channel != null) {
                return channel;
            }
            synchronized (LOCK) {
                if (channel != null) {
                    return channel;
                }
                initChannel(address, port);
                return channel;
            }
        }

        private static void initChannel(String address, int port) {
            NioEventLoopGroup loopGroup = new NioEventLoopGroup();
            Bootstrap client = new Bootstrap();
            ChannelFuture connect = client.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(ChannelInitializerConfig.buildWithResponse())
                    .connect(address, port);
            try {
                channel = connect.sync().channel();
                channel.closeFuture().addListener(promise -> {
                    loopGroup.shutdownGracefully();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
