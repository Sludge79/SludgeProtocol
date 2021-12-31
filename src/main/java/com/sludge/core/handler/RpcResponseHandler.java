package com.sludge.core.handler;

import com.sludge.core.protocol.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.info("----rpcResponse receipt{}", msg);
        try {
            Promise<Object> remove = RpcResultKeeper.RESULT_KEEPER.remove(msg.getSequenceId());
            //awake promise
            if (msg.getExceptionValue() == null) {
                remove.setSuccess(msg.getReturnValue());
            } else {
                remove.setFailure(msg.getExceptionValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
