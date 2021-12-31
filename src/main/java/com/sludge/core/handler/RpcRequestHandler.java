package com.sludge.core.handler;

import com.sludge.core.protocol.RpcRequestMessage;
import com.sludge.core.protocol.RpcResponseMessage;
import com.sludge.sample.SludgeService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        try {
            //缺少接口与实现类的绑定关系
            String interfaceName = msg.getInterfaceName();
            SludgeService sludgeService = (SludgeService) Class.forName("com.sludge.sample.SludgeImpl").newInstance();
            Method method = sludgeService.getClass().getMethod(msg.getMethod(), msg.getParameterTypes());
            Object invoke = method.invoke(sludgeService, msg.getParameterValues());
            log.info("------invoke success,return {}", invoke);
            ctx.writeAndFlush(RpcResponseMessage.builder()
                    .sequenceId(msg.getSequenceId())
                    .returnValue(invoke)
                    .build());
        } catch (Exception e) {
            ctx.writeAndFlush(RpcResponseMessage.builder()
                    .sequenceId(msg.getSequenceId())
                    .exceptionValue(e)
                    .build());
        }


    }
}
