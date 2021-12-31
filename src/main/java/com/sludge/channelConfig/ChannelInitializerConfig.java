package com.sludge.channelConfig;

import com.sludge.core.codec.SludgeFrameDecoder;
import com.sludge.core.codec.SludgeMessageCodec;
import com.sludge.core.handler.RpcRequestHandler;
import com.sludge.core.handler.RpcResponseHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class ChannelInitializerConfig extends ChannelInitializer<NioSocketChannel> {


    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

    private static final SludgeMessageCodec SLUDGE_MESSAGE_CODEC = new SludgeMessageCodec();

    private static final RpcRequestHandler REQUEST_HANDLER = new RpcRequestHandler();

    private static final RpcResponseHandler RESPONSE_HANDLER = new RpcResponseHandler();

    private SimpleChannelInboundHandler<?> handler;

    public ChannelInitializerConfig(SimpleChannelInboundHandler<?> handler) {
        this.handler = handler;
    }

    public static ChannelInitializerConfig buildWithRequest() {
        return new ChannelInitializerConfig(REQUEST_HANDLER);
    }

    public static ChannelInitializerConfig buildWithResponse() {
        return new ChannelInitializerConfig(RESPONSE_HANDLER);
    }


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new SludgeFrameDecoder())
                .addLast(LOGGING_HANDLER)
                .addLast(SLUDGE_MESSAGE_CODEC)
                .addLast(handler);
    }

}